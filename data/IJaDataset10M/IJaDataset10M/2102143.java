package diff.misc;

public abstract class PathNode {

    public final int i;

    public final int j;

    public final PathNode prev;

    public PathNode(int i, int j, PathNode prev) {
        this.i = i;
        this.j = j;
        this.prev = prev;
    }

    public abstract boolean isSnake();

    public boolean isBootstrap() {
        return i < 0 || j < 0;
    }

    public final PathNode previousSnake() {
        PathNode result = this;
        if (isBootstrap()) {
            result = null;
        } else if (!isSnake() && prev != null) {
            result = prev.previousSnake();
        }
        return result;
    }
}
