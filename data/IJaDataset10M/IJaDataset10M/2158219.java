package org.pqt.mr2rib.ribtranslator;

import org.pqt.mr2rib.mrparser.MRPConstants;
import org.pqt.mr2rib.mrutil.Basis;

/**Translates a basis
 *
 * @author Peter Quint  */
public class BasisTranslator extends Translator {

    /**The name of this basis, if it is one of the standard types*/
    public String basisName = "";

    /**The order of this basis, can be either linear or cubic, i.e 1 or 3*/
    public int order = -1;

    /**The basis matrix, if this basis is not one of the standard ones*/
    public float[] matrix = null;

    /**This is the step of the basis*/
    public int step = -1;

    /** Creates a new instance of BasisTranslator */
    public BasisTranslator(Basis b) {
        if (b.type == MRPConstants.BSPLINE) basisName = "b-spline"; else if (b.type == MRPConstants.BEZIER) basisName = "bezier";
        if (b.matrix != null) matrix = b.matrix;
        if ((b.degree == 1) || (b.degree == 3)) order = (int) b.degree;
        step = (int) b.stepSize;
    }

    public void writeNormal(PrettyPrint out, RenderContext rc) {
    }

    public void writePart(PrettyPrint out) {
        if (matrix != null) out.printRIB(matrix); else if (basisName != null) out.print(basisName + ' '); else return;
        out.printRIB(step);
    }
}
