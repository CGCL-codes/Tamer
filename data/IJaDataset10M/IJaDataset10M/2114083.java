package com.javabi.htmlbuilder.html.element.form;

/**
 * An HTML Text Input Element
 * <p>
 * &lt;input type="text" /&gt;
 * </p>
 */
public class TextBox extends Input {

    /**
	 * Creates a new text box.
	 */
    public TextBox() {
    }

    /**
	 * Creates a new text box.
	 * @param name the name.
	 */
    public TextBox(String name) {
        setName(name);
    }

    /**
	 * Creates a new text box.
	 * @param name the name.
	 * @param value the value.
	 */
    public TextBox(String name, Object value) {
        setName(name);
        setValue(value);
    }

    @Override
    public Type getType() {
        return Type.TEXT;
    }
}
