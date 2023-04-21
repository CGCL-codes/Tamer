package com.anzsoft.client.XMPP.log;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.TextArea;

public class DebugPanel extends LayoutContainer implements LoggerOuput {

    private final TextArea area;

    private ButtonBar buttonBar;

    public DebugPanel() {
        setLayout(new FitLayout());
        area = new TextArea();
        add(area);
        buttonBar = new ButtonBar();
        Button clearButton = new Button("Clear");
        clearButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                area.setText("");
            }
        });
        buttonBar.add(clearButton);
    }

    public ButtonBar getBar() {
        return buttonBar;
    }

    public void log(final String message) {
        area.setText(area.getText() + message + "\n");
    }
}
