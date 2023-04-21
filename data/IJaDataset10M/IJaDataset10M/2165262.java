package com.lubq.lm.util;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import com.lubq.lm.bestwiz.order.ui.cons.GlobalConstants;
import com.lubq.lm.bestwiz.order.ui.cons.GlobalResources;

/**
 * 
 * システムで使ったフォント、色、画像を管理するツールクラス
 * 
 * @author wenyi <jhf@bestwiz.cn>
 * 
 * @copyright 2006, BestWiz(Dalian) Co.,Ltd
 * @version $Id: ResourceUtil.java,v 1.18 2007/04/14 07:56:37 liyan Exp $
 */
public class ResourceUtil {

    private static final FontRegistry fRegistry = JFaceResources.getFontRegistry();

    static {
        fRegistry.put(GlobalConstants.FONT_KEY_MSPGOTHIC8, new FontData[] { new FontData("MS PGothic", 8, SWT.NORMAL) });
        fRegistry.put(GlobalConstants.FONT_KEY_MSPGOTHIC9, new FontData[] { new FontData("MS PGothic", 9, SWT.NORMAL) });
        fRegistry.put(GlobalConstants.FONT_KEY_MSPGOTHIC18, new FontData[] { new FontData("MS PGothic", 18, SWT.BOLD) });
        fRegistry.put(GlobalConstants.FONT_KEY_MSPGOTHIC12, new FontData[] { new FontData("MS PGothic", 12, SWT.NORMAL) });
    }

    private static final ColorRegistry cRegistry = JFaceResources.getColorRegistry();

