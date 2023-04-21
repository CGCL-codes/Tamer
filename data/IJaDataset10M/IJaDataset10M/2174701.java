package com.germinus.xpression.cms;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.xpression.cms.web.TemporaryFilesHandler;

/**
 * This is class implements the methods of the struts DiskFile class
 * that are not implemented properly.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision$
 */
public class DiskFile extends org.apache.struts.upload.DiskFile implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6197349354493385362L;

    private static final Log log = LogFactory.getLog(DiskFile.class);

    /**
     * Creates a new <code>DiskFile</code> instance and initilise the
     * internal variables.
     *
     * @param filePath a <code>String</code> value
     */
    public DiskFile(String filePath) {
        super(filePath);
        File tempFile = new File(filePath);
        TemporaryFilesHandler.register(null, tempFile);
        this.fileName = tempFile.getName();
        this.fileSize = (int) tempFile.length();
    }
}
