package ch.unibe.im2.inkanno.imageProcessing;

import ch.unibe.eindermu.utils.Config;
import ch.unibe.im2.inkanno.InkAnnoAnnotationStructure;
import ch.unibe.inkml.InkTraceViewContainer;

public class ImageTraceDrawer extends TraceDrawVisitor {

    public void visitHook(InkTraceViewContainer container) {
        if (!Config.getMain().getB("no-marking") || (!(container.containsAnnotation(InkAnnoAnnotationStructure.TYPE) && container.getAnnotation(InkAnnoAnnotationStructure.TYPE).equals("Marking")))) {
            super.visitHook(container);
        }
    }
}
