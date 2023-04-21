package gov.lanl.arc.heritrixImpl;

import gov.lanl.arc.ARCException;
import gov.lanl.arc.ARCProperties;
import gov.lanl.util.uuid.UUIDFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.archive.io.arc.ARCWriter;

/**
 * ARCFileWriter.java
 * <p>Class to write content to an arc file.</p>
 *      
 *   Usage:<br>
 *   String arcfileDir = "/lanl/repository/arc/";<br>
 *   long timeStamp = ARCFileWriter.getCurrentTimeAsLong();<br>
 *   String project = "EPRINT";<br>
 *   String mimeType = "application/pdf";<br>
 *   ByteArrayOutputStream baos = create a bytearrayoutputstream;<br>
 *   int recordLength = baos.size();<br>
 *      
 *   ARCFileWriter aw = new ARCFileWriter(arcfileDir, project)<br>
 *   aw.write(aw.getUUIDResourceURI(), "0.0.0.0", mimeType, baos, timeStamp, recordLength);<br>
 *   aw.close();<br>
 *
 * @author rchute
 */
public class ARCFileWriter {

    private static final int MAXSIZE = 1024 * 1024 * 1000;

    private static SimpleDateFormat df;

    private ARCWriter arcWriter;

    private String arcFileDir;

    private int maxSize = MAXSIZE;

    private String arcDate;

    private String arcFileName;

    private ArrayList<String> arcFileNameList;

    private boolean compression = false;

    private String prefix = "";

    private String organization = ARCProperties.getLocalDataStreamPrefix();

    private PrintStream ps;

