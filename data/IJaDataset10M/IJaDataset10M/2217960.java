package ca.etsmtl.ihe.logger;

import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;

/**
 * Define a HL7 error log level.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class HL7error extends Level {

    private static final long serialVersionUID = 1L;

    /** HL7Error Level Integer Values */
    public static final int HL7ERROR_INT = FATAL_INT + 10000;

    /** HL7Error Level Values */
    public static final Level HL7error = new HL7error(HL7ERROR_INT, "HL7error", SyslogAppender.LOG_LOCAL0);

    /**
   * Constructor
   *
   * @param level
   * @param levelStr
   * @param syslogEquivalent
   */
    public HL7error(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
}
