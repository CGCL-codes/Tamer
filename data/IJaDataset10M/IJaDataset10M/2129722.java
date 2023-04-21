package org.rugby.online.impl;

import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.rugby.online.core.exception.RboException;
import org.rugby.online.core.humans.RboPlayer;
import org.rugby.online.core.humans.RboPlayerLevel;
import org.rugby.online.core.log.RboLogger;
import org.rugby.online.core.skills.RboSkill;
import org.rugby.online.core.skills.RboSkillType;
import org.rugby.online.core.utils.NameUtils;

public class DefaultPlayer extends DefaultHuman implements RboPlayer {

    /**
	 * Serial ID
	 */
    private static final long serialVersionUID = 4993099499734298644L;

    public static final int MINIMAL_HEIGHT = 165;

    public static final int MAXIMAL_HEIGHT = 210;

    public static final int MINIMAL_WEIGHT = 70;

    public static final int MAXIMAL_WEIGHT = 130;

    public static final short MINIMAL_AGE = 18;

    public static final short MAXIMAL_AGE = 40;

    public static final short N_SKILLS = 12;

    private int weight;

    private int height;

    private Map<String, RboSkill> skillMap;

    private RboPlayerLevel level;

    private Locale nationality;

    /**
	 * Constructor
	 * @param locale the nationality of the player
	 */
    public DefaultPlayer(Locale locale) {
        super();
        try {
            this.nationality = locale;
            this.setFirstName(NameUtils.getRandomFirstName(nationality));
            this.setName(NameUtils.getRandomLastName(nationality));
            this.setLevel(RboPlayerLevel.AMATEUR);
            this.height = (new Random()).nextInt(MAXIMAL_HEIGHT - MINIMAL_HEIGHT) + MINIMAL_HEIGHT;
            this.weight = (new Random()).nextInt(MAXIMAL_WEIGHT - MINIMAL_WEIGHT) + MINIMAL_WEIGHT;
            this.setAge((short) ((new Random()).nextInt(MAXIMAL_AGE - MINIMAL_AGE) + MINIMAL_AGE));
            this.initSkillMap();
        } catch (RboException e) {
            RboLogger.parse(e);
        }
    }

    /**
	 * @param level the level to set
	 */
    public final void setLevel(RboPlayerLevel level) {
        this.level = level;
    }

    @Override
    public final int getWeight() {
        return this.weight;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    @Override
    public final Map<String, RboSkill> getSkillMap() {
        return this.skillMap;
    }

    @Override
    public final RboPlayerLevel getLevel() {
        return this.level;
    }

    @Override
    public final Locale getNationality() {
        return this.nationality;
    }

    /**
	 * Initiate the map of skills
	 */
    private void initSkillMap() {
        this.skillMap = new ConcurrentHashMap<String, RboSkill>(N_SKILLS);
        for (RboSkillType skillType : RboSkillType.values()) {
            String skillName = skillType.toString();
            if (skillName.startsWith("PLAYER_")) {
                this.skillMap.put(skillName, new DefaultSkill(skillType));
            }
        }
    }
}
