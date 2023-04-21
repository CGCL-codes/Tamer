package de.carne.swt;

import java.text.MessageFormat;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Widget;
import de.carne.util.resource.ResourceArrayLoader;
import de.carne.util.resource.ResourceUnavailableException;
import de.carne.util.resource.Resources;

/**
 * Utility class providing common builder services for building an UI.
 */
public final class UIBuilderContext {

    private Widget owner;

    /**
	 * Construct <code>UIBuilderContext</code>.
	 * 
	 * @param owner The widget owning the loaded resources.
	 */
    public UIBuilderContext(Widget owner) {
        assert owner != null;
        this.owner = owner;
    }

    /**
	 * Load and optionally format a text resource.
	 * <p>
	 * To load the underlying String resource the submitted key is directly forwarded to the Resources class. If one or
	 * more arguments are submitted the loaded text is used as a pattern for the <code>MessageFormat</code> class.
	 * </p>
	 * 
	 * @param key The resource key of the String resource to load.
	 * @param arguments The optional arguments used to format the resulting text.
	 * @return The loaded and formatted text.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 * @see Resources
	 * @see MessageFormat
	 */
    public String loadText(String[] key, Object... arguments) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        assert arguments != null;
        final String string = Resources.getString(key);
        String text;
        if (arguments.length > 0) {
            text = MessageFormat.format(string, arguments);
        } else {
            text = string;
        }
        return text;
    }

    /**
	 * Load and optionally format a widget's text attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".text"</code> to the widget's base resource key. If
	 * one or more arguments are submitted the loaded text is used as a pattern for the <code>MessageFormat</code>
	 * class.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @param arguments The optional arguments used to format the widget text.
	 * @return The loaded and formatted widget text.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 * @see MessageFormat
	 */
    public String loadWidgetText(String[] widgetKey, Object... arguments) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        assert arguments != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".text" };
        return loadText(key, arguments);
    }

    private ImageLoader imageLoader = null;

    private synchronized ImageLoader imageLoader() {
        if (this.imageLoader == null) {
            this.imageLoader = new ImageLoader(this.owner);
        }
        return this.imageLoader;
    }

    /**
	 * Load an Image resource.
	 * 
	 * @param key The resource key of the Image resource to load.
	 * @return The loaded Image resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image loadImage(String[] key) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        return Resources.getResource(imageLoader(), key);
    }

    /**
	 * Load a widget's image attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".image"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Image resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image loadWidgetImage(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".image" };
        return loadImage(key);
    }

    /**
	 * Load a widget's disabled image attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".disabledimage"</code> to the widget's base resource
	 * key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Image resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image loadWidgetDisabledImage(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".disabledimage" };
        return loadImage(key);
    }

    /**
	 * Load a widget's hot image attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".hotimage"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Image resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image loadWidgetHotImage(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".hotimage" };
        return loadImage(key);
    }

    private ResourceArrayLoader<Image> imagesLoader = null;

    private synchronized ResourceArrayLoader<Image> imagesLoader() {
        if (this.imagesLoader == null) {
            this.imagesLoader = new ResourceArrayLoader<Image>(imageLoader());
        }
        return this.imagesLoader;
    }

    /**
	 * Load an Image resource array.
	 * 
	 * @param key The resource key of the Image resources to load.
	 * @return The loaded Image resources.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image[] loadImages(String[] key) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        return Resources.getResource(imagesLoader(), key);
    }

    /**
	 * Load a widget's images attribute.
	 * <p>
	 * The actual string resource key is build by appending <code>".images"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Image resources.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Image[] loadWidgetImages(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".images" };
        return Resources.getResource(imagesLoader(), key);
    }

    private FontLoader fontLoader = null;

    private synchronized FontLoader fontLoader() {
        if (this.fontLoader == null) {
            this.fontLoader = new FontLoader(this.owner);
        }
        return this.fontLoader;
    }

    /**
	 * Load a Font resource.
	 * 
	 * @param key The resource key of the Font resource to load.
	 * @return The loaded Font resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Font loadFont(String[] key) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        return Resources.getResource(fontLoader(), key);
    }

    /**
	 * Load a widget's font attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".font"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Font resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Font loadWidgetFont(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".font" };
        return loadFont(key);
    }

    private ColorLoader colorLoader = null;

    private synchronized ColorLoader colorLoader() {
        if (this.colorLoader == null) {
            this.colorLoader = new ColorLoader(this.owner);
        }
        return this.colorLoader;
    }

    /**
	 * Load a Color resource.
	 * 
	 * @param key The resource key of the Color resource to load.
	 * @return The loaded Color resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Color loadColor(String[] key) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        return Resources.getResource(colorLoader(), key);
    }

    /**
	 * Load a widget's color attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".color"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded Color resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public Color loadWidgetColor(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".color" };
        return loadColor(key);
    }

    private TextStyleLoader styleLoader = null;

    private synchronized TextStyleLoader styleLoader() {
        if (this.styleLoader == null) {
            this.styleLoader = new TextStyleLoader(fontLoader(), colorLoader());
        }
        return this.styleLoader;
    }

    /**
	 * Load a TextStyle resource.
	 * 
	 * @param key The resource key of the TextStyle resource to load.
	 * @return The loaded TextStyle resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public TextStyle loadStyle(String[] key) throws ResourceUnavailableException {
        assert key != null;
        assert key.length == 2;
        assert key[0] != null;
        assert key[1] != null;
        return Resources.getResource(styleLoader(), key);
    }

    /**
	 * Load a widget's style attribute.
	 * <p>
	 * The actual String resource key is build by appending <code>".style"</code> to the widget's base resource key.
	 * </p>
	 * 
	 * @param widgetKey The base resource key of the widget.
	 * @return The loaded TextStyle resource.
	 * @throws ResourceUnavailableException If the requested resource is not available.
	 */
    public TextStyle loadWidgetStyle(String[] widgetKey) throws ResourceUnavailableException {
        assert widgetKey != null;
        assert widgetKey.length == 2;
        assert widgetKey[0] != null;
        assert widgetKey[1] != null;
        final String[] key = new String[] { widgetKey[0], widgetKey[1] + ".style" };
        return loadStyle(key);
    }
}
