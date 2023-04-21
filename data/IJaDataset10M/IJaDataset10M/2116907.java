package org.jopsdb.model;

import java.sql.Timestamp;

public class Measurement {

    public static final int OK = 0;

    public static final int WARNING = 1;

    public static final int ERROR = 2;

    private Long id;

    private Node node;

    private Observation observation;

    private String value;

    private int state;

    private Timestamp created;

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String translateState() {
        if (state == ERROR) {
            return "ERROR";
        }
        if (state == WARNING) {
            return "WARNING";
        }
        return "OK";
    }
}
