package org.pdfbox.util.operator.pagedrawer;

import java.awt.geom.GeneralPath;
import java.util.List;
import org.pdfbox.cos.COSNumber;
import org.pdfbox.pdfviewer.PageDrawer;
import org.pdfbox.util.PDFOperator;
import org.pdfbox.util.operator.OperatorProcessor;

/**
 * Implementation of content stream operator for page drawer.
 * 
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.2 $
 */
public class MoveTo extends OperatorProcessor {

    /**
     * process : m : Begin new subpath.
     * @param operator The operator that is being executed.
     * @param arguments List
     */
    public void process(PDFOperator operator, List arguments) {
        PageDrawer drawer = (PageDrawer) context;
        COSNumber x = (COSNumber) arguments.get(0);
        COSNumber y = (COSNumber) arguments.get(1);
        drawer.getLineSubPaths().add(drawer.getLinePath());
        GeneralPath newPath = new GeneralPath();
        newPath.moveTo(x.floatValue(), (float) drawer.fixY(x.doubleValue(), y.doubleValue()));
        drawer.setLinePath(newPath);
    }
}
