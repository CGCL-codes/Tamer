package org.avaje.ebean.enhance.agent;

import java.util.ArrayList;
import java.util.HashSet;
import org.avaje.ebean.enhance.asm.AnnotationVisitor;
import org.avaje.ebean.enhance.asm.ClassAdapter;
import org.avaje.ebean.enhance.asm.EmptyVisitor;
import org.avaje.ebean.enhance.asm.MethodAdapter;
import org.avaje.ebean.enhance.asm.MethodVisitor;
import org.avaje.ebean.enhance.asm.Opcodes;

/**
 * ClassAdapter used to detect if this class needs enhancement for entity or
 * transactional support.
 */
public class ClassAdapterDetectEnhancement extends ClassAdapter {

    final ClassLoader classLoader;

    final EnhanceContext enhanceContext;

    final HashSet<String> classAnnotation = new HashSet<String>();

    final ArrayList<DetectMethod> methods = new ArrayList<DetectMethod>();

    String className;

    boolean entity;

    boolean enhancedEntity;

    boolean transactional;

    boolean enhancedTransactional;

    public ClassAdapterDetectEnhancement(ClassLoader classLoader, EnhanceContext context) {
        super(new EmptyVisitor());
        this.classLoader = classLoader;
        this.enhanceContext = context;
    }

    public boolean isEntityOrTransactional() {
        return entity || isTransactional();
    }

    public String getStatus() {
        String s = "class: " + className;
        if (isEntity()) {
            s += " entity:true  enhanced:" + enhancedEntity;
            s = "*" + s;
        } else if (isTransactional()) {
            s += " transactional:true  enhanced:" + enhancedTransactional;
            s = "*" + s;
        } else {
            s = " " + s;
        }
        return s;
    }

    public boolean isLog(int level) {
        return enhanceContext.isLog(level);
    }

    public void log(String msg) {
        enhanceContext.log(className, msg);
    }

    public void log(int level, String msg) {
        if (isLog(level)) {
            log(msg);
        }
    }

    public boolean isEnhancedEntity() {
        return enhancedEntity;
    }

    public boolean isEnhancedTransactional() {
        return enhancedTransactional;
    }

    /**
	 * Return true if this is an entity bean or embeddable bean.
	 */
    public boolean isEntity() {
        return entity;
    }

    /**
	 * Return true if ANY method has the transactional annotation.
	 */
    public boolean isTransactional() {
        if (transactional) {
            return transactional;
        }
        for (int i = 0; i < methods.size(); i++) {
            DetectMethod m = methods.get(i);
            if (m.isTransactional()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Visit the class with interfaces.
	 */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            throw new NoEnhancementRequiredException(name + " is an Interface");
        }
        className = name;
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].equals(EnhanceConstants.C_ENTITYBEAN)) {
                enhancedEntity = true;
                entity = true;
            } else if (interfaces[i].equals(EnhanceConstants.C_ENHANCEDTRANSACTIONAL)) {
                enhancedTransactional = true;
            } else {
                ClassMeta intefaceMeta = enhanceContext.getInterfaceMeta(interfaces[i], classLoader);
                if (intefaceMeta != null && intefaceMeta.isTransactional()) {
                    transactional = true;
                    if (isLog(9)) {
                        log("detected implements tranactional interface " + intefaceMeta);
                    }
                }
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
	 * Visit class level annotations.
	 */
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        classAnnotation.add(desc);
        if (isEntityAnnotation(desc)) {
            entity = true;
        } else if (desc.equals(EnhanceConstants.AVAJE_TRANSACTIONAL_ANNOTATION)) {
            transactional = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    /**
	 * Return true if the annotation is for an Entity, Embeddable or MappedSuperclass.
	 */
    private boolean isEntityAnnotation(String desc) {
        if (desc.equals(EnhanceConstants.ENTITY_ANNOTATION)) {
            return true;
        } else if (desc.equals(EnhanceConstants.EMBEDDABLE_ANNOTATION)) {
            return true;
        } else if (desc.equals(EnhanceConstants.MAPPEDSUPERCLASS_ANNOTATION)) {
            return true;
        }
        return false;
    }

    /**
	 * Visit the methods specifically looking for method level transactional
	 * annotations.
	 */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        DetectMethod dmv = new DetectMethod(mv);
        methods.add(dmv);
        return dmv;
    }

    /**
	 * Check methods for Transactional annotation.
	 */
    private static class DetectMethod extends MethodAdapter {

        boolean transactional;

        public DetectMethod(final MethodVisitor mv) {
            super(mv);
        }

        /**
		 * Return true if this method has the transaction annotation supplied.
		 */
        public boolean isTransactional() {
            return transactional;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.equals(EnhanceConstants.AVAJE_TRANSACTIONAL_ANNOTATION)) {
                transactional = true;
            }
            return super.visitAnnotation(desc, visible);
        }
    }
}
