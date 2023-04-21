package de.gee.erep.shared.entities.citizen.details;

import de.gee.erep.shared.entities.citizen.CitizenDetail;
import de.gee.erep.shared.entities.citizen.CitizenDetailId;

public class MilitarySkill implements CitizenDetail {

    @Override
    public String toString() {
        return "MilitarySkill [id=" + id + ", points=" + points + ", level=" + level + ", name=" + name + "]";
    }

    /***/
    private static final long serialVersionUID = 1L;

    public static final String[] fieldnames = { "points", "level", "name" };

    CitizenDetailId id;

    double points;

    String level;

    String name;

    public MilitarySkill() {
    }

    public MilitarySkill(CitizenDetailId id) {
        this.id = id;
    }

    public MilitarySkill(CitizenDetailId id, int points, String level, String name) {
        this.id = id;
        this.points = points;
        this.level = level;
        this.name = name;
    }

    @Override
    public CitizenDetailId getId() {
        return id;
    }

    /**
	 * @param element
	 * @param data
	 */
    public void set(String element, String data) {
        try {
            if (element.equals("points")) {
                points = Double.valueOf(data.equals("") ? "0.0" : data);
            } else if (element.equals("level")) {
                level = data;
            } else {
                name = data;
            }
        } catch (NumberFormatException e) {
            System.err.println("idNotSet");
        }
    }
}
