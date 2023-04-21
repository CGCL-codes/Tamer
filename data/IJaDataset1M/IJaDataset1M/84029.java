package handlers.scripthandlers;

import l2.universe.gameserver.handler.IScriptHandler;
import l2.universe.gameserver.handler.ScriptHandler.CallSite;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author BiggBoss
 *
 */
public class EnterWorld implements IScriptHandler {

    @Override
    public boolean execute(L2PcInstance activeChar, L2Object target) {
        return true;
    }

    @Override
    public CallSite getCallSite() {
        return CallSite.ON_ENTER;
    }
}
