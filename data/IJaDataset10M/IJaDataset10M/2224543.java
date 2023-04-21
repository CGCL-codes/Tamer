package com.rapidminer.gui.renderer;

import java.awt.Component;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.report.Reportable;

/**
 * This is the abstract renderer superclass for all renderers which
 * should simply use a Java component. Basically, this class only exists
 * to allow for dirty hacks and should not be used in general.
 * 
 * @author Ingo Mierswa
 */
public class DefaultComponentRenderer extends AbstractRenderer {

    private String name;

    private Component component;

    public DefaultComponentRenderer(String name, Component component) {
        this.name = name;
        this.component = component;
    }

    public Reportable createReportable(Object renderable, IOContainer ioContainer, int desiredWidth, int desiredHeight) {
        return new DefaultComponentRenderable(component);
    }

    public String getName() {
        return this.name;
    }

    public Component getVisualizationComponent(Object renderable, IOContainer ioContainer) {
        return component;
    }
}
