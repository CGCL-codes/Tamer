package org.iqual.infodrome;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import org.objectweb.asm.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
* <p/>
* Created by IntelliJ IDEA.
* User: zslajchrt
* Date: May 23, 2010
* Time: 10:54:45 PM
*/
class CookieClassLoader extends ClassLoader {

    private final Method method;

    private final MetaCookie metaCookie;

    private final String expectedClassName;

    private final URL starterClassResource;

    CookieClassLoader(Method method, MetaCookie metaCookie, String expectedClassName, URL starterClassResource) {
        super(Thread.currentThread().getContextClassLoader());
        this.method = method;
        this.metaCookie = metaCookie;
        this.expectedClassName = expectedClassName;
        this.starterClassResource = starterClassResource;
    }

    public MetaCookie getMetaCookie() {
        return metaCookie;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        if (!className.equals(expectedClassName)) {
            return null;
        }
        try {
            return defineCookieClass(className);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Class defineCookieClass(String className) throws Exception {
        byte[] byteCode = initializeCookieGenerator(metaCookie.getCookieProperties(), className);
        return defineClass(className, byteCode, 0, byteCode.length);
    }

    private byte[] initializeCookieGenerator(final List<CookieProperty> cookieProperties, final String className) throws Exception {
        BeanGenerator cookieGenerator = new BeanGenerator();
        cookieGenerator.setSuperclass(Cookie.class);
        for (final CookieProperty cookieProperty : cookieProperties) {
            cookieGenerator.addProperty(cookieProperty.getName(), cookieProperty.getType());
        }
        cookieGenerator.setNamingPolicy(new NamingPolicy() {

            @Override
            public String getClassName(String prefix, String source, Object key, Predicate names) {
                return className;
            }
        });
        return generateCookieClass(cookieGenerator);
    }

    private byte[] generateCookieClass(BeanGenerator cookieGenerator) throws Exception {
        cookieGenerator.setUseCache(false);
        cookieGenerator.createClass();
        ClassWriter classWriter = new ClassWriter(1);
        cookieGenerator.generateClass(new CookieClassWriter(classWriter));
        return classWriter.toByteArray();
    }

    private class CookieClassWriter extends ClassAdapter {

        private int propertyCounter;

        public CookieClassWriter(ClassWriter classWriter) {
            super(classWriter);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final MethodVisitor getterVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (!name.startsWith("get")) {
                return getterVisitor;
            }
            final int getterIndex = propertyCounter++;
            try {
                InputStream classAsStream = starterClassResource.openStream();
                boolean annotationProcessed;
                do {
                    ClassReader classReader = new ClassReader(classAsStream);
                    CopyAnnotationsVisitor copyAnnotationsVisitor = new CopyAnnotationsVisitor(getterIndex, getterVisitor, method);
                    classReader.accept(copyAnnotationsVisitor, 1);
                    annotationProcessed = copyAnnotationsVisitor.isAnnotationProcessed();
                    if (!annotationProcessed) {
                        String superClassName = copyAnnotationsVisitor.getSuperClassName();
                        if (superClassName == null) {
                            throw new IllegalStateException("Cannot copy annotations from " + name + " method");
                        }
                        classAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(superClassName + ".class");
                    }
                } while (!annotationProcessed);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            return getterVisitor;
        }
    }
}
