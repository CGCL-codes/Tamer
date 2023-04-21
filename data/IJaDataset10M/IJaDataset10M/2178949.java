package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.reusablecores.InstanceManipulation;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.AggregatorFunction;
import weka.core.Instance;

/**
 *
 */
public class AggregateGraphToGraph extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateGraphToGraph() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Graph");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Graph");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregator");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SourceAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("InnerFunction", AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("FirstItem");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("OuterFunction", AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("Sum");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("ActorProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("GraphProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode query = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        query.buildQuery((String) parameter.get("Mode").get(), ".*", false);
        Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter, g, query, null, null);
        AggregatorFunction innerAggregate = (AggregatorFunction) parameter.get("InnerFunction").get();
        AggregatorFunction outerAggregate = (AggregatorFunction) parameter.get("OuterFunction").get();
        if ((innerAggregate != null) && (outerAggregate != null)) {
            if (actor != null) {
                LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
                while (actor.hasNext()) {
                    Property properties = actor.next().getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("ActorProperty").get()));
                    if (properties != null) {
                        LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(properties);
                        double[] weight = new double[actorProperty.size()];
                        Arrays.fill(weight, 1.0);
                        Instance[] toBeAdded = innerAggregate.aggregate(actorProperty.toArray(new Instance[] {}), weight);
                        for (int k = 0; k < toBeAdded.length; ++k) {
                            instanceFromProperty.add(toBeAdded[k]);
                        }
                    }
                }
                Instance[] result = new Instance[] {};
                if (instanceFromProperty.size() > 0) {
                    double[] weights = new double[instanceFromProperty.size()];
                    Arrays.fill(weights, 1.0);
                    result = outerAggregate.aggregate(instanceFromProperty.toArray(new Instance[] {}), weights);
                }
                Property aggregator = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("GraphProperty").get()), weka.core.Instance.class);
                for (int j = 0; j < result.length; ++j) {
                    try {
                        aggregator.add(result[j]);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                g.add(aggregator);
            } else {
                Logger.getLogger(AggregateActorToGraph.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter.get("Mode").get() + "' are present");
            }
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateActorToGraph.class.getName()).log(Level.SEVERE, "Inner Aggregator Function does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateActorToGraph.class.getName()).log(Level.SEVERE, "Outer Aggregator Function does not exist");
            }
        }
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
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate By Graph'
     * <li/><b>actorType</b>: Mode of the actor to aggregate over. Default is 'tag'
     * <li/><b>innerAggregatorFunction</b>: aggregator function to use over values inside
     * an actor property. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'
     * <li/><b>outerAggregatorFunction</b>: aggregator function to use over all
     * actors. Deafult is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator'
     * <li/><b>actorProperty</b>: ID of the actor property to aggregate across.  Default is 'actorProperty'
     * <li/><b>graphProperty</b>: ID of the graph property to create. By default, it is
     * the value of 'outerAggregatorFunction' property concatenated with a space and the value
     * of 'actorProperty' property.
     * </ul>
     * @param map parameters to be loaded - may be null
     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, (String) parameter.get("ActorProperty").get(), "");
            if ((Boolean) parameter.get("SourceAppendGraphID").get()) {
                desc.setAppendGraphID(true);
            }
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, (String) parameter.get("GraphProperty").get(), "");
            if ((Boolean) parameter.get("DestinationAppendGraphID").get()) {
                desc.setAppendGraphID(true);
            }
            output.add(desc);
        }
    }

    public AggregateGraphToGraph prototype() {
        return new AggregateGraphToGraph();
    }
}
