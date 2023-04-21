package net.community.chest.ui.helpers.text;

import javax.swing.text.Document;
import org.w3c.dom.Element;
import net.community.chest.dom.DOMUtils;
import net.community.chest.lang.ExceptionUtil;
import net.community.chest.swing.component.text.BasePasswordField;
import net.community.chest.ui.helpers.XmlElementComponentInitializer;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Nov 11, 2010 7:51:38 AM
 */
public class HelperPasswordField extends BasePasswordField implements XmlElementComponentInitializer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6827728858715064471L;

    private Element _cElem;

    @Override
    public Element getComponentElement() throws RuntimeException {
        return _cElem;
    }

    @Override
    public void setComponentElement(Element elem) {
        if (_cElem != elem) _cElem = elem;
    }

    @Override
    public void layoutComponent(Element elem) throws RuntimeException {
        if (elem != null) {
            try {
                if (fromXml(elem) != this) throw new IllegalStateException("layoutComponent(" + DOMUtils.toString(elem) + ") mismatched re-constructed instance");
            } catch (Exception e) {
                throw ExceptionUtil.toRuntimeException(e);
            }
            setComponentElement(elem);
        }
    }

    @Override
    public void layoutComponent() throws RuntimeException {
        layoutComponent(getComponentElement());
    }

    public HelperPasswordField() {
        this((String) null);
    }

    public HelperPasswordField(String text) {
        this(text, 0);
    }

    public HelperPasswordField(int columns) {
        this(null, columns);
    }

    public HelperPasswordField(String text, int columns) {
        this(null, text, columns);
    }

    public HelperPasswordField(Document doc, String txt, int columns) {
        this(doc, txt, columns, true);
    }

    public HelperPasswordField(String text, int columns, boolean autoLayout) {
        this((Document) null, text, columns, autoLayout);
    }

    public HelperPasswordField(Document doc, String txt, int columns, boolean autoLayout) {
        this(null, doc, txt, columns, autoLayout);
    }

    public HelperPasswordField(Element elem, Document doc, String txt, int columns) {
        this(elem, doc, txt, columns, true);
    }

    public HelperPasswordField(Element elem) {
        this(elem, true);
    }

    public HelperPasswordField(Element elem, boolean autoLayout) {
        this(elem, null, null, 0, autoLayout);
    }

    public HelperPasswordField(Element elem, Document doc, String txt, int columns, boolean autoLayout) {
        super(doc, txt, columns);
        setComponentElement(elem);
        if (autoLayout) layoutComponent();
    }
}
