package org.shopformat.controller.admin.marketing;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.shopformat.services.ImageUtils;

public class ImageHandler {

    private static Log log = LogFactory.getLog(ImageHandler.class);

    public void imageChanged(ValueChangeEvent event) {
        UploadedFile imageFile = (UploadedFile) event.getNewValue();
        if (imageFile != null) {
            try {
                Image image = ImageIO.read(imageFile.getInputStream());
                String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getContext("/content").getRealPath("/images/contentManagement");
                ImageUtils.saveImage(image, new File(path + "/" + imageFile.getFilename()).getPath());
            } catch (IOException ex) {
                log.error("Problem saving content image", ex);
            }
        }
    }

    public String getImageList() {
        String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getContext("/content").getRealPath("/images/contentManagement");
        File directory = new File(path);
        File[] fileArray = directory.listFiles();
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < fileArray.length; i++) {
            File file = fileArray[i];
            String fileName = file.getName();
            buff.append("[\"");
            buff.append(fileName);
            buff.append("\", \"");
            buff.append("/images/contentManagement");
            buff.append("/");
            buff.append(fileName);
            buff.append("\"]");
            if (i < fileArray.length - 1) {
                buff.append(",\n");
            }
        }
        return buff.toString();
    }
}
