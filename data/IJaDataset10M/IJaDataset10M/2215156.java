package org.pqt.mr2rib.ribtranslator;

import java.util.Iterator;
import java.util.Vector;
import org.pqt.mr2rib.RendererOptions;
import org.pqt.mr2rib.mrutil.SubdivisionSurface;

/**
 *
 * @author Peter Quint
 */
public class RIBCurves extends RIBPrimitive {

    /** The array indicating number of vertices each polygon has*/
    public int ncurves;

    public int degree = 3;

    public FloatFile nvertices;

    public float widths[] = null;

    public float constantWidth = -1;

    public void writePartPrim(PrettyPrint out) {
        out.print("Curves ");
        if (degree != 3) out.printRIB("linear"); else out.printRIB("cubic");
        out.printRIBInt(nvertices);
        out.printRIB("nonperiodic");
    }

    public void clear() {
        super.clear();
        nvertices = null;
        widths = null;
    }
}
