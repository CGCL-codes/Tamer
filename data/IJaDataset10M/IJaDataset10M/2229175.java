package com.kenstevens.stratinit.ws.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SIGame implements Serializable {

    public int gameId;

    public String name;

    public int players;

    public int islands;

    public int size;

    public Date started;

    public Date ends;
}
