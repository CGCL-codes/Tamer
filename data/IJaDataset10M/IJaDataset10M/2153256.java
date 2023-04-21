package de.jassda.modules.trace.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JEditorPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class JassdaEditorPane extends JEditorPane {

    private boolean editorContentHasBeenChanged;

    public JassdaEditorPane() {
        super();
        editorContentHasBeenChanged = false;
        setEditorKit(new MyEditorKit());
    }

    public void setContentChangedStatus(boolean status) {
        editorContentHasBeenChanged = status;
    }

    public boolean getContentChangedStatus() {
        return editorContentHasBeenChanged;
    }

    class MyEditorKit extends StyledEditorKit {

        public ViewFactory getViewFactory() {
            return new MyRTFViewFactory();
        }
    }

    class MyRTFViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new MyParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new MySectionView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    class MySectionView extends BoxView {

        public MySectionView(Element e, int axis) {
            super(e, axis);
        }

        public void paintChild(Graphics g, Rectangle r, int n) {
            if (n > 0) {
                MyParagraphView child = (MyParagraphView) this.getView(n - 1);
                int shift = child.shift + child.childCount;
                MyParagraphView current = (MyParagraphView) this.getView(n);
                current.shift = shift;
            }
            super.paintChild(g, r, n);
        }
    }

    class MyParagraphView extends javax.swing.text.ParagraphView {

        public int childCount;

        public int shift = 0;

        public MyParagraphView(Element e) {
            super(e);
            short top = 0;
            short left = 20;
            short bottom = 0;
            short right = 0;
            this.setInsets(top, left, bottom, right);
        }

        public void paint(Graphics g, Shape a) {
            childCount = this.getViewCount();
            super.paint(g, a);
        }

        public void paintChild(Graphics g, Rectangle r, int n) {
            super.paintChild(g, r, n);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(Integer.toString(shift + n + 1), r.x - 20, r.y + r.height - 3);
        }
    }
}
