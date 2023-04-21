package uk.ac.ebi.pride.tools.ms2_parser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.pride.tools.braf.BufferedRandomAccessFile;
import uk.ac.ebi.pride.tools.jmzreader.JMzReader;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.jmzreader.model.IndexElement;
import uk.ac.ebi.pride.tools.jmzreader.model.Spectrum;
import uk.ac.ebi.pride.tools.jmzreader.model.impl.IndexElementImpl;
import uk.ac.ebi.pride.tools.ms2_parser.model.Ms2Spectrum;

/**
 * Represents a ms2 file. This parser is a read-only parser
 * and cannot write ms2 files.
 * @author jg
 *
 */
public class Ms2File implements JMzReader {

    /**
	 * The sourcefile this class represents.
	 */
    private File sourcefile;

    /**
	 * An array holding the position of every
	 * ms2 spectrum in the file.
	 */
    private List<IndexElement> index;

    /**
	 * A HashMap holding the file's header fields.
	 */
    private HashMap<String, String> header;

    /**
	 * Creates a Ms2File object based on the given
	 * source file.
	 * @param sourcefile The file to parse.
	 * @throws JMzReaderException 
	 */
    public Ms2File(File sourcefile) throws JMzReaderException {
        this.sourcefile = sourcefile;
        indexFile();
    }

    /**
	 * Reads a spectrum from a MS2 file who's
	 * position is already known.
	 * 
	 * @param sourcefile The file to read the spectrum from.
	 * @param indexElement The IndexElement specifying the position of the spectrum in the file.
	 * @return The unmarshalled Spectrum object.
	 * @throws JMzReaderException
	 */
    public static Spectrum getIndexedSpectrum(File sourcefile, IndexElement indexElement) throws JMzReaderException {
        if (sourcefile == null) throw new JMzReaderException("Invalid sourcefile parameter passed.");
        if (indexElement == null) throw new JMzReaderException("Required parameter indexElement missing.");
        return readIndexSpectrumFromFile(sourcefile, indexElement, 0);
    }

    /**
	 * Index the current sourcefile. This function
	 * parses the current sourcefile and puts the
	 * offset of every spectrum into the fileIndex
	 * ArrayList. The position in the array corresponds
	 * to the spectrum's position in the file.
	 * Additionally, this function also parses the header.
	 * @throws JMzReaderException 
	 */
    private void indexFile() throws JMzReaderException {
        index = new ArrayList<IndexElement>();
        header = new HashMap<String, String>();
        try {
            BufferedRandomAccessFile reader = new BufferedRandomAccessFile(sourcefile, "r", 1024 * 1000);
            String line = "";
            Long lineOffset = 0L;
            Long lastSpecOffset = null;
            boolean inHeader = true;
            while ((line = reader.getNextLine()) != null) {
                if (inHeader && !line.startsWith("H")) inHeader = false;
                if (inHeader) {
                    String[] fields = line.split("\t");
                    if (fields.length < 2 || fields.length > 3) throw new JMzReaderException("Invalid header line encountered: '" + line + "'");
                    if (header.containsKey(fields[1])) {
                        int nNumber = 1;
                        while (header.containsKey(fields[1] + "_" + nNumber)) {
                            nNumber++;
                        }
                        fields[1] = fields[1] + "_" + nNumber;
                    }
                    header.put(fields[1], (fields.length == 3) ? fields[2] : "");
                }
                if (!inHeader && line.startsWith("S")) {
                    if (lastSpecOffset != null) {
                        int size = (int) (lineOffset - lastSpecOffset);
                        index.add(new IndexElementImpl(lastSpecOffset, size));
                    }
                    lastSpecOffset = lineOffset;
                }
                lineOffset = reader.getFilePointer();
            }
            if (lastSpecOffset != null) {
                int size = (int) (lineOffset - lastSpecOffset);
                index.add(new IndexElementImpl(lastSpecOffset, size));
            }
            reader.close();
        } catch (IOException e) {
            throw new JMzReaderException("Failed to read from file.", e);
        }
    }

    /**
	 * Returns the header information from the given ms2 file
	 * as a HashMap where the field's name is the key and its value
	 * the value. In case a field name is used multiple times, 
	 * subsequent values are stored under the key [fieldname]_[1-n].
	 * F.e. if "Comments" is used twice, the first comment is stored
	 * with "Comment" as field name, the second one with "Comment_1". 
	 * For the required header fields special getter functions exist.
	 * Nevertheless, these are also included in the returned HashMap.
	 * @return The header information as a HashMap
	 */
    public HashMap<String, String> getHeader() {
        return header;
    }

    /**
	 * Returns the number of spectra in the file.
	 * @return
	 */
    public int getSpectraCount() {
        return index.size();
    }

    /**
	 * The creation date set in the header. This is
	 * a required header field.
	 * @return
	 */
    public String getCreationDate() {
        return header.get("CreationDate");
    }

    /**
	 * The extractor used to generate the field list. This is
	 * a required header field.
	 * @return
	 */
    public String getExtractor() {
        return header.get("Extractor");
    }

    /**
	 * The required extractor's version.
	 * @return
	 */
    public String getExtractorVersion() {
        return header.get("ExtractorVersion");
    }

