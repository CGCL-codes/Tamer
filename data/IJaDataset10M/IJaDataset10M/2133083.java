package uk.ac.ebi.intact.util.uniprotExport.writers.drlinewriters;

import uk.ac.ebi.intact.util.uniprotExport.parameters.drlineparameters.DRParameters;
import uk.ac.ebi.intact.util.uniprotExport.writers.WriterUtils;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Default writer for DR lines
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>28/01/11</pre>
 */
public class DRLineWriter1 implements DRLineWriter {

    /**
     * The writer
     */
    private OutputStreamWriter writer;

    public DRLineWriter1(OutputStreamWriter outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("You must give a non null OutputStream writer");
        }
        writer = outputStream;
    }

    @Override
    public void writeDRLine(DRParameters parameters) throws IOException {
        if (parameters != null) {
            writeDRTitle();
            writeDRLineParameters(parameters);
            writer.flush();
        }
    }

    /**
     * Write the content of a DR line
     * @param parameters
     */
    private void writeDRLineParameters(DRParameters parameters) throws IOException {
        writer.write(parameters.getUniprotAc());
        writer.write("; ");
        writer.write((parameters.getNumberOfInteractions() > 0 ? Integer.toString(parameters.getNumberOfInteractions()) : "-"));
        writer.write(".");
        writer.write(WriterUtils.NEW_LINE);
    }

    /**
     * Write the title of a DR line
     */
    private void writeDRTitle() throws IOException {
        writer.write("DR   IntAct; ");
    }

    @Override
    public void writeDRLines(List<DRParameters> DRLines) throws IOException {
        for (DRParameters parameter : DRLines) {
            writeDRLine(parameter);
        }
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }
}
