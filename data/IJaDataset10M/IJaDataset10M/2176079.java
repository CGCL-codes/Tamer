package com.l2jserver.gameserver.skills;

import gnu.trove.TIntObjectHashMap;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.Item;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.templates.item.L2Armor;
import com.l2jserver.gameserver.templates.item.L2EtcItem;
import com.l2jserver.gameserver.templates.item.L2EtcItemType;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.templates.item.L2Weapon;
import javolution.util.FastList;

/**
 * @author mkizub
 */
public class SkillsEngine {

    protected static final Logger _log = Logger.getLogger(SkillsEngine.class.getName());

    private List<File> _armorFiles = new FastList<File>();

    private List<File> _weaponFiles = new FastList<File>();

    private List<File> _etcitemFiles = new FastList<File>();

    private List<File> _skillFiles = new FastList<File>();

    public static SkillsEngine getInstance() {
        return SingletonHolder._instance;
    }

    private SkillsEngine() {
        hashFiles("data/stats/etcitem", _etcitemFiles);
        hashFiles("data/stats/armor", _armorFiles);
        hashFiles("data/stats/weapon", _weaponFiles);
        hashFiles("data/stats/skills", _skillFiles);
    }

    private void hashFiles(String dirname, List<File> hash) {
        File dir = new File(Config.DATAPACK_ROOT, dirname);
        if (!dir.exists()) {
            _log.warning("Dir " + dir.getAbsolutePath() + " not exists");
            return;
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".xml") && !f.getName().startsWith("custom")) hash.add(f);
        }
        File customfile = new File(Config.DATAPACK_ROOT, dirname + "/custom.xml");
        if (customfile.exists()) hash.add(customfile);
    }

    public List<L2Skill> loadSkills(File file) {
        if (file == null) {
            _log.warning("Skill file not found.");
            return null;
        }
        DocumentSkill doc = new DocumentSkill(file);
        doc.parse();
        return doc.getSkills();
    }

    public void loadAllSkills(final TIntObjectHashMap<L2Skill> allSkills) {
        int count = 0;
        for (File file : _skillFiles) {
            List<L2Skill> s = loadSkills(file);
            if (s == null) continue;
            for (L2Skill skill : s) {
                allSkills.put(SkillTable.getSkillHashCode(skill), skill);
                count++;
            }
        }
        _log.info("SkillsEngine: Loaded " + count + " Skill templates from XML files.");
    }

    public List<L2Armor> loadArmors(Map<Integer, Item> armorData) {
        List<L2Armor> list = new FastList<L2Armor>();
        for (L2Item item : loadData(armorData, _armorFiles)) {
            list.add((L2Armor) item);
        }
        return list;
    }

    public List<L2Weapon> loadWeapons(Map<Integer, Item> weaponData) {
        List<L2Weapon> list = new FastList<L2Weapon>();
        for (L2Item item : loadData(weaponData, _weaponFiles)) {
            list.add((L2Weapon) item);
        }
        return list;
    }

    public List<L2EtcItem> loadItems(Map<Integer, Item> itemData) {
        List<L2EtcItem> list = new FastList<L2EtcItem>();
        List<Integer> xmlItem = new FastList<Integer>();
        for (L2Item item : loadData(itemData, _etcitemFiles)) {
            list.add((L2EtcItem) item);
            xmlItem.add(item.getItemId());
        }
        for (Item item : itemData.values()) {
            if (!xmlItem.contains(item.id)) list.add(new L2EtcItem((L2EtcItemType) item.type, item.set));
        }
        return list;
    }

    public List<L2Item> loadData(Map<Integer, Item> itemData, List<File> files) {
        List<L2Item> list = new FastList<L2Item>();
        for (File f : files) {
            DocumentItem document = new DocumentItem(itemData, f);
            document.parse();
            list.addAll(document.getItemList());
        }
        return list;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final SkillsEngine _instance = new SkillsEngine();
    }
}
