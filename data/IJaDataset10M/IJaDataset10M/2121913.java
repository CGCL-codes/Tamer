package net.sf.jmoney.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;

public class TextControlWithSimpleTextbox extends TextComposite {

    protected Text textbox;

    public TextControlWithSimpleTextbox(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());
        textbox = new Text(this, SWT.NONE);
    }

    @Override
    public String getText() {
        return textbox.getText();
    }

    @Override
    public void setText(String text) {
        textbox.setText(text);
    }

    @Override
    public void rememberChoice() {
    }

    @Override
    public void init(IMemento memento) {
    }

    @Override
    public void saveState(IMemento memento) {
    }
}
