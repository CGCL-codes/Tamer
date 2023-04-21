package pcgen.core;

import pcgen.core.character.CharacterSpell;
import pcgen.core.character.SpellInfo;
import pcgen.core.prereq.PrereqHandler;
import pcgen.core.spell.Spell;
import pcgen.util.DoubleKeyMap;
import pcgen.util.HashMapToList;
import pcgen.util.Logging;
import java.util.*;

/**
 * @author Tom Parker <thpr@sourceforge.net>
 *
 * Spell Support object for PObject (offloads code related to spell lists)
 */
public class SpellSupport implements Cloneable {

    private static final String PIPE = "|";

    private static final String SPELLCASTER = "SPELLCASTER";

    private static final String CLASSSPELLCASTER = "CLASS|SPELLCASTER";

    private static final String ALL = "ALL";

    private HashMap spellLevelMap = new HashMap();

    private DoubleKeyMap spellInfoMap = new DoubleKeyMap();

    private HashMapToList spellMap = new HashMapToList();

    private HashMap preReqSpellLevelMap = new HashMap();

    private List characterSpellList = null;

    public void clearSpellLevelMap() {
        spellLevelMap.clear();
    }

    public void putLevel(String tagType, String className, String spellName, String spellLevel) {
        spellLevelMap.put(tagType + "|" + className + "|" + spellName, spellLevel);
    }

    public boolean containsLevelFor(String tagType, String className, String spellName) {
        return spellLevelMap.containsKey(tagType + "|" + className + "|" + spellName);
    }

    public void putInfo(String tagType, String spellName, String className, String spellLevel) {
        spellInfoMap.put(tagType, spellName, new Info(className, Integer.parseInt(spellLevel)));
    }

    public class Info {

        public final String name;

        public final int level;

        public Info(String n, int l) {
            name = n;
            level = l;
        }
    }

    public void clearSpellInfoMap() {
        spellInfoMap.clear();
    }

    public boolean containsInfoFor(String string, String spellName) {
        return spellInfoMap.containsKey(string, spellName);
    }

    public Info getInfo(String string, String spellName) {
        return (Info) spellInfoMap.get(string, spellName);
    }

    public void addSpells(final int level, final List aSpellList) {
        final String aLevel = Integer.toString(level);
        for (Iterator it = aSpellList.iterator(); it.hasNext(); ) {
            Object spell = it.next();
            if (!spellMap.containsInList(aLevel, spell)) {
                spellMap.addToListFor(aLevel, spell);
            }
        }
    }

    public List getSpellList(int levelLimit) {
        boolean allSpells = levelLimit == -1;
        final ArrayList aList = new ArrayList();
        if (spellMap != null) {
            for (Iterator it = spellMap.getKeySet().iterator(); it.hasNext(); ) {
                Object o = it.next();
                if (allSpells || Integer.parseInt(o.toString()) <= levelLimit) {
                    aList.addAll(spellMap.getListFor(o));
                }
            }
        }
        return aList;
    }

    /**
	 * Retrieve the list of spells registered for the specific level.
	 * 
	 * @param level The level to be retrieved.
	 * @return A List of the level's spells  
	 */
    public List getSpellListForLevel(int level) {
        final ArrayList aList = new ArrayList();
        if (spellMap != null) {
            for (Iterator it = spellMap.getKeySet().iterator(); it.hasNext(); ) {
                Object o = it.next();
                if (Integer.parseInt(o.toString()) == level) {
                    aList.addAll(spellMap.getListFor(o));
                }
            }
        }
        return aList;
    }

    public final void clearSpellList() {
        spellMap = new HashMapToList();
    }

