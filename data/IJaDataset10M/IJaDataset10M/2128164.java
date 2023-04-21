package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.media.j3d.SceneGraphObject;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple4d;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;

public class NullSceneGraphObjectState extends SceneGraphObjectState {

    SymbolTableData symbolTableData;

    /**
     * Dummy class to represent a null object in the scene graph
     *
     */
    public NullSceneGraphObjectState(SymbolTableData symbol, Controller control) {
        super(null, control);
        symbolTableData = new SymbolTableData(-1, null, this, -1);
    }

    /**
     * DO NOT call symbolTable.addReference in writeObject as this (may)
     * result in a concurrentModificationException.
     *
     * All references should be created in the constructor
     */
    public void writeObject(DataOutput out) throws IOException {
    }

    public void readObject(DataInput in) throws IOException {
    }

    public SceneGraphObject getNode() {
        return null;
    }

    public int getNodeID() {
        return -1;
    }

    public SymbolTableData getSymbol() {
        return symbolTableData;
    }

    protected javax.media.j3d.SceneGraphObject createNode() {
        return null;
    }
}
