package net.ar.guia.own.implementation;

import net.ar.guia.own.interfaces.*;
import net.ar.guia.own.interfaces.listeners.*;

public class DefaultTextAreaComponent extends DefaultVisualComponent implements TextAreaComponent {

    public DefaultTextAreaComponent() {
        setModel(new DefaultTextArea(getListenerManager()));
        setDocument(new DefaultTextDocument(getListenerManager()));
    }

    public DefaultTextAreaComponent(int aColumns, int aRows) {
        this();
        setColumns(aColumns);
        setRows(aRows);
    }

    public TextArea getTextAreaModel() {
        return (TextArea) model;
    }

    public int getColumns() {
        return getTextAreaModel().getColumns();
    }

    public TextDocument getDocument() {
        return getTextAreaModel().getDocument();
    }

    public int getRows() {
        return getTextAreaModel().getRows();
    }

    public void setColumns(int aColumns) {
        getTextAreaModel().setColumns(aColumns);
    }

    public void setDocument(TextDocument aDocument) {
        getTextAreaModel().setDocument(aDocument);
    }

    public void setRows(int aRows) {
        getTextAreaModel().setRows(aRows);
    }

    public void addKeyListener(KeyEventListener aKeyListener) {
        addListener(KeyEventListener.class, aKeyListener);
    }

    public void removeKeyListener(KeyEventListener aKeyListener) {
        removeListener(aKeyListener);
    }
}
