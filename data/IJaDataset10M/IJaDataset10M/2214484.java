package org.speech.asr.recognition.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.linguist.SearchGraphBuilder;
import org.speech.asr.recognition.math.MathUtils;
import javax.annotation.PostConstruct;
import java.util.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ViterbiSearchManager implements SearchManager {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(ViterbiSearchManager.class.getName());

    protected List<Token> activeList;

    protected SearchNode startNode;

    protected SearchNode endNode;

    protected SearchGraphBuilder searchGraphBuilder;

    protected int beamWidth;

    protected Feature currentFeature;

    @PostConstruct
    public void init() {
        log.info("Building search graph...");
        SearchGraphSegment graph = searchGraphBuilder.createSearchGraph();
        startNode = graph.getInitialNode();
        endNode = graph.getFinalNode();
        activeList = new LinkedList();
        dumpGraphConnections();
    }

    private void dumpGraphConnections() {
        log.debug("Dumping search graph...");
        traverse("", startNode);
    }

    private void traverse(String path, SearchNode activeNode) {
        String newPath = path + activeNode.getNodeId() + " (" + activeNode.getSuccessors().size() + ") ; ";
        if (activeNode.getNodeId().equals(endNode.getNodeId())) {
            log.debug("Found path: {}", newPath);
        } else {
            for (SearchArc arc : activeNode.getSuccessors()) {
                if (arc.getNextNode() != activeNode) {
                    traverse(newPath, arc.getNextNode());
                }
            }
        }
    }

    public List<Token> getActiveList() {
        return activeList;
    }

    public void start() {
        log.trace("Starting recognition...");
        activeList.clear();
        Token firstToken = new TokenImpl(startNode, 0.0);
        activeList.add(firstToken);
    }

    public void stop() {
        log.trace("Expanding paths for active list");
        assert !activeList.isEmpty();
        List<Token> oldActiveList = activeList;
        activeList = new LinkedList();
        for (Token token : oldActiveList) {
            expandTokenForEnd(token);
        }
    }

    protected void expandTokenForEnd(Token token) {
        Stack<Token> stack = new Stack();
        stack.push(token);
        while (!stack.isEmpty()) {
            Token activeToken = stack.pop();
            SearchNode activeNode = activeToken.getActiveNode();
            if (activeNode == endNode) {
                activeList.add(activeToken);
            } else {
                for (SearchArc arc : activeNode.getSuccessors()) {
                    double transitionScore = arc.getTransitionScore();
                    SearchNode nextNode = arc.getNextNode();
                    if (!nextNode.isEmitting() && activeNode != nextNode) {
                        double newScore = MathUtils.sum(activeToken.getScore(), transitionScore);
                        Token newToken = new TokenImpl(activeToken, nextNode, newScore);
                        if (nextNode.getNodeId().equals(endNode.getNodeId())) {
                            activeList.add(newToken);
                        } else {
                            stack.push(newToken);
                        }
                    }
                }
            }
        }
    }

    public void feed(Feature feature) {
        log.trace("Decoding frame {}", feature);
        currentFeature = feature;
        expandPaths();
        prune();
    }

    protected void expandPaths() {
        log.trace("Expanding paths for active list");
        assert !activeList.isEmpty();
        List<Token> oldActiveList = activeList;
        activeList = new LinkedList();
        for (Token token : oldActiveList) {
            expandToken(token);
        }
    }

    protected void expandToken(Token token) {
        Stack<Token> stack = new Stack();
        stack.push(token);
        while (!stack.isEmpty()) {
            Token activeToken = stack.pop();
            SearchNode activeNode = activeToken.getActiveNode();
            for (SearchArc arc : activeNode.getSuccessors()) {
                double transitionScore = arc.getTransitionScore();
                SearchNode nextNode = arc.getNextNode();
                if (nextNode.isEmitting()) {
                    double featureScore = nextNode.score(currentFeature);
                    double newScore = MathUtils.sum(activeToken.getScore(), transitionScore, featureScore);
                    FrameAlignment fa = new FrameAlignment(nextNode.getNodeId(), currentFeature.getSequenceNumber());
                    Token newToken = new TokenImpl(activeToken, nextNode, newScore, fa);
                    activeList.add(newToken);
                } else {
                    double newScore = MathUtils.sum(activeToken.getScore(), transitionScore);
                    Token newToken = new TokenImpl(activeToken, nextNode, newScore);
                    stack.push(newToken);
                }
            }
        }
    }

    protected void prune() {
        log.trace("Pruning tokens in active list");
        removeEqualsPaths();
        Collections.sort(activeList, new TokenComparator());
        int toRemove = activeList.size() - beamWidth;
        if (toRemove > 0) {
            log.trace("Removing {} tokens from active list", toRemove);
            for (int i = 0; i < toRemove; i++) {
                activeList.remove(0);
            }
        }
    }

    protected void removeEqualsPaths() {
        log.trace("Removing equals path, active list before removing {}", activeList.size());
        List<Token> oldActiveList = activeList;
        activeList = new LinkedList();
        for (Token token : oldActiveList) {
            Token toCompare = findSamePath(token, activeList);
            if (toCompare != null) {
                if (token.getScore() > toCompare.getScore()) {
                    activeList.remove(toCompare);
                    activeList.add(token);
                }
            } else {
                activeList.add(token);
            }
        }
        log.trace("New active list after removing {}", activeList.size());
    }

    protected Token findSamePath(Token token, List<Token> list) {
        for (Token toCompare : list) {
            if (comparePaths(token.getNodeTrace(), toCompare.getNodeTrace())) {
                return toCompare;
            }
        }
        return null;
    }

    protected boolean comparePaths(List<SearchNode> path1, List<SearchNode> path2) {
        if (path1.size() != path2.size()) {
            return false;
        }
        SearchNode last1 = path1.get(path1.size() - 1);
        SearchNode last2 = path2.get(path1.size() - 1);
        return last1 == last2;
    }

    /**
   * Setter for property 'searchGraphBuilder'.
   *
   * @param searchGraphBuilder Value to set for property 'searchGraphBuilder'.
   */
    public void setSearchGraphBuilder(SearchGraphBuilder searchGraphBuilder) {
        this.searchGraphBuilder = searchGraphBuilder;
    }

    /**
   * Setter for property 'beamWidth'.
   *
   * @param beamWidth Value to set for property 'beamWidth'.
   */
    public void setBeamWidth(int beamWidth) {
        this.beamWidth = beamWidth;
    }

    private class TokenComparator implements Comparator<Token> {

        public int compare(Token o1, Token o2) {
            assert MathUtils.isReal(o1.getScore()) : "Value " + o1.getScore();
            assert MathUtils.isReal(o2.getScore()) : "Value " + o2.getScore();
            if (o1.getScore() == o2.getScore()) {
                return 0;
            }
            if (o1.getScore() < o2.getScore()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
