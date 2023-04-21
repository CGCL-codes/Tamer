package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 *
 */
class MediaPlayerBufferingEvent extends AbstractMediaPlayerEvent {

    /**
   * Create a media player event.
   * 
   * @param mediaPlayer media player the event relates to
   */
    MediaPlayerBufferingEvent(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.buffering(mediaPlayer);
    }
}
