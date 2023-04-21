package jsynoptic.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import jsynoptic.base.ContextualActionProvider;
import simtools.diagram.Element;
import simtools.shapes.AbstractShape;
import simtools.ui.MenuResourceBundle;
import simtools.ui.ResourceFinder;

/**
 *
 * @author Nicolas Brodu
 *
 * @version 1.0 2001
 */
public class NewShapePopup extends ActionPopup implements ActionListener {

    public static MenuResourceBundle resources = ResourceFinder.getMenu(NewShapePopup.class);

    JMenuItem jmicreate;

    ShapeCreator shapeCreator;

    ShapesContainer invoker;

    public NewShapePopup(ShapesContainer invoker, ShapeCreator s, double x, double y) {
        super(invoker.getComponent(), x, y, s, ContextualActionProvider.SHAPELIST_CONTEXT);
        this.invoker = invoker;
        shapeCreator = s;
        jmicreate = resources.getItem("create", this);
        if ((super.getComponentCount() > 0) && (!(super.getComponent(0) instanceof JPopupMenu.Separator))) super.insert(new JPopupMenu.Separator(), 0);
        super.insert(jmicreate, 0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jmicreate) {
            Element element = shapeCreator.create();
            if (element instanceof AbstractShape) {
                ((AbstractShape) element).setAnchor(0, 0);
            }
            element.translate((int) actionX, (int) actionY);
            invoker.addElement(element);
            invoker.getComponent().repaint();
        }
        super.actionPerformed(e);
    }
}
