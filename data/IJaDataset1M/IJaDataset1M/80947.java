package com.db4o.internal.cs;

public interface BroadcastFilter {

    public boolean accept(ServerMessageDispatcher dispatcher);
}
