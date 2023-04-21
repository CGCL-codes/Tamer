package com.lightminds.map.tileserver.admin.xml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TileProviderName {

    private int id;

    private String name;

    TileProviderName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().getName().equals("java.lang.Integer") && ((Integer) o).intValue() == (this.id)) return true;
        if (this.id == ((TileProviderName) o).id) return true;
        return false;
    }

    public static final List<TileProviderName> getAllTileProviderNames() {
        ArrayList<TileProviderName> allTileProviderNames = new ArrayList<TileProviderName>();
        allTileProviderNames.add(new TileProviderName(1, "TeleAtlas"));
        allTileProviderNames.add(new TileProviderName(3, "LMVSE"));
        allTileProviderNames.add(new TileProviderName(4, "LMVSEOrtho"));
        allTileProviderNames.add(new TileProviderName(5, "LMVSESweref99"));
        allTileProviderNames.add(new TileProviderName(6, "TeleAtlas2009"));
        return allTileProviderNames;
    }
}
