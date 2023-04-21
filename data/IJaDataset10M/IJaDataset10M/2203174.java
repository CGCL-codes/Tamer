package org.designerator.media.internet.picasa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.designerator.common.data.Album;
import org.designerator.common.interfaces.IThumb;
import org.designerator.common.interfaces.IThumbAlbum;
import org.designerator.common.interfaces.IThumbsContainer;
import org.designerator.common.string.StringUtil;
import org.designerator.media.MediaPlugin;
import org.designerator.media.thumbs.ThumbUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

public class PicasaUpdateAction extends Action {

    public static void delete(final List<PhotoEntry> photos) {
        if (photos != null && photos.size() > 0) {
            for (PhotoEntry entry : photos) {
                try {
                    entry.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateTitle(PhotoEntry entry, String title) {
        if (!StringUtil.isEmpty(title) && entry != null) {
            entry.setDescription(new PlainTextConstruct(title));
            entry.setTitle(new PlainTextConstruct(title));
            try {
                entry.update();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    IThumbsContainer thumbScroller;

    private PicasawebService service;

    public PicasaUpdateAction(IThumbsContainer thumbScroller1) {
        this.thumbScroller = thumbScroller1;
        setText(Messages.PicasaUpdateAction_title);
        setToolTipText(Messages.PicasaUpdateAction_tip);
    }

    /**
	 * @param photos
	 * @param thumb
	 * @return PhotoEntry looks for index and checks last modified <
	 *         photoentry.getEdited
	 */
    public PhotoEntry findPhotoEntryOfThumb(final List<PhotoEntry> photos, IThumb thumb) {
        if (photos == null || thumb == null) {
            return null;
        }
        for (PhotoEntry pho : photos) {
            String checksum = pho.getChecksum();
            if (!StringUtil.isEmpty(checksum)) {
                try {
                    Integer index2 = Integer.valueOf(checksum);
                    if (thumb.getIndexKey() == index2) {
                        long lastModified = thumb.getSysfile().lastModified();
                        DateTime edited = pho.getEdited();
                        if (edited == null) {
                            return pho;
                        }
                        long value = edited.getValue();
                        if (lastModified < value) {
                            return pho;
                        }
                        return null;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getTitle(PhotoEntry entry) {
        if (entry == null) {
            return StringUtil.EMTPY;
        }
        TextConstruct entrytitle = entry.getTitle();
        if (entrytitle != null && entrytitle.getPlainText() != null) {
            return entrytitle.getPlainText();
        }
        TextConstruct entrydesc = entry.getDescription();
        if (entrydesc != null && entrydesc.getPlainText() != null) {
            return entrydesc.getPlainText();
        }
        return StringUtil.EMTPY;
    }

    public boolean isValidThumb(IThumb thumb, String title) {
        return !StringUtil.isEmpty(title) && thumb.getIndexKey() > 0;
    }

    public void run() {
        if (thumbScroller == null || thumbScroller.getControl().isDisposed()) {
            return;
        }
        IThumbAlbum activeDisplayAlbum = thumbScroller.getActiveDisplayAlbum();
        if (activeDisplayAlbum == null) {
            return;
        }
        Album album = activeDisplayAlbum.getAlbum();
        if (album != null) {
            PicasaManager picasaManager = PicasaManager.getInstance();
            service = picasaManager.isConnected();
            if (service == null) {
                PicasaLoginWizard.createDialog(thumbScroller.getControl().getShell());
            }
            if ((service = picasaManager.isConnected()) != null) {
                List<IThumb> ts = activeDisplayAlbum.getThumbsWithChildren();
                runUpdateJob(album, ts.toArray(new IThumb[ts.size()]));
            }
        }
    }

    public void runUpdateJob(final Album album, final IThumb[] iThumbs) {
        Job job = new Job("Picasa update") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                monitor.beginTask(Messages.PicasaUpdateAction_updating, iThumbs.length + 20);
                AlbumEntry picasaAlbum = null;
                if (service == null) {
                    monitor.done();
                    return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_logerror);
                }
                monitor.subTask(Messages.PicasaUpdateAction_logsuccess);
                monitor.worked(5);
                try {
                    picasaAlbum = findMatchingPicasaAlbum(album);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_logerror2);
                } catch (ServiceException e) {
                    e.printStackTrace();
                    return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_logerror2);
                }
                if (picasaAlbum == null) {
                    return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_noalbum);
                }
                monitor.worked(5);
                try {
                    final List<PhotoEntry> photos = PicasawebClient.getPhotos(picasaAlbum, service);
                    monitor.subTask(Messages.PicasaUpdateAction_getsuccess);
                    if (photos == null) {
                        return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_nofoto);
                    }
                    final int thumbSize = iThumbs.length;
                    if (thumbSize < 1) {
                        return new Status(IStatus.ERROR, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_nofoto);
                    }
                    final List<IThumb> forUpload = new ArrayList<IThumb>();
                    for (int i = 0; i < iThumbs.length; i++) {
                        IThumb thumb = iThumbs[i];
                        String title = ThumbUtils.getTitle(thumb);
                        if (isValidThumb(thumb, title)) {
                            PhotoEntry entry = findPhotoEntryOfThumb(photos, thumb);
                            if (entry == null) {
                                forUpload.add(thumb);
                            } else {
                                if (monitor.isCanceled()) {
                                    return PicasaWizard.getCancelStatus();
                                }
                                String entrytitle = getTitle(entry);
                                if (!title.equalsIgnoreCase(entrytitle)) {
                                    monitor.subTask(Messages.PicasaUpdateAction_updatingtitles);
                                    updateTitle(entry, title);
                                }
                                photos.remove(entry);
                            }
                        }
                    }
                    if (monitor.isCanceled()) {
                        return PicasaWizard.getCancelStatus();
                    }
                    monitor.worked(5);
                    monitor.subTask(Messages.PicasaUpdateAction_deleting);
                    delete(photos);
                    monitor.worked(5);
                    if (monitor.isCanceled()) {
                        return PicasaWizard.getCancelStatus();
                    }
                    double rf = album.getResizeFactor() / 100.0;
                    if (rf == 0) {
                        rf = 0.25;
                    }
                    PicasaManager pm = PicasaManager.getInstance();
                    for (IThumb toUpload : forUpload) {
                        try {
                            if (monitor.isCanceled()) {
                                return PicasaWizard.getCancelStatus();
                            }
                            pm.upLoad(picasaAlbum, monitor, rf, toUpload);
                            monitor.worked(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                monitor.done();
                return new Status(IStatus.OK, MediaPlugin.PLUGIN_ID, Messages.PicasaUpdateAction_update1 + picasaAlbum + Messages.PicasaUpdateAction_update2);
            }
        };
        job.setUser(true);
        job.schedule();
    }

    public AlbumEntry findMatchingPicasaAlbum(final Album album) throws IOException, ServiceException {
        IThumbAlbum activeDisplayAlbum = null;
        if (thumbScroller != null && thumbScroller.getControl() != null && !thumbScroller.getControl().isDisposed()) {
            activeDisplayAlbum = thumbScroller.getActiveDisplayAlbum();
        }
        List<AlbumEntry> albums = PicasawebClient.getAlbums(service);
        for (AlbumEntry albumEntry : albums) {
            String title = albumEntry.getTitle().getPlainText();
            if (title != null && title.equalsIgnoreCase(album.getName())) {
                return albumEntry;
            }
            if (activeDisplayAlbum != null) {
                if (title != null && title.equalsIgnoreCase(activeDisplayAlbum.getName())) {
                    return albumEntry;
                }
            }
        }
        return null;
    }
}
