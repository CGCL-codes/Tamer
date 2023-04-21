package loci.formats;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * ImageReader is the master file format reader for all supported formats.
 * It uses one instance of each reader subclass (specified in readers.txt,
 * or other class list source) to identify file formats and read data.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/loci/formats/ImageReader.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/loci/formats/ImageReader.java">SVN</a></dd></dl>
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 */
public class ImageReader implements IFormatReader {

    /** Default list of reader classes, for use with noargs constructor. */
    private static ClassList defaultClasses;

    private static ClassList getDefaultReaderClasses() {
        if (defaultClasses == null) {
            try {
                defaultClasses = new ClassList("readers.txt", IFormatReader.class);
            } catch (IOException exc) {
                defaultClasses = new ClassList(IFormatReader.class);
                LogTools.trace(exc);
            }
        }
        return defaultClasses;
    }

    /** List of supported file format readers. */
    private IFormatReader[] readers;

    /**
   * Valid suffixes for this file format.
   * Populated the first time getSuffixes() is called.
   */
    private String[] suffixes;

    /** Name of current file. */
    private String currentId;

    /** Current form index. */
    private int current;

    /**
   * Constructs a new ImageReader with the default
   * list of reader classes from readers.txt.
   */
    public ImageReader() {
        this(getDefaultReaderClasses());
    }

    /** Constructs a new ImageReader from the given list of reader classes. */
    public ImageReader(ClassList classList) {
        Vector v = new Vector();
        Class[] c = classList.getClasses();
        for (int i = 0; i < c.length; i++) {
            IFormatReader reader = null;
            try {
                reader = (IFormatReader) c[i].newInstance();
            } catch (IllegalAccessException exc) {
            } catch (InstantiationException exc) {
            }
            if (reader == null) {
                LogTools.println("Error: " + c[i].getName() + " cannot be instantiated.");
                continue;
            }
            v.add(reader);
        }
        readers = new IFormatReader[v.size()];
        v.copyInto(readers);
    }

    /** Gets a string describing the file format for the given file. */
    public String getFormat(String id) throws FormatException, IOException {
        return getReader(id).getFormat();
    }

    /** Gets the reader used to open the given file. */
    public IFormatReader getReader(String id) throws FormatException, IOException {
        if (!id.equals(currentId)) {
            boolean success = false;
            for (int i = 0; i < readers.length; i++) {
                if (readers[i].isThisType(id)) {
                    current = i;
                    currentId = id;
                    success = true;
                    break;
                }
            }
            if (!success) throw new FormatException("Unknown file format: " + id);
        }
        return readers[current];
    }

    /** Gets the reader used to open the current file. */
    public IFormatReader getReader() {
        return readers[current];
    }

    /** Gets the file format reader instance matching the given class. */
    public IFormatReader getReader(Class c) {
        for (int i = 0; i < readers.length; i++) {
            if (readers[i].getClass().equals(c)) return readers[i];
        }
        return null;
    }

    /** Gets all constituent file format readers. */
    public IFormatReader[] getReaders() {
        IFormatReader[] r = new IFormatReader[readers.length];
        System.arraycopy(readers, 0, r, 0, readers.length);
        return r;
    }

    public boolean isThisType(byte[] block) {
        for (int i = 0; i < readers.length; i++) {
            if (readers[i].isThisType(block)) return true;
        }
        return false;
    }

