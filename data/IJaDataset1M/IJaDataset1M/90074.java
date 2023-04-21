package com.g2d.studio.icon;

import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JList;
import com.g2d.awt.util.*;
import com.g2d.studio.Config;
import com.g2d.studio.fileobj.FileObject;
import com.g2d.studio.io.File;
import com.g2d.studio.swing.G2DListItem;

public class IconFile extends FileObject {

    private transient BufferedImage image;

    private transient ImageIcon icon;

    IconFile(File file) {
        super(file.getName().substring(0, file.getName().length() - Config.ICON_SUFFIX.length()), file);
    }

    IconFile(String name, File file, BufferedImage image, ImageIcon icon) {
        super(name, file);
        this.image = image;
        this.icon = icon;
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = Tools.readImage(getFile().getPath());
        }
        return image;
    }

    @Override
    public ImageIcon getListIcon(boolean update) {
        if (icon == null) {
            icon = new ImageIcon(getImage());
        }
        return icon;
    }

    @Override
    public String getSaveListArgs() {
        return getImage().getWidth() + "," + getImage().getHeight();
    }
}
