package net.jwpa.controller;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mediautil.image.jpeg.LLJTran;
import net.jwpa.config.Config;
import net.jwpa.config.LogUtil;
import net.jwpa.dao.JpegImageDAO;
import net.jwpa.model.JpegImage;
import net.jwpa.view.Theme;

public class ImageTransform {

    private static final Logger logger = LogUtil.getLogger();

    Dimension targetSize;

    Dimension sourceSize;

    double cropPolicy;

    List<Overlay> overlays;

    public boolean hasOverlays() {
        return overlays != null && !overlays.isEmpty();
    }

    public ImageTransform(Dimension _target, Dimension _source) {
        targetSize = _target;
        sourceSize = _source;
        cropPolicy = 0;
    }

    public ImageTransform(String mode, Dimension _source, TplSerCtx ctx) throws IOException {
        Config jwpa = Config.getCurrentConfig();
        sourceSize = _source;
        cropPolicy = 0;
        if (mode.equals("icon")) {
            throw new RuntimeException("Usage of predefined 'icon' is deprecated");
        } else if (mode.equals("iconsmall")) {
            throw new RuntimeException("Usage of predefined 'iconsmall' is deprecated");
        } else if (mode.equals("medium")) {
            targetSize = new Dimension(jwpa.getPreviewSize(), jwpa.getPreviewSize());
        } else {
            int width = Integer.valueOf(mode.substring(0, mode.indexOf('x')));
            int height;
            int us = mode.indexOf("_");
            int cm = 0;
            if (us < 0) height = Integer.valueOf(mode.substring(mode.indexOf('x') + 1)); else {
                height = Integer.valueOf(mode.substring(mode.indexOf('x') + 1, us));
                us = mode.indexOf("_", us + 1);
                if (us < 0) {
                    cm = Integer.valueOf(mode.substring(mode.indexOf('_') + 1));
                } else {
                    cm = Integer.valueOf(mode.substring(mode.indexOf('_') + 1, us));
                    addOverlay(ctx.getCurrentTheme(), mode.substring(us + 1));
                }
            }
            targetSize = new Dimension(width, height);
            cropPolicy = cm / 100f;
        }
    }

    public void addOverlay(Theme theme, String overlayList) {
        String[] overlays = overlayList.split(",");
        for (String on : overlays) {
            String overlayDesc = theme.getConfigAsString("overlay." + on);
            String[] props = overlayDesc.split(";");
            Overlay o = new Overlay();
            o.theme = theme;
            for (String prop : props) {
                String[] nvp = prop.split(":");
                if (nvp[0].toLowerCase().trim().equals("url")) {
                    o.file = nvp[1].trim();
                }
                if (nvp[0].toLowerCase().trim().equals("mode")) {
                    o.mode = nvp[1].trim();
                }
            }
            addOverlay(o);
        }
    }

    public void addOverlay(Overlay o) {
        if (overlays == null) overlays = new ArrayList<Overlay>();
        overlays.add(o);
    }

    public ImageTransform(int w, int h, Dimension _source) {
        this(new Dimension(w, h), _source);
    }

    public ImageTransform(Dimension _target, Dimension _source, double cropP) {
        this(_target, _source);
        cropPolicy = cropP;
    }

    public ImageTransform(int w, int h, Dimension _source, double cropP) {
        this(new Dimension(w, h), _source, cropP);
    }

    private Dimension bounds = null;

    public Dimension getBounds() {
        if (sourceSize == null) return null;
        if (bounds == null) {
            double scale1 = targetSize.getWidth() / sourceSize.getWidth();
            double scale2 = targetSize.getHeight() / sourceSize.getHeight();
            double scaleSmall = Math.min(Math.min(scale1, scale2), 1);
            double scaleBig = Math.min(Math.max(scale1, scale2), 1);
            double scale = scaleBig;
            double destWidth = sourceSize.getWidth() * scale;
            double destHeight = sourceSize.getHeight() * scale;
            if (destWidth > targetSize.getWidth()) {
                double areaOrig = destWidth * destHeight;
                double areaDest = targetSize.getWidth() * destHeight;
                if ((1 - areaDest / areaOrig) > cropPolicy) {
                    double newWidth = targetSize.getWidth() / (1 - cropPolicy);
                    scale = newWidth / sourceSize.getWidth();
                }
            } else if (destHeight > targetSize.getHeight()) {
                double areaOrig = destWidth * destHeight;
                double areaDest = targetSize.getHeight() * destWidth;
                if ((1 - areaDest / areaOrig) > cropPolicy) {
                    double newHeight = targetSize.getHeight() / (1 - cropPolicy);
                    scale = newHeight / sourceSize.getHeight();
                }
            }
            bounds = new Dimension((int) Math.round(sourceSize.getWidth() * scale), (int) Math.round(sourceSize.getHeight() * scale));
        }
        return bounds;
    }

