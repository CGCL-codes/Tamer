package net.sf.l2j.gameserver.datatables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javolution.util.FastList;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.templates.L2BuffTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BuffTemplateTable {

    private static final Log _log = LogFactory.getLog(BuffTemplateTable.class.getName());

    private static BuffTemplateTable _instance;

    /** This table contains all the Buff Templates */
    private FastList<L2BuffTemplate> _buffs;

    public static BuffTemplateTable getInstance() {
        if (_instance == null) _instance = new BuffTemplateTable();
        return _instance;
    }

    /**
	 * Creates and charges all the Buff templates from the SQL Table buff_templates
	 */
    public BuffTemplateTable() {
        _buffs = new FastList<L2BuffTemplate>();
        ReloadBuffTemplates();
    }

    /**
	 * Reads and charges all the Buff templates from the SQL table
	 */
    public void ReloadBuffTemplates() {
        _buffs.clear();
        java.sql.Connection con = null;
        try {
            try {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT * FROM buff_templates ORDER BY id, skill_order");
                ResultSet rset = statement.executeQuery();
                int _buffTemplates = 0;
                int templateId = -1;
                while (rset.next()) {
                    StatsSet Buff = new StatsSet();
                    if (templateId != rset.getInt("id")) _buffTemplates++;
                    templateId = rset.getInt("id");
                    Buff.set("id", templateId);
                    Buff.set("name", rset.getString("name"));
                    Buff.set("skillId", rset.getInt("skill_id"));
                    Buff.set("skillLevel", rset.getInt("skill_level"));
                    Buff.set("skillOrder", rset.getInt("skill_order"));
                    Buff.set("forceCast", rset.getInt("skill_force"));
                    Buff.set("minLevel", rset.getInt("char_min_level"));
                    Buff.set("maxLevel", rset.getInt("char_max_level"));
                    Buff.set("race", rset.getInt("char_race"));
                    Buff.set("class", rset.getInt("char_class"));
                    Buff.set("faction", rset.getInt("char_faction"));
                    Buff.set("adena", rset.getInt("price_adena"));
                    Buff.set("points", rset.getInt("price_points"));
                    L2BuffTemplate template = new L2BuffTemplate(Buff);
                    if (template.getSkill() == null) _log.warn("Error while loading buff template Id " + template.getId() + " skill Id " + template.getSkillId()); else _buffs.add(template);
                }
                _log.info("BuffTemplateTable: Loaded " + _buffTemplates + " Buff Templates.");
                rset.close();
                statement.close();
            } catch (Exception e) {
                _log.warn("Error while loading buff templates " + e.getMessage());
            }
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * @return Returns the buffs of template by template Id
	 */
    public FastList<L2BuffTemplate> getBuffTemplate(int Id) {
        FastList<L2BuffTemplate> _templateBuffs = new FastList<L2BuffTemplate>();
        for (L2BuffTemplate _bt : _buffs) if (_bt.getId() == Id) _templateBuffs.add(_bt);
        return _templateBuffs;
    }

    /**
	 * @return Returns the template Id by template Name
	 */
    public int getTemplateIdByName(String _name) {
        int _id = 0;
        for (L2BuffTemplate _bt : _buffs) if (_bt.getName().equals(_name)) {
            _id = _bt.getId();
            break;
        }
        return _id;
    }

    /**
	 * @return Returns the lowest char level for Buff template
	 */
    public int getLowestLevel(int Id) {
        int _lowestLevel = 255;
        for (L2BuffTemplate _bt : _buffs) if (_bt.getId() == Id && _lowestLevel > _bt.getMinLevel()) _lowestLevel = _bt.getMinLevel();
        return _lowestLevel;
    }

    /**
	 * @return Returns the highest char level for Buff template
	 */
    public int getHighestLevel(int Id) {
        int _highestLevel = 0;
        for (L2BuffTemplate _bt : _buffs) if (_bt.getId() == Id && _highestLevel < _bt.getMaxLevel()) _highestLevel = _bt.getMaxLevel();
        return _highestLevel;
    }

    /**
	 * @return Returns the buff templates list
	 */
    public FastList<L2BuffTemplate> getBuffTemplateTable() {
        return _buffs;
    }
}
