package org.dag.dmj;

import java.io.*;
import java.util.ArrayList;

public class MapData {

    public int[] numitemsin = new int[4];

    public boolean[] hasmonin = new boolean[5];

    public boolean nomons = false;

    public boolean noghosts = false;

    public char mapchar;

    public int numProjs = 0;

    public ArrayList mapItems;

    public boolean canHoldItems;

    public boolean isPassable;

    public boolean hasParty;

    public boolean canPassProjs;

    public boolean canPassMons = true;

    public boolean canPassImmaterial = true;

    public boolean hasMons;

    public boolean hasCloud;

    public boolean hasItems;

    public boolean drawItems;

    public boolean drawFurtherItems;

    public MapData() {
    }

    public void addItem(Item i) {
        if (i == null) return;
        if (mapItems == null) mapItems = new ArrayList();
        mapItems.add(i);
        hasItems = true;
        numitemsin[i.subsquare]++;
    }

    public void removeItem(int index) {
        if (mapItems.size() == 1) hasItems = false;
        numitemsin[((Item) mapItems.get(index)).subsquare]--;
        mapItems.remove(index);
    }

    public void changeLevel(int amt, int lvlnum) {
    }

    public void setMapCoord(int level, int x, int y) {
    }

    public String toString() {
        return "" + mapchar;
    }

    public void save(ObjectOutputStream so) throws IOException {
        so.writeChar(mapchar);
        so.writeBoolean(canHoldItems);
        so.writeBoolean(isPassable);
        so.writeBoolean(canPassProjs);
        so.writeBoolean(canPassMons);
        so.writeBoolean(canPassImmaterial);
        so.writeBoolean(drawItems);
        so.writeBoolean(drawFurtherItems);
        so.writeInt(numProjs);
        so.writeBoolean(hasParty);
        so.writeBoolean(hasMons);
        so.writeBoolean(hasItems);
        if (hasItems) so.writeObject(mapItems);
    }

    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
    }
}
