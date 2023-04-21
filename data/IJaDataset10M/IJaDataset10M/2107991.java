package org.openscience.cdk.ringsearch;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.ringsearch.Path;
import org.openscience.cdk.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class PathTest extends CDKTestCase {

    public PathTest() {
        super();
    }

    @Test
    public void testJoin() {
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom atom2 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        Path path1 = new Path(atom1, atom2);
        IAtom atom3 = DefaultChemObjectBuilder.getInstance().newAtom("F");
        Path path2 = new Path(atom2, atom3);
        Path joinedPath = Path.join(path1, path2, atom2);
        Assert.assertEquals(3, joinedPath.size());
        Assert.assertEquals(joinedPath.get(0), atom1);
        Assert.assertEquals(joinedPath.get(1), atom2);
        Assert.assertEquals(joinedPath.get(2), atom3);
    }

    @Test
    public void testGetIntersectionSize() {
        IAtom atom1 = DefaultChemObjectBuilder.getInstance().newAtom("C");
        IAtom atom2 = DefaultChemObjectBuilder.getInstance().newAtom("Cl");
        Path path1 = new Path(atom1, atom2);
        IAtom atom3 = DefaultChemObjectBuilder.getInstance().newAtom("F");
        Path path2 = new Path(atom2, atom3);
        int intersectSize = path1.getIntersectionSize(path2);
        Assert.assertEquals(1, intersectSize);
    }
}
