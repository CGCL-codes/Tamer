package gov.nist.atlas.io;

import gov.nist.atlas.Corpus;
import gov.nist.atlas.util.LogManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;

/**
 * This abstract exporter implement the handling of a single export.
 * You can export to a URL or a File (the URL will be transform to a file)
 *
 * @author <a href='mailto:nradde@nist.gov'>Nicolas Radde</a>
 * @author <a href='ubik@users.sf.net'>Chris Laprun</a>
 * @version $Revision: 1.6 $
 */
public abstract class SingleURLExport extends AbstractATLASExport {

    public void save(Corpus corpus, Object destination) throws ATLASIOException {
        URL whereToSave = null;
        whereToSave = URIManager.getAsURL(destination);
        File out = new File(whereToSave.getFile());
        File backup = null;
        if (out.isFile()) {
            backup = new File(out.getAbsolutePath() + ".old");
            out.renameTo(backup);
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(out)));
            write(corpus, writer);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            LogManager.log(this, LogManager.ERROR, "Impossible to save file (" + destination.toString() + ") the exporter have a probleme to access to the file.");
            if (backup != null) {
                LogManager.log(this, LogManager.INFO, "Restore old file");
                backup.renameTo(out);
            }
            throw new RuntimeException("Impossible to write to the file (" + destination.toString() + ")");
        }
    }

    public boolean canHandleLocation(Object location) {
        return URIManager.getAsURL(location) != null;
    }

    /**
   * Writes the given corpus into this XMLExport's associated output file,
   * using the specified writer
   * This method is used by the <CODE>save</CODE> method
   *
   * @param corpus - The Corpus to export
   * @param writer - The Writer to use
   * @throws ATLASIOException if an probleme occure during the printing of the file
   */
    protected abstract void write(Corpus corpus, Writer writer) throws ATLASIOException;
}
