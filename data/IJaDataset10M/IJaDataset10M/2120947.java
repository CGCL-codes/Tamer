package org.pdfbox.util.operator;

import java.util.List;
import org.pdfbox.cos.COSNumber;
import org.pdfbox.util.Matrix;
import org.pdfbox.util.PDFOperator;

/**
 * <p>Titre : PDFEngine Modification.</p>
 * <p>Description : Structal modification of the PDFEngine class : the long sequence of conditions 
 *    in processOperator is remplaced by this strategy pattern</p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Soci�t� : DBGS</p>
 * @author Huault : huault@free.fr
 * @version $Revision: 1.4 $
 */
public class SetMatrix extends OperatorProcessor {

    /**
     * Tm Set text matrix and text line matrix.
     * @param operator The operator that is being executed.
     * @param arguments List
     */
    public void process(PDFOperator operator, List arguments) {
        COSNumber a = (COSNumber) arguments.get(0);
        COSNumber b = (COSNumber) arguments.get(1);
        COSNumber c = (COSNumber) arguments.get(2);
        COSNumber d = (COSNumber) arguments.get(3);
        COSNumber e = (COSNumber) arguments.get(4);
        COSNumber f = (COSNumber) arguments.get(5);
        Matrix textMatrix = new Matrix();
        textMatrix.setValue(0, 0, a.floatValue());
        textMatrix.setValue(0, 1, b.floatValue());
        textMatrix.setValue(1, 0, c.floatValue());
        textMatrix.setValue(1, 1, d.floatValue());
        textMatrix.setValue(2, 0, e.floatValue());
        textMatrix.setValue(2, 1, f.floatValue());
        context.setTextMatrix(textMatrix);
        context.setTextLineMatrix(textMatrix.copy());
    }
}
