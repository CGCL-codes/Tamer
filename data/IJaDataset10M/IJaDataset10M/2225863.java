package net.sf.beatrix.internal.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.beatrix.core.ComplexComponent;
import net.sf.beatrix.core.Detector;
import net.sf.beatrix.core.DetectorConfiguration;
import net.sf.beatrix.core.DetectorConfigurations;
import net.sf.beatrix.core.DetectorStatistics;
import net.sf.beatrix.core.container.AnnotatedDataCollection;
import net.sf.beatrix.core.container.DataChunk;
import net.sf.beatrix.core.event.ClassificationEvent;
import net.sf.beatrix.core.event.DetectorStateChangedEvent;
import net.sf.beatrix.core.event.listener.ClassificationListener;
import net.sf.beatrix.core.exceptions.DetectorAlreadyRunningException;
import net.sf.beatrix.core.module.Module;
import net.sf.beatrix.core.module.ModuleImplementation;
import net.sf.beatrix.core.module.analyzer.Analyzer;
import net.sf.beatrix.core.module.analyzer.Classification;
import net.sf.beatrix.core.module.analyzer.ComplexAnalyzer;
import net.sf.beatrix.core.module.dumper.ComplexDumper;
import net.sf.beatrix.core.module.dumper.Dumper;
import net.sf.beatrix.core.module.dumper.DumperResult;
import net.sf.beatrix.core.module.extractor.ComplexExtractor;
import net.sf.beatrix.core.module.extractor.Extractor;
import net.sf.beatrix.core.module.formatter.ComplexFormatter;
import net.sf.beatrix.core.module.formatter.Formatter;
import net.sf.beatrix.core.module.input.ComplexInput;
import net.sf.beatrix.core.module.input.Input;
import net.sf.beatrix.core.module.input.InputProperties;
import net.sf.beatrix.core.module.input.InputProvider;
import net.sf.beatrix.core.module.output.ComplexOutput;
import net.sf.beatrix.core.module.output.Output;
import net.sf.beatrix.core.module.preferences.PreferenceStore;
import net.sf.beatrix.lang.NullArgumentException;
import net.sf.beatrix.lang.Strings;
import net.sf.beatrix.util.event.provider.AbstractChangedEventProvider;
import net.sf.beatrix.util.logging.Logger;
import net.sf.beatrix.util.logging.StandardLogger;

