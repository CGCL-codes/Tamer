package weka.classifiers.misc.monotone;

/**
 * Interface for incorporating different loss functions.
 * <p>
 * This interface contains only one method, namely <code> loss
 * </code> that measures the error between an actual class
 * value <code> actual </code> and a predicted value <code>
 * predicted. </code>  It is understood that the return value
 * of this method is always be positive and that it is zero
 * if and only if the actual and the predicted value coincide.
 * </p>
 * <p>
 * This implementation is done as part of the master's thesis: "Studie
 * en implementatie van instantie-gebaseerde algoritmen voor gesuperviseerd
 * rangschikken", Stijn Lievens, Ghent University, 2004. 
 * </p>
 * 
 * @author Stijn Lievens (stijn.lievens@ugent.be)
 * @version $Revision: 1.1 $
 */
public interface NominalLossFunction {

    /**
   * Calculate the loss between an actual and a predicted class value.
   *
   * @param actual the actual class value
   * @param predicted the predicted class value
   * @return a measure for the error of making the prediction
   * <code> predicted </code> instead of <code> actual </code>
   */
    public double loss(double actual, double predicted);
}
