package de.humanfork.treemerge.treeeditor;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

public class ActiveAnnotation {

    private Annotation annotation;

    private IAnnotationModel model;

    public ActiveAnnotation(Annotation annotation, IAnnotationModel model) {
        super();
        this.annotation = annotation;
        this.model = model;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public IAnnotationModel getModel() {
        return model;
    }

    public void dispose() {
        this.annotation = null;
        this.model = null;
    }
}
