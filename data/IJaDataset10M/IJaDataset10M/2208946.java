package helper;

/**
 * This class is used to create mutable boolean Flag. This way instances can be
 * declared final but the value property can still be changed. This is handy for inner classes
 * that would like to return values to other code such as Runnable etc.
 */
public class TestFlag {

    public boolean value = false;

    /**
   * Create Flag with Default Value
   */
    public TestFlag(boolean v) {
        value = v;
    }
}
