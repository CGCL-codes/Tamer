package soundlibrary;

import java.net.*;

/**
 * This class is used to encapsulate the data contained in each entry in the
 * soundlibrary Database. For each datum, there is a get and set method. A 
 * SoundLibraryEntry is primarily used to hold the information for a remote
 * sound stream. This stream is held on the teaching server and can be opened by
 * using the url field.
 * 
 * Each entry holds the data it gets from the server. It has the Ttile, Author,
 * Genre, and Tags of the stream, as well as the url where the stream itself is.
 * You may also get the stream's Mime Type by calling the getMimeType() method.
 * 
 * Additionally, it also has the date is wat uploaded and the sequence number.
 * The date, sequence, and URL are server generated values and may not be
 * changed.
 * @author dan
 */
public class SoundLibraryEntry {

    /**
     * This the the stream's remote location. By making its type URL, it ensures
     * that only a valid URL may be used.
     */
    private URL url;

    /**
     * This is the stream's title as given by the SQL database
     */
    private String title;

    /**
     * This is the stream's author. This person is typically also the uploader.
     */
    private String author;

    /**
     * This is the genre of the stream. It gives a general idea about what kind
     * of sound the stream is.
     */
    private String genre;

    /**
     * These are the comma seperated tags assigned to the stream. The author 
     * assigns them at upload time. These are typically more specific 
     * descriptors of the file. 
     */
    private String tags;

    /**
     * This is the date the entry was last uploaded. The server timestamps each
     * file as it is uploaded. If this file was updated from an older version,
     * the most recent date will be saved.
     */
    private String date;

    /**
     * This is the sequence number for the sound as assigned by the server. This
     * number is a part of the file's final URL.
     */
    private String sequence;

    /**
     * The constructor for this class takes in each of the values that make up
     * the SoundLibraryEntry object. It takes in a URL and four Strings and 
     * assigns them to the proper private data members.
     * @param given_url
     * @param given_title
     * @param given_author
     * @param given_genre
     * @param given_tags
     * @param given_date 
     * @param given_sequence 
     */
    public SoundLibraryEntry(URL given_url, String given_title, String given_author, String given_genre, String given_tags, String given_date, String given_sequence) {
        url = given_url;
        title = given_title;
        author = given_author;
        genre = given_genre;
        tags = given_tags;
        date = given_date;
        sequence = given_sequence;
    }

    /**
     * This method will check the Mime Type of a remote sound stream. URL is
     * only used for remote sound files, so it cannot be used for files on the
     * local system. Keep in mind, SoundLibraryEntries created for the purpose
     * of uploading do not use the URL field anyway.
     * @return
     */
    public String getMimeType() throws Exception {
        if (url == null) throw new Exception("Cannot get Mime type from undefined URL.");
        URLConnection connection = url.openConnection();
        return (connection.getContentType());
    }

    /**
     * The the URL of the SoundLibraryEntry.
     * @return the URL
     */
    public URL getURL() {
        return (url);
    }

    /**
     * The the Title of the SoundLibraryEntry.
     * @return the title
     */
    public String getTitle() {
        return (title);
    }

    /**
     * The the Author of the SoundLibraryEntry.
     * @return the author
     */
    public String getAuthor() {
        return (author);
    }

    /**
     * The the Genre of the SoundLibraryEntry.
     * @return the genre
     */
    public String getGenre() {
        return (genre);
    }

    /**
     * The the Tags of the SoundLibraryEntry.
     * @return the tags
     */
    public String getTags() {
        return (tags);
    }

    /**
     * We may get the date the file was most recently changed here.
     * @return date last uploaded
     */
    public String getDate() {
        return (date);
    }

    /**
     * This is the get method for the Entry's sequence number.
     * @return Entry's sequence number
     */
    public String getSequence() {
        return (sequence);
    }

    /**
     * Sets the Title to the given value.
     * @param new_title
     */
    public void setTitle(String new_title) {
        title = new_title;
    }

    /**
     * Sets the Author to the given value.
     * @param new_author
     */
    public void setAuthor(String new_author) {
        author = new_author;
    }

    /**
     * Sets the Genre to the given value.
     * @param new_genre
     */
    public void setGenre(String new_genre) {
        genre = new_genre;
    }

    /**
     * Sets the Tags to the given value.
     * @param new_tags
     */
    public void setTags(String new_tags) {
        tags = new_tags;
    }
}
