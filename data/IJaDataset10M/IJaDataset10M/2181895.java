package inventor.nodes;

public class SoPointLight extends SoLight {

    /**
     *  Creates a SoPointLight.
     */
    public SoPointLight() {
        this(create(), false);
    }

    /**
     *  Returns a new native object.
     */
    private static native long create();

    /**
     *  Creates a SoPointLight from the given native object id.
     *
     *  @param nativeObject the pointer to a native object.
     *  @param ownsNativeObject if true the object will destroy
     *  the native object when finalized. Otherwise the native
     *  object is not deleted.
     */
    public SoPointLight(long nativeObject, boolean ownsNativeObject) {
        super(nativeObject, ownsNativeObject);
    }

    /**
     *  Destroys the object.
     */
    public void finalize() throws Throwable {
        super.finalize();
    }
}
