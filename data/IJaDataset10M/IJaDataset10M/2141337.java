package org.ocallahan.chronicle;

public class FindContainingFunctionQuery extends Query {

    public FindContainingFunctionQuery(Session session, long address, long tStamp, Listener listener) {
        super(session, "findContainingFunction");
        builder.append("address", address);
        builder.append("TStamp", tStamp);
        this.listener = listener;
    }

    /**
     * Listener callbacks are run on the session's thread.
     */
    public interface Listener {

        public void notifyDone(FindContainingFunctionQuery q, boolean complete, Function function);
    }

    ;

    void handleDone(boolean complete) {
        listener.notifyDone(this, complete, result);
        listener = null;
    }

    @Override
    void handleResult(JSONObject r) throws JSONParserException {
        if (r.hasValue("entryPoint")) {
            result = new Function(session, r);
        }
    }

    private Listener listener;

    private Function result;
}
