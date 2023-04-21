package de.fessermn.framework.rcp.guibuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * The Class LabelBuilder.
 */
public class LabelBuilder {

    private final Label label;

    public LabelBuilder(Composite parent, String text) {
        this.label = new Label(parent, SWT.WRAP);
        this.label.setText(text);
    }

    public Label build() {
        return this.label;
    }

    public LabelBuilder setFont(Font font) {
        this.label.setFont(font);
        return this;
    }
}
