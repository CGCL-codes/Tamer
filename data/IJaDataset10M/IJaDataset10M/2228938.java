package pl.org.minions.stigma.client.ui.swing;

import pl.org.minions.stigma.databases.Resourcer;

/**
 * ResourcerIcon subclass that implements
 * {@link javax.swing.plaf.UIResource} interface.
 */
public class ResourcerIconUIResource extends ResourcerIcon implements javax.swing.plaf.UIResource {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an icon by loading an image using
     * {@link Resourcer}.
     * @param path
     *            path to image
     */
    public ResourcerIconUIResource(String path) {
        super(path);
    }
}
