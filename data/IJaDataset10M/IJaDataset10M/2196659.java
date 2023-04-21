package org.elmarweber.sf.mp3index.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Main model class, contains all information required to create and maintain
 * the MP3 index.
 * 
 * 
 * @author Elmar Weber (university@elmarweber.org)
 */
public class Database {

    private Map<String, Artist> artists = new HashMap<String, Artist>();

    private Map<AlbumIdentifier, Album> albums = new HashMap<AlbumIdentifier, Album>();

    /**
     * Adds the specified song to the database. It updates all internal caches
     * and references with regard to artists, albums, etc. Furthermore it
     * updates the {@link Artist} and {@link Album} object associated with the
     * specified {@link Song}. The artist and album elements should be set and
     * retrieved via the corresponding methods in this class.
     * 
     * @param song
     *            the {@link Song} to add to the database.
     */
    public void addSong(Song song) {
    }

    /**
     * Returns the {@link Artist} with the specified name.
     * 
     * @param name
     *            the name of the artist to look for.
     * 
     * @return the {@link Artist} object in the database belonging to the
     *         specified name, <code>null</code> in case no such {@link Artist}
     *         exists.
     */
    public Artist getArtist(String name) {
        return artists.get(name);
    }

    /**
     * Returns the {@link Artist} with the specified name or, in case the
     * {@link Artist} does not exist it creates it and returns the newly created
     * {@link Artist}.
     * 
     * @param name
     *            the name of the artist to look for.
     * 
     * @return the {@link Artist} object in the database belonging to the
     *         specified name, if it does not exist it is created.
     */
    public Artist getOrCreateArtist(String name) {
        Artist artist = getArtist(name);
        if (null == artist) {
            artist = new Artist();
            artist.setName(name);
            artists.put(name, artist);
        }
        return artist;
    }

    /**
     * Returns the {@link Album} with the specified parameters.
     * 
     * @param name
     *            the name of the album to look for.
     * @param year
     *            the year the album was released.
     * 
     * @return the {@link Album} object in the database belonging to the
     *         specified name and year, <code>null</code> in case no such
     *         {@link Album} exists.
     */
    public Album getAlbum(String name, int year) {
        return albums.get(new AlbumIdentifier(name, year));
    }

    /**
     * Returns the {@link Album} with the specified parameters or, in case the
     * {@link Album} does not exist it creates it and returns the newly created
     * {@link Album}.
     * 
     * @param name
     *            the name of the album to look for.
     * @param year
     *            the year the album was released.
     * 
     * @return the {@link Album} object in the database belonging to the
     *         specified name and year, if it does not exist it is created.
     */
    public Album getOrCreateAlbum(String name, int year) {
        Album album = getAlbum(name, year);
        if (null == album) {
            album = new Album();
            album.setName(name);
            album.setYear(year);
            albums.put(new AlbumIdentifier(album), album);
        }
        return album;
    }

    /**
     * Used to identify {@link Album} objects by name and year in {@link Map
     * Maps}.
     * 
     * 
     * @author Elmar Weber (university@elmarweber.org)
     */
    private class AlbumIdentifier {

        private Album album;

        public AlbumIdentifier(Album album) {
            this.album = album;
        }

        public AlbumIdentifier(String name, int year) {
            this.album = new Album();
            this.album.setName(name);
            this.album.setYear(year);
        }

        @Override
        public int hashCode() {
            if (null == album) {
                return super.hashCode();
            }
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(album.getName());
            builder.append(album.getYear());
            return builder.toHashCode();
        }
    }
}
