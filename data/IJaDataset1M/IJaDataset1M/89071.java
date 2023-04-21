package com.bluebrim.text.impl.client;

import java.awt.*;
import javax.swing.*;
import com.bluebrim.text.impl.client.swing.*;

public class CoStyledTextEditorPane extends CoAbstractTextEditorPane {

    private static class StyledTextEditor extends CoStyledTextEditor implements ScaleAndMargins {

        private double m_scale = 1.0;

        private int m_margin = 20;

        private CoHorizontalMarginController m_marginController;

        private class OneColumnGeometry extends com.bluebrim.text.shared.CoAbstractColumnGeometry {

            private com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF m_column;

            public OneColumnGeometry(float w, float h) {
                w = w - 2 * m_margin;
                m_column = new com.bluebrim.text.shared.CoRectangularColumn(Math.round(m_margin / m_scale), 0, Math.round(w / m_scale), Integer.MAX_VALUE);
            }

            public int getColumnCount() {
                return 1;
            }

            public com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF getColumn(int index) {
                return m_column;
            }

            public boolean isRectangular() {
                return m_column.isRectangular();
            }
        }

        ;

        public StyledTextEditor(com.bluebrim.text.shared.CoStyledDocumentIF doc) {
            super(doc);
            setColumn();
            m_marginController = new CoHorizontalMarginController(this);
        }

        public void setScale(double scale) {
            if (m_scale == scale) return;
            m_scale = scale;
            setColumn();
            revalidate();
        }

        public void setHorizontalMargin(int m) {
            if (m_margin == m) return;
            m_margin = m;
            setColumn();
            revalidate();
        }

        public double getScale() {
            return m_scale;
        }

        public int getHorizontalMargin() {
            return (int) m_margin;
        }

        public Dimension getMinimumSize() {
            return new Dimension(10, 10);
        }

        public void setDocument(javax.swing.text.Document doc) {
            super.setDocument(doc);
            revalidate();
        }

        private void setColumn() {
            int w = getWidth();
            int h = getHeight();
            setGeometry(new OneColumnGeometry(w, h), null, null);
        }

        public void reshape(int x, int y, int w, int h) {
            int W = getWidth();
            super.reshape(x, y, w, h);
            setColumn();
            if (W != w) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        revalidate();
                    }
                });
            }
        }

        public void paint(Graphics g) {
            ((Graphics2D) g).scale(m_scale, m_scale);
            super.paint(g);
            ((Graphics2D) g).scale(1.0 / m_scale, 1.0 / m_scale);
            m_marginController.paint(g);
        }

        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.height = (int) (((com.bluebrim.text.shared.swing.CoSectionView) getUI().getRootView(this).getView(0)).getPreferredHeight() * m_scale);
            return size;
        }
    }

    ;

    public CoStyledTextEditorPane() {
        super();
    }

    protected ScaleAndMargins createScaleAndMargin() {
        return (ScaleAndMargins) m_textEditor;
    }

    protected CoAbstractTextEditor createTextEditor(com.bluebrim.text.shared.CoStyledDocumentIF doc) {
        return new StyledTextEditor(doc);
    }

    public CoStyledTextEditorPane(com.bluebrim.gui.client.CoUserInterfaceBuilder b, com.bluebrim.menus.client.CoMenuBuilder mb) {
        super(b, mb);
    }
}
