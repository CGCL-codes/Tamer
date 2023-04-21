package at.tuwien.minimee.migration.engines;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import at.tuwien.minimee.migration.runners.DefaultRunner;
import at.tuwien.minimee.migration.runners.IRunner;
import at.tuwien.minimee.migration.runners.RunInfo;
import at.tuwien.minimee.model.Machine;
import at.tuwien.minimee.model.ToolConfig;
import at.tuwien.minimee.registry.ToolRegistry;
import eu.planets_project.pp.plato.model.DigitalObject;
import eu.planets_project.pp.plato.model.FormatInfo;
import eu.planets_project.pp.plato.model.measurement.MeasurableProperty;
import eu.planets_project.pp.plato.model.measurement.Measurement;
import eu.planets_project.pp.plato.model.values.FreeStringValue;
import eu.planets_project.pp.plato.model.values.PositiveFloatValue;
import eu.planets_project.pp.plato.services.action.MigrationResult;
import eu.planets_project.pp.plato.util.FileUtils;
import eu.planets_project.pp.plato.util.OS;
import eu.planets_project.pp.plato.util.PlatoLogger;

public class MiniMeeDefaultMigrationEngine implements IMigrationEngine {

    private Log log = PlatoLogger.getLogger(this.getClass());

    protected DecimalFormat df = new DecimalFormat("#####.###");

    /**
     * temporary directory to be used for storing files
     */
    private String tempDir;

    private String machine;

    private String configParam;

    public String getConfigParam() {
        return configParam;
    }

