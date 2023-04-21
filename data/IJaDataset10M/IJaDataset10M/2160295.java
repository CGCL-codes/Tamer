package org.bff.slimserver.musicobjects;

/**
 * Represents a genre
 * @author Bill Findeisen
 */
public class SlimGenre extends SlimObject {

    /**
     * Default constructor
     */
    public SlimGenre() {
        super();
    }

    /**
     * Constructor
     * @param id genre id
     * @param name genre name
     */
    public SlimGenre(int id, String name) {
        super(id, name);
    }
}
