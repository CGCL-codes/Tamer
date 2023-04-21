package at.wwu.tunes2db.types;

import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 * @author willi.w
 * Artist information
 */
public class Artist extends Element {

    private static final Logger log = Logger.getLogger(Artist.class.getName());

    private static final Hashtable<String, Artist> artists = new Hashtable<String, Artist>();

    private Artist(String new_name) {
        super(new_name);
    }

    public static Artist add(String name) {
        if (artists.containsKey(name.trim())) {
            instance = artists.get(name.trim());
            instance.isNew = false;
        } else {
            instance = new Artist(name.trim());
            instance.isNew = true;
            artists.put(name.trim(), (Artist) instance);
        }
        assert instance != null;
        if (instance == null) {
            System.exit(1);
        }
        return (Artist) instance;
    }

    @Override
    public String toString() {
        return "Artist " + id + ": " + name;
    }
}
