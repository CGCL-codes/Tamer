package brickdoh.game;

/**
 *
 * @author Hj. Malthaner
 */
public interface ScoreCallback {

    public void raiseScore(int howmuch);

    public void clearScore();

    public void setSteady(int count);

    public void setHectic(int amount);

    public void setEasygo(int howmuch);
}
