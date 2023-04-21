package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
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
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.reusablecores.InstanceManipulation;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DoubleArrayDataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.InstanceDataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.DistanceFunction;
import weka.core.Instance;

/**

 * Calculates similarity between actors via a double[] describing a vector property

 * 

 * @author Daniel McEnnis

 */
public class SimilarityByProperty extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public SimilarityByProperty() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Similarity");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SimilarityFunction", DistanceFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, DistanceFunction.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("DestinationRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Threshold", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.75);
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
    }

    @Override
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(), ".*", false);
        Vector<Actor> tags = new Vector<Actor>();
        tags.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)));
        DistanceFunction similarity = (DistanceFunction) parameter.get("SimilarityFunction").get();
        if (!tags.isEmpty()) {
            HashMap<Actor, Double>[] actorMap = new HashMap[tags.size()];
            HashSet<Actor> actorCount = new HashSet<Actor>();
            DataVector[] dataVectorArray = new DataVector[actorMap.length];
            for (int i = 0; i < tags.size(); ++i) {
                Property source = tags.get(i).getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));
                dataVectorArray[i] = getDataVector(source);
            }
            for (int i = 0; i < dataVectorArray.length; ++i) {
                dataVectorArray[i].setSize(actorCount.size());
            }
            for (int i = 0; i < dataVectorArray.length; ++i) {
                for (int j = 0; j < dataVectorArray.length; ++j) {
                    if (i != j) {
                        double sim = similarity.distance(dataVectorArray[i], dataVectorArray[j]);
                        if (sim >= ((Double) parameter.get("Threshold").get()).doubleValue()) {
                            Link simLink = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationRelation").get()));
                            simLink.set(tags.get(i), sim, tags.get(j));
                            g.add(simLink);
                        }
                    }
                }
            }
        } else {
            Logger.getLogger(SimilarityByLink.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter.get("Mode").get() + "' are present");
        }
    }

    protected DataVector getDataVector(Property type) {
        if (type.getPropertyClass().getName().contentEquals(DoubleArrayDataVector.class.getName())) {
            return (DataVector) type.getValue().get(0);
        }
        if (type.getPropertyClass().getName().contentEquals(Instance.class.getName())) {
            return new InstanceDataVector((Instance) type.getValue().get(0));
        }
        if (type.getPropertyClass().getName().contentEquals(InstanceDataVector.class.getName())) {
            return (InstanceDataVector) type.getValue().get(0);
        }
        if (type.getPropertyClass().getName().contentEquals("[D")) {
            return new DoubleArrayDataVector((double[]) type.getValue().get(0));
        } else {
            LinkedList<Instance> inst = InstanceManipulation.propertyToInstance(type);
            return new InstanceDataVector(inst.get(0));
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

     * <li/><b>name</b>: Name of this algorithm. Default is 'Simlarity By Property'

     * <li/><b>actorType</b>: mode of actor to calculate similarity with. Default 

     * is 'tag'

     * <li/><b>property</b>: Name of the property containing double arrays. Default

     * is 'Tags'

     * <li/><b>distanceFunction</b>: Distance function to use. Default is 'EuclideanDistance'

     * <li/><b>threshold</b>: threshold for creating a similarity link. Default is '0.75'

     * <li/><b>relation</b>: relation for similarity. Default is 'Similarity'

     * </ul>

     * @param map parameters to be loaded - may be null

     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(Type.ACTOR, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, null, "", false);
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("SourceRelation").get(), null, null, "", (Boolean) parameter.get("SourceAppendGraphID").get());
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.LINK, (String) parameter.get("Name").get(), (String) parameter.get("DestinationRelation").get(), null, null, "", (Boolean) parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public SimilarityByProperty prototype() {
        return new SimilarityByProperty();
    }
}
