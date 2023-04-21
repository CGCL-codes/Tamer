package com.intel.gpe.client2.common.transfers.baseline;

import java.io.OutputStream;
import com.intel.gpe.client2.FileFetchingProgressListener;
import com.intel.gpe.client2.security.GPESecurityManager;
import com.intel.gpe.client2.transfers.FileExport;
import com.intel.gpe.clients.api.FileTransferClient;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.fts.baseline.BaselineFileTransferClient;

/**
 * @author Thomas Kentemich 
 * @version $Id: BaselineFileExportImpl.java,v 1.3 2006/08/15 13:19:03 lukichev Exp $
 */
public class BaselineFileExportImpl implements FileExport {

    private StorageClient storage;

    public BaselineFileExportImpl(StorageClient storage) {
        this.storage = storage;
    }

    public void getFile(GPESecurityManager manager, String remoteFile, OutputStream os, FileFetchingProgressListener progress) throws Exception {
        BaselineFileTransferClient transfer = (BaselineFileTransferClient) storage.exportFile(remoteFile, FileTransferClient.BASELINE, false);
        byte[] data = transfer.getFile();
        os.write(data);
        if (progress != null) {
            progress.dataFetched(data.length);
        }
    }

    public String getName() {
        return "Baseline";
    }
}
