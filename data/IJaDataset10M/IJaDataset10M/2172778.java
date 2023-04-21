package net.face2face.core.location;

import java.io.*;

/**
 *
 * @author Patrice
 */
public interface Location extends Serializable {

    public double getLat();

    public double getLon();

    public String toString();
}