    public double getMaxBoundSize() {
        Dimension bounds = getBounds();
        if (bounds == null) return 0;
        return Utils.getMax(bounds);
    }

    public int getWidth() {
        return targetSize.width;
    }

    public int getHeight() {
        return targetSize.height;
    }

    public int getFinalWidth() {
        return Math.min(Math.min(getBounds().width, targetSize.width), sourceSize.width);
    }

    public int getFinalHeight() {
        return Math.min(Math.min(getBounds().height, targetSize.height), sourceSize.height);
    }

    public BufferedImage applyFilters(BufferedImage image) throws IOException {
        if (overlays != null) {
            for (Overlay o : overlays) {
                BufferedImage oi = ImageIO.read(o.theme.getStream(o.file));
                if (o.mode.trim().toLowerCase().equals("center")) {
                    int x = (image.getWidth() - oi.getWidth()) / 2;
                    int y = (image.getHeight() - oi.getHeight()) / 2;
                    image.getGraphics().drawImage(oi, x, y, null);
                }
            }
        }
        return image;
    }

    public void scaleImage(Dimension d, String img, OutputStream out) throws IOException {
        scaleImage(d, new File(img), out);
    }

    public void scaleImage(Dimension d, File img, OutputStream out) throws IOException {
        if (d == null) d = Utils.getImageDimension(img);
        boolean resizeNeeded = d.width > getWidth() || d.height > getHeight();
        if (!resizeNeeded && !hasOverlays()) {
            Utils.serveFile(img, out);
        } else {
            synchronized (Utils.IMG_SCALE_LOCK) {
                try {
                    BufferedImage res;
                    if (resizeNeeded) {
                        int[] sizes = new int[2];
                        BufferedImage im = Utils.readImage(img, sizes, (int) Math.round(getMaxBoundSize()));
                        double sizeMax = Math.max(sizes[0], sizes[1]);
                        im = Utils.scaleBufferedImage(im, this.getMaxBoundSize() / sizeMax, this);
                        res = new BufferedImage(getFinalWidth(), getFinalHeight(), im.getType());
                        res.getGraphics().drawImage(im, (res.getWidth() - im.getWidth()) / 2, (res.getHeight() - im.getHeight()) / 4, null);
                    } else {
                        res = ImageIO.read(img);
                    }
                    res = applyFilters(res);
                    Utils.writeImage(res, out);
                } catch (IOException t) {
                    throw t;
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        }
    }

    public class Overlay {

        String file;

        String mode;

        Theme theme;
    }

    private static Executor imageRotateExec;

    private static final Object ROTATE_LOCK = new Object();

    private static BlockingQueue rotateQueue = new LinkedBlockingQueue();

    private static int rotateQueueSize = 0;

    public static synchronized void rotateImage(final String image, final boolean ccw) throws IOException {
        if (imageRotateExec == null) imageRotateExec = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, rotateQueue);
        rotateQueueSize++;
        try {
            imageRotateExec.execute(new Runnable() {

                public void run() {
                    try {
                        ImageTransform.rotateImageImpl(image, ccw);
                    } catch (Exception e) {
                        LogUtil.logError(logger, e);
                    } finally {
                        rotateQueueSize--;
                    }
                }
            });
        } catch (Throwable t) {
            rotateQueueSize--;
            LogUtil.logError(logger, t);
        }
    }

    public static synchronized int getRotateQueueSize() {
        return rotateQueueSize;
    }

    private static void rotateImageImpl(String image, boolean ccw) throws Exception {
        synchronized (ROTATE_LOCK) {
            LLJTran llj = new LLJTran(new File(image));
            llj.read(LLJTran.READ_ALL, true);
            int options = LLJTran.OPT_DEFAULTS | LLJTran.OPT_XFORM_ORIENTATION;
            llj.transform((ccw) ? LLJTran.ROT_270 : LLJTran.ROT_90, options);
            OutputStream fout = new FileOutputStream(image + ".rot");
            OutputStream out = new BufferedOutputStream(fout);
            try {
                llj.save(out, LLJTran.OPT_WRITE_ALL);
            } finally {
                out.close();
                fout.close();
            }
            llj.freeMemory();
        }
        new File(image).delete();
        new File(image + ".rot").renameTo(new File(image));
        JpegImage i = JpegImageDAO.get(image);
        i.transformed();
    }
}
