package song;

import org.ozoneDB.*;

/**
 * Aplication singleton for song services.
 * song.Song services basically encompass application logic relating
 * to songs -- adding, removing, fetching.
 * It does stuff like managing the MasterCollection,
 * and doing case-insensitive indexing.
 *
 * @version $Revision: 1.1 $ $Date: 2003/01/30 09:48:44 $
 * @author James Stiefel
 */
public class SongServices {

    private static SongCollection allSongs = null;

    private static OzoneInterface db = null;

    /**
     * Initialize the system - establish database connection, etc.
     *
     */
    public static void init(OzoneInterface database) {
        db = database;
        try {
            try {
                allSongs = (SongCollection) db.objectForName("_AllSongs");
                if (allSongs == null) {
                    System.out.println("init():  _AllSongs collection null. Creating...");
                    allSongs = (SongCollection) db.createObject(SongCollectionImpl.class.getName(), OzoneInterface.Public, "_AllSongs");
                } else {
                    System.out.println("init() allSongs found.");
                }
            } catch (org.ozoneDB.PermissionDeniedException e) {
                System.out.println("init():  _AllSongs collection not found. Creating...");
                allSongs = (SongCollection) db.createObject(SongCollectionImpl.class.getName(), OzoneInterface.Public, "_AllSongs");
            }
        } catch (Exception e) {
            System.out.println("init(): Problem getting or creating allSong collection.");
            e.printStackTrace();
        }
    }

    /**
     * Close down the application -- disconnect from the database, etc.
     *
     */
    public static void term() {
        allSongs = null;
        db = null;
    }

    /**
      * Retrieves the collection of all songs in the system.
      *
      */
    public static SongCollection getAllSongs() {
        return allSongs;
    }

    /**
     * Create a new song.Song in the system. If it can't create a song.Song
     * for any reason, it will throw an exception.
     *
     * @param title the song's title. You best check that it is unique
     * before you try. If is already taken, you'll get an exception.
     *
     * @return A proxy object for the newly created song.Song object.
     * @throws java.lang.Exception to signal a problem creating the new song
     * (e.g. a song with that title already exists).
     */
    public static Song createSong(String title) throws Exception {
        Song song = null;
        try {
            song = (Song) db.createObject(SongImpl.class.getName(), OzoneInterface.Public);
            song.setTitle(title);
            allSongs.addSong(title.toUpperCase(), song);
        } catch (Exception e) {
            System.out.println("createSong(): something went wrong adding to Ozone.");
            throw e;
        }
        return song;
    }

    /**
     * Deletes a song from the database.
     * Note that (until garbage collection is implemented),
     * any references to that song will be dangling pointers, so use extreme
     * caution when deleting a song.Song.
     * Be sure you have first eliminated references to it in other objects.
     *
     * @param title The song's title
     *
     * @return true if successful. False if not found.
     */
    public static boolean deleteSong(String title) {
        System.out.println("deleteSong <" + title + ">");
        Song song = allSongs.deleteSong(title.toUpperCase());
        return (song != null);
    }

    /**
     * Get a song.Song from the system by it's title.
     *
     * @param title The song's title.
     *
     * @return A proxy object for the requested song.Song object, or null if not found.
     */
    public static Song songForTitle(String title) {
        Song song = null;
        try {
            song = allSongs.findSong(title.toUpperCase());
            if (song == null) {
                System.out.println("song.Song not found by title <" + title + ">");
            }
        } catch (Exception e) {
            System.out.println("songForHandle(): something went wrong finding song.");
            e.printStackTrace();
        }
        return song;
    }

    /**
     * Get a song.Song from the system by its Handle.
     *
     * @param handle the user's unique ID
     *
     * @return A proxy object for the requested song.Song object, or null if not found.
     */
    public static Song songForHandle(String handle) {
        Song song = null;
        try {
            song = (Song) db.objectForHandle(handle);
            if (song == null) {
                System.out.println("song.Song not found by handle <" + handle + ">");
            }
        } catch (Exception e) {
            System.out.println("songForHandle(): something went wrong finding song.");
            e.printStackTrace();
        }
        return song;
    }
}
