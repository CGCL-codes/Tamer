package org.aspencloud.cdatetime.examples;

import java.util.Calendar;
import java.util.Date;
import org.aspencloud.cdatetime.Body;
import org.aspencloud.cdatetime.CDT;
import org.aspencloud.cdatetime.CDateTime;
import org.aspencloud.cdatetime.CDateTimeBuilder;
import org.aspencloud.cdatetime.CDateTimePainter;
import org.aspencloud.cdatetime.Header;
import org.aspencloud.v.VButton;
import org.aspencloud.v.VControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class CDTPainterExample02 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("CDateTime");
        GridLayout layout = new GridLayout();
        layout.marginHeight = 20;
        layout.marginWidth = 20;
        shell.setLayout(layout);
        CDateTimeBuilder builder = CDateTimeBuilder.getCompact();
        builder.setHeader(Header.MonthPrev(), Header.DateNow(), Header.MonthNext(), Header.Month(), Header.Year(), Header.Time());
        builder.setBody(Body.Days().spacedAt(0), Body.Months().spacedAt(0), Body.Years().spacedAt(0), Body.Time());
        CDateTimePainter painter = new CDateTimePainter() {

            @Override
            protected void paintDayButtonBackground(VControl control, Event e) {
                if (((VButton) control).getSelection()) {
                    super.paintDayButtonBackground(control, e);
                    control.setAlpha(e.gc);
                } else {
                    super.paintDayButtonBackground(control, e);
                    control.setAlpha(e.gc, 100);
                }
                Date date = (Date) control.getData(CDT.Key.Date);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                Rectangle bounds = control.getBounds();
                if (!isActive(control)) {
                    e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_CYAN));
                } else {
                    e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLUE));
                }
                if (indexOf(control) % 7 == 0) {
                    int half = (bounds.width - 2) / 2;
                    e.gc.fillRectangle(bounds.x + 1 + half, bounds.y + 1, bounds.width, bounds.height - 2);
                    e.gc.fillArc(bounds.x + 1, bounds.y + 1, half * 2, bounds.height - 2, 90, 180);
                } else if (indexOf(control) % 7 == 6) {
                    int half = (bounds.width) / 2;
                    e.gc.fillRectangle(bounds.x, bounds.y + 1, half, bounds.height - 2);
                    e.gc.fillArc(bounds.x, bounds.y + 1, bounds.width - 1, bounds.height - 2, 90, -180);
                } else {
                    e.gc.fillRectangle(bounds.x, bounds.y + 1, bounds.width, bounds.height - 2);
                }
            }
        };
        CDateTime cdt = new CDateTime(shell, CDT.BORDER | CDT.SIMPLE | CDT.DATE_LONG | CDT.TIME_MEDIUM);
        cdt.setBuilder(builder);
        cdt.setPainter(painter);
        cdt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        cdt = new CDateTime(shell, CDT.BORDER | CDT.SIMPLE);
        cdt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        shell.pack();
        Point size = shell.getSize();
        Rectangle screen = display.getMonitors()[0].getBounds();
        shell.setBounds((screen.width - size.x) / 2, (screen.height - size.y) / 2, size.x, size.y);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
