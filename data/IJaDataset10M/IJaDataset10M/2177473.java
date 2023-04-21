package samples.suppresseverything;

public class SuppressEverything {

    public SuppressEverything() {
        throw new IllegalStateException("error");
    }

    public SuppressEverything(String string) {
        throw new IllegalStateException("error");
    }

    public int something() {
        throw new IllegalStateException("error");
    }

    public void somethingElse() {
        throw new IllegalStateException("error");
    }
}