    public int getImageCount() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getImageCount();
    }

    public boolean isRGB() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isRGB();
    }

    public int getSizeX() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSizeX();
    }

    public int getSizeY() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSizeY();
    }

    public int getSizeC() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSizeC();
    }

    public int getSizeZ() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSizeZ();
    }

    public int getSizeT() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSizeT();
    }

    public int getPixelType() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getPixelType();
    }

    public int getEffectiveSizeC() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getEffectiveSizeC();
    }

    public int getRGBChannelCount() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getRGBChannelCount();
    }

    public boolean isIndexed() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isIndexed();
    }

    public boolean isFalseColor() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isFalseColor();
    }

    public byte[][] get8BitLookupTable() throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().get8BitLookupTable();
    }

    public short[][] get16BitLookupTable() throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().get16BitLookupTable();
    }

    public int[] getChannelDimLengths() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getChannelDimLengths();
    }

    public String[] getChannelDimTypes() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getChannelDimTypes();
    }

    public int getThumbSizeX() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getThumbSizeX();
    }

    public int getThumbSizeY() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getThumbSizeY();
    }

    public boolean isLittleEndian() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isLittleEndian();
    }

    public String getDimensionOrder() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getDimensionOrder();
    }

    public boolean isOrderCertain() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isOrderCertain();
    }

    public boolean isInterleaved() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isInterleaved();
    }

    public boolean isInterleaved(int subC) {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isInterleaved(subC);
    }

    public BufferedImage openImage(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().openImage(no);
    }

    public byte[] openBytes(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().openBytes(no);
    }

    public byte[] openBytes(int no, byte[] buf) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().openBytes(no, buf);
    }

    public BufferedImage openThumbImage(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().openThumbImage(no);
    }

    public byte[] openThumbBytes(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 2);
        return getReader().openThumbBytes(no);
    }

    public int getSeriesCount() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getSeriesCount();
    }

    public void setSeries(int no) {
        FormatTools.assertId(currentId, true, 2);
        getReader().setSeries(no);
    }

    public int getSeries() {
        return getReader().getSeries();
    }

    public String[] getUsedFiles() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getUsedFiles();
    }

    public int getIndex(int z, int c, int t) {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getIndex(z, c, t);
    }

    public int[] getZCTCoords(int index) {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getZCTCoords(index);
    }

    public Object getMetadataValue(String field) {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getMetadataValue(field);
    }

    public Hashtable getMetadata() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getMetadata();
    }

    public CoreMetadata getCoreMetadata() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getCoreMetadata();
    }

    public void close(boolean fileOnly) throws IOException {
        for (int i = 0; i < readers.length; i++) readers[i].close(fileOnly);
    }

    public void setGroupFiles(boolean group) {
        FormatTools.assertId(currentId, false, 2);
        for (int i = 0; i < readers.length; i++) readers[i].setGroupFiles(group);
    }

    public boolean isGroupFiles() {
        return getReader().isGroupFiles();
    }

    public int fileGroupOption(String id) throws FormatException, IOException {
        return getReader(id).fileGroupOption(id);
    }

    public boolean isMetadataComplete() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().isMetadataComplete();
    }

    public void setNormalized(boolean normalize) {
        FormatTools.assertId(currentId, false, 2);
        for (int i = 0; i < readers.length; i++) readers[i].setNormalized(normalize);
    }

    public boolean isNormalized() {
        return readers[0].isNormalized();
    }

    public void setMetadataCollected(boolean collect) {
        FormatTools.assertId(currentId, false, 2);
        for (int i = 0; i < readers.length; i++) {
            readers[i].setMetadataCollected(collect);
        }
    }

    public boolean isMetadataCollected() {
        return readers[0].isMetadataCollected();
    }

    public void setOriginalMetadataPopulated(boolean populate) {
        FormatTools.assertId(currentId, false, 1);
        for (int i = 0; i < readers.length; i++) {
            readers[i].setOriginalMetadataPopulated(populate);
        }
    }

    public boolean isOriginalMetadataPopulated() {
        return readers[0].isOriginalMetadataPopulated();
    }

    public String getCurrentFile() {
        return getReader().getCurrentFile();
    }

    public void setMetadataFiltered(boolean filter) {
        FormatTools.assertId(currentId, false, 2);
        for (int i = 0; i < readers.length; i++) readers[i].setMetadataFiltered(filter);
    }

    public boolean isMetadataFiltered() {
        return readers[0].isMetadataFiltered();
    }

    public void setMetadataStore(MetadataStore store) {
        FormatTools.assertId(currentId, false, 2);
        for (int i = 0; i < readers.length; i++) readers[i].setMetadataStore(store);
    }

    public MetadataStore getMetadataStore() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getMetadataStore();
    }

    public Object getMetadataStoreRoot() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getMetadataStoreRoot();
    }

    public boolean isThisType(String name) {
        return isThisType(name, false);
    }

    public boolean isThisType(String name, boolean open) {
        for (int i = 0; i < readers.length; i++) {
            if (readers[i].isThisType(name, open)) return true;
        }
        return false;
    }

    public String getFormat() {
        return getReader().getFormat();
    }

    public String[] getSuffixes() {
        if (suffixes == null) {
            HashSet suffixSet = new HashSet();
            for (int i = 0; i < readers.length; i++) {
                String[] suf = readers[i].getSuffixes();
                for (int j = 0; j < suf.length; j++) suffixSet.add(suf[j]);
            }
            suffixes = new String[suffixSet.size()];
            suffixSet.toArray(suffixes);
            Arrays.sort(suffixes);
        }
        return suffixes;
    }

    public void setId(String id) throws FormatException, IOException {
        getReader(id).setId(id);
    }

    public void setId(String id, boolean force) throws FormatException, IOException {
        getReader(id).setId(id, force);
    }

    public void close() throws IOException {
        currentId = null;
        for (int i = 0; i < readers.length; i++) readers[i].close();
    }

    public void addStatusListener(StatusListener l) {
        for (int i = 0; i < readers.length; i++) readers[i].addStatusListener(l);
    }

    public void removeStatusListener(StatusListener l) {
        for (int i = 0; i < readers.length; i++) readers[i].removeStatusListener(l);
    }

    public StatusListener[] getStatusListeners() {
        return readers[0].getStatusListeners();
    }

    /** @deprecated Replaced by {@link #getImageCount()} */
    public int getImageCount(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getImageCount();
    }

    /** @deprecated Replaced by {@link #isRGB()} */
    public boolean isRGB(String id) throws FormatException, IOException {
        setId(id);
        return getReader().isRGB();
    }

    /** @deprecated Replaced by {@link #getSizeX()} */
    public int getSizeX(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSizeX();
    }

    /** @deprecated Replaced by {@link #getSizeY()} */
    public int getSizeY(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSizeY();
    }

    /** @deprecated Replaced by {@link #getSizeZ()} */
    public int getSizeZ(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSizeZ();
    }

    /** @deprecated Replaced by {@link #getSizeC()} */
    public int getSizeC(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSizeC();
    }

    /** @deprecated Replaced by {@link #getSizeT()} */
    public int getSizeT(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSizeT();
    }

    /** @deprecated Replaced by {@link #getPixelType()} */
    public int getPixelType(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getPixelType();
    }

    /** @deprecated Replaced by {@link #getEffectiveSizeC()} */
    public int getEffectiveSizeC(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getEffectiveSizeC();
    }

    /** @deprecated Replaced by {@link #getRGBChannelCount()} */
    public int getRGBChannelCount(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getRGBChannelCount();
    }

    /** @deprecated Replaced by {@link #getChannelDimLengths()} */
    public int[] getChannelDimLengths(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getChannelDimLengths();
    }

    /** @deprecated Replaced by {@link #getChannelDimTypes()} */
    public String[] getChannelDimTypes(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getChannelDimTypes();
    }

    /** @deprecated Replaced by {@link #getThumbSizeX()} */
    public int getThumbSizeX(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getThumbSizeX();
    }

    /** @deprecated Replaced by {@link #getThumbSizeY()} */
    public int getThumbSizeY(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getThumbSizeY();
    }

    /** @deprecated Replaced by {@link #isLittleEndian()} */
    public boolean isLittleEndian(String id) throws FormatException, IOException {
        setId(id);
        return getReader().isLittleEndian();
    }

    /** @deprecated Replaced by {@link #getDimensionOrder()} */
    public String getDimensionOrder(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getDimensionOrder();
    }

    /** @deprecated Replaced by {@link #isOrderCertain()} */
    public boolean isOrderCertain(String id) throws FormatException, IOException {
        setId(id);
        return getReader().isOrderCertain();
    }

    /** @deprecated Replaced by {@link #isInterleaved()} */
    public boolean isInterleaved(String id) throws FormatException, IOException {
        setId(id);
        return getReader().isInterleaved();
    }

    /** @deprecated Replaced by {@link #isInterleaved(int)} */
    public boolean isInterleaved(String id, int subC) throws FormatException, IOException {
        setId(id);
        return getReader().isInterleaved(subC);
    }

    /** @deprecated Replaced by {@link #openImage(int)} */
    public BufferedImage openImage(String id, int no) throws FormatException, IOException {
        setId(id);
        return getReader().openImage(no);
    }

    /** @deprecated Replaced by {@link #openBytes(int)} */
    public byte[] openBytes(String id, int no) throws FormatException, IOException {
        setId(id);
        return getReader().openBytes(no);
    }

    /** @deprecated Replaced by {@link #openBytes(int, byte[])} */
    public byte[] openBytes(String id, int no, byte[] buf) throws FormatException, IOException {
        setId(id);
        return getReader().openBytes(no, buf);
    }

    /** @deprecated Replaced by {@link #openThumbImage(int)} */
    public BufferedImage openThumbImage(String id, int no) throws FormatException, IOException {
        setId(id);
        return getReader().openThumbImage(no);
    }

    /** @deprecated Replaced by {@link #openThumbBytes(int)} */
    public byte[] openThumbBytes(String id, int no) throws FormatException, IOException {
        setId(id);
        return getReader().openThumbBytes(no);
    }

    /** @deprecated Replaced by {@link #getSeriesCount()} */
    public int getSeriesCount(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSeriesCount();
    }

    /** @deprecated Replaced by {@link #setSeries(int)} */
    public void setSeries(String id, int no) throws FormatException, IOException {
        setId(id);
        getReader().setSeries(no);
    }

    /** @deprecated Replaced by {@link #getSeries()} */
    public int getSeries(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getSeries();
    }

    /** @deprecated Replaced by {@link #getUsedFiles()} */
    public String[] getUsedFiles(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getUsedFiles();
    }

    /** @deprecated Replaced by {@link #getIndex(int, int, int)} */
    public int getIndex(String id, int z, int c, int t) throws FormatException, IOException {
        setId(id);
        return getReader().getIndex(z, c, t);
    }

    /** @deprecated Replaced by {@link #getZCTCoords(int)} */
    public int[] getZCTCoords(String id, int index) throws FormatException, IOException {
        setId(id);
        return getReader().getZCTCoords(index);
    }

    /** @deprecated Replaced by {@link #getMetadataValue(String)} */
    public Object getMetadataValue(String id, String field) throws FormatException, IOException {
        setId(id);
        return getReader().getMetadataValue(field);
    }

    /** @deprecated Replaced by {@link #getMetadata()} */
    public Hashtable getMetadata(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getMetadata();
    }

    /** @deprecated Replaced by {@link #getCoreMetadata()} */
    public CoreMetadata getCoreMetadata(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getCoreMetadata();
    }

    /** @deprecated Replaced by {@link #getMetadataStore()} */
    public MetadataStore getMetadataStore(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getMetadataStore();
    }

    /** @deprecated Replaced by {@link #getMetadataStoreRoot()} */
    public Object getMetadataStoreRoot(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getMetadataStoreRoot();
    }
}
