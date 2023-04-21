package samples.packageprivate;

/**
 * This class demonstrates the ability for PowerMock to mock package private
 * classes. This is normally not an issue but since we've changed the CgLib
 * naming policy to allow for signed mocking PowerMock needs to byte-code
 * manipulate this class.
 */
class PackagePrivateClass {

    public int getValue() {
        return returnAValue();
    }

    private int returnAValue() {
        return 82;
    }
}
