package org.peaseplate.queryengine;

import org.peaseplate.utils.command.Scope;
import org.peaseplate.utils.exception.ExecuteException;

public interface Query extends QueryExecutor {

    public Object execute(Scope scope) throws ExecuteException;
}
