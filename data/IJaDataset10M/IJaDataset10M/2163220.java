package org.actorsguildframework.internal.codegenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.actorsguildframework.Actor;
import org.actorsguildframework.ActorException;
import org.actorsguildframework.ConfigurationException;
import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.internal.BeanClassDescriptor;
import org.actorsguildframework.internal.BeanFactory;
import org.actorsguildframework.internal.BeanHelper;
import org.actorsguildframework.internal.PropertyDescriptor;
import org.actorsguildframework.internal.PropertyDescriptor.PropertySource;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * BeanCreator is a singleton that creates bean implementation classes for all {@link Bean}
 * classes. It works system-wide, for all Controllers. 
 */
public final class BeanCreator {

    /**
	 * The only instance of BeanCreator.
	 */
    private static final BeanCreator instance = new BeanCreator();

    /**
	 * The ActorProxyCreator.
	 */
    private static final ActorProxyCreator actorCreator = ActorProxyCreator.getInstance();

    /**
	 * Map of bean factories. Synchronize before accessing it!
	 */
    private final HashMap<Class<?>, BeanFactory> beanFactories;

    /**
	 * The version of the generated Java classes. 1.5 or 1.6, depending on the
	 * Java version. 
	 */
    private static final int codeVersion = System.getProperty("java.version").startsWith("1.5") ? Opcodes.V1_5 : Opcodes.V1_6;

    /**
	 * Private constructor.
	 */
    private BeanCreator() {
        beanFactories = new HashMap<Class<?>, BeanFactory>();
    }

    /**
	 * Returns the BeanCreator.
	 * @return the BeanCreator
	 */
    public static BeanCreator getInstance() {
        return instance;
    }

