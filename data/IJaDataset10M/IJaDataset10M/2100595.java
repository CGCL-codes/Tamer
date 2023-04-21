package org.ufacekit.ui.viewers;

public class LabelConverter<ModelElement> {

    public String getText(ModelElement element) {
        return element.toString();
    }

    public void dispose() {
    }
}
