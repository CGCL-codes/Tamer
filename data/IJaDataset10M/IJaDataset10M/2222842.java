package org.joy.index.db.entry;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 海
 */
public class Pos implements Comparable, Serializable {

    private int pos;

    private float weight;

    /** Creates a new instance of Pos */
    public Pos(int pos, float level) {
        setPos(pos);
        setWeight(level);
    }

    public Pos(int pos) {
        setPos(pos);
        setWeight((byte) 1);
    }

    public Pos(DataInputStream in) {
        try {
            pos = in.readInt();
            weight = in.readFloat();
        } catch (IOException ex) {
            Logger.getLogger(Hit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeInt(pos);
            out.writeFloat(weight);
            baos.close();
            out.close();
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Hit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float level) {
        this.weight = level;
    }

    @Override
    public boolean equals(Object obj) {
        Pos p = (Pos) obj;
        return getPos() == p.getPos();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.pos;
        return hash;
    }

    public int compareTo(Object o) {
        Pos p = (Pos) o;
        return getPos() - p.getPos();
    }
}
