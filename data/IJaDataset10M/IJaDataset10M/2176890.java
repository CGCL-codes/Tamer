package net.sf.gratepic.gui.user;

import net.sf.gratepic.gui.*;
import net.sf.gratepic.gui.comp.VisualPhotoSet;
import net.sf.gratepic.gui.comp.ThumbnailListModel;
import net.sf.gratepic.gui.comp.Thumbnail;
import flickr.response.PhotoSet;
import flickr.response.PhotoSets;
import flickr.service.Flickr;
import flickr.service.SwingFlickrResponseHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class PhotoSetsComboBoxModel extends ThumbnailListModel implements ComboBoxModel {

    private String userId;

    private PhotoSets photoSets;

    private List<VisualPhotoSet> visualPhotoSets;

    private boolean loading;

    private Object selectedItem;

    public PhotoSetsComboBoxModel(String userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void reload() {
        loadPhotoSets();
    }

    public int getSize() {
        if (photoSets == null) {
            loadPhotoSets();
            return 0;
        } else {
            return photoSets.getPhotoSets().size();
        }
    }

    public VisualPhotoSet getElementAt(int index) {
        return visualPhotoSets.get(index);
    }

    private void loadPhotoSets() {
        if (loading) {
            return;
        }
        loading = true;
        final ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(PhotoSetsComboBoxModel.class, "loadingPhotoSets"));
        Flickr.get().getPhotoSetsService().loadPhotoSets(userId, new SwingFlickrResponseHandler<PhotoSets>() {

            @Override
            public void doBeforeStart() {
                ph.start();
            }

            @Override
            public void doCallFailed(Exception ex) {
                FlickrErrorHandler.handle(NbBundle.getMessage(PhotoSetsComboBoxModel.class, "photoSetsNotLoaded"), ex);
            }

            @Override
            public void doAfterFinish() {
                ph.finish();
                loading = false;
            }

            @Override
            public void doCallFinished(PhotoSets photoSets) {
                PhotoSetsComboBoxModel.this.photoSets = photoSets;
                List<VisualPhotoSet> vpss = new ArrayList<VisualPhotoSet>();
                for (PhotoSet photoSet : photoSets.getPhotoSets()) {
                    VisualPhotoSet vps = new VisualPhotoSet(photoSet);
                    vpss.add(vps);
                }
                PhotoSetsComboBoxModel.this.visualPhotoSets = vpss;
                fireContentsChanged(PhotoSetsComboBoxModel.this, 0, getSize());
            }
        });
    }

    public void setSelectedItem(Object anItem) {
        this.selectedItem = anItem;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    protected Thumbnail getThumbnailAt(int index) {
        return visualPhotoSets.get(index);
    }
}
