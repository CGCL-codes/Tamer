package pl.edu.agh.iosr.ftpserverremote.data;

/**
 *
 * @author Tomasz Jadczyk
 */
public class StatisticsData extends ServerData {

    private int statID;

    private String statName;

    private String statValue;

    public int getStatID() {
        return statID;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getStatValue() {
        return statValue;
    }

    public void setStatValue(String statValue) {
        this.statValue = statValue;
    }
}
