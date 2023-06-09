package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2Skill;

/**
 * This class builds a list of skills that L2BufferInstance will be able to cast. Info is directly taken from SQL (table buffer_skills) which should contain at least 4 fields: id (primary key), level, type, adena. "type" will allow administrators to separate and skills in different groups, specially useful to restrict the access to some groups to determined kind of players. Example: <br>
 * <br>
 * <li>Prophet and similar</li> <li>Songs</li> <li>Dances</li> <li>Newbie buffs</li> <li>Advanced buffs</li> <li>Vip buffs</li> <li>...</li> <br>
 * <font color="red"><b>IMPORTANT: type must not contain spaces</b></font> <br>
 * <br>
 * The whole info is stored in different FastList objects, one per each group. These lists contain a collection of L2Skills. Finally, these objects are stored in a new FastMap which has group as key and previous maps as values.<br>
 * <br>
 *
 * @author House
 */
public class BufferSkillsTable {

    private static BufferSkillsTable _instance = null;

    private static FastMap<String, FastList<L2Skill>> _bufferSkillsMap;

    private static FastList<String> _buffTypes;

    private static FastMap<Integer, Integer> _buffPrizes;

    private static FastMap<Integer, SkillInfo> _allSkills;

    private static Logger _log = Logger.getLogger(BufferSkillsTable.class.getName());

    private static final String SQL_LOAD_SKILLS = "SELECT * FROM `mods_buffer_skills`";

    BufferSkillsTable() {
        _bufferSkillsMap = new FastMap<String, FastList<L2Skill>>();
        _buffTypes = new FastList<String>();
        _buffPrizes = new FastMap<Integer, Integer>();
        _allSkills = new FastMap<Integer, SkillInfo>();
        load();
    }

    private void load() {
        Connection con = null;
        int id = 0;
        int level = 0;
        String type = "";
        int adena = 0;
        int count = 0;
        int typesCount = 0;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(SQL_LOAD_SKILLS);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                level = rs.getInt("level");
                type = rs.getString("skill_group");
                adena = rs.getInt("adena");
                _allSkills.put(id, new SkillInfo(level, type));
                _buffPrizes.put(id, adena);
                count++;
                if (!_buffTypes.contains(type)) {
                    _buffTypes.add(type);
                    typesCount++;
                }
            }
            statement.close();
            rs.close();
        } catch (Exception e) {
            _log.warning("Error while loading BufferSkillsTable at entry: " + "ID: " + id + ", Level: " + level + "Type: " + type);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
            _log.fine("BufferSkillsTable: Loaded " + count + " skills and " + typesCount + " types.");
        }
        for (String t : _buffTypes) _bufferSkillsMap.put(t, new FastList<L2Skill>());
        for (FastMap.Entry<Integer, SkillInfo> e = _allSkills.head(), end = _allSkills.tail(); (e = e.getNext()) != end; ) _bufferSkillsMap.get(e.getValue()._skillType).add(SkillTable.getInstance().getInfo(e.getKey(), e.getValue()._skillLevel));
    }

    /**
	 * This method returns a list of L2Skill objects whose type equals skillType
	 *
	 * @param skillType
	 * @return A list of L2Skill or null if skillType doesn't match.
	 */
    public FastList<L2Skill> getSkillsByType(String skillType) {
        return _bufferSkillsMap.get(skillType);
    }

    public FastList<String> getSkillsTypeList() {
        return _buffTypes;
    }

    public int getSkillLevelById(int id) {
        return _allSkills.get(id)._skillLevel;
    }

    public int getSkillFee(int id) {
        return _buffPrizes.get(id);
    }

    /**
	 * This will reload BufferSkillsTable info from DataBase
	 */
    public static void reload() {
        _instance = new BufferSkillsTable();
    }

    public static BufferSkillsTable getInstance() {
        if (_instance == null) _instance = new BufferSkillsTable();
        return _instance;
    }

    private class SkillInfo {

        protected int _skillLevel;

        protected String _skillType;

        protected SkillInfo(int level, String type) {
            _skillLevel = level;
            _skillType = type;
        }
    }
}
