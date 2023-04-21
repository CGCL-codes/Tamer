package edu.cmu.sphinx.knowledge.acoustic;

import edu.cmu.sphinx.util.SphinxProperties;
import edu.cmu.sphinx.util.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.StringTokenizer;

/**
 * Represents the generic interface to the Acoustic 
 * Model for sphinx4
 */
public class TrainerAcousticModel extends AcousticModel {

    /**
     * Prefix for acoustic model SphinxProperties.
     */
    public static final String PROP_PREFIX = "edu.cmu.sphinx.knowledge.acoustic.";

    /**
     * The directory where the acoustic model data can be found.
     */
    public static final String PROP_LOCATION_SAVE = PROP_PREFIX + "location.save";

    /**
     * The default value of PROP_LOCATION_SAVE.
     */
    public static final String PROP_LOCATION_SAVE_DEFAULT = ".";

    /**
     * The save format for the acoustic model data. Current supported
     * formats are:
     *
     *  sphinx3_ascii
     *  sphinx3_binary
     */
    public static final String PROP_FORMAT_SAVE = PROP_PREFIX + "format.save";

    /**
     * The default value of PROP_FORMAT_SAVE.
     */
    public static final String PROP_FORMAT_SAVE_DEFAULT = "sphinx3.binary";

    /**
     * The file containing the phone list.
     */
    public static final String PROP_PHONE_LIST = "phone_list";

    /**
     * The default value of PROP_PHONE_LIST.
     */
    public static final String PROP_PHONE_LIST_DEFAULT = "phonelist";

    /**
     * Flag indicating all models should be operated on.
     */
    public static final int ALL_MODELS = -1;

    /**
     * The logger for this class
     */
    private static Logger logger = Logger.getLogger(PROP_PREFIX + "TrainerAcousticModel");

    /**
     * The pool manager
     */
    private HMMPoolManager hmmPoolManager;

    /**
      * Initializes an acoustic model of a given context. This method
      * should be called once per context. It is used to associate a
      * particular context with an acoustic model resource.  This
      * method should be called only when one acoustic model is
      * specified in the properties file. Otherwise, use the method
      * <code>getAcousticModel(name, context)</code>.
      *
      * @param context	the context of interest
      *
      * @return the acoustic model associated with the context or null
      * if the given context has no associated acoustic model
      *
      * @throws IOException if the model could not be loaded
      * @throws FileNotFoundException if the model does not exist
      */
    public static TrainerAcousticModel getTrainerAcousticModel(String context) throws IOException, FileNotFoundException {
        SphinxProperties props = SphinxProperties.getSphinxProperties(context);
        String amNames = props.getString(PROP_NAMES, PROP_NAMES_DEFAULT);
        if (amNames != null) {
            StringTokenizer tokenizer = new StringTokenizer(amNames);
            if (tokenizer.countTokens() == 0) {
                amNames = null;
            } else if (tokenizer.countTokens() == 1) {
                amNames = amNames.trim();
            } else if (tokenizer.countTokens() > 1) {
                throw new Error("TrainerAcousticModel: more than one acoustic model" + " specified. " + "Instead of method getAcousticModel(context), " + "use method getAcousticModel(name, context).");
            }
        }
        return getTrainerAcousticModel(amNames, context);
    }

    /**
     * Returns the acoustic model of the given name and context.
     * If the acoustic model of the given name and context has not
     * been loaded, it will be loaded, and returned.
     * If there is only one acoustic model for this context,
     * "name" can be null.
     *
     * @param name  the name of the acoustic model, or null if
     *    the acoustic model has no name.
     * @param context  the context of interest
     *
     * @return the name acoustic model in the given context, or
     *   null if no such acoustic model is found
     *
     * @throws IOException if the model count not be loaded
     * @throws FileNotFoundException if the model does not exist
     */
    public static TrainerAcousticModel getTrainerAcousticModel(String name, String context) throws IOException, FileNotFoundException {
        String key = getModelKey(name, context);
        if (contextMap.get(key) == null) {
            TrainerAcousticModel model = new TrainerAcousticModel(name, context);
            contextMap.put(key, model);
        }
        return (TrainerAcousticModel) contextMap.get(key);
    }

