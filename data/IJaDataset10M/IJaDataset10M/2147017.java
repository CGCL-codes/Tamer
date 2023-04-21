package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.reusablecores.FindStronglyConnectedComponentsCore;
import nz.ac.waikato.mcennis.rat.reusablecores.OptimizedLinkBetweenessCore;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**

 * Class implementing NormanGirvan edge betweeness clustering.  This differs from

 * traditional edge betweeness clustering by recalculating edge-betweeness after

 * every edge is removed.

 * 

 * Girvan, M. and M. Newman. 2002. "Community structure in social and biological networks."

 * Proceedings of the National Academy of Science. 99(12):7821-6.

 * 

 * @author Daniel McEnnis

 */
public class NormanGirvanEdgeBetweenessClustering extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    Graph graph;

    OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();

    FindStronglyConnectedComponentsCore stronglyConnected = new FindStronglyConnectedComponentsCore();

    int count = 0;

    public NormanGirvanEdgeBetweenessClustering() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Norman-Girvan Edge Betweeness Clustering");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Norman-Girvan Edge Betweeness Clustering");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Clustering");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("LinkQuery", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SourceAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("GraphIDPrefix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("AddContext", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }

    public void execute(Graph g) {
        count = 0;
        stronglyConnected.setGraphPrefix((String) parameter.get("GraphIDPrefix").get());
        fireChange(Scheduler.SET_ALGORITHM_COUNT, g.getActorCount((String) parameter.get("Mode").get()));
        splitGraph(g, (String) parameter.get("GraphIDPrefix").get());
    }

    protected void splitGraph(Graph base, String prefix) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(), ".*", false);
        try {
            Duples<Double, Link>[] orderedBetweeness;
            HashSet<Actor> actorSet = new HashSet<Actor>();
            Iterator<Actor> actors = AlgorithmMacros.filterActor(parameter, base, mode, null, null);
            if ((actors.hasNext())) {
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, count++);
                while (actors.hasNext()) {
                    actorSet.add(actors.next());
                }
                if (actorSet.size() > 1) {
                    Graph rootGraph = getGraph(base, actorSet, prefix);
                    stronglyConnected.setGraphPrefix(prefix);
                    stronglyConnected.execute(rootGraph);
                    Graph[] components = stronglyConnected.getGraph();
                    if (components != null) {
                        for (int i = 0; i < components.length; ++i) {
                            orderedBetweeness = null;
                            splitGraph(components[i], prefix + i);
                            base.addChild(components[i]);
                        }
                    } else {
                        while (stronglyConnected.getGraph() == null) {
                            LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
                            relation.buildQuery((String) parameter.get("Relation").get(), false);
                            Collection<Link> links = AlgorithmMacros.filterLink(parameter, base, relation.execute(rootGraph, null, null, null));
                            if (links.size() == 0) {
                                orderedBetweeness = new Duples[] {};
                            } else {
                                orderedBetweeness = new Duples[links.size()];
                            }
                            for (int i = 0; i < orderedBetweeness.length; ++i) {
                                orderedBetweeness[i] = new Duples<Double, Link>();
                            }
                            calculateBetweeness(rootGraph, orderedBetweeness);
                            rootGraph.remove(orderedBetweeness[orderedBetweeness.length - 1].getRight());
                            stronglyConnected.setGraphPrefix(prefix);
                            stronglyConnected.execute(rootGraph);
                        }
                        orderedBetweeness = null;
                        components = stronglyConnected.getGraph();
                        for (int i = 0; i < components.length; ++i) {
                            splitGraph(components[i], prefix + i);
                            base.addChild(stronglyConnected.getGraph()[i]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void calculateBetweeness(Graph root, Duples<Double, Link>[] orderedBetweeness) {
        core.execute(root);
        Map<Link, Double> linkMap = core.getLinkMap();
        LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String) parameter.get("Relation").get(), false);
        Collection<Link> links = AlgorithmMacros.filterLink(parameter, root, relation.execute(root, null, null, null));
        if (links.size() > 0) {
            Iterator<Link> it = links.iterator();
            while (it.hasNext()) {
                Link l = it.next();
                if (!linkMap.containsKey(l)) {
                    linkMap.put(l, 0.0);
                }
            }
        }
        Iterator<Link> it = linkMap.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Link l = it.next();
            orderedBetweeness[count].setLeft(linkMap.get(l));
            orderedBetweeness[count].setRight(l);
            count++;
        }
        java.util.Arrays.sort(orderedBetweeness);
    }

    protected Graph getGraph(Graph g, HashSet<Actor> component, String prefix) {
        try {
            Graph graph = GraphFactory.newInstance().create(prefix, parameter);
            Iterator<Actor> it = component.iterator();
            while (it.hasNext()) {
                graph.add(it.next());
            }
            LinkQuery query = (LinkQuery) parameter.get("LinkQuery").get();
            Iterator<Link> link = query.executeIterator(g, component, component, null);
            while (link.hasNext()) {
                graph.add(link.next());
            }
            if ((Boolean) parameter.get("AddContext").get()) {
                HashSet<Actor> actorSet = new HashSet<Actor>();
                actorSet.addAll(graph.getActor());
                link = query.executeIterator(g, actorSet, null, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    Actor d = l.getDestination();
                    if (graph.getActor(d.getMode(), d.getID()) == null) {
                        graph.add(d);
                    }
                    if (graph.getLink(l.getRelation(), l.getSource(), l.getDestination()) == null) {
                        graph.add(l);
                    }
                }
                link = query.executeIterator(g, null, actorSet, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    Actor d = l.getSource();
                    if (graph.getActor(d.getMode(), d.getID()) == null) {
                        graph.add(d);
                    }
                    if (graph.getLink(l.getRelation(), l.getSource(), l.getDestination()) == null) {
                        graph.add(l);
                    }
                }
            }
            return graph;
        } catch (Exception ex) {
            Logger.getLogger(FindWeaklyConnectedComponents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<IODescriptor> getInputType() {
        return input;
    }

    @Override
    public List<IODescriptor> getOutputType() {
        return output;
    }

    @Override
    public Properties getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        return parameter.get(param);
    }

    /**

     * Parameters to be initialized.  Subclasses should override if they provide

     * any additional parameters or require additional inputs.

     * 

     * <ol>

     * <li>'name' - Name of this instance of the algorithm.  Default is ''.

     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.

     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.

     * <li>'normalize' - boolean for whether or not to normalize prestige vectors. 

     * Default 'false'.

     * </ol>

     * <br>

     * <br>Input 0 - Link

     * <br>NOTE - subclasses define the ouput - see subclasses for output information

     * 

     * @param map parameters to be loaded - may be null.

     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(Type.ACTOR, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, null, "", (Boolean) parameter.get("SourceAppendGraphID").get());
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.LINK, (String) parameter.get("Name").get(), (String) parameter.get("Relation").get(), null, null, "", false);
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.GRAPH, (String) parameter.get("Name").get(), (String) parameter.get("GraphIDPrefix").get(), null, null, "", true);
            output.add(desc);
        }
    }

    public NormanGirvanEdgeBetweenessClustering prototype() {
        return new NormanGirvanEdgeBetweenessClustering();
    }
}
