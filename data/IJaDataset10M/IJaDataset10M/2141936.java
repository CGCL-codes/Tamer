package name.nanek.gdwprototype.client.controller.support;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Plays sounds.
 * 
 * @author Lance Nanek
 *
 */
public class SoundPlayer {

    private ClickHandler menuButtonClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            playMenuScreenButtonSound();
        }
    };

    private final SoundController soundController = new SoundController();

    private final Music menuScreenMusic = new Music(soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/menu_screen_music.mp3"), true);

    private final Music inGameMusic = new Music(soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/in-game_music.mp3"), true);

    private final Music winGameMusic = new Music(soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/win_game_music.mp3"), false);

    private final Music loseGameMusic = new Music(soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/lose_game_music.mp3"), false);

    private final Sound inGameErrorSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/in-game_error_sound.mp3");

    private final Sound piecePlacementSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/piece_placement_sound.mp3");

    private final Sound menuScreenButtonSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/menu_screen_button_sound.mp3");

    private final Sound yourTurnSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/your_turn_sound.mp3");

    private final Sound carrotSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/carrot_sound.mp3");

    private final Sound dyingSound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "sound/dying_sound.mp3");

    public void playMenuScreenMusic() {
        inGameMusic.stop();
        loseGameMusic.stop();
        winGameMusic.stop();
        menuScreenMusic.start();
    }

    public void playInGameMusic() {
        menuScreenMusic.stop();
        loseGameMusic.stop();
        winGameMusic.stop();
        inGameMusic.start();
    }

    public void playWinGameMusic() {
        menuScreenMusic.stop();
        loseGameMusic.stop();
        inGameMusic.stop();
        winGameMusic.start();
    }

    public void playLoseGameMusic() {
        menuScreenMusic.stop();
        inGameMusic.stop();
        winGameMusic.stop();
        loseGameMusic.start();
    }

    public void playInGameErrorSound() {
        inGameErrorSound.play();
    }

    public void playPiecePlacementSound() {
        piecePlacementSound.play();
    }

    public void playMenuScreenButtonSound() {
        menuScreenButtonSound.play();
    }

    public void playYourTurnSound() {
        yourTurnSound.play();
    }

    public void playCarrotSound() {
        carrotSound.play();
    }

    public void playDyingSound() {
        dyingSound.play();
    }

    public void playPickupPiceSound() {
        menuScreenButtonSound.play();
    }

    public void addMenuClick(HasClickHandlers... clickables) {
        if (null == clickables) {
            return;
        }
        for (HasClickHandlers clickable : clickables) {
            clickable.addClickHandler(menuButtonClickHandler);
        }
    }
}
