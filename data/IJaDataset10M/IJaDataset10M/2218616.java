package au.gov.naa.digipres.xena.kernel.batchfilter;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.xml.sax.XMLReader;
import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.XenaInputSource;
import au.gov.naa.digipres.xena.kernel.normalise.NormaliserResults;
import au.gov.naa.digipres.xena.kernel.type.Type;

/**
 * Some kinds of input files gather in other input files. An example is TRIM
 * email directories, where the trim email file refers to other files in the
 * directory. This class can be overridden by plugins to filter out these
 * extra files from the regular batch processing so that they can be handled
 * specially by the appropriate plugin. This means that say, an MS-Word document
 * that belongs as an attachment to an email, will only be normalised once
 * within the email, instead of being normalised a second time as an individual
 * file.
 *
 * @see BatchFilterManager
 */
public abstract class BatchFilter {

    public abstract Map<String, FileAndType> filter(Map<String, FileAndType> files) throws XenaException;

    public abstract Map<XenaInputSource, NormaliserResults> getChildren(Collection<XenaInputSource> xisColl) throws XenaException;

    /**
	 * An Iterator version of this method is required in order to be able handle a massive set of of objects 
	 * (eg by using a scrollable results set from a database).
	 * 
	 * @param xisIter
	 * @return
	 * @throws XenaException
	 */
    public abstract Map<XenaInputSource, NormaliserResults> getChildren(Iterator<XenaInputSource> xisIter) throws XenaException;

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }

    public static class FileAndType {

        File file;

        Type type;

        XMLReader normaliser;

        public FileAndType(File file, Type guess, XMLReader normaliser) {
            this.file = file;
            type = guess;
            this.normaliser = normaliser;
        }

        public File getFile() {
            return file;
        }

        public Type getType() {
            return type;
        }

        public XMLReader getNormaliser() {
            return normaliser;
        }
    }
}
