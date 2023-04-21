package mipt.math.sys.pde.scheme;

import mipt.math.Number;
import mipt.math.array.Array;
import mipt.math.array.Matrix;
import mipt.math.array.Vector;
import mipt.math.sys.num.Method;
import mipt.math.sys.pde.grid.Grid;

/**
 * 
 * 
 *
 * @author Korchak Anton
 */
public abstract class AbstractScheme<P extends Pattern> implements Scheme<P> {

    /**
	 * Concrete pattern for scheme.
	 */
    protected P pattern = null;

    /**
	 * Grid for numerical solution.
	 */
    private Grid grid = null;

    /**
	 * The dimension of scheme. In real it is equal to
	 * dimension of grid. So if this value wasn't set manualy
	 * it will be got form grid.
	 */
    private int dimension = -1;

    /**
	 * Concrete scheme multipliers to be used.
	 */
    protected Array scheme = null;

    /**
	 * Trivial class constructor.
	 * @see #setPattern(P)
	 */
    public AbstractScheme() {
    }

    /**
	 * Class constructor.
	 * @param dimension - scheme dimension.
	 * @see #setPattern(P)
	 */
    public AbstractScheme(int dimension) {
        setDimension(dimension);
    }

    /**
	 * Class constructor.
	 * @param pattern - pattern for difference scheme.
	 */
    public AbstractScheme(P pattern) {
        setPattern(pattern);
    }

    /**
	 * Class constructor.
	 * @param grid - grid for numerical solution.
	 * @see #setPattern(P)
	 */
    public AbstractScheme(Grid grid) {
        setGrid(grid);
    }

    /**
	 * Class constructor.
	 * @param pattern - pattern for difference scheme. 
	 * @param dimension - scheme dimension.
	 */
    public AbstractScheme(P pattern, int dimension) {
        this(dimension);
        setPattern(pattern);
    }

    /**
	 * Class constructor.
	 * @param pattern - pattern for difference scheme.
	 * @param grid - grid for numerical solution.
	 */
    public AbstractScheme(P pattern, Grid grid) {
        this(grid);
        setPattern(pattern);
    }

    /**
	 * @return not null.
	 * @see mipt.math.sys.pde.scheme.Scheme#getScheme()
	 */
    public Array getScheme() {
        if (scheme == null) scheme = initScheme();
        return scheme;
    }

    /**
	 * Creation of scheme structure of multipliers. Concrete realization.
	 * Must be called after grid initialization.
	 * @return created scheme. Not null.
	 */
    protected abstract Array initScheme();

    /**
	 * Getting a scheme multiplier for given coordinate of pattern point.
	 * @param coordinates - relative coordinates of pattern point.
	 * @return scheme multiplier.
	 */
    protected abstract Number getMultiplier(int... coordinates);

    /**
	 * @return some grid not null.
	 * @exception IllegalStateException when the grid wasn't defined.
	 * @see mipt.math.sys.pde.scheme.Scheme#getGrid()
	 */
    public Grid<Vector, Matrix> getGrid() {
        if (grid == null) throw new IllegalStateException("The grid wasn't defined.");
        return grid;
    }

    /**
	 * Setting grid to scheme.
	 * @param grid - grid for numerical solution.
	 */
    public void setGrid(Grid grid) {
        this.grid = grid;
        scheme = null;
    }

    /**
	 * @see mipt.math.sys.pde.scheme.Scheme#setPattern(null)
	 */
    public final void setPattern(P pattern) {
        this.pattern = pattern;
    }

    /**
	 * Getting pattern for current scheme.
	 * Hook-method. Should be overridden.
	 * @return pattern to be used.
	 */
    public P getPattern() {
        if (pattern == null) throw new IllegalStateException("The pattern is unknowm.");
        return pattern;
    }

    /**
	 * Setting dimension of scheme.
	 * @param dimension - dimmension of scheme.
	 * @see #getDimension()
	 */
    public final void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
	 * @return some correct value.
	 * @see mipt.math.sys.pde.scheme.Scheme#getDimension()
	 */
    public int getDimension() {
        if (dimension == -1) dimension = getGrid().getDimension();
        return dimension;
    }

    /**
	 * Getting zero type of Number (sub-Number).
	 * Here default realization.
	 * @return zero type of Number (sub-Number).
	 */
    public static final Number getZero() {
        return Number.zero();
    }

    /**
	 * Getting one type of Number (sub-Number).
	 * Here default realization.
	 * @return one type of Number (sub-Number).
	 */
    public static final Number getOne() {
        return Number.one();
    }

    /**
	 * @see mipt.math.sys.num.Algorithm#setMethod(mipt.math.sys.num.Method)
	 */
    public void setMethod(Method method) {
        if (method instanceof Pattern) setPattern((P) method);
    }
}
