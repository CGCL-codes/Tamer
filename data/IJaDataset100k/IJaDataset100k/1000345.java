package v4view.web;

/**
 * Represents the br tag.
 * @author J Patrick Davenport
 *
 */
public class Br extends ChildlessPageElement implements ICoreAttributes, IInlineElement {

    private static final String BR_TAG = "br";

    {
        this.setTag(BR_TAG);
        this.setSupportsAttributes(true);
    }

    @Override
    public String getCssClass() {
        return this.getAttributeValue(CSS_CLASS_KEY);
    }

    @Override
    public String getId() {
        return this.getAttributeValue(ID_KEY);
    }

    @Override
    public String getStyle() {
        return this.getAttributeValue(STYLE_KEY);
    }

    @Override
    public String getTitle() {
        return this.getAttributeValue(TITLE_KEY);
    }

    @Override
    public Br setCssClass(final String _value) {
        return (Br) this.setAttribute(CSS_CLASS_KEY, _value);
    }

    @Override
    public Br setId(final String _value) {
        return (Br) this.setAttribute(ID_KEY, _value);
    }

    @Override
    public Br setStyle(final String _value) {
        return (Br) this.setAttribute(STYLE_KEY, _value);
    }

    @Override
    public Br setTitle(final String _value) {
        return (Br) this.setAttribute(TITLE_KEY, _value);
    }
}
