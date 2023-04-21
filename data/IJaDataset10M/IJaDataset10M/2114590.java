package hu.ihash.common.service.thumbnail;

import hu.ihash.common.model.file.ImageFilter;
import hu.ihash.common.util.ResourceUtils;
import hu.ihash.hashing.util.ImageLoader;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Generates thumbnails from folders.
 * 
 * @author Gergely Kiss
 */
public class FolderThumbnailer extends AbstractThumbnailer {

    /** The folder icon. */
    private static final BufferedImage folderImage = ResourceUtils.getImage("images/folder.png", null);

    /**
	 * The number of generated image thumbs horizontally and vertically.
	 * Default: 2.
	 */
    private int thumbs = 2;

    /** The gap size in pixels between image thumbs. Default: 2. */
    private int gapSize = 2;

    public FolderThumbnailer() {
        background(folderImage);
        clip(new Rectangle(4, 17, 92, 75));
    }

    public FolderThumbnailer thumbs(int thumbs) {
        this.thumbs = thumbs;
        return this;
    }

    public FolderThumbnailer gap(int gapSize) {
        this.gapSize = gapSize;
        return this;
    }

    @Override
    protected void generateImpl(File source, Graphics target, ThumbnailParameters params) {
        Rectangle r = target.getClipBounds();
        File[] files = source.listFiles(ImageFilter.Name);
        if (files == null) {
            files = new File[0];
        }
        int totalGapPixelsX = (thumbs - 1) * gapSize;
        int totalGapPixelsY = (thumbs - 1) * gapSize;
        int smallWidth = (r.width - totalGapPixelsX) / thumbs;
        int smallHeight = (r.height - totalGapPixelsY) / thumbs;
        int currentFile = 0;
        int stepSize = files.length / (thumbs * thumbs);
        stepSize = stepSize == 0 ? 1 : stepSize;
        for (int y = r.y; y < r.height; y += smallHeight + gapSize) {
            for (int x = r.x; x < r.width; x += smallWidth + gapSize) {
                if (currentFile >= files.length) {
                    continue;
                }
                File file = files[currentFile];
                currentFile += stepSize;
                BufferedImage bimg = ImageLoader.loadOptimized(file, smallWidth, smallHeight);
                target.drawImage(bimg, x, y, null);
            }
        }
    }
}
