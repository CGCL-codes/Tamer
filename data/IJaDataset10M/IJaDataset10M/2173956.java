package ToolSystem;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Vector;
import java.util.Hashtable;
import java.net.URL;

public class ImageSorter extends Component {

    private ImageLoader il;

    private Vector<ImageHolder> sortedImage;

    private Hashtable<String, ImageHolder> hashImage;

    private ImageHolder tempImage;

    private Vector<Color> fromColor;

    private Vector<Color> toColor;

    private int sizex;

    private int sizey;

    public ImageSorter() {
        sortedImage = new Vector<ImageHolder>();
        hashImage = new Hashtable<String, ImageHolder>();
        il = new ImageLoader();
        tempImage = new ImageHolder();
        fromColor = new Vector<Color>();
        toColor = new Vector<Color>();
        sizex = 0;
        sizey = 0;
    }

    public void setImageSize(int x, int y) {
        sizex = x;
        sizey = y;
    }

    public void changeColor(Color fromThisColor, Color toThisColor) {
        if (toThisColor != null && fromThisColor != null) {
            fromColor.add(fromThisColor);
            toColor.add(toThisColor);
        }
    }

    public boolean inputImage(String filename) {
        tempImage.image = il.loadFileImage(filename);
        return sortedImage.add(regularImg(filename, true));
    }

    public boolean inputImage(String filename, boolean pixelate) {
        tempImage.image = il.loadFileImage(filename);
        return sortedImage.add(regularImg(filename, pixelate));
    }

