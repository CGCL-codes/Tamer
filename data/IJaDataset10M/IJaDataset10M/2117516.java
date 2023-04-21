package org.mvc.form.elements;

import org.mvc.form.Element;
import org.mvc.form.renderers.InputRenderer;
import org.mvc.form.renderers.Renderer;

public class Submit extends Element {

    public Submit(String name) {
        super();
        this.type = "submit";
        this.name = name;
    }

    @Override
    public Renderer createRenderer() {
        return new InputRenderer();
    }
}
