package net.sourceforge.x360mediaserve.plugins.sanselanFormats.impl;

import java.io.File;
import java.io.IOException;
import net.sourceforge.x360mediaserve.api.database.items.media.ImageItem;
import net.sourceforge.x360mediaserve.api.formats.Container;
import net.sourceforge.x360mediaserve.api.formats.Tagger;
import net.sourceforge.x360mediaserve.api.formats.TaggingException;
import net.sourceforge.x360mediaserve.util.database.items.media.ImageItemImp;
import net.sourceforge.x360mediaserve.util.database.items.media.resources.ImageResourceImp;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

public class SanselanTagger implements Tagger {

    private static final String[] supportedMimeTypes = { "image/jpeg", "image/tiff", "image/png", "image/gif", "image/bmp" };

    public String getName() {
        return "SanselanTagger";
    }

    public ImageItem getImageItem(File file) throws TaggingException {
        ImageItemImp imageItem = new ImageItemImp();
        try {
            ImageInfo info = Sanselan.getImageInfo(file);
            imageItem.setName(file.getName());
            ImageResourceImp imageResource = new ImageResourceImp();
            imageResource.setLocation(file.toURI().toString());
            imageResource.setWidth(info.getWidth());
            imageResource.setHeight(info.getHeight());
            imageResource.setSize(file.length());
            imageResource.setMimeType(info.getMimeType());
            imageItem.setFirstResource(imageResource);
            return imageItem;
        } catch (Exception e) {
            throw new TaggingException("Error in sanselan tagger");
        }
    }

    public ImageItem getTag(File file, String mimeType) throws TaggingException {
        return getImageItem(file);
    }

    public boolean supportsContainer(Container container) {
        if (container.getMimeType() != null) {
            for (String mimeType : supportedMimeTypes) {
                if (container.getMimeType().equals(mimeType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String[] getSupportedMimeTypes() {
        return supportedMimeTypes;
    }

    public byte[] getThumbnailData(File file) {
        return null;
    }

    public boolean supportsThumbnail() {
        return false;
    }

    public int getDefaultPriority() {
        return 5;
    }

    public String getId() {
        return "sanselantagger";
    }
}
