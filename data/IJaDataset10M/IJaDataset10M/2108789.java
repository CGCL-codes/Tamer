package net.slashie.serf.level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LevelMetaData implements Serializable {

    private String levelID;

    private List<String> exits = new ArrayList<String>();

    private Hashtable<String, String> hexits = new Hashtable<String, String>();

    public LevelMetaData(String levelID) {
        this.levelID = levelID;
    }

    public List<String> getExits() {
        return exits;
    }

    public void addExits(String exit, String exitID) {
        exits.add(exit);
        hexits.put(exitID, exit);
    }

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
    }

    public String getExit(int number) {
        return (String) exits.get(number);
    }

    public String getExit(String exitID) {
        return (String) hexits.get(exitID);
    }
}
