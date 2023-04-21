package game.database.race.vo;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Race {

    private static final long serialVersionUID = 1L;

    private int id;

    private String race;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + " - Name: " + getRace();
    }
}
