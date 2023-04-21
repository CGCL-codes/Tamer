package org.youchan.mashedpotato.framework.ui.swing;

import javax.swing.JComponent;
import javax.swing.JLabel;
import org.youchan.mashedpotato.framework.model.Setter;

public class Label extends AbstractWidget {

    static {
        initialize(Label.class);
    }

    JLabel label;

    @Override
    public void configure() {
    }

    @Override
    public void dispose() {
    }

    @Setter
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    protected JComponent createComponent() {
        label = new JLabel();
        return label;
    }
}
