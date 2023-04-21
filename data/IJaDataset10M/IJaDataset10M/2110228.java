package com.rapidminer.operator.io;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rapidminer.Process;
import com.rapidminer.operator.Annotations;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessSetupError.Severity;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MDTransformationRule;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.MetaDataError;
import com.rapidminer.operator.ports.metadata.SimpleMetaDataError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.Observable;
import com.rapidminer.tools.Observer;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.io.Encoding;

/** 
 * Superclass of all operators that have no input and generate a single output.
 * This class is mainly a tribute to the e-LICO DMO.
 * 
 * @author Simon Fischer 
 */
public abstract class AbstractReader<T extends IOObject> extends Operator {

    private final OutputPort outputPort = getOutputPorts().createPort("output");

    private final Class<? extends IOObject> generatedClass;

    private boolean cacheDirty = true;

    private MetaData cachedMetaData;

    private MetaDataError cachedError;

    public AbstractReader(OperatorDescription description, Class<? extends IOObject> generatedClass) {
        super(description);
        this.generatedClass = generatedClass;
        getTransformer().addRule(new MDTransformationRule() {

            @Override
            public void transformMD() {
                if (cacheDirty || !isMetaDataCacheable()) {
                    try {
                        cachedMetaData = AbstractReader.this.getGeneratedMetaData();
                        cachedError = null;
                    } catch (OperatorException e) {
                        cachedMetaData = new MetaData(AbstractReader.this.generatedClass);
                        String msg = e.getMessage();
                        if ((msg == null) || (msg.length() == 0)) {
                            msg = e.toString();
                        }
                        cachedError = new SimpleMetaDataError(Severity.WARNING, outputPort, "cannot_create_exampleset_metadata", new Object[] { msg });
                    }
                    if (cachedMetaData != null) {
                        cachedMetaData.addToHistory(outputPort);
                    }
                    cacheDirty = false;
                }
                outputPort.deliverMD(cachedMetaData);
                if (cachedError != null) {
                    outputPort.addError(cachedError);
                }
            }
        });
        observeParameters();
    }

    private void observeParameters() {
        getParameters().addObserverAsFirst(new Observer<String>() {

            @Override
            public void update(Observable<String> observable, String arg) {
                cacheDirty = true;
            }
        }, false);
    }

    public MetaData getGeneratedMetaData() throws OperatorException {
        return new MetaData(generatedClass);
    }

    protected boolean isMetaDataCacheable() {
        return false;
    }

    /** Creates (or reads) the ExampleSet that will be returned by {@link #apply()}. */
    public abstract T read() throws OperatorException;

    @Override
    public void doWork() throws OperatorException {
        final T result = read();
        addAnnotations(result);
        outputPort.deliver(result);
    }

    protected void addAnnotations(T result) {
        for (ReaderDescription rd : READER_DESCRIPTIONS.values()) {
            if (rd.readerClass.equals(this.getClass())) {
                if (result.getAnnotations().getAnnotation(Annotations.KEY_SOURCE) == null) {
                    try {
                        String source = getParameter(rd.fileParameterKey);
                        if (source != null) {
                            result.getAnnotations().setAnnotation(Annotations.KEY_SOURCE, source);
                        }
                    } catch (UndefinedParameterError e) {
                    }
                }
                return;
            }
        }
    }

    /** Describes an operator that can read certain file types. */
    public static class ReaderDescription {

        private final String fileExtension;

        private final Class<? extends AbstractReader> readerClass;

        /** This parameter must be set to the file name. */
        private final String fileParameterKey;

        public ReaderDescription(String fileExtension, Class<? extends AbstractReader> readerClass, String fileParameterKey) {
            super();
            this.fileExtension = fileExtension;
            this.readerClass = readerClass;
            this.fileParameterKey = fileParameterKey;
        }
    }

    private static final Map<String, ReaderDescription> READER_DESCRIPTIONS = new HashMap<String, ReaderDescription>();

    /** Registers an operator that can read files with a given extension. */
    protected static void registerReaderDescription(ReaderDescription rd) {
        READER_DESCRIPTIONS.put(rd.fileExtension.toLowerCase(), rd);
    }

    public static AbstractReader createReader(URL url) throws OperatorCreationException {
        String file = url.getFile();
        int dot = file.lastIndexOf('.');
        if (dot == -1) {
            return null;
        } else {
            String extension = file.substring(dot + 1).toLowerCase();
            ReaderDescription rd = READER_DESCRIPTIONS.get(extension);
            if (rd == null) {
                return null;
            }
            AbstractReader reader = OperatorService.createOperator(rd.readerClass);
            reader.setParameter(rd.fileParameterKey, url.toString());
            return reader;
        }
    }

    public static boolean canMakeReaderFor(URL url) {
        String file = url.getFile();
        int dot = file.lastIndexOf('.');
        if (dot == -1) {
            return false;
        } else {
            String extension = file.substring(dot + 1).toLowerCase();
            return READER_DESCRIPTIONS.containsKey(extension);
        }
    }

    /** Returns the key of the parameter that specifies the file to be read. */
    public static String getFileParameterForOperator(Operator operator) {
        for (ReaderDescription rd : READER_DESCRIPTIONS.values()) {
            if (rd.readerClass.equals(operator.getClass())) {
                return rd.fileParameterKey;
            }
        }
        return null;
    }

    @Override
    protected void registerOperator(Process process) {
        super.registerOperator(process);
        cacheDirty = true;
    }

    protected boolean supportsEncoding() {
        return false;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        if (supportsEncoding()) types.addAll(Encoding.getParameterTypes(this));
        return types;
    }
}
