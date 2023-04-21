package clubmixer.client.ui.utils;

import javax.swing.JLabel;
import com.slychief.clubmixer.commons.converter.TimeFormatter;
import com.slychief.clubmixer.server.library.entities.Song;

/**
 *
 * @author Alexander Schindler
 */
public class NextSongsLabel extends JLabel {

    private Song song;

    private JLabel length;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
        length.setText(TimeFormatter.convertMilliSecondsToTime(song.getDuration()));
        this.setText(song.getArtist() + " - " + song.getTitle());
    }

    public void setLabelLength(JLabel lbl) {
        this.length = lbl;
    }
}
