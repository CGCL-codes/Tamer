package gov.nih.niaid.bcbb.nexplorer3.client.rpc;

import gov.nih.niaid.bcbb.nexplorer3.client.Mediator;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class DeleteUploadedFileCallback implements RequestCallback {

    Mediator mediator = Mediator.getInstance();

    private InputDataItemClient i;

    private String fileName;

    public DeleteUploadedFileCallback(InputDataItemClient i) {
        this.i = i;
        this.fileName = i.getFileName();
        mediator.showAjaxLoader("Deleting file from the server - " + fileName, true);
    }

    public void onError(Request request, Throwable exception) {
        mediator.showErrorMessage("Error deleting file content from the server - " + fileName);
    }

    public void onResponseReceived(Request request, Response response) {
        String message = response.getText();
        if (message.startsWith("Success")) {
            mediator.showAjaxLoader("Deleted file from the server - " + fileName, false);
            mediator.deleteUploadedContent(i);
        } else {
            mediator.showErrorMessage(message);
        }
    }
}
