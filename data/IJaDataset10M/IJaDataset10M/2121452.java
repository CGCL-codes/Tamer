package com.iv.flash.context;

/**
 * Fake Generator context.
 * <p>
 * Generator context which sends all requests through itself
 * to its parent. Helpful when we don't know which kind of context
 * it's going to be: XMLContext or StandardContext. After we know
 * this this FakeContext is discarded and new context (xml or text) is
 * linked directly to the parent of this one.
 *
 * @author Dmitry Skavish
 * @see com.iv.flash.api.Script#process
 */
public class FakeContext extends Context {

    private Context context;

    public FakeContext() {
    }

    public FakeContext(Context parent) {
        setParent(parent);
    }

    public String getValue(String name) {
        return getValueFromParent(name);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