    public void addSpellLevel(final String line) {
        final StringTokenizer aTok = new StringTokenizer(line, "|");
        if (aTok.countTokens() < 3) {
            Logging.errorPrint("Badly formed SPELLLEVEL tag1: " + line);
            return;
        }
        final String tagType = aTok.nextToken();
        final List preList = new ArrayList();
        final List wNameList = new ArrayList();
        final List wSpellList = new ArrayList();
        while (aTok.hasMoreTokens()) {
            final String nameList = aTok.nextToken();
            if (nameList.startsWith("PRE") || nameList.startsWith("!PRE")) {
                preList.add(nameList);
                break;
            }
            if (nameList.indexOf("=") < 0) {
                Logging.errorPrint("Badly formed SPELLLEVEL tag2: " + line);
                return;
            }
            wNameList.add(nameList);
            if (!aTok.hasMoreTokens()) {
                Logging.errorPrint("Badly formed SPELLLEVEL tag3: " + line);
                return;
            }
            wSpellList.add(aTok.nextToken());
        }
        while (aTok.hasMoreTokens()) {
            final String nameList = aTok.nextToken();
            if (nameList.startsWith("PRE") || nameList.startsWith("!PRE")) {
                preList.add(nameList);
            } else {
                Logging.errorPrint("Badly formed SPELLLEVEL PRE tag: " + line);
                return;
            }
        }
        for (Iterator iSpell = wSpellList.iterator(), iName = wNameList.iterator(); iSpell.hasNext() || iName.hasNext(); ) {
            if (!(iSpell.hasNext() && iName.hasNext())) {
                Logging.errorPrint("Badly formed SPELLLEVEL tag4: " + line);
                return;
            }
            final StringTokenizer bTok = new StringTokenizer((String) iSpell.next(), ",");
            final String classList = (String) iName.next();
            while (bTok.hasMoreTokens()) {
                final String spellLevel = classList.substring(classList.indexOf("=") + 1);
                final String spellName = bTok.nextToken();
                final StringTokenizer cTok = new StringTokenizer(classList.substring(0, classList.indexOf("=")), ",");
                while (cTok.hasMoreTokens()) {
                    final String className = cTok.nextToken();
                    if (className.startsWith("SPELLCASTER.") || !containsLevelFor(tagType, className, spellName)) {
                        preReqSpellLevelMap.put(tagType + "|" + className + "|" + spellName, preList);
                        putLevel(tagType, className, spellName, spellLevel);
                        putInfo(tagType, spellName, className, spellLevel);
                    }
                }
            }
        }
    }

    public Map getSpellMapPassesPrereqs(int levelMatch, PlayerCharacter pc) {
        final Map tempMap = new HashMap();
        for (Iterator sm = spellLevelMap.keySet().iterator(); sm.hasNext(); ) {
            final String key = sm.next().toString();
            int levelInt = -1;
            try {
                levelInt = Integer.parseInt((String) spellLevelMap.get(key));
            } catch (NumberFormatException nfe) {
            }
            if (((levelMatch == -1) && (levelInt >= 0)) || ((levelMatch >= 0) && (levelInt == levelMatch))) {
                if (preReqSpellLevelMap.containsKey(key)) {
                    if (key.startsWith(CLASSSPELLCASTER)) {
                        String spellType = key.substring(18);
                        spellType = spellType.substring(0, spellType.indexOf(PIPE));
                        if (ALL.equals(spellType) || pc.isSpellCaster(spellType, 1)) {
                            if (PrereqHandler.passesAll((List) preReqSpellLevelMap.get(key), pc, null)) {
                                for (Iterator iClass = pc.getClassList().iterator(); iClass.hasNext(); ) {
                                    final PCClass aClass = (PCClass) iClass.next();
                                    if (aClass.getSpellType().equals(spellType) || ALL.equals(spellType)) {
                                        StringBuffer tempSb = new StringBuffer();
                                        tempSb.append(aClass.getSpellKey()).append(PIPE).append(key.substring(key.lastIndexOf(PIPE) + 1));
                                        tempMap.put(tempSb.toString(), Integer.toString(levelInt));
                                    }
                                }
                            }
                        }
                    } else if (PrereqHandler.passesAll((List) preReqSpellLevelMap.get(key), pc, null)) {
                        tempMap.put(key, Integer.toString(levelInt));
                    }
                }
            }
        }
        return tempMap;
    }

