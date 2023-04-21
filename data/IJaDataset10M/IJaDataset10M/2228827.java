package com.hk.bean;

import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;
import com.hk.svr.pub.ImageConfig;

@Table(name = "objphoto")
public class ObjPhoto {

    @Id
    private long photoId;

    @Column
    private long userId;

    @Column
    private long companyId;

    @Column
    private String path;

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getH_0Pic() {
        return ImageConfig.getPhotoPicH_0Url(this.path);
    }

    public String getH_1Pic() {
        return ImageConfig.getPhotoPicH_1Url(this.path);
    }

    public String getH_2Pic() {
        return ImageConfig.getPhotoPicH_2Url(this.path);
    }
}
