package net.sf.l2j.gameserver.handler;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public interface IUserCommandHandler {

    public boolean useUserCommand(int id, L2PcInstance activeChar);

    public int[] getUserCommandList();
}
