package cn.edu.thss.iise.beehivez.server.spsd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Token;
import org.processmining.framework.models.petrinet.Transition;

public class SimpleParallelStructure {

    private Transition forkNode = null;

    private Transition joinNode = null;

    private TokenTree branches = null;

    private String str = null;

    public void setFork(Transition node) {
        forkNode = node;
    }

    public void setJoin(Transition node) {
        joinNode = node;
    }

    public void setBranches(TokenTree tt) {
        branches = tt;
    }

    public Transition getFork() {
        return forkNode;
    }

    public Transition getJoin() {
        return joinNode;
    }

    private void removeTokenFromForkSuccessor() {
        if (forkNode == null) return;
        Iterator itSuccessor = forkNode.getSuccessors().iterator();
        while (itSuccessor.hasNext()) {
            Place cur = (Place) itSuccessor.next();
            Token t = cur.getRandomAvailableToken();
            cur.removeToken(t);
        }
    }

    private void addTokenToJoinPredecessor() {
        if (joinNode == null) return;
        Iterator itPredecessor = joinNode.getPredecessors().iterator();
        while (itPredecessor.hasNext()) {
            Place cur = (Place) itPredecessor.next();
            cur.addToken(new Token());
        }
    }

    private void removeTokenFromJoinPredecessor() {
        if (joinNode == null) return;
        Iterator itPredecessor = joinNode.getPredecessors().iterator();
        while (itPredecessor.hasNext()) {
            Place cur = (Place) itPredecessor.next();
            Token t = cur.getRandomAvailableToken();
            cur.removeToken(t);
        }
    }

    private void addTokenToForkSuccessor() {
        if (forkNode == null) return;
        Iterator itSuccessor = forkNode.getSuccessors().iterator();
        while (itSuccessor.hasNext()) {
            Place cur = (Place) itSuccessor.next();
            cur.addToken(new Token());
        }
    }

    public void fireMe() {
        removeTokenFromForkSuccessor();
        addTokenToJoinPredecessor();
    }

    public void unfireMe() {
        removeTokenFromJoinPredecessor();
        addTokenToForkSuccessor();
    }

    @Override
    public String toString() {
        if (str != null) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        for (TokenTree sequence : branches.getChildren()) {
            sb.append(sequence.getSequence());
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public Set<Transition> getAllTransitions() {
        if (forkNode == null || joinNode == null) return null;
        Set<Transition> result = new HashSet<Transition>();
        result.add(joinNode);
        Vector<TokenTree> children = branches.getChildren();
        Iterator<TokenTree> it = children.iterator();
        while (it.hasNext()) {
            TokenTree tt = it.next();
            result.addAll(tt.getTransitions());
        }
        return result;
    }

    public Set<Place> getAllPlaces() {
        if (forkNode == null || joinNode == null) return null;
        Set<Place> result = new HashSet<Place>();
        Vector<TokenTree> children = branches.getChildren();
        Iterator<TokenTree> it = children.iterator();
        while (it.hasNext()) {
            TokenTree tt = it.next();
            result.addAll(tt.getPlaces());
        }
        return result;
    }

    public TokenTree getBranches() {
        return branches;
    }
}
