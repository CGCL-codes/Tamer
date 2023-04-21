package net.sf.doolin.gui.core.view.layout;

/**
 * This interface allows the constraints to be adapted to the actual layout.
 * 
 * @author Damien Coraboeuf
 * @version $Id: LayoutConstraintAdapter.java,v 1.1 2007/08/10 16:54:41 guinnessman Exp $
 * @param <C>
 *            Configured constraint type
 * @param <A>
 *            Actual constraint type
 */
public interface LayoutConstraintAdapter<C, A> {

    /**
	 * Adapts the constraint given in the configuration to a constraint suitable
	 * for a layout.
	 * 
	 * @param constraint
	 *            Constraint given in the configuration
	 * @return Constraint suitable for a layout.
	 */
    A adapt(C constraint);
}
