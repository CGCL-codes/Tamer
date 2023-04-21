package net.sf.l2j.gameserver.network.serverpackets;

/**
 * Format: ch
 *
 * @author devScarlet & mrTJO
 */
public class ExShowSlideshowKamael extends L2GameServerPacket {

    private static final String _S__FE_5B_EXSHOWSLIDESHOWKAMAEL = "[S] FE:5B ExShowSlideshowKamael";

    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x5b);
    }

    @Override
    public String getType() {
        return _S__FE_5B_EXSHOWSLIDESHOWKAMAEL;
    }
}
