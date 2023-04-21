package de.sambalmueslie.gpx_manager.data;

import java.util.Vector;
import de.sambalmueslie.geocache_planer.common.Geocache;

public class GroundspeakCache {

    public GroundspeakCache(final String name, final String placed_by, final String owner_id, final String owner_name, final Geocache.TYPE type, final String container, final String difficulty, final String terrain, final String country, final String state, final String short_description, final String long_description, final String encoded_hints, final Vector<GroundspeakLog> logs) {
        setName(name);
        setPlaced_by(placed_by);
        setOwnerId(owner_id);
        setOwnerName(owner_name);
        setType(type);
        setContainer(container);
        setDifficulty(difficulty);
        setTerrain(terrain);
        setCountry(country);
        setState(state);
        setShortDesciption(short_description);
        setLongDesciption(long_description);
        setEncodedHints(encoded_hints);
        setLogs(logs);
    }

    public String getContainer() {
        return container;
    }

    public String getCountry() {
        return country;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getEncodedHints() {
        return encoded_hints;
    }

    public Vector<GroundspeakLog> getLogs() {
        return logs;
    }

    public String getLongDesciption() {
        return long_desciption;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return owner_id;
    }

    public String getOwnerName() {
        return owner_name;
    }

    public String getPlacedBy() {
        return placed_by;
    }

    public String getShortDesciption() {
        return short_desciption;
    }

    public String getState() {
        return state;
    }

    public String getTerrain() {
        return terrain;
    }

    public Geocache.TYPE getType() {
        return type;
    }

    private void setContainer(final String value) {
        container = value;
    }

    private void setCountry(final String value) {
        country = value;
    }

    private void setDifficulty(final String value) {
        difficulty = value;
    }

    private void setEncodedHints(final String value) {
        encoded_hints = value;
    }

    private void setLogs(final Vector<GroundspeakLog> value) {
        logs = value;
    }

    private void setLongDesciption(final String value) {
        long_desciption = value;
    }

    private void setName(final String value) {
        name = value;
    }

    private void setOwnerId(final String value) {
        owner_id = value;
    }

    private void setOwnerName(final String value) {
        owner_name = value;
    }

    private void setPlaced_by(final String value) {
        placed_by = value;
    }

    private void setShortDesciption(final String value) {
        short_desciption = value;
    }

    private void setState(final String value) {
        state = value;
    }

    private void setTerrain(final String value) {
        terrain = value;
    }

    private void setType(final Geocache.TYPE value) {
        type = value;
    }

    private String container = null;

    private String country = null;

    private String difficulty = null;

    private String encoded_hints = null;

    private Vector<GroundspeakLog> logs = null;

    private String long_desciption = null;

    private String name = null;

    private String owner_id = null;

    private String owner_name = null;

    private String placed_by = null;

    private String short_desciption = null;

    private String state = null;

    private String terrain = null;

    private Geocache.TYPE type = null;
}
