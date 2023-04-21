package net.sourceforge.javautil.gui.swing.util;

import java.awt.Component;
import java.awt.Container;
import net.sourceforge.javautil.common.visitor.VisitorContextBase;

/**
 * The context in which {@link Component}'s are visited inside a {@link Container} tree.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class ComponentVisitorContext extends VisitorContextBase<Container, Component, ComponentVisitorContext> {

    public ComponentVisitorContext(Container visitable) {
        super(visitable);
    }

    /**
	 * This assumes visiting from the root
	 * 
	 * @see #visit(IComponentVisitor, Component)
	 */
    public void visit(IComponentVisitor visitor) {
        this.visit(visitor, this.visitable);
    }

    /**
	 * The actual tree loop for visiting the components.
	 * 
	 * @param visitor The visitor that will be invoked
	 * @param component The component currently to visit
	 */
    protected void visit(IComponentVisitor visitor, Component component) {
        this.skip = false;
        visitor.visit(this.setVisited(component));
        if (this.isAborted() || this.isSkipped()) return;
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++) {
                this.visit(visitor, container.getComponent(i));
                if (this.isAborted() || this.isSkipped()) break;
            }
        }
    }
}
