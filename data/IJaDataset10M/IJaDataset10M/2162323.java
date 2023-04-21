package org.openscience.cdk.smsd.algorithm.rgraph;

import org.openscience.cdk.annotations.TestClass;

/**
 *  An CDKRMap implements the association between an edge (bond) in G1 and an edge
 *  (bond) in G2, G1 and G2 being the compared graphs in a RGraph context.
 * 
 * @author      Stephane Werner, IXELIS <mail@ixelis.net>,
 *              Syed Asad Rahman <asad@ebi.ac.uk> (modified the orignal code)
 * @cdk.created 2002-07-24
 * @cdk.module  smsd
 * @cdk.githash
 */
@TestClass("org.openscience.cdk.smsd.algorithm.cdk.CDKRMapTest")
public class CDKRMap {

    private int id1 = 0;

    private int id2 = 0;

    /**
     *  Constructor for the CDKRMap
     *
     * @param  id1  number of the edge (bond) in the graphe 1
     * @param  id2  number of the edge (bond) in the graphe 2
     */
    public CDKRMap(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    /**
     *  Sets the id1 attribute of the CDKRMap object
     *
     * @param  id1  The new id1 value
     */
    public void setId1(int id1) {
        this.id1 = id1;
    }

    /**
     *  Sets the id2 attribute of the CDKRMap object
     *
     * @param  id2  The new id2 value
     */
    public void setId2(int id2) {
        this.id2 = id2;
    }

    /**
     *  Gets the id1 attribute of the CDKRMap object
     *
     * @return    The id1 value
     */
    public int getId1() {
        return id1;
    }

    /**
     *  Gets the id2 attribute of the CDKRMap object
     *
     * @return    The id2 value
     */
    public int getId2() {
        return id2;
    }

    /**
     *  The equals method.
     *
     * @param  obj  The object to compare.
     * @return    true=if both ids equal, else false.
     */
    @Override
    public boolean equals(Object obj) {
        if (((CDKRMap) obj).getId1() == getId1() && ((CDKRMap) obj).getId2() == getId2()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Returns a hash code for object comparison.
     * @return    Returns a hash code for object comparison.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.getId1();
        hash = 79 * hash + this.getId2();
        return hash;
    }
}
