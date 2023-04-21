package co.fxl.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import co.fxl.gui.api.ICheckBox;

class SwingCheckBox extends SwingTextElement<JCheckBox, ICheckBox> implements ICheckBox {

    SwingCheckBox(SwingContainer<JCheckBox> container) {
        super(container);
        container.component.setOpaque(false);
    }

    @Override
    public ICheckBox checked(boolean checked) {
        container.component.setSelected(checked);
        return this;
    }

    @Override
    public ICheckBox editable(boolean editable) {
        container.component.setEnabled(editable);
        return this;
    }

    @Override
    public ICheckBox addUpdateListener(final IUpdateListener<Boolean> listener) {
        container.component.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                listener.onUpdate(container.component.isSelected());
            }
        });
        return this;
    }

    @Override
    protected void setTextOnComponent(String text) {
        container.component.setText(text);
    }

    @Override
    public boolean checked() {
        return container.component.getSelectedObjects() != null;
    }

    @Override
    public String text() {
        return container.component.getText();
    }

    @Override
    public ICheckBox text(String text) {
        setText(text);
        return this;
    }

    @Override
    public boolean editable() {
        return container.component.isEnabled();
    }
}
