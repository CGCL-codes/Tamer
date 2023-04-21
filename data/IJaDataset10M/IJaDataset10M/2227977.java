package serl.equalschecker.controlflow;

/**
 * Note that this class is not a public API. It is used store intermediate result of path expansion.
 *
 * @author Chandan Raj Rupakheti
 */
class Result {

    public static final int FINAL = 0;

    public static final int SUCCESS = 1;

    public static final int CANCELED = 2;

    public static final int CUT_OFF = 3;

    public static final int PRUNE_DISPATCH = 4;

    public int type;

    public PathGroup pathGroup;

    public Result() {
        this.type = CANCELED;
        this.pathGroup = new PathGroup(0);
    }
}
