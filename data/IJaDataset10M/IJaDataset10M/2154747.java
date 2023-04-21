package clubmixer.server.control;

import clubmixer.server.handler.audiodevice.AudiodeviceChangeEvent;
import clubmixer.server.handler.audiodevice.AudiodeviceChangeListener;
import clubmixer.server.player.CrossfadingPlayer;
import clubmixer.commons.plugins.player.IPlayer;
import clubmixer.commons.plugins.player.Playersettings;
import clubmixer.server.communication.StreamingServer;
import com.slychief.clubmixer.server.library.entities.Song;

/**
 *
 * @author Alexander Schindler
 */
public class PlayerController implements IPlayer, AudiodeviceChangeListener {

    private IPlayer activePlayer;

    private StreamingServer streamServ;

    /**
     * Constructs ...
     *
     */
    public PlayerController() {
        activePlayer = new CrossfadingPlayer();
        EventhandlerController.addListener(this);
    }

    /**
     * Method description
     *
     */
    public void play() {
        activePlayer.play();
    }

    /**
     * Method description
     *
     */
    public void pause() {
        activePlayer.pause();
    }

    /**
     * Method description
     *
     */
    public void stop() {
        activePlayer.stop();
    }

    /**
     * Method description
     *
     *
     * @param song
     */
    public void next(Song song) {
        activePlayer.next(song);
    }

    /**
     * Method description
     *
     *
     * @param interval
     */
    public void fastforward(int interval) {
        activePlayer.fastforward(interval);
    }

    /**
     * Method description
     *
     *
     * @param interval
     */
    public void fastrewind(int interval) {
        activePlayer.fastrewind(interval);
    }

    /**
     * Method description
     *
     *
     * @param se
     */
    public void onAudiodeviceChanged(AudiodeviceChangeEvent se) {
        activePlayer.setMixer(se.getDeviceName());
        activePlayer.stop();
        activePlayer.play();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public IPlayer getActivePlayer() {
        return activePlayer;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Song getCurrentSong() {
        return activePlayer.getCurrentSong();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isPlaying() {
        return activePlayer.isPlaying();
    }

    /**
     * Method description
     *
     *
     * @param activePlayer
     */
    public void setActivePlayer(IPlayer activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Method description
     *
     *
     * @param gain
     */
    public void setGain(float gain) {
        activePlayer.setGain(gain);
    }

    /**
     * Method description
     *
     *
     * @param mixerName
     */
    public void setMixer(String mixerName) {
        activePlayer.setMixer(mixerName);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Playersettings getPlayersettings() {
        return activePlayer.getPlayersettings();
    }

    public void enableStreaming() {
        streamServ = new StreamingServer();
        streamServ.startStreaming();
    }

    public void stopStreaming() {
        streamServ.stopStreaming();
        streamServ.close();
        streamServ = null;
    }
}
