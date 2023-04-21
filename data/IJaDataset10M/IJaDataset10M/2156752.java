package com.intel.gpe.services.fts.gtk.byteio;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.axis.types.UnsignedLong;
import org.ggf.byteio.byte_io.CustomFaultType;
import org.ggf.byteio.byte_io.ReadNotPermittedFaultType;
import org.ggf.byteio.byte_io.TransferInformationType;
import org.ggf.byteio.byte_io.WriteNotPermittedFaultType;
import org.ggf.byteio.random_access.Append;
import org.ggf.byteio.random_access.AppendResponse;
import org.ggf.byteio.random_access.Read;
import org.ggf.byteio.random_access.ReadResponse;
import org.ggf.byteio.random_access.TruncAppend;
import org.ggf.byteio.random_access.TruncAppendResponse;
import org.ggf.byteio.random_access.Write;
import org.ggf.byteio.random_access.WriteResponse;
import org.ggf.jsdl.SourceTarget_Type;
import org.globus.wsrf.RemoveCallback;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.ResourceLifetime;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertyMetaData;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.utils.AddressingUtils;
import com.intel.gpe.globus.constants.FTSConstants;
import com.intel.gpe.globus.constants.RandomByteIOConstants;
import com.intel.gpe.services.common.gtk.GT4ResourcePropertyNamesProperty;
import com.intel.gpe.services.common.gtk.GTKConstants;
import com.intel.gpe.services.sms.common.DynamicStorageResource;
import com.intel.gpe.services.sms.common.FileListFailedException;
import com.intel.gpe.tsi.common.FileInfo;
import com.intel.gpe.tsi.common.IllegalFileException;
import com.intel.gpe.tsi.common.User;
import com.intel.gpe.util.FaultUtil;
import com.intel.util.URIUtil;

/**
 * @author Dmitry Ivanov
 * @author Alexander Lukichev
 * @version $Id$
 */
public class RandomByteIOResource implements Resource, ResourceProperties, ResourceLifetime, ResourceIdentifier, RemoveCallback {

    private static Logger logger = Logger.getLogger("com.intel.gpe");

    private ResourcePropertySet resProps;

    private Object resourceId;

    private Calendar terminationTime;

    private DynamicStorageResource storageResource;

    private String file;

    private String originalFile;

    private ResourceProperty modificationTime;

    private ResourceProperty accessTime;

    private ResourceProperty writable;

    private ResourceProperty readable;

    private ResourceProperty size;

    private GT4ResourcePropertyNamesProperty rpnames;

    private boolean changeOwnerFlag;

    private User user;

