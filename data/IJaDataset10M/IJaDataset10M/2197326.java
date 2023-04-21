package org.jmathematics;

public interface Representation {

    Representation expand(int level);

    Representation normalize();
}
