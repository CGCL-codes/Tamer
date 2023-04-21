package gameslave.dnd35.db;

import gameslave.d20.system.D20;
import gameslave.db.CompareNamedEntitiesByName;
import gameslave.db.NamedEntityInABookWithDescriptionAndScript;
import gameslave.util.CacheableScript;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 */
public class CharacterClass extends NamedEntityInABookWithDescriptionAndScript {

    String group;

    int hitDie;

    int skillPoints;

    int attackBonusAdvancement;

    int savesAdvancement[];

    Set favoredClassFor;

    Set classSkills;

    Set bonusFeats;

    Set bonusFeatLevels;

    SpellList spellList;

    int casterType;

    WeaponAndArmorProficiency weaponAndArmorProficiency = new WeaponAndArmorProficiency();

    final CacheableScript secondCacheableScript = new CacheableScript(this);

    public void setSecondScript(String script) {
        secondCacheableScript.setScript(script);
    }

    public String getSecondScript() {
        return secondCacheableScript.getScript();
    }

    public void setSecondScriptClassBytes(byte[][] classBytes) {
        secondCacheableScript.setScriptClassBytes(classBytes);
    }

    public byte[][] getSecondScriptClassBytes() {
        return secondCacheableScript.getScriptClassBytes();
    }

    public CacheableScript getSecondCacheableScript() {
        return secondCacheableScript;
    }

    public void initWithDefaults(D20 system) {
        weaponAndArmorProficiency.initWithDefaults();
        CompareNamedEntitiesByName compareNamedEntitiesByName = new CompareNamedEntitiesByName();
        favoredClassFor = new TreeSet(compareNamedEntitiesByName);
        savesAdvancement = new int[system.getSaveCount()];
        classSkills = new TreeSet(compareNamedEntitiesByName);
        bonusFeats = new TreeSet(compareNamedEntitiesByName);
        bonusFeatLevels = new TreeSet();
    }

    public int getAttackBonusAdvancement() {
        return attackBonusAdvancement;
    }

    public void setAttackBonusAdvancement(int baseAttackBonusAdvancement) {
        this.attackBonusAdvancement = baseAttackBonusAdvancement;
    }

    public Set getFavoredClassFor() {
        return favoredClassFor;
    }

    public void setFavoredClassFor(Set favoredClassFor) {
        this.favoredClassFor = favoredClassFor;
    }

    public int getHitDie() {
        return hitDie;
    }

    public void setHitDie(int hitDie) {
        this.hitDie = hitDie;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public SpellList getSpellList() {
        return spellList;
    }

    public void setSpellList(SpellList spellList) {
        this.spellList = spellList;
    }

    public Set getClassSkills() {
        return classSkills;
    }

    public void setClassSkills(Set classSkills) {
        this.classSkills = classSkills;
    }

    public int[] getSavesAdvancement() {
        return savesAdvancement;
    }

    public void setSavesAdvancement(int[] savesAdvancement) {
        this.savesAdvancement = savesAdvancement;
    }

    public int getSaveAdvancement(int saveNum) {
        return savesAdvancement[saveNum];
    }

    public WeaponAndArmorProficiency getWeaponAndArmorProficiency() {
        return weaponAndArmorProficiency;
    }

    public void setWeaponAndArmorProficiency(WeaponAndArmorProficiency weaponAndArmorProficiency) {
        this.weaponAndArmorProficiency = weaponAndArmorProficiency;
    }

    public Set getBonusFeats() {
        return bonusFeats;
    }

    public void setBonusFeats(Set bonusFeats) {
        this.bonusFeats = bonusFeats;
    }

    public Set getBonusFeatLevels() {
        return bonusFeatLevels;
    }

    public void setBonusFeatLevels(Set bonusFeatLevels) {
        this.bonusFeatLevels = bonusFeatLevels;
    }

    public int getCasterType() {
        return casterType;
    }

    public void setCasterType(int casterType) {
        this.casterType = casterType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
