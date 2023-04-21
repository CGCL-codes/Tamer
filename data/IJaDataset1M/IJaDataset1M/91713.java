package org.maestroframework.maestro.widgets;

import org.maestroframework.markup.Component;

public class MFormFieldSet extends AbstractMaestroForm {

    public MFormFieldSet(String name) {
        super("fieldset");
        Component legend = add(new Component("legend"));
        legend.add(name);
    }
}
