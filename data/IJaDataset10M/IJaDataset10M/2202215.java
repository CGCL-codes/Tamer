package bioweka.core.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import bioweka.core.BioWekaUtils;
import bioweka.core.components.DebugableComponent;
import bioweka.core.components.OptionHandlerComponent;
import bioweka.core.debuggers.Debugger;
import bioweka.core.debuggers.DebuggerProperty;
import bioweka.core.inspection.Inspection;
import bioweka.core.inspection.Report;
import bioweka.core.inspection.StringReport;
import bioweka.core.managers.ComponentPropertyManager;
import bioweka.core.managers.DebugableComponentManager;
import bioweka.core.managers.InspectionPropertyManager;
import bioweka.core.managers.OptionHandlerManager;
import bioweka.core.managers.PropertyManager;
import bioweka.core.properties.io.AbstractFileProperty;
import bioweka.core.properties.io.AbstractInputFileProperty;
import bioweka.core.properties.io.AbstractOutputFileProperty;
import bioweka.core.properties.io.ConsoleInputFileProperty;
import bioweka.core.properties.io.ConsoleOutputFileProperty;
import weka.core.FastVector;
import weka.core.Utils;
import weka.core.converters.AbstractLoader;
import weka.core.converters.AbstractSaver;
import weka.core.converters.ArffSaver;
import weka.core.converters.FileSourcedConverter;

/**
 * Abstract class for BioWeka's {@link weka.core.converters.Loader} 
 * implementations.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.10 $
 */
public abstract class AbstractBioWekaLoader extends AbstractLoader implements FileSourcedConverter, DebugableComponent, OptionHandlerComponent, Inspection {

    /**
     * Runs a loader from command line.
	 * @param loader the loader object
	 * @param args the command line parameters as passed to the 
     * <code>main</code> method. 
	 * @return exit code for usage with {@link System#exit(int)}
     * @throws NullPointerException if one of the parameters is null.
     * @see BioWekaUtils#EXIT_CODE_ERROR
     * @see BioWekaUtils#EXIT_CODE_OK
	 */
    public static int doMain(AbstractBioWekaLoader loader, String[] args) throws NullPointerException {
        if (loader == null) {
            throw new NullPointerException("loader is null.");
        }
        if (args == null) {
            throw new NullPointerException("args is null.");
        }
        AbstractInputFileProperty inputProperty = new ConsoleInputFileProperty();
        AbstractOutputFileProperty outputProperty = new ConsoleOutputFileProperty();
        AbstractSaver saver = new ArffSaver();
        try {
            loader.setOptions(args);
            inputProperty.setOptions(args);
            outputProperty.setOptions(args);
            Utils.checkForRemainingOptions(args);
            if (inputProperty.getFile().equals(AbstractFileProperty.FILE_MISSING)) {
                loader.setSource(inputProperty.getInput());
            } else {
                loader.setSource(inputProperty.getFile());
            }
            if (outputProperty.getFile().equals(AbstractFileProperty.FILE_MISSING)) {
                saver.setDestination(outputProperty.getOutput());
            } else {
                saver.setFile(outputProperty.getFile());
                saver.setDestination(outputProperty.getFile());
            }
            BioWekaUtils.convert(loader, saver);
            outputProperty.close();
            inputProperty.close();
            loader.getDebugger().state(loader);
            return BioWekaUtils.EXIT_CODE_OK;
        } catch (Exception e) {
            loader.getDebugger().severe(e);
            System.err.println(e.toString());
            System.err.println();
            FastVector generalOptions = new FastVector();
            inputProperty.listOptions(generalOptions);
            outputProperty.listOptions(generalOptions);
            BioWekaUtils.printUsage(loader, generalOptions.elements());
            return BioWekaUtils.EXIT_CODE_ERROR;
        }
    }

    /**
	 * The input stream for the file that should be loaded.
	 */
    private transient InputStream inputStream = null;

    /**
	 * The file that should be loaded.
	 */
    private File inputFile = null;

    /**
     * Property manager for option handling.
     */
    private OptionHandlerManager optionManager = null;

    /**
     * Property manager for the inspection of the property values.
     */
    private InspectionPropertyManager inspectionManager = null;

    /**
     * The debugger property.
     */
    private DebuggerProperty debuggerProperty = null;

    /**
     * Initializes the loader. Both the input stream and the input file are set
     * to <code>null</code>.
     */
    public AbstractBioWekaLoader() {
        super();
        inspectionManager = new InspectionPropertyManager(new DebugableComponentManager(new ComponentPropertyManager(this)));
        optionManager = new OptionHandlerManager(inspectionManager);
        debuggerProperty = new DebuggerProperty();
        optionManager.addProperty(debuggerProperty);
    }

