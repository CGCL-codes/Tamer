package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import weka.clusterers.Clusterer;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;

/**

 * Utilize an arbitrary probabilistic-clusterer (vector of probable membership

 * in a cluster).  Which clusterer used is determined by parameter.

 * 

 * @author Daniel McEnnis

 */
public class WekaProbablisticClusterer extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public WekaProbablisticClusterer() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Multi-Attribute");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Multi-Attribute");
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
        name = ParameterFactory.newInstance().create("Clusterer", Class.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Class.class);
        name.setRestrictions(syntax);
        name.add(weka.classifiers.meta.AdaBoostM1.class);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Options", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("GroundMode").get(), ".*", false);
        try {
            Clusterer clusterer = (Clusterer) ((Class) parameter.get("Clusterer").get()).newInstance();
            String[] options = ((String) parameter.get("Options").get()).split("\\s+");
            ((OptionHandler) clusterer).setOptions(options);
            Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            Instances dataSet = null;
            while (actor.hasNext()) {
                Actor a = actor.next();
                Property property = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));
                if (!property.getValue().isEmpty()) {
                    Instance value = (Instance) property.getValue().get(0);
                    if ((dataSet == null) && (value.dataset() != null)) {
                        FastVector attributes = new FastVector();
                        for (int i = 0; i < value.dataset().numAttributes(); ++i) {
                            attributes.addElement(value.dataset().attribute(i));
                        }
                        dataSet = new Instances("Clustering", attributes, 1000);
                    } else if ((dataSet == null)) {
                        FastVector attributes = new FastVector();
                        for (int i = 0; i < value.numAttributes(); ++i) {
                            Attribute element = new Attribute(Integer.toString(i));
                            attributes.addElement(element);
                        }
                        dataSet = new Instances("Clustering", attributes, 1000);
                    }
                    dataSet.add(value);
                }
            }
            clusterer.buildClusterer(dataSet);
            actor = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            HashMap<Integer, Graph> clusters = new HashMap<Integer, Graph>();
            while (actor.hasNext()) {
                Actor a = actor.next();
                Property property = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));
                if (!property.getValue().isEmpty()) {
                    Instance instance = (Instance) property.getValue().get(0);
                    double[] cluster = new double[] {};
                    try {
                        cluster = clusterer.distributionForInstance(instance);
                    } catch (Exception ex) {
                        Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, "ClusterInstance on clusterer failed", ex);
                    }
                    Property clusterProperty = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()), (new double[] {}).getClass());
                    clusterProperty.add(cluster);
                    a.add(clusterProperty);
                }
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);
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

     * Parameter:<br/>

     * <br/>

     * <ul>

    * <li/><b>name</b>: name of this Algorithm.  Default is 'Weka Classifier Clustering Algorithm'

     * <li/><b>class</b>: class of the clusterer. Default is 'weka.clusterers.Cobweb'

     * <li/><b>options</b>: string of options for this clusterer. Default is ''

     * <li/><b>instancesProperty</b>: ID of the graph property containing the 

     * Instances object. Deafult is 'AudioFile'

     * <li/><b>actorType</b>: mode of the actor containing the Instance property to

     * be clustered. Default is 'User'

     * <li/><b>actorProperty</b>: ID of the property where Instance objects are stored.

     * Default is 'AudioFile'

     * <li/><b>destinationProperty</b>: ID of the property to create on each actor.

     * Deafult is 'clusterID'

     * <li/><b>storeClassifier</b>: is the classifier to be stored on the graph.

     * Default is 'false'

     * <li/><b>classifierProperty</b>: property ID for storing the built classifier.

     * Deafult is 'clusterer'

      * </ul>

     * @param map parameters to be loaded - may be null.

     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, (String) parameter.get("SourceProperty").get(), "", (Boolean) parameter.get("SourceAppendGraphID").get());
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, (String) parameter.get("DestinationProperty").get(), "", (Boolean) parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.GRAPH, (String) parameter.get("Name").get(), (String) parameter.get("GraphIDPrefix").get(), null, null, "", true);
            output.add(desc);
        }
    }

    public WekaProbablisticClusterer prototype() {
        return new WekaProbablisticClusterer();
    }
}
