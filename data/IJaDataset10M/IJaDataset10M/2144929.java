package inventor.nodes;

public class SoTransformation extends SoNode {

    /**
	 *  Creates a SoTransformation from the given native object id.
	 *
	 *  @param nativeObject the pointer to a native object.
	 *  @param ownsNativeObject if true the object will destroy
	 *  the native object when finalized. Otherwise the native
	 *  object is not deleted.
	 */
    public SoTransformation(long nativeObject, boolean ownsNativeObject) {
        super(nativeObject, ownsNativeObject);
    }

    /**
	 *  Destroys the object.
	 */
    public void finalize() throws Throwable {
        super.finalize();
    }
}
