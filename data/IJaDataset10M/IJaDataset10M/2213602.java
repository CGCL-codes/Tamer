package net.afternoonsun.imaso.core.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.afternoonsun.imaso.common.Image;

public class ImageManager extends AbstractObjectManager {

    /** The name of the table where info on already sorted images is stored. */
    public static final String TABLE_SORTED = "images_sorted";

    /** The name of the table where info on user-ignored images is stored. */
    public static final String TABLE_IGNORED = "images_ignored";

    /** SQL code for creating the table for sorted images. */
    private static final String CREATE_SORTED = "CREATE TABLE " + DBConnect.DBNAME + "." + TABLE_SORTED + " (" + "    id          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS " + "AS IDENTITY (START WITH 1, INCREMENT BY 1)," + "    filename    VARCHAR(255)," + "    hash        VARCHAR(255))";

    /** SQL code for creating a table for ignored images. */
    private static final String CREATE_IGNORED = "CREATE TABLE " + DBConnect.DBNAME + "." + TABLE_IGNORED + " (" + "    id          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS " + "AS IDENTITY (START WITH 1, INCREMENT BY 1)," + "    filename    VARCHAR(255)," + "    hash        VARCHAR(255))";

    /** SQL code for deleting the sorted table. */
    private static final String DELETE_SORTED = "DELETE FROM " + DBConnect.DBNAME + "." + TABLE_SORTED;

    /** SQL code for deleting the table with info on ignored images. */
    private static final String DELETE_IGNORED = "DELETE FROM " + DBConnect.DBNAME + "." + TABLE_IGNORED;

    /** SQL code for retrieving info on images that have been sorted. For MD5
     device management type. */
    private static final String GET_IMAGE_HASH_SORTED = "SELECT * FROM " + DBConnect.DBNAME + "." + TABLE_SORTED + " WHERE hash = ? ";

    /** SQL code for retrieving images which are already in collection. For MD5
     device management type. */
    private static final String GET_IMAGE_HASH_IGNORED = "SELECT * FROM " + DBConnect.DBNAME + "." + TABLE_IGNORED + " WHERE hash = ? ";

    /** SQL code for storing images that are in collection. For MD5 device
     management type. */
    private static final String SAVE_IMAGE_HASH_SORTED = "INSERT INTO " + DBConnect.DBNAME + "." + TABLE_SORTED + "    (filename, hash) VALUES (?, ?)";

    /** SQL code for storing images that are ignored by the user. For MD5 device
     management type. */
    private static final String SAVE_IMAGE_HASH_IGNORED = "INSERT INTO " + DBConnect.DBNAME + "." + TABLE_IGNORED + " " + "   (filename, hash) VALUES (?, ?)";

    /** Holds the instance for the Singleton pattern. */
    private static ImageManager instance;

    /** Compiled statement to save an image to the sorted table (MD5 mode). */
    private PreparedStatement saveImageSorted;

    /** Compiled statement to save an image to the ignored table (MD5 mode). */
    private PreparedStatement saveImageIgnored;

    /** Compiled statement to retrieve an image from the sorted table
     (MD5 mode). */
    private PreparedStatement getImageSorted;

    /** Compiled statement to retrieve an image from the ignored table
     (MD5 mode). */
    private PreparedStatement getImageIgnored;

    /**
     * A default constructor to be used with the Singleton pattern.
     */
    protected ImageManager() {
        initOrder = 0;
    }

    /**
     * Returns a singleton instance of the image object manager.
     *
     * @return instance of the object manager
     */
    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    /**
     * Checks if an image is already in the collection. It does that by checking
     * whether image's MD5 has is in the database. Used in conjuction with MD5
     * management mode.
     *
     * @param image object which is checked against the database
     * @return true if an image is in collection, false otherwise
     */
    public boolean isSorted(Image image) {
        try {
            getImageSorted.clearParameters();
            getImageSorted.setString(1, image.getHash());
            final ResultSet result = getImageSorted.executeQuery();
            return result.next();
        } catch (SQLException e) {
            logger.warn("Query failed", e);
            return false;
        }
    }

    /**
     * Checks if an image has been ignored by the user. It does that by checking
     * whether image's MD5 has is in the database. Used in conjuction with MD5
     * management mode.
     *
     * @param image object which is checked against the database
     * @return true if an image is ignored, false otherwise
     */
    public boolean isIgnored(Image image) {
        try {
            getImageIgnored.clearParameters();
            getImageIgnored.setString(1, image.getHash());
            final ResultSet result = getImageIgnored.executeQuery();
            return result.next();
        } catch (SQLException e) {
            logger.warn("Query failed", e);
            return false;
        }
    }

    /**
     * Puts image's MD5 hash in the database to indicate that it is in
     * collection now.
     *
     * @param image photo that has been sorted
     */
    public void putSorted(Image image) {
        try {
            saveImageSorted.clearParameters();
            saveImageSorted.setString(1, image.getFilename());
            saveImageSorted.setString(2, image.getHash());
            saveImageSorted.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Query failed", e);
        }
    }

    /**
     * Puts image's MD5 hash in the database to indicate that it is ignored by
     * the user.
     *
     * @param image photo that has been ignored
     */
    public void putIgnored(Image image) {
        try {
            saveImageIgnored.clearParameters();
            saveImageIgnored.setString(1, image.getFilename());
            saveImageIgnored.setString(2, image.getHash());
            saveImageIgnored.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Query failed", e);
        }
    }

    /**
     * Called when a new database is created and initialization is needed.
     *
     * @throws SQLException in case SQL fails
     */
    @Override
    public void init() throws SQLException {
        final Statement statement = connection.createStatement();
        statement.execute(CREATE_SORTED);
        statement.execute(CREATE_IGNORED);
    }

    /**
     * Called when the contents of the database should be cleared. Deletes
     * everything from the database.
     *
     * @throws SQLException in case SQL fails
     */
    @Override
    public void clear() throws SQLException {
        final Statement statement = connection.createStatement();
        statement.execute(DELETE_SORTED);
        statement.execute(DELETE_IGNORED);
    }

    /**
     * Called when a database is loaded and commonly used SQL statements are
     * compiled for more efficient database interaction.
     *
     * @throws SQLException in case SQL fails
     */
    @Override
    public void compile() throws SQLException {
        saveImageSorted = connection.prepareStatement(SAVE_IMAGE_HASH_SORTED);
        saveImageIgnored = connection.prepareStatement(SAVE_IMAGE_HASH_IGNORED);
        getImageSorted = connection.prepareStatement(GET_IMAGE_HASH_SORTED);
        getImageIgnored = connection.prepareStatement(GET_IMAGE_HASH_IGNORED);
    }

    /**
     * Returns a meaningful name of the object manager.
     *
     * @return name of the object manager
     */
    @Override
    public String toString() {
        return "Image manager";
    }
}
