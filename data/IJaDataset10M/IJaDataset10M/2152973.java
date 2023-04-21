package com.sun.opengl.impl.tessellator;

class ActiveRegion {

    GLUhalfEdge eUp;

    DictNode nodeUp;

    int windingNumber;

    boolean inside;

    boolean sentinel;

    boolean dirty;

    boolean fixUpperEdge;
}
