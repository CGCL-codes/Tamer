package net.community.chest.ui.helpers.text;

import javax.swing.InputVerifier;
import net.community.chest.ui.helpers.input.URLInputVerifier;
import org.w3c.dom.Element;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since Mar 31, 2009 1:50:02 PM
 */
public class URLInputTextField extends InputTextField {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5766974319985009501L;

    public URLInputTextField(String text, int columns, Element elem, boolean autoLayout) {
        super(text, columns, elem, autoLayout);
    }

    public URLInputTextField(String text, Element elem, boolean autoLayout) {
        this(text, 0, elem, autoLayout);
    }

    public URLInputTextField(String text, boolean autoLayout) {
        this(text, 0, autoLayout);
    }

    public URLInputTextField(String text, Element elem) {
        this(text, elem, true);
    }

    public URLInputTextField(String text, int columns, boolean autoLayout) {
        this(text, columns, null, autoLayout);
    }

    public URLInputTextField(String text, int columns) {
        this(text, columns, true);
    }

    public URLInputTextField(int columns) {
        this(null, columns);
    }

    public URLInputTextField(String text) {
        this(text, 0);
    }

    public URLInputTextField(Element elem, boolean autoLayout) {
        this(null, elem, autoLayout);
    }

    public URLInputTextField(Element elem) {
        this(elem, true);
    }

    public URLInputTextField(boolean autoLayout) {
        this((Element) null, autoLayout);
    }

    public URLInputTextField() {
        this(true);
    }

    @Override
    protected InputVerifier createDefaultVerifier() {
        return URLInputVerifier.URL;
    }
}
