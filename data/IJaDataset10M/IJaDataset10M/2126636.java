package spacefaring.events;

public class DialogEvent extends Event {

    public enum Result {

        OK, Canceled
    }

    private Result result;

    private int contextid;

    public DialogEvent(Result res) {
        this(res, 0);
    }

    public DialogEvent(Result res, int conid) {
        result = res;
        contextid = conid;
    }

    public Result getResult() {
        return result;
    }

    public int getContextId() {
        return contextid;
    }
}
