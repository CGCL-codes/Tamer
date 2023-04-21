package net.sf.logdistiller.plugins;

import java.io.*;
import net.sf.logdistiller.LogDistillation;
import net.sf.logdistiller.LogDistiller;
import net.sf.logdistiller.LogEvent;
import net.sf.logdistiller.Plugin;
import net.sf.logdistiller.ReportFormat;
import net.sf.logdistiller.util.FormatUtil;

/**
 * <b>Log sampling</b> plugin: works like FreqPlugin, but saves some log events to a log file.
 * <p>
 * Parameters (added to FreqPlugin's parameters):
 * </p>
 * <ul>
 * <li><code>sampling.filename</code> (default: <code>[id]-sampling.log</code>): name of the file to save the sample of
 * log events for each attribute's value.</li>
 * <li><code>sampling.maxCount</code> (default: 10, -1 = no limit): number of log events to save for each attribute's
 * value.</li>
 * <li><code>sampling.maxSize</code> (default: -1 = no limit): maximum size (in kb) of the sample file</li>
 * </ul>
 *
 * @see FreqPlugin
 * @since 0.7
 */
public class SamplingPlugin extends FreqPlugin {

    private static final long serialVersionUID = -2960554427849032861L;

    public static final String ID = "sampling";

    public static final Plugin TYPE = new Type();

    private final int samplingMaxCount;

    private final long samplingMaxSize;

    private final String samplingFilename;

    private File samplingLogFile;

    private transient PrintStream samplingLogStream;

    public SamplingPlugin(LogDistiller.Plugin definition) {
        super(definition, false);
        samplingMaxCount = Integer.parseInt(definition.getParam("sampling.maxCount", "10"));
        samplingMaxSize = Long.parseLong(definition.getParam("sampling.maxSize", "-1")) * 1024;
        samplingFilename = definition.getParam("sampling.filename", definition.getGroup().getId() + "-sampling.log");
    }

    public void begin(File destinationDirectory) throws FileNotFoundException {
        super.begin(destinationDirectory);
        samplingLogFile = new File(destinationDirectory, samplingFilename);
    }

    protected PrintStream getSamplingLogStream() throws FileNotFoundException {
        if (samplingLogStream == null) {
            samplingLogStream = new PrintStream(new FileOutputStream(samplingLogFile));
        }
        return samplingLogStream;
    }

    public void addLogEventToFreq(LogEvent logEvent, Freq freq) throws IOException {
        super.addLogEventToFreq(logEvent, freq);
        if (((samplingMaxCount < 0) || ((samplingMaxCount > 0) && (freq.count <= samplingMaxCount))) && ((samplingMaxSize < 0) || ((samplingMaxSize > 0) && (freq.bytes <= samplingMaxSize)))) {
            getSamplingLogStream().println(logEvent.getRawLog());
        }
    }

    public void end() throws IOException {
        if (samplingLogStream != null) {
            samplingLogStream.close();
        }
        super.end();
    }

    protected void appendLinkToFreqReport(ReportFormat.PluginReport report) {
        report.addLink(samplingLogFile.getName(), "sampling log file (max count = " + ((samplingMaxCount < 0) ? "none" : String.valueOf(samplingMaxCount)) + ", max size = " + ((samplingMaxSize < 0) ? "none" : FormatUtil.formatSize(samplingMaxSize)) + ")");
    }

    private static class Type extends Plugin {

        public Type() {
            super(ID);
        }

        public LogDistillation.Plugin newInstance(LogDistiller.Plugin conf) {
            return new SamplingPlugin(conf);
        }
    }
}
