package de.mpiwg.vspace.util.images;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.PlatformUI;
import de.mpiwg.vspace.filehandler.services.FileHandler;

public class ImageRegistry {

    public static final ImageRegistry REGISTRY = new ImageRegistry();

    private Map<URL, Image> imageMap;

    private Map<String, Image> imagePathMap;

    private Map<Image, Image> disabledImages;

    private ImageRegistry() {
        imageMap = new HashMap<URL, Image>();
        imagePathMap = new HashMap<String, Image>();
        disabledImages = new HashMap<Image, Image>();
    }

    public Image getImage(URL url) {
        if (imageMap.containsKey(url)) return imageMap.get(url);
        File absoluteFile = FileHandler.getAbsoluteFileFromRelativeUrl(url);
        if (absoluteFile == null) return null;
        ImageData data = new ImageData(absoluteFile.getAbsolutePath());
        Image image = new Image(PlatformUI.getWorkbench().getDisplay(), data);
        imageMap.put(url, image);
        return image;
    }

    public Image getImage(String path) {
        if (imagePathMap.containsKey(path)) return imagePathMap.get(path);
        Image image = null;
        try {
            ImageData data = new ImageData(path);
            image = new Image(PlatformUI.getWorkbench().getDisplay(), data);
        } catch (IllegalArgumentException e) {
            image = null;
        } catch (SWTException e2) {
            image = null;
        }
        if (image != null) imagePathMap.put(path, image);
        return image;
    }

    public Image disableImage(Image image) {
        Image newImage = new Image(PlatformUI.getWorkbench().getDisplay(), image, SWT.IMAGE_DISABLE);
        disabledImages.put(image, newImage);
        return newImage;
    }

    public void disposeAllImage() {
        for (Image image : disabledImages.keySet()) {
            disabledImages.get(image).dispose();
        }
        for (String key : imagePathMap.keySet()) {
            imagePathMap.get(key).dispose();
        }
        for (URL key : imageMap.keySet()) {
            imageMap.get(key).dispose();
        }
    }
}
