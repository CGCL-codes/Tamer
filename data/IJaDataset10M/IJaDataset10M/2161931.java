package org.lnicholls.galleon.skins;

import java.util.*;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Descriptor {

    public Descriptor() {
        mImages = new ArrayList();
    }

    public void getImages(java.util.List value) {
        mImages = value;
    }

    public java.util.List getImages() {
        return mImages;
    }

    public Image getImage(String id, int resolution) {
        Image defaultImage = null;
        for (int i = 0; i < mImages.size(); i++) {
            Image image = (Image) mImages.get(i);
            if (image.getId().toLowerCase().equals(id.toLowerCase())) {
                if (defaultImage == null && image.getResolution() == 0) {
                    defaultImage = image;
                }
                if (image.getResolution() == resolution) {
                    return image;
                }
            }
        }
        return defaultImage;
    }

    public void addImage(Image value) {
        mImages.add(value);
    }

    public static class Image {

        public Image() {
        }

        public String getSource() {
            return mSource;
        }

        public void setSource(String value) {
            mSource = value;
        }

        public String getId() {
            return mId;
        }

        public void setId(String value) {
            mId = value;
        }

        public int getResolution() {
            return mResolution;
        }

        public void setResolution(int value) {
            mResolution = value;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        private String mSource;

        private String mId;

        private int mResolution;
    }

    private List mImages;
}
