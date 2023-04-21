package net.sf.l2j.gameserver.model;

import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.AugmentationData;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.funcs.FuncAdd;
import net.sf.l2j.gameserver.skills.funcs.LambdaConst;

/**
 * Used to store an augmentation and its boni
 *
 * @author durgus
 */
public final class L2Augmentation {

    private static final Logger _log = Logger.getLogger(L2Augmentation.class.getName());

    private L2ItemInstance _item;

    private int _effectsId = 0;

    private augmentationStatBoni _boni = null;

    private L2Skill _skill = null;

    public L2Augmentation(L2ItemInstance item, int effects, L2Skill skill, boolean save) {
        _item = item;
        _effectsId = effects;
        _boni = new augmentationStatBoni(_effectsId);
        _skill = skill;
        if (save) saveAugmentationData();
    }

    public L2Augmentation(L2ItemInstance item, int effects, int skill, int skillLevel, boolean save) {
        this(item, effects, SkillTable.getInstance().getInfo(skill, skillLevel), save);
    }

    public class augmentationStatBoni {

        private Stats _stats[];

        private float _values[];

        private boolean _active;

        public augmentationStatBoni(int augmentationId) {
            _active = false;
            FastList<AugmentationData.AugStat> as = AugmentationData.getInstance().getAugStatsById(augmentationId);
            _stats = new Stats[as.size()];
            _values = new float[as.size()];
            int i = 0;
            for (AugmentationData.AugStat aStat : as) {
                _stats[i] = aStat.getStat();
                _values[i] = aStat.getValue();
                i++;
            }
        }

        public void applyBonus(L2PcInstance player) {
            if (_active) return;
            for (int i = 0; i < _stats.length; i++) ((L2Character) player).addStatFunc(new FuncAdd(_stats[i], 0x40, this, new LambdaConst(_values[i])));
            _active = true;
        }

        public void removeBonus(L2PcInstance player) {
            if (!_active) return;
            ((L2Character) player).removeStatsOwner(this);
            _active = false;
        }
    }

    private void saveAugmentationData() {
        java.sql.Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO augmentations (item_id,attributes,skill,level) VALUES (?,?,?,?)");
            statement.setInt(1, _item.getObjectId());
            statement.setInt(2, _effectsId);
            if (_skill != null) {
                statement.setInt(3, _skill.getId());
                statement.setInt(4, _skill.getLevel());
            } else {
                statement.setInt(3, 0);
                statement.setInt(4, 0);
            }
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "Could not save augmentation for item: " + _item.getObjectId() + " from DB:", e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void deleteAugmentationData() {
        if (!_item.isAugmented()) return;
        java.sql.Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM augmentations WHERE item_id=?");
            statement.setInt(1, _item.getObjectId());
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "Could not delete augmentation for item: " + _item.getObjectId() + " from DB:", e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * Get the augmentation "id" used in serverpackets.
	 *
	 * @return augmentationId
	 */
    public int getAugmentationId() {
        return _effectsId;
    }

    public L2Skill getSkill() {
        return _skill;
    }

    /**
	 * Applys the Bonus to the player.
	 *
	 * @param player
	 */
    public void applyBonus(L2PcInstance player) {
        _boni.applyBonus(player);
        if (_skill != null) {
            player.addSkill(_skill);
            player.sendSkillList();
        }
    }

    /**
	 * Removes the augmentation Bonus from the player.
	 *
	 * @param player
	 */
    public void removeBonus(L2PcInstance player) {
        _boni.removeBonus(player);
        if (_skill != null) {
            player.removeSkill(_skill);
            player.sendSkillList();
        }
    }
}
