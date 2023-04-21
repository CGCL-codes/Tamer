package tddc77.yahtzee;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tddc77.yahtzee.Score.ScoreType;

/**
 *
 * @author John Törnblom (johto012), Mikael Silvén (miksi016)
 */
public class Game {

    public static final String CURRENT_PLAYER_PROPERTY = "CurrentPlayer";

    public static final String PLAYER_PROPERTY = "Player";

    public static final String ROLLS_LEFT_PROPERTY = "RollsLeft";

    public static final String RUNNING_PROPERTY = "Running";

    public static final String DICE_PROPERTY = "Dice";

    public static final String POSSIBLE_SCORES_PROPERTY = "PossibleScores";

    public static final String WAITING_FOR_ACTION_EVENT = "WaitingForAction";

    public static final String EVENT_CHOOSE_SCORE = "ChooseScore";

    private PropertyChangeSupport prop;

    private ArrayList<Player> players;

    private Map<Player, List<Score>> unusedScores;

    private Player currentPlayer;

    private int rollsLeft;

    private boolean running;

    private Die[] dice;

    public Game() {
        currentPlayer = null;
        rollsLeft = 0;
        running = false;
        prop = new PropertyChangeSupport(this);
        players = new ArrayList<Player>();
        unusedScores = new HashMap<Player, List<Score>>();
        dice = new Die[0];
    }

    /**
     * Sets the dices in the Game to the new new die[] that is inputed. This
     * should happen only when the game starts.
     *
     * @param dice the new dices to be used.
     */
    private void setDice(Die[] dice) {
        Die[] oldValue = this.dice;
        this.dice = dice;
        prop.firePropertyChange(DICE_PROPERTY, oldValue, dice);
    }

    /**
     * Gets the die[] that is being used in the current game.
     *
     * @return Die[] the dies that are being used.
     */
    public Die[] getDice() {
        return dice;
    }

    /**
     * Returns the status of the game, true if it is running, false if it isn't.
     * The game starts running after a start, and stops when the game is 
     * finished (when all players fills their scoreboards).
     *
     * @return true/false is the game running or not?
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets if the game should continue to run.
     * Whenever this option changes, a RUNNING_PROPERTY change event is fired.
     *
     * @param running Should the game be running or not? (true/false)
     */
    private void setRunning(boolean running) {
        boolean oldValue = this.running;
        this.running = running;
        prop.firePropertyChange(RUNNING_PROPERTY, oldValue, running);
    }

