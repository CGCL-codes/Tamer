package urban.model;

import org.antlr.stringtemplate.StringTemplate;

/**
 * Experimental feature support for representing sites in the form a[4]
 */
@SuppressWarnings("all")
public class SiteArray extends Site {

    private int index;

    public SiteArray(String agent, String name, int index, String state, String bindingMark) {
        super(agent, name, state, bindingMark);
        this.index = index;
    }

    public int getSize() {
        return index;
    }

    public int getIndex() {
        return index;
    }

    public SiteArray clone() {
        return new SiteArray(getAgent(), getName(), index, getState(), getBindingMark());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof SiteArray)) return false;
        SiteArray other = (SiteArray) obj;
        if (index != other.index) return false;
        return true;
    }

    @Override
    public String toString() {
        StringTemplate template = new StringTemplate("$s.name$[$s.index$]$if(s.state)$~$s.state$$endif$$if(s.bindingMark)$!$s.bindingMark$$endif$");
        template.setAttribute("s", this);
        return template.toString();
    }
}
