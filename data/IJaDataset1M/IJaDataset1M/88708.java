package de.objectcode.time4u.client.views.parts;

import java.util.Iterator;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class DayGraphFigure extends Figure {

    Font m_hourFont;

    Dimension m_hourSize;

    Font m_minuteFont;

    Dimension m_minuteSize;

    public DayGraphFigure() {
        setLayoutManager(new DayGraphLayout());
    }

    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        Dimension size = super.getPreferredSize(wHint, hHint);
        size.height = 24 * 60;
        return size;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        if (m_hourFont == null || m_minuteFont == null) initFonts();
        Rectangle r = getBounds();
        int labelWidth = m_hourSize.width + m_minuteSize.width;
        for (int hour = 0; hour <= 24; hour++) {
            int y = hour * 60;
            graphics.setForegroundColor(ColorConstants.lightGray);
            graphics.setLineWidth(3);
            graphics.drawLine(r.x + 10, r.y + y, r.x + r.width, r.y + y);
            graphics.setLineWidth(1);
            graphics.drawLine(r.x + labelWidth + 10, r.y + y + 30, r.x + r.width, r.y + y + 30);
            graphics.setForegroundColor(ColorConstants.black);
            graphics.setFont(m_hourFont);
            graphics.drawString(String.valueOf(hour), m_hourSize.width - FigureUtilities.getTextWidth(String.valueOf(hour), m_hourFont) + 5, r.y + y + (60 - m_hourSize.height) / 2);
            graphics.setFont(m_minuteFont);
            graphics.drawString("00", m_hourSize.width + 10, r.y + y);
            graphics.drawString("30", m_hourSize.width + 10, r.y + y + 30);
        }
    }

    protected void initFonts() {
        if (m_hourFont == null) {
            FontData fd[] = getFont().getFontData();
            for (int i = 0; i < fd.length; i++) {
                fd[i].setHeight(24);
            }
            m_hourFont = new Font(Display.getCurrent(), fd);
        }
        m_hourSize = FigureUtilities.getStringExtents("22", m_hourFont);
        if (m_minuteFont == null) {
            FontData fd[] = getFont().getFontData();
            for (int i = 0; i < fd.length; i++) {
                fd[i].setHeight(10);
            }
            m_minuteFont = new Font(Display.getCurrent(), fd);
        }
        m_minuteSize = FigureUtilities.getStringExtents("22", m_minuteFont);
    }

    private class DayGraphLayout extends XYLayout {

        @Override
        public void layout(IFigure parent) {
            if (m_hourFont == null || m_minuteFont == null) initFonts();
            int labelWidth = m_hourSize.width + m_minuteSize.width;
            Rectangle clientArea = parent.getClientArea();
            Iterator children = parent.getChildren().iterator();
            Point offset = clientArea.getLocation();
            IFigure f;
            while (children.hasNext()) {
                f = (IFigure) children.next();
                Rectangle bounds = (Rectangle) getConstraint(f);
                if (bounds == null) continue;
                bounds.x = labelWidth + 15;
                bounds.width = clientArea.width - labelWidth - 20;
                bounds = bounds.getTranslated(offset);
                f.setBounds(bounds);
            }
        }
    }
}
