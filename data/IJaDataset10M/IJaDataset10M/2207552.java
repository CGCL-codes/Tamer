package org.designerator.media.thumbs.two;

import java.io.File;
import java.io.FilenameFilter;
import org.designerator.media.image.util.IO;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class ThumbManager2 {

    private static final String IMAGEFP = ".imagefp";

    int width = 120;

    int height = 80;

    private FilenameFilter imageNameFilter;

    public ThumbManager2() {
    }

    public void loadThumnailsThread(final ThumbScroller2 parent, String dirName, String thumbDir, final Display display) {
        if (parent == null) {
            return;
        }
        if (display == null) {
            return;
        }
        if (dirName == null || dirName.equals("")) {
            setNoImages(parent, display);
            parent.layout();
        }
        if (imageNameFilter == null) {
            imageNameFilter = IO.getImageNameFilter();
        }
        final File dir = getWorkingDir(dirName);
        final File localThumbDir;
        if (thumbDir != null) {
            File tmp = new File(thumbDir);
            if (!tmp.isDirectory()) {
                return;
            }
            localThumbDir = tmp;
        } else {
            localThumbDir = getThumbDir(dirName);
        }
        if (dir != null && localThumbDir != null) {
            final File[] files = dir.listFiles(imageNameFilter);
            if (files.length == 0) {
                setNoImages(parent, display);
                parent.layout();
                return;
            }
            new Thread(new Runnable() {

                public void run() {
                    long startTime = System.currentTimeMillis();
                    final Thumb2[] ttc = new Thumb2[files.length];
                    display.syncExec(new Runnable() {

                        public void run() {
                            final ThumbProxy2 thumb = new ThumbProxy2(files[0], localThumbDir, width, height, display);
                            int c = 10;
                            for (int i = 0; i < files.length; i++) {
                                ttc[i] = parent.addThumb(thumb);
                                if (c == i) {
                                    parent.layout();
                                    c += 10;
                                }
                            }
                            parent.layout();
                        }
                    });
                    final ThumbProxy2[] thumbToLoad = new ThumbProxy2[files.length - 1];
                    for (int i = 0; i < thumbToLoad.length; i++) {
                        thumbToLoad[i] = (new ThumbProxy2(files[i + 1], localThumbDir, width, height, display));
                        final ThumbProxy2 proxy = thumbToLoad[i];
                        final int c = i;
                        display.syncExec(new Runnable() {

                            public void run() {
                                ttc[c + 1].setImagePlus(proxy);
                            }
                        });
                    }
                    long loadTime = System.currentTimeMillis() - startTime;
                    System.out.println("ThumbnailManager loadThumnailsThread run: " + loadTime);
                }
            }).start();
        }
    }

    public void setNoImages(final ThumbScroller2 parent, final Display display) {
        final ThumbProxy2 thumb = new ThumbProxy2(display.getSystemImage(SWT.ICON_INFORMATION), "No Images");
        thumb.error = true;
        parent.addThumb(thumb);
    }

    private File getThumbDir(String dirName) {
        File thumbDir = new File(dirName, IMAGEFP);
        if (!thumbDir.exists()) {
            if (!thumbDir.mkdir()) {
                thumbDir = null;
            }
        } else if (!thumbDir.isDirectory()) {
            System.err.println(".imagefp is a File");
            thumbDir = null;
        }
        return thumbDir;
    }

    private File getWorkingDir(String dirName) {
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