    /**
     * Returns the player whose turns it is to role the dices.
     *
     * @return currentPlayer the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player to the new player that is being sent as a 
     * parameter. When the player changes, or if the turn changes, a
     * CURRENT_PLAYER_PROPERTY change is fired.
     *
     * @param currentPlayer the new current player.
     */
    private void setCurrentPlayer(Player currentPlayer) {
        Player oldValue = this.currentPlayer;
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(oldValue)) {
            oldValue = null;
        }
        prop.firePropertyChange(CURRENT_PLAYER_PROPERTY, oldValue, currentPlayer);
    }

    /**
     * Returns a Score[] with the possible scores left for the current player.
     *
     * @return a Score[] array with the scores left to put points in.
     */
    public Score[] getPossileScores() {
        List<Score> scores = unusedScores.get(getCurrentPlayer());
        Score[] ret = new Score[scores.size()];
        return scores.toArray(ret);
    }

    /**
     * Returns the number of rolls the current player has left to roll before
     * his turn has to end.
     *
     * @return rollsLeft the number of rolls the player has left this turn.
     */
    public int getRollsLeft() {
        return rollsLeft;
    }

    /**
     * Sets the numbers of rolls a player has left.
     * This is used with the parameter 3 whenever a new turn begins.
     *
     * @param rollsLeft the new number of rolls the player has left.
     */
    private void setRollsLeft(int rollsLeft) {
        int oldValue = this.rollsLeft;
        this.rollsLeft = rollsLeft;
        prop.firePropertyChange(ROLLS_LEFT_PROPERTY, oldValue, rollsLeft);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        prop.addPropertyChangeListener(l);
    }

    /**
     * Adds a player to the game if he does not exist already.
     * @param p the new player to be added.
     */
    public void addPlayer(Player p) {
        if (!players.contains(p) && !running) {
            players.add(p);
            unusedScores.put(p, ScoreFactory.createAllScores());
            prop.firePropertyChange(PLAYER_PROPERTY, null, p);
        }
    }

    /**
     * Returns a Player[] array with the current player in the game.
     *
     * @return player[] the players that are playing.
     */
    public Player[] getPlayers() {
        Player[] ret = new Player[players.size()];
        return players.toArray(ret);
    }

    /**
     * Starts the game.
     * The game cannot start without players or if the game has ended once
     * befoure.
     *
     * Start() creates new dices to use in the game, sets the rolls a player can
     * use to 3, sets the running variable to true and makes the first player
     * the current player.
     *
     */
    public void start() {
        if (players.size() == 0 || unusedScores.isEmpty()) {
            return;
        }
        setDice(new Die[] { new Die(), new Die(), new Die(), new Die(), new Die() });
        setRollsLeft(3);
        setRunning(true);
        setCurrentPlayer(players.get(0));
    }

    /**
     * Choses the scoreType (score category, ie. ones/twos/small straight, etc.)
     * the player wants to input into his scoreboard. The methods checks if
     * it's available to him (if it's in the unusedScores map) and puts into
     * into his usedScores list. Then it resets the dices if these actions was
     * successfull. Later it calculates if the player acheives the bonus score.
     * And finally it checks if this was the final round of the game and if it
     * was, it stops the game.
     *
     * @param scoreType The type of the score the players whises to put into his
     * score board.
     * @return true if the action was successfull, false if it wasnt.
     */
    public boolean chooseScoreType(ScoreType scoreType) {
        if (currentPlayer == null) {
            return false;
        }
        Score s = ScoreFactory.findScore(scoreType, unusedScores.get(currentPlayer));
        if (s == null) {
            return false;
        }
        unusedScores.get(currentPlayer).remove(s);
        currentPlayer.addScore(s);
        for (Die die : dice) {
            die.reset();
        }
        int leftForBonus = ScoreFactory.getLeftForBonus(currentPlayer.getScores());
        currentPlayer.setLeftForBonus(leftForBonus);
        if (leftForBonus == 0) {
            Score bonus = ScoreFactory.createBonusScore();
            bonus.setValue(dice);
            currentPlayer.addScore(bonus);
        }
        for (List<Score> l : unusedScores.values()) {
            if (!l.isEmpty()) {
                initNextPlayer();
                return true;
            }
        }
        Collections.sort(players);
        setRunning(false);
        unusedScores.clear();
        return false;
    }

    /**
     * nextRound() starts the next round of the game by re-roll the dices
     * and telling the player what possible scores the rolled dices may
     * give him.
     *
     * A POSSIBLE_SCORE_PROPERTY change and a WAITING_FOR_ACTION_EVENT is fired.
     *
     * @return If the nextRound was started okey
     */
    public boolean nextRound() {
        if (!isRunning() || getRollsLeft() <= 0) {
            return false;
        }
        List<Score> scoreList = unusedScores.get(currentPlayer);
        if (scoreList == null) {
            return false;
        }
        throwDice();
        Score[] scores = new Score[scoreList.size()];
        for (Score s : scoreList) {
            s.setValue(dice);
        }
        Collections.sort(scoreList);
        scores = scoreList.toArray(scores);
        prop.firePropertyChange(POSSIBLE_SCORES_PROPERTY, null, scores);
        prop.firePropertyChange(WAITING_FOR_ACTION_EVENT, null, EVENT_CHOOSE_SCORE);
        return true;
    }

    /**
     * Changes the currentplayer to the next one in the player list and resets
     * the number of rolls.
     */
    private void initNextPlayer() {
        int index = players.indexOf(currentPlayer);
        if (index >= players.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        setRollsLeft(3);
        setCurrentPlayer(players.get(index));
    }

    /**
     * throws all the dices and reduces the numbers of rolls left by 1
     *
     * @return true after a successfull reroll
     */
    private boolean throwDice() {
        for (Die die : dice) {
            die.roll();
        }
        setRollsLeft(getRollsLeft() - 1);
        return true;
    }
}
