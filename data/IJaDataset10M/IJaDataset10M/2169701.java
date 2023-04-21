package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.arcalgorithm.variable.GenericConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;

/**
 * Utility for creating iterators that can be used by arcs when processing generic nodes
 */
public class GenericNodeIndexManager extends GenericIndexManager {

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     */
    public GenericNodeIndexManager(Node x, Node y, Node z) {
        this(x, y, z, null, false);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     */
    public GenericNodeIndexManager(Node x, Node y, Node z, GenericIndex restrictedIndices[]) {
        this(x, y, z, restrictedIndices, false);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     * @param   restrictZ           True if Z node should be restricted
     */
    public GenericNodeIndexManager(Node x, Node y, Node z, GenericIndex restrictedIndices[], boolean restrictZ) {
        GenericIndex zIndices[] = null;
        if (z != null && z instanceof GenericNode) {
            GenericNode znode = (GenericNode) z;
            zIndices = znode.getIndices();
        }
        GenericIndex xIndices[] = null;
        if (x != null && x instanceof GenericNode) {
            GenericNode xnode = (GenericNode) x;
            xIndices = xnode.getIndices();
        }
        GenericIndex yIndices[] = null;
        if (y != null && y instanceof GenericNode) {
            GenericNode ynode = (GenericNode) y;
            yIndices = ynode.getIndices();
        }
        init(xIndices, yIndices, zIndices, restrictedIndices, restrictZ);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     */
    public GenericNodeIndexManager(GenericConstant x, Node y, Node z) {
        this(x, y, z, null);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     */
    public GenericNodeIndexManager(Node x, GenericConstant y, Node z) {
        this(x, y, z, null);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     */
    public GenericNodeIndexManager(GenericConstant x, Node y, Node z, GenericIndex restrictedIndices[]) {
        this(null, x, y, null, z, restrictedIndices, false);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     */
    public GenericNodeIndexManager(Node x, GenericConstant y, Node z, GenericIndex restrictedIndices[]) {
        this(x, null, null, y, z, restrictedIndices, false);
    }

    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   x                   X source node for the equation
     * @param   y                   Y source node for the equation
     * @param   z                   Z source node for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     * @param   restrictZ           True if Z node should be restricted
     */
    public GenericNodeIndexManager(Node x, GenericConstant xConst, Node y, GenericConstant yConst, Node z, GenericIndex restrictedIndices[], boolean restrictZ) {
        GenericIndex zIndices[] = null;
        if (z != null && z instanceof GenericNode) {
            GenericNode znode = (GenericNode) z;
            zIndices = znode.getIndices();
        }
        GenericIndex xIndices[] = null;
        if (x != null && x instanceof GenericNode) {
            GenericNode xnode = (GenericNode) x;
            xIndices = xnode.getIndices();
        } else if (xConst != null) {
            xIndices = (GenericIndex[]) xConst.getIndices();
        }
        GenericIndex yIndices[] = null;
        if (y != null && y instanceof GenericNode) {
            GenericNode ynode = (GenericNode) y;
            yIndices = ynode.getIndices();
        } else if (yConst != null) {
            yIndices = (GenericIndex[]) yConst.getIndices();
        }
        init(xIndices, yIndices, zIndices, restrictedIndices, restrictZ);
    }
}
