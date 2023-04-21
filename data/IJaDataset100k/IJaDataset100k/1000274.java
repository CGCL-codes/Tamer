package unclej.log;

import unclej.framework.UTargetSpec;
import java.io.Serializable;
import java.util.logging.Level;

/**
 * Creates {@link ULog}s as necessary. Although it cannot be specified in the interface, each <tt>ULogFactory</tt>
 * implementation must also have a public, no-argument constructor.
 * @author scottv
 */
public interface ULogFactory extends Serializable {

    /**
   * Retrieves the "root" log, to be used by the framework when it is outside a target. This log is also the parent
   * of target logs in that its {@link ULog#setLevel} and {@link ULog#addOutput} methods will affect all target logs
   * as well.  It is recommended that initially the root log should have no outputs and filter at the <tt>INFO</tt>
   * level.
   * @return the root log
   */
    ULog getRootLog();

    /**
   * Get a ULog for target-level output.  It should have its own level filter setting, and two calls to this method
   * with the same project/target name combination should produce the same ULog.  All other implications of
   * "target-level output" are up to the implementation.
   * @param targetSpec identifies the target
   * @return a unique ULog for the project/target combination
   */
    ULog getTargetLog(UTargetSpec targetSpec);

    /**
   * Captures <tt>System.out</tt> to a log by using {@link System#setOut(java.io.PrintStream)}. (The log replaces the
   * original stream.) It is the caller's responsibility to restore the original stream if desired.
   * @param level the logging level for captured output
   */
    void captureSystemOut(Level level);

    /**
   * Capture <tt>System.err</tt> to a log by using {@link System#setErr(java.io.PrintStream)}. (The log replaces the
   * original stream.) It is the caller's responsibility to restore the original stream if desired.
   * @param level the logging level for captured output
   */
    void captureSystemErr(Level level);
}
