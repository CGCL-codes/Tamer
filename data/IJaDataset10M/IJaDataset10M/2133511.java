package com.mojang.joxsi.loader;

import java.io.Serializable;
import java.util.ListIterator;

/**
 * Specifies the positions, normals, colors, and uv coordinates for 
 * triangle vertices by indexing into the SI_Shape template that defines the mesh.
 * 
 * <p>This class is a container for a template in the dotXSI file format, as specified by XSIFTK template reference.
 * 
 * <p>It's very sparsely documented.
 * @author Notch
 * @author Egal
 * TODO identify if this is 3.5 and newer and then fill <code>newVersion</code> and parse <code>Material</code>.
 */
public class SI_TriangleList extends Template {

    public static final String NORMAL = "NORMAL";

    public static final String COLOR = "COLOR";

    public static final String TEX_COORD_UV = "TEX_COORD_UV";

    public boolean newVersion;

    /** Number of triangles in the tessellation. */
    public int nbTriangles;

    /**
     * Specifies what information is stored in the template. Can be one or more of the following:<br>
     * � NORMAL = Template contains Normals information (see ni below).<br>
     * � COLOR = Template contains Color information (see ci below).<br>
     * � TEX_COORD_UV# = Template contains texture UV coordinates information (see uvi below).
     * <p>The number sign (#) represents the number of the texture UV coordinates starting at 0.
     * <p>Note: The TEX_COORD_UV# element is only available in v3.5 and beyond.
     * <p>If more than one of these is present, use a vertical bar to separate the strings 
     * (for example, "NORMAL|COLOR|TEX_COORD_UV0|TEX_COORD_UV1").
     * <p>Note: Vertex positions are always present in a {@link SI_TriangleList } 
     * template (see {@link Triangle#v } below).
     */
    public String elements;

    /**
     * Name of the material.
     * <p>Note: This is only available in v3.5 and beyond.
     */
    public String material;

    /** Array of triangles. */
    public Triangle[] triangles;

    /**
     * A triangle, with indexes for vertex position (v), normal (n), color (c) and several uv mappings (uv) 
     */
    public static class Triangle implements Serializable {

        /** Index of a vertex position in the POSITIONS section of the <code>SI_TriangleList</code> template for the mesh. */
        public int[] v;

        /** Index of a normal in the NORMAL section of the <code>SI_TriangleList</code> template for the mesh. */
        public int[] n;

        /** Index of a color in the COLOR section of the <code>SI_TriangleList</code> template for the mesh. */
        public int[] c;

        /**
         * Index of a UV coordinate in the TEX_COORD_UV section of the <code>SI_TriangleList</code> 
         * template for the mesh.
         * <p>Note: The uvi element is only available in v3.5 and beyond. 
         */
        public int[][] uv;
    }

    @Override
    public void parse(RawTemplate block) {
        ListIterator<Object> it = block.values.listIterator();
        nbTriangles = ((Integer) it.next()).intValue();
        elements = (String) it.next();
        Object next = it.next();
        it.previous();
        if (next instanceof String) material = (String) it.next();
        triangles = new Triangle[nbTriangles];
        for (int i = 0; i < nbTriangles; i++) {
            triangles[i] = new Triangle();
            triangles[i].v = new int[3];
            for (int j = 0; j < 3; j++) {
                triangles[i].v[j] = ((Integer) it.next()).intValue();
            }
        }
        if (elements.indexOf(NORMAL) >= 0) {
            for (int i = 0; i < nbTriangles; i++) {
                triangles[i].n = new int[3];
                for (int j = 0; j < 3; j++) {
                    triangles[i].n[j] = ((Integer) it.next()).intValue();
                }
            }
        }
        if (elements.indexOf(COLOR) >= 0) {
            for (int i = 0; i < nbTriangles; i++) {
                triangles[i].c = new int[3];
                for (int j = 0; j < 3; j++) {
                    triangles[i].c[j] = ((Integer) it.next()).intValue();
                }
            }
        }
        int uvN = 0;
        while (elements.indexOf(TEX_COORD_UV + uvN) >= 0) uvN++;
        if (uvN == 0 && elements.indexOf(TEX_COORD_UV) >= 0) uvN++;
        for (int i = 0; i < nbTriangles; i++) {
            triangles[i].uv = new int[uvN][];
        }
        for (int uv = 0; uv < uvN; uv++) {
            for (int i = 0; i < nbTriangles; i++) {
                triangles[i].uv[uv] = new int[3];
                for (int j = 0; j < 3; j++) {
                    triangles[i].uv[uv][j] = ((Integer) it.next()).intValue();
                }
            }
        }
    }

    @Override
    public String toString() {
        return template_type + " " + template_info + ", triangles: " + nbTriangles + ", element type: " + elements;
    }
}
