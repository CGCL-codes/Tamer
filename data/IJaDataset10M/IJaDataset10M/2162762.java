package org.gruposp2p.dnie.client.ui.widget.dynatable;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import java.util.ArrayList;
import java.util.List;
import org.gruposp2p.dnie.client.dto.DocumentToSignDTO;
import org.gruposp2p.dnie.client.event.DocumentToSignChangeEvent;
import org.gruposp2p.dnie.client.event.DocumentToSignChangeHandler;
import org.gruposp2p.dnie.client.event.EventBusManager;
import org.gruposp2p.dnie.client.json.JSONToDTO;
import org.gruposp2p.dnie.client.server.ServerFacade;
import org.gruposp2p.dnie.client.server.StatusData;
import org.gruposp2p.dnie.client.ui.widget.GlassedDialog;
import org.gruposp2p.dnie.client.util.URLConstants;

/**
 *
 * @author jj
 */
public class UploadedDocumentsDynaTableProvider implements DynaTableDataProvider, DocumentToSignChangeHandler {

    private int lastMaxRows = -1;

    private int lastStartRow = -1;

    private RowDataAcceptor rowDataAcceptor = null;

    private int startRow;

    private int maxRows;

    public UploadedDocumentsDynaTableProvider() {
        EventBusManager.getInstance().registerToDocumentToSignChange(this);
    }

    public void updateRowData(final int startRow, final int maxRows, final RowDataAcceptor acceptor) {
        this.startRow = startRow;
        this.maxRows = maxRows;
        if (startRow == lastStartRow) {
            if (maxRows == lastMaxRows) {
                acceptor.accept(startRow, new ArrayList<DocumentToSignDTO>());
                return;
            }
        }
        rowDataAcceptor = acceptor;
        ServerFacade.doGet(URLConstants.getUserDocumentsToSignGetUrl(startRow, maxRows), new ServerRequestCallback());
    }

    public void processDocumentToSign(DocumentToSignChangeEvent event) {
        if (event.eventType != DocumentToSignChangeEvent.DELETED) {
            if (event.cardinality == DocumentToSignChangeEvent.MULTIPLE) {
                List<DocumentToSignDTO> documents = event.documents;
                rowDataAcceptor.accept(startRow, documents);
            } else {
                rowDataAcceptor.insertNewElement();
            }
        } else {
            rowDataAcceptor.deleteElement();
        }
    }

    private class ServerRequestCallback implements RequestCallback {

        @Override
        public void onError(Request request, Throwable exception) {
            StatusData status = new StatusData();
            status.description = exception.getMessage();
            GlassedDialog.getInstance().showErrorGettingResource(status);
        }

        @Override
        public void onResponseReceived(Request request, Response response) {
            if (response.getStatusCode() == Response.SC_OK) {
                JSONValue jsonValue = JSONParser.parse(response.getText());
                List<DocumentToSignDTO> documents = JSONToDTO.getDocumentsToSignDTO(jsonValue);
                DocumentToSignChangeEvent documentToSignChangeEvent = new DocumentToSignChangeEvent(documents);
                documentToSignChangeEvent.cardinality = DocumentToSignChangeEvent.MULTIPLE;
                EventBusManager.getInstance().fireEvent(documentToSignChangeEvent);
            } else {
                StatusData statusData = new StatusData();
                statusData.code = String.valueOf(response.getStatusCode());
                statusData.description = response.getText();
                GlassedDialog.getInstance().showErrorGettingResource(statusData);
            }
        }
    }
}
