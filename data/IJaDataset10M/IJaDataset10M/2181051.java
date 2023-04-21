package evolution.mlp.functions;

/**
 * @author camille
 * 
 */
public interface Function {

    /**
	 * Computes and return F(x)
	 * @param x
	 * @return F(x)
	 */
    public abstract double eval(double x);
}
