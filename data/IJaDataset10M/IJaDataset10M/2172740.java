package mipt.math.sys.pde.poisson;

import mipt.math.Number;
import mipt.math.array.Vector;
import mipt.math.sys.pde.AbstractBoundaryConditionContainer;
import mipt.math.sys.pde.BoundaryCondition;
import mipt.math.sys.pde.Rectangular2DBoundary;

/**
 * The simples boundary condition for Poisson equation problem.
 * The boundary is a rectangle. And doundary conditions are type of Dirichlet.
 * 
 * @author Korchak Anton
 */
public class Rectangle2DBoundaryConditionContainer extends AbstractBoundaryConditionContainer<Rectangular2DBoundary> {

    /**
	 * The top boundary condition.
	 */
    protected BoundaryCondition topCondition = null;

    /**
	 * The bottom boundary condition.
	 */
    protected BoundaryCondition bottomCondition = null;

    /**
	 * The right boundary condition.
	 */
    protected BoundaryCondition rightCondition = null;

    /**
	 * The left boundary condition.
	 */
    protected BoundaryCondition leftCondition = null;

    /**
	 * Default class constructor.
	 */
    public Rectangle2DBoundaryConditionContainer() {
    }

    /**
	 * Class constructor.
	 * @param topCondition - the top boundary condition.
	 * @param rightCondition - the right boundary condition.
	 * @param bottomCondition - the bottom boundary condition.
	 * @param leftCondition - the left boundary condition.
	 */
    public Rectangle2DBoundaryConditionContainer(BoundaryCondition topCondition, BoundaryCondition rightCondition, BoundaryCondition bottomCondition, BoundaryCondition leftCondition) {
        setTopCondition(topCondition);
        setRightCondition(rightCondition);
        setBottomCondition(bottomCondition);
        setLeftCondition(leftCondition);
    }

    /**
	 * Getting the top boundary condition.
	 * @return the top boundary condition. Not null.
	 * @exception IllegalStateException when the top boundary condition wasn't set.
	 */
    public BoundaryCondition getTopCondition() {
        if (topCondition == null) throw new IllegalStateException("The top boundary condition wasn't set.");
        return topCondition;
    }

    /**
	 * Getting the bottom boundary condition.
	 * @return the bottom boundary condition. Not null.
	 * @exception IllegalStateException when the bottom boundary condition wasn't set.
	 */
    public BoundaryCondition getBottomCondition() {
        if (bottomCondition == null) throw new IllegalStateException("The bottom boundary condition wasn't set.");
        return bottomCondition;
    }

    /**
	 * Getting the right boundary condition.
	 * @return the right boundary condition. Not null.
	 * @exception IllegalStateException when the right boundary condition wasn't set.
	 */
    public BoundaryCondition getRightCondition() {
        if (rightCondition == null) throw new IllegalStateException("The right boundary condition wasn't set.");
        return rightCondition;
    }

    /**
	 * Getting the left boundary condition.
	 * @return the left boundary condition. Not null.
	 * @exception IllegalStateException when the left boundary condition wasn't set.
	 */
    public BoundaryCondition getLeftCondition() {
        if (leftCondition == null) throw new IllegalStateException("The right boundary condition wasn't set.");
        return leftCondition;
    }

    /**
	 * Setting the top boundary condition.
	 * @param topCondition - the top boundary condition.
	 */
    public final void setTopCondition(BoundaryCondition topCondition) {
        this.topCondition = topCondition;
    }

    /**
	 * Setting the bottom boundary condition.
	 * @param bottomCondition - the bottom boundary condition.
	 */
    public final void setBottomCondition(BoundaryCondition bottomCondition) {
        this.bottomCondition = bottomCondition;
    }

    /**
	 * Setting the right boundary condition.
	 * @param rightCondition - the right boundary condition.
	 */
    public final void setRightCondition(BoundaryCondition rightCondition) {
        this.rightCondition = rightCondition;
    }

    /**
	 * Setting the left boundary condition.
	 * @param leftCondition - the left boundary condition.
	 */
    public final void setLeftCondition(BoundaryCondition leftCondition) {
        this.leftCondition = leftCondition;
    }

    /**
	 * @see mipt.math.sys.pde.BoundaryConditionsContainer#getType()
	 */
    public int getType() {
        int type = getTopCondition().getType();
        if (getBottomCondition().getType() == type && getRightCondition().getType() == type && getLeftCondition().getType() == type) return type;
        return BoundaryCondition.MIXED;
    }

    /**
	 * Delegating to two dimensional case function.
	 * @see #getBoundaryCondition(Number, Number)
	 * @see mipt.math.sys.pde.AbstractBoundaryConditionContainer#getBoundaryCondition(mipt.math.array.Vector)
	 * @exception IllegalArgumentException when dimension of coordinates are less than 2.
	 */
    public BoundaryCondition getBoundaryCondition(Vector coordinates) {
        if (coordinates == null) return null;
        if (coordinates.size() != 2) throw new IllegalArgumentException("Poisson problem has 2 dimensions, x and y.");
        return getBoundaryCondition(coordinates.get(0), coordinates.get(1));
    }

    /**
	 * Getting a boundary condition corresponding to given coordinates.
	 * @param x - x-coordinate of point of bound.
	 * @param y - y-coordinate of point of bound.
	 * @return the boundary condition. Not null.
	 */
    public BoundaryCondition getBoundaryCondition(Number x, Number y) {
        int side = getBoundary().border(x, y);
        switch(side) {
            case Rectangular2DBoundary.BOTTOM:
                return getBottomCondition();
            case Rectangular2DBoundary.TOP:
                return getTopCondition();
            case Rectangular2DBoundary.RIGHT:
                return getRightCondition();
            case Rectangular2DBoundary.LEFT:
                return getLeftCondition();
            default:
                throw new IllegalArgumentException("Given point is out of the boundary.");
        }
    }
}
