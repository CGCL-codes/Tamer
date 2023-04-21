package org.richfaces.demo.slides;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Shaikovsky
 *
 */
public class SlidesBean {

    private static final String FILE_EXT = ".jpg";

    private static final int FILES_COUNT = 9;

    private static final String PATH_PREFIX = "/richfaces/jQuery/images/";

    private static final String PIC_NAME = "pic";

    private List<String> pictures;

    private int currentIndex = 1;

    public SlidesBean() {
        pictures = new ArrayList<String>();
        for (int i = 1; i <= FILES_COUNT; i++) {
            pictures.add(PATH_PREFIX + PIC_NAME + i + FILE_EXT);
        }
    }

    public void next() {
        if (currentIndex < FILES_COUNT) {
            currentIndex++;
        } else {
            setCurrentIndex(1);
        }
    }

    public void previous() {
        if (currentIndex > 1) {
            currentIndex--;
        } else {
            setCurrentIndex(FILES_COUNT);
        }
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getCurrentPicture() {
        return pictures.get(currentIndex - 1);
    }

    public static String getFILE_EXT() {
        return FILE_EXT;
    }

    public static int getFILES_COUNT() {
        return FILES_COUNT;
    }

    public static String getPATH_PREFIX() {
        return PATH_PREFIX;
    }

    public static String getPIC_NAME() {
        return PIC_NAME;
    }
}
