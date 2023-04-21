package com.moviejukebox.rottentomatoes.model;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

public class Cast {

    private static final Logger LOGGER = Logger.getLogger(Cast.class);

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String castName;

    @JsonProperty("characters")
    private Set<String> characters = new HashSet<String>();

    public String getCastName() {
        return castName;
    }

    public Set<String> getCharacters() {
        return characters;
    }

    public int getId() {
        return id;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public void setCharacters(Set<String> characters) {
        this.characters = characters;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder unknownBuilder = new StringBuilder();
        unknownBuilder.append("Unknown property: '").append(key);
        unknownBuilder.append("' value: '").append(value).append("'");
        LOGGER.warn(unknownBuilder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Cast=");
        builder.append("[id=").append(id);
        builder.append("], [castName=").append(castName);
        builder.append("], [characters=").append(characters);
        builder.append("]]");
        return builder.toString();
    }
}