    /**
	 * The required extractor's options.
	 * @return
	 */
    public String getExtractorOptions() {
        return header.get("ExtractorOptions");
    }

    /**
	 * Returns the spectrum identified with it's
	 * 1-based index in the file.
	 * @param nIndex The 1-based index of the spectrum in the file.
	 * @return The respective Ms2Spectrum object.
	 * @throws JMzReaderException 
	 */
    public Ms2Spectrum getSpectrum(int nIndex) throws JMzReaderException {
        return readSpectrumFromFile(nIndex - 1);
    }

    /**
	 * Reads a spectrum from the (indexed) file.
	 * @param specIndex The 0-based index of the spectrum in the file.
	 * @param file A random access file object to read from. If null is passed a new RandomAccessFile is created.
	 * @return The Ms2Spectrum object
	 * @throws JMzReaderException 
	 */
    private Ms2Spectrum readSpectrumFromFile(int specIndex) throws JMzReaderException {
        if (specIndex < 0 || specIndex >= index.size()) throw new JMzReaderException("Invalid spectrum index passed.");
        IndexElement indexElement = index.get(specIndex);
        return readIndexSpectrumFromFile(sourcefile, indexElement, specIndex);
    }

    /**
	 * Reads a spectrum from the passed file who's position
	 * in the file is known.
	 * @param file The file to read the spectrum from.
	 * @param specIndex The 0-based index of the spectrum in the file.
	 * @return The Ms2Spectrum object
	 * @throws JMzReaderException 
	 */
    private static Ms2Spectrum readIndexSpectrumFromFile(File file, IndexElement indexElement, int specIndex) throws JMzReaderException {
        if (indexElement == null) throw new JMzReaderException("Invalid spectrum index passed.");
        try {
            RandomAccessFile inputFile = new RandomAccessFile(file, "r");
            inputFile.seek(indexElement.getStart());
            byte[] bytes = new byte[indexElement.getSize()];
            inputFile.read(bytes);
            inputFile.close();
            return new Ms2Spectrum(new String(bytes), specIndex + 1);
        } catch (IOException e) {
            throw new JMzReaderException("Failed to read from file.", e);
        }
    }

    /**
	 * Returns an iterator over all spectra.
	 * @return
	 * @throws JMzReaderException 
	 */
    public Iterator<Ms2Spectrum> getMs2SpectrumIterator() throws JMzReaderException {
        return new Ms2FileSpectrumIterator();
    }

    private class SpectrumIterator implements Iterator<Spectrum> {

        private Ms2FileSpectrumIterator it;

        public SpectrumIterator() throws JMzReaderException {
            it = new Ms2FileSpectrumIterator();
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Spectrum next() {
            return it.next();
        }

        public void remove() {
            it.remove();
        }
    }

    /**
	 * An iterator over all spectra in a ms2 file.
	 * @author jg
	 *
	 */
    private class Ms2FileSpectrumIterator implements Iterator<Ms2Spectrum>, Iterable<Ms2Spectrum> {

        /**
		 * The current spectrum
		 */
        int nCurrentSpectrum = 0;

        public Ms2FileSpectrumIterator() {
        }

        public Iterator<Ms2Spectrum> iterator() {
            return this;
        }

        public boolean hasNext() {
            return nCurrentSpectrum < index.size();
        }

        public Ms2Spectrum next() {
            try {
                return readSpectrumFromFile(nCurrentSpectrum++);
            } catch (JMzReaderException e) {
                throw new IllegalStateException("Failed to parse spectrum.", e);
            }
        }

        public void remove() {
        }
    }

    public boolean acceptsFile() {
        return true;
    }

    public boolean acceptsDirectory() {
        return false;
    }

    public List<String> getSpectraIds() {
        List<String> ids = new ArrayList<String>(getSpectraCount());
        for (Integer id = 1; id <= getSpectraCount(); id++) ids.add(id.toString());
        return ids;
    }

    public Spectrum getSpectrumById(String id) throws JMzReaderException {
        Integer index = Integer.parseInt(id);
        return getSpectrum(index);
    }

    public Spectrum getSpectrumByIndex(int index) throws JMzReaderException {
        return getSpectrum(index);
    }

    @Override
    public List<IndexElement> getMsNIndexes(int msLevel) {
        if (msLevel != 2) return Collections.emptyList();
        return new ArrayList<IndexElement>(index);
    }

    @Override
    public List<Integer> getMsLevels() {
        List<Integer> msLevels = new ArrayList<Integer>(2);
        msLevels.add(2);
        return msLevels;
    }

    @Override
    public Map<String, IndexElement> getIndexElementForIds() {
        Map<String, IndexElement> idToIndexMap = new HashMap<String, IndexElement>(index.size());
        for (Integer i = 1; i <= index.size(); i++) idToIndexMap.put(i.toString(), index.get(i - 1));
        return idToIndexMap;
    }

    public Iterator<Spectrum> getSpectrumIterator() {
        try {
            return new SpectrumIterator();
        } catch (JMzReaderException e) {
            throw new IllegalStateException(e);
        }
    }
}