/**
 * Implements the detector which performs the actual analysis in a global view.
 * It is able to use multiple modules per type. This is not true for the
 * {@link Dumper} which is supposed to process the data in that category on it's
 * own.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public class DetectorImpl extends AbstractChangedEventProvider<DetectorStateChangedEvent> implements Detector {

    /** The {@link List} of inputs ({@link ComplexInput}s). */
    private List<ComplexInput> inputs;

    /** The {@link List} of dumpers ({@link ComplexDumper}s). */
    private ComplexDumper dumper;

    /** The {@link List} of extractors ({@link ComplexExtractor}s). */
    private List<ComplexExtractor> extractors;

    /** The {@link List} of analyzers ({@link ComplexAnalyzer}s). */
    private List<ComplexAnalyzer> analyzers;

    /** The {@link List} of outputs ({@link ComplexOutput}s). */
    private List<ComplexFormatter> formatters;

    /** The {@link List} of outputs ({@link ComplexOutput}s). */
    private List<ComplexOutput> outputs;

    /**
   * A map to link {@link Module} identifiers to concrete
   * {@link ComplexComponent}s. This is used to provide the runtime read-out
   * functionality of the {@link Module}s for the analysis part.
   */
    private Map<String, ComplexComponent> idMap;

    /** The corresponding detector configuration. */
    private DetectorConfiguration detectorConfiguration;

    /** The logging entity the detector writes its output to. */
    private Logger log;

    /**
   * The default constructor.
   * 
   * @param detectorConfig
   *          The corresponding detector.
   */
    public DetectorImpl(DetectorConfiguration detectorConfig) {
        if (detectorConfig == null) {
            throw new NullArgumentException("detectorConfig");
        }
        this.detectorConfiguration = DetectorConfigurations.unmodifiableConfiguration(detectorConfig);
        inputs = new ArrayList<ComplexInput>();
        extractors = new ArrayList<ComplexExtractor>();
        analyzers = new ArrayList<ComplexAnalyzer>();
        formatters = new ArrayList<ComplexFormatter>();
        outputs = new ArrayList<ComplexOutput>();
        idMap = new HashMap<String, ComplexComponent>();
        for (Module m : detectorConfiguration) {
            ModuleImplementation modImpl = m.getImplementation();
            if (modImpl != null) {
                addDetectorPart(m.getID(), modImpl);
            }
        }
        this.log = StandardLogger.log;
    }

    /**
   * Adds a concrete implementation (input, dumper, extractor, analyzer or
   * output) to the detector.
   * 
   * @param module
   *          The module implementation.
   * @throws IllegalArgumentException
   *           If the given module implementation isn't of one of the expected
   *           types.
   */
    @SuppressWarnings("unchecked")
    protected void addDetectorPart(String id, ModuleImplementation module) {
        if (module == null) {
            throw new NullArgumentException("module");
        }
        if (module instanceof Input) {
            addInput(id, (Input) module);
        } else if (module instanceof Dumper) {
            setDumper(id, (Dumper) module);
        } else if (module instanceof Extractor) {
            addExtractor(id, (Extractor) module);
        } else if (module instanceof Analyzer) {
            addAnalyser(id, (Analyzer) module);
        } else if (module instanceof Formatter) {
            addFormatter(id, (Formatter) module);
        } else if (module instanceof Output) {
            addOutput(id, (Output) module);
        } else {
            throw new IllegalArgumentException("You have to pass a class implementing one of the following interfaces as well: " + "Input, Dumper, Extractor, Analyzer, Formatter, Output");
        }
    }

    /**
   * Verifies if the {@link Module}'s arguments (given as the both input
   * parameters) are valid.
   * 
   * @param id
   *          The {@link Module}'s identifier.
   * @param modImpl
   *          The module implementation.
   */
    private void verifyAddModuleArguments(String id, ModuleImplementation modImpl) {
        if (isActive()) {
            throw new DetectorAlreadyRunningException();
        }
        if (modImpl == null) {
            throw new NullArgumentException("modImpl");
        }
        if (!Strings.isValidID(id)) {
            throw new IllegalArgumentException("The given identifier isn't valid.");
        }
    }

    /**
   * Adds an {@link Input} implementation to the detector.
   * 
   * @param input
   *          The {@link Input} implementation.
   */
    protected synchronized void addInput(String id, Input input) {
        verifyAddModuleArguments(id, input);
        ComplexInput complexInput = new ComplexInput(input);
        inputs.add(complexInput);
        idMap.put(id, complexInput);
    }

    /**
   * Sets the {@link Dumper} implementation for the detector.
   * 
   * @param dumper
   *          The {@link Dumper} implementation.
   */
    protected synchronized void setDumper(String id, Dumper dumper) {
        verifyAddModuleArguments(id, dumper);
        if (hasDumper()) {
            throw new IllegalArgumentException("A Detector must not have more than one dumper");
        }
        this.dumper = new ComplexDumper(dumper);
        idMap.put(id, this.dumper);
    }

    /**
   * Checks if there is already a {@link Dumper} implementation.
   * 
   * @return The boolean indicator for the presents of a {@link Dumper}
   *         implementation.
   */
    public boolean hasDumper() {
        return dumper != null;
    }

    /**
   * Adds an {@link Extractor} implementation to the detector.
   * 
   * @param extractor
   *          The {@link Extractor} implementation.
   */
    protected synchronized void addExtractor(String id, Extractor extractor) {
        verifyAddModuleArguments(id, extractor);
        ComplexExtractor complexExtractor = new ComplexExtractor(extractor);
        extractors.add(complexExtractor);
        idMap.put(id, complexExtractor);
    }

    /**
   * Adds an {@link Analyzer} implementation to the detector.
   * 
   * @param analyzer
   *          The {@link Analyzer} implementation.
   */
    protected synchronized void addAnalyser(String id, Analyzer analyzer) {
        verifyAddModuleArguments(id, analyzer);
        ComplexAnalyzer complexAnalyzer = new ComplexAnalyzer(analyzer);
        analyzers.add(complexAnalyzer);
        idMap.put(id, complexAnalyzer);
    }

    /**
   * Adds an {@link Formatter} implementation to the detector.
   * 
   * @param formatter
   *          The {@link Formatter} implementation.
   */
    protected synchronized void addFormatter(String id, Formatter<? extends Classification> formatter) {
        verifyAddModuleArguments(id, formatter);
        ComplexFormatter complexFormatter = new ComplexFormatter(formatter);
        formatters.add(complexFormatter);
        idMap.put(id, complexFormatter);
    }

    /**
   * Adds an {@link Output} implementation to the detector.
   * 
   * @param output
   *          The {@link Output} implementation.
   */
    protected synchronized void addOutput(String id, Output output) {
        verifyAddModuleArguments(id, output);
        ComplexOutput complexOutput = new ComplexOutput(output);
        outputs.add(complexOutput);
        idMap.put(id, complexOutput);
    }

    /** Some statistics of the running detector thread. */
    private DetectorStatistics statistics;

    /** Renders if the {@link Detector} is in debug-mode or not. */
    private boolean debug = false;

    /**
   * If set the {@link Detector} pauses (interrupts the detection process) at
   * the next possibility to do so ( after each classification and dumper
   * output).
   */
    private boolean pause = false;

    /** Indicates if the {@link Detector} is currently waiting/ got paused. */
    private boolean isWaiting = false;

    /** If set the {@link Detector} is going to halt. */
    private boolean breakOffThread;

    /** Object for synchronizing the {@link Detector} when stopping/ pausing etc. */
    private Object sync = new Object();

    /** The actual {@link Thread} which perform the detection steps. */
    private Thread detectorThread = new Thread() {

        /**
     * {@link RuntimeException} to indicate, that the {@link Detector} should
     * stop.
     * 
     * @author Christian Wressnegger <chwress@users.sourceforge.net>
     */
        class StopException extends RuntimeException {

            private static final long serialVersionUID = 0L;

            public StopException() {
            }
        }

        /**
     * Indicates if a successful classification was performed. This flag is
     * import for the pausing policy, such that the debugger doesn't stop twice
     * if a classification occurred.
     */
        private boolean successfullClassified;

        /**
     * Classification a potential output are handled as listener events.
     */
        private ClassificationListener classificationListener = new ClassificationListener() {

            /**
       * Pass the {@link Classification} to all output modules.
       * 
       * @param c
       *          The classification occurred.
       */
            private void output(Classification c) {
                for (ComplexOutput output : outputs) {
                    output.export(c);
                }
            }

            @Override
            public void classified(ClassificationEvent event) {
                if (event.getClassification() != null) {
                    successfullClassified = true;
                    output(event.getClassification());
                    for (ComplexFormatter formatter : formatters) {
                        Classification fc = formatter.transform(event.getClassification());
                        if (!fc.equals(event.getClassification())) {
                            output(fc);
                        }
                    }
                    if (pause) {
                        pause();
                    }
                }
            }
        };

        /**
     * Checks if the entity using the {@link Detector} asked to stop or the
     * {@link Thread} already is interrupted. If so it throws a
     * {@link StopException}.
     * 
     * @throws StopException
     */
        private void checkState() throws StopException {
            if (breakOffThread || isInterrupted()) {
                throw new StopException();
            }
        }

        /**
     * Preforms the actual pausing of the detector {@link Thread}.
     */
        private void pause() {
            checkState();
            try {
                synchronized (sync) {
                    isWaiting = true;
                    fireStateChangedEvent(DetectorStateChangedEvent.State.PAUSED);
                    sync.wait();
                }
            } catch (InterruptedException e) {
            } catch (Exception e) {
                log.warning("Unable to halt the detector after a single step.");
            } finally {
                isWaiting = false;
                fireStateChangedEvent(DetectorStateChangedEvent.State.RESUMED);
            }
            checkState();
        }

        /**
     * Implements the actual detection process.
     */
        @Override
        public void run() {
            statistics = new DetectorStatistics();
            statistics.setStartingTime(new Date(System.currentTimeMillis()));
            breakOffThread = false;
            for (ComplexAnalyzer a : analyzers) {
                a.addClassificationListener(classificationListener);
            }
            try {
                for (ComplexInput input : inputs) {
                    checkState();
                    for (InputProvider in : input) {
                        checkState();
                        log.info(in.toString());
                        dumper.setInput(in);
                        statistics.setCurrentInput(in.toString());
                        Object size = in.getData(InputProperties.SIZE);
                        if (size instanceof Long) {
                            statistics.setCurrentInputSize((Long) size);
                        }
                        for (AnnotatedDataCollection result : dumper) {
                            checkState();
                            statistics.setCurrentInputPosition(dumper.getCurrentInputPosition());
                            AnnotatedDataCollection data = new AnnotatedDataCollection(result);
                            for (ComplexExtractor fExtractor : extractors) {
                                checkState();
                                data.putAll(fExtractor.start(data));
                            }
                            for (ComplexAnalyzer analyzer : analyzers) {
                                checkState();
                                successfullClassified = false;
                                analyzer.start(data);
                            }
                            if (pause && !successfullClassified) {
                                pause();
                            }
                        }
                    }
                }
            } catch (StopException e) {
            }
            for (ComplexAnalyzer a : analyzers) {
                a.removeClassificationListener(classificationListener);
            }
            log.info("waiting for all output modules to close...");
            for (ComplexOutput out : outputs) {
                out.close();
            }
            fireStateChangedEvent(DetectorStateChangedEvent.State.TERMINATED);
            log.info("Bye.");
        }
    };

    @Override
    public synchronized void start(boolean debug) {
        start(debug, true);
    }

    @Override
    public synchronized void start(boolean debug, boolean async) {
        if (isActive()) {
            throw new DetectorAlreadyRunningException();
        }
        this.debug = pause = debug;
        detectorThread.start();
        fireStateChangedEvent(DetectorStateChangedEvent.State.STARTED);
        if (!detectorThread.isAlive()) {
            log.error("Unable to start detector thread.");
            fireStateChangedEvent(DetectorStateChangedEvent.State.TERMINATED);
        } else {
            if (!async) {
                try {
                    detectorThread.join();
                } catch (InterruptedException e) {
                    System.err.println("interrupt");
                }
            }
        }
    }

    @Override
    public synchronized boolean isActive() {
        return detectorThread != null && detectorThread.isAlive() && !breakOffThread;
    }

    @Override
    public boolean getsDebugged() {
        return debug;
    }

    @Override
    public synchronized void step() {
        if (!isActive()) {
            throw new IllegalStateException("The detector isn't running.");
        }
        if (!debug) {
            throw new IllegalStateException("The detector isnt' running in debug mode.");
        }
        synchronized (sync) {
            sync.notifyAll();
        }
    }

    @Override
    public synchronized void resume() {
        if (!isActive()) {
            throw new IllegalStateException("The detector isn't running.");
        }
        if (!debug) {
            throw new IllegalStateException("The detector isnt' running in debug mode.");
        }
        synchronized (sync) {
            pause = false;
            sync.notifyAll();
        }
    }

    @Override
    public synchronized boolean isWaiting() {
        return isActive() && isWaiting;
    }

    @Override
    public synchronized Object get(String id) {
        if (!isWaiting() && isActive()) {
            throw new IllegalStateException("Make sure you fetch module data only when the detector got paused.");
        }
        if (!Strings.isValidID(id)) {
            throw new IllegalArgumentException("The given module identifier isn't a valid ID.");
        }
        ComplexComponent cc = idMap.get(id);
        if (cc instanceof ComplexInput) {
            DumperResult result = dumper.getLastResult();
            return result == null ? null : new DataChunk(result.getRawData(), dumper.getCurrentInputPosition());
        }
        return cc.getLastOutput();
    }

    @Override
    public synchronized DetectorStatistics getStatistics() {
        return isWaiting() ? statistics.clone() : null;
    }

    @Override
    public synchronized void stop() {
        if (!isActive()) {
            throw new IllegalStateException("The detector isn't running.");
        }
        synchronized (sync) {
            breakOffThread = true;
            sync.notifyAll();
        }
        log.info("Detector will stop now...");
    }

    @Override
    public DetectorConfiguration getDetectorConfiguration() {
        return detectorConfiguration;
    }

    @Override
    public void setLogger(Logger logger) {
        this.log = logger;
    }

    @Override
    public void setPreferences(PreferenceStore preferences) {
        if (isActive()) {
            throw new DetectorAlreadyRunningException();
        }
        for (ComplexComponent cc : inputs) {
            cc.setPreferences(preferences);
        }
        if (hasDumper()) {
            dumper.setPreferences(preferences);
        }
        for (ComplexComponent cc : extractors) {
            cc.setPreferences(preferences);
        }
        for (ComplexComponent cc : analyzers) {
            cc.setPreferences(preferences);
        }
        for (ComplexComponent cc : formatters) {
            cc.setPreferences(preferences);
        }
        for (ComplexComponent cc : outputs) {
            cc.setPreferences(preferences);
        }
    }

    /**
   * Sets of a {@link DetectorStateChangedEvent} for the given state.
   * 
   * @param state
   *          The state which should be propagated.
   */
    private void fireStateChangedEvent(DetectorStateChangedEvent.State state) {
        fireChangedEvent(new DetectorStateChangedEvent(this, state));
    }

    /**
   * Returns the last outputs of all analyzers in use.
   * 
   * @see net.sf.beatrix.core.module.analyzer.ComplexAnalyzer#getLastOutput()
   */
    @Override
    public Object getLastOutput() {
        ArrayList<Object> list = new ArrayList<Object>();
        for (ComplexAnalyzer analyzer : analyzers) {
            if (analyzer.getLastOutput() != null) {
                list.add(analyzer.getLastOutput());
            }
        }
        return list.isEmpty() ? null : list.toArray();
    }
}
