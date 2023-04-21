package org.jpedal.io;

import java.util.Map;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PageLookup;
import org.jpedal.objects.PdfFileInformation;

public interface PdfObjectReader {

    /**
	 * read first start ref from last 1024 bytes
	 */
    public int readFirstStartRef() throws PdfException;

    /**set a password for encryption*/
    public void setEncryptionPassword(String password);

    /**
	 * turns any refs into String or Map
	 */
    public Object resolveToMapOrString(String command, Object field);

    /**
	 * read a dictionary object
	 */
    public int readDictionary(String objectRef, int level, Map rootObject, int i, byte[] raw, boolean isEncryptionObject, Map textFields, int endPoint);

    /**read a stream*/
    public byte[] readStream(Map objData, String objectRef, boolean cacheValue, boolean decompress, boolean keepRaw);

    /**read a stream*/
    public byte[] readStream(String ref, boolean decompress);

    /**
	 * read an object in the pdf into a Map which can be an indirect or an object,
	 * used for compressed objects
	 *
	 */
    public Map readObject(int objectRef);

    /**
	 * stop cache of last object in readObject
	 *
	 */
    public void flushObjectCache();

    /**
	 * read an object in the pdf into a Map which can be an indirect or an object
	 *
	 */
    public Map readObject(String objectRef, boolean isEncryptionObject, Map textFields);

    /**
	 * read an object in the pdf into a Map which can be an indirect or an object
	 *
	 */
    public byte[] readObjectAsByteArray(String objectRef, boolean isEncryptionObject);

    /**
	 * read reference table start to see if new 1.5 type or traditional xref
	 * @throws PdfException
	 */
    public String readReferenceTable() throws PdfException;

    /**
	 * read the form data from the file
	 */
    public PdfFileInformation readPdfFileMetadata(String ref);

    /**
	 * take object ref for XML in stream, read and return as StringBuffer
	 */
    public StringBuffer convertStreamToXML(String ref);

    /**
	 * get value which can be direct or object
	 */
    public String getValue(String value);

    /**
	 * get text value as byte stream which can be direct or object
	 */
    public byte[] getByteTextStringValue(Object rawValue, Map fields);

    /**
	 * get sub dictionary value which can be direct or object
	 */
    public Map getSubDictionary(Object value);

    /**
	 * return flag to show if encrypted
	 */
    public boolean isEncrypted();

    /**
	 * return flag to show if valid password has been supplied
	 */
    public boolean isPasswordSupplied();

    /**
	 * return flag to show if encrypted
	 */
    public boolean isExtractionAllowed();

    /**show if file can be displayed*/
    public boolean isFileViewable();

    /**extract  metadata for  encryption object
	 */
    public void readEncryptionObject(String ref) throws PdfSecurityException;

    /**
	 * return pdf data
	 */
    public byte[] getPdfBuffer();

    /**
	 * read any names
	 */
    public void readNames(Object nameObj, Javascript javascript);

    /**
	 * convert name into object ref
	 */
    public String convertNameToRef(String value);

    /**
	 * convert all object refs (ie 1 0 R) into actual data.
	 * Works recursively to cover all levels.
	 * @param pageLookup
	 */
    public void flattenValuesInObject(boolean addPage, boolean keepKids, Map formData, Map newValues, Map fields, PageLookup pageLookup, String formObject);

    /**
	 * set size over which objects kept on disk
	 */
    public void setCacheSize(int miniumumCacheSize);

    /**read data directly from PDF*/
    public byte[] readStreamFromPDF(int start, int end);

    public void readStreamIntoMemory(Map downField);

    /**
	 * main routine which is passed list of filters to decode and the binary
	 * data. JPXDecode/DCTDecode are not handled here (we leave data as is and
	 * then put straight into a JPEG)<br>
	 * <p>
	 * <b>Note</b>
	 * </p>
	 * Not part of API
	 * </p>
	 */
    public byte[] decodeFilters(byte[] data, String filter_list, Map objData, int width, int height, boolean useNewCCITT, String cacheName) throws Exception;

    /**
	 * @param decodeParams
	 * @param rawParams
	 */
    public void convertStringToMap(Map decodeParams, Object rawParams);

    /**
	 * get pdf type in file (found at start of file)
	 */
    public String getType();

    /**
	 * open pdf file<br> Only files allowed (not http)
	 * so we can handle Random Access of pdf
	 */
    public void openPdfFile(String filename) throws PdfException;

    /**
	 * open pdf file using a byte stream
	 */
    public void openPdfFile(byte[] data) throws PdfException;

    /**
	 * close the file
	 */
    public void closePdfFile();

    public String getTextString(byte[] title);

    public Map directValuesToMap(String annot);

    public void setInterruptRefReading(boolean b);

    public Map readFDF() throws PdfException;

    /**give user access to internal PDF values */
    int getPDFflag(Integer i);

    boolean containsJBIG();
}
