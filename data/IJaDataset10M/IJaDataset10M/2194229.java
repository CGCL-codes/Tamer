package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * ShortCutInit format d *(1dddd)/(2ddddd)/(3dddd)
 *
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class ShortCutInit extends L2GameServerPacket {

    private static final String _S__57_SHORTCUTINIT = "[S] 45 ShortCutInit";

    private L2ShortCut[] _shortCuts;

    private L2PcInstance _activeChar;

    public ShortCutInit(L2PcInstance activeChar) {
        _activeChar = activeChar;
        if (_activeChar == null) return;
        _shortCuts = _activeChar.getAllShortCuts();
    }

    @Override
    protected final void writeImpl() {
        writeC(0x45);
        writeD(_shortCuts.length);
        for (L2ShortCut sc : _shortCuts) {
            writeD(sc.getType());
            writeD(sc.getSlot() + sc.getPage() * 12);
            switch(sc.getType()) {
                case L2ShortCut.TYPE_ITEM:
                    writeD(sc.getId());
                    writeD(0x01);
                    writeD(-1);
                    writeD(0x00);
                    writeD(0x00);
                    writeH(0x00);
                    writeH(0x00);
                    break;
                case L2ShortCut.TYPE_SKILL:
                    writeD(sc.getId());
                    writeD(sc.getLevel());
                    writeC(0x00);
                    writeD(0x01);
                    break;
                case L2ShortCut.TYPE_ACTION:
                    writeD(sc.getId());
                    writeD(0x01);
                    break;
                case L2ShortCut.TYPE_MACRO:
                    writeD(sc.getId());
                    writeD(0x01);
                    break;
                case L2ShortCut.TYPE_RECIPE:
                    writeD(sc.getId());
                    writeD(0x01);
                    break;
                default:
                    writeD(sc.getId());
                    writeD(0x01);
            }
        }
    }

    @Override
    public String getType() {
        return _S__57_SHORTCUTINIT;
    }
}
