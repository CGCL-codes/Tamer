package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.EnchantGroupsTable;
import com.l2jserver.gameserver.model.L2EnchantSkillLearn;
import com.l2jserver.gameserver.model.L2EnchantSkillGroup.EnchantSkillDetail;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * 
 * @author KenM
 */
public class ExEnchantSkillInfoDetail extends L2GameServerPacket {

    private static final int TYPE_NORMAL_ENCHANT = 0;

    private static final int TYPE_SAFE_ENCHANT = 1;

    private static final int TYPE_UNTRAIN_ENCHANT = 2;

    private static final int TYPE_CHANGE_ENCHANT = 3;

    private int bookId = 0;

    private int reqCount = 0;

    private int multi = 1;

    private final int _type;

    private final int _skillid;

    private final int _skilllvl;

    private final int _chance;

    private int _sp;

    private final int _adenacount;

    public ExEnchantSkillInfoDetail(int type, int skillid, int skilllvl, L2PcInstance ply) {
        L2EnchantSkillLearn enchantLearn = EnchantGroupsTable.getInstance().getSkillEnchantmentBySkillId(skillid);
        EnchantSkillDetail esd = null;
        if (enchantLearn != null) {
            if (skilllvl > 100) {
                esd = enchantLearn.getEnchantSkillDetail(skilllvl);
            } else esd = enchantLearn.getFirstRouteGroup().getEnchantGroupDetails().get(0);
        }
        if (esd == null) throw new IllegalArgumentException("Skill " + skillid + " dont have enchant data for level " + skilllvl);
        if (type == 0) multi = EnchantGroupsTable.NORMAL_ENCHANT_COST_MULTIPLIER; else if (type == 1) multi = EnchantGroupsTable.SAFE_ENCHANT_COST_MULTIPLIER;
        _chance = esd.getRate(ply);
        _sp = esd.getSpCost();
        if (type == TYPE_UNTRAIN_ENCHANT) _sp = (int) (0.8 * _sp);
        _adenacount = esd.getAdenaCost() * multi;
        _type = type;
        _skillid = skillid;
        _skilllvl = skilllvl;
        switch(type) {
            case TYPE_NORMAL_ENCHANT:
                bookId = EnchantGroupsTable.NORMAL_ENCHANT_BOOK;
                reqCount = ((_skilllvl % 100 > 1) ? 0 : 1);
                break;
            case TYPE_SAFE_ENCHANT:
                bookId = EnchantGroupsTable.SAFE_ENCHANT_BOOK;
                reqCount = 1;
                break;
            case TYPE_UNTRAIN_ENCHANT:
                bookId = EnchantGroupsTable.UNTRAIN_ENCHANT_BOOK;
                reqCount = 1;
                break;
            case TYPE_CHANGE_ENCHANT:
                bookId = EnchantGroupsTable.CHANGE_ENCHANT_BOOK;
                reqCount = 1;
                break;
            default:
                return;
        }
        if (type != TYPE_SAFE_ENCHANT && !Config.ES_SP_BOOK_NEEDED) reqCount = 0;
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#getType()
	 */
    @Override
    public String getType() {
        return "[S] FE:5E ExEnchantSkillInfoDetail";
    }

    /**
	 * @see com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0x5e);
        writeD(_type);
        writeD(_skillid);
        writeD(_skilllvl);
        writeD(_sp * multi);
        writeD(_chance);
        writeD(2);
        writeD(57);
        writeD(_adenacount);
        writeD(bookId);
        writeD(reqCount);
    }
}
