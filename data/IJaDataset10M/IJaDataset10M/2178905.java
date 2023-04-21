package net.sf.jardevil.locations;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Represents an unknown location of a class
 * 
 * @author Achim Huegen
 */
public class UnknownLocation extends ClassLocation {

    public UnknownLocation() {
        super(false);
    }

    public String getName() {
        return "Unknown";
    }

    public InputStream getByteCode(String className) throws FileNotFoundException {
        throw new FileNotFoundException("Location of class unknown: " + className);
    }
}
