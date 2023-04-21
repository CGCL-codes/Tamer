package pdp.scrabble.game;

import java.io.Serializable;
import java.sql.Time;

public interface AILevel extends Serializable {

    public Time getTime();

    public void setTime(Time time);

    public int getNbSimulations();

    public void setNbSimulations(int nbSimulation);

    public String getName();

    public void setName(String name);

    public void setAdult(boolean adult);

    public void setAllowedChangingLetters(boolean changingLetters);

    public void setFreeJoker(boolean freeJoker);

    public void setRandomResearch(boolean randomResearch);

    public boolean isAdult();

    public boolean isAllowedChangingLetters();

    public boolean isfreeToUseJoker();

    public boolean isRandomResearch();

    public boolean sameLevel(AILevel ai);

    public boolean sameName(AILevel ai);
}
