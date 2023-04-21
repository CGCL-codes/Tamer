package hokutonorogue.character;

import hokutonorogue.game.MainGame;
import hokutonorogue.level.Log;
import hokutonorogue.level.LogMessage;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class SkillLevelExperience extends Experience {

    protected Skill skill = null;

    public SkillLevelExperience(int maxLevel, Skill skill) {
        super(maxLevel);
        this.skill = skill;
    }

    public long calculateXpForLevel(int level) {
        return (long) Math.pow(level, 2);
    }

    @Override
    public void addXp(long xpValue) {
        if (level < maxLevel) {
            xp += xpValue;
            while (xp >= xpToNextLevel) {
                setLevel(level + 1);
                LogMessage message = new LogMessage(skill.getName().toUpperCase() + " SKILL LEVEL UP!");
                message.setType(LogMessage.POSITIVE);
                Log.getInstance().addMessage(message);
                MainGame.getInstance().playSound("resources/sound/ding.wav");
            }
        }
    }
}
