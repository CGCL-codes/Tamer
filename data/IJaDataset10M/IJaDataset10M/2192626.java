package codesheet;

import org.eclipse.jface.text.source.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;

public class DefaultMarkerAnnotationAccess implements IAnnotationAccess, IAnnotationAccessExtension {

    public Object getType(Annotation annotation) {
        return annotation.getType();
    }

    public boolean isMultiLine(Annotation annotation) {
        return true;
    }

    public boolean isTemporary(Annotation annotation) {
        return !annotation.isPersistent();
    }

    public String getTypeLabel(Annotation annotation) {
        return null;
    }

    public int getLayer(Annotation annotation) {
        return 0;
    }

    public void paint(Annotation annotation, GC gc, Canvas canvas, Rectangle bounds) {
        if (annotation instanceof IAnnotationPresentation) {
            IAnnotationPresentation presentation = (IAnnotationPresentation) annotation;
            presentation.paint(gc, canvas, bounds);
            return;
        }
        Object preference = null;
        if (preference != null) {
            Object type = getType(annotation);
            String annotationType = (type == null ? null : type.toString());
            Image image = getImage(annotation, preference, annotationType);
            if (image != null) {
                ImageUtilities.drawImage(image, gc, canvas, bounds, SWT.CENTER, SWT.TOP);
                return;
            }
        }
    }

    public boolean isPaintable(Annotation annotation) {
        Object type = getType(annotation);
        String annotationType = (type == null ? null : type.toString());
        Object preference = null;
        Image image = getImage(annotation, preference, annotationType);
        return image != null;
    }

    public boolean isSubtype(Object o, Object o1) {
        return false;
    }

    public Object[] getSupertypes(Object o) {
        return new Object[0];
    }

    private Image getImage(Annotation annotation, Object pereference, String type) {
        return null;
    }
}
