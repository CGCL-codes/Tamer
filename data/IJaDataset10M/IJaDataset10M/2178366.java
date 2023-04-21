package net.ar.guia.own.layouters;

import net.ar.guia.own.implementation.*;
import net.ar.guia.render.templates.*;

public class DefaultGuiaTemplate extends DefaultTemplate {

    protected DefaultBounds bounds;

    public DefaultGuiaTemplate() {
        this(new DefaultBounds(0, 0, 100, 100));
    }

    public DefaultGuiaTemplate(DefaultBounds aBounds) {
        super(new DefaultGuiaTemplateBody());
        bounds = aBounds;
    }

    public DefaultBounds getBounds() {
        return bounds;
    }

    public void setBounds(DefaultBounds bounds) {
        this.bounds = bounds;
    }

    public String toString() {
        return this.getClass().getName();
    }
}
