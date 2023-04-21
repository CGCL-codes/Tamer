package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**

 * Utilizes Given(user->artist) as ground truth and Derived(user->artist) as predictions.

 * Calcualtes the Pearsons Correlation between two sets of recommendation with

 * correlations made only if links are present in both sets.

 * 

 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 

 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53

 *

 * @author Daniel McEnnis

 * 

 */
public class PearsonCorrelation extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of Evaluation */
    public PearsonCorrelation() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
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
        name = ParameterFactory.newInstance().create("Direction", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);
        name = ParameterFactory.newInstance().create("SourceRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
        name = ParameterFactory.newInstance().create("DerivedRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
    }

    /**

     * Calculate all the evaluation metrics against the given graph.

     * 

     * TODO: add F-measure stastic

     * 

     */
    public void execute(Graph g) {
        fireChange(Scheduler.SET_ALGORITHM_COUNT, 1);
        pearsonsCorrelation(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);
    }

    protected void pearsonsCorrelation(Graph g) {
        try {
            ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
            mode.buildQuery((String) parameter.get("Mode").get(), ".*", false);
            LinkedList<LinkQuery> derived = new LinkedList<LinkQuery>();
            LinkByRelation derivedRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
            derivedRelation.buildQuery(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("DerivedRelation").get()), false);
            derived.add(derivedRelation);
            LinkByActor derivedActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
            LinkEnd end = (LinkEnd) parameter.get("LinkEnd").get();
            if (end == LinkEnd.SOURCE) {
                derivedActor.buildQuery(false, mode, null, null);
            } else {
                derivedActor.buildQuery(false, null, mode, null);
            }
            derived.add(derivedActor);
            if (!parameter.get("LinkFilter").getValue().isEmpty()) {
                derived.add((LinkQuery) parameter.get("LinkFilter").get());
            }
            AndLinkQuery derivedAnd = (AndLinkQuery) LinkQueryFactory.newInstance().create("AndLinkQuery");
            derivedAnd.buildQuery(derived);
            LinkedList<LinkQuery> source = new LinkedList<LinkQuery>();
            LinkByRelation sourceRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
            sourceRelation.buildQuery((String) parameter.get("SourceRelation").get(), false);
            source.add(sourceRelation);
            LinkByActor sourceActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
            if (end == LinkEnd.SOURCE) {
                sourceActor.buildQuery(false, mode, null, null);
            } else {
                sourceActor.buildQuery(false, null, mode, null);
            }
            source.add(sourceActor);
            if (!parameter.get("LinkFilter").getValue().isEmpty()) {
                source.add((LinkQuery) parameter.get("LinkFilter").get());
            }
            AndLinkQuery sourceAnd = (AndLinkQuery) LinkQueryFactory.newInstance().create("AndLinkQuery");
            sourceAnd.buildQuery(source);
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double correlationSquaredSum = 0.0;
            double correlationSum = 0.0;
            int n = 0;
            while (it.hasNext()) {
                ++n;
                LinkedList<Actor> s = new LinkedList<Actor>();
                LinkedList<Actor> r = new LinkedList<Actor>();
                try {
                    Actor actor = it.next();
                    s.add(actor);
                    Iterator<Link> gLink = null;
                    if (end == LinkEnd.SOURCE) {
                        gLink = sourceAnd.executeIterator(g, s, null, null);
                    } else {
                        gLink = sourceAnd.executeIterator(g, null, s, null);
                    }
                    double givenMean = 0.0;
                    double givenSD = 0.0;
                    double derivedMean = 0.0;
                    double derivedSD = 0.0;
                    double sum = 0.0;
                    double squaredSum = 0.0;
                    int gLinkSize = 0;
                    while (gLink.hasNext()) {
                        Link l = gLink.next();
                        if (l.getStrength() > 0.0) {
                            sum += l.getStrength();
                            squaredSum += l.getStrength() * l.getStrength();
                            gLinkSize++;
                        }
                    }
                    if (gLinkSize > 0) {
                        givenMean = sum / gLinkSize;
                        givenSD = ((gLinkSize * squaredSum) - sum * sum) / gLinkSize;
                        givenSD += 1e-10;
                    }
                    double correlation = 0.0;
                    sum = 0.0;
                    squaredSum = 0.0;
                    if (end == LinkEnd.SOURCE) {
                        gLink = sourceAnd.executeIterator(g, s, null, null);
                    } else {
                        gLink = sourceAnd.executeIterator(g, null, s, null);
                    }
                    while (gLink.hasNext()) {
                        Iterator<Link> derivedIt = null;
                        if (end == LinkEnd.SOURCE) {
                            r.add(gLink.next().getDestination());
                            derivedIt = derivedAnd.executeIterator(g, s, r, null);
                        } else {
                            r.add(gLink.next().getSource());
                            derivedIt = derivedAnd.executeIterator(g, r, s, null);
                        }
                        if (derivedIt.hasNext()) {
                            Link l = derivedIt.next();
                            sum += l.getStrength();
                            squaredSum += l.getStrength() * l.getStrength();
                        }
                        r.clear();
                    }
                    if (gLinkSize > 0) {
                        derivedMean = sum / gLinkSize;
                        derivedSD = ((gLinkSize * squaredSum) - sum * sum) / gLinkSize;
                    } else {
                        derivedMean = 0.0;
                        derivedSD = 0.0;
                    }
                    derivedSD += 1e-10;
                    if (end == LinkEnd.SOURCE) {
                        gLink = sourceAnd.executeIterator(g, s, null, null);
                    } else {
                        gLink = sourceAnd.executeIterator(g, null, s, null);
                    }
                    while (gLink.hasNext()) {
                        Link gl = gLink.next();
                        Iterator<Link> derivedIt = null;
                        if (end == LinkEnd.SOURCE) {
                            r.add(gl.getDestination());
                            derivedIt = derivedAnd.executeIterator(g, s, r, null);
                        } else {
                            r.add(gl.getSource());
                            derivedIt = derivedAnd.executeIterator(g, r, s, null);
                        }
                        if (derivedIt.hasNext()) {
                            Link l = derivedIt.next();
                            correlation += (l.getStrength() - derivedMean) * (gl.getStrength() - givenMean);
                        }
                        r.clear();
                    }
                    correlation += 1e-20;
                    if (gLinkSize > 0) {
                        correlation /= (gLinkSize * derivedSD * givenSD);
                    } else {
                        correlation = 1.0;
                    }
                    Property precisionProperty = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()), Double.class);
                    precisionProperty.add(new Double(correlation));
                    actor.add(precisionProperty);
                    correlationSum += correlation;
                    correlationSquaredSum += correlation * correlation;
                    source.clear();
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            double sd = ((n * correlationSquaredSum) - correlationSum * correlationSum) / n;
            double mean = correlationSum / n;
            Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get() + " Standard Deviation"), Double.class);
            property.add(new Double(sd));
            g.add(property);
            property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get() + " Mean"), Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(PearsonCorrelation.class.getName()).log(Level.INFO, "Pearsons Correlation\t" + mean);
            Logger.getLogger(PearsonCorrelation.class.getName()).log(Level.INFO, "Pearsons Correlation SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PearsonCorrelation.class.getName()).log(Level.SEVERE, null, ex);
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

     * Parameters to be initialized

     * 

     * <ol>

     * <li>'name' - name of this instance of this algorithm. Deafult 'Evaluation'

     * <li>'relationGroundTruth' - type (relation) of link that describes ground

     * truth relations between two actor types (modes). Default 'Given'

     * <li>'relationDerived' - type (relation) of link that describes calculated

     * references to be evaluated between two modes. Deafult 'Derived'.

     * <li>'propertyPrefix' - prefix for the property names generated. Default 

     * 'Evaluation '.

     * </ol>

     * <br>

     * <br>Input 1 - Link

     * <br>Input 2 - Link

     * <br>Output 1-12 - Graph Property

     * 

     * 

     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(Type.ACTOR, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, null, "", false);
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.LINK, (String) parameter.get("Name").get(), (String) parameter.get("SourceRelation").get(), null, null, "", false);
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.LINK, (String) parameter.get("Name").get(), (String) parameter.get("DerivedRelation").get(), null, null, "", (Boolean) parameter.get("SourceAppendGraphID").get());
            input.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) parameter.get("Name").get(), (String) parameter.get("Mode").get(), null, (String) parameter.get("DestinationProperty").get(), "", (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.GRAPH_PROPERTY, (String) parameter.get("Name").get(), null, null, (String) parameter.get("DestinationProperty").get() + " Standard Deviation", "", (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.GRAPH_PROPERTY, (String) parameter.get("Name").get(), null, null, (String) parameter.get("DestinationProperty").get() + " Mean", "", (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);
        }
    }

    public PearsonCorrelation prototype() {
        return new PearsonCorrelation();
    }
}
