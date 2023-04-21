package org.magicable.openmuseau.client.behavior;

import org.magicable.openmuseau.client.services.StorageService;
import org.magicable.openmuseau.client.services.StorageServiceAsync;
import org.magicable.openmuseau.client.views.CreateCollectionView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class CreateCollectionClickHandler extends BaseHandler implements ClickHandler {

    private final StorageServiceAsync storageService = GWT.create(StorageService.class);

    CreateCollectionView view;

    public CreateCollectionClickHandler(CreateCollectionView view) {
        this.view = view;
    }

    @Override
    public void onClick(ClickEvent event) {
        sendDataToServer();
    }

    private void sendDataToServer() {
        errorPanel.clear();
        msgPanel.clear();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                errorPanel.add(new Label(caught.getMessage()));
            }

            public void onSuccess(String result) {
                msgPanel.add(new Label(result));
            }
        };
        try {
            storageService.storeToServer(this.view.getModel(), callback);
        } catch (Exception e) {
            errorPanel.add(new Label(e.getMessage()));
        }
    }
}
