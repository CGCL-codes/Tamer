package nz.ac.waikato.mcennis.rat.reusablecores;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.path.PathNode;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;

/**

 * Core abstract class for algorithm cores that utilize shortest-path info

 * in their algorithms.  It creates shortest-path using Djikstra's algorithm

 * for each source actor, allows performing of caluclations then, and allows 

 * algorithms to perform additional calculatio afterwards.

 * 

 * @author Daniel McEnnis

 */
public abstract class PathBaseCore {

    HashMap<Actor, Integer> actorToID = new HashMap<Actor, Integer>();

    PathNode[] path = null;

    String relation = "Knows";

    String mode = "User";

    Properties parameter = PropertiesFactory.newInstance().create();

    /**

     * Generates a spanning tree for a given actor.  Calls the abstract methods:

     * <br>setSize - gives the derived class the size of the user list.

     * <br>doAnalysis - gives a set of PathNodes and the root of the tree.

     * <br>doCleanup - allows any extra calculations to be made before the algorithm

     * returns control to the scheduler.

     *

     */
    public void execute(Graph g) {
        HashSet<PathNode> lastSet = new HashSet<PathNode>();
        HashSet<PathNode> nextSet = new HashSet<PathNode>();
        HashSet<PathNode> seen = new HashSet<PathNode>();
        LinkedList<Actor> sortActor = new LinkedList<Actor>();
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(), ".*", false);
        sortActor.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)));
        Collections.sort(sortActor);
        int count = 0;
        Iterator<Actor> a = sortActor.iterator();
        while (a.hasNext()) {
            actorToID.put(a.next(), count++);
        }
        path = new PathNode[sortActor.size()];
        a = sortActor.iterator();
        count = 0;
        while (a.hasNext()) {
            path[count] = new PathNode();
            path[count].setId(count);
            path[count].setActor(a.next());
            path[count].setCost(Double.POSITIVE_INFINITY);
            count++;
        }
        sortActor = null;
        setSize(path.length);
        for (int i = 0; i < path.length; ++i) {
            Logger.getLogger(PathBaseCore.class.getName()).log(Level.FINE, "Creating paths from source " + i);
            lastSet.clear();
            nextSet.clear();
            seen.clear();
            lastSet.add(path[i]);
            seen.add(path[i]);
            for (int j = 0; j < path.length; ++j) {
                path[j].setCost(Double.POSITIVE_INFINITY);
                path[j].setPrevious(null);
            }
            path[i].setCost(0.0);
            LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
            relation.buildQuery((String) parameter.get("Relation").get(), false);
            while (lastSet.size() > 0) {
                Iterator<PathNode> last_it = lastSet.iterator();
                while (last_it.hasNext()) {
                    PathNode currentSource = last_it.next();
                    LinkedList<Actor> actor = new LinkedList<Actor>();
                    actor.add(currentSource.getActor());
                    Iterator<Link> linkSet = AlgorithmMacros.filterLink(parameter, g, relation, actor, null, null);
                    while (linkSet.hasNext()) {
                        Link link = linkSet.next();
                        PathNode dest = path[actorToID.get(link.getDestination())];
                        compare(currentSource, dest, link);
                        if (!seen.contains(dest)) {
                            nextSet.add(dest);
                        }
                        seen.add(dest);
                    }
                }
                lastSet = nextSet;
                nextSet = new HashSet<PathNode>();
            }
            doAnalysis(path, path[i]);
        }
        doCleanup(path, g);
    }

    protected void compare(PathNode current, PathNode next, Link link) {
        double totalCost = current.getCost() + link.getStrength();
        if (next.getCost() > totalCost) {
            next.setPrevious(current);
            next.setPreviousLink(link);
            next.setCost(totalCost);
        }
    }

    /**

     * Any additional calculations to be performed before control returns to the

     * scheduler

     * @param path 

     * @param g 

     */
    protected abstract void doCleanup(PathNode[] path, Graph g);

    /**

     * 

     * @param path 

     * @param source 

     */
    protected abstract void doAnalysis(PathNode[] path, PathNode source);

    /**

     * 

     * @param size 

     */
    protected abstract void setSize(int size);

    /**

     * Set which relation of links to use in path calculations

     * 

     * @param r relation of links to utilize

     */
    public void setRelation(String r) {
        relation = r;
        parameter.set("Relation", relation);
    }

    /**

     * Returns which relation of links to use in path calculations

     * 

     * @return relation of links utilized

     */
    public String getRelation() {
        if (parameter.get("Relation").getValue().size() > 0) {
            return (String) parameter.get("Relation").get();
        }
        return relation;
    }

    /**

     * Set the mode of actors to use for calculations

     * @param m mode of actors to utilize

     */
    public void setMode(String m) {
        parameter.set("Mode", m);
        mode = m;
    }

    /**

     * Return which mode of actors are to be used for shortest path calculations

     * 

     * @return mode of actors to use

     */
    public String getMode() {
        if (parameter.get("Mode").getValue().size() > 0) {
            return (String) parameter.get("Mode").get();
        }
        return mode;
    }

    public Properties getProperties() {
        return parameter;
    }

    public void setProperties(Properties props) {
        parameter = props;
    }
}
