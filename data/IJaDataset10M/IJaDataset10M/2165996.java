package uk.ac.ebi.intact.confidence.model.io;

import uk.ac.ebi.intact.confidence.model.ProteinSimplified;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Writer strategy for ProteinSimplified objects.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version $Id$
 * @since 1.6.0
 *        <pre>
 *        13-Dec-2007
 *        </pre>
 */
public interface ProteinSimplifiedWriter {

    void append(ProteinSimplified protein, File outFile) throws IOException;

    void append(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void write(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void appendGO(ProteinSimplified protein, File outFile) throws IOException;

    void appendGO(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void writeGO(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void appendIp(ProteinSimplified protein, File outFile) throws IOException;

    void appendIp(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void writeIp(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void appendSeq(ProteinSimplified protein, File outFile) throws IOException;

    void appendSeq(List<ProteinSimplified> proteins, File outFile) throws IOException;

    void writeSeq(List<ProteinSimplified> proteins, File outFile) throws IOException;
}
