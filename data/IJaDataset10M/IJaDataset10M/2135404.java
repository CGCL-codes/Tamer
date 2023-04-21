package net.sf.dict4j.entity;

import java.io.Serializable;

/**
 * Represents word definition meta info
 */
public class Match implements Serializable {

    private Database database;

    private String word;

    public Match(Database database, String word) {
        this.database = database;
        this.word = word;
    }

    public Match(String databaseName, String word) {
        this(new DatabaseImpl(databaseName), word);
    }

    public Database getDatabase() {
        return database;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return getWord();
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Match match = (Match) object;
        return database.equals(match.database) && word.equals(match.word);
    }

    public int hashCode() {
        int result;
        result = database.hashCode();
        result = 31 * result + word.hashCode();
        return result;
    }
}
