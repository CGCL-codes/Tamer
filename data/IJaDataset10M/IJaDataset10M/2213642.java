package org.jikesrvm.compilers.opt.dfsolver;

import java.util.Iterator;
import org.jikesrvm.compilers.opt.util.GraphNode;

/**
 * DF_LatticeCell.java
 *
 * Represents a single lattice cell in a dataflow equation system.
 */
public interface DF_LatticeCell extends GraphNode {

    /**
   * Returns an enumeration of the equations in which this
   * lattice cell is used.
   * @return an enumeration of the equations in which this
   * lattice cell is used
   */
    Iterator<DF_Equation> getUses();

    /**
   * Returns an enumeration of the equations in which this
   * lattice cell is defined.
   * @return an enumeration of the equations in which this
   * lattice cell is defined
   */
    Iterator<DF_Equation> getDefs();

    /**
   * Return a string representation of the cell
   * @return a string representation of the cell
   */
    String toString();

    /**
   * Note that this variable appears on the RHS of an equation
   *
   * @param eq the equation
   */
    void addUse(DF_Equation eq);

    /**
   * Note that this variable appears on the LHS of an equation
   *
   * @param eq the equation
   */
    void addDef(DF_Equation eq);
}