    public Map getSpellInfoMapPassesPrereqs(String key1, String key2, PlayerCharacter pc) {
        final Map tempMap = new HashMap();
        if (spellInfoMap.containsKey(key1, key2)) {
            Info si = (Info) spellInfoMap.get(key1, key2);
            StringBuffer keysb = new StringBuffer();
            keysb.append(key1).append(PIPE).append(si.name).append(PIPE).append(key2);
            String key = keysb.toString();
            if (preReqSpellLevelMap.containsKey(key)) {
                if (si.name.startsWith(SPELLCASTER)) {
                    final String spellType = si.name.substring(12);
                    if (ALL.equals(spellType) || pc.isSpellCaster(spellType, 1)) {
                        if (PrereqHandler.passesAll((List) preReqSpellLevelMap.get(key), pc, null)) {
                            for (Iterator iClass = pc.getClassList().iterator(); iClass.hasNext(); ) {
                                final PCClass aClass = (PCClass) iClass.next();
                                if (aClass.getSpellType().equals(spellType) || ALL.equals(spellType)) {
                                    tempMap.put(aClass.getSpellKey(), new Integer(si.level));
                                }
                            }
                        }
                    }
                } else if (PrereqHandler.passesAll((List) preReqSpellLevelMap.get(key), pc, null)) {
                    StringBuffer tempSb = new StringBuffer();
                    tempSb.append(key1).append(PIPE).append(si.name);
                    tempMap.put(tempSb.toString(), new Integer(si.level));
                }
            }
        }
        return tempMap;
    }

    final boolean removeCharacterSpell(final CharacterSpell spell) {
        if (characterSpellList == null) {
            return false;
        }
        return characterSpellList.remove(spell);
    }

    final void clearCharacterSpells() {
        if ((characterSpellList != null) && !characterSpellList.isEmpty()) {
            characterSpellList.clear();
        }
    }

    public final void sortCharacterSpellList() {
        if (characterSpellList != null) {
            Collections.sort(characterSpellList);
        }
    }

    public final void addAllCharacterSpells(final List l) {
        if (characterSpellList == null) {
            characterSpellList = new ArrayList();
        }
        characterSpellList.addAll(l);
    }

    public final void addCharacterSpell(final CharacterSpell spell) {
        if (characterSpellList == null) {
            characterSpellList = new ArrayList();
        }
        characterSpellList.add(spell);
    }

    public final boolean containsCharacterSpell(final CharacterSpell spell) {
        return characterSpellList != null && characterSpellList.contains(spell);
    }

    public final int getCharacterSpellCount() {
        if (characterSpellList == null) {
            return 0;
        }
        return characterSpellList.size();
    }

    public final CharacterSpell getCharacterSpellForSpell(final Spell aSpell, final PObject anOwner) {
        if ((aSpell == null) || (characterSpellList == null)) {
            return null;
        }
        for (Iterator i = characterSpellList.iterator(); i.hasNext(); ) {
            final CharacterSpell cs = (CharacterSpell) i.next();
            final Spell bSpell = cs.getSpell();
            if (aSpell.equals(bSpell) && ((anOwner == null) || cs.getOwner().equals(anOwner))) {
                return cs;
            }
        }
        return null;
    }

    public final List getCharacterSpell(final Spell aSpell, final String book, final int level, final ArrayList fList) {
        final ArrayList aList = new ArrayList();
        if (getCharacterSpellCount() == 0) {
            return aList;
        }
        for (Iterator i = characterSpellList.iterator(); i.hasNext(); ) {
            final CharacterSpell cs = (CharacterSpell) i.next();
            if ((aSpell == null) || cs.getSpell().equals(aSpell)) {
                final SpellInfo si = cs.getSpellInfoFor(book, level, -1, fList);
                if (si != null) {
                    aList.add(cs);
                }
            }
        }
        return aList;
    }

    /**
	 * return an ArrayList of CharacterSpell with following criteria: Spell
	 * aSpell ignored if null book ignored if "" level ignored if < 0 fList
	 * (ignored if null) Array of Feats
	 * @param aSpell
	 * @param book
	 * @param level
	 * @return List
	 */
    public final List getCharacterSpell(final Spell aSpell, final String book, final int level) {
        return getCharacterSpell(aSpell, book, level, null);
    }

    public Collection getCharacterSpellList() {
        if (characterSpellList == null) {
            return new ArrayList();
        }
        return new ArrayList(characterSpellList);
    }

    public Object clone() throws CloneNotSupportedException {
        SpellSupport ss = (SpellSupport) super.clone();
        ss.spellInfoMap = (DoubleKeyMap) spellInfoMap.clone();
        ss.spellMap = new HashMapToList();
        ss.spellMap.addAllLists(spellMap);
        if (characterSpellList != null) {
            ss.characterSpellList = new ArrayList(characterSpellList);
        }
        ss.preReqSpellLevelMap = new HashMap(preReqSpellLevelMap);
        ss.spellLevelMap = new HashMap(spellLevelMap);
        return ss;
    }
}
