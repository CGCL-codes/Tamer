package javax.microedition.lcdui;

import com.sun.midp.configurator.Constants;
import com.sun.midp.chameleon.CGraphicsUtil;
import com.sun.midp.chameleon.skins.ImageItemSkin;
import com.sun.midp.chameleon.skins.resources.ImageItemResources;

/**
* This is the Look &amps; Feel implementation for ImageItem.
*/
class ImageItemLFImpl extends ItemLFImpl implements ImageItemLF {

    /**
     * Creates look&amps;feel for an ImageItem
     * @param imageItem the ImageItem associated with this look&amps;feel
     */
    ImageItemLFImpl(ImageItem imageItem) {
        super(imageItem);
        this.imgItem = imageItem;
        ImageItemResources.load();
        appearanceMode = Item.PLAIN;
    }

    /**
     * Notifies L&F of an image change in the corresponding ImageItem.
     * @param img - the new image set in the ImageItem
     */
    public void lSetImage(Image img) {
        lRequestInvalidate(true, true);
    }

    /**
     * Notifies L&F of an alternative text change 
     * in the corresponding ImageItem.
     * @param altText - the new alternative text set in the ImageItem
     */
    public void lSetAltText(String altText) {
    }

    /**
     * Notifies L&F of a command addition in the corresponding ImageItem.
     * @param cmd the newly added command
     * @param i the index of the added command in the ImageItem's
     *        commands[] array
     */
    public void lAddCommand(Command cmd, int i) {
        super.lAddCommand(cmd, i);
        if ((imgItem.numCommands >= 1) && (appearanceMode == Item.PLAIN)) {
            appearanceMode = imgItem.appearanceMode == Item.BUTTON ? Item.BUTTON : Item.HYPERLINK;
            lRequestInvalidate(true, true);
        }
    }

    /**
     * Notifies L&F of a command removal in the corresponding ImageItem.
     * @param cmd the newly removed command
     * @param i the index of the removed command in the ImageItem's
     *        commands[] array
     */
    public void lRemoveCommand(Command cmd, int i) {
        super.lRemoveCommand(cmd, i);
        if (imgItem.numCommands < 1) {
            appearanceMode = Item.PLAIN;
            lRequestInvalidate(true, true);
        }
    }

    /**
     * Returns vertical padding per ImageItem's appearance mode.
     * That is the distance between the top edge and 
     * the top part of the text. 
     * @param appearance The appearance mode of StringItem
     * @return the vertical padding used in ImageItem per its appearance mode
     */
    static int getVerticalPad(int appearance) {
        switch(appearance) {
            case Item.PLAIN:
                return 0;
            case Item.HYPERLINK:
                return ImageItemSkin.PAD_LINK_V;
            default:
                return ImageItemSkin.PAD_BUTTON_V;
        }
    }

    /**
     * Returns horizontal padding per ImageItem's appearance mode.
     * That is the distance between the left edge and the left part 
     * of the text. 
     * @param appearance The appearance mode of StringItem
     * @return the horizontal padding used in ImageItem per its appearance mode
     */
    static int getHorizontalPad(int appearance) {
        switch(appearance) {
            case Item.PLAIN:
                return 0;
            case Item.HYPERLINK:
                return ImageItemSkin.PAD_LINK_H;
            default:
                return ImageItemSkin.PAD_BUTTON_H;
        }
    }

    /**
     * Determine if this Item should have a newline before it
     *
     * @return true if it should have a newline before
     */
    boolean equateNLB() {
        if (super.equateNLB()) {
            return true;
        }
        if ((imgItem.layout & Item.LAYOUT_2) == Item.LAYOUT_2) {
            return false;
        }
        return imgItem.label != null && imgItem.label.length() > 0;
    }

    /**
     * Called by the system to signal a key press
     *
     * @param keyCode the key code of the key that has been pressed
     * @see #getInteractionModes
     */
    void uCallKeyPressed(int keyCode) {
        if (keyCode != Constants.KEYCODE_SELECT) {
            return;
        }
        ItemCommandListener cl;
        Command defaultCmd;
        synchronized (Display.LCDUILock) {
            if (imgItem.numCommands == 0 || imgItem.commandListener == null) {
                return;
            }
            cl = imgItem.commandListener;
            defaultCmd = imgItem.defaultCommand;
        }
        if (cl != null) {
            try {
                synchronized (Display.calloutLock) {
                    if (defaultCmd != null) {
                        cl.commandAction(defaultCmd, imgItem);
                    } else {
                    }
                }
            } catch (Throwable thr) {
                Display.handleThrowable(thr);
            }
        }
    }

    /**
     * Sets the content size in the passed in array.
     * Content is calculated based on the availableWidth.
     * size[WIDTH] and size[HEIGHT] should be set by this method.
     * @param size The array that holds Item content size and location 
     *             in Item internal bounds coordinate system.
     * @param availableWidth The width available for this Item
     */
    void lGetContentSize(int size[], int availableWidth) {
        Image img = imgItem.immutableImg;
        if (img == null) {
            size[WIDTH] = size[HEIGHT] = 0;
        } else {
            size[WIDTH] = img.getWidth() + 2 * getHorizontalPad(appearanceMode);
            size[HEIGHT] = img.getHeight() + 2 * getVerticalPad(appearanceMode);
        }
    }

    /**
     * Paint this ImageItem
     *
     * @param g the Graphics context to paint to
     * @param width the width of the content area
     * @param height the height of the content area
     */
    void lPaintContent(Graphics g, int width, int height) {
        if (appearanceMode == Item.HYPERLINK) {
            CGraphicsUtil.drawTop_BottomBorder(g, ImageItemSkin.IMAGE_LINK_H, ImageItemSkin.IMAGE_LINK_H, ImageItemSkin.PAD_LINK_H, contentBounds[WIDTH] - ImageItemSkin.PAD_LINK_H, ImageItemSkin.PAD_LINK_H, height - ImageItemSkin.PAD_LINK_H - ImageItemSkin.IMAGE_LINK_H.getHeight());
            CGraphicsUtil.drawLeft_RightBorder(g, ImageItemSkin.IMAGE_LINK_V, ImageItemSkin.IMAGE_LINK_V, ImageItemSkin.PAD_LINK_H + ImageItemSkin.IMAGE_LINK_V.getHeight(), contentBounds[HEIGHT] - ImageItemSkin.PAD_LINK_H - ImageItemSkin.IMAGE_LINK_H.getHeight(), ImageItemSkin.PAD_LINK_H, width - ImageItemSkin.PAD_LINK_H - ImageItemSkin.IMAGE_LINK_V.getWidth());
        } else if (appearanceMode == Item.BUTTON) {
            if (ImageItemSkin.IMAGE_BUTTON == null) {
                CGraphicsUtil.draw2ColorBorder(g, 0, 0, width, height, hasFocus, ImageItemSkin.COLOR_BORDER_DK, ImageItemSkin.COLOR_BORDER_LT, ImageItemSkin.BUTTON_BORDER_W);
            } else {
                CGraphicsUtil.draw9pcsBackground(g, 0, 0, width, height, ImageItemSkin.IMAGE_BUTTON);
            }
        }
        Image img = imgItem.immutableImg;
        if (img != null) {
            g.drawImage(img, getHorizontalPad(appearanceMode), getVerticalPad(appearanceMode), Graphics.TOP | Graphics.LEFT);
        }
    }

    /** ImageItem associated with this view */
    private ImageItem imgItem;

    /**
     * The appearance hint
     */
    private int appearanceMode;
}
