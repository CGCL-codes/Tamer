package net.sf.opengroove.realmserver.handlers;

import net.sf.opengroove.realmserver.DataStore;
import net.sf.opengroove.realmserver.web.Handler;
import net.sf.opengroove.realmserver.web.HandlerContext;

public class ListUsers implements Handler {

    @Override
    public void handle(HandlerContext context) throws Exception {
        context.getRequest().setAttribute("userList", DataStore.listUsers());
    }
}
