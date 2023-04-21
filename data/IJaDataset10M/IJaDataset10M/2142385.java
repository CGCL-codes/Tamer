package net.asfun.jvalog.vo;

import java.util.Date;
import net.asfun.jvalog.common.InteractException;
import net.asfun.jvalog.entity.File;

public class Media {

    File file;

    public Media(File file) {
        if (file == null) {
            throw new InteractException("Media not found");
        }
        this.file = file;
    }

    public Long getId() {
        return file.getKey().getId();
    }

    public Date getCreation() {
        return file.getDate();
    }

    public String getName() {
        return file.getName();
    }

    public String getUrl() {
        return Site.getInstance().getUrl() + "media/" + file.getName();
    }

    public int getSize() {
        return file.getBytes().getBytes().length / 1024;
    }

    public String getContentType() {
        return file.getMime();
    }

    public int getLength() {
        return file.getBytes().getBytes().length;
    }

    public byte[] getByte() {
        return file.getBytes().getBytes();
    }
}
