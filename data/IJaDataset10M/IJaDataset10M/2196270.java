package imp.gui;

/**
 *
 * @author keller
 */
public class LoopPlayer implements Runnable {

    private Playable playable;

    private double swingValue = 0.5;

    long msGap = 0;

    boolean play = true;

    public LoopPlayer(Playable playable, double swingValue, long msGap) {
        this.playable = playable;
        this.swingValue = swingValue;
        this.msGap = msGap;
        this.play = true;
    }

    public void setPlayable(Playable playable) {
        this.playable = playable;
        setPlaying(true);
    }

    public void setGap(long msGap) {
        this.msGap = msGap;
    }

    public void setPlaying(boolean play) {
        this.play = play;
        if (!play) {
            playable.stopPlaying();
        }
    }

    public void run() {
        while (true) {
            if (play) {
                playable.playMe(swingValue);
            }
            try {
                Thread.sleep(msGap);
            } catch (Exception e) {
            }
        }
    }
}
