package zuilib.manager;

import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;
import zuilib.utils.ImageControl;

public class simplePImageManager extends imagemanager {

    public ImageControl[] images;

    public int index;

    private HashMap<String, Integer> images_map = new HashMap<String, Integer>();

    public simplePImageManager(String sname, boolean benable) {
        super(sname, benable);
        images = new ImageControl[0];
        index = 0;
    }

    public int addImage(String newi) {
        if (getZUI().DEBUG >= 5) PApplet.println("* Add Image " + newi);
        images = (ImageControl[]) PApplet.append(images, new ImageControl(newi, this));
        images_map.put(newi, index);
        index += 1;
        return index - 1;
    }

    public PImage getImage(String sname) {
        Integer i = images_map.get(sname);
        if (i != null) images[i].get();
        return getImage(addImage(sname));
    }

    public PImage getImage(int i) {
        if (i < index) {
            return images[i].get();
        } else {
            PApplet.println("[WARNING]: getImage:  Requested image no. " + i + " returned null.");
            return null;
        }
    }

    public PImage get(String sname) {
        return getImage(sname);
    }

    public PImage get(int i) {
        return getImage(i);
    }

    public boolean setImage(int i, String newi) {
        if (i < index) {
            images_map.remove(images[i].Name);
            images[i] = new ImageControl(newi, this);
            images_map.put(newi, i);
            return true;
        } else {
            if (getZUI().DEBUG >= 1) PApplet.println("[WARNING]: setImage:  Setting image " + newi + " on position " + i + " failed.");
            return false;
        }
    }

    public boolean set(int i, String newf) {
        return setImage(i, newf);
    }
}
