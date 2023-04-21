package org.pdfclown.documents.contents.objects;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.contents.ContentScanner.GraphicsState;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfNumber;
import org.pdfclown.objects.PdfReal;

/**
  'Append a straight line segment from the current point' operation [PDF:1.6:4.4.1].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.2, 02/04/12
*/
@PDF(VersionEnum.PDF10)
public final class DrawLine extends Operation {

    public static final String Operator = "l";

    /**
    @param point Final endpoint.
  */
    public DrawLine(Point2D point) {
        this(point.getX(), point.getY());
    }

    /**
    @param pointX Final endpoint X.
    @param pointY Final endpoint Y.
  */
    public DrawLine(double pointX, double pointY) {
        super(Operator, PdfReal.get(pointX), PdfReal.get(pointY));
    }

    public DrawLine(List<PdfDirectObject> operands) {
        super(Operator, operands);
    }

    /**
    Gets the final endpoint.
  */
    public Point2D getPoint() {
        return new Point2D.Double(((PdfNumber<?>) operands.get(0)).getDoubleValue(), ((PdfNumber<?>) operands.get(1)).getDoubleValue());
    }

    @Override
    public void scan(GraphicsState state) {
        Path2D pathObject = (Path2D) state.getScanner().getRenderObject();
        if (pathObject != null) {
            Point2D point = getPoint();
            pathObject.lineTo(point.getX(), point.getY());
        }
    }

    /**
    Sets the final endpoint.

    @see #getPoint()
  */
    public void setPoint(Point2D value) {
        operands.set(0, PdfReal.get(value.getX()));
        operands.set(1, PdfReal.get(value.getY()));
    }
}
