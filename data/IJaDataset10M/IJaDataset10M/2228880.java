package com.l2jserver.gameserver.model;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * This class manages requests (transactions) between two L2PcInstance.
 *
 * @author  kriau
 */
public class L2Request {

    private static final int REQUEST_TIMEOUT = 15;

    protected L2PcInstance _player;

    protected L2PcInstance _partner;

    protected boolean _isRequestor;

    protected boolean _isAnswerer;

    protected L2GameClientPacket _requestPacket;

    public L2Request(L2PcInstance player) {
        _player = player;
    }

    protected void clear() {
        _partner = null;
        _requestPacket = null;
        _isRequestor = false;
        _isAnswerer = false;
    }

    /**
	 * Set the L2PcInstance member of a transaction (ex : FriendInvite, JoinAlly, JoinParty...).<BR><BR>
	 */
    private synchronized void setPartner(L2PcInstance partner) {
        _partner = partner;
    }

    /**
	 * Return the L2PcInstance member of a transaction (ex : FriendInvite, JoinAlly, JoinParty...).<BR><BR>
	 */
    public L2PcInstance getPartner() {
        return _partner;
    }

    /**
	 * Set the packet incomed from requestor.<BR><BR>
	 */
    private synchronized void setRequestPacket(L2GameClientPacket packet) {
        _requestPacket = packet;
    }

    /**
	 * Return the packet originally incomed from requestor.<BR><BR>
	 */
    public L2GameClientPacket getRequestPacket() {
        return _requestPacket;
    }

    /**
	 * Checks if request can be made and in success case puts both PC on request state.<BR><BR>
	 */
    public synchronized boolean setRequest(L2PcInstance partner, L2GameClientPacket packet) {
        if (partner == null) {
            _player.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET));
            return false;
        }
        if (partner.getRequest().isProcessingRequest()) {
            SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
            sm.addString(partner.getName());
            _player.sendPacket(sm);
            sm = null;
            return false;
        }
        if (isProcessingRequest()) {
            _player.sendPacket(new SystemMessage(SystemMessageId.WAITING_FOR_ANOTHER_REPLY));
            return false;
        }
        _partner = partner;
        _requestPacket = packet;
        setOnRequestTimer(true);
        _partner.getRequest().setPartner(_player);
        _partner.getRequest().setRequestPacket(packet);
        _partner.getRequest().setOnRequestTimer(false);
        return true;
    }

    private void setOnRequestTimer(boolean isRequestor) {
        _isRequestor = isRequestor ? true : false;
        _isAnswerer = isRequestor ? false : true;
        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable() {

            public void run() {
                clear();
            }
        }, REQUEST_TIMEOUT * 1000);
    }

    /**
	 * Clears PC request state. Should be called after answer packet receive.<BR><BR>
	 */
    public void onRequestResponse() {
        if (_partner != null) {
            _partner.getRequest().clear();
        }
        clear();
    }

    /**
	 * Return True if a transaction is in progress.<BR><BR>
	 */
    public boolean isProcessingRequest() {
        return _partner != null;
    }
}
