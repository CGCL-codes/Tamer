package de.ueppste.ljb.server.model;

import java.io.*;
import java.util.logging.*;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.*;
import de.ueppste.ljb.server.Options;
import de.ueppste.ljb.share.Mp3;
import de.ueppste.ljb.share.Mp3Cover;

/** 	
 * Liesst aus den Mp3 Dateien die Id3 Tags aus und
 * gibt eine Mp3 Instanz mit den entsprechenden Attributen zurück
 * @author bernhard
 */
public class ModelId3 {

    /** Logger */
    private static Logger logger = Logger.getLogger("ljbs.GetId3");

    private static final String[] genres = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Brass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A Capela", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "SynthPop" };

    /**
	 * Konstruktor
	 */
    public ModelId3() {
        Logger loggerAudio = Logger.getLogger("org.jaudiotagger");
        loggerAudio.setLevel(Level.OFF);
    }

    /**
	 * Ließt die ID3-Tags aus einer Mp3-Datei aus
	 * und übergibt eine Mp3 Instanz
	 * @param strFile	Pfad der auszulesenden Mp3-Datei
	 * @return Mp3-Instanz
	 */
    public Mp3 getMp3(String strFile) {
        Mp3 mp3 = null;
        if (strFile.length() == 0) {
            logger.log(Level.WARNING, "No File given! Exiting ...");
        }
        try {
            File file = new File(strFile);
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();
            int length = audioHeader.getTrackLength();
            int bitrate = (int) audioHeader.getBitRateAsNumber();
            String artist = tag.getFirstArtist();
            String title = tag.getFirstTitle();
            String album = tag.getFirstAlbum();
            String genre = tag.getFirstGenre();
            try {
                int i = Integer.valueOf(genre);
                if (i >= 0 && i < genres.length) {
                    genre = genres[i];
                }
            } catch (NumberFormatException e) {
                if (genre.equals("")) genre = "Other";
            }
            if (Options.getNotWithoutId3()) {
                if (artist.length() > 0 && title.length() > 0 && album.length() > 0 && genre.length() > 0) {
                    mp3 = new Mp3(artist, title, album, genre, bitrate, length, strFile);
                }
            } else {
                mp3 = new Mp3(artist, title, album, genre, bitrate, length, strFile);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error beim Lesen von: " + strFile + " " + e.getMessage());
        }
        return mp3;
    }

    /**
	 * Setzt neue ID3 Tags in eine Mp3
	 * @param mp3
	 * @return etwas geändert?
	 */
    public boolean setMp3(Mp3 mp3) {
        boolean edit = false;
        try {
            File file = new File(mp3.getPath());
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            if (mp3.getAlbum() != null && !mp3.getAlbum().equals("")) {
                tag.setAlbum(mp3.getAlbum());
                edit = true;
            }
            if (mp3.getArtist() != null && !mp3.getArtist().equals("")) {
                tag.setArtist(mp3.getArtist());
                edit = true;
            }
            if (mp3.getGenre() != null && !mp3.getGenre().equals("")) {
                tag.setGenre(mp3.getGenre());
                edit = true;
            }
            if (mp3.getTitle() != null && !mp3.getTitle().equals("")) {
                tag.setTitle(mp3.getTitle());
                edit = true;
            }
            audioFile.commit();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error beim Schreiben von: " + mp3.getPath() + " " + e.getMessage());
            return false;
        }
        return edit;
    }

    /**
	 * Cover aus einer Mp3 auslesen
	 * @param strFile	Pfad zu einer Mp3 Datei
	 * @return	Cover der Mp3
	 */
    public Mp3Cover getCover(String strFile) {
        Mp3Cover cover = null;
        try {
            File file = new File(strFile);
            AudioFile audioFile = AudioFileIO.read(file);
            TagField imageField = audioFile.getTag().get(TagFieldKey.COVER_ART).get(0);
            if (imageField instanceof AbstractID3v2Frame) {
                FrameBodyAPIC imageFrameBody = (FrameBodyAPIC) ((AbstractID3v2Frame) imageField).getBody();
                if (!imageFrameBody.isImageUrl()) {
                    byte[] coverRaw = (byte[]) imageFrameBody.getObjectValue(DataTypes.OBJ_PICTURE_DATA);
                    cover = new Mp3Cover(coverRaw);
                }
            } else if (imageField instanceof MetadataBlockDataPicture) {
                byte[] coverRaw = (byte[]) ((MetadataBlockDataPicture) imageField).getImageData();
                cover = new Mp3Cover(coverRaw);
            }
        } catch (Exception e) {
            return null;
        }
        return cover;
    }
}
