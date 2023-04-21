package edu.patrones.presentacion.view;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import edu.patrones.presentacion.model.Picture;
import edu.patrones.presentacion.service.PictureService;

@Component("pictureList")
@Scope("request")
public class PicturesList {

    PictureService pictureService = null;

    public PictureService getPictureService() {
        return pictureService;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public List<Picture> getOilPictures() {
        return pictureService.loadOils();
    }

    public List<Picture> getWaterColorPictures() {
        return pictureService.loadWaterColors();
    }

    public List<Picture> getPictures() {
        return pictureService.loadAll();
    }
}
