package net.sf.pegasos.application.mediacenter.functions;

import java.io.File;
import javax.media.MediaLocator;
import javax.swing.Icon;
import net.sf.pegasos.application.mediacenter.functions.pluginrelated.MediaPlayerFunction;
import net.sf.pegasos.application.mediacenter.plugins.MediaPlayerPlugin;
import de.pegasos.extensions.Category;
import de.pegasos.extensions.Function;
import de.pegasos.extensions.PluginRegistry;
import de.pegasos.license.License;
import de.pegasos.license.gpl.GPLv3;
import de.pegasos.media.Media;
import de.pegasos.swing.icon.IconContext;
import de.pegasos.swing.icon.IconManager;
import de.pegasos.swing.icon.IconSize;

/**
 * @author fragro
 */
public class MediaLibraryPlaylistAdd extends MediaPlayerFunction {

    private Media media;

    private String id = null;

    private String iconName = IconManager.UNKNOWN_ICON_NAME;

    private static MediaPlayerPlugin mediaplayer = null;

    /**
     *
     */
    public MediaLibraryPlaylistAdd(File file, Category category) {
        this(Media.getMedia(file), category);
    }

    /**
     *
     */
    public MediaLibraryPlaylistAdd(MediaLocator ml, Category category) {
        this(Media.getMedia(ml), category);
    }

    /**
     *
     */
    public MediaLibraryPlaylistAdd(Media media, Category category) {
        super("add", category);
        this.setIcon(IconContext.ACTIONS, "list-add");
        this.media = media;
        iconName = IconManager.getIconNameByMimeType(media.getMimeType());
        if (iconName == IconManager.UNKNOWN_ICON_NAME) {
            iconName = IconManager.getIconNameByFileExtension(media.getFileExtension());
        }
    }

    public License getLicense() {
        return License.getLicense(GPLv3.class);
    }

    public String invoke() {
        if (!media.getFile().isDirectory()) {
            if (!mediaplayer.contains(media)) {
                mediaplayer.add(media);
            }
        } else {
            this.addRecursive(media.getFile().listFiles());
        }
        return this.getName();
    }

    private void addRecursive(File[] files) {
        File file;
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            if (file.isFile()) {
                this.getMediaPlayer().add(Media.getMedia(file));
            }
            if (file.isDirectory()) {
                this.addRecursive(file.listFiles());
            }
        }
    }

    @Override
    public Icon getIcon(IconSize iconSize) {
        return IconManager.getImageIcon(iconSize, IconContext.ACTIONS, "list-add");
    }
}
