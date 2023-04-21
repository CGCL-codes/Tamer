package dk.hewison.client.panels;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.Widget;
import dk.hewison.client.domain.Photo;
import dk.hewison.client.Log;
import java.util.List;

/**
 * @author John Hewison
 * @author $LastChangedBy: john.hewison $:  $ $LastChangedDate: 2009-02-01 06:50:11 -0500 (Sun, 01 Feb 2009) $:
 * @version $Revision: 78 $:
 */
public class ImageSheet extends Composite implements LoadListener {

    public static final int THUMBNAIL_SIZE = 100;

    List<Photo> photos;

    FlowPanel flowPanel;

    int imageCount = 0;

    int loadedCount = 0;

    long start;

    public ImageSheet() {
        flowPanel = new FlowPanel();
        flowPanel.setStyleName("imagesheet");
        flowPanel.setWidth("100%");
        initWidget(flowPanel);
    }

    public void setPhotos(List<Photo> photos) {
        start = System.currentTimeMillis();
        this.photos = photos;
        redraw();
    }

    private void redraw() {
        flowPanel.clear();
        imageCount = photos.size();
        for (Photo photo : photos) {
            flowPanel.add(getThumbnail(photo));
        }
    }

    private Widget getThumbnail(Photo photo) {
        ThumbnailPanel thumbnailPanel = new ThumbnailPanel(photo.getThumbnailUrl(), this);
        thumbnailPanel.setPixelSize(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        thumbnailPanel.setStyleName("myimage");
        return thumbnailPanel;
    }

    public void fadeIn() {
    }

    /**
     * Fired when a widget encounters an error while loading.
     *
     * @param sender the widget sending the event.
     */
    public void onError(Widget sender) {
    }

    /**
     * Fired when a widget finishes loading.
     *
     * @param sender the widget sending the event.
     */
    public void onLoad(Widget sender) {
        loadedCount++;
        if (loadedCount == imageCount) {
            long took = System.currentTimeMillis() - start;
            Log.debug("Took " + took + " ms to load " + imageCount + " user.");
        }
    }
}
