package trungsi.gea.photos.controller;

import static trungsi.gea.photos.util.StreamUtils.toByteArray;
import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import trungsi.gea.photos.domain.Folder;
import trungsi.gea.photos.domain.Photo;
import trungsi.gea.photos.service.PhotoService;
import trungsi.gea.photos.util.JsonUtils;

/**
 * @author trungsi
 *
 */
@UrlBinding("/photo-admin")
public class PhotoActionBean extends SecurityActionBean {

    private static Log log = LogFactory.getLog(PhotoActionBean.class);

    @SpringBean
    private PhotoService photoService;

    private Folder folder;

    private Photo photo;

    private FileBean photoFile;

    private FileBean thumbnailFile;

    private FileBean slideshowFile;

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhotoFile(FileBean photoFile) {
        this.photoFile = photoFile;
    }

    public void setThumbnailFile(FileBean thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }

    public void setSlideshowFile(FileBean slideshowFile) {
        this.slideshowFile = slideshowFile;
    }

    @Before(on = "save")
    public void beforeSave() {
    }

    @After(on = "save")
    public void afterSave() {
        System.out.println("Used memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }

    @HandlesEvent("save")
    public Resolution save() {
        try {
            if (photoFile != null && photoFile.getSize() > 0) {
                photo.setPhoto(toByteArray(photoFile.getInputStream()));
            }
            if (slideshowFile != null && slideshowFile.getSize() > 0) {
                photo.setSlideshow(toByteArray(slideshowFile.getInputStream()));
            }
            if (thumbnailFile != null && thumbnailFile.getSize() > 0) {
                photo.setThumbnail(toByteArray(thumbnailFile.getInputStream()));
            }
            photoService.save(photo);
            if (photo.getId() == null) {
                throw new RuntimeException("photo " + photo + " could not be saved");
            }
            JSONObject json = getJsonPhotoResponse();
            return new StreamingResolution("text/html", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            return new StreamingResolution("text/html", "{success : false, error : '" + e + "'}");
        }
    }

    private JSONObject getJsonPhotoResponse() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("photo", JsonUtils.toJson(photo));
        return json;
    }

    @HandlesEvent("delete")
    public Resolution delete() {
        try {
            photoService.delete(photo);
            return new StreamingResolution("application/json", "{success : true}");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            return new StreamingResolution("application/json", "{success : false, error : '" + e + "'}");
        }
    }
}
