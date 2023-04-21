package uk.ac.shef.wit.trex.util.logger;

import uk.ac.shef.wit.trex.util.Util;
import java.io.PrintStream;

/**
 * An observer that logs application's activity to a PrintStream, outputing some profiling info as well.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LoggerProfiler extends LoggerPlain {

    long _time[] = new long[100];

    public LoggerProfiler(final PrintStream out) {
        super(out);
    }

    public void start(final int level, final int numSteps, final String title) {
        super.start(level, numSteps, title);
        _time[level] = System.currentTimeMillis();
    }

    public void finish(final int level) {
        super.finish(level);
        for (int i = 0; i < level * DISPLACEMENT_UNIT; ++i) _out.print(" ");
        _out.println(new StringBuffer().append("> ").append("total time: ").append(Util.getPrettyTime(System.currentTimeMillis() - _time[level])).append(", ").append("available memory: ").append((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) / 1048576).append("Mb").toString());
    }
}