    /**
	 * Returns a proxy factory for the given {@link Bean} class. 
	 * @param beanClass the class of the Bean
	 * @return the new factory
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
    public BeanFactory getFactory(Class<?> beanClass) {
        synchronized (this) {
            BeanFactory apf = beanFactories.get(beanClass);
            if (apf != null) return apf;
            if (beanClass.getAnnotation(Bean.class) == null) throw new ConfigurationException("The given class has no @Bean annotation (and is no actor, which are @Beans).");
            BeanFactory r;
            if (Actor.class.isAssignableFrom(beanClass)) r = actorCreator.createFactory(beanClass); else r = createFactory(beanClass);
            beanFactories.put(beanClass, r);
            return r;
        }
    }

    /**
	 * Creates a new factory.
	 * @param beanClass
	 * @return the factory
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
    private BeanFactory createFactory(Class<?> beanClass) {
        try {
            BeanClassDescriptor bcd = BeanClassDescriptor.create(beanClass);
            generateBeanClass(beanClass, bcd);
            return generateFactoryClass(beanClass, String.format("%s__BEAN", beanClass.getName()), bcd, false);
        } catch (NoSuchMethodException e) {
            throw new ActorException("Unexpected error while creating bean", e);
        }
    }

    /**
	 * Creates and loads the bean's factory class.
	 * @param beanClass the Bean class
	 * @param generatedBeanClassName the name of the class that this factory will produce
	 * @param bcd the bean class descriptor
	 * @param synchronizeInitializers true to synchronize the initializer invocations (actors
	 * 		do this), false otherwise
	 * @return the new factory
	 */
    public static BeanFactory generateFactoryClass(Class<?> beanClass, String generatedBeanClassName, BeanClassDescriptor bcd, boolean synchronizeInitializers) {
        String className = String.format("%s__BEANFACTORY", beanClass.getName());
        String classNameInternal = className.replace('.', '/');
        String generatedBeanClassNameInternal = generatedBeanClassName.replace('.', '/');
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        cw.visit(codeVersion, Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_SUPER + Opcodes.ACC_SYNTHETIC, classNameInternal, null, "java/lang/Object", new String[] { Type.getInternalName(BeanFactory.class) });
        cw.visitSource(null, null);
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(Opcodes.RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + classNameInternal + ";", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "createNewInstance", "(Lorg/actorsguildframework/internal/Controller;Lorg/actorsguildframework/Props;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            final int initCount = bcd.getInitializerCount();
            Label tryStart = new Label();
            Label tryEnd = new Label();
            Label tryFinally = new Label();
            Label tryFinallyEnd = new Label();
            if (synchronizeInitializers && (initCount > 0)) {
                mv.visitTryCatchBlock(tryStart, tryEnd, tryFinally, null);
                mv.visitTryCatchBlock(tryFinally, tryFinallyEnd, tryFinally, null);
            }
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(Opcodes.NEW, generatedBeanClassNameInternal);
            mv.visitInsn(Opcodes.DUP);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, generatedBeanClassNameInternal, "<init>", "(Lorg/actorsguildframework/internal/Controller;Lorg/actorsguildframework/Props;)V");
            if (synchronizeInitializers) {
                mv.visitInsn(Opcodes.DUP);
                mv.visitInsn(Opcodes.MONITORENTER);
                mv.visitLabel(tryStart);
            }
            for (int i = 0; i < initCount; i++) {
                Method m = bcd.getInitializers(i);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, generatedBeanClassNameInternal, m.getName(), Type.getMethodDescriptor(m));
            }
            if (synchronizeInitializers) {
                if (initCount > 0) {
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitInsn(Opcodes.MONITOREXIT);
                    mv.visitLabel(tryEnd);
                    mv.visitJumpInsn(Opcodes.GOTO, tryFinallyEnd);
                }
                mv.visitLabel(tryFinally);
                mv.visitInsn(Opcodes.DUP);
                mv.visitInsn(Opcodes.MONITOREXIT);
                mv.visitLabel(tryFinallyEnd);
            }
            mv.visitInsn(Opcodes.ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + classNameInternal + ";", null, l0, l1, 0);
            mv.visitLocalVariable("controller", "Lorg/actorsguildframework/internal/Controller;", null, l0, l1, 1);
            mv.visitLocalVariable("props", "Lorg/actorsguildframework/Props;", null, l0, l1, 2);
            mv.visitLocalVariable("synchronizeInitializer", "Z", null, l0, l1, 3);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        cw.visitEnd();
        Class<?> newClass = GenerationUtils.loadClass(className, cw.toByteArray());
        try {
            return (BeanFactory) newClass.newInstance();
        } catch (Exception e) {
            throw new ConfigurationException("Failure loading ActorProxyFactory", e);
        }
    }

    private static final String PROP_FIELD_NAME_TEMPLATE = "%s__BEAN_PROP";

    /**
	 * Creates and loads the bean implementation class.
	 * @param beanClass the bean class
	 * @param bcd the BeanClassDescriptor to use
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
    private static Class<?> generateBeanClass(Class<?> beanClass, BeanClassDescriptor bcd) throws NoSuchMethodException {
        String className = String.format("%s__BEAN", beanClass.getName());
        String classNameInternal = className.replace('.', '/');
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        cw.visit(codeVersion, Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_SUPER + Opcodes.ACC_SYNTHETIC, classNameInternal, null, Type.getInternalName(beanClass), new String[] {});
        cw.visitSource(null, null);
        writePropFields(bcd, cw);
        {
            mv = cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        writeConstructor(beanClass, bcd, classNameInternal, cw, null);
        writePropAccessors(bcd, classNameInternal, cw);
        cw.visitEnd();
        try {
            return (Class<?>) GenerationUtils.loadClass(className, cw.toByteArray());
        } catch (Exception e) {
            throw new ConfigurationException("Failure loading generated Bean", e);
        }
    }

    /**
	 * Writes the accessor methods for @Prop generated properties.
	 * @param bcd the class descriptor
	 * @param classNameInternal the internal name of this class
	 * @param cw the ClassWriter to write to
	 */
    public static void writePropAccessors(BeanClassDescriptor bcd, String classNameInternal, ClassWriter cw) {
        String classNameDescriptor = "L" + classNameInternal + ";";
        MethodVisitor mv;
        for (int i = 0; i < bcd.getPropertyCount(); i++) {
            PropertyDescriptor pd = bcd.getProperty(i);
            if (!pd.getPropertySource().isGenerating()) continue;
            {
                Type t = Type.getType(pd.getPropertyClass());
                Method orig = pd.getGetter();
                mv = cw.visitMethod(GenerationUtils.convertAccessModifiers(orig.getModifiers()) + (bcd.isThreadSafe() ? Opcodes.ACC_SYNCHRONIZED : 0), orig.getName(), Type.getMethodDescriptor(orig), GenericTypeHelper.getSignature(orig), null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classNameInternal, String.format(PROP_FIELD_NAME_TEMPLATE, pd.getName()), Type.getDescriptor(pd.getPropertyClass()));
                mv.visitInsn(t.getOpcode(Opcodes.IRETURN));
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", classNameDescriptor, null, l0, l1, 0);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
            if (pd.getAccess().isWritable()) {
                Type t = Type.getType(pd.getPropertyClass());
                Method orig = pd.getSetter();
                mv = cw.visitMethod(GenerationUtils.convertAccessModifiers(orig.getModifiers()) + (bcd.isThreadSafe() ? Opcodes.ACC_SYNCHRONIZED : 0), orig.getName(), Type.getMethodDescriptor(orig), GenericTypeHelper.getSignature(orig), null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(t.getOpcode(Opcodes.ILOAD), 1);
                mv.visitFieldInsn(Opcodes.PUTFIELD, classNameInternal, String.format(PROP_FIELD_NAME_TEMPLATE, pd.getName()), Type.getDescriptor(pd.getPropertyClass()));
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", classNameDescriptor, null, l0, l1, 0);
                mv.visitLocalVariable("value", Type.getDescriptor(pd.getPropertyClass()), null, l0, l1, 1);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        }
    }

    /**
	 * Write the fields of @Prop properties.
	 * @param bcd the class descriptor
	 * @param cw the ClassWriter to write to
	 */
    public static void writePropFields(BeanClassDescriptor bcd, ClassWriter cw) {
        for (int i = 0; i < bcd.getPropertyCount(); i++) {
            PropertyDescriptor pd = bcd.getProperty(i);
            if (pd.getPropertySource() != PropertySource.ABSTRACT_METHOD) continue;
            cw.visitField(Opcodes.ACC_PRIVATE + (pd.getAccess().isWritable() ? 0 : Opcodes.ACC_FINAL), String.format(PROP_FIELD_NAME_TEMPLATE, pd.getName()), Type.getDescriptor(pd.getPropertyClass()), GenericTypeHelper.getSignature(pd.getPropertyType()), null).visitEnd();
        }
    }

    /**
	 * Interface for classes that write only one snippet of code into a method.
	 */
    public interface SnippetWriter {

        /**
		 * Writes something into the given MethodVisitor.
		 * @param mv the MethodVisitor to use
		 */
        void write(MethodVisitor mv);
    }

    /**
	 * Writes the bean constructor to the given ClassWriter.
	 * @param beanClass the original bean class to extend
	 * @param bcd the descriptor of the bean
	 * @param classNameInternal the internal name of the new class
	 * @param cw the ClassWriter to write to
	 * @param snippetWriter if not null, this will be invoked to add a snippet
	 *                      after the invocation of the super constructor
	 */
    public static void writeConstructor(Class<?> beanClass, BeanClassDescriptor bcd, String classNameInternal, ClassWriter cw, SnippetWriter snippetWriter) {
        String classNameDescriptor = "L" + classNameInternal + ";";
        int localPropertySize = 0;
        ArrayList<PropertyDescriptor> localVarProperties = new ArrayList<PropertyDescriptor>();
        for (int i = 0; i < bcd.getPropertyCount(); i++) {
            PropertyDescriptor pd = bcd.getProperty(i);
            if (pd.getPropertySource().isGenerating() || (pd.getDefaultValue() != null)) {
                localVarProperties.add(pd);
                localPropertySize += Type.getType(pd.getPropertyClass()).getSize();
            }
        }
        final int locVarThis = 0;
        final int locVarController = 1;
        final int locVarProps = 2;
        final int locVarPropertiesOffset = 3;
        final int locVarP = 3 + localPropertySize;
        final int locVarK = 4 + localPropertySize;
        final int locVarV = 5 + localPropertySize;
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lorg/actorsguildframework/internal/Controller;Lorg/actorsguildframework/Props;)V", null, null);
        mv.visitCode();
        Label lTry = new Label();
        Label lCatch = new Label();
        mv.visitTryCatchBlock(lTry, lCatch, lCatch, "java/lang/ClassCastException");
        Label lBegin = new Label();
        mv.visitLabel(lBegin);
        mv.visitVarInsn(Opcodes.ALOAD, locVarThis);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(beanClass), "<init>", "()V");
        if (snippetWriter != null) snippetWriter.write(mv);
        Label lPropertyInit = new Label();
        mv.visitLabel(lPropertyInit);
        int varCount = 0;
        for (PropertyDescriptor pd : localVarProperties) {
            Type pt = Type.getType(pd.getPropertyClass());
            if (pd.getDefaultValue() != null) mv.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(pd.getDefaultValue().getDeclaringClass()), pd.getDefaultValue().getName(), Type.getDescriptor(pd.getDefaultValue().getType())); else GenerationUtils.generateLoadDefault(mv, pd.getPropertyClass());
            mv.visitVarInsn(pt.getOpcode(Opcodes.ISTORE), locVarPropertiesOffset + varCount);
            varCount += pt.getSize();
        }
        mv.visitVarInsn(Opcodes.ALOAD, locVarProps);
        mv.visitVarInsn(Opcodes.ASTORE, locVarP);
        Label lWhile = new Label();
        Label lEndWhile = new Label();
        Label lWhileBody = new Label();
        mv.visitLabel(lWhile);
        mv.visitJumpInsn(Opcodes.GOTO, lEndWhile);
        mv.visitLabel(lWhileBody);
        mv.visitVarInsn(Opcodes.ALOAD, locVarP);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/actorsguildframework/Props", "getKey", "()Ljava/lang/String;");
        mv.visitVarInsn(Opcodes.ASTORE, locVarK);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/actorsguildframework/Props", "getValue", "()Ljava/lang/Object;");
        mv.visitVarInsn(Opcodes.ASTORE, locVarV);
        mv.visitLabel(lTry);
        Label lEndIf = new Label();
        varCount = 0;
        int ifCount = 0;
        for (int i = 0; i < bcd.getPropertyCount(); i++) {
            PropertyDescriptor pd = bcd.getProperty(i);
            boolean usesLocal = pd.getPropertySource().isGenerating() || (pd.getDefaultValue() != null);
            Class<?> propClass = pd.getPropertyClass();
            Type pt = Type.getType(propClass);
            mv.visitVarInsn(Opcodes.ALOAD, locVarK);
            mv.visitLdcInsn(pd.getName());
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
            Label lElse = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, lElse);
            if (!usesLocal) mv.visitVarInsn(Opcodes.ALOAD, locVarThis);
            if (propClass.isPrimitive()) {
                mv.visitLdcInsn(pd.getName());
                mv.visitVarInsn(Opcodes.ALOAD, locVarV);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(BeanHelper.class), String.format("get%s%sFromPropValue", propClass.getName().substring(0, 1).toUpperCase(Locale.US), propClass.getName().substring(1)), "(Ljava/lang/String;Ljava/lang/Object;)" + pt.getDescriptor());
            } else if (!propClass.equals(Object.class)) {
                mv.visitVarInsn(Opcodes.ALOAD, locVarV);
                mv.visitTypeInsn(Opcodes.CHECKCAST, pt.getInternalName());
            } else mv.visitVarInsn(Opcodes.ALOAD, locVarV);
            if (!usesLocal) mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classNameInternal, pd.getSetter().getName(), Type.getMethodDescriptor(pd.getSetter())); else mv.visitVarInsn(pt.getOpcode(Opcodes.ISTORE), varCount + locVarPropertiesOffset);
            mv.visitJumpInsn(Opcodes.GOTO, lEndIf);
            mv.visitLabel(lElse);
            ifCount++;
            if (usesLocal) varCount += pt.getSize();
        }
        mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(IllegalArgumentException.class));
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Unknown property \"%s\".");
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        mv.visitInsn(Opcodes.DUP);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ALOAD, locVarK);
        mv.visitInsn(Opcodes.AASTORE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(IllegalArgumentException.class), "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitLabel(lCatch);
        mv.visitInsn(Opcodes.POP);
        mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(IllegalArgumentException.class));
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Incompatible type for property \"%s\". Got %s.");
        mv.visitInsn(Opcodes.ICONST_2);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        mv.visitInsn(Opcodes.DUP);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ALOAD, locVarK);
        mv.visitInsn(Opcodes.AASTORE);
        mv.visitInsn(Opcodes.DUP);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitVarInsn(Opcodes.ALOAD, locVarV);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;");
        mv.visitInsn(Opcodes.AASTORE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(IllegalArgumentException.class), "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitLabel(lEndIf);
        mv.visitVarInsn(Opcodes.ALOAD, locVarP);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/actorsguildframework/Props", "tail", "()Lorg/actorsguildframework/Props;");
        mv.visitVarInsn(Opcodes.ASTORE, locVarP);
        mv.visitLabel(lEndWhile);
        mv.visitVarInsn(Opcodes.ALOAD, locVarP);
        mv.visitJumpInsn(Opcodes.IFNONNULL, lWhileBody);
        varCount = 0;
        for (PropertyDescriptor pd : localVarProperties) {
            Type pt = Type.getType(pd.getPropertyClass());
            mv.visitVarInsn(Opcodes.ALOAD, locVarThis);
            if (pd.getPropertySource() == PropertySource.ABSTRACT_METHOD) {
                mv.visitVarInsn(pt.getOpcode(Opcodes.ILOAD), locVarPropertiesOffset + varCount);
                mv.visitFieldInsn(Opcodes.PUTFIELD, classNameInternal, String.format(PROP_FIELD_NAME_TEMPLATE, pd.getName()), pt.getDescriptor());
            } else if (pd.getPropertySource() == PropertySource.USER_WRITTEN) {
                mv.visitVarInsn(pt.getOpcode(Opcodes.ILOAD), locVarPropertiesOffset + varCount);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classNameInternal, pd.getSetter().getName(), Type.getMethodDescriptor(pd.getSetter()));
            } else throw new RuntimeException("Internal error");
            varCount += pt.getSize();
        }
        if (bcd.isThreadSafe()) {
            mv.visitVarInsn(Opcodes.ALOAD, locVarThis);
            mv.visitInsn(Opcodes.DUP);
            mv.visitInsn(Opcodes.MONITORENTER);
            mv.visitInsn(Opcodes.MONITOREXIT);
        }
        mv.visitInsn(Opcodes.RETURN);
        Label lEnd = new Label();
        mv.visitLabel(lEnd);
        mv.visitLocalVariable("this", classNameDescriptor, null, lBegin, lEnd, locVarThis);
        mv.visitLocalVariable("controller", "Lorg/actorsguildframework/internal/Controller;", null, lBegin, lEnd, locVarController);
        mv.visitLocalVariable("props", "Lorg/actorsguildframework/Props;", null, lBegin, lEnd, locVarProps);
        varCount = 0;
        for (PropertyDescriptor pd : localVarProperties) {
            Type pt = Type.getType(pd.getPropertyClass());
            mv.visitLocalVariable("__" + pd.getName(), pt.getDescriptor(), GenericTypeHelper.getSignature(pd.getPropertyType()), lPropertyInit, lEnd, locVarPropertiesOffset + varCount);
            varCount += pt.getSize();
        }
        mv.visitLocalVariable("p", "Lorg/actorsguildframework/Props;", null, lPropertyInit, lEnd, locVarP);
        mv.visitLocalVariable("k", "Ljava/lang/String;", null, lWhile, lEndWhile, locVarK);
        mv.visitLocalVariable("v", "Ljava/lang/Object;", null, lWhile, lEndWhile, locVarV);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
}
