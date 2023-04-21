package org.zkoss.json;

/**
 * Used to generate a snippet of the JavaScript code.
 *
 * @author tomyeh
 * @since 6.0.0
 */
public class JavaScriptValue implements org.zkoss.json.JSONAware {

    private final String _js;

    /** Constructor
	 * @param js the snippet of the JavaScript code.
	 * For example, "{what: 123, another: 'a'}".
	 * The content is generated directly to the AU response, so it must be
	 * a valid JavaScript code.
	 */
    public JavaScriptValue(String js) {
        if (js == null) throw new IllegalArgumentException();
        _js = js;
    }

    public String toJSONString() {
        return _js;
    }

    public int hashCode() {
        return _js.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof JavaScriptValue && _js.equals(((JavaScriptValue) o)._js);
    }
}
