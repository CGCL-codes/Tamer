package org.drftpd.id3;

import org.apache.log4j.Logger;

/**
 * @author Jamal
 * @version $Id: ID3GenreList.java 1621 2007-02-13 20:41:31Z djb61 $
 */
public class ID3GenreList {

    public static final String[] genres = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "Alternative Rock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native US", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhytmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "Acapella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary C", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "SynthPop" };

    private static final Logger logger = Logger.getLogger(ID3GenreList.class);

    /**
	 * Returns the Genre for given number
	 * 
	 * @return Genre as String
	 */
    public static String getGenre(int number) {
        int index = number & 0xff;
        try {
            return genres[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.warn("Unknown genre number: " + index);
            return "";
        }
    }

    /**
	 * Checks, if the given genre is a valid one
	 * 
	 * @return boolean
	 */
    public static boolean validateGenre(String genre) {
        if (getGenreIndex(genre) != -1) {
            return true;
        }
        return false;
    }

    /**
	 * Returns the array index of this genre, or -1 if it is an invalid one.
	 * 
	 * @return int array index
	 */
    public static int getGenreIndex(String genre) {
        int index = -1;
        for (int i = 0; i < genres.length; i++) {
            if (genre.equals(genres[i])) {
                index = i;
            }
        }
        return index;
    }
}
