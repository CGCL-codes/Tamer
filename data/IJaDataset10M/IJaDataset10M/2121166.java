package org.statefive.feedstate.demo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author rich
 */
public class RegexWriter extends AbstractThreadedDemoWriter {

    /** How this writer formats it's dates. */
    public static final String DATE_FORMAT = "yyMMddhhmmss";

    /** The output stream the writer will write to. */
    private OutputStream os;

    /**
   * Creates a new XML writer.
   *
   * @param os the output stream to write to; must not be <tt>null</tt>.
   */
    public RegexWriter(OutputStream os, int maxDelay) {
        super(maxDelay);
        this.os = os;
    }

    /**
   *
   * @throws IOException
   */
    @Override
    public void write() throws IOException {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(RegexWriter.DATE_FORMAT);
        String data = "seq: " + (count) + " " + sdf.format(date) + " " + getLevel() + " message here...\n";
        os.write(data.getBytes());
    }

    /**
   * Gets a randomly generated level.
   * @return
   */
    protected String getLevel() {
        Random random = new Random();
        final String[] LEVELS = new String[] { "ERROR", "WARN", "INFO", "DEBUG" };
        return LEVELS[Math.abs(random.nextInt() % 4)];
    }
}