    /**
     * Creates an acoustic model with the given name and context.
     * Since acoustic models are only created by the factory method,
     * getAcousticModel(), this constructor is private.
     *
     * @param name the name of the acoustic model
     * @param context the context for this acoustic model
     *
     * @throws IOException if the model could not be loaded
     *
     * @see #getAcousticModel
     */
    private TrainerAcousticModel(String name, String context) throws IOException {
        this.name = name;
        this.context = context;
        this.props = SphinxProperties.getSphinxProperties(context);
        this.loadTimer = Timer.getTimer(context, TIMER_LOAD);
        logInfo();
    }

    /**
     * Creates an acoustic model. Since acoustic models are only
     * created by the factory method <code> getAcousticModel </code>,
     * this contructor is <code> private </code>. This constructor
     * is used when there is only one acoustic model for the given
     * context, which is why the acoustic model has no name.
     * Note that an acoustic model can have a name even if it is
     * the only acoustic model in this context.
     *
     * @param context 	the context for this acoustic model
     *
     * @throws IOException if the model could not be loaded
     *
     * @see #getAcousticModel
     */
    private TrainerAcousticModel(String context) throws IOException {
        this(null, context);
    }

    /**
     * Initializes the acoustic model
     *
     * @throws IOException if the model could not be created
     */
    public void initialize() throws IOException {
        loader = new ModelInitializerLoader(name, props);
        hmmPoolManager = new HMMPoolManager(loader, props);
    }

    /**
     * Saves the acoustic model with a given name and format
     *
     * @param name the name of the acoustic model
     *
     * @throws IOException if the model could not be loaded
     * @throws FileNotFoundException if the model does not exist
     */
    public void save(String name) throws IOException, FileNotFoundException {
        Saver saver;
        String formatProp = PROP_FORMAT_SAVE;
        if (name != null) {
            formatProp = PROP_PREFIX + name + ".format.save";
        }
        String format = props.getString(formatProp, PROP_FORMAT_SAVE_DEFAULT);
        if (format.equals("sphinx3.ascii")) {
            saver = new Sphinx3Saver(name, props, false, loader);
        } else if (format.equals("sphinx3.binary")) {
            saver = new Sphinx3Saver(name, props, true, loader);
        } else {
            saver = null;
            logger.severe("Unsupported acoustic model format " + format);
        }
    }

    /**
     * Loads the acoustic models. This has to be explicitly requested
     * in this class.
     *
     * @throws IOException if the model could not be loaded
     * @throws FileNotFoundException if the model does not exist
     */
    public void load() throws IOException, FileNotFoundException {
        loadTimer.start();
        super.load();
        loadTimer.stop();
        logInfo();
        hmmPoolManager = new HMMPoolManager(loader, props);
    }

    /**
     * Resets the buffers.
     */
    public void resetBuffers() {
        hmmPoolManager.resetBuffers();
    }

    /**
     * Accumulate the current TrainerScore into the buffers.
     *
     * @param index the current index into the TrainerScore vector
     * @param trainerScore the TrainerScore in the current frame
     * @param nextTrainerScore the TrainerScore in the next frame
     */
    public void accumulate(int index, TrainerScore[] trainerScore, TrainerScore[] nextTrainerScore) {
        hmmPoolManager.accumulate(index, trainerScore, nextTrainerScore);
    }

    /**
     * Accumulate the current TrainerScore into the buffers.
     *
     * @param index the current index into the TrainerScore vector
     * @param trainerScore the TrainerScore
     */
    public void accumulate(int index, TrainerScore[] trainerScore) {
        hmmPoolManager.accumulate(index, trainerScore);
    }

    /**
     * Update the log likelihood. This should be called at the end of
     * each utterance.
     */
    public void updateLogLikelihood() {
        hmmPoolManager.updateLogLikelihood();
    }

    /**
     * Normalize the buffers and update the models.
     *
     * @return the log likelihood for the whole training set
     */
    public float normalize() {
        float logLikelihood = hmmPoolManager.normalize();
        hmmPoolManager.update();
        return logLikelihood;
    }
}
