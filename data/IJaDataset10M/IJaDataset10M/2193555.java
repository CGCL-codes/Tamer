package org.designerator.media.image.util;

import java.io.File;
import java.io.IOException;
import org.designerator.common.data.MediaFile;
import org.designerator.common.data.ThumbProxy;
import org.designerator.media.image.ImagePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class PluginUtil {

    public static ThumbProxy getThumbProxy(IFile file) {
        if (file == null) {
            return null;
        }
        IPath thumbDir = getDefaultThumbContainer(file.getParent().getFullPath());
        ThumbProxy tm = new ThumbProxy(1);
        IPath thumbPath = thumbDir.append(addFileExtension("jpg", file.getName()));
        tm.thumbPath = thumbPath.toOSString();
        tm.setMediaFile(new MediaFile(file));
        return tm;
    }

    public static String addFileExtension(String extension, String name) {
        return name + "." + extension;
    }

    public static IPath getDefaultThumbContainer(IPath container) {
        if (container == null) return null;
        IPath currentThumbdir = ImagePlugin.imagesDir.append(container.toPortableString());
        return getOrCreateDir(currentThumbdir, false);
    }

    public static IPath getOrCreateDir(IPath directory, boolean setModified) {
        File f = directory.toFile();
        if (f != null && !f.exists()) {
            if (f.mkdirs()) {
                return directory;
            }
            System.err.println("Failed to make Thumbnail container");
        } else {
            if (f != null && setModified) {
                f.setLastModified(System.currentTimeMillis());
            }
            return directory;
        }
        return null;
    }

    public static void makeDir(File f) throws IOException {
        if (f.exists()) {
            if (!f.isDirectory()) {
                final String message = "Cannot create Dir! Output isFile! " + f.getAbsolutePath();
                throw new IOException(message);
            }
        } else {
            if (!f.mkdirs()) {
                throw new IOException("Failed to create Dir: " + f.getAbsolutePath());
            }
        }
    }

    public static void makeFile(File f) throws IOException {
        if (f.exists()) {
            if (f.isDirectory()) {
                final String message = "Cannot create File! Output_is Directory! " + f.getAbsolutePath();
                throw new IOException(message);
            }
        } else {
            if (!f.createNewFile()) {
                throw new IOException("Failed to create File: " + f.getAbsolutePath());
            }
        }
    }
}