    public boolean inputImage(int index, String filename) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = il.loadFileImage(filename);
            sortedImage.set(index, regularImg(filename, true));
            return true;
        }
        return false;
    }

    public boolean inputImage(int index, String filename, boolean pixelate) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = il.loadFileImage(filename);
            sortedImage.set(index, regularImg(filename, pixelate));
            return true;
        }
        return false;
    }

    public boolean inputImage(Image desimg) {
        tempImage.image = desimg;
        return sortedImage.add(regularImg("", true));
    }

    public boolean inputImage(Image desimg, boolean pixelate) {
        tempImage.image = desimg;
        return sortedImage.add(regularImg("", pixelate));
    }

    public boolean inputImage(int index, Image desimg) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = desimg;
            sortedImage.set(index, regularImg("", true));
            return true;
        }
        return false;
    }

    public boolean inputImage(int index, Image desimg, boolean pixelate) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = desimg;
            sortedImage.set(index, regularImg("", pixelate));
            return true;
        }
        return false;
    }

    public boolean inputImage(String filename, Image desimg) {
        tempImage.image = desimg;
        return sortedImage.add(regularImg(filename, true));
    }

    public boolean inputImage(String filename, Image desimg, boolean pixelate) {
        tempImage.image = desimg;
        return sortedImage.add(regularImg(filename, pixelate));
    }

    public boolean inputImage(int index, String filename, Image desimg) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = desimg;
            sortedImage.set(index, regularImg(filename, true));
            return true;
        }
        return false;
    }

    public boolean inputImage(int index, String filename, Image desimg, boolean pixelate) {
        if (index >= 0 && index < sortedImage.size()) {
            tempImage.image = desimg;
            sortedImage.set(index, regularImg(filename, pixelate));
            return true;
        }
        return false;
    }

    public Image getImage(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).image);
        return getColorBox(Color.RED, 10, 10);
    }

    public Image getImage(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).image);
        return getColorBox(Color.RED, 10, 10);
    }

    public Image getColorBox(Color theColor, int sizex, int sizey) {
        if (sizex < 1) sizex = 100;
        if (sizey < 1) sizey = 100;
        int[] colorbox = new int[sizex * sizey];
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) colorbox[i + (j * sizex)] = theColor.getRGB();
        }
        return createImage(new MemoryImageSource(sizex, sizey, colorbox, 0, sizex));
    }

    public int[] getPixels(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).pixels);
        return (new int[0]);
    }

    public int[] getPixels(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).pixels);
        return (new int[0]);
    }

    public int getOrigX(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).origx);
        return 0;
    }

    public int getOrigX(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).origx);
        return 0;
    }

    public int getOrigY(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).origy);
        return 0;
    }

    public int getOrigY(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).origy);
        return 0;
    }

    public int getX(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).sizex);
        return 0;
    }

    public int getX(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).sizex);
        return 0;
    }

    public int getY(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).sizey);
        return 0;
    }

    public int getY(String filename) {
        if (hashImage.get(filename) != null) return (hashImage.get(filename).sizey);
        return 0;
    }

    public String getFilename(int index) {
        if (index >= 0 && index < sortedImage.size()) return (sortedImage.get(index).filename);
        return "";
    }

    public int getIndex(String filename) {
        for (int i = 0; i < sortedImage.size(); i++) {
            if (filename == sortedImage.get(i).filename) return i;
        }
        return -1;
    }

    public int length() {
        return sortedImage.size();
    }

    public int hashLength() {
        return hashImage.size();
    }

    private int handlesinglepixel(int x, int y, int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        Color cool = new Color(red, green, blue, alpha);
        return (cool.getRGB());
    }

    private int[] handlepixels(Image img, int x, int y, int w, int h) {
        int[] pixel = new int[w * h];
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixel, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Error: Interrupted Waiting for Pixels");
            return null;
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("Error: Image Fetch Aborted");
            return null;
        }
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                pixels[i + j * w] = handlesinglepixel(x + i, y + j, pixel[j * w + i]);
            }
        }
        return pixels;
    }

    private ImageHolder regularImg(String filename, boolean pixelate) {
        ImageHolder heldImg = new ImageHolder();
        heldImg.origx = tempImage.image.getWidth(this);
        heldImg.origy = tempImage.image.getHeight(this);
        if (fromColor.size() > 0) {
            tempImage.pixels = handlepixels(tempImage.image, 0, 0, heldImg.origx, heldImg.origy);
            for (int i = 0; i < fromColor.size(); i++) tempImage.pixels = tempImage.changeColor(fromColor.get(i), toColor.get(i));
            tempImage.image = createImage(new MemoryImageSource(heldImg.origx, heldImg.origy, tempImage.pixels, 0, heldImg.origx));
            fromColor.clear();
            toColor.clear();
        }
        if (sizex < 1 || sizey < 1) {
            heldImg.image = tempImage.image;
            sizex = heldImg.origx;
            sizey = heldImg.origy;
        } else heldImg.image = tempImage.image.getScaledInstance(sizex, sizey, Image.SCALE_AREA_AVERAGING);
        il.waitForImage(heldImg.image);
        heldImg.sizex = sizex;
        heldImg.sizey = sizey;
        if (pixelate) heldImg.pixels = handlepixels(heldImg.image, 0, 0, sizex, sizey);
        heldImg.filename = filename;
        if (!filename.matches("")) hashImage.put(filename, heldImg);
        sizex = 0;
        sizey = 0;
        return heldImg;
    }
}

class ImageHolder {

    public Image image;

    public int[] pixels;

    public int sizex;

    public int sizey;

    public int origx;

    public int origy;

    public String filename;

    ImageHolder() {
        sizex = 1;
        sizey = 1;
        origx = 1;
        origy = 1;
        pixels = new int[0];
        filename = "";
    }

    public int[] changeColor(Color fromThisColor, Color toThisColor) {
        int[] change = pixels;
        for (int i = 0; i < change.length; i++) {
            if (fromThisColor.getRGB() == change[i]) change[i] = toThisColor.getRGB();
        }
        return change;
    }
}

class ImageLoader extends Component {

    public Image loadFileImage(String filename) {
        Image image = null;
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(filename);
        waitForImage(image);
        return (image);
    }

    public Image loadURLImage(String urlstring) {
        Image image = null;
        URL url = null;
        try {
            url = new URL(urlstring);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(url);
        waitForImage(image);
        return (image);
    }

    public void waitForImage(Image image) {
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
