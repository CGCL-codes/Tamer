package sce.swt.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

public class SideButtonHelper<T extends TabView> {

    public interface SideButtonInterface<T extends TabView> {

        String getName();

        String getFriendlyName();

        void run(T view);
    }

    public Button[] createButtons(Group group, final T view, SideButtonInterface<T>[] sideButtons) {
        Button[] buttons = new Button[sideButtons.length];
        for (int i = 0; i < sideButtons.length; i++) {
            GridData gd = new GridData();
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalAlignment = SWT.FILL;
            final SideButtonInterface<T> sideButton = sideButtons[i];
            buttons[i] = new Button(group, SWT.PUSH);
            buttons[i].setLayoutData(gd);
            buttons[i].setText(sideButton.getFriendlyName());
            buttons[i].setData(SideButtonInterface.class.getName(), sideButton);
            buttons[i].setVisible(true);
            buttons[i].addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    sideButton.run(view);
                }
            });
        }
        group.pack();
        group.layout();
        group.redraw();
        return buttons;
    }
}
