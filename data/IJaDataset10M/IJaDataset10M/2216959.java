package mp3player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jd3lib.MetaData;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

/**
 * A class for getting information about local MP3 files.
 */
public class MP3File extends MP3 {

    private File file;

    private int frames;

    private float length;

    private float ms_per_frame;

    private String album, artist, comment, genre, title;

    private int track, year;

    /**
	 * Creates a new MP3 object for a file.
	 * @param f The file to read.
	 * @throws BitstreamException If the MP3 is broken.
	 */
    public MP3File(String f) throws BitstreamException, IOException {
        this(new File(f));
    }

    /**
	 * Creates a new MP3 object for a file.
	 * @param f The file to read.
	 * @throws BitstreamException If the MP3 is broken.
	 */
    public MP3File(File f) throws BitstreamException, IOException {
        file = f;
        FileInputStream in = new FileInputStream(f);
        Bitstream bits = new Bitstream(in);
        frames = 0;
        Header hdr;
        while ((hdr = bits.readFrame()) != null) {
            frames++;
            length += hdr.ms_per_frame();
            ms_per_frame = hdr.ms_per_frame();
            bits.closeFrame();
        }
        bits.close();
        in.close();
        org.jd3lib.MP3File mp3f = new org.jd3lib.MP3File(f);
        MetaData md = mp3f.getMetaData();
        if (md != null) {
            album = md.getAlbum();
            if (album != null && album.equals("")) album = null;
            artist = md.getArtist();
            if (artist != null && artist.equals("")) artist = null;
            comment = md.getComment();
            if (comment != null && comment.equals("")) comment = null;
            genre = md.getGenre();
            if (genre != null && genre.equals("")) genre = null;
            title = md.getTitle();
            if (title != null && title.equals("")) title = null;
            track = md.getTrack();
            if (track <= 0 || track == 32) track = -1;
            year = md.getYear();
            if (year <= 0) year = -1;
        } else {
            track = year = -1;
        }
    }

    /**
	 * Returns an input stream for the MP3 data.
	 * @throws IOException If an error occurs.
	 */
    public InputStream getStream() throws IOException {
        return new FileInputStream(file);
    }

    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        if (artist != null) {
            sb.append(artist);
            sb.append(" - ");
        }
        if (title != null) {
            sb.append(title);
        } else {
            sb.append(file.getName());
        }
        return sb.toString();
    }

    /**
	 * Returns the File object for this MP3 file.
	 */
    public File getFile() {
        return file;
    }

    /**
	 * Returns the amount of frames in the file.
	 */
    public int getFrames() {
        return frames;
    }

    /**
	 * Returns the total length of the MP3 in ms.
	 */
    public float getLength() {
        return length;
    }

    /**
	 * Returns <code>true</code> if the file is seekable.
	 */
    public boolean isSeekable() {
        return true;
    }

    public boolean isStream() {
        return false;
    }

    /**
	 * Returns the frame with the closest start time
	 * before the provided value.
	 * @param ms Time.
	 * @return Frame number.
	 */
    public int getClosestFrame(float ms) {
        return (int) Math.floor(ms / ms_per_frame);
    }

    /**
	 * Converts the frame index to time offset.
	 * @param frame Frame number.
	 * @return Time from the beginning of the song, in milliseconds.
	 */
    public float getFrameTime(int frame) {
        return frame * ms_per_frame;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getComment() {
        return comment;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public int getTrack() {
        return track;
    }

    public int getYear() {
        return year;
    }
}