    static {
        df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Simple Constructor accepting the destination arc directory & project name
     * A filename of the format <PROJECT><UUID><.arc> will be generated.
     * @throws ARCException
     */
    public ARCFileWriter(String arcfileDir, String project) throws ARCException {
        this(arcfileDir, ARCFileWriter.getUUIDArcName(project, false), false);
        this.prefix = project;
    }

    /**
     * Constructor accepting the destination arc directory, target arc file name and 
     * whether the file is to be compressed or not.
     * @throws ARCException
     */
    public ARCFileWriter(String arcfileDir, String arcfileName, boolean compression) throws ARCException {
        this.arcFileDir = arcfileDir;
        this.compression = compression;
        this.arcFileName = arcfileName;
        File arcFile = new File(arcfileDir, arcfileName);
        createARCFile(arcFile.getAbsolutePath());
    }

    /**
     * Constructor accepting the destination arc directory, target arc file name and 
     * whether the file is to be compressed or not.
     * @throws ARCException
     */
    public ARCFileWriter(String arcfileDir, String prefix, boolean compression, int maxsize) throws ARCException {
        this.arcFileDir = arcfileDir;
        this.compression = compression;
        this.maxSize = maxsize;
        this.prefix = prefix;
        createARCFile(new File(arcfileDir, getUUIDArcName(prefix, compression)).getAbsolutePath());
    }

    /**
     * Construtor overrides default organization and maxsize parameters.  
     * Arc files will be written to List<File> in a round-robin fashion.
     */
    public ARCFileWriter(List<File> arcfileDir, boolean compression, int maxsize) {
        this.compression = compression;
        this.maxSize = maxsize;
        this.arcWriter = new ARCWriter(arcfileDir, prefix, compression, maxSize);
    }

    /**
     * Writes resource to an arc file associated with ARCFileWriter instance
     * @param arcid - local URI for archived resource
     * @param ipAddress - source IP address of resource
     * @param mimeType - the MIME type, as defined in RFC 2045 and 2046.
     * @param byteStream - Byte Array of Archived Resource
     * @throws ARCException
     */
    public void write(String arcid, String ipAddress, String mimeType, byte[] byteStream) throws ARCException {
        try {
            long timeStamp = System.currentTimeMillis();
            this.write(arcid, ipAddress, mimeType, byteStream, timeStamp);
        } catch (ARCException e) {
            throw new ARCException("Error occured: " + e.getCause());
        }
    }

    /**
     * Writes resource to an arc file associated with ARCFileWriter instance
     * @param arcid - local URI for archived resource
     * @param ipAddress - source IP address of resource
     * @param mimeType - the MIME type, as defined in RFC 2045 and 2046
     * @param byteStream - Byte Array of Archived Resource
     * @param timeStamp  - milliseconds since epoc.
     * @throws ARCException
     */
    public void write(String arcid, String ipAddress, String mimeType, byte[] byteStream, long timeStamp) throws ARCException {
        ByteArrayOutputStream baos = null;
        try {
            int recordLength = byteStream.length;
            baos = new ByteArrayOutputStream(recordLength);
            baos.write(byteStream);
            this.write(arcid, ipAddress, mimeType, baos, timeStamp, recordLength);
            baos.close();
        } catch (IOException e) {
            throw new ARCException("Error writing arc file: " + e.getCause());
        } catch (ARCException e) {
            throw new ARCException("Error occured: " + e.getCause());
        }
    }

    /**
     * Writes resource to an arc file associated with ARCFileWriter instance
     * @param arcid - local URI for archived resource
     * @param ipAddress - source IP address of resource
     * @param mimeType - the MIME type, as defined in RFC 2045 and 2046.
     * @param byteStream - ByteArrayOutputStream of archived resource
     * @param timeStamp - milliseconds since epoc
     * @param recordLength - length of the content fetched.
     * @throws ARCException
     */
    public void write(String arcid, String ipAddress, String mimeType, ByteArrayOutputStream byteStream, long timeStamp, int recordLength) throws ARCException {
        try {
            checkARCFileSize();
            arcWriter.write(arcid, mimeType, ipAddress, timeStamp, recordLength, byteStream);
        } catch (IOException e) {
            throw new ARCException("Error writing arc file: " + e.getCause());
        }
    }

    /**
     * Call in a finally block to ensure the object is properly disposed of.
     * @throws IOException
     */
    public void close() throws IOException {
        arcWriter.close();
        arcWriter = null;
    }

    /**
     * Get Arc File Name including extention
     * @return Returns the arcFileName.
     */
    public String getArcFileName() {
        return arcFileName;
    }

    /**
     * Set Arc File Name including extension
     * @param arcFileName The arcFileName to set.
     */
    public void setArcFileName(String arcFileName) {
        this.arcFileName = arcFileName;
    }

    /**
     * Availble should you need access to the lower-level Heritrix object
     * @return Returns the Heritrix ARCWriter
     */
    public ARCWriter getArcWriter() {
        return arcWriter;
    }

    /**
     * Get the Date of the Arc File
     * @return arcDate as a Date format
     * @throws ARCException
     */
    public Date getDate() throws ARCException {
        Date date = null;
        try {
            date = df.parse(arcDate);
        } catch (ParseException e) {
            throw new ARCException("Error parsing date, " + arcDate + ": " + e.getCause());
        }
        return date;
    }

    /**
     * Get current time in long format
     */
    public static long getCurrentTimeAsLong() {
        return getDateAsLong(new Date());
    }

    /**
     * Get initialFetchTime of resource in long format
     */
    public static long getDateAsLong(Date initialFetchTime) {
        return Long.parseLong(df.format(initialFetchTime));
    }

    /**
     * Generate a UUID compliant arc file name of format <PREFIX><UUID><EXT>
     */
    public static String getUUIDArcName(String prefix, boolean compression) {
        StringBuffer sb = new StringBuffer();
        df.format(new Date(), sb, new FieldPosition(0));
        String uuid = UUIDFactory.generateUUID().toString();
        uuid = uuid.substring(9);
        return prefix + uuid + ".arc" + (compression ? ".gz" : "");
    }

    /**
     * Generate a UUID compliant resource id
     * @return uuid for resource including local datastream prefix
     */
    public String getUUIDResourceURI() {
        StringBuffer sb = new StringBuffer();
        df.format(new Date(), sb, new FieldPosition(0));
        String uuid = UUIDFactory.generateUUID().toString();
        uuid = uuid.substring(9);
        return organization + uuid;
    }

    /**
     * @return Returns the organization.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization The organization to set.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Strip directory path and extensions
     * @return file name minus path and extension
     */
    public String getFileID() {
        if (!compression) {
            int jjj = arcFileName.lastIndexOf(".");
            String fname = arcFileName.substring(0, jjj);
            return fname;
        } else {
            int jjj = arcFileName.lastIndexOf(".");
            String fname = arcFileName.substring(0, jjj);
            int kk = fname.lastIndexOf(".");
            fname = fname.substring(0, kk);
            return fname;
        }
    }

    public int getArcMaxSize() {
        return maxSize;
    }

    public void checkARCFileSize() throws ARCException {
        if (this.arcWriter == null || (this.getArcMaxSize() != -1 && (this.arcWriter.getArcFile().length() > this.getArcMaxSize()))) {
            createARCFile(new File(this.arcFileDir, getUUIDArcName(prefix, false)).getAbsolutePath());
        }
    }

    public void createARCFile(String arcFilePath) throws ARCException {
        if (arcFileNameList == null) arcFileNameList = new ArrayList<String>();
        File arcFile = new File(arcFilePath);
        try {
            if (ps != null) this.ps.close();
            if (arcWriter != null) this.arcWriter.close();
            ps = new PrintStream(arcFile);
            this.arcFileName = arcFile.getName();
            if (!arcFileNameList.contains(arcFileName)) arcFileNameList.add(arcFileName);
            this.arcWriter = new ARCWriter(ps, arcFile, compression, null, null);
            this.arcDate = df.format(new Date());
        } catch (FileNotFoundException e) {
            throw new ARCException("Error occured: " + e.getCause());
        } catch (IOException e) {
            throw new ARCException("Error occured: " + e.getCause());
        }
    }

    public ArrayList<String> getArcFileNameList() {
        return arcFileNameList;
    }
}
