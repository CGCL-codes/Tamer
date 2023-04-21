package jwbroek.cuelib.tools.genrenormalizer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GenreNormalizer {

    /**
   * Logger for this class.
   */
    private static final Logger logger = Logger.getLogger(GenreNormalizer.class.getCanonicalName());

    /**
   * Enum for specifying various search modes. 
   */
    public static enum SearchMode {

        STRICT, NORMAL, HEURISTIC
    }

    /**
   * All supported genres, indexed as per ID3v1.
   */
    private static final String[] genres = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "Alternative Rock", "Bass", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native US", "Cabaret", "New Wave", "Psychedelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "Acappella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "SynthPop" };

    /**
   * First genre index that is an extension of WinAmp (added to ID3v1). Next follows a block of
   * such genres. Everything before is original ID3v1.
   */
    private static int firstWinAmpExtensionIndex = 80;

    /**
   * First genre index that is an extension of LAME. Next follows a block of such genres. The
   * indices directly before this are a block of WinAmp extensions (added to ID3v1).
   */
    private static int firstLameExtensionIndex = 126;

    /**
   * Maps the uppercase genre to its ID3 index.
   */
    private static Map<String, Integer> genreSignatureToID3Index = new HashMap<String, Integer>();

    static {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "static block");
        for (int index = 0; index < GenreNormalizer.genres.length; index++) {
            GenreNormalizer.genreSignatureToID3Index.put(GenreNormalizer.getGenreSignature(GenreNormalizer.genres[index]), index);
        }
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "static block");
    }

    /**
   * Create a new GenreNormalizer. Should never be called.
   */
    private GenreNormalizer() {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "GenreNormalizer()");
        GenreNormalizer.logger.warning(GenreNormalizer.class.getCanonicalName() + " should not be instantiated.");
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "GenreNormalizer()");
    }

    /**
   * <p>Get a signature of the genre. Two genres with the same signature are considered identical in the
   * normal search mode.</p>
   * <p>The current implementation simply returns the genre string with all letters turned to uppercase,
   * and all other characters removed.</p>
   * @param genre
   * @return The signature of the genre.
   */
    private static String getGenreSignature(final String genre) {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "getGenreSignature(String)");
        StringBuilder builder = new StringBuilder(genre.length());
        for (int index = 0; index < genre.length(); index++) {
            char currentChar = genre.charAt(index);
            if (Character.isLetter(currentChar)) {
                builder.append(Character.toUpperCase(currentChar));
            }
        }
        String result = builder.toString();
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "getGenreSignature(String)", result);
        return result;
    }

    /**
   * Get the code for the specified genre. This is a number as per ID3v1. If no matching code can be found, then
   * -1 is returned. 
   * @param genreDescription The genre description.
   * @param allowWinAmpExtensions Allow the WinAmp genre extensions. These were later incorporated in ID3v1.
   * @param allowLameExtensions Allow the LAME genre extensions. These are not part of ID3v1.
   * @param searchMode How to search for a matching code. STRICT requires the genre description to be exactly as
   * specified; NORMAL will tolerate differences in case and characters other than letters; HEURISTIC will try to
   * find an appropriate code if the NORMAL method fails.
   * @return The code for the specified genre, or -1 if no matching code can be found.
   */
    public static int getGenreCode(final String genreDescription, final boolean allowWinAmpExtensions, final boolean allowLameExtensions, final SearchMode searchMode) {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "getGenreCode(String,boolean,boolean,SearchMode)");
        int result = -1;
        final String inputGenreSignature = getGenreSignature(genreDescription);
        final Integer index = GenreNormalizer.genreSignatureToID3Index.get(inputGenreSignature);
        if (index == null) {
            if (searchMode == SearchMode.HEURISTIC) {
                final int maxGenre;
                if (!allowWinAmpExtensions) {
                    maxGenre = GenreNormalizer.firstWinAmpExtensionIndex - 1;
                } else if (!allowLameExtensions) {
                    maxGenre = GenreNormalizer.firstLameExtensionIndex - 1;
                } else {
                    maxGenre = GenreNormalizer.genres.length - 1;
                }
                for (int heuristicGenreIndex = maxGenre; heuristicGenreIndex >= 0 && result == -1; heuristicGenreIndex--) {
                    if (inputGenreSignature.contains(getGenreSignature(GenreNormalizer.genres[heuristicGenreIndex])) || getGenreSignature(GenreNormalizer.genres[heuristicGenreIndex]).contains(inputGenreSignature)) {
                        result = heuristicGenreIndex;
                    }
                }
            }
        } else {
            result = index;
            if (searchMode == SearchMode.STRICT && !genreDescription.equals(GenreNormalizer.genres[index])) {
                result = -1;
            }
            if (!allowWinAmpExtensions && result >= GenreNormalizer.firstWinAmpExtensionIndex) {
                result = -1;
            }
            if (!allowLameExtensions && result >= GenreNormalizer.firstLameExtensionIndex) {
                result = -1;
            }
        }
        if (result == -1 && searchMode == SearchMode.HEURISTIC) {
            result = 12;
        }
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "getGenreCode(String,boolean,boolean,SearchMode)", result);
        return result;
    }

    /**
   * Get the description for the specified genre. This is a string as per ID3v1. If no matching description can be
   * found, then null is returned. 
   * @param genreCode The genre code.
   * @param allowWinAmpExtensions Allow the WinAmp genre extensions. These were later incorporated in ID3v1.
   * @param allowLameExtensions Allow the LAME genre extensions. These are not part of ID3v1.
   * @return The description for the specified genre, or null if no matching description can be found.
   */
    public static String getGenreDescription(final int genreCode, final boolean allowWinAmpExtensions, final boolean allowLameExtensions) {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "getGenreDescription(int,boolean,boolean)");
        String result = null;
        if (genreCode >= 0 && genreCode < GenreNormalizer.genres.length && (allowWinAmpExtensions || genreCode < GenreNormalizer.firstWinAmpExtensionIndex) && (allowLameExtensions || genreCode < GenreNormalizer.firstLameExtensionIndex)) {
            result = GenreNormalizer.genres[genreCode];
        }
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "getGenreDescription(int,boolean,boolean)", result);
        return result;
    }

    /**
   * Normalize the genre description as per the ID3/LAME specification. If no matching genre can be found, then
   * null is returned.
   * @param genreDescription The genre description.
   * @param allowWinAmpExtensions Allow the WinAmp genre extensions. These were later incorporated in ID3v1.
   * @param allowLameExtensions Allow the LAME genre extensions. These are not part of ID3v1.
   * @return The normalized description for the specified genre, or null if no matching code can be
   * found.
   */
    public static String normalizeGenreDescription(final String genreDescription, final boolean allowWinAmpExtensions, final boolean allowLameExtensions) {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "normalizeGenreDescription(String,boolean,boolean)");
        final String result = GenreNormalizer.normalizeGenreDescription(genreDescription, allowWinAmpExtensions, allowLameExtensions, GenreNormalizer.SearchMode.HEURISTIC);
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "normalizeGenreDescription(String,boolean,boolean)", result);
        return result;
    }

    /**
   * Normalize the genre description as per the ID3/LAME specification. If no matching genre can be found, then
   * null is returned.
   * @param genreDescription The genre description.
   * @param allowWinAmpExtensions Allow the WinAmp genre extensions. These were later incorporated in ID3v1.
   * @param allowLameExtensions Allow the LAME genre extensions. These are not part of ID3v1.
   * @param searchMode How to search for a matching code. STRICT requires the genre description to be exactly as
   * specified; NORMAL will tolerate differences in case and characters other than letters; HEURISTIC will try to
   * find an appropriate code if the NORMAL method fails. It is generally advised to use HEURISTIC.
   * @return The normalized description for the specified genre, or null if no matching code can be
   * found.
   */
    public static String normalizeGenreDescription(final String genreDescription, final boolean allowWinAmpExtensions, final boolean allowLameExtensions, final SearchMode searchMode) {
        GenreNormalizer.logger.entering(GenreNormalizer.class.getCanonicalName(), "normalizeGenreDescription(String,boolean,boolean,SearchMode)");
        final String result = GenreNormalizer.getGenreDescription(GenreNormalizer.getGenreCode(genreDescription, allowWinAmpExtensions, allowLameExtensions, searchMode), allowWinAmpExtensions, allowLameExtensions);
        GenreNormalizer.logger.exiting(GenreNormalizer.class.getCanonicalName(), "normalizeGenreDescription(String,boolean,boolean,SearchMode)", result);
        return result;
    }

    public static void main(String... Param) {
        System.out.println(GenreNormalizer.normalizeGenreDescription("Crossover", false, false));
        System.out.println(String.format("%1$s", (Object) null));
    }
}
