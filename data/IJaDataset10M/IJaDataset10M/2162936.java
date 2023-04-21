package uk.ac.ebi.intact.dataexchange.imex.idassigner.report;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Report writer interface.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.1.1
 */
public interface ReportWriter extends Flushable, Closeable {

    void writeHeaderIfNecessary(String... colHeaderTexts) throws IOException;

    void writeColumnValues(String... colValues) throws IOException;

    void writeLine(String str) throws IOException;
}
