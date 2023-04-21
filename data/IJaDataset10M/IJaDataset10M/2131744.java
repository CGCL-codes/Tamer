package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.geom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for translating vertical custom gridlines
 * 
 * @author: Dennis Malmstr�m
 */
public class CoMoveYCustomGridLineTool extends CoMoveCustomGridLineTool {

    protected Line2D createLine(double pos, double span) {
        return new Line2D.Double(0, pos, span, pos);
    }

    protected void doit() {
        final double pos = m_line.getY1();
        if (pos != m_pos0) {
            CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare(m_view.getMutableCustomGrid(), Double.NaN, Double.NaN, m_pos0, pos);
            m_editor.getCommandExecutor().doit(CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null);
        }
    }

    protected double getPos(java.awt.geom.Point2D p) {
        return p.getY();
    }

    protected void updateLine(double pos) {
        m_line.setLine(0, pos, m_line.getX2(), pos);
    }

    protected void xorDrawText(Graphics2D g) {
        double pos = m_line.getY1();
        if (pos <= 0) return;
        if (pos >= m_view.getHeight()) return;
        float x = 10 + (float) m_pos.getX() + g.getFont().getSize();
        float y = (float) m_pos.getY();
        g.translate(x, y);
        double s = 1 / com.bluebrim.base.shared.CoBaseUtilities.getXScale(g.getTransform());
        g.scale(s, s);
        g.drawString(CoLengthUnitSet.format(pos, CoLengthUnit.LENGTH_UNITS), 0, 0);
        g.translate(-x, -y);
    }

    public CoMoveYCustomGridLineTool(CoTool previousTool, CoLayoutEditor pageItemEditor, CoShapePageItemView v, double pos, double span) {
        super(previousTool, pageItemEditor, v, pos, span);
    }
}
