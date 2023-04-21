package com.shudes.pt.pojo;

import java.io.*;
import com.shudes.util.*;

public class HandRank implements Serializable {

    private Long id;

    private String description;

    public String toString() {
        return Dumper.INSTANCE.dump(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
