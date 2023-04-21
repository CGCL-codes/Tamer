package au.com.langdale.gwt.client.model;

/**
 * Base for all Character properties.  Concrete properties must 
 * provide _get(), _set(), and getName().
 * 
 * @author adv
 *
 */
public abstract class CharacterProperty extends Property {

    public CharacterProperty() {
    }

    protected abstract Character _get(Object model);

    protected abstract void _set(Object model, Character value);

    public String get(Object model) {
        Character value = _get(model);
        if (value != null) return value.toString(); else return "";
    }

    public boolean isSet(Object model) {
        Character value = _get(model);
        return value != null && "TYty".indexOf(value.charValue()) != -1;
    }

    public void set(Object model, boolean value) {
        _set(model, new Character(value ? 'T' : 'F'));
    }

    public void set(Object model, String value) {
        if (value.length() > 0) _set(model, new Character(value.charAt(0))); else _set(model, null);
    }

    public int getLength() {
        return 1;
    }

    public String getStyleName() {
        return "char-field";
    }
}
