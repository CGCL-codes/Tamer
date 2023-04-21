package org.dag.dmj.data.wall;

import java.util.ArrayList;
import java.io.*;
import org.dag.dmj.*;
import org.dag.dmj.data.*;
import org.dag.dmj.data.WallData;
import org.dag.dmj.editor.*;

public class AlcoveData extends WallData {

    public ArrayList northside = new ArrayList();

    public ArrayList southside = new ArrayList();

    public ArrayList eastside = new ArrayList();

    public ArrayList westside = new ArrayList();

    public boolean isSwitch = false;

    public MultWallSwitchData alcoveswitchdata;

    public AlcoveData() {
        super();
        mapchar = '[';
    }

    public AlcoveData(MultWallSwitchData fs) {
        super();
        mapchar = '[';
        alcoveswitchdata = fs;
        isSwitch = true;
    }

    public void addItem(Item i) {
        addItem(i, 0);
    }

    public void addItem(Item i, int side) {
        i.subsquare = side;
        if (side == 0) northside.add(i); else if (side == 1) westside.add(i); else if (side == 2) southside.add(i); else eastside.add(i);
        numitemsin[side]++;
    }

    public void removeItem(int index) {
        removeItem(index, 0);
    }

    public Item removeItem(int index, int side) {
        if (numitemsin[side] == 0) return null;
        numitemsin[side]--;
        Item tempitem;
        if (side == 0) tempitem = (Item) northside.remove(index); else if (side == 1) tempitem = (Item) westside.remove(index); else if (side == 2) tempitem = (Item) southside.remove(index); else tempitem = (Item) eastside.remove(index);
        return tempitem;
    }

    public void changeItemSide(int index, int oldside, int newside) {
        Item tempitem = removeItem(index, oldside);
        addItem(tempitem, newside);
    }

    public void changeLevel(int amt, int lvlnum) {
        if (isSwitch) alcoveswitchdata.changeLevel(amt, lvlnum);
    }

    public void setMapCoord(int level, int x, int y) {
        if (isSwitch) alcoveswitchdata.setMapCoord(level, x, y);
    }

    public String toString() {
        if (isSwitch) return "4-Sided Alcove (Switch)"; else return "4-Sided Alcove";
    }

    public boolean holdingItems() {
        if (northside.isEmpty() && southside.isEmpty() && eastside.isEmpty() && westside.isEmpty()) return false; else return true;
    }

    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(northside);
        so.writeObject(southside);
        so.writeObject(eastside);
        so.writeObject(westside);
        so.writeBoolean(isSwitch);
        if (isSwitch) alcoveswitchdata.save(so);
    }

    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        northside = (ArrayList) si.readObject();
        southside = (ArrayList) si.readObject();
        eastside = (ArrayList) si.readObject();
        westside = (ArrayList) si.readObject();
        isSwitch = si.readBoolean();
        if (isSwitch) alcoveswitchdata = (MultWallSwitchData) DMEditor.loadMapData(si, -1, 0, 0);
    }
}
