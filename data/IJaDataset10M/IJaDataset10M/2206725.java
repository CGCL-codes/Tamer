package jbomberman.server;

import jbomberman.game.Board;
import jbomberman.game.Command;
import jbomberman.game.Level;
import jbomberman.game.RuleSet;
import jbomberman.game.Score;
import java.io.IOException;

public class Game {

    protected RuleSet ruleset_;

    protected Board board_;

    protected Level level_;

    protected Command[] player_Commands_;

    public Game(String ruleset, String level, PlayerInfo[] player_List) throws GameCreationException {
        try {
            ruleset = "jbomberman.game.ruleset." + ruleset + "RuleSet";
            ruleset_ = (RuleSet) Class.forName(ruleset).newInstance();
            level = "res/layouts/" + level + ".level";
            level_ = new Level(level);
            board_ = level_.getInstance();
            player_Commands_ = new Command[player_List.length];
            ruleset_.init(level_, board_, new byte[] { 0, 0, 0, 0 });
        } catch (Exception e) {
            throw new GameCreationException();
        }
    }

    public void simulatePlayerSteps(PlayerInfo[] player_List) {
        for (int index = 0; index < player_List.length; index++) {
            if (player_List[index] == null) {
                player_Commands_[index] = null;
            } else if (player_List[index] instanceof PlayerInfo) {
                player_Commands_[index] = player_List[index].getLastCommand();
            }
        }
        ruleset_.simulate(player_Commands_);
    }

    public byte[] getFrame(byte frame_Type) {
        try {
            switch(frame_Type) {
                case 0:
                    return board_.getAFrame();
                case 1:
                    return board_.getFFrame();
                case 2:
                    return board_.getIFrame();
                default:
                    return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isGameOver() {
        return ruleset_.isGameOver();
    }

    public Score getPlayerScore(int player_ID) {
        return ruleset_.getScore(player_ID);
    }
}
