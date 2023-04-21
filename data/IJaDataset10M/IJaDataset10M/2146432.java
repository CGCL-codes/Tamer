package game.data.projection;

/**
 * User: honza
 * Date: Jun 18, 2007
 * Time: 12:59:54 AM
 */
public interface OptimizeInterface {

    public double minimize(double[] op);

    public int getNumEvaluateCalls();

    public int getNumGradientCalls();

    public int getNumHessianCalls();
}
