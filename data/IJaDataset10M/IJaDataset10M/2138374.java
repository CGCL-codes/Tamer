package org.t2framework.t2.navigation;

import org.t2framework.commons.util.Assertion;
import org.t2framework.t2.spi.Navigation;

/**
 * <#if locale="en">
 * <p>
 * Base text type of navigation class.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public abstract class TextNavigation implements Navigation {

    protected final Object object;

    protected final String text;

    /**
	 * <#if locale="en">
	 * <p>
	 * Construct this TextNavigation with object which must not be null.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param object
	 */
    public TextNavigation(Object object) {
        this.object = Assertion.notNull(object);
        this.text = null;
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Construct this TextNavigation with string which must not be null.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param text
	 */
    public TextNavigation(String text) {
        this.object = null;
        this.text = Assertion.notNull(text);
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get object.Can be null.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return object to be text
	 */
    public Object getObject() {
        return object;
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get text.Can be null.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return string to be text
	 */
    public String getText() {
        return text;
    }
}
