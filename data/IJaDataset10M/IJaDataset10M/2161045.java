package org.picalbums.client;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.picalbums.client.services.AlbumServiceAsync;

/**
 * Base class for page item widgets.
 * @author itamar
 */
public abstract class PageItemWidget extends Composite implements HasDragHandle {

    protected long myId;

    public PageItemWidget(long myId) {
        this.myId = myId;
    }

    /**
     * Handles deletion of the item if necessary. To be called from the onClick
     * handler.
     */
    void handleDeletion() {
        if (Window.confirm("Are you sure you want to delete this item?")) {
            AlbumServiceAsync albumService = ClientContext.obtain().getAlbumService();
            AsyncCallback<Boolean> callback = createPageRefreshCallback();
            albumService.deleteItem(myId, callback);
        }
    }

    protected AsyncCallback<Boolean> createPageRefreshCallback() {
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean result) {
                if (result) {
                    ClientContext.obtain().getAlbumViewWidget().getAlbumPageViewWidget().refreshDisplay();
                } else {
                    onFailure(null);
                }
            }

            public void onFailure(Throwable caught) {
                Window.alert(Util.TECHNICAL_ERROR_MESSAGE);
            }
        };
        return callback;
    }

    /**
     * @return the draggable widget that should be used as the drag handle (the widget
     * the user clicks and drags) of the page item widget. This widget should implement
     * the interface SourcesMouseEvents.
     */
    public abstract Widget getDragHandle();

    /**
     * @return A draggable proxy - a new widget, to be shown when dragging
     */
    public abstract Widget createDragProxy();

    /**
     * Repositions the page item to the specified location, saving the new
     * location on the server side.
     * @param posX
     * @param posY
     */
    void reposition(int posX, int posY) {
        if (posX < 0 || posY < 0) {
            Window.alert("Error: Item cannot be positioned this way");
            return;
        }
        AlbumServiceAsync albumService = ClientContext.obtain().getAlbumService();
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean result) {
                if (!result) onFailure(null);
            }

            public void onFailure(Throwable caught) {
                Window.alert(Util.TECHNICAL_ERROR_MESSAGE);
            }
        };
        albumService.repositionItem(myId, posX, posY, callback);
    }
}
