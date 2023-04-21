package com.sun.midp.chameleon.skins.resources;

import com.sun.midp.chameleon.skins.SkinPropertiesIDs;
import com.sun.midp.chameleon.skins.ScrollIndSkin;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;

public class ScrollIndResources {

    private static boolean init;

    private ScrollIndResources() {
    }

    public static void load() {
        load(false);
    }

    public static void load(boolean reload) {
        if (init && !reload) {
            return;
        }
        ScrollIndSkin.MODE = SkinResources.getInt(SkinPropertiesIDs.SCROLL_MODE);
        ScrollIndSkin.WIDTH = SkinResources.getInt(SkinPropertiesIDs.SCROLL_WIDTH);
        ScrollIndSkin.COLOR_BG = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_BG);
        ScrollIndSkin.COLOR_FG = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_FG);
        ScrollIndSkin.COLOR_FRAME = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_FRAME);
        ScrollIndSkin.COLOR_DN_ARROW = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_DN_ARROW);
        ScrollIndSkin.COLOR_UP_ARROW = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_UP_ARROW);
        ScrollIndSkin.IMAGE_UP = SkinResources.getImage(SkinPropertiesIDs.SCROLL_IMAGE_UP);
        ScrollIndSkin.IMAGE_DN = SkinResources.getImage(SkinPropertiesIDs.SCROLL_IMAGE_DN);
        ScrollIndSkin.COLOR_AU_BG = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_AU_BG);
        ScrollIndSkin.COLOR_AU_FG = SkinResources.getInt(SkinPropertiesIDs.SCROLL_COLOR_AU_FG);
        ScrollIndSkin.IMAGE_AU_UP = SkinResources.getImage(SkinPropertiesIDs.SCROLL_IMAGE_AU_UP);
        ScrollIndSkin.IMAGE_AU_DN = SkinResources.getImage(SkinPropertiesIDs.SCROLL_IMAGE_AU_DN);
        init = true;
    }
}
