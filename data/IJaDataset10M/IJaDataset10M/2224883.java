package com.intel.gpe.clients.api.fts.byteio;

import java.util.List;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import com.intel.gpe.clients.api.Constants;
import com.intel.gpe.clients.api.FileTransferClient;
import com.intel.gpe.clients.api.exceptions.GPEInvalidResourcePropertyQNameException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareServiceException;
import com.intel.gpe.clients.api.exceptions.GPEResourceUnknownException;
import com.intel.gpe.clients.api.exceptions.GPESecurityException;
import com.intel.gpe.clients.api.exceptions.GPEUnmarshallingException;
import com.intel.gpe.constants.StreamableByteIOConstants;

/**
 * @author Dmitry Ivanov
 * @version $Id: StreamableByteIOFileTransferClient.java,v 1.2 2006/06/10 09:42:58 nbmalysh Exp $
 */
public interface StreamableByteIOFileTransferClient extends ByteIOFileTransferClient {

    /**
	 * 
	 * @param offset
	 * @param seekOrigin
	 * @param bytesToRead
	 * @return
	 * @throws NumberFormatException
	 * @throws GPEByteIOCustomException
	 * @throws GPEByteIOReadNotPermittedException
	 * @throws GPEByteIOUnsupportedTransferException
	 * @throws GPEResourceUnknownException
	 * @throws GPEMiddlewareRemoteException
	 * @throws GPEMiddlewareServiceException
	 * @throws GPESecurityException
	 */
    public byte[] seekRead(long offset, StreamableByteIOConstants.SeekOrigin seekOrigin, int bytesToRead) throws NumberFormatException, GPEByteIOCustomException, GPEByteIOReadNotPermittedException, GPEByteIOSeekNotPermittedException, GPEByteIOUnsupportedTransferException, GPEResourceUnknownException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, GPESecurityException, GPEByteIOUnsupportedClientTransferMechanismException, GPEInvalidResourcePropertyQNameException, GPEUnmarshallingException;

    /**
	 * 
	 * @param offset
	 * @param seekOrigin
	 * @param data
	 * @throws NumberFormatException
	 * @throws GPEByteIOCustomException
	 * @throws GPEByteIOWriteNotPermittedException
	 * @throws GPEByteIOUnsupportedTransferException
	 * @throws GPEResourceUnknownException
	 * @throws GPEMiddlewareRemoteException
	 * @throws GPEMiddlewareServiceException
	 * @throws GPESecurityException
	 */
    public void seekWrite(long offset, StreamableByteIOConstants.SeekOrigin seekOrigin, byte[] data) throws NumberFormatException, GPEByteIOCustomException, GPEByteIOWriteNotPermittedException, GPEByteIOSeekNotPermittedException, GPEByteIOUnsupportedTransferException, GPEResourceUnknownException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException, GPESecurityException, GPEByteIOUnsupportedClientTransferMechanismException, GPEInvalidResourcePropertyQNameException, GPEUnmarshallingException;

    public long getSize() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public long getPosition() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public boolean isReadable() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public boolean isWritable() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public boolean isSeekable() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public List<String> getTransferMechanisms() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public boolean isEndOfStream() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;

    public EndpointReferenceType getDataResource() throws GPEInvalidResourcePropertyQNameException, GPEResourceUnknownException, GPEUnmarshallingException, GPESecurityException, GPEMiddlewareRemoteException, GPEMiddlewareServiceException;
}
