package ch.HaagWeirich.Agenda;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import javax.swing.JPanel;
import ch.rgw.swingtools.SwingHelper;
import ch.rgw.tools.Log;
import ch.rgw.tools.TimeTool;

/**
 * @author gerry
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class ViewMonth extends JPanel {

    static final String Version() {
        return "1.0.0";
    }

    ClientWindow frame;

    final String frei = AgendaEntry.TerminTypes[AgendaEntry.FREI];

    final String reserviert = AgendaEntry.TerminTypes[AgendaEntry.RESERVIERT];

    String[][] fields = new String[8][8];

    double xWidth, hWidth;

    ViewMonth(ClientWindow f) {
        super();
        frame = f;
        frame.mAnzeigeOptionen.setEnabled(false);
        addMouseListener(new mListen());
    }

    public void paint(Graphics g) {
        double w = getWidth();
        double h = getHeight();
        xWidth = w / 7;
        hWidth = h / 7;
        int xOff = (int) Math.round(xWidth);
        int yOff = (int) Math.round(hWidth);
        int y = yOff;
        int x = xOff;
        int th = (int) Math.round(hWidth - 4);
        Font fnt = g.getFont().deriveFont((float) hWidth / 2);
        g.setFont(fnt);
        FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
        for (int i = 0; i < 8; i++) {
            g.drawLine(0, y, 7 * xOff, y);
            y += yOff;
            g.drawLine(x, 0, x, 7 * yOff);
            x += xOff;
        }
        for (int i = 0; i < 7; i++) {
            String tx = TimeTool.wdays[i];
            int dx = (int) Math.round(i * xWidth);
            int dw = xOff;
            int dh = yOff;
            int dy = th / 2;
            SwingHelper.writeCentered(g, tx, dx, dy, dw, dh, frc);
        }
        fnt = fnt.deriveFont((float) hWidth);
        g.setFont(fnt);
        TimeTool tt = new TimeTool(Agenda.actDate);
        tt.setFirstDayOfWeek(TimeTool.SUNDAY);
        tt.setMinimalDaysInFirstWeek(1);
        int last = tt.getActualMaximum(TimeTool.DAY_OF_MONTH);
        tt.add(TimeTool.DAY_OF_MONTH, -tt.get(TimeTool.DAY_OF_MONTH) + 1);
        for (int i = 0; i < last; i++) {
            int hField = tt.get(TimeTool.DAY_OF_WEEK) - 1;
            int vField = tt.get(TimeTool.WEEK_OF_MONTH);
            int dx = (int) Math.round(hField * xOff);
            int dy = (int) Math.round(vField * yOff);
            String dat = tt.toString(TimeTool.DATE_COMPACT);
            fields[hField][vField] = dat;
            AgendaEntry[] tag = AgendaEntry.loadDay(dat, Agenda.getSelectedMands());
            int off1 = 0;
            int off2 = 0;
            int start = 0;
            int end = tag.length;
            double ppm = (xWidth - 4) / (1440 - off1 - off2);
            for (int t = start; t < end; t++) {
                Color bck = Color.RED;
                AgendaEntry ae = tag[t];
                if (ae.Typ.equals(reserviert)) {
                    bck = Color.BLACK;
                } else if (ae.Typ.equals(AgendaEntry.TerminTypes[AgendaEntry.FREI])) {
                    bck = Color.GREEN;
                }
                int ax = 2 + (int) Math.round((ae.Beginn - off1) * ppm);
                int ay = 2;
                int aw = (int) Math.round(ae.Dauer * ppm);
                g.setColor(bck);
                g.fillRect(dx + ax, dy + ay, aw, th);
                g.setColor(Color.BLACK);
            }
            g.setXORMode(Color.ORANGE);
            SwingHelper.writeCentered(g, Integer.toString(i + 1), dx, dy + th, xOff, yOff, frc);
            g.setPaintMode();
            tt.addHours(24);
        }
    }

    class mListen extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            Point pt = me.getPoint();
            int hField = (int) Math.floor(pt.x / xWidth);
            int vField = (int) Math.floor(pt.y / hWidth);
            Agenda.log.log("click field x:" + pt.x + ",y=:" + pt.y + ",h:" + hField + " v:" + vField, Log.DEBUGMSG);
            String dat = fields[hField][vField];
            frame.cal.setDate(new TimeTool(dat));
        }
    }
}
