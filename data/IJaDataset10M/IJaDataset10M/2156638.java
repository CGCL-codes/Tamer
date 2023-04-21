package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.SevenSigns;

/**
 * Changes the sky color depending on the outcome
 * of the Seven Signs competition.
 *
 * packet type id 0xf8
 * format: c h
 *
 * @author Tempy
 */
public class SSQInfo extends L2GameServerPacket {

    private static final String _S__F8_SSQINFO = "[S] 73 SSQInfo";

    private static int _state = 0;

    public SSQInfo() {
        int compWinner = SevenSigns.getInstance().getCabalHighestScore();
        if (SevenSigns.getInstance().isSealValidationPeriod()) if (compWinner == SevenSigns.CABAL_DAWN) _state = 2; else if (compWinner == SevenSigns.CABAL_DUSK) _state = 1;
    }

    public SSQInfo(int state) {
        _state = state;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x73);
        if (_state == 2) {
            writeH(258);
        } else if (_state == 1) {
            writeH(257);
        } else {
            writeH(256);
        }
    }

    @Override
    public String getType() {
        return _S__F8_SSQINFO;
    }
}
