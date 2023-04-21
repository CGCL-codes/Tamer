package com.lewisshell.helpyourself.psa;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import com.lewisshell.helpyourself.web.*;
import org.apache.commons.logging.*;
import com.sun.image.codec.jpeg.*;

/**
 * @author richard
 */
public class Thumbnailer {

    private static final Log LOG = LogFactory.getLog(Thumbnailer.class);

    public static final int DEFAULT_THUMBNAIL_SIZE = 160;

    public static final int DEFAULT_THUMBNAIL_QUALITY = 90;

    private String thumbnailDirectoryPath;

    public Thumbnailer(String thumbnailDirectoryPath) {
        LOG.info("*** thumbnailer created ***");
        this.thumbnailDirectoryPath = thumbnailDirectoryPath;
    }

    public Thumbnail createThumbnail(Image image, int size) {
        return new Thumbnail(image, size, this.thumbnailDirectoryPath, Global.MAX_FILE_TRANSFER_BUFFER_SIZE);
    }

    /**
     * create the thumbnail file regardless of whether it exists or not.
     */
    private void createThumbnailFile(Thumbnail thumbnail, OutputStream out) throws ThumbnailGenerationException {
        java.awt.Image awtImage = Toolkit.getDefaultToolkit().getImage(thumbnail.getImage().getMediaFullPath());
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(awtImage, 0);
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            throw new ThumbnailGenerationException(e);
        }
        if (mediaTracker.isErrorID(0)) {
            String message = "error waiting for image: " + thumbnail.getImage().getMediaFullPath();
            LOG.error(message);
            throw new ThumbnailGenerationException(message);
        }
        BufferedImage thumbImage = new BufferedImage(thumbnail.getWidth(), thumbnail.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(awtImage, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), null);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
        int quality = Math.max(0, Math.min(DEFAULT_THUMBNAIL_QUALITY, 100));
        param.setQuality((float) quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        try {
            encoder.encode(thumbImage);
            out.close();
        } catch (ImageFormatException e) {
            throw new ThumbnailGenerationException(e);
        } catch (IOException e) {
            throw new ThumbnailGenerationException(e);
        }
        LOG.info("Generated thumbnail " + thumbnail.getPath() + " for " + thumbnail.getImage().getMediaFullPath());
    }

    /**
     * synchronise the thumbnail cache for thumbnail. ie. if the cache has no thumbnail file
     * for the thumbnail's image, then create one.  if the cache has a file but the image has
     * been removed, remove the cached thumbnail. (this case is unlikely)
     */
    public Thumbnail ensureThumbnailFileExists(Thumbnail thumbnail) {
        boolean cached = thumbnail.isCached();
        boolean orphaned = thumbnail.isOrphaned();
        if (cached && orphaned) {
            LOG.warn("original image " + thumbnail.getImage().getMediaFullPath() + " lost, removing cached thumbnail");
            if (!new File(thumbnail.getPath()).delete()) {
                LOG.error("cannot remove cached thumbnail: " + thumbnail.getPath());
            }
            return thumbnail;
        }
        if (cached) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Using previously generated thumbnail for " + thumbnail.getImage().getMediaFullPath());
            }
        } else if (!orphaned) {
            String tempThumbnailPath = thumbnail.getPath() + "_";
            OutputStream out;
            try {
                out = new FileOutputStream(tempThumbnailPath);
            } catch (FileNotFoundException e) {
                throw new ThumbnailGenerationException(e);
            }
            this.createThumbnailFile(thumbnail, out);
            new File(tempThumbnailPath).renameTo(new File(thumbnail.getPath()));
        }
        return thumbnail;
    }
}
