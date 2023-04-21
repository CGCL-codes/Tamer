package org.iqual.chaplin;

import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

/**
 * @author Zbynek Slajchrt
 * @since 29.12.2009 20:58:19
 */
public class WrapperAnnotResolver extends ClassAdapter {

    private boolean found;

    public WrapperAnnotResolver() {
        super(new EmptyVisitor());
    }

    @Override
    public AnnotationVisitor visitAnnotation(String annotClassName, boolean b) {
        if (FieldRedirector.WRAPPER_CTX_ANNOT_DESC.endsWith(annotClassName)) {
            found = true;
        }
        return null;
    }

    /**
     * @param cls
     * @return true if a template was found
     */
    public synchronized boolean resolve(Class cls) {
        return resolve(cls.getName().replace('.', '/'));
    }

    /**
     * @param className
     * @return true if a template was found
     */
    public synchronized boolean resolve(String className) {
        ClassReader clsReader = ResourceClassReader.newResourceClassReader(className);
        clsReader.accept(this, 0);
        return found;
    }
}
