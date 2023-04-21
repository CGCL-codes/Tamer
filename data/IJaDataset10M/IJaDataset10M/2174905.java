package edu.byu.ece.edif.tools.sterilize.halflatch;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifCellInstance;

/**
 * @since Created on Oct 31, 2005
 */
public class XilinxProblemPrimitiveMap implements ProblemPrimitiveMap, Serializable {

    /**
     * Constructs a new XilinxProblemPrimitiveMap object, which contains sub
     * maps corresponding to IOB primitives and CLB primitives.
     * 
     * @param cell The EdifCell this XilinxProblemPrimitiveMap should be
     * associated with.
     */
    public XilinxProblemPrimitiveMap() {
        _iobPrimitives = new LinkedHashSet<EdifCellInstance>();
        _clbMap = XilinxCLBProblemPrimitiveMap.getXilinxCLBProblemPrimitiveMap();
        _iobMap = XilinxIOBProblemPrimitiveMap.getXilinxIOBProblemPrimitiveMap();
    }

    /**
     * Add the given EdifCellInstances to the the list of IOB primitives in this
     * ProblemPrimitiveMap. These instances will be handled as IOB instances
     * rather than CLB instances.
     * 
     * @param iobRegs A Collection of EdifCellInstances that correspond to
     * registers to be placed in an IOB
     */
    public void addIOBRegisters(Collection<EdifCellInstance> iobRegs) {
        _iobPrimitives.addAll(iobRegs);
    }

    public String getPrimitiveReplacementType(EdifCellInstance primitiveECI) {
        String primitiveReplacementType = null;
        if (_iobPrimitives.contains(primitiveECI)) primitiveReplacementType = _iobMap.getPrimitiveReplacementType(primitiveECI); else primitiveReplacementType = _clbMap.getPrimitiveReplacementType(primitiveECI);
        return primitiveReplacementType;
    }

    public String[] getPrimitiveReplacementFloatingPorts(EdifCellInstance primitiveECI) {
        String[] primitiveReplacementFloatingPorts = null;
        if (_iobPrimitives.contains(primitiveECI)) primitiveReplacementFloatingPorts = _iobMap.getPrimitiveReplacementFloatingPorts(primitiveECI); else primitiveReplacementFloatingPorts = _clbMap.getPrimitiveReplacementFloatingPorts(primitiveECI);
        return primitiveReplacementFloatingPorts;
    }

    public int getPrimitiveReplacementFloatingPortDefaultValue(EdifCellInstance primitiveECI, String floatingPort) {
        if (primitiveECI == null || floatingPort == null) return -1;
        int primitiveReplacementFloatingPortDefaultValue = -1;
        if (_iobPrimitives.contains(primitiveECI)) primitiveReplacementFloatingPortDefaultValue = _iobMap.getPrimitiveReplacementFloatingPortDefaultValue(primitiveECI, floatingPort); else primitiveReplacementFloatingPortDefaultValue = _clbMap.getPrimitiveReplacementFloatingPortDefaultValue(primitiveECI, floatingPort);
        return primitiveReplacementFloatingPortDefaultValue;
    }

    EdifCell _cell;

    XilinxCLBProblemPrimitiveMap _clbMap;

    XilinxIOBProblemPrimitiveMap _iobMap;

    Set<EdifCellInstance> _iobPrimitives;

    public static final boolean _debug = false;

    public static int test() {
        return 0;
    }

    public static void main(String[] args) {
        XilinxProblemPrimitiveMap.test();
    }
}
