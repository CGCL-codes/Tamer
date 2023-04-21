package ch.photoindex.db.virtual;

/**
 * A tag.
 * 
 * @author Lukas Blunschi
 * 
 */
public class Tag {

    public final String name;

    public final int count;

    public Tag(String name, int count) {
        this.name = name;
        this.count = count;
    }
}
