package org.fao.waicent.util;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;

public class ImageCreator extends java.lang.Object {

    static JFrame image_creator_frame = null;

    public static synchronized JFrame getImageCreatorFrame() {
        if (image_creator_frame == null) {
            image_creator_frame = new JFrame();
            image_creator_frame.addNotify();
        }
        return image_creator_frame;
    }

    public static Image getImage(Dimension image_dim) {
        return getImage(image_dim.width, image_dim.height);
    }

    public static Image getImage(int width, int height) {
        return getImageCreatorFrame().createImage(width, height);
    }

    public static FontMetrics getFontMetrics() {
        return getImageCreatorFrame().getGraphics().getFontMetrics();
    }

    public static Graphics getGraphics() {
        return getImageCreatorFrame().getGraphics();
    }
}
