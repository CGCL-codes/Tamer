package net.sf.redsetter.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import net.sf.redsetter.figure.ElementFigure;
import net.sf.redsetter.figure.MappingFigure;
import net.sf.redsetter.layout.GraphBorderLayout;
import net.sf.redsetter.model.Element;
import net.sf.redsetter.model.Mapping;
import net.sf.redsetter.model.Element.Position;
import net.sf.redsetter.policy.MappingContainerEditPolicy;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.CommandStackListener;

/**
 * Edit part for Schema object, and uses a SchemaDiagram figure as the container
 * for all graphical objects
 * 
 */
public class MappingPart extends PropertyAwarePart {

    private GraphBorderLayout layoutManager;

    CommandStackListener stackListener = new CommandStackListener() {

        public void commandStackChanged(EventObject event) {
            getFigure().getUpdateManager().performUpdate();
        }
    };

    /**
	 * Adds this EditPart as a command stack listener, which can be used to call
	 * performUpdate() when it changes
	 */
    public void activate() {
        super.activate();
        getViewer().getEditDomain().getCommandStack().addCommandStackListener(stackListener);
    }

    /**
	 * Removes this EditPart as a command stack listener
	 */
    public void deactivate() {
        getViewer().getEditDomain().getCommandStack().removeCommandStackListener(stackListener);
        super.deactivate();
    }

    protected IFigure createFigure() {
        Figure f = new MappingFigure();
        layoutManager = new GraphBorderLayout(this);
        f.setLayoutManager(layoutManager);
        return f;
    }

    public Mapping getMapping() {
        return (Mapping) getModel();
    }

    /**
	 * @return the children Model objects as a new ArrayList
	 */
    protected List getModelChildren() {
        List<Element> l = new ArrayList<Element>();
        Element classA = getMapping().getElementA();
        classA.setVisible(true);
        l.add(classA);
        addContainedClasses(l, classA);
        Element classB = getMapping().getElementB();
        l.add(classB);
        classB.setVisible(true);
        addContainedClasses(l, classB);
        return l;
    }

    private void addContainedClasses(List<Element> l, Element element) {
        List<Element> containedElements = element.getContainedElements();
        for (Iterator<Element> iter = containedElements.iterator(); iter.hasNext(); ) {
            Element containedElement = iter.next();
            l.add(containedElement);
            addContainedClasses(l, containedElement);
        }
    }

    /**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
    public boolean isSelectable() {
        return false;
    }

    /**
	 * Creates EditPolicy objects for the EditPart. The LAYOUT_ROLE policy is
	 * left to the delegating layout manager
	 */
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new MappingContainerEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
    }

    /**
	 * Updates the table bounds in the model so that the same bounds can be
	 * restored after saving
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any TableFigure has no bounds set for any of
	 *         the Table model objects
	 */
    public boolean setClassModelBounds() {
        List mappedClassParts = getChildren();
        for (Iterator iter = mappedClassParts.iterator(); iter.hasNext(); ) {
            ElementPart elementPart = (ElementPart) iter.next();
            ElementFigure elementFigure = (ElementFigure) elementPart.getFigure();
            if (elementFigure == null) continue;
        }
        return true;
    }

    /**
	 * Updates the bounds of the table figure (without invoking any event
	 * handling), and sets layout constraint data
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any Table objects have no bounds set or if
	 *         no figure is available for the TablePart
	 */
    public boolean setMappedClassFigureBounds(boolean updateConstraint) {
        List tableParts = getChildren();
        for (Iterator iter = tableParts.iterator(); iter.hasNext(); ) {
            ElementPart elementPart = (ElementPart) iter.next();
            Element table = elementPart.getElement();
            ElementFigure elementFigure = (ElementFigure) elementPart.getFigure();
            layoutManager.setConstraint(elementFigure, table.getLayoutPosistion());
        }
        Figure f = new Figure();
        Dimension d = new Dimension(20, 20);
        f.setPreferredSize(d);
        f.setVisible(true);
        layoutManager.setConstraint(f, BorderLayout.CENTER);
        return true;
    }

    /**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
    protected void handleLayoutChange(PropertyChangeEvent evt) {
        getFigure().setLayoutManager(layoutManager);
    }

    /**
	 * Sets layout constraint only if XYLayout is active
	 */
    public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint) {
        super.setLayoutConstraint(child, childFigure, constraint);
    }

    /**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
    protected void handleChildChange(PropertyChangeEvent evt) {
        this.refresh();
        if (evt.getNewValue() != null && evt.getNewValue().getClass().isInstance(Element.class)) {
            Element element = (Element) evt.getNewValue();
            ElementPart elementPart = this.findPartForElement(element);
            int width = elementPart.getFigure().getBounds().width;
            if (element.getPosition().equals(Position.LEFT)) {
                this.getMapping().setLeftOffset(width);
            } else {
                this.getMapping().setRightOffset(width);
            }
        }
        super.handleChildChange(evt);
    }
}
