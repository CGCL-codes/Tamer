package net.sf.fgts.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Method Trace Class Transformer
 * @author rafaelri
 *
 */
public class MethodTraceClassTransformer implements ClassFileTransformer {

    private static final String[] ignore = new String[] { "sun/", "java/", "javax/" };

    public byte[] transform(ClassLoader classLoader, String className, Class<?> arg2, ProtectionDomain arg3, byte[] classBytes) throws IllegalClassFormatException {
        for (int i = 0; i < ignore.length; i++) {
            if (className.startsWith(ignore[i])) {
                return classBytes;
            }
        }
        if (className.equals("Foo") == false) return classBytes;
        CtClass ct;
        try {
            ct = ClassPool.getDefault().makeClass(new java.io.ByteArrayInputStream(classBytes));
            if (ct.isInterface() == false) {
                CtMethod[] methods = ct.getMethods();
                for (CtMethod m : methods) {
                    if (!m.isEmpty() && !Modifier.isNative(m.getModifiers())) {
                        String intercept = "return com.rar.ExecutionContext.dispatch(" + className + ".class, $0, \"" + m.getName() + "\",$sig,$args);";
                        m.setBody(intercept);
                    }
                }
                return ct.toBytecode();
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classBytes;
    }
}
