package methods;

/**
 *
 * @author misiek (mw219725@gmail.com)
 */
public class TransformingMethodLog implements DoubleTransformingMethod {

    public Double transform(Double x) {
        return Math.log(x);
    }
}
