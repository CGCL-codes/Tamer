package bfpl.gui.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.text.AttributedString;
import bfpl.gui.BfplGuiModel;
import bfpl.gui.BfplPainter;
import bfpl.gui.ZlsGuiModel;

public class ZuglaufstellenPainter extends BfplPainter {

    public ZuglaufstellenPainter(final ZlsGuiModel m) {
        model = m;
    }

    @Override
    public void paint(final Graphics2D gr) {
        ZlsGuiModel model = getModel();
        BfplGuiModel bm = model.getBfplGuiModel();
        Dimension size = bm.getSize();
        float firstGleis = model.getZuglaufstellenOffset() + BfplGuiModel.MARGIN;
        gr.setColor(Color.BLACK);
        gr.drawString(model.getZlsName(), firstGleis, 20);
        Line2D.Float l = new Line2D.Float(firstGleis, BfplGuiModel.HEADER_HEIGTH, firstGleis, size.height);
        gr.draw(l);
        for (int i = 0; i < model.getGleisCount(); i++) {
            String gl = model.getGleisName(i);
            AttributedString str = new AttributedString(gl);
            str.addAttribute(TextAttribute.SIZE, ZlsGuiModel.GLEIS_DIFF - 1);
            str.addAttribute(TextAttribute.TRANSFORM, AffineTransform.getQuadrantRotateInstance(-1));
            float x = firstGleis + (i + 0.5f) * ZlsGuiModel.GLEIS_DIFF;
            gr.drawString(str.getIterator(), x, BfplGuiModel.HEADER_HEIGTH);
        }
        gr.setColor(Color.GRAY);
        for (int i = 1; i < model.getGleisCount(); i++) {
            float x = firstGleis + i * ZlsGuiModel.GLEIS_DIFF;
            Line2D.Float gll = new Line2D.Float(x, BfplGuiModel.HEADER_HEIGTH, x, size.height);
            gr.draw(gll);
        }
    }

    protected ZlsGuiModel getModel() {
        return model;
    }

    protected void setModel(final ZlsGuiModel model) {
        this.model = model;
    }

    private ZlsGuiModel model;
}
