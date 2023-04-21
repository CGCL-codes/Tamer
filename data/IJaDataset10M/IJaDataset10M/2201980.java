package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class Snippet140 {

    static Display display;

    static Shell shell;

    static CoolBar coolBar;

    static Menu chevronMenu = null;

    public static void main(String[] args) {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout());
        coolBar = new CoolBar(shell, SWT.FLAT | SWT.BORDER);
        coolBar.setLayoutData(new GridData(GridData.FILL_BOTH));
        ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.WRAP);
        int minWidth = 0;
        for (int j = 0; j < 5; j++) {
            int width = 0;
            ToolItem item = new ToolItem(toolBar, SWT.PUSH);
            item.setText("B" + j);
            width = item.getWidth();
            if (width > minWidth) minWidth = width;
        }
        CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
        coolItem.setControl(toolBar);
        Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point coolSize = coolItem.computeSize(size.x, size.y);
        coolItem.setMinimumSize(minWidth, coolSize.y);
        coolItem.setPreferredSize(coolSize);
        coolItem.setSize(coolSize);
        coolItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                if (event.detail == SWT.ARROW) {
                    CoolItem item = (CoolItem) event.widget;
                    Rectangle itemBounds = item.getBounds();
                    Point pt = coolBar.toDisplay(new Point(itemBounds.x, itemBounds.y));
                    itemBounds.x = pt.x;
                    itemBounds.y = pt.y;
                    ToolBar bar = (ToolBar) item.getControl();
                    ToolItem[] tools = bar.getItems();
                    int i = 0;
                    while (i < tools.length) {
                        Rectangle toolBounds = tools[i].getBounds();
                        pt = bar.toDisplay(new Point(toolBounds.x, toolBounds.y));
                        toolBounds.x = pt.x;
                        toolBounds.y = pt.y;
                        Rectangle intersection = itemBounds.intersection(toolBounds);
                        if (!intersection.equals(toolBounds)) break;
                        i++;
                    }
                    if (chevronMenu != null) chevronMenu.dispose();
                    chevronMenu = new Menu(coolBar);
                    for (int j = i; j < tools.length; j++) {
                        MenuItem menuItem = new MenuItem(chevronMenu, SWT.PUSH);
                        menuItem.setText(tools[j].getText());
                    }
                    pt = coolBar.toDisplay(new Point(event.x, event.y));
                    chevronMenu.setLocation(pt.x, pt.y);
                    chevronMenu.setVisible(true);
                }
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
