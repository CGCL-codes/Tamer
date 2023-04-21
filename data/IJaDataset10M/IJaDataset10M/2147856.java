package net.ar.webonswing.own.adapters;

import javax.swing.*;
import net.ar.guia.own.interfaces.*;

public class JTextAreaAdapter extends JComponentAdapter implements TextAreaComponent, HasListeners {

    protected DocumentAdapter documentAdapter;

    public JTextAreaAdapter() {
        setSwingComponentClass(JTextArea.class);
    }

    public JTextAreaAdapter(JComponent aSwingButton) {
        super(aSwingButton);
        setSwingComponentClass(JTextArea.class);
    }

    public void setWrappedComponent(Object object) {
        super.setWrappedComponent(object);
        documentAdapter = new DocumentAdapter(getJTextArea().getDocument());
    }

    private JTextArea getJTextArea() {
        return (JTextArea) container;
    }

    public int getColumns() {
        return getJTextArea().getColumns();
    }

    public int getRows() {
        return getJTextArea().getRows();
    }

    public String getText() {
        return getJTextArea().getText();
    }

    public void setText(String string) {
        getJTextArea().setText(string);
    }

    public void setColumns(int aColumns) {
        getJTextArea().setColumns(aColumns);
    }

    public void setRows(int aRows) {
        getJTextArea().setRows(aRows);
    }

    public TextDocument getDocument() {
        return documentAdapter;
    }

    public void setDocument(TextDocument aDocument) {
    }
}
