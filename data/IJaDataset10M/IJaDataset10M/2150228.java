package jfun.yan;

/**
 * A lazily evaluated Component.
 * <p>
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
public interface LazyComponent extends Typeful, java.io.Serializable {

    /**
   * Evaluate the Component object.
   * @return the Component object.
   */
    Component eval();
}
