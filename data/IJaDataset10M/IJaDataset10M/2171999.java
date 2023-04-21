package com.ynhenc.gis.model.mapobj;

import java.io.*;
import java.util.*;
import java.sql.SQLException;
import com.ynhenc.comm.DebugInterface;
import com.ynhenc.comm.util.*;
import com.ynhenc.gis.model.shape.*;

public class MapFolder extends MapObject implements DebugInterface {

    public int getSize() {
        return this.objList.size();
    }

    public final void add(MapObject obj) {
        this.objList.add(obj);
        this.nameList.put(obj.getName(), obj);
        super.mbr = null;
    }

    public final MapObject remove(MapObject obj) {
        this.objList.remove(obj);
        this.nameList.remove(obj.getName());
        return obj;
    }

    public final void removeAll() {
        this.objList = new ArrayList<MapObject>();
        this.nameList = new Hashtable<String, MapObject>();
    }

    public void setSelectedSelf(boolean b) {
        super.setSelected(b);
    }

    @Override
    public void setSelected(boolean b) {
        this.setSelectedRecursive(b);
    }

    public final void setSelectedRecursive(boolean b) {
        this.setSelectedSelf(b);
        MapObject[] objs = this.toArrays();
        MapObject obj;
        MapFolder folder;
        for (int i = 0, len = objs.length; i < len; i++) {
            obj = objs[i];
            if (obj instanceof MapFolder) {
                folder = (MapFolder) obj;
                folder.setSelectedRecursive(b);
            } else {
                obj.setSelected(b);
            }
        }
    }

    public final MapObject get(String name) {
        return nameList.get(name);
    }

    public final MapObject get(int i) {
        if (i < objList.size()) {
            return objList.get(i);
        } else {
            return null;
        }
    }

    public MapObject[] toArrays() {
        int size = this.objList.size();
        MapObject[] objs = new MapObject[size];
        this.objList.toArray(objs);
        return objs;
    }

    public Object[] toArrays(Object[] objs) {
        return this.objList.toArray(objs);
    }

    @Override
    protected final Mbr calcMbr() {
        Mbr mbr = null;
        MbrInterface[] objs = this.toArrays();
        for (int i = 0, len = objs.length; i < len; i++) {
            mbr = Mbr.union(mbr, objs[i].getMbr());
        }
        return mbr;
    }

    public MapFolder(String name, Integer index) {
        super(name, index);
        this.objList = new ArrayList<MapObject>();
        this.nameList = new Hashtable<String, MapObject>();
    }

    private ArrayList<MapObject> objList;

    private Hashtable<String, MapObject> nameList;
}
