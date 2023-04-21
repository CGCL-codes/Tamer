package wjhk.jupload2.testhelpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.exception.JUploadIOException;
import wjhk.jupload2.filedata.FileData;
import wjhk.jupload2.upload.AbstractJUploadTestHelper;
import wjhk.jupload2.upload.helper.ByteArrayEncoder;

/**
 * @author etienne_sf
 * 
 */
public class FileDataTestHelper implements FileData {

    /**	 */
    public String relativeDir = File.separator + "files";

    /** */
    public String fileName;

    /** */
    public File file;

    /** */
    public int fileNumber;

    /** */
    public String fileContent;

    /** */
    public String fileExtention = "txt";

    /** */
    public String md5sum = null;

    /** */
    public String mimeType = "text/plain";

    /** */
    public Date lastModified = new Date();

    /** */
    public boolean preparedForUpload = true;

    /**
	 * @param fileNumber
	 */
    public FileDataTestHelper(int fileNumber) {
        this.fileNumber = fileNumber;
        this.fileName = fileNumber + "." + this.fileExtention;
        this.file = AbstractJUploadTestHelper.getTestFile(this.relativeDir + File.separator + this.fileName);
        this.fileContent = "This is the file content for the file number " + fileNumber;
    }

    /** */
    public void afterUpload() {
    }

    /**
	 * @param bae
	 * @param index
	 * @throws JUploadIOException
	 */
    public void appendFileProperties(ByteArrayEncoder bae, int index) throws JUploadIOException {
        throw new UnsupportedOperationException(this.getClass() + ".appendFileProperties() is not implemented in tests cases");
    }

    /**
	 * @throws JUploadException
	 */
    public void beforeUpload() throws JUploadException {
    }

    /**
	 * @return canRead
	 * */
    public boolean canRead() {
        return true;
    }

    /**
	 * @return dir
	 */
    public String getDirectory() {
        return this.file.getParent();
    }

    /**
	 * @return file
	 */
    public File getFile() {
        return this.file;
    }

    /**
	 * @return extention
	 */
    public String getFileExtension() {
        return this.fileExtention;
    }

    /**
	 * @return length
	 */
    public long getFileLength() {
        return this.fileContent.length();
    }

    /**
	 * @return filename
	 */
    public String getFileName() {
        return this.fileName;
    }

    /**
	 * @return inputStream
	 * @throws JUploadException
	 */
    public InputStream getInputStream() throws JUploadException {
        return new ByteArrayInputStream(this.fileContent.getBytes());
    }

    /**
	 * @return last modified
	 */
    public Date getLastModified() {
        return this.lastModified;
    }

    /**
	 * @see wjhk.jupload2.filedata.FileData#getMD5()
	 */
    public String getMD5() throws JUploadException {
        return this.md5sum;
    }

    /**
	 * @return mime type
	 */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
	 * @return rel dir
	 */
    public String getRelativeDir() {
        return this.relativeDir;
    }

    /**
	 * @return upload length
	 */
    public long getUploadLength() {
        return this.fileContent.length();
    }

    /**
	 * @return is prepared
	 */
    public boolean isPreparedForUpload() {
        return this.preparedForUpload;
    }
}
