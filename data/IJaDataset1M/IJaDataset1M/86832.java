package org.perfmon4j.instrument.javassist;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

/**
 * Utility for calculating serialVersionUIDs for Serializable classes.
 *
 * @author Bob Lee (crazybob@crazybob.org)
 * @author modified by Shigeru Chiba
 */
public class SerialVersionUID382 {

    /**
     * Adds serialVersionUID if one does not already exist. Call this before
     * modifying a class to maintain serialization compatability.
     */
    public static void setSerialVersionUID(CtClass clazz) throws CannotCompileException, NotFoundException {
        try {
            clazz.getDeclaredField("serialVersionUID");
            return;
        } catch (NotFoundException e) {
        }
        if (!isSerializable(clazz)) return;
        CtField field = new CtField(CtClass.longType, "serialVersionUID", clazz);
        field.setModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL);
        clazz.addField(field, calculateDefault(clazz) + "L");
    }

    /**
     * Does the class implement Serializable?
     */
    static boolean isSerializable(CtClass clazz) throws NotFoundException {
        ClassPool pool = clazz.getClassPool();
        return clazz.subtypeOf(pool.get("java.io.Serializable"));
    }

    /**
     * Calculate default value. See Java Serialization Specification, Stream
     * Unique Identifiers.
     */
    static long calculateDefault(CtClass clazz) throws CannotCompileException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            ClassFile classFile = clazz.getClassFile();
            String javaName = javaName(clazz);
            out.writeUTF(javaName);
            out.writeInt(clazz.getModifiers() & (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT));
            String[] interfaces = classFile.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) interfaces[i] = javaName(interfaces[i]);
            Arrays.sort(interfaces);
            for (int i = 0; i < interfaces.length; i++) out.writeUTF(interfaces[i]);
            CtField[] fields = clazz.getDeclaredFields();
            Arrays.sort(fields, new Comparator<Object>() {

                public int compare(Object o1, Object o2) {
                    CtField field1 = (CtField) o1;
                    CtField field2 = (CtField) o2;
                    return field1.getName().compareTo(field2.getName());
                }
            });
            for (int i = 0; i < fields.length; i++) {
                CtField field = (CtField) fields[i];
                int mods = field.getModifiers();
                if (((mods & Modifier.PRIVATE) == 0) || ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0)) {
                    out.writeUTF(field.getName());
                    out.writeInt(mods);
                    out.writeUTF(field.getFieldInfo2().getDescriptor());
                }
            }
            if (classFile.getStaticInitializer() != null) {
                out.writeUTF("<clinit>");
                out.writeInt(Modifier.STATIC);
                out.writeUTF("()V");
            }
            CtConstructor[] constructors = clazz.getDeclaredConstructors();
            Arrays.sort(constructors, new Comparator<Object>() {

                public int compare(Object o1, Object o2) {
                    CtConstructor c1 = (CtConstructor) o1;
                    CtConstructor c2 = (CtConstructor) o2;
                    return c1.getMethodInfo2().getDescriptor().compareTo(c2.getMethodInfo2().getDescriptor());
                }
            });
            for (int i = 0; i < constructors.length; i++) {
                CtConstructor constructor = constructors[i];
                int mods = constructor.getModifiers();
                if ((mods & Modifier.PRIVATE) == 0) {
                    out.writeUTF("<init>");
                    out.writeInt(mods);
                    out.writeUTF(constructor.getMethodInfo2().getDescriptor().replace('/', '.'));
                }
            }
            CtMethod[] methods = clazz.getDeclaredMethods();
            Arrays.sort(methods, new Comparator<Object>() {

                public int compare(Object o1, Object o2) {
                    CtMethod m1 = (CtMethod) o1;
                    CtMethod m2 = (CtMethod) o2;
                    int value = m1.getName().compareTo(m2.getName());
                    if (value == 0) value = m1.getMethodInfo2().getDescriptor().compareTo(m2.getMethodInfo2().getDescriptor());
                    return value;
                }
            });
            for (int i = 0; i < methods.length; i++) {
                CtMethod method = methods[i];
                int mods = method.getModifiers();
                if ((mods & Modifier.PRIVATE) == 0) {
                    out.writeUTF(method.getName());
                    out.writeInt(mods);
                    out.writeUTF(method.getMethodInfo2().getDescriptor().replace('/', '.'));
                }
            }
            out.flush();
            MessageDigest digest = MessageDigest.getInstance("SHA");
            byte[] digested = digest.digest(bout.toByteArray());
            long hash = 0;
            for (int i = Math.min(digested.length, 8) - 1; i >= 0; i--) hash = (hash << 8) | (digested[i] & 0xFF);
            return hash;
        } catch (IOException e) {
            throw new CannotCompileException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new CannotCompileException(e);
        }
    }

    private static String javaName(CtClass clazz) {
        return Descriptor.toJavaName(Descriptor.toJvmName(clazz));
    }

    private static String javaName(String name) {
        return Descriptor.toJavaName(Descriptor.toJvmName(name));
    }
}
