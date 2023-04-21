package org.incava.java;

import java.io.*;
import java.util.*;

/**
 * Code location.
 */
public class Location {

    public int line;

    public int column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public String toString() {
        return "[line: " + line + ", column: " + column + "]";
    }

    public boolean equals(Object obj) {
        return obj instanceof Location && equals((Location) obj);
    }

    public boolean equals(Location other) {
        return other.line == line && other.column == column;
    }
}
