package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.EnchantGroupsTable;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2EnchantSkillLearn;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2ShortCut;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2EnchantSkillGroup.EnchantSkillDetail;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExBrExtraUserInfo;
import com.l2jserver.gameserver.network.serverpackets.ExEnchantSkillInfo;
import com.l2jserver.gameserver.network.serverpackets.ExEnchantSkillInfoDetail;
import com.l2jserver.gameserver.network.serverpackets.ExEnchantSkillResult;
import com.l2jserver.gameserver.network.serverpackets.ShortCutRegister;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * Format (ch) dd
 * c: (id) 0xD0
 * h: (subid) 0x33
 * d: skill id
 * d: skill lvl
 * @author -Wooden-
 *
 */
public final class RequestExEnchantSkillUntrain extends L2GameClientPacket {

    private static final Logger _log = Logger.getLogger(RequestExEnchantSkillUntrain.class.getName());

    private static final Logger _logEnchant = Logger.getLogger("enchant");

    private int _skillId;

    private int _skillLvl;

    @Override
    protected void readImpl() {
        _skillId = readD();
        _skillLvl = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (player.getClassId().level() < 3) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_IN_THIS_CLASS);
            return;
        }
        if (player.getLevel() < 76) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_ON_THIS_LEVEL);
            return;
        }
        if (!player.isAllowedToEnchantSkills()) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_USE_SKILL_ENCHANT_ATTACKING_TRANSFORMED_BOAT);
            return;
        }
        L2EnchantSkillLearn s = EnchantGroupsTable.getInstance().getSkillEnchantmentBySkillId(_skillId);
        if (s == null) return;
        if (_skillLvl % 100 == 0) {
            _skillLvl = s.getBaseLevel();
        }
        L2Skill skill = SkillTable.getInstance().getInfo(_skillId, _skillLvl);
        if (skill == null) return;
        int reqItemId = EnchantGroupsTable.UNTRAIN_ENCHANT_BOOK;
        int currentLevel = player.getSkillLevel(_skillId);
        if (currentLevel - 1 != _skillLvl && (currentLevel % 100 != 1 || _skillLvl != s.getBaseLevel())) return;
        EnchantSkillDetail esd = s.getEnchantSkillDetail(currentLevel);
        int requiredSp = esd.getSpCost();
        int requireditems = esd.getAdenaCost();
        L2ItemInstance spb = player.getInventory().getItemByItemId(reqItemId);
        if (Config.ES_SP_BOOK_NEEDED) {
            if (spb == null) {
                player.sendPacket(new SystemMessage(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL));
                return;
            }
        }
        if (player.getInventory().getAdena() < requireditems) {
            player.sendPacket(new SystemMessage(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL));
            return;
        }
        boolean check = true;
        if (Config.ES_SP_BOOK_NEEDED) {
            check &= player.destroyItem("Consume", spb.getObjectId(), 1, player, true);
        }
        check &= player.destroyItemByItemId("Consume", 57, requireditems, player, true);
        if (!check) {
            player.sendPacket(new SystemMessage(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL));
            return;
        }
        player.getStat().addSp((int) (requiredSp * 0.8));
        if (Config.LOG_SKILL_ENCHANTS) {
            LogRecord record = new LogRecord(Level.INFO, "Untrain");
            record.setParameters(new Object[] { player, skill, spb });
            record.setLoggerName("skill");
            _logEnchant.log(record);
        }
        player.addSkill(skill, true);
        player.sendPacket(ExEnchantSkillResult.valueOf(true));
        if (Config.DEBUG) {
            _log.fine("Learned skill ID: " + _skillId + " Level: " + _skillLvl + " for " + requiredSp + " SP, " + requireditems + " Adena.");
        }
        player.sendPacket(new UserInfo(player));
        player.sendPacket(new ExBrExtraUserInfo(player));
        if (_skillLvl > 100) {
            SystemMessage sm = new SystemMessage(SystemMessageId.UNTRAIN_SUCCESSFUL_SKILL_S1_ENCHANT_LEVEL_DECREASED_BY_ONE);
            sm.addSkillName(_skillId);
            player.sendPacket(sm);
        } else {
            SystemMessage sm = new SystemMessage(SystemMessageId.UNTRAIN_SUCCESSFUL_SKILL_S1_ENCHANT_LEVEL_RESETED);
            sm.addSkillName(_skillId);
            player.sendPacket(sm);
        }
        player.sendSkillList();
        player.sendPacket(new ExEnchantSkillInfo(_skillId, player.getSkillLevel(_skillId)));
        player.sendPacket(new ExEnchantSkillInfoDetail(2, _skillId, player.getSkillLevel(_skillId) - 1, player));
        this.updateSkillShortcuts(player);
    }

    private void updateSkillShortcuts(L2PcInstance player) {
        L2ShortCut[] allShortCuts = player.getAllShortCuts();
        for (L2ShortCut sc : allShortCuts) {
            if (sc.getId() == _skillId && sc.getType() == L2ShortCut.TYPE_SKILL) {
                L2ShortCut newsc = new L2ShortCut(sc.getSlot(), sc.getPage(), sc.getType(), sc.getId(), player.getSkillLevel(_skillId), 1);
                player.sendPacket(new ShortCutRegister(newsc));
                player.registerShortCut(newsc);
            }
        }
    }

    @Override
    public String getType() {
        return "[C] D0:33 RequestExEnchantSkillUntrain";
    }
}
