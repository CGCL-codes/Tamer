package jreceiver.server.rec;

import java.util.Hashtable;
import jreceiver.common.rec.Key;
import jreceiver.util.HelperMap;

/**
 * An interface representing a single master/slave binding key
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:09 $
 */
public class ArtistAlbumKey implements Key {

    public static final String HKEY_ARTIST_ID = "ARTIST_ID";

    public static final String HKEY_ALBUM_ID = "ALBUM_ID";

    /** ctor - shallow */
    public ArtistAlbumKey(int artist_id, int album_id) {
        this.artist_id = artist_id;
        this.album_id = album_id;
    }

    /**
     * ctor - construct from a hashtable, usually retrieved via XML-RPC
     */
    public ArtistAlbumKey(Hashtable hash) {
        this(HelperMap.getInt(hash, HKEY_ARTIST_ID), HelperMap.getInt(hash, HKEY_ALBUM_ID));
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    public boolean equals(Object obj) {
        ArtistAlbumKey other = (ArtistAlbumKey) obj;
        return other.album_id == this.album_id && other.artist_id == this.artist_id;
    }

    public int getArtistId() {
        return artist_id;
    }

    public int getAlbumId() {
        return album_id;
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString() {
        return new StringBuffer().append("ArtistAlbumKey: [artist_id=").append(artist_id).append(", album_id=").append(album_id).append("]").toString();
    }

    /**
     * Store the object as a hash suitable for
     * transport over an XML-RPC connection or similar.
     * <p>
     * Use <code>ArtistAlbumRec(Hashtable)</code> to transform the data
     * back into an object of this type.
     * <p>
     * @return
     */
    public Hashtable toHash() {
        Hashtable hash = new Hashtable();
        hash.put(HKEY_ARTIST_ID, new Integer(artist_id));
        hash.put(HKEY_ALBUM_ID, new Integer(album_id));
        return hash;
    }

    int artist_id;

    int album_id;
}
