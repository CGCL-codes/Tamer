package fr.free.davidsoft.calibre.datamodel;

import fr.free.davidsoft.tools.Helper;

public class Author {

    private String id;

    private String name;

    private String sort;

    private String guessedLastName;

    public Author(String id, String name, String sort) {
        super();
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        if (guessedLastName == null) {
            guessedLastName = sort;
            if (Helper.isNotNullOrEmpty(sort)) {
                int posOfSpace = sort.indexOf(',');
                if (posOfSpace >= 0) guessedLastName = sort.substring(0, posOfSpace);
            }
        }
        return guessedLastName;
    }

    public String getSort() {
        return sort;
    }

    public String toString() {
        return getId() + " - " + getName();
    }
}
