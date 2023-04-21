package org.esb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class IProject implements Serializable {

    public abstract int getId();

    public abstract void setId(int id);

    public abstract void setName(String name);

    public abstract String getName();

    public abstract void setDate(Date d);

    public abstract Date getDate();

    public abstract void addMediaFile(MediaFile file);

    public abstract List<MediaFile> getMediaFiles();

    public abstract void addProfile(Profile p);

    public abstract List<Profile> getProfiles();
}
