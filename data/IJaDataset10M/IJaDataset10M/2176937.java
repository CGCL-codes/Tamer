package org.gamegineer.common.internal.ui;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A registry of images used by the bundle.
 */
@ThreadSafe
public final class ImageRegistry {

    /** The path to the dialog default title image. */
    public static final String DIALOG_DEFAULT_TITLE_PATH = "/icons/default-title-image.png";

    /** The path to the dialog error message image. */
    public static final String DIALOG_ERROR_MESSAGE_PATH = "/icons/message-error.png";

    /** The path to the dialog information message image. */
    public static final String DIALOG_INFORMATION_MESSAGE_PATH = "/icons/message-information.png";

    /** The path to the dialog warning message image. */
    public static final String DIALOG_WARNING_MESSAGE_PATH = "/icons/message-warning.png";

    /**
     * The collection of images in the registry. The key is the image path. The
     * value is the image.
     */
    @GuardedBy("lock_")
    private final Map<String, Image> images_;

    /** The instance lock. */
    private final Object lock_;

    /**
     * Initializes a new instance of the {@code ImageRegistry} class.
     */
    ImageRegistry() {
        images_ = new HashMap<String, Image>();
        lock_ = new Object();
    }

    public Image getImage(final String path) {
        assertArgumentNotNull(path, "path");
        Image image = null;
        synchronized (lock_) {
            image = images_.get(path);
            if (image == null) {
                try {
                    image = ImageIO.read(Activator.getDefault().getBundleContext().getBundle().getEntry(path));
                    if (image != null) {
                        images_.put(path, image);
                    }
                } catch (final IOException e) {
                    Loggers.getDefaultLogger().log(Level.WARNING, NonNlsMessages.ImageRegistry_loadImage_error(path), e);
                }
            }
        }
        return image;
    }

    /**
     * Disposes of the resources used by this object.
     */
    void dispose() {
        synchronized (lock_) {
            for (final Image image : images_.values()) {
                image.flush();
            }
            images_.clear();
        }
    }
}
