package me.yumin.ladder.jsp.html;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author yumin
 * @see http://docs.jquery.com/Plugins/Validation
 * 
 */
public abstract class HTMLTag extends TagSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4228933858836390222L;

    /**
	 * 
	 */
    private String name;

    private String id;

    private String value;

    private boolean required;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return the required
	 */
    public boolean getRequired() {
        return required;
    }

    /**
	 * @param required the required to set
	 */
    public void setRequired(boolean required) {
        this.required = required;
    }
}