    public void setConfigParam(String configParam) {
        this.configParam = configParam;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public void addProperty(MeasurableProperty p) {
        measurableProperties.add(p);
    }

    private List<MeasurableProperty> measurableProperties = new ArrayList<MeasurableProperty>();

    protected String name;

    /**
     * initialises the engine
     * @see #initTempDir()
     */
    public MiniMeeDefaultMigrationEngine() {
        initTempDir();
    }

    /**
     * We have to make sure here that the temporary directory is initialized correctly.
     * Depending on the configuration and on the operating system, getProperty("java.io.tmpdir")
     * comes with or without an ending slash. We rely on the fact that it is 
     */
    private void initTempDir() {
        tempDir = OS.getTmpPath();
    }

    public String getTempDir() {
        if (tempDir == "") {
            initTempDir();
        }
        return tempDir;
    }

    public String getName() {
        return name;
    }

    public List<MeasurableProperty> getMeasurableProperties() {
        return measurableProperties;
    }

    public void setMeasurableProperties(List<MeasurableProperty> measurableProperties) {
        this.measurableProperties = measurableProperties;
    }

    /**
     * @see IMigrationEngine#migrate(byte[], String, String)
     * This measures <b>elapsed time</b> and relative file size only.
     */
    public MigrationResult migrate(byte[] data, String toolID, String params) {
        MigrationResult result = new MigrationResult();
        String key = "minimee/";
        String toolIdentifier = toolID.substring(toolID.indexOf(key) + key.length());
        ToolConfig config = ToolRegistry.getInstance().getToolConfig(toolIdentifier);
        migrate(data, config, params, result);
        normaliseMeasurements(result, toolIdentifier);
        result.getMeasurements().put("input:filesize", new Measurement("input:filesize", data.length));
        return result;
    }

    /**
     * Currently does not do anything.
     * @param result
     * @param toolIdentifier
     */
    protected void normaliseMeasurements(MigrationResult result, String toolIdentifier) {
    }

    /**
     * migrates a bytestream using the provided toolconfig and params,
     * and measures elapsed time.
     * 
     */
    public boolean migrate(byte[] data, ToolConfig config, String params, MigrationResult result) {
        System.gc();
        boolean success = false;
        long time = System.nanoTime();
        String inputFile = prepareInputFile(data, config, time);
        String outputFile = prepareOutputFile(config, time);
        try {
            String command = prepareCommand(config, params, inputFile, outputFile, time);
            IRunner runner = makeRunner(command, config);
            RunInfo r = runner.run();
            result.setSuccessful(r.isSuccess());
            result.setReport(r.getReport());
            byte[] migratedFile = new byte[] {};
            try {
                migratedFile = FileUtils.getBytesFromFile(new File(outputFile));
                DigitalObject u = new DigitalObject();
                u.getData().setData(migratedFile);
                FormatInfo tFormat = new FormatInfo();
                tFormat.setDefaultExtension(config.getOutEnding());
                result.setTargetFormat(tFormat);
                result.setMigratedObject(u);
            } catch (Exception e) {
                log.error("Could not get outputfile " + outputFile);
                result.setSuccessful(false);
                log.error(e);
            }
            collectData(config, time, result);
            double length = migratedFile.length;
            double elapsed = r.getElapsedTimeMS();
            double elapsedPerMB = ((double) elapsed) / (getMByte(data));
            Measurement me = new Measurement(MigrationResult.MIGRES_ELAPSED_TIME, elapsed);
            result.getMeasurements().put(MigrationResult.MIGRES_ELAPSED_TIME, me);
            for (MeasurableProperty property : getMeasurableProperties()) {
                if (!property.getName().startsWith("machine:")) {
                    Measurement m = new Measurement();
                    m.setProperty(property);
                    PositiveFloatValue v = (PositiveFloatValue) property.getScale().createValue();
                    if (property.getName().equals(MigrationResult.MIGRES_ELAPSED_TIME)) {
                        v.setValue(elapsed);
                        m.setValue(v);
                        result.getMeasurements().put(property.getName(), m);
                    } else if (property.getName().equals(MigrationResult.MIGRES_ELAPSED_TIME_PER_MB)) {
                        v.setValue(elapsedPerMB);
                        m.setValue(v);
                        result.getMeasurements().put(property.getName(), m);
                    } else if (property.getName().equals(MigrationResult.MIGRES_RELATIVE_FILESIZE)) {
                        v.setValue(((double) length) / data.length * 100);
                        m.setValue(v);
                        result.getMeasurements().put(property.getName(), m);
                    } else if (property.getName().equals(MigrationResult.MIGRES_RESULT_FILESIZE)) {
                        v.setValue((double) length);
                        m.setValue(v);
                        result.getMeasurements().put(property.getName(), m);
                    }
                }
            }
            success = r.isSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            cleanup(time, inputFile, outputFile);
        }
        return success;
    }

    /**
     * utility method to get the size in megabyte from a bytestream.
     * @param data
     * @return
     */
    protected double getMByte(byte[] data) {
        return ((double) data.length) / 1048576;
    }

    /**
     * deletes input and outputfile
     * @param time not used at the moment
     * @param inputFile absolute path of the input file 
     * @param outputFile absolute path of the input file
     */
    protected void cleanup(long time, String inputFile, String outputFile) {
        new File(inputFile).delete();
        new File(outputFile).delete();
    }

    /**
     * collects all available measurements and puts them into the provided
     * MigrationResult
     * @param config {@link ToolConfig} to use
     * @param time 
     * @param result here the measurements are deposited
     */
    protected void collectData(ToolConfig config, long time, MigrationResult result) {
        Machine m = ToolRegistry.getInstance().getMachine(machine);
        for (MeasurableProperty property : getMeasurableProperties()) {
            if (property.getName().startsWith("machine:")) {
                Measurement measurement = new Measurement();
                measurement.setProperty(property);
                FreeStringValue v = (FreeStringValue) property.getScale().createValue();
                if (property.getName().equals(Machine.MACHINE_NAME)) {
                    v.setValue(m.getId());
                } else if (property.getName().equals(Machine.MACHINE_OS)) {
                    v.setValue(m.getOperatingSystem());
                } else if (property.getName().equals(Machine.MACHINE_CPUS)) {
                    v.setValue(m.getCpus());
                } else if (property.getName().equals(Machine.MACHINE_CPUCLOCK)) {
                    v.setValue(m.getCpuClock());
                } else if (property.getName().equals(Machine.MACHINE_CPUTYPE)) {
                    v.setValue(m.getCpuType());
                } else if (property.getName().equals(Machine.MACHINE_MEMORY)) {
                    v.setValue(m.getMemory());
                }
                measurement.setValue(v);
                result.getMeasurements().put(property.getName(), measurement);
            }
        }
    }

    /**
     * creates an {@link IRunner} instance to be doing the job
     * @param command to be executed
     * @param config {@link ToolConfig} in use
     * @return an {@link IRunner}
     */
    protected IRunner makeRunner(String command, ToolConfig config) {
        DefaultRunner r = new DefaultRunner();
        r.setCommand(command);
        r.setWorkingDir(getTempDir());
        return r;
    }

    /**
     * constructs the command string to be executed
     * @param config {@link ToolConfig} to be used
     * @param inputFile absolute path of the input file
     * @param outputFile absolute path of the output file
     * @return the command string to be executed
     */
    protected String prepareCommand(ToolConfig config, String params, String inputFile, String outputFile, long time) throws Exception {
        StringBuffer cmd = new StringBuffer();
        cmd.append(config.getTool().getExecutablePath());
        cmd.append(" ");
        if (params != null && !"".equals(params)) {
            cmd.append(params).append(" ");
        }
        cmd.append(config.getParams()).append(" ");
        cmd.append(inputFile);
        if (!config.isNoOutFile()) {
            cmd.append(" ").append(outputFile);
        }
        String command = cmd.toString();
        command = command.replaceAll("%OUTFILE%", outputFile);
        return command;
    }

    /**    
     * this GENERATES the file name for the outputfile
     * @param config {@link ToolConfig} to be used
     * @param time 
     * @return
     */
    protected String makeOutputFilename(ToolConfig config, long time) {
        String outputFile = tempDir + "out" + time;
        if (config.getOutEnding() != null && !"".equals(config.getOutEnding())) {
            outputFile = tempDir + "in" + time + "." + config.getOutEnding();
        }
        return outputFile;
    }

    /**
     * This might actually DO something for preparing the outfile, but in the default implementation
     * it just delegates to the file name generating method
     * @param config
     * @param time
     * @return
     */
    protected String prepareOutputFile(ToolConfig config, long time) {
        return makeOutputFilename(config, time);
    }

    /**
     * writes the input bytestream to a file
     * @param data to be migrated
     * @param config {@link ToolConfig} for this migration run
     * @param time the identifier for this migration run
     * @return absolute path of the file that the bytestream has been written to
     * @throws FileNotFoundException if the constructed filename can't be found 
     * @throws IOException if anything else goes wrong with writing the file
     */
    protected String prepareInputFile(byte[] data, ToolConfig config, long time) {
        String inputFile = makeInputFilename(config, time);
        OutputStream in;
        try {
            in = new BufferedOutputStream(new FileOutputStream(inputFile));
            in.write(data);
            in.close();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return inputFile;
    }

    /**
     * @param config
     * @param time
     * @return absolute path for the input file
     */
    protected String makeInputFilename(ToolConfig config, long time) {
        String inputFile = tempDir + "in" + time;
        if (config.getInEnding() != null && !"".equals(config.getInEnding())) {
            inputFile = inputFile + "." + config.getInEnding();
        }
        return inputFile;
    }

    /**
     * creates a temporary working directory 
     * @param time to be used for naming the directory
     * @return the absolute path of the newly created working directory
     * @throws Exception if something goes wrong
     */
    protected String prepareWorkingDirectory(long time) throws Exception {
        File wDir = new File(getTempDir() + time);
        if (wDir.mkdir()) {
            return wDir.getAbsolutePath();
        } else {
            return "<failed to create working dir>";
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
