package JavaOrc.diagram;

import diagram.*;
import java.awt.Color;
import java.awt.Component;

/**
 * @class ClassRenderer
 *
 * @date 08-27-2001
 * @author Eric Crahen
 * @version 1.1
 * 
 */
public class ClassRenderer extends DefaultFigureRenderer {

    protected ClassRendererComponent classComponent = new ClassRendererComponent();

    /**
   * Get the Component to be fixed to the Figure bounds.
   */
    public Component getUserComponent() {
        ClassFigure figure = (ClassFigure) getFigure();
        ClassItem item = (ClassItem) getDiagram().getModel().getValue(figure);
        classComponent.setTitle((item == null) ? "" : item.getName());
        classComponent.setFields((item == null) ? "" : item.getAttributes());
        classComponent.setMembers((item == null) ? "" : item.getDescription());
        classComponent.setDivider(figure.getDividerLocation());
        return classComponent;
    }
}
