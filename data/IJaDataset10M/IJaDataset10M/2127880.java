package org.avaje.ebean.bean;

import org.avaje.ebean.Ebean;
import org.avaje.ebean.EbeanServer;
import org.avaje.ebean.TxScope;
import org.avaje.ebean.server.core.InternalEbeanServer;

/**
 * Helper object to make AOP generated code simpler.
 */
public class InternalServer {

    /**
	 * Create a ScopeTrans for a given methods TxScope.
	 */
    public static ScopeTrans createScopeTrans(TxScope txScope) {
        EbeanServer server = Ebean.getServer(txScope.getServerName());
        InternalEbeanServer iserver = (InternalEbeanServer) server;
        return iserver.createScopeTrans(txScope);
    }

    /**
	 * Exiting the method in an expected fashion.
	 * <p>
	 * That is returning successfully or via a caught exception. 
	 * Unexpected exceptions are caught via the Thread uncaughtExceptionHandler.
	 * </p>
	 * @param returnOrThrowable the return or throwable object
	 * @param opCode the opcode for ATHROW or ARETURN etc
	 * @param scopeTrans the scoped transaction the method was run with.
	 */
    public static void onExitScopeTrans(Object returnOrThrowable, int opCode, ScopeTrans scopeTrans) {
        scopeTrans.onExit(returnOrThrowable, opCode);
    }
}
