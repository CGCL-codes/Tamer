package nk.visual.bricks.ext;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class BioSequenceStyledEditorKit extends StyledEditorKit {

    private static final long serialVersionUID = -2209903914764376509L;

    ViewFactory defaultFactory = new WrapColumnFactory();

    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    public BioSequenceStyledEditorKit() {
        super();
    }

    class BioSequenceLabelView extends LabelView {

        public BioSequenceLabelView(Element elem) {
            super(elem);
        }

        public int getBreakWeight(int axis, float pos, float len) {
            if (axis == View.X_AXIS) {
                checkPainter();
                int p0 = getStartOffset();
                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
                if (p1 == p0) {
                    return View.BadBreakWeight;
                }
                return View.GoodBreakWeight;
            }
            return super.getBreakWeight(axis, pos, len);
        }
    }

    class WrapColumnFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new BioSequenceLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }
}
