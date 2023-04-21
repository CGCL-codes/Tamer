package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 *
 */
class MediaPlayerTitleChangedEvent extends AbstractMediaPlayerEvent {

    /**
   * 
   */
    private final int newTitle;

    /**
   * Create a media player event.
   * 
   * @param mediaPlayer media player the event relates to
   * @param newTitle
   */
    MediaPlayerTitleChangedEvent(MediaPlayer mediaPlayer, int newTitle) {
        super(mediaPlayer);
        this.newTitle = newTitle;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.titleChanged(mediaPlayer, newTitle);
    }
}
