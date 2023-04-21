package inventor;

public class SbVec3f {

    /**
     * pointer to the native object accessed by this object.
     */
    protected long nativeObject = 0;

    /** 
     * whehter this object owns the native object, and thus will
     * delete it during finalization.
     */
    protected boolean ownsNativeObject = true;

    /**
     * Creates a SbVec3f from the given native object id.
     *
     * @param nativeObject the pointer to a native object.
     * @param ownsNativeObject if true the object will destroy
     * the native object when finalized, otherwise the native
     * object is not deleted.
     */
    public SbVec3f(long nativeObject, boolean ownsNativeObject) {
        this.nativeObject = nativeObject;
        this.ownsNativeObject = ownsNativeObject;
    }

    /**
     * Destroys the object.
     */
    public void finalize() throws Throwable {
        super.finalize();
    }
}
