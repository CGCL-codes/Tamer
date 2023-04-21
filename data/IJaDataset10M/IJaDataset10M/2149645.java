package tufts.vue.ibisimage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import tufts.vue.*;
import tufts.vue.ibisicon.*;

public class IBISAnswerImage extends IBISImage {

    private static BufferedImage mImage = VueResources.getBufferedImage("IBISNodeTool.answer.image");

    private static File mImageFile = createImageFile(VueResources.getString("IBISNodeTool.answer.image.filename"), mImage);

    private static Resource mImageResource = new LWMap("dummy map").getResourceFactory().get(mImageFile);

    private IBISImageIcon mIcon = null;

    private String saveImageFile = "";

    public IBISAnswerImage() {
        super(mImageResource);
        this.setIcon();
        this.setSaveImageFile(mImageFile.toString());
    }

    public void setImageFile(File f) {
        mImageFile = f;
    }

    public File getImageFile() {
        return mImageFile;
    }

    /** persistance only */
    public String getSaveImageFile() {
        return saveImageFile == null ? null : saveImageFile.toString();
    }

    /** persistance only */
    public void setSaveImageFile(String path) {
        saveImageFile = path;
    }

    public void setImageResource(Resource r) {
        mImageResource = r;
    }

    public void setImageResource(File f) {
        mImageResource = new LWMap("dummy map").getResourceFactory().get(f);
    }

    public Resource getImageResource() {
        return mImageResource;
    }

    public void setIcon() {
        mIcon = new IBISAnswerIcon();
    }

    public IBISImageIcon getIcon() {
        return mIcon;
    }
}
