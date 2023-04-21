package org.jnormalform;

import org.jnormalform.ui.IUser;

public interface IRefactoringStratergy {

    public int refactor(Context ctx);

    public IUser getUser();
}
