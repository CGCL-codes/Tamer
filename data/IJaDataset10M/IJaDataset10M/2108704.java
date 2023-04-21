package com.digdia.app.gwt.client.model.service.rpcgwt;

import com.digdia.app.gwt.client.constanst.DataConstant;
import com.digdia.app.gwt.client.model.service.rpcgwt.rpcutil.GwtRpcDataSourceServiceAsync;
import com.digdia.app.gwt.client.model.service.rpcgwt.rpcutil.ListDataModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;

/**
 * Generic abstract {@link GwtRpcDataSourceBase} implementation, supporting server-side paging and sorting.
 * Extend this class if you want to create a GWT RPC DataSource for SmartGWT. This is based on the
 * {@link GwtRpcDataSourceBase} example provided in the smartgwt-extensions.
 * 
 * In order to use this class, you have to implement both
 * {@link GwtRpcDataSourceService} and {@link GwtRpcDataSourceServiceAsync}
 * provided in the same package. To use paging ({@link FetchMode#PAGED}), you'll have to return
 * a {@link GwtRpcResultSet} from the fetch()-method of your {@link GwtRpcDataSourceService}-implementation.
 * 
 * @param <DTO>
 *            type of the transfer object holding the data (will most likely be
 *            a simple POJO), must implement {@link Serializable} or {@link IsSerializable}.
 * @param <R>
 *            any extension of {@link Record}, such as {@link ListGridRecord},
 *            {@link DetailViewerRecord} or {@link TreeNode} to use with your SmartGWT widget.
 * @param <SA>
 *            the asynchronous version of your service. Extend
 *            {@link GwtRpcDataSourceService} and then 
 *            {@link GwtRpcDataSourceServiceAsync} to implement it.
 * 
 * @see GwtRpcDataSourceBase
 * @see GwtRpcDataSourceService
 * @see GwtRpcDataSourceServiceAsync
 * @see GwtRpcResultSet
 * 
 * @author Francois Marbot
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.1
 */
public abstract class GwtRpcDataSource<DTO, R extends Record, SA extends GwtRpcDataSourceServiceAsync<DTO>> extends GwtRpcDataSourceBase {

    private GwtRpcDataSourceServiceAsync<DTO> serviceAsync;

    private Long maxRow;

    private SuccessAjaxListener fetchDataSuccessAjaxListener, addDataSuccessAjaxListener, updateDataSuccessAjaxListener, removeDataSuccessAjaxListener;

    public GwtRpcDataSource(String id) {
        super(id);
        setDataSourceFields();
        serviceAsync = getServiceAsync();
    }

    /**
     * @return a list of {@link DataSourceField}, used to define the fields of
     *         your {@link DataSource}. NOTE: Make sure to set a primary key, as
     *         some problems might occur if it's omitted.
     */
    public abstract List<DataSourceField> getDataSourceFields();

    /**
     * Copies values from the {@link Record} to the data object.
     *
     * @param from
     *            the {@link Record} to copy from.
     * @param to
     *            the data object to copy to.
     */
    public abstract void copyValues(R from, DTO to);

    /**
     * Copies values from the data object to the {@link Record}.
     *
     * @param from
     *            the data object to copy from.
     * @param to
     *            the {@link Record} to copy to.
     */
    public abstract void copyValues(DTO from, R to);

    /**
     * @return the {@link GwtRpcDataSourceServiceAsync} to use, created
     *         using
     *         <code>GWT.create(YourGenericGwtRpcDataSourceService.class)</code>.
     *
     *         This is unfortunately necessary as <code>GWT.create()</code> only
     *         allows class literal as argument. We cannot create a class
     *         literal from a parameterized type because it has no exact runtime
     *         type representation, which is due to type erasure at compile
     *         time.
     */
    public abstract SA getServiceAsync();

    /**
     * @return a new instance of your {@link Record}, such as
     *         <code>new Record()</code> or <code>new ListGridRecord()</code>.
     *
     *         This method is needed because we cannot instantiate parameterized
     *         types at runtime. It also increases flexibility as we can pass
     *         more complex default objects.
     */
    public abstract R getNewRecordInstance();

    /**
     * @return a new instance of your data object, such as
     *         <code>new YourDataObject()</code>.
     *
     *         This method is needed because we cannot instantiate parameterized
     *         types at runtime. It also increases flexibility as we can pass
     *         more complex default objects.
     */
    public abstract DTO getNewDataObjectInstance();

