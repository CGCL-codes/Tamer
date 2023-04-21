package net.anjackson.physics.lattices;

import net.anjackson.maths.VectorD3;

/**
 * Creates the AB CsCl lattice by combining two SC lattices.
 * The minor sites lie at (0.5,0.5,0.5) of the SC unit cell.
 *
 * @author ajackso1
 * @version $Id: ABCsClLattice.java 993 2006-08-25 11:07:06Z anj $
 *
 */
public class ABCsClLattice extends MultipleLattice {

    /**
	 * Set up the CsCl unit cell.
	 */
    public ABCsClLattice() {
        uc[0] = 1.0;
        uc[1] = 1.0;
        uc[2] = 1.0;
        uc_sep = 1.0;
        nspc = 2;
        name = "ABCsCl";
    }

    /** Generate a CsCl lattice by combining to SC lattices.
	 * @see net.anjackson.physics.lattices.Lattice#generate()
	 */
    protected void generate() {
        if (!isInitialised()) return;
        lats = new VectorD3[2][];
        int nat = nx * ny * nz;
        na = nspc * nat;
        SimpleCubicLattice sc = new SimpleCubicLattice();
        sc.setSizeInCells(nx, ny, nz);
        sc.generate();
        lats[0] = sc.getLattice();
        sc.setOriginOffset(ucx * uc[0] * 0.5, ucy * uc[1] * 0.5, ucz * uc[2] * 0.5);
        sc.generate();
        lats[1] = sc.getLattice();
    }
}
