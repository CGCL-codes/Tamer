package org.nightlabs.calendar.ui.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.nightlabs.calendar.ui.monthviewer.MonthItemViewer;

public class TestView extends ViewPart {

    MonthItemViewer viewer;

    @Override
    public void createPartControl(final Composite parent) {
        final Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout());
        viewer = new MonthItemViewer(comp, SWT.NONE | MonthItemViewer.RED_WEEKEND);
        viewer.setCalendarItemProvider(new RandomCalendarItemProvider());
        viewer.refresh();
        Text text = new Text(comp, SWT.BORDER);
        text.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                if (comp.getBackground().equals(Display.getDefault().getSystemColor(SWT.COLOR_RED))) {
                    comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
                } else {
                    comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                }
            }
        });
    }

    @Override
    public void setFocus() {
        System.out.println(viewer.forceFocus());
    }
}
