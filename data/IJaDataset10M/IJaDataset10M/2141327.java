package jpfm.fs.splitfs;

/**
 *
 * @author Shashank Tulsyan
 */
public final class SplitFSInstanceProvider {

    private static final SplitFSInstanceProvider INSTANCE = new SplitFSInstanceProvider();

    private SplitFSInstanceProvider() {
    }

    static SplitFSInstanceProvider getInstance() {
        return INSTANCE;
    }
}
