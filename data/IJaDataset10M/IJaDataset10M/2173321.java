package com.gface.experiments;

import java.text.DateFormatSymbols;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MyListExperiment {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.TOP | SWT.BORDER | SWT.MODELESS);
        shell.setLayout(new FillLayout());
        addList(shell);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    private static void addList(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.verticalSpacing = 0;
        layout.marginLeft = layout.marginRight = 0;
        layout.marginTop = layout.marginBottom = 0;
        composite.setLayout(layout);
        composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] calednar = symbols.getMonths();
        GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.CENTER | GridData.GRAB_HORIZONTAL);
        for (int i = 0; i < 12; i++) {
            final Label l = new Label(composite, SWT.CENTER);
            l.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
            l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            l.setText(calednar[i]);
            l.setLayoutData(gd);
            l.addMouseTrackListener(new MouseTrackListener() {

                public void mouseEnter(MouseEvent e) {
                    l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
                    l.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
                }

                public void mouseExit(MouseEvent e) {
                    l.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
                    l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                }

                public void mouseHover(MouseEvent e) {
                }
            });
        }
    }
}
