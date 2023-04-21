package messages;

import java.util.ArrayList;
import logic.Team;

public class MatchWinner implements GameMessage {

    private Team winner;

    private ArrayList<Integer[]> lastTrajectory;

    public MatchWinner(Team winner, ArrayList<Integer[]> lastTrajectory) {
        this.winner = winner;
        this.lastTrajectory = lastTrajectory;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void setMessage(String message) {
    }

    public Team getWinner() {
        return winner;
    }

    public ArrayList<Integer[]> getLastTrajectory() {
        return lastTrajectory;
    }
}
