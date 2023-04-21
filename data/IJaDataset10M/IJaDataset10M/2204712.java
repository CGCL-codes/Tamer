package stack.era.domain.character;

import java.util.ArrayList;
import stack.era.constants.CharacterAttributes;
import stack.era.constants.CharacterAttributes.Skill;
import stack.era.constants.CharacterAttributes.Stat;
import stack.era.constants.CrimeAttributes.CrimeType;
import stack.era.domain.crime.Crime;

public class PlayerCharacter extends Character {

    private static final long serialVersionUID = -1068503928577630054L;

    public PlayerCharacter(String newName) {
        super(newName);
        this.statPoints = CharacterAttributes.STARTING_STAT_POINTS;
        this.skillPoints = CharacterAttributes.STARTING_SKILL_POINTS;
        for (Stat s : Stat.values()) {
            baseStats.put(s, 0);
            pointsAddedToStats.put(s, 1);
        }
        for (Skill s : Skill.values()) {
            baseSkills.put(s, 0);
            pointsAddedToSkills.put(s, 0);
        }
        for (CrimeType ct : CrimeType.values()) {
            crimesCommitted.put(ct, new ArrayList<Crime>());
        }
        knownTowns.put("Town1", null);
        knownTowns.put("Town2", null);
        knownTowns.put("Town3", null);
    }

    @Override
    protected void levelUp() {
        this.level++;
    }

    @Override
    public void update() {
        for (Stat s : Stat.values()) {
            int baseAmount = (int) Math.ceil(((double) this.getStat(s) / 2d));
            switch(s) {
                case AGILITY:
                    this.baseSkills.put(Skill.HORSEBACK, baseAmount);
                    this.baseSkills.put(Skill.HUNTING, baseAmount);
                    break;
                case CHARISMA:
                    this.baseSkills.put(Skill.POLITICS, baseAmount);
                    this.baseSkills.put(Skill.BARTER, baseAmount);
                    this.baseSkills.put(Skill.HOSPITALITY, baseAmount);
                    break;
                case DEXTERITY:
                    this.baseSkills.put(Skill.CRAFT, baseAmount);
                    this.baseSkills.put(Skill.FISHING, baseAmount);
                    break;
                case ENDURANCE:
                    this.baseSkills.put(Skill.FARMING, baseAmount);
                    this.baseSkills.put(Skill.WOODCUTTING, baseAmount);
                    break;
                case INTELLIGENCE:
                    this.baseSkills.put(Skill.ECONOMICS, baseAmount);
                    this.baseSkills.put(Skill.MEDICINE, baseAmount);
                    break;
                case PERCEPTION:
                    this.baseSkills.put(Skill.MINING, baseAmount);
                    this.baseSkills.put(Skill.NAVIGATION, baseAmount);
                    break;
                case STRENGTH:
                    this.baseSkills.put(Skill.SMITHING, baseAmount);
                    this.baseSkills.put(Skill.CONSTRUCTION, baseAmount);
                    break;
            }
        }
    }
}
