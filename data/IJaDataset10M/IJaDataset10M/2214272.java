package com.intel.gpe.services.jms.gtkwf;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.globus.wsrf.ResourceContext;
import org.unigrids.services.atomic.jms.Abort;
import org.unigrids.services.atomic.jms.AbortResponse;
import org.unigrids.services.atomic.jms.Hold;
import org.unigrids.services.atomic.jms.HoldResponse;
import org.unigrids.services.atomic.jms.JobNotAbortedFaultType;
import org.unigrids.services.atomic.jms.JobNotHeldFaultType;
import org.unigrids.services.atomic.jms.JobNotResumedFaultType;
import org.unigrids.services.atomic.jms.JobNotStartedFaultType;
import org.unigrids.services.atomic.jms.Resume;
import org.unigrids.services.atomic.jms.ResumeResponse;
import org.unigrids.services.atomic.jms.Start;
import org.unigrids.services.atomic.jms.StartResponse;
import org.unigrids.services.atomic.types.GridFileType;
import com.intel.gpe.clients.api.exceptions.GPEDirectoryNotListedException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareServiceException;
import com.intel.gpe.clients.api.exceptions.GPEResourceUnknownException;
import com.intel.gpe.clients.api.exceptions.GPESecurityException;
import com.intel.gpe.clients.gpe.DefaultProperties;
import com.intel.gpe.clients.gpe.DummyCache;
import com.intel.gpe.clients.gpe4gtk.ISecuritySetup;
import com.intel.gpe.clients.gpe4gtk.sms.GridFileImpl;
import com.intel.gpe.clients.gpe4gtk.sms.StorageClientImpl;
import com.intel.gpe.services.common.gtk.DelegatedSecuritySetup;
import com.intel.gpe.services.wes.DirectoryNotListedFaultType;
import com.intel.gpe.services.wes.ListDirectory;
import com.intel.gpe.services.wes.ListDirectoryResponse;
import com.intel.gpe.util.filesets.FilePathUtils;

/**
 * @author Alexander Lukichev
 * @version $Id: GTKWorkflowJobManagementService.java,v 1.2 2006/10/20 13:35:19 mlukichev Exp $
 */
public class GTKWorkflowJobManagementService {

    public StartResponse start(Start startRequest) throws RemoteException, JobNotStartedFaultType {
        GTKWorkflowJobManagementResource job = (GTKWorkflowJobManagementResource) ResourceContext.getResourceContext().getResource();
        return job.start(startRequest);
    }

    public AbortResponse abort(Abort abortRequest) throws RemoteException, JobNotAbortedFaultType {
        GTKWorkflowJobManagementResource job = (GTKWorkflowJobManagementResource) ResourceContext.getResourceContext().getResource();
        return job.abort(abortRequest);
    }

    public HoldResponse hold(Hold holdRequest) throws RemoteException, JobNotHeldFaultType {
        GTKWorkflowJobManagementResource job = (GTKWorkflowJobManagementResource) ResourceContext.getResourceContext().getResource();
        return job.hold(holdRequest);
    }

    public ResumeResponse resume(Resume resumeRequest) throws RemoteException, JobNotResumedFaultType {
        GTKWorkflowJobManagementResource job = (GTKWorkflowJobManagementResource) ResourceContext.getResourceContext().getResource();
        return job.resume(resumeRequest);
    }

    public ListDirectoryResponse listDirectory(ListDirectory listDirecotryRequest) throws RemoteException, DirectoryNotListedFaultType {
        GTKWorkflowJobManagementResource job = (GTKWorkflowJobManagementResource) ResourceContext.getResourceContext().getResource();
        return job.listDirectory(listDirecotryRequest);
    }
}
