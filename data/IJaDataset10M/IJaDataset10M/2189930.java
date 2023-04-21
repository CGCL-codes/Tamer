package MediaManager;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class AudioPlayer {

    private Player player;

    private String audioFile;

    public AudioPlayer() {
    }

    public AudioPlayer(String audioFile) {
        this.audioFile = audioFile;
    }

    /**
	* M�thode permettant de d�marrer la lecture
	* @throws Exception
	*/
    public void start() throws Exception {
        player = Manager.createPlayer(new MediaLocator(audioFile));
        player.realize();
    }

    public void play() {
        player.start();
    }

    /**
	* Stop playing
	*
	*/
    public void stop() {
        if (player != null) {
            javax.media.Time newTemp = new javax.media.Time((double) 0);
            player.setMediaTime(newTemp);
            player.stop();
        }
    }

    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer("file:/I:/music/Thousand_Miles.mp3");
        try {
            audioPlayer.start();
            char choice = 'c';
            while (choice == 'c') {
                System.out.println("Si vous souhaitez arr�ter, tapez 'q' : ");
                choice = (char) System.in.read();
                System.in.read();
            }
            audioPlayer.stop();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Impossible de lire (cause : " + e + ")");
        }
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public void avancer(float number) {
        if (player.getMediaTime().getSeconds() < player.getDuration().getSeconds()) {
            javax.media.Time newTemp = new javax.media.Time((double) (player.getMediaTime().getSeconds() + 1));
            player.setMediaTime(newTemp);
        }
    }

    public void reculer(float number) {
        if (player.getMediaTime().getSeconds() >= 1) {
            javax.media.Time newTemp = new javax.media.Time((double) (player.getMediaTime().getSeconds() - 1));
            player.setMediaTime(newTemp);
        }
    }

    public void pause() {
        player.stop();
    }

    public int getPosition() {
        return (int) (player.getMediaTime().getSeconds() * 100 / player.getDuration().getSeconds());
    }
}
