package org.iqual.chaplin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * @author Zbynek Slajchrt
 * @since 24.12.2009 15:46:15
 */
public class FromAnnotationTemplateResolver extends ClassAdapter {

    private final FromAnnotContent fromAnnotContent;

    private boolean found;

    public FromAnnotationTemplateResolver(FromAnnotContent fromAnnot) {
        super(new EmptyVisitor());
        fromAnnotContent = fromAnnot;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String annotClassName, boolean b) {
        if (FieldRedirector.FROM_CTX_ANNOT_DESC.endsWith(annotClassName)) {
            found = true;
            return fromAnnotContent.createAnnotationVisitor();
        } else {
            return null;
        }
    }

    /**
     *
     * @param cls
     * @return true if a template was found
     */
    public synchronized boolean resolve(Class cls) {
        return resolve(cls.getName().replace('.', '/'));
    }

    /**
     *
     * @param className
     * @return true if a template was found
     */
    public synchronized boolean resolve(String className) {
        found = false;
        ClassReader clsReader = ResourceClassReader.newResourceClassReader(className);
        clsReader.accept(this, 0);
        return found;
    }

    public FromAnnotContent getFromAnnotContent() {
        return fromAnnotContent;
    }
}
