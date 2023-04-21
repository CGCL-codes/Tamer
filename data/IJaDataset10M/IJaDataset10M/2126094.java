package uk.org.ponder.genetic;

import java.io.FileOutputStream;
import java.io.PrintStream;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.FilenameUtil;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * The class 
 * 
 * @author Bosmon
 */
public class StreamPackage {

    public PrintOutputStream transcript;

    public PrintStream fststream;

    public PrintStream iststream;

    static String deriveFilename(String transcriptfilename, String ext) {
        return FilenameUtil.getStem(transcriptfilename) + ext;
    }

    public void open(String transcriptname, PrintOutputStream transcript) {
        String fstname = deriveFilename(transcriptname, ".fst");
        String istname = deriveFilename(transcriptname, ".ist");
        try {
            fststream = new PrintStream(new FileOutputStream(fstname));
        } catch (Exception e) {
            throw UniversalRuntimeException.accumulate(e, "Couldn't open file " + fstname + " for writing");
        }
        try {
            iststream = new PrintStream(new FileOutputStream(istname));
        } catch (Exception e) {
            fststream.close();
            throw UniversalRuntimeException.accumulate(e, "Couldn't open file " + istname + " for writing");
        }
        this.transcript = transcript;
    }

    public void close() {
        fststream.close();
        iststream.close();
        transcript.close();
    }
}
