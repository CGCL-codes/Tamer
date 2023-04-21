package cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.io;

import java.io.File;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.core.FileObject;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.BaseOutput;

/**
 * @author Jimmy
 * 
 */
public class IODownloadOutput extends BaseOutput {

    /** file object. */
    private FileObject toDownload;

    /** byte result. */
    private byte[] byteResult;

    /** input stream result. */
    private File outputFile;

    /**
     * 
     */
    public IODownloadOutput() {
    }

    /**
     * @return the toDownload
     */
    public FileObject getToDownload() {
        return this.toDownload;
    }

    /**
     * @param toDownload
     *            the toDownload to set
     */
    public void setToDownload(FileObject toDownload) {
        this.toDownload = toDownload;
    }

    /**
     * @return the byteResult
     */
    public byte[] getByteResult() {
        return this.byteResult;
    }

    /**
     * @param byteResult
     *            the byteResult to set
     */
    public void setByteResult(byte[] byteResult) {
        this.byteResult = byteResult;
    }

    /**
     * @return the outputFile
     */
    public File getOutputFile() {
        return this.outputFile;
    }

    /**
     * @param outputFile
     *            the outputFile to set
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }
}
