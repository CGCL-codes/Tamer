package org.magicable.openmuseau.client.behavior;

import org.magicable.openmuseau.client.services.StorageService;
import org.magicable.openmuseau.client.services.StorageServiceAsync;
import org.magicable.openmuseau.client.views.ListCollectionView;
import org.magicable.openmuseau.shared.models.CollectionModel;
import org.magicable.openmuseau.shared.models.CollectionModelList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class ShowListCollectionCommand extends ShowCommand {

    private final StorageServiceAsync storageService = GWT.create(StorageService.class);

    @Override
    public void execute() {
        super.execute();
        final ListCollectionView view = new ListCollectionView();
        try {
            storageService.retrieveFromServer(new AsyncCallback<CollectionModelList>() {

                @Override
                public void onSuccess(CollectionModelList result) {
                    for (CollectionModel c : result) {
                        view.add(c);
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    errorPanel.add(new Label(caught.getMessage()));
                }
            });
        } catch (Exception e) {
            errorPanel.add(new Label(e.getMessage()));
        }
        this.mainContainer.add(view);
    }
}
