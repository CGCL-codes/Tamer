package sound;

/**
 * @author Andi
 *
 */
public class SoundPlayer {

    private static SoundPlayer instance;

    private SoundPlayer() {
    }

    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }
        return instance;
    }

    public void intro() {
        play(ISound.INTRO);
    }

    public void button() {
        play(ISound.BUTTON);
    }

    public void button2() {
        play(ISound.BUTTON2);
    }

    public void hintergrund() {
        play(ISound.HINTERGRUND);
    }

    public void bWerfen() {
        play(ISound.B_WERFEN);
    }

    public void explosion1() {
        play(ISound.EXPLOSION1);
    }

    public void explosion2() {
        play(ISound.EXPLOSION1);
    }

    public void kasse() {
        play(ISound.KASSE);
    }

    public void powerUp() {
        play(ISound.POWER_UP);
    }

    public void verliert() {
        play(ISound.VERLIERT);
    }

    public void gewinnt() {
        play(ISound.GEWINNT);
    }

    public void outro() {
        play(ISound.OUTRO);
    }

    public void stirbt() {
        play(ISound.STIRBT);
    }

    /**
	 * @param sound
	 */
    private void play(String sound) {
        new Thread(new Sound(sound)).start();
    }

    /**
	 * 
	 */
    public void ready() {
        play(ISound.READY);
    }
}
