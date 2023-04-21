package org.nakedobjects.runtime.transaction;

public interface TransactionalClosure {

    public void preExecute();

    public void execute();

    public void onSuccess();

    public void onFailure();
}
