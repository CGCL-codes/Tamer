package com.gft.larozanam.client.componentes.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBoxBase;

public abstract class NumberBox<E extends Number> extends ValueBoxBase<E> implements LeafValueEditor<E> {

    public NumberBox(Renderer<E> renderer, Parser<E> parser) {
        super(Document.get().createTextInputElement(), renderer, parser);
        this.setAlignment(TextAlignment.RIGHT);
    }
}
