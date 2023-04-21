package uk.org.ogsadai.client.toolkit.presentation.cxf;

import java.util.Iterator;
import java.util.Arrays;
import java.net.URL;
import uk.org.ogsadai.client.toolkit.DataStreamStatus;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.InvalidURIException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.UnhandledException;
import uk.org.ogsadai.client.toolkit.resource.BaseDataSinkResource;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.service.cxf.datasink.ClientFault;
import uk.org.ogsadai.service.cxf.datasink.PutBlock;
import uk.org.ogsadai.service.cxf.datasink.PutNBlocks;
import uk.org.ogsadai.service.cxf.datasink.PutNBlocksNB;
import uk.org.ogsadai.service.cxf.datasink.PutNBlocksNBResponse;
import uk.org.ogsadai.service.cxf.datasink.PutNBlocksResponse;
import uk.org.ogsadai.service.cxf.datasink.ResourceUnknownFault;
import uk.org.ogsadai.service.cxf.datasink.ServerFault;
import uk.org.ogsadai.service.cxf.datasink.WSDataSinkServicePortType;
import uk.org.ogsadai.service.cxf.datasink.service.DataSinkService;
import javax.xml.namespace.QName;
import org.oasis.wsrf.properties.*;

/**
 * The Class CXFDataSinkResource.
 *
 * @author The OGSA-DAI Project Team.
 */
public class CXFDataSinkResource extends BaseDataSinkResource {

    /**
     * Copyright statement.
     */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    /**
     * The stub.
     */
    private WSDataSinkServicePortType port = null;

    private static final QName SERVICE_NAME = new QName("http://ogsadai.org.uk/namespaces/2007/04/service/datasink/service", "DataSinkService");

    /**
     * The Constructor.
     *
     * @param resource the resource
     * @throws ServerException     the server exception
     * @throws InvalidURIException
     *  if the client toolkit generates invalid URIs when
     *                             accessing the stubs.
     */
    public CXFDataSinkResource(final CXFResource resource) throws ServerException, InvalidURIException {
        super(resource);
        try {
            final CXFResource cxfResource = (CXFResource) getResource();
            URL wsdlURL = DataSinkService.WSDL_LOCATION;
            final DataSinkService service = new DataSinkService(wsdlURL, SERVICE_NAME);
            port = service.getWSDataSinkServicePortTypePort();
        } catch (Exception e) {
            throw new UnhandledException(e);
        }
    }

