package com.substanceofcode.tracker.model;

import com.substanceofcode.gps.GpsPosition;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Marker is a special location entity that has a higher meaning on a trail.
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class Marker {

    private GpsPosition pos;

    private String name;

    private String reference;

    public Marker(GpsPosition position, String name, String reference) {
        this.pos = position;
        this.name = name;
        this.reference = reference;
    }

    /** Unserialize marker from stream */
    public Marker(DataInputStream dis) throws IOException {
        name = dis.readUTF();
        reference = dis.readUTF();
        pos = new GpsPosition(dis);
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeUTF(name);
        dos.writeUTF(reference);
        pos.serialize(dos);
    }

    public GpsPosition getPosition() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }
}
