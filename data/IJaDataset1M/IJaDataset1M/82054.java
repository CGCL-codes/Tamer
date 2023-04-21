package com.db4o.internal.cs.messages;

import com.db4o.internal.cs.*;
import com.db4o.internal.query.result.*;

/**
 * @exclude
 */
public abstract class MObjectSet extends MsgD {

    protected AbstractQueryResult queryResult(int queryResultID) {
        return stub(queryResultID).queryResult();
    }

    protected LazyClientObjectSetStub stub(int queryResultID) {
        ServerMessageDispatcher serverThread = serverMessageDispatcher();
        return serverThread.queryResultForID(queryResultID);
    }
}
