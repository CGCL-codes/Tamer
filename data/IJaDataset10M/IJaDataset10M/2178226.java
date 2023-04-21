package corina.index;

/**
    A basis function for fitting data points to, using
    least-squares.  If an object implements this interface,
    <code>Solver.leastSquares()</code> can be used to fit it to data.

    <p>For example, if you want to fit some data to a quadratic,
    your basis vector would be [ 1 x x<sup>2</sup> ], and your code
    might look like:</p>

<pre>
    public double[] f(double x) {
        return new double[] { 1, x, x*x };
    }
</pre>

    @see Solver

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id: Function.java,v 1.1 2004/01/18 18:03:08 aaron Exp $
*/
public interface Function {

    /**
        Evaluate the basis function(s) at a point.  The size of
        the return array is assumed to always be the same size.
        This must return a new array every call, because it it
        used to fill in the rows of an array.

        @param x the point to evaluate
        @return the basis functions evaluated at x
    */
    public double[] f(double x);
}
