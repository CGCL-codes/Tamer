package server.finish;

import objects.Galaxy;
import objects.Race;
import java.util.Properties;
import java.util.Set;

public class AllianceVictory implements IGameFinisher {

    private double ratio;

    @Override
    public void init(Properties props) {
        ratio = Double.parseDouble(props.getProperty("Finish.AllianceVictory.Votes"));
    }

    @Override
    public boolean checkGameEnd(Galaxy galaxy) {
        double limit = galaxy.totalVotes() * ratio;
        for (Race race : galaxy.getRaces()) {
            Set<Race> alliance = Galaxy.getAlliance(race);
            if (alliance != null && Galaxy.allianceVotes(alliance) > limit) {
                galaxy.setWinners(alliance);
                return true;
            }
        }
        return false;
    }
}
