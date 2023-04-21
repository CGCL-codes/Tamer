package org.dyno.visual.swing.widgets;

import java.awt.Component;
import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;

@SuppressWarnings("unchecked")
public class JEditorPaneAdapter extends J2DTextComponentAdapter {

    public JEditorPaneAdapter() {
        super(null);
    }

    @Override
    protected JTextComponent createTextComponent() {
        return new JEditorPane();
    }

    @Override
    protected Component newWidget() {
        return new JEditorPane();
    }

    @Override
    public Class getWidgetClass() {
        return JEditorPane.class;
    }
}
