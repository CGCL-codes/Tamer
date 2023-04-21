package org.fframework.visibles.xhtml;

import org.fframework.core.annotation.Visible;

@Visible
public class SelectInput {

    @Visible
    protected final String name;

    @Visible
    protected final SelectInputOption[] options;

    public SelectInput(String name, SelectInputOption... options) {
        this.name = name;
        this.options = options;
    }
}
