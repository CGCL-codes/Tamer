package com.peterhi.client.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.MessageBox;
import com.peterhi.client.Application;
import com.peterhi.client.ExceptionListener;
import com.peterhi.client.ui.widgets.DockPanel;
import com.peterhi.client.ui.widgets.PanelSide;
import com.peterhi.client.ui.widgets.MainWindow;
import com.peterhi.client.ui.widgets.Whiteboard;
import com.peterhi.client.ui.widgets.whiteboard.Line;
import com.peterhi.client.ui.widgets.whiteboard.Oval;
import com.peterhi.client.ui.widgets.whiteboard.Pixels;
import com.peterhi.client.ui.widgets.whiteboard.Poly;
import com.peterhi.client.ui.widgets.whiteboard.Rectangle;
import com.peterhi.client.ui.widgets.whiteboard.Text;
import com.peterhi.client.ui.widgets.whiteboard.editors.FreeformEditor;
import com.peterhi.util.Property;
import static com.peterhi.util.Property.*;

public class ClientWindow extends MainWindow {

    private DockPanel dockPanel;

    @Override
    public void init() {
        getShell().setText("PeterHi Client");
        getShell().setLayout(new FillLayout());
        dockPanel = new DockPanel(getShell(), SWT.NONE);
        PropertyViewPanel vp = (PropertyViewPanel) dockPanel.add(PropertyViewPanel.class, PanelSide.BottomRight, "Properties", null);
        WhiteboardPanel w = (WhiteboardPanel) dockPanel.add(WhiteboardPanel.class, PanelSide.Center, "Whiteboard", null);
        WhiteboardExplorerPanel w2 = (WhiteboardExplorerPanel) dockPanel.add(WhiteboardExplorerPanel.class, PanelSide.UpperRight, "Explorer", null);
        w2.setWhiteboard(w.getWhiteboard());
        w2.setPropertyPanel(vp.getPropertyPanel());
        Whiteboard wb = w.getWhiteboard();
        wb.setEditor(FreeformEditor.class, null);
        try {
            Text t = (Text) wb.create(Text.class);
            t.set(Name, "Me");
            t.set(Text, "XXXXXReturns the character offset for the specified point. For a typical character, the trailing argument will be filled in to indicate whether the point is closer to the leading edge (0) or the trailing edge (1). When the point is over a cluster composed of multiple characters, the trailing argument will be filled with the position of the character in the cluster that is closest to the point.");
            t.set(Width, 500);
            t.set(StrokeWidth, 1);
            t.set(Property.Font, new FontData("Arial", 12, SWT.NORMAL));
            t.set(Align, com.peterhi.client.ui.widgets.whiteboard.Text.JUSTIFY);
            t.set(Fill, new RGB(255, 0, 0));
            int height = (Integer) t.get(Height);
            int width = (Integer) t.get(Width);
            t.set(Y, -(height / 2));
            t.set(X, -(width / 2));
            t.set(XTranslate, 250);
            t.set(YTranslate, 80);
            t.setAutoUpdate(true);
            w.add(t);
            Rectangle r = (Rectangle) wb.create(Rectangle.class);
            r.set(Name, "Rectangle");
            r.set(XTranslate, 100);
            r.set(YTranslate, 100);
            r.set(Width, 100);
            r.set(Height, 50);
            r.set(Fill, new RGB(255, 0, 0));
            r.set(Stroke, new RGB(0, 255, 0));
            r.set(StrokeWidth, 6);
            r.setAutoUpdate(true);
            w.add(r);
            Oval o = (Oval) wb.create(Oval.class);
            o.set(Name, "Oval");
            o.set(XTranslate, 200);
            o.set(YTranslate, 200);
            o.set(Width, 100);
            o.set(Height, 100);
            o.set(Fill, new RGB(255, 0, 255));
            o.set(Stroke, new RGB(0, 255, 255));
            o.set(StrokeWidth, 2);
            o.setAutoUpdate(true);
            w.add(o);
            Line l = (Line) wb.create(Line.class);
            l.set(Name, "Line");
            l.set(XTranslate, 150);
            l.set(YTranslate, 150);
            l.set(X2, 50);
            l.set(Y2, 50);
            l.set(StrokeWidth, 3);
            l.setAutoUpdate(true);
            w.add(l);
            Poly po = (Poly) wb.create(Poly.class);
            po.set(Name, "polyline");
            po.set(IsLine, true);
            po.set(Points, new int[] { 240, 240, 290, 290, 310, 287 });
            po.set(Fill, new RGB(128, 129, 255));
            po.set(Stroke, new RGB(255, 0, 0));
            po.set(StrokeWidth, 2);
            po.setAutoUpdate(true);
            w.add(po);
            Poly po2 = (Poly) wb.create(Poly.class);
            po2.set(Name, "polygon");
            po2.set(IsLine, false);
            po2.set(Points, new int[] { 210, 210, 250, 250, 270, 207 });
            po2.set(Fill, new RGB(128, 129, 255));
            po2.set(StrokeWidth, 2);
            po2.setAutoUpdate(true);
            w.add(po2);
            Pixels pi = (Pixels) wb.create(Pixels.class);
            pi.set(Name, "Pixels");
            pi.setPoints(new short[] { 300, 300, 301, 301, 302, 302, 303, 303, 304, 304 });
            pi.set(StrokeWidth, 5);
            pi.setAutoUpdate(true);
            w.add(pi);
            vp.set(t);
            vp.refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Application.addExceptionListener(new ExceptionListener() {

            @Override
            public void thrown(Throwable ex) {
                ex = Application.peel(ex);
                ex.printStackTrace();
                String msg;
                if (ex instanceof NullPointerException) {
                    msg = "Value cannot be null.";
                } else if (ex instanceof NumberFormatException) {
                    msg = "Value is invalid.";
                } else {
                    msg = "Unknown error occurred.";
                }
                MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
                mb.setText("Error");
                mb.setMessage(msg + ", " + ex.toString());
                mb.open();
            }
        });
    }
}
