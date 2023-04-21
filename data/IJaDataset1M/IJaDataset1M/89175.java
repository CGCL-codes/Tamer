package org.ais.convert;

import java.io.PrintWriter;

/**
 * Reverse to RawReader (i.e. serializer of RawRecords). Convenience class. 
 */
public class RawWriter {

    private static final String DELIMITER = "\t";

    /**
	 * For now - just static. Then - when we'll need more functionality - we'll
	 * do as an object.
	 */
    public static void write(RawRecord record, PrintWriter out) throws Exception {
        out.print(record.snip);
        out.print(DELIMITER);
        out.print(record.chromosome);
        out.print(DELIMITER);
        out.print(record.position);
        out.print(DELIMITER);
        out.print(record.base);
        out.println();
    }

    /**
	 * Convenience method
	 */
    public static void writeCommented(RawRecord record, PrintWriter out) throws Exception {
        out.print("#");
        out.print(record.snip);
        out.print(DELIMITER);
        out.print(record.chromosome);
        out.print(DELIMITER);
        out.print(record.position);
        out.print(DELIMITER);
        out.print(record.base);
        out.println();
    }

    /**
	 * Convenience method
	 */
    public static void writeCommented(RawRecord record, RawRecord record2, PrintWriter out) throws Exception {
        out.print("#");
        out.print(record.snip);
        out.print(DELIMITER);
        out.print(record.chromosome);
        out.print(DELIMITER);
        out.print(record.position);
        out.print(DELIMITER);
        out.print(record.base);
        out.print(DELIMITER);
        out.print(record2.base);
        out.println();
    }
}
