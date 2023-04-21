package org.pdfclown.documents.contents.objects;

import java.util.List;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.contents.ContentScanner.GraphicsState;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfNumber;
import org.pdfclown.objects.PdfReal;

/**
  'Set the line width' operation [PDF:1.6:4.3.3].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.4
  @version 0.1.2, 02/04/12
*/
@PDF(VersionEnum.PDF10)
public final class SetLineWidth extends Operation {

    public static final String Operator = "w";

    public SetLineWidth(double value) {
        super(Operator, PdfReal.get(value));
    }

    public SetLineWidth(List<PdfDirectObject> operands) {
        super(Operator, operands);
    }

    public double getValue() {
        return ((PdfNumber<?>) operands.get(0)).getDoubleValue();
    }

    @Override
    public void scan(GraphicsState state) {
        state.setLineWidth(getValue());
    }

    public void setValue(double value) {
        operands.set(0, PdfReal.get(value));
    }
}
