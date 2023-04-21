package co.fxl.gui.swing;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTextField;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;

class SwingTextField extends SwingTextInput<JTextField, ITextField> implements ITextField {

    private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();

    SwingTextField(SwingContainer<JTextField> container) {
        super(container);
    }

    @Override
    public ITextField height(int height) {
        return super.height(height + 1);
    }

    @Override
    public int height() {
        return super.height() - 1;
    }

    @Override
    public ITextField text(String text) {
        if (text == null) text = "";
        setTextOnComponent(text);
        font.updateFont();
        for (IUpdateListener<String> l : updateListeners) l.onUpdate(text);
        return this;
    }

    @Override
    public IUpdateable<String> addUpdateListener(co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
        updateListeners.add(listener);
        return super.addStringUpdateListener(listener);
    }

    void addActionListener(ActionListener actionListener) {
        container.component.addActionListener(actionListener);
    }

    @Override
    public int cursorPosition() {
        return container.component.getCaretPosition();
    }

    @Override
    public ITextField cursorPosition(int position) {
        container.component.setCaretPosition(position);
        container.component.requestFocus();
        return this;
    }
}
