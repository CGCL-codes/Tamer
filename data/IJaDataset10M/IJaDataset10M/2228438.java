package weka.ptolemy.actor;

import ptolemy.actor.TypedIOPort;
import ptolemy.actor.lib.Transformer;
import ptolemy.data.ArrayToken;
import ptolemy.data.BooleanToken;
import ptolemy.data.DoubleToken;
import ptolemy.data.ObjectToken;
import ptolemy.data.StringToken;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.NamedObj;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.ptolemy.WekaHelper;
import weka.ptolemy.data.expr.GoeParameter;

/**
 * Simple Actor for a Weka classifier.
 * 
 * @author fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 10 $
 */
public class IncrementalClassifier extends Transformer {

    /** for serialization. */
    private static final long serialVersionUID = 5934491602499473565L;

    /** The classifier string. */
    public GoeParameter classifierSetup;

    /** the actual classifier. */
    protected weka.classifiers.Classifier _classifier;

    /** the "classifyInstance" input port. */
    public TypedIOPort classifyInstance;

    /** the "built" output port. */
    public TypedIOPort built;

    /** the "classification" output port. */
    public TypedIOPort classification;

    /** the "distribution" output port. */
    public TypedIOPort distribution;

    /**
   * Constructor.
   * 
   * @param container 	The container.
   * @param name 	The name of this actor.
   */
    public IncrementalClassifier(CompositeEntity container, String name) throws NameDuplicationException, IllegalActionException {
        super(container, name);
        input.setTypeEquals(BaseType.OBJECT);
        input.setName("update");
        classifyInstance = new TypedIOPort(this, "classifyInstance", true, false);
        classifyInstance.setTypeEquals(BaseType.OBJECT);
        output.setTypeEquals(BaseType.STRING);
        output.setName("model");
        built = new TypedIOPort(this, "built", false, true);
        built.setTypeEquals(BaseType.OBJECT);
        classification = new TypedIOPort(this, "classification", false, true);
        classification.setTypeEquals(BaseType.DOUBLE);
        distribution = new TypedIOPort(this, "distribution", false, true);
        classification.setTypeEquals(BaseType.ARRAY_BOTTOM);
        classifierSetup = new GoeParameter(this, "classifier");
        new Parameter(classifierSetup, GoeParameter.ATT_CLASS, new StringToken(weka.classifiers.Classifier.class.getName()));
        new Parameter(classifierSetup, GoeParameter.ATT_DEFVALUE, new StringToken(WekaHelper.toCommandline(new weka.classifiers.bayes.NaiveBayesUpdateable())));
        new Parameter(classifierSetup, GoeParameter.ATT_CANCHANGECLASS, new BooleanToken(true));
    }

    /** 
   * Initialize this actor. Resets the classifier.
   *
   * @throws IllegalActionException 	never thrown.
   */
    public void initialize() throws IllegalActionException {
        super.initialize();
        _classifier = null;
    }

    /** 
   * Return false if the input port has no token, otherwise return
   * what the superclass returns (presumably true).
   * 
   * @throws IllegalActionException If there is no director.
   */
    public boolean prefire() throws IllegalActionException {
        if (!input.hasToken(0) && !classifyInstance.hasToken(0)) return false;
        return super.prefire();
    }

    /**
   * Performs the updating of the classifier.
   * 
   * @throws IllegalActionException	if a problem during training occurs
   */
    protected void doUpdate() throws IllegalActionException {
        Instance trainInst;
        String cmdLine;
        Parameter att;
        trainInst = (Instance) ((ObjectToken) input.get(0)).getValue();
        if (trainInst.classIndex() == -1) throw new IllegalActionException(this, "No class attribute set!");
        if (_classifier == null) {
            cmdLine = ((StringToken) classifierSetup.getToken()).stringValue();
            if (cmdLine.length() == 0) {
                att = (Parameter) ((NamedObj) classifierSetup).getAttribute(GoeParameter.ATT_DEFVALUE, Parameter.class);
                cmdLine = ((StringToken) att.getToken()).stringValue();
            }
            _classifier = (weka.classifiers.Classifier) WekaHelper.fromCommandline(cmdLine);
            if (_classifier == null) throw new IllegalActionException(this, "Failed to instantiate classifier '" + classifierSetup.getValueAsString() + "'");
            if (!(_classifier instanceof UpdateableClassifier)) throw new IllegalActionException(this, "Classifier '" + classifierSetup.getValueAsString() + "' does not implement " + UpdateableClassifier.class.getName() + "!");
            try {
                _classifier.buildClassifier(trainInst.dataset());
            } catch (Exception e) {
                throw new IllegalActionException(this, e, "Failed to build classifier '" + classifierSetup.getValueAsString() + "'");
            }
        }
        try {
            ((UpdateableClassifier) _classifier).updateClassifier(trainInst);
        } catch (Exception e) {
            throw new IllegalActionException(this, e, "Failed to update classifier '" + classifierSetup.getValueAsString() + "'");
        }
        output.broadcast(new StringToken(_classifier.toString()));
        built.broadcast(new ObjectToken(_classifier));
    }

    /**
   * Performs the classification of an Instance.
   * 
   * @throws IllegalActionException	if a problem during training occurs
   */
    protected void doClassification() throws IllegalActionException {
        Instance inst;
        double pred;
        double[] dist;
        DoubleToken[] distTokens;
        int i;
        inst = (Instance) ((ObjectToken) classifyInstance.get(0)).getValue();
        if (inst.classIndex() == -1) throw new IllegalActionException(this, "No class attribute set!");
        if (_classifier == null) throw new IllegalActionException(this, "Classifier not built!");
        pred = Instance.missingValue();
        try {
            pred = _classifier.classifyInstance(inst);
        } catch (Exception e) {
            throw new IllegalActionException(this, e, "Failed to classify instance '" + inst + "'");
        }
        classification.broadcast(new DoubleToken(pred));
        dist = null;
        try {
            dist = _classifier.distributionForInstance(inst);
        } catch (Exception e) {
            throw new IllegalActionException(this, e, "Failed to generate distribution for instance '" + inst + "'");
        }
        distTokens = new DoubleToken[dist.length];
        for (i = 0; i < distTokens.length; i++) distTokens[i] = new DoubleToken(dist[i]);
        distribution.broadcast(new ArrayToken(distTokens));
    }

    /**
   * Read exactly one token from the input and output the converted
   * value.
   * 
   * @throws IllegalActionException If there is no director.
   */
    public void fire() throws IllegalActionException {
        super.fire();
        if ((input.getWidth() > 0) && input.hasToken(0)) doUpdate(); else if ((classifyInstance.getWidth() > 0) && classifyInstance.hasToken(0)) doClassification();
    }
}
