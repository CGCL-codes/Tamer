package cz.fi.muni.xkremser.editor.client.gwtrpcds;

import java.util.ArrayList;
import javax.inject.Inject;
import com.allen_sauer.gwt.log.client.Log;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import cz.fi.muni.xkremser.editor.client.LangConstants;
import cz.fi.muni.xkremser.editor.client.dispatcher.DispatchCallback;
import cz.fi.muni.xkremser.editor.client.util.Constants;
import cz.fi.muni.xkremser.editor.shared.rpc.InputQueueItem;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ScanInputQueueAction;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ScanInputQueueResult;

/**
 * The Class InputTreeGwtRPCDS.
 */
public class InputTreeGwtRPCDS extends AbstractGwtRPCDS {

    /** The dispatcher. */
    private final DispatchAsync dispatcher;

    /**
     * Instantiates a new input tree gwt rpcds.
     * 
     * @param dispatcher
     *        the dispatcher
     */
    @Inject
    public InputTreeGwtRPCDS(DispatchAsync dispatcher, LangConstants lang) {
        this.dispatcher = dispatcher;
        DataSourceField field;
        field = new DataSourceTextField(Constants.ATTR_ID, "id");
        field.setPrimaryKey(true);
        field.setRequired(true);
        field.setHidden(true);
        addField(field);
        field = new DataSourceTextField(Constants.ATTR_PARENT, "parent");
        field.setForeignKey(Constants.ATTR_ID);
        field.setHidden(true);
        addField(field);
        field = new DataSourceTextField(Constants.ATTR_NAME, lang.name());
        field.setRequired(true);
        field.setHidden(true);
        addField(field);
        field = new DataSourceTextField(Constants.ATTR_BARCODE, "ID");
        field.setAttribute("width", "60%");
        field.setRequired(true);
        addField(field);
        field = new DataSourceTextField(Constants.ATTR_INGEST_INFO, "ingestInfo");
        field.setHidden(true);
        field.setRequired(true);
        addField(field);
    }

    @Override
    protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
        String id = (String) request.getCriteria().getValues().get(Constants.ATTR_PARENT);
        if (id == null) {
            id = request.getCriteria().getAttributeAsString(Constants.ATTR_PARENT);
        }
        dispatcher.execute(new ScanInputQueueAction(id, false), new DispatchCallback<ScanInputQueueResult>() {

            @Override
            public void callbackError(final Throwable cause) {
                Log.error("Handle Failure:", cause);
                response.setStatus(RPCResponse.STATUS_FAILURE);
            }

            @Override
            public void callback(final ScanInputQueueResult result) {
                ArrayList<InputQueueItem> items = result.getItems();
                ListGridRecord[] list = new ListGridRecord[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    ListGridRecord record = new ListGridRecord();
                    copyValues(items.get(i), record);
                    list[i] = record;
                }
                response.setData(list);
                response.setTotalRows(items.size());
                processResponse(requestId, response);
            }
        });
    }

    @Override
    protected void executeAdd(final String requestId, final DSRequest request, final DSResponse response) {
    }

    @Override
    protected void executeUpdate(final String requestId, final DSRequest request, final DSResponse response) {
    }

    @Override
    protected void executeRemove(final String requestId, final DSRequest request, final DSResponse response) {
    }

    /**
     * Copy values.
     * 
     * @param from
     *        the from
     * @param to
     *        the to
     */
    private static void copyValues(InputQueueItem from, ListGridRecord to) {
        to.setAttribute(Constants.ATTR_ID, from.getPath());
        to.setAttribute(Constants.ATTR_BARCODE, from.getBarcode());
        to.setAttribute(Constants.ATTR_INGEST_INFO, from.getIngestInfo());
        to.setAttribute(Constants.ATTR_NAME, from.getName());
    }
}
