package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * 
 * @author JIV
 */
public final class EndScenePlayer extends L2GameClientPacket {

    private static final String _C__d05b_EndScenePlayer = "[C] d0:5b EndScenePlayer";

    private static Logger _log = Logger.getLogger(EndScenePlayer.class.getName());

    private int _movieId;

    @Override
    protected void readImpl() {
        _movieId = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        if (_movieId == 0) return;
        if (activeChar.getMovieId() != _movieId) {
            _log.warning("Player " + getClient() + " sent EndScenePlayer with wrong movie id: " + _movieId);
            return;
        }
        activeChar.setMovieId(0);
        activeChar.setIsTeleporting(true, false);
        activeChar.decayMe();
        activeChar.spawnMe(activeChar.getPosition().getX(), activeChar.getPosition().getY(), activeChar.getPosition().getZ());
        activeChar.setIsTeleporting(false, false);
    }

    @Override
    public String getType() {
        return _C__d05b_EndScenePlayer;
    }
}
