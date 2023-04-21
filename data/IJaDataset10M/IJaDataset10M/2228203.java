package org.jazzteam.gurudev.game;

import java.util.LinkedList;
import org.jazzteam.gurudev.model.Area;
import org.jazzteam.gurudev.model.SkillList;

/**
 * @author Noxt
 *
 */
public class Game {

    private int id;

    private SkillList skills;

    private LinkedList<Area> areas;

    public SkillList getSkills() {
        return skills;
    }

    public int getId() {
        return id;
    }

    public void setSkills(SkillList skills) {
        this.skills = skills;
    }

    public LinkedList<Area> getAreas() {
        return areas;
    }

    public void setAreas(LinkedList<Area> areas) {
        this.areas = areas;
    }
}
