package it.jwallpaper.plugins.filesystem.cache;

import java.util.Comparator;

public class OldestAccessedImageInfoComparator implements Comparator<ImageInfo> {

    public int compare(ImageInfo o1, ImageInfo o2) {
        if (o1.getLastAccessed() - o2.getLastAccessed() < 0) {
            return 1;
        } else if (o1.getLastAccessed() - o2.getLastAccessed() > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
