package com.android.im.imps;

/**
 * A default implementation of AsyncTransaction for some simple transactions.
 * It might be convenient for a subclass to extend this class instead of
 * AsyncTransaction so it doesn't have to override both onResponseError and
 * onResponseOk but it could as well be missing either of them when doing so.
 * Therefore we make this class final to prevent that from happening.
 */
final class SimpleAsyncTransaction extends AsyncTransaction {

    public SimpleAsyncTransaction(ImpsTransactionManager manager, AsyncCompletion completion) {
        super(manager, completion);
        if (completion == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public void onResponseError(ImpsErrorInfo error) {
    }

    @Override
    public void onResponseOk(Primitive response) {
    }
}
