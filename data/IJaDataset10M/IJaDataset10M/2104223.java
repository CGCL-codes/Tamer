package org.odlabs.wiquery.core.javascript.helper;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.DefaultChainableStatement;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;

/**
 * $Id: AttributesHelper.java 2111 2012-01-19 10:59:18Z reiern70 $
 * <p>
 * Helper to bind attributes functions.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.7
 * @see "http://docs.jquery.com/Attributes"
 */
public class AttributesHelper {

    /**
	 * Binds the <code>attr</code> statement.
	 */
    public static ChainableStatement attr(String key, String value) {
        return new DefaultChainableStatement("attr", JsUtils.quotes(key), JsUtils.quotes(value));
    }

    /**
	 * Binds the <code>attr</code> statement.
	 */
    public static ChainableStatement attr(String key, JsScope computedValue) {
        return new DefaultChainableStatement("attr", JsUtils.quotes(key), computedValue.render());
    }

    /**
	 * Binds the <code>removeAttr</code> statement.
	 */
    public static ChainableStatement removeAttr(String key) {
        return new DefaultChainableStatement("removeAttr", JsUtils.quotes(key));
    }

    /**
	 * Binds the <code>addClass</code> statement.
	 */
    public static ChainableStatement addClass(String className) {
        return new DefaultChainableStatement("addClass", JsUtils.quotes(className));
    }

    /**
	 * Binds the <code>removeClass</code> statement.
	 */
    public static ChainableStatement removeClass(String className) {
        return new DefaultChainableStatement("removeClass", JsUtils.quotes(className));
    }

    /**
	 * Binds the <code>toggleClass</code> statement.
	 */
    public static ChainableStatement toggleClass(String className) {
        return new DefaultChainableStatement("toggleClass", JsUtils.quotes(className));
    }

    /**
	 * Binds the <code>html</code> statement.
	 */
    public static ChainableStatement html(CharSequence htmlContents) {
        return new DefaultChainableStatement("html", JsUtils.quotes(htmlContents));
    }
}
