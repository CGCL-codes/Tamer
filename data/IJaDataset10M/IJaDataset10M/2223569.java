package ru.ifmo.fcdesigner.flowchart.view;

import taiga.config.ConfigurationException;
import java.awt.*;
import ru.ifmo.fcdesigner.flowchart.model.ScriptElement;
import ru.ifmo.fcdesigner.flowchart.view.visitors.ViewVisitor;
import ru.ifmo.fcdesigner.flowchart.view.visitors.SimpleViewVisitor;

/**
 * Script element.
 *
 * @author Dmitry Paraschenko
 */
public class ScriptElementView extends AbstractBoundedElementView<ScriptElement> {

    public ScriptElementView(Rectangle rectangle, FlowchartElementView flowchart) throws ConfigurationException {
        super(rectangle, flowchart);
    }

    public ScriptElementView(Point p, FlowchartElementView flowchart) {
        super(p, flowchart);
    }

    public <R> R accept(SimpleViewVisitor<R> visitor) {
        return visitor.visitScriptElement(this);
    }

    public <R, A> R accept(ViewVisitor<R, A> visitor, A arg) {
        return visitor.visitScriptElement(this, arg);
    }
}
