package com.esri.gpt.control.webharvest.client.waf;

import com.esri.gpt.control.webharvest.IterationContext;
import com.esri.gpt.framework.resource.api.Resource;
import com.esri.gpt.framework.resource.query.Criteria;
import com.esri.gpt.framework.util.Val;
import java.util.Iterator;
import org.apache.commons.net.ftp.FTPFile;

/**
 * FTP file iterable.
 */
class FtpFileIterable implements Iterable<Resource> {

    protected IterationContext iterationContext;

    protected FtpClientRequest ftpClient;

    protected Criteria criteria;

    private FTPFile[] files;

    protected String folder;

    private Iterator<Resource> iterator;

    /**
   * Creates instance of the iterable.
   * @param iterationContext iteration context
   * @param url server url
   * @param folder folder
   */
    public FtpFileIterable(IterationContext iterationContext, FtpClientRequest ftpClient, Criteria criteria, FTPFile[] files, String folder) {
        this.iterationContext = iterationContext;
        this.ftpClient = ftpClient;
        this.criteria = criteria;
        this.files = files != null ? files : new FTPFile[] {};
        this.folder = Val.chkStr(folder);
    }

    @Override
    public Iterator<Resource> iterator() {
        if (iterator == null) {
            iterator = new FtpFileIterator(iterationContext, getFtpClient(), criteria, files, folder);
        }
        return iterator;
    }

    /**
   * Gets files.
   * @return files
   */
    public final FTPFile[] getFiles() {
        return files;
    }

    /**
   * Gets request.
   * @return request
   */
    public final FtpClientRequest getFtpClient() {
        return ftpClient;
    }
}
