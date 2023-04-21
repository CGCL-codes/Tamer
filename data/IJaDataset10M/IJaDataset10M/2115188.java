package com.novocode.naf.gui.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.novocode.naf.app.*;

/**
 * Manages a pool of shared images which are loaded as resources.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 26, 2003
 */
public final class ResourceImageManager implements IImageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceImageManager.class);

    private final Map<String, IManagedImage> images;

    private final Display display;

    private final boolean autoDispose;

    private final URL baseURL;

    private final class RefCountImage implements IManagedImage {

        private final String resname;

        private Image image;

        private int references;

        RefCountImage(String resname, Image image) {
            this.resname = resname;
            this.image = image;
        }

        public String getName() {
            return resname;
        }

        public synchronized Image acquire() throws NAFException {
            if (image == null) throw new NAFException("Can't acquire image: Image has already been disposed");
            references++;
            return image;
        }

        synchronized void release(boolean dispose) throws NAFException {
            if (references == 0) throw new NAFException("Can't return image: No references left");
            references--;
            if (autoDispose && references == 0 && dispose) dispose(true);
        }

        public synchronized void release() throws NAFException {
            release(true);
        }

        synchronized void dispose(boolean removeFromManager) {
            if (image == null) return;
            LOGGER.debug("ImageManager: Disposing of image {}", resname);
            if (removeFromManager) {
                synchronized (ResourceImageManager.this) {
                    images.remove(resname);
                }
            }
            image.dispose();
            image = null;
        }
    }

    private final class SystemImage implements IManagedImage {

        private final String resname;

        private final Image image;

        SystemImage(String resname, Image image) {
            this.resname = resname;
            this.image = image;
        }

        public String getName() {
            return resname;
        }

        public Image acquire() {
            return image;
        }

        public void release() {
        }
    }

    /**
   * Create a new ImageManager.
   */
    public ResourceImageManager(URL baseURL, Display display, boolean autoDispose) {
        this.baseURL = baseURL;
        this.images = new HashMap<String, IManagedImage>();
        this.display = display;
        this.autoDispose = autoDispose;
        display.addListener(SWT.Dispose, new Listener() {

            public void handleEvent(Event event) {
                disposeAll();
            }
        });
    }

    public synchronized IManagedImage getImage(String resURI) throws NAFException {
        IManagedImage mi = images.get(resURI);
        if (mi == null) {
            if (resURI.endsWith("#mask")) {
                boolean dispose = true;
                IManagedImage base = getImage(resURI.substring(0, resURI.length() - 5));
                try {
                    Image baseImage = base.acquire();
                    ImageData baseData = baseImage.getImageData();
                    int ttype = baseData.getTransparencyType();
                    if (ttype == SWT.TRANSPARENCY_MASK || ttype == SWT.TRANSPARENCY_NONE) {
                        dispose = false;
                        return base;
                    }
                    ImageData maskData;
                    if (ttype == SWT.TRANSPARENCY_PIXEL) maskData = baseData.getTransparencyMask(); else maskData = createMaskFromAlphaChannel(baseImage);
                    if (maskData == null) return base;
                    mi = new RefCountImage(resURI, new Image(display, baseData, maskData));
                } finally {
                    if (base instanceof RefCountImage) ((RefCountImage) base).release(dispose);
                }
            } else if (resURI.endsWith("#alpha")) {
                boolean dispose = true;
                IManagedImage base = getImage(resURI.substring(0, resURI.length() - 6));
                try {
                    Image baseImage = base.acquire();
                    ImageData baseData = baseImage.getImageData();
                    if (baseData.getTransparencyType() == SWT.TRANSPARENCY_ALPHA) {
                        dispose = false;
                        return base;
                    }
                    ImageData maskData = baseData.getTransparencyMask();
                    if (maskData == null) return base;
                    mi = new RefCountImage(resURI, new Image(display, baseData, maskData));
                } finally {
                    if (base instanceof RefCountImage) ((RefCountImage) base).release(dispose);
                }
            } else if (resURI.endsWith("#gray")) {
                IManagedImage base = getImage(resURI.substring(0, resURI.length() - 5));
                try {
                    Image baseImage = base.acquire();
                    mi = new RefCountImage(resURI, new Image(display, baseImage, SWT.IMAGE_GRAY));
                } finally {
                    base.release();
                }
            } else if (resURI.startsWith("sysimage:")) {
                String specialName = resURI.substring(9);
                Image img;
                if (specialName.equals("error")) img = display.getSystemImage(SWT.ICON_ERROR); else if (specialName.equals("information")) img = display.getSystemImage(SWT.ICON_INFORMATION); else if (specialName.equals("warning")) img = display.getSystemImage(SWT.ICON_WARNING); else if (specialName.equals("question")) img = display.getSystemImage(SWT.ICON_QUESTION); else if (specialName.equals("working")) img = display.getSystemImage(SWT.ICON_WORKING); else throw new NAFException("Unknown system image name \"" + specialName + "\"");
                if (img == null) mi = new RefCountImage(resURI, new Image(display, 0, 0)); else mi = new SystemImage(resURI, img);
            } else {
                InputStream in = null;
                try {
                    in = new URL(baseURL, resURI).openStream();
                    mi = new RefCountImage(resURI, new Image(display, in));
                } catch (MalformedURLException ex) {
                    throw new NAFException("Error creating URL object for image resource URL \"" + resURI + "\"", ex);
                } catch (IOException ex) {
                    throw new NAFException("Error loading image resource \"" + resURI + "\"", ex);
                } finally {
                    if (in != null) try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            images.put(resURI, mi);
        }
        return mi;
    }

    public synchronized IManagedImage getAlphaRemappedImage(String resname) throws NAFException {
        if (resname.indexOf("#alpha") == -1) resname = resname + "#alpha";
        return getImage(resname);
    }

    public synchronized IManagedImage getMaskRemappedImage(String resname) throws NAFException {
        if (resname.indexOf("#mask") == -1) resname = resname + "#mask";
        return getImage(resname);
    }

    public synchronized IManagedImage getAlphaRemappedGrayImage(String resname) throws NAFException {
        if (resname.indexOf("#alpha") == -1) resname = resname + "#alpha";
        if (resname.indexOf("#gray") == -1) resname = resname + "#gray";
        return getImage(resname);
    }

    public synchronized IManagedImage getMaskRemappedGrayImage(String resname) throws NAFException {
        if (resname.indexOf("#mask") == -1) resname = resname + "#mask";
        if (resname.indexOf("#gray") == -1) resname = resname + "#gray";
        return getImage(resname);
    }

    private static ImageData createMaskFromAlphaChannel(Image img) {
        Rectangle bounds = img.getBounds();
        ImageData imgData = img.getImageData();
        PaletteData palette = new PaletteData(new RGB[] { new RGB(255, 255, 255), new RGB(0, 0, 0) });
        ImageData alphaData = new ImageData(bounds.width, bounds.height, 1, palette);
        for (int x = 0; x < bounds.width; x++) {
            for (int y = 0; y < bounds.height; y++) {
                int alpha = imgData.getAlpha(x, y);
                if (alpha > 127) alphaData.setPixel(x, y, 0xFFFFFF);
            }
        }
        return alphaData;
    }

    public synchronized void disposeUnallocatedImages() {
        for (Iterator<IManagedImage> it = images.values().iterator(); it.hasNext(); ) {
            IManagedImage mi = it.next();
            if (mi instanceof RefCountImage) {
                RefCountImage rci = (RefCountImage) mi;
                if (rci.references == 0) {
                    rci.dispose(false);
                    it.remove();
                }
            }
        }
    }

    public synchronized void disposeAll() {
        for (IManagedImage mi : images.values()) if (mi instanceof RefCountImage) ((RefCountImage) mi).dispose(false);
        images.clear();
    }

    public static String absoluteURIFor(URL baseURL, String uri) throws NAFException {
        if (uri.startsWith("sysimage:")) return uri;
        try {
            return new URL(baseURL, uri).toExternalForm();
        } catch (MalformedURLException ex) {
            throw new NAFException("Error creating URL object for image URI \"" + uri + "\"", ex);
        }
    }
}
