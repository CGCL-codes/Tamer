package org.openscience.cdk.math.qm;

import org.openscience.cdk.math.Matrix;
import org.openscience.cdk.math.Vector;

/**
 * A basis set must implement this class.
 * 
 * @author  Stephan Michels <stephan@vern.chem.tu-berlin.de>
 * @cdk.githash
 * @cdk.created 2001-07-02
 * @cdk.module  qm
 */
public interface IBasis {

    /**
   * Gets the number of base vectors
   */
    public int getSize();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMinX();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMaxX();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMinY();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMaxY();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMinZ();

    /**
   * Gets the dimension of the volume, which describes the base.
   */
    public double getMaxZ();

    /**
   * Calculates the function value an (x,y,z).
   * @param index The number of the base 
   */
    public double getValue(int index, double x, double y, double z);

    /**
   * Calculates the function values.
   * @param index The number of the base 
   */
    public Vector getValues(int index, Matrix x);

    /**
   * Calculate the overlap integral S = &lt;phi_i|phi_j>.
   *
   * @param i Index of the first base
   * @param j Index of the second base
   */
    public double calcS(int i, int j);

    /**
   * Calculates the impulse J = -&lt;d/dr chi_i | d/dr chi_j>.
   *
   * @param i Index of the first base
   * @param j Index of the second base
   */
    public double calcJ(int i, int j);

    /**
   * Calculates the potential V = &lt;chi_i | 1/r | chi_j>.
   *
   * @param i Index of the first base
   * @param j Index of the second base
   */
    public double calcV(int i, int j);

    /**
   * Calculates a two eletron fout center integral
   * I = &ltchi_i chi_j | 1/r12 | chi_k chi_l >.
   *
   * @param i Index of the first base
   * @param j Index of the second base
   * @param k Index of the third base
   * @param l Index of the fourth base
   */
    public double calcI(int i, int j, int k, int l);
}
