package kr.ac.ssu.imc.durubi.report.viewer.components.tables;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import kr.ac.ssu.imc.durubi.report.viewer.*;
import kr.ac.ssu.imc.durubi.report.viewer.components.*;

public class DRTableLine extends JComponent {

    DRLinePoint start, end;

    boolean isinclude;

    int thick = 1, lineshape = 0;

    float type[][] = { { 1, 0, 1, 0 }, { 5, 5, 5, 5 }, { 2, 5, 5, 5 } };

    int Ca = 255, Cr = 100, Cg = 100, Cb = 100;

    Color lineColor;

    public DRTableLine() {
        int i;
        start = new DRLinePoint();
        end = new DRLinePoint();
        isinclude = false;
        lineColor = Color.black;
    }

    public void toInt() {
        start.copyint();
        end.copyint();
    }

    public int getr() {
        return lineColor.getRed();
    }

    public int getg() {
        return lineColor.getGreen();
    }

    public int getb() {
        return lineColor.getBlue();
    }

    public void draw(Graphics g, DRTableLine tline, int gcx1, int gcy1, int gcx2, int gcy2) {
        Graphics2D gc = (Graphics2D) g;
        Stroke oldstroke = gc.getStroke();
        Stroke stroke = new BasicStroke(tline.thick, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, tline.type[tline.lineshape], 0);
        if (tline.thick == 1) stroke = new BasicStroke(tline.thick * .5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, tline.type[tline.lineshape], 0);
        gc.setStroke(stroke);
        gc.setPaint(tline.lineColor);
        if (tline.thick == 0) {
            return;
        }
        Line2D e = new Line2D.Double(gcx1, gcy1, gcx2, gcy2);
        gc.draw(e);
        gc.setStroke(oldstroke);
    }
}
