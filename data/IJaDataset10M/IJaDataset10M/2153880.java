package com.intel.gpe.clients.api.fts.gridftp;

import org.globus.io.streams.GridFTPInputStream;
import org.globus.io.streams.GridFTPOutputStream;
import com.intel.gpe.clients.api.FileTransferClient;
import com.intel.gpe.clients.api.exceptions.GPEFTPException;
import com.intel.gpe.clients.api.exceptions.GPEInvalidResourcePropertyQNameException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareServiceException;
import com.intel.gpe.clients.api.exceptions.GPEResourceUnknownException;
import com.intel.gpe.clients.api.exceptions.GPESecurityException;
import com.intel.gpe.clients.api.exceptions.GPEUnmarshallingException;
import com.intel.gpe.clients.api.security.gss.GSSSecurityManager;

/**
 * The file transfer client for GridFTP file transfer operations
 *
 * @author Alexander Lukichev
 * @version $Id: GridFTPFileTransferClient.java,v 1.5 2006/03/23 09:24:56 lukichev Exp $
 */
public interface GridFTPFileTransferClient extends FileTransferClient {

    /**
     * @return The input stream to put the data to the remote location
     */
    public GridFTPInputStream getInputStream(GSSSecurityManager secMgr) throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, GPEFTPException;

    /**
     * @return The output stream to get the data from the remote location
     */
    public GridFTPOutputStream getOutputStream(GSSSecurityManager secMgr) throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, GPEFTPException;
}
