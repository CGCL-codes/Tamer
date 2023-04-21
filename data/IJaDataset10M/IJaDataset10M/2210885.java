package net.sf.gratepic.gui;

import flickr.response.Photo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import org.openide.util.RequestProcessor;

/**
 * @author leon
 */
public class VisualPhotoListModel extends AbstractListModel {

    private List<VisualPhoto> visualPhotos;

    private static RequestProcessor imageFetcher = new RequestProcessor("imageFetcher", 3);

    private List<Photo> photos;

    public void addFirst(VisualPhoto vp) {
        visualPhotos.add(0, vp);
        fireIntervalAdded(this, 0, 0);
    }

    public VisualPhoto getElementAt(int index) {
        return visualPhotos.get(index);
    }

    public void setIconUpdateEnabled(boolean enabled) {
        int i = 0;
        if (visualPhotos == null) {
            return;
        }
        for (VisualPhoto visualPhoto : visualPhotos) {
            visualPhoto.setIconUpdateEnabled(enabled);
            if (enabled) {
                VisualPhoto vp = getElementAt(i);
                if (vp.getIcon() == null) {
                    updateIcon(i);
                }
            }
            i++;
        }
    }

    public int getSize() {
        if (visualPhotos == null) {
            return 0;
        } else {
            return visualPhotos.size();
        }
    }

    public void updateIcon(final int index) {
        final VisualPhoto visualPhoto = visualPhotos.get(index);
        if (!visualPhoto.isIconUpdateEnabled()) {
            return;
        }
        imageFetcher.post(new Runnable() {

            public void run() {
                try {
                    if (!visualPhoto.isIconUpdateEnabled()) {
                        return;
                    }
                    visualPhoto.setIcon(new ImageIcon(visualPhoto.getPhoto().toThumbnailURL()));
                    fireContentsChanged(VisualPhotoListModel.this, index, index);
                } catch (Exception ex) {
                }
            }
        }, 50);
    }

    public void setPhotos(List<Photo> photos) {
        int oldSize = getSize();
        this.photos = photos;
        visualPhotos = new ArrayList<VisualPhoto>(photos.size());
        for (Photo photo : photos) {
            visualPhotos.add(new VisualPhoto(photo));
        }
        fireIntervalRemoved(this, 0, oldSize);
        fireIntervalAdded(this, 0, getSize());
    }
}