    /**
     * Returns the relation for the current input;
     * @return the relation name including the path of the input file, if set,
     * the class name of the loader and its options 
     */
    protected final String relationName() {
        String relationName = null;
        File inputFile = retrieveFile();
        if (inputFile == null) {
            relationName = BioWekaUtils.makeRelationName(this);
        } else {
            relationName = BioWekaUtils.makeRelationName(inputFile.getName(), this);
        }
        return relationName;
    }

    /**
     * Returns the property manager.
     * @return a manager responsible for property handling
     */
    protected PropertyManager manager() {
        return optionManager;
    }

    /**
     * Checks if there is an input for the <code>Loader</code>
     * @throws IOException if no input is specified.
     */
    protected void checkInput() throws IOException {
        if (inputStream == null) {
            throw new IOException("No source has been specified.");
        }
    }

    /**
     * Checks if a retrieval mode is already set and throws an exception if it
     * is. Otherwise it sets the retrieval mode.
     * @param mode the new retrieval mode
     * @throws IOException if the current retrieval mode is not
     * {@link AbstractLoader#NONE}.
     * @see #setRetrieval(int)
     */
    protected final void checkRetrieval(int mode) throws IOException {
        int actualMode = getRetrieval();
        if (actualMode != mode) {
            if (actualMode == NONE) {
                setRetrieval(mode);
            } else {
                throw new IOException("Cannot mix getting instances in both " + "incremental and batch modes.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void getOptions(FastVector options) throws NullPointerException {
        optionManager.getOptions(options);
    }

    /**
     * {@inheritDoc}
     */
    public void listOptions(FastVector options) throws NullPointerException {
        optionManager.listOptions(options);
    }

    /**
     * {@inheritDoc}
     */
    public final String[] getOptions() {
        return optionManager.getOptions();
    }

    /**
     * {@inheritDoc}
     */
    public final Enumeration listOptions() {
        return optionManager.listOptions();
    }

    /**
     * {@inheritDoc}
     */
    public void setOptions(String[] options) throws Exception {
        optionManager.setOptions(options);
    }

    /**
     * Closes the input stream.
     * @throws Throwable if the input stream could not be closed.
	 */
    protected void finalize() throws Throwable {
        super.finalize();
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public final String toString() {
        return StringReport.toString(this);
    }

    /**
     * {@inheritDoc}
     */
    public void inspect(Report report) throws NullPointerException {
        inspectionManager.inspect(report);
        report.append("mode", (new String[] { "NONE", "INCREMENTAL", "BATCH" })[getRetrieval()]);
        report.append("file", inputFile);
    }

    /**
     * Sets the input file and opens it as an input stream. The input stream is
     * passed to the {@link #setSource(java.io.InputStream)} 
     * method. If the loader has already opened a file it is closed and both
     * the input stream and the input file are set to <code>null</code>. The 
     * passed file is only remembered as the input file if the method suceeded.
     * @param file the input file that should be loaded.
     * @throws NullPointerException if <code>file</code> is <code>null</code>.
     * @throws IOException if the input file could not be loaded.
	 */
    public final void setSource(File file) throws IOException, NullPointerException {
        if (file == null) {
            throw new NullPointerException("file is null.");
        }
        if (inputStream != null) {
            inputStream.close();
        }
        inputStream = null;
        inputFile = null;
        inputStream = new FileInputStream(file);
        setSource(inputStream);
        this.inputFile = file;
        getDebugger().config("source", file.getPath());
    }

    /**
     * Sets the retrieval mode. The mode change is logged via the debug helper.
     * @param mode the new retrieval mode
     * @see AbstractLoader#BATCH
     * @see AbstractLoader#INCREMENTAL
     * @see AbstractLoader#NONE
     */
    protected final void setRetrieval(int mode) {
        super.setRetrieval(mode);
        getDebugger().config("retrieval", new Integer(mode));
    }

    /**
     * {@inheritDoc}
     */
    public final File retrieveFile() {
        return inputFile;
    }

    /**
     * {@inheritDoc}
     */
    public final void setFile(File file) throws IOException {
        setSource(file);
    }

    /**
     * {@inheritDoc}
     */
    public final String debuggerTipText() {
        return debuggerProperty.debuggerTipText();
    }

    /**
     * {@inheritDoc}
     */
    public final Debugger getDebugger() {
        return debuggerProperty.getDebugger();
    }

    /**
     * {@inheritDoc}
     */
    public final void setDebugger(Debugger debugger) throws Exception {
        debuggerProperty.setDebugger(debugger);
    }
}
