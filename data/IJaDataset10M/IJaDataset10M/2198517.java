package net.sourceforge.jaulp.designpattern.visitor.example.first;

import net.sourceforge.jaulp.designpattern.visitor.GenericVisitor;

/**
 * The Interface MenuVisitor.
 */
public interface MenuVisitor extends GenericVisitor<MenuVisitor, MenuVisitableObject> {

    /**
	 * Visit.
	 *
	 * @param menu the menu
	 */
    void visit(Menu menu);

    /**
	 * Visit.
	 *
	 * @param menuItem the menu item
	 */
    void visit(MenuItem menuItem);
}
