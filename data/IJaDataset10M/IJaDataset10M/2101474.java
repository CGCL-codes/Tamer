package de.mse.mogwai.utils.uieditor.widgets;

import de.mse.mogwai.impl.swing.beans.ExtendedGroupBox;

/**
 *
 * @author  Mirko Sertic
 */
public class GroupBoxWidget extends EditorWidget {

    private ExtendedGroupBox m_delegate;

    public GroupBoxWidget(ExtendedGroupBox paintdelegate) {
        super(paintdelegate);
        this.m_delegate = paintdelegate;
    }

    public boolean supportsChilds() {
        return true;
    }

    public String getText() {
        return this.m_delegate.getText();
    }

    public void setText(String value) {
        this.m_delegate.setText(value);
        this.repaint();
    }

    public String getTypeName() {
        return "GroupBox";
    }
}
