package org.databene.html;

import java.io.File;
import java.io.IOException;
import org.databene.commons.FileUtil;

/**
 * {@link Anchor} implementation which relates to a {@link File} 
 * and provides the resolution of relative paths.<br/><br/>
 * Created: 13.06.2011 12:29:36
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class FileAnchor extends Anchor {

    public final File file;

    public FileAnchor(File file, String label) {
        super(label);
        this.file = file;
    }

    public FileAnchor(File file, String label, String target) {
        super(label, target);
        this.file = file;
    }

    public static FileAnchor createAnchorForNewWindow(File file, String label) {
        return new FileAnchor(file, label, "_blank");
    }

    public String relativeLinkFrom(File referer) {
        return linkForURL(FileUtil.relativePath(referer, file, '/'));
    }

    @Override
    public String toString() {
        try {
            return linkForURL("file://" + file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return linkForURL("file://" + file.getAbsolutePath());
        }
    }

    private String linkForURL(String url) {
        return "<a href='" + url + "'" + (target != null ? " target='" + target + "'" : "") + ">" + label + "</a>";
    }
}