    public void putValue(final DataValue value, DataStreamStatus streamStatus) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutBlock pb = new PutBlock();
            pb.setData(requestBuilder.getDataType(value));
            pb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putBlock(pb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void putValues(final DataValue[] values, DataStreamStatus streamStatus) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutNBlocks pnb = new PutNBlocks();
            pnb.getData().clear();
            pnb.getData().addAll(Arrays.asList(requestBuilder.getDataTypes(values)));
            pnb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pnb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putNBlocks(pnb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void putValues(final Iterator dataValueIterator, DataStreamStatus streamStatus) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutNBlocks pnb = new PutNBlocks();
            pnb.getData().clear();
            pnb.getData().addAll(Arrays.asList(requestBuilder.getDataTypes(dataValueIterator)));
            pnb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pnb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putNBlocks(pnb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void close() throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final PutBlock pb = new PutBlock();
            pb.setEndOfData(true);
            port.putBlock(pb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void closeDueToError() throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final PutBlock pb = new PutBlock();
            pb.setEndOfDataDueToError(true);
            port.putBlock(pb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    /**
     * Maps an exception to a client toolkt exception.
     *
     * @param ex the exception
     * @throws ServerCommsException
     *                         if an error occurs communicating with the server.
     * @throws ServerException       if an internal error occurs at the server.
     * @throws ResourceUnknownException if the resource is unknown to the server.
     * @throws ClientException
     *                      if resource does not contain the specified property.
     */
    protected void mapException(Exception ex) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            throw ex;
        } catch (final ClientFault clientEx) {
            throw CXFExceptionUtil.buildClientException(clientEx.getFaultInfo().getId(), clientEx.getFaultInfo().getParameter().toArray(new String[clientEx.getFaultInfo().getParameter().size()]));
        } catch (final ResourceUnknownFault resourceEx) {
            final ResourceID resourceID = new ResourceID(resourceEx.getFaultInfo().getParameter().get(0));
            throw new ResourceUnknownException(resourceID);
        } catch (final ServerFault serverEx) {
            throw new ServerException(new ErrorID(serverEx.getFaultInfo().getId()), serverEx.getFaultInfo().getParameter().toArray(), serverEx.getCause());
        } catch (final Exception unhandledEx) {
            throw new UnhandledException(unhandledEx);
        }
    }

    public void putValue(DataValue value, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        putValue(value, DataStreamStatus.OPEN, sequenceNumber);
    }

    public void putValue(DataValue value, DataStreamStatus streamStatus, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutBlock pb = new PutBlock();
            pb.setSequenceNumber(sequenceNumber);
            pb.setData(requestBuilder.getDataType(value));
            pb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putBlock(pb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void putValues(DataValue[] values, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        putValues(values, DataStreamStatus.OPEN, sequenceNumber);
    }

    public void putValues(DataValue[] values, DataStreamStatus streamStatus, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutNBlocks pnb = new PutNBlocks();
            pnb.setSequenceNumber(sequenceNumber);
            pnb.getData().clear();
            pnb.getData().addAll(Arrays.asList(requestBuilder.getDataTypes(values)));
            pnb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pnb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putNBlocks(pnb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    public void putValues(Iterator dataValueIterator, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        putValues(dataValueIterator, DataStreamStatus.OPEN, sequenceNumber);
    }

    public void putValues(Iterator dataValueIterator, DataStreamStatus streamStatus, long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        try {
            final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
            final PutNBlocks pnb = new PutNBlocks();
            pnb.setSequenceNumber(sequenceNumber);
            pnb.getData().clear();
            pnb.getData().addAll(Arrays.asList(requestBuilder.getDataTypes(dataValueIterator)));
            pnb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
            pnb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
            port.putNBlocks(pnb);
        } catch (final Exception ex) {
            mapException(ex);
        }
    }

    /**
     * Helper method. Writes multiple data values to the sink resource and
     * flags the status of the data stream after this value is written.
     *
     * @param values     data values to write.
     * @param start     position in array to start writing from (zero based). If
     *                    <code>null</code>, the whole array is written.
     * @param length      number of data values to write starting from
     *              <code>start</code>. If <code>null</code>, the whole array is
     *                    written.
     * @param streamStatus
     *           the status of the data stream after this value has been
     *           written. This flag can used to eliminate the extra call to
     *           the server that could be made by calling the <code>close</code>
     *           or <code>closeDueToError</code> methods.
     * @param sequenceNumber
     *           sequence number of the call: 0, 1, 2, ... If <code>null</code>,
     *                       sequence number will not be set.
     * @return number of data values written.
     * @throws ServerCommsException
     *                          if an error occurs communicating with the server.
     * @throws ServerException    if an internal error occurs at the server.
     * @throws ResourceUnknownException
     *            if the resource is unknown to the server or is the wrong type.
     * @throws ClientException
     *              if the data could not be written to the data sink due to the
     *                                  state of the data sink.
     */
    protected int putValuesNBHelper(DataValue[] values, Integer start, Integer length, DataStreamStatus streamStatus, Long sequenceNumber) throws ServerCommsException, ServerException, ResourceUnknownException, ClientException {
        mCallController.preDataCall(length);
        final CXFRequestBuilder requestBuilder = new CXFRequestBuilder();
        final PutNBlocksNB pnbnb = new PutNBlocksNB();
        DataValue[] putValues = null;
        if (start != null || length != null) {
            putValues = new DataValue[length];
            System.arraycopy(values, start + 0, putValues, 0, length);
        } else {
            putValues = values;
        }
        pnbnb.getData().clear();
        pnbnb.getData().addAll(Arrays.asList(requestBuilder.getDataTypes(putValues)));
        if (sequenceNumber != null) {
            pnbnb.setSequenceNumber(sequenceNumber);
        }
        pnbnb.setEndOfData(streamStatus == DataStreamStatus.CLOSED);
        pnbnb.setEndOfDataDueToError(streamStatus == DataStreamStatus.CLOSED_DUE_TO_ERROR);
        int accepted = 0;
        try {
            PutNBlocksNBResponse response = port.putNBlocksNB(pnbnb);
            accepted = response.getBlocksAccepted();
        } catch (final Exception ex) {
            mapException(ex);
        }
        mCallController.postDataCall(length, accepted);
        return accepted;
    }

    public PutNBlocksResponse putNBlocks(PutNBlocks putNBlocksParameters) throws ServerException, ResourceUnknownException, ClientException {
        uk.org.ogsadai.service.cxf.datasink.PutNBlocksResponse putNBlocksReturn = null;
        try {
            putNBlocksReturn = port.putNBlocks(putNBlocksParameters);
        } catch (final Exception ex) {
            mapException(ex);
        }
        return putNBlocksReturn;
    }

    public void destroy() throws ResourceUnknownException {
        try {
            port.destroy();
        } catch (final Exception ex) {
            throw new UnhandledException(ex);
        }
    }

    public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties request) throws UnknownQueryExpressionDialectFault, InvalidQueryExpressionFault, QueryEvaluationErrorFault, InvalidResourcePropertyQNameFault, org.oasis.wsrf.properties.ResourceUnknownFault {
        QueryResourcePropertiesResponse queryResourcePropertiesReturn = port.queryResourceProperties(request);
        return queryResourcePropertiesReturn;
    }
}
