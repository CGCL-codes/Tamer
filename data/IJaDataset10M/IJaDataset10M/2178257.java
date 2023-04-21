package com.lepidllama.packageeditor.resources;

import java.beans.PropertyChangeListener;
import com.lepidllama.packageeditor.dbpf.Header;
import com.lepidllama.packageeditor.dbpf.IndexBlock;
import com.lepidllama.packageeditor.dbpf2.Tgi64;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.DataWriter;
import com.lepidllama.packageeditor.resources.interfaces.Scenegraph;

public class Geom extends Scenegraph {

    SegmentGeom segment = new SegmentGeom();

    public void read(DataReader in, Header header, IndexBlock indexBlock) {
        super.read(in, header, indexBlock);
        segment.read(in, header, indexBlock);
    }

    public void write(DataWriter out, Header header, IndexBlock indexBlock) {
        super.write(out, header, indexBlock);
        segment.write(out, header, indexBlock);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        segment.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        segment.addPropertyChangeListener(name, listener);
    }

    public boolean equals(Object obj) {
        return segment.equals(obj);
    }

    public long getEmbeddedID() {
        return segment.getEmbeddedID();
    }

    public int[][] getFaceIndex() {
        return segment.getFaceIndex();
    }

    public long getFormat() {
        return segment.getFormat();
    }

    public float[][] getNormals() {
        return segment.getNormals();
    }

    public Tgi64[] getReferences() {
        return segment.getReferences();
    }

    public float[][] getUV() {
        return segment.getUV();
    }

    public int getVersion() {
        return segment.getVersion();
    }

    public float[][] getVertices() {
        return segment.getVertices();
    }

    public int hashCode() {
        return segment.hashCode();
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        segment.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
        segment.removePropertyChangeListener(name, listener);
    }

    public void setEmbeddedID(long embeddedID) {
        segment.setEmbeddedID(embeddedID);
    }

    public void setFaceIndex(int[][] faceIndex) {
        segment.setFaceIndex(faceIndex);
    }

    public void setFormat(long format) {
        segment.setFormat(format);
    }

    public void setReferences(Tgi64[] references) {
        segment.setReferences(references);
    }

    public void setVersion(int version) {
        segment.setVersion(version);
    }

    public String toString() {
        return segment.toString();
    }
}
