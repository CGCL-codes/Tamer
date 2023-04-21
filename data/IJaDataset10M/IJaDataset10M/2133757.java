package com.android.ide.eclipse.ddms.views;

import com.android.ddmuilib.ThreadPanel;
import org.eclipse.swt.widgets.Composite;

public class ThreadView extends TableView {

    public static final String ID = "com.android.ide.eclipse.ddms.views.ThreadView";

    private ThreadPanel mPanel;

    public ThreadView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        mPanel = new ThreadPanel();
        mPanel.createPanel(parent);
        setSelectionDependentPanel(mPanel);
        setupTableFocusListener(mPanel, parent);
    }

    @Override
    public void setFocus() {
        mPanel.setFocus();
    }
}