    @Override
    protected final void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
        final Integer startRow = request.getStartRow();
        final Integer endRow = request.getEndRow();
        final Criteria criteria = request.getCriteria();
        final Map<String, String> criterias = (criteria != null) ? criteria.getValues() : new HashMap<String, String>();
        serviceAsync.fetchData(startRow, endRow, id, criterias, new AsyncCallback<ListDataModel<DTO>>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(ListDataModel<DTO> result) {
                final Record[] records;
                if (null != result) {
                    maxRow = result.getMaxRow();
                    if (result.getListData() != null) {
                        records = new Record[result.getListData().size()];
                        int i = 0;
                        for (DTO data : result.getListData()) {
                            R newRec = getNewRecordInstance();
                            copyValues(data, newRec);
                            records[i++] = newRec;
                        }
                        if (startRow != null && endRow != null && result.getListData() instanceof GwtRpcResultSet<?>) {
                            Integer totalRows = ((GwtRpcResultSet<DTO>) result.getListData()).getTotalRows();
                            response.setStartRow(startRow);
                            if (totalRows == null) {
                                throw new NullPointerException("totalRows cannot be null when using GenericGwtRpcList");
                            }
                            response.setEndRow(Math.min(endRow, totalRows));
                            response.setTotalRows(totalRows);
                        }
                    } else {
                        records = new Record[] {};
                        response.setEndRow(0);
                        response.setTotalRows(0);
                    }
                } else {
                    records = new Record[] {};
                    response.setEndRow(0);
                    response.setTotalRows(0);
                }
                response.setData(records);
                processResponse(requestId, response);
                if (fetchDataSuccessAjaxListener != null) {
                    fetchDataSuccessAjaxListener.onSuccessAjaxGwt();
                }
            }
        });
    }

    @Override
    protected final void executeAdd(final String requestId, final DSRequest request, final DSResponse response) {
        R newRec = getNewRecordInstance();
        newRec.setJsObj(request.getData());
        DTO data = getNewDataObjectInstance();
        copyValues(newRec, data);
        serviceAsync.add(data, new AsyncCallback<DTO>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(DTO result) {
                R newRec = getNewRecordInstance();
                copyValues(result, newRec);
                response.setData(new Record[] { newRec });
                processResponse(requestId, response);
            }
        });
    }

    @Override
    protected final void executeUpdate(final String requestId, final DSRequest request, final DSResponse response) {
        R editedRec = getEditedRecord(request);
        DTO data = getNewDataObjectInstance();
        copyValues(editedRec, data);
        serviceAsync.update(data, new AsyncCallback<DTO>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(DTO result) {
                R updatedRec = getNewRecordInstance();
                copyValues(result, updatedRec);
                response.setData(new Record[] { updatedRec });
                processResponse(requestId, response);
            }
        });
    }

    @Override
    protected final void executeRemove(final String requestId, final DSRequest request, final DSResponse response) {
        final R rec = getNewRecordInstance();
        rec.setJsObj(request.getData());
        DTO data = getNewDataObjectInstance();
        copyValues(rec, data);
        serviceAsync.remove(data, new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(Void v) {
                response.setData(new Record[] { rec });
                processResponse(requestId, response);
                executeFetch(requestId, request, response);
            }
        });
    }

    protected final void executeRefreshDML(final String requestId, final DSRequest request, final DSResponse response) {
        final Map<String, String> criterias = new HashMap<String, String>();
        criterias.put(DataConstant.FIND_PARAM, DataConstant.FIND_ALL);
        final String sortBy = request.getAttribute("sortBy");
        final Integer startRow = request.getStartRow();
        final Integer endRow = request.getEndRow();
        serviceAsync.fetch(startRow, endRow, sortBy, criterias, new AsyncCallback<List<DTO>>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(final List<DTO> result) {
                final Record[] records;
                if (null != result) {
                    records = new Record[result.size()];
                    int i = 0;
                    for (DTO data : result) {
                        R newRec = getNewRecordInstance();
                        copyValues(data, newRec);
                        records[i++] = newRec;
                    }
                    if (startRow != null && endRow != null && result instanceof GwtRpcResultSet<?>) {
                        Integer totalRows = ((GwtRpcResultSet<DTO>) result).getTotalRows();
                        response.setStartRow(startRow);
                        if (totalRows == null) {
                            throw new NullPointerException("totalRows cannot be null when using GenericGwtRpcList");
                        }
                        response.setEndRow(Math.min(endRow, totalRows));
                        response.setTotalRows(totalRows);
                    }
                } else {
                    records = new Record[] {};
                    response.setEndRow(0);
                    response.setTotalRows(0);
                }
                response.setData(records);
                processResponse(requestId, response);
            }
        });
    }

    private R getEditedRecord(final DSRequest request) {
        R newRecord = getNewRecordInstance();
        if (request.getOldValues() != null) {
            JavaScriptObject oldValues = request.getOldValues().getJsObj();
            JSOHelper.apply(oldValues, newRecord.getJsObj());
        }
        JavaScriptObject changedData = request.getData();
        JSOHelper.apply(changedData, newRecord.getJsObj());
        return newRecord;
    }

    private void setDataSourceFields() {
        List<DataSourceField> fields = getDataSourceFields();
        if (fields != null) {
            for (DataSourceField field : fields) {
                addField(field);
            }
        }
    }

    public Long getMaxRow() {
        return maxRow;
    }

    public void setAddDataSuccessAjaxListener(SuccessAjaxListener addDataSuccessAjaxListener) {
        this.addDataSuccessAjaxListener = addDataSuccessAjaxListener;
    }

    public void setFetchDataSuccessAjaxListener(SuccessAjaxListener fetchDataSuccessAjaxListener) {
        this.fetchDataSuccessAjaxListener = fetchDataSuccessAjaxListener;
    }

    public void setRemoveDataSuccessAjaxListener(SuccessAjaxListener removeDataSuccessAjaxListener) {
        this.removeDataSuccessAjaxListener = removeDataSuccessAjaxListener;
    }

    public void setUpdateDataSuccessAjaxListener(SuccessAjaxListener updateDataSuccessAjaxListener) {
        this.updateDataSuccessAjaxListener = updateDataSuccessAjaxListener;
    }
}
