package net.sf.rcpforms.experimenting.rcp_base.form;

import net.sf.rcpforms.tablesupport.tables.RCPTableFormToolkit;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Section;

/**
 * <code><i>RCPTableFormToolkit2</i></code> kann einen etwas sch�neren
 * Horizontalen Separator machen als das Standardtoolkit. Sleak :-)
 * 
 * <p>
 */
public class RCPTableFormToolkit2 extends RCPTableFormToolkit {

    private MyPaintListener s_paintListener;

    public RCPTableFormToolkit2(final Display display) {
        super(display);
        s_paintListener = new MyPaintListener();
    }

    @Override
    public Label createSeparator(final Composite parent, final int style) {
        final FormColors colors = getColors();
        final Label label = new Label(parent, SWT.NONE);
        s_paintListener.m_foreground = colors.getBorderColor();
        s_paintListener.m_background = colors.getBackground();
        label.addPaintListener(s_paintListener);
        return label;
    }

    private class MyPaintListener implements PaintListener {

        Color m_foreground = null;

        Color m_background = null;

        public void paintControl(final PaintEvent event) {
            final GC gc = event.gc;
            final int xm = getFadeFraction(event.width);
            gc.setForeground(m_foreground);
            gc.setBackground(m_background);
            gc.fillGradientRectangle(event.x, event.y, xm, event.height, false);
            gc.fillGradientRectangle(event.x + xm, event.y, event.width - xm, event.height, false);
        }
    }

    private static int getFadeFraction(final int width) {
        int xm = (width * 15) / 100;
        if (xm > 20) {
            xm = 20;
        }
        return xm;
    }

    /**
	 * Creats the composite that can server as a separator between various parts
	 * of a form. Separator height should be controlled by setting the height
	 * hint on the layout data for the composite.
	 * 
	 * @param parent
	 *            the separator parent
	 * @return the separator widget
	 */
    @Override
    public Composite createCompositeSeparator(final Composite parent) {
        final FormColors colors = getColors();
        final Composite composite = new Composite(parent, Window.getDefaultOrientation());
        composite.addListener(SWT.Paint, new Listener() {

            public void handleEvent(final Event e) {
                if (composite.isDisposed()) return;
                final Rectangle bounds = composite.getBounds();
                final GC gc = e.gc;
                gc.setBackground(colors.getColor(IFormColors.SEPARATOR));
                if (colors.getBackground() != null) gc.setForeground(colors.getBackground());
                final int xm = getFadeFraction(bounds.width);
                gc.fillGradientRectangle(0, 0, xm, bounds.height, false);
                gc.setForeground(colors.getColor(IFormColors.SEPARATOR));
                if (colors.getBackground() != null) gc.setBackground(colors.getBackground());
                gc.fillGradientRectangle(xm, 0, bounds.width - xm, bounds.height, false);
            }
        });
        if (parent instanceof Section) ((Section) parent).setSeparatorControl(composite);
        return composite;
    }
}