    public RandomByteIOResource(User user, Object resourceId, boolean export, ResourceKey storageKey, RandomByteIOConfiguration configuration, String path, String originalPath, DynamicStorageResource storageResource) throws Exception {
        this.storageResource = storageResource;
        this.file = path;
        this.originalFile = originalPath;
        this.resourceId = resourceId;
        this.user = user;
        EndpointReferenceType storageEPR = getStorageEPR(configuration, storageKey);
        String fileURL = "rbyteio-" + storageEPR.getAddress().toString() + "?resource_id=" + storageKey.getValue() + "#" + URIUtil.quoteForURI(originalPath);
        resProps = new SimpleResourcePropertySet(RandomByteIOConstants.RANDOM_BYTEIO_RP);
        ResourceProperty source = new SimpleResourceProperty(FTSConstants.SOURCE_Q);
        if (export) {
            SourceTarget_Type sourceValue = new SourceTarget_Type();
            sourceValue.setURI(new URI(fileURL));
            source.add(sourceValue);
        }
        resProps.add(source);
        ResourceProperty target = new SimpleResourceProperty(FTSConstants.TARGET_Q);
        if (!export) {
            SourceTarget_Type targetValue = new SourceTarget_Type();
            targetValue.setURI(new URI(fileURL));
            target.add(targetValue);
        }
        resProps.add(target);
        ResourceProperty storageReference = new SimpleResourceProperty(FTSConstants.STORAGE_EPR_Q);
        storageReference.add(storageEPR);
        resProps.add(storageReference);
        rpnames = new GT4ResourcePropertyNamesProperty(RandomByteIOConstants.RP_NAMES);
        resProps.add(rpnames);
        ResourceProperty currentTime = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.CURRENT_TIME, this);
        resProps.add(currentTime);
        ResourceProperty terminationTime = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.TERMINATION_TIME, this);
        resProps.add(terminationTime);
        ResourceProperty transferMechanisms = new SimpleResourceProperty(RandomByteIOConstants.TRANSFER_MECHANISMS_Q);
        transferMechanisms.add(RandomByteIOConstants.SIMPLE_TRANSFER_MECHANISM);
        resProps.add(transferMechanisms);
        ResourceProperty creationTime = new SimpleResourceProperty(RandomByteIOConstants.CREATE_TIME_Q);
        creationTime.add(getCurrentTime());
        resProps.add(creationTime);
        modificationTime = new SimpleResourceProperty(RandomByteIOConstants.MODIFICATION_TIME_Q);
        resProps.add(modificationTime);
        accessTime = new SimpleResourceProperty(RandomByteIOConstants.ACCESS_TIME_Q);
        resProps.add(accessTime);
        size = new SimpleResourceProperty(RandomByteIOConstants.SIZE_Q);
        resProps.add(size);
        readable = new SimpleResourceProperty(RandomByteIOConstants.READABLE_Q);
        resProps.add(readable);
        writable = new SimpleResourceProperty(RandomByteIOConstants.WRITABLE_Q);
        resProps.add(writable);
        try {
            FileInfo fileInfo = storageResource.listFile(user, file);
            size.add(new UnsignedLong(fileInfo.getSize()));
            writable.add(new Boolean(fileInfo.getPermissions().isWritable()));
            readable.add(new Boolean(fileInfo.getPermissions().isReadable()));
            changeOwnerFlag = false;
        } catch (FileListFailedException e) {
            if (export) {
                logger.log(Level.SEVERE, "Error listing file " + file, e);
            } else {
                logger.log(Level.SEVERE, "Error listing file " + file + "; new file assumed");
            }
            size.add(new UnsignedLong(0));
            writable.add(new Boolean(true));
            readable.add(new Boolean(false));
            changeOwnerFlag = true;
        }
    }

    public ResourcePropertySet getResourcePropertySet() {
        return resProps;
    }

    public void setTerminationTime(Calendar time) {
        Calendar currentTime = getCurrentTime();
        if (time != null && time.before(currentTime)) {
            time = (Calendar) currentTime.clone();
            time.add(Calendar.MILLISECOND, GTKConstants.TERMINATION_TIME_DELTA_MILLISECONDS);
        }
        terminationTime = time;
    }

    public Calendar getTerminationTime() {
        return terminationTime;
    }

    public Calendar getCurrentTime() {
        return Calendar.getInstance();
    }

    public Object getID() {
        return resourceId;
    }

    public void remove() throws ResourceException {
    }

    private EndpointReferenceType getStorageEPR(RandomByteIOConfiguration serviceConfiguration, ResourceKey key) throws Exception {
        return AddressingUtils.createEndpointReference(ServiceHost.getBaseURL() + serviceConfiguration.getStorageManagementServicePath(), key);
    }

    public ReadResponse read(Read request) throws java.rmi.RemoteException, org.ggf.byteio.byte_io.CustomFaultType, org.ggf.byteio.byte_io.ReadNotPermittedFaultType, org.ggf.byteio.byte_io.UnsupportedTransferFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        accessTime.clear();
        accessTime.add(getCurrentTime());
        long startOffset = request.getStartOffset().longValue();
        int bytesPerBlock = request.getBytesPerBlock().intValue();
        int numBlocks = request.getNumBlocks().intValue();
        long stride = request.getStride();
        TransferInformationType inti = request.getTransferInformation();
        try {
            byte[] indata = TransferMechanismsUtil.parse(MessageContext.getCurrentContext().getRequestMessage(), inti);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot parse input message", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot parse input message");
        }
        byte[] data;
        try {
            data = storageResource.read(user, this.file, startOffset, bytesPerBlock, numBlocks, stride);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Error reading data from remote file: " + file, e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Error reading data from remote file: " + originalFile);
        } catch (IllegalFileException e) {
            logger.log(Level.SEVERE, "Illegal file: " + file);
            throw FaultUtil.buildFault(new ReadNotPermittedFaultType(), e, "Illegal file: " + originalFile);
        }
        ReadResponse resp = new ReadResponse();
        TransferInformationType ti = new TransferInformationType();
        resp.setTransferInformation(ti);
        try {
            TransferMechanismsUtil.envelop(MessageContext.getCurrentContext().getResponseMessage(), inti.getTransferMechanism().toString(), ti, data);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot encode data", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot encode data");
        }
        return resp;
    }

    public WriteResponse write(Write request) throws java.rmi.RemoteException, org.ggf.byteio.byte_io.WriteNotPermittedFaultType, org.ggf.byteio.byte_io.CustomFaultType, org.ggf.byteio.byte_io.UnsupportedTransferFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        accessTime.clear();
        accessTime.add(getCurrentTime());
        long startOffset = request.getStartOffset().longValue();
        int bytesPerBlock = request.getBytesPerBlock().intValue();
        long stride = request.getStride();
        TransferInformationType inti = request.getTransferInformation();
        byte[] indata;
        try {
            indata = TransferMechanismsUtil.parse(MessageContext.getCurrentContext().getRequestMessage(), inti);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot parse input message", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot parse input message");
        }
        try {
            storageResource.write(user, this.file, startOffset, bytesPerBlock, stride, indata);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Error writing data to remote file: " + file, e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + originalFile);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Error writing data to remote file: " + file, e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + originalFile);
        } catch (IllegalFileException e) {
            logger.log(Level.SEVERE, "Illegal file: " + file);
            throw FaultUtil.buildFault(new WriteNotPermittedFaultType(), e, "Illegal file: " + originalFile);
        }
        byte[] data = null;
        if (changeOwnerFlag && indata.length > 0) {
            try {
                storageResource.changeOwner(user, this.file);
            } catch (RemoteException e) {
                logger.log(Level.SEVERE, "Cannot change owner of the file: " + this.file, e);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot change owner of the file: " + originalFile);
            } catch (IllegalFileException e) {
                logger.log(Level.SEVERE, "Illegal file: " + file);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Illegal file: " + originalFile);
            }
            changeOwnerFlag = false;
        }
        try {
            long fileSize = storageResource.getFileSize(user, this.file);
            size.set(0, new UnsignedLong(fileSize));
        } catch (FileListFailedException e) {
            if (indata.length > 0) {
                logger.log(Level.SEVERE, "Cannot list file: " + this.file, e);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + file);
            } else {
            }
        }
        modificationTime.clear();
        modificationTime.add(getCurrentTime());
        WriteResponse resp = new WriteResponse();
        TransferInformationType ti = new TransferInformationType();
        try {
            TransferMechanismsUtil.envelop(MessageContext.getCurrentContext().getResponseMessage(), inti.getTransferMechanism().toString(), ti, data);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot encode data", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot encode data");
        }
        resp.setTransferInformation(ti);
        return resp;
    }

    public AppendResponse append(Append request) throws java.rmi.RemoteException, org.ggf.byteio.byte_io.WriteNotPermittedFaultType, org.ggf.byteio.byte_io.CustomFaultType, org.ggf.byteio.byte_io.UnsupportedTransferFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        accessTime.clear();
        accessTime.add(getCurrentTime());
        TransferInformationType inti = request.getTransferInformation();
        byte[] indata;
        try {
            indata = TransferMechanismsUtil.parse(MessageContext.getCurrentContext().getRequestMessage(), inti);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot parse input message", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot parse input message");
        }
        long oldsize = ((UnsignedLong) size.get(0)).longValue();
        try {
            storageResource.write(user, this.file, oldsize, indata.length, 0, indata);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Error writing data to remote file: " + file, e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + file);
        } catch (IllegalFileException e) {
            logger.log(Level.SEVERE, "Illegal file: " + file);
            throw FaultUtil.buildFault(new WriteNotPermittedFaultType(), e, "Illegal file: " + file);
        }
        if (changeOwnerFlag && indata.length > 0) {
            try {
                storageResource.changeOwner(user, this.file);
            } catch (RemoteException e) {
                logger.log(Level.SEVERE, "Cannot change owner of the file: " + this.file, e);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot change owner of the file: " + originalFile);
            } catch (IllegalFileException e) {
                logger.log(Level.SEVERE, "Illegal file: " + file);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Illegal file: " + originalFile);
            }
            changeOwnerFlag = false;
        }
        try {
            long fileSize = storageResource.getFileSize(user, this.file);
            size.set(0, new UnsignedLong(fileSize));
        } catch (FileListFailedException e) {
            if (indata.length > 0) {
                logger.log(Level.SEVERE, "Cannot list file: " + this.file, e);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + file);
            } else {
            }
        }
        byte[] data = null;
        modificationTime.clear();
        modificationTime.add(getCurrentTime());
        AppendResponse resp = new AppendResponse();
        TransferInformationType ti = new TransferInformationType();
        resp.setTransferInformation(ti);
        try {
            TransferMechanismsUtil.envelop(MessageContext.getCurrentContext().getResponseMessage(), inti.getTransferMechanism().toString(), ti, data);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot encode data", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot encode data");
        }
        return resp;
    }

    public TruncAppendResponse truncAppend(TruncAppend request) throws java.rmi.RemoteException, org.ggf.byteio.random_access.TruncateNotPermittedFaultType, org.ggf.byteio.byte_io.WriteNotPermittedFaultType, org.ggf.byteio.byte_io.CustomFaultType, org.ggf.byteio.byte_io.UnsupportedTransferFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        accessTime.clear();
        accessTime.add(getCurrentTime());
        long startOffset = request.getOffset().longValue();
        TransferInformationType inti = request.getTransferInformation();
        byte[] indata = null;
        try {
            indata = TransferMechanismsUtil.parse(MessageContext.getCurrentContext().getRequestMessage(), inti);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot parse input message", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot parse input message");
        }
        try {
            storageResource.truncAppend(user, this.file, startOffset, indata.length, indata);
        } catch (IllegalFileException e) {
            logger.log(Level.SEVERE, "Illegal file: " + file);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Illegal file: " + originalFile);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Cannot truncate and append to file: " + file);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot truncate and append to file: " + originalFile);
        }
        try {
            long fileSize = storageResource.getFileSize(user, this.file);
            size.set(0, new UnsignedLong(fileSize));
        } catch (FileListFailedException e) {
            if (indata.length > 0) {
                logger.log(Level.SEVERE, "Cannot list file: " + this.file, e);
                throw FaultUtil.buildFault(new CustomFaultType(), e, "Error writing data to remote file: " + file);
            } else {
            }
        }
        byte[] data = null;
        modificationTime.clear();
        modificationTime.add(getCurrentTime());
        TruncAppendResponse resp = new TruncAppendResponse();
        TransferInformationType ti = new TransferInformationType();
        resp.setTransferInformation(ti);
        try {
            TransferMechanismsUtil.envelop(MessageContext.getCurrentContext().getResponseMessage(), inti.getTransferMechanism().toString(), ti, data);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot encode data", e);
            throw FaultUtil.buildFault(new CustomFaultType(), e, "Cannot encode data");
        }
        return resp;
    }
}
