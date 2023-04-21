package com.wavechain.system;

import java.util.ArrayList;

public class Sensor {

    String id = null;

    ArrayList<int[]> tagIds = new ArrayList<int[]>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList getTagIds() {
        return tagIds;
    }

    public void setTagIds(ArrayList<int[]> name) {
        this.tagIds = name;
    }

    public Sensor(String id) throws Exception {
        if ((id == null) || (id.length() == 0)) {
            throw new Exception("id must not be null or empty");
        }
        this.id = id;
    }
}
