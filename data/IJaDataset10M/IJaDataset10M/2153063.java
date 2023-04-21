package jp.go.ipa.jgcl;

/**
 * Complex function base class
 *
 * @author Information-technology Promotion Agency, Japan
 */
public interface ComplexFunction {

    /**
	 * Evaluator
	 *
	 * @param parameter
	 * @return
	 */
    public Complex[] evaluate(Complex[] parameter);
}
