package net.jwpa.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.jwpa.config.Config;
import net.jwpa.config.LogUtil;
import net.jwpa.model.JpegImageFacade;
import net.jwpa.model.MediaFacade;

public class VisualMediaUtils {

    private static final Logger logger = LogUtil.getLogger();

    private static void prepareImage(JpegImageFacade image, ImageTransform target, OutputStream out) throws IOException {
        if (image.isBroken()) return;
        int precalcis = Config.getCurrentConfig().getPrecalcIconSize();
        boolean fromThumb = target.getMaxBoundSize() <= precalcis;
        if (fromThumb) {
            String thumbimg = image.getFile().getAbsolutePath() + ".icon";
            File f = new File(thumbimg);
            synchronized (VisualMediaUtils.class) {
                if (!f.exists() || f.length() == 0) {
                    Dimension d = Utils.getImageDimension(image.getFile());
                    if (d == null) {
                        LogUtil.logWarn(logger, "Image " + image.getAbsolutePath() + " looks broken.");
                        return;
                    }
                    if (d.width <= precalcis && d.height <= precalcis) {
                        target.scaleImage(d, image.getFile(), out);
                        return;
                    } else {
                        OutputStream os = new FileOutputStream(thumbimg);
                        new ImageTransform(precalcis, precalcis, image.getDimension()).scaleImage(d, image.getFile(), os);
                        try {
                            os.flush();
                        } catch (IOException r) {
                            r.printStackTrace();
                        }
                        try {
                            os.close();
                        } catch (IOException r) {
                            r.printStackTrace();
                        }
                    }
                }
            }
            if (!f.exists() || f.length() == 0) target.scaleImage(null, image.getFile(), out); else target.scaleImage(new Dimension(precalcis, precalcis), thumbimg, out);
        } else {
            target.scaleImage(null, image.getFile(), out);
        }
    }

    private static void serveMediaImpl(String mode, HttpServletResponse response, OutputStream out, TplSerCtx context, JpegImageFacade image) throws IOException {
        response.setHeader("content-type", "image/jpeg");
        response.setHeader("Content-Disposition", "inline; filename=\"" + image.getFile().getName() + "\"");
        if (mode.equals("full")) {
            response.setContentLength((int) image.getFile().length());
            Utils.serveFile(image.getFile(), out);
        } else prepareImage(image, new ImageTransform(mode, image.getDimension(), context), out);
    }

    public static void serveMedia(String mode, HttpServletResponse response, OutputStream out, TplSerCtx context, MediaFacade image) throws IOException {
        if (image instanceof JpegImageFacade) serveMediaImpl(mode, response, out, context, (JpegImageFacade) image); else throw new RuntimeException(Utils.WIP_MESSAGE);
    }
}
