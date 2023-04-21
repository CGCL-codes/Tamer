package com.intel.gpe.client2.common.requests;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.common.i18n.Messages;
import com.intel.gpe.client2.common.i18n.MessagesKeys;
import com.intel.gpe.client2.common.requests.utils.GPEFileAccessor;
import com.intel.gpe.client2.common.utils.RemoteGPEFile;
import com.intel.gpe.client2.providers.FileProvider;
import com.intel.gpe.client2.requests.BaseRequest;
import com.intel.gpe.client2.security.GPESecurityManager;
import com.intel.gpe.clients.api.FileTransferClient;
import com.intel.gpe.clients.api.Job;
import com.intel.gpe.clients.api.JobClient;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.TargetSystemClient;
import com.intel.gpe.clients.api.jsdl.JSDLJob;
import com.intel.gpe.gridbeans.GPEFile;
import com.intel.gpe.gridbeans.IGridBean;
import com.intel.util.sets.Pair;

/**
 * 
 * @author Alexander Lukichev
 * @author Valery Shorin
 * @version $Id: SubmitRequest.java,v 1.23 2006/11/27 06:16:22 vashorin Exp $
 * 
 */
public class SubmitRequest extends BaseRequest {

    private static final long serialVersionUID = -6681900114297476852L;

    private Job job;

    private ClientWrapper<TargetSystemClient, ?> targetSystem;

    private Calendar terminationTime;

    private GPEFileAccessor<GPEFile> localFilesAccessor;

    private GPEFileAccessor<RemoteGPEFile> remoteFilesAccessor;

    private IGridBean model;

    private FileProvider fileProvider;

    private GPESecurityManager securityManager;

    public SubmitRequest(ClientWrapper<TargetSystemClient, ?> targetSystem, Job job, GPEFileAccessor<GPEFile> localFilesAccessor, GPEFileAccessor<RemoteGPEFile> remoteFilesAccessor, Calendar terminationTime, FileProvider fileProvider, GPESecurityManager securityManager) {
        super(MessageFormat.format(Messages.getString(MessagesKeys.common_requests_SubmitRequest_Submit_job_to), targetSystem));
        this.targetSystem = targetSystem;
        this.job = job;
        this.localFilesAccessor = localFilesAccessor;
        this.remoteFilesAccessor = remoteFilesAccessor;
        this.terminationTime = terminationTime;
        this.fileProvider = fileProvider;
        this.securityManager = securityManager;
    }

    public Object perform() throws Throwable {
        List<Pair<GPEFile, String>> localFiles = this.localFilesAccessor.getFiles();
        List<Pair<RemoteGPEFile, String>> remoteFiles = this.remoteFilesAccessor.getFiles();
        if (job instanceof JSDLJob) {
            for (Pair<RemoteGPEFile, String> file : remoteFiles) {
                StorageClient storage = file.getM1().getTargetSystem().getClient().getStorage(file.getM1().getStorageType());
                FileTransferClient ftc = storage.exportFile(file.getM1().getPath(), storage.getSupportedProtocols()[0], false);
                String uri = ftc.getSource();
                ftc.destroy();
                ((JSDLJob) job).addDataStagingImportElement(uri, Messages.getString(MessagesKeys.common_requests_SubmitRequest_Work), file.getM2());
            }
        }
        JobClient jobClient = targetSystem.getClient().submit(job, terminationTime);
        while (!jobClient.getStatus().isReady() && !jobClient.getStatus().isFailed()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (jobClient.getStatus().isFailed()) {
            throw new Exception(Messages.getString(MessagesKeys.common_requests_SubmitRequest_Job_failed));
        }
        StorageClient storage = jobClient.getWorkingDirectory();
        List lf = localFiles;
        List<Pair<GPEFile, String>> lf1 = lf;
        new PutFilesRequest(fileProvider, storage, lf1, securityManager).perform();
        jobClient.start();
        return jobClient;
    }

    public ClientWrapper<TargetSystemClient, ?> getTargetSystem() {
        return targetSystem;
    }
}