    static {
        cRegistry.put(GlobalConstants.COLOR_KEY_WHITE, new RGB(255, 255, 255));
        cRegistry.put(GlobalConstants.COLOR_KEY_BLACK, new RGB(0, 0, 0));
        cRegistry.put(GlobalConstants.COLOR_KEY_RED, new RGB(255, 0, 0));
        cRegistry.put(GlobalConstants.COLOR_KEY_YELLOW, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW).getRGB());
        cRegistry.put(GlobalConstants.COLOR_KEY_CYAN, new RGB(0, 128, 255));
        cRegistry.put(GlobalConstants.COLOR_KEY_GREEN, Display.getCurrent().getSystemColor(SWT.COLOR_GREEN).getRGB());
        cRegistry.put(GlobalConstants.COLOR_KEY_PINK, new RGB(255, 0, 255));
        cRegistry.put(GlobalConstants.COLOR_KEY_ORANGE, new RGB(239, 190, 0));
        cRegistry.put(GlobalConstants.COLOR_KEY_BABYBLUE, new RGB(140, 198, 255));
        cRegistry.put(GlobalConstants.COLOR_KEY_GRAY, Display.getCurrent().getSystemColor(SWT.COLOR_GRAY).getRGB());
        cRegistry.put(GlobalConstants.COLOR_KEY_BABYGREEN, new RGB(235, 241, 224));
        cRegistry.put(GlobalConstants.COLOR_KEY_HEAVYBLUE, new RGB(0, 0, 153));
        cRegistry.put(GlobalConstants.COLOR_KEY_WIDGET_BACKGROUND, Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB());
        cRegistry.put(GlobalConstants.COLOR_KEY_WIDGET_DARKSHADOW, Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW).getRGB());
    }

    private static final ColorRegistry cRegistryFont = JFaceResources.getColorRegistry();

    static {
        cRegistryFont.put(GlobalConstants.COLOR_KEY_WHITE, new RGB(255, 255, 255));
        cRegistryFont.put(GlobalConstants.COLOR_KEY_BLACK, new RGB(0, 0, 0));
    }

    private static final ImageRegistry iRegistry = JFaceResources.getImageRegistry();

    static {
        iRegistry.put(GlobalConstants.IMAGE_KEY_FXICON, OriginalImageDescriptor.createFromURL(ResourceUtil.class.getResource(GlobalConstants.RESOURCE_PATH + "images/fxicon.gif")));
        iRegistry.put(GlobalConstants.IMAGE_KEY_CHB_CHECKED, OriginalImageDescriptor.createFromURL(ResourceUtil.class.getResource(GlobalConstants.RESOURCE_PATH + "images/chb_checked.gif")));
        iRegistry.put(GlobalConstants.IMAGE_KEY_CHB_UNCHECKED, OriginalImageDescriptor.createFromURL(ResourceUtil.class.getResource(GlobalConstants.RESOURCE_PATH + "images/chb_unchecked.gif")));
        iRegistry.put(GlobalConstants.IMAGE_KEY_POINT, OriginalImageDescriptor.createFromURL(ResourceUtil.class.getResource(GlobalConstants.RESOURCE_PATH + "images/point.GIF")));
    }

    /**
     * ImageDescriptorの作成
     * 
     * @author wenyi
     * 
     */
    private static class OriginalImageDescriptor extends ImageDescriptor {

        private Image original;

        private int refCount = 0;

        private Device originalDisplay;

        /**
         * コンストラクタ
         * 
         * @param original
         * @param originalDisplay
         */
        public OriginalImageDescriptor(Image original, Device originalDisplay) {
            this.original = original;
            this.originalDisplay = originalDisplay;
        }

        /**
         * リソースの作成
         */
        public Object createResource(Device device) throws DeviceResourceException {
            if (device == originalDisplay) {
                refCount++;
                return original;
            }
            return super.createResource(device);
        }

        /**
         * リソースの解放
         */
        public void destroyResource(Object toDispose) {
            if (original == toDispose) {
                refCount--;
                if (refCount == 0) {
                    original.dispose();
                    original = null;
                }
            } else {
                super.destroyResource(toDispose);
            }
        }

        /**
         * ImageDataを取得する
         */
        public ImageData getImageData() {
            return original.getImageData();
        }
    }

    /**
     * Fontを取得する
     * 
     * @return
     */
    public static Font getDefaultFont() {
        return fRegistry.get("MSPGothic9");
    }

    /**
     * fRegistryを設定する
     * 
     * @param fontKey
     *            the key used to retrieve this font
     * @param fdata
     *            font Data with Font definition
     */
    public static void registerFont(String fontKey, FontData[] fdata) {
        fRegistry.put(fontKey, fdata);
    }

    /**
     * fRegistryを取得する
     * 
     * @param fontKey
     * @return the font to retrieve
     */
    public static Font getFontByName(String fontKey) {
        return fRegistry.get(fontKey);
    }

    /**
     * cRegistryを取得する
     * 
     * @return
     */
    public static Color getDefaultColor() {
        return cRegistry.get("white");
    }

    /**
     * cRegistryを設定する
     * 
     * @param colorKey
     *            the key used to retrieve this Color
     * @param rgb
     *            RGB with Color definition
     */
    public static void registerColor(String colorKey, RGB rgb) {
        cRegistry.put(colorKey, rgb);
    }

    /**
     * cRegistryを取得する
     * 
     * @param colorKey
     * @return the Color to retrieve
     */
    public static Color getColorByName(String colorKey) {
        return cRegistry.get(colorKey);
    }

    /**
     * iRegistryを取得する
     * 
     * @return
     */
    public static Image getDefaultImage() {
        return iRegistry.get("fxicon");
    }

    /**
     * iRegistryを設定する
     * 
     * @param imageKey
     *            the key used to retrieve this image
     * @param image
     *            image name
     */
    public static void registerImage(String imageKey, String image) {
        iRegistry.put(imageKey, OriginalImageDescriptor.createFromURL(ResourceUtil.class.getResource(GlobalConstants.RESOURCE_PATH + "images/" + image)));
    }

    /**
     * iRegistryを取得する
     * 
     * @param imageKey
     * @return the Image to retrieve
     */
    public static Image getImageByName(String imageKey) {
        return iRegistry.get(imageKey);
    }

    /**
     * Colorを取得する
     * 
     * @param colorKey
     * @return the Color to retrieve
     */
    public static Color getColorByCp(String counterpartyId) {
        if (null == counterpartyId) {
            counterpartyId = "";
        }
        String colorkey = null;
        try {
            colorkey = GlobalResources.GlobalResource.getString(counterpartyId);
        } catch (Exception e) {
        }
        if (null == colorkey) {
            return getDefaultColor();
        }
        return cRegistry.get(GlobalResources.GlobalResource.getString(counterpartyId));
    }

    /**
     * FontColorを取得する
     * 
     * @param colorKey
     * @return the Color to retrieve
     */
    public static Color getFontColorByCp(String counterpartyId) {
        if (null == counterpartyId) {
            counterpartyId = "";
        }
        String colorkey = null;
        try {
            colorkey = GlobalResources.GlobalResource.getString(counterpartyId + "Font");
        } catch (Exception e) {
        }
        if (null == colorkey) {
            return getDefaultColor();
        }
        return cRegistryFont.get(GlobalResources.GlobalResource.getString(counterpartyId + "Font"));
    }
}
