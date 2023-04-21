package com.shudes.pt.pojo;

import java.io.*;
import com.shudes.util.*;

public class GameLevel implements Serializable {

    private Long id;

    private String description;

    private Double bigBet;

    private Integer plNl;

    public String toString() {
        return Dumper.INSTANCE.dump(this);
    }

    public Double getBigBet() {
        return bigBet;
    }

    public void setBigBet(Double bet) {
        this.bigBet = bet;
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

    protected void setId(Long id) {
        this.id = id;
    }

    public Integer getPlNl() {
        return plNl;
    }

    public void setPlNl(Integer plNl) {
        this.plNl = plNl;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
