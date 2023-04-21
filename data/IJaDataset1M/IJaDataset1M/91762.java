package com.google.gwt.dev.shell.rewrite;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import com.google.gwt.dev.shell.rewrite.HostedModeClassRewriter.SingleJsoImplData;
import com.google.gwt.dev.util.collect.Maps;
import com.google.gwt.dev.util.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Effects the renaming of {@code @SingleJsoImpl} methods from their original
 * name to their mangled name. Let us call the original method an "unmangled
 * method" and the new method a "mangled method". There are three steps in this
 * process:
 * <ol>
 * <li>Within {@code @SingleJsoImpl} interfaces rename all unmangled methods to
 * become mangled methods.</li>
 * <li>Within non-JSO classes containing a concrete implementation of an
 * unmangled method, add a mangled method which is implemented as a simple
 * trampoline to the unmangled method. (We don't do this in JSO classes here
 * because the one-and-only trampoline lives in JavaScriptObject$ and is emitted
 * in {@link WriteJsoImpl}).
 * <li>Update all call sites targeting unmangled methods to target mangled
 * methods instead, provided the caller is binding to the interface rather than
 * a concrete type.</li>
 * </ol>
 */
public class RewriteSingleJsoImplDispatches extends ClassAdapter {

    private class MyMethodVisitor extends MethodAdapter {

        public MyMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            if (opcode == Opcodes.INVOKEINTERFACE) {
                if (jsoData.getSingleJsoIntfTypes().contains(owner)) {
                    name = owner.replace('/', '_') + "_" + name;
                    assert jsoData.getMangledNames().contains(name) : "Missing " + name;
                } else {
                    outer: for (String intf : computeAllInterfaces(owner)) {
                        if (jsoData.getSingleJsoIntfTypes().contains(intf)) {
                            String maybeMangled = intf.replace('/', '_') + "_" + name;
                            List<Method> methods = jsoData.getImplementations(maybeMangled);
                            if (methods != null) {
                                for (Method method : methods) {
                                    assert method.getArgumentTypes().length >= 1;
                                    Type[] argumentTypes = new Type[method.getArgumentTypes().length - 1];
                                    System.arraycopy(method.getArgumentTypes(), 1, argumentTypes, 0, argumentTypes.length);
                                    String maybeDescriptor = Type.getMethodDescriptor(method.getReturnType(), argumentTypes);
                                    if (maybeDescriptor.equals(desc)) {
                                        name = maybeMangled;
                                        break outer;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    private String currentTypeName;

    private final Set<String> implementedMethods = new HashSet<String>();

    private boolean inSingleJsoImplInterfaceType;

    private Map<String, Set<String>> intfNamesToAllInterfaces = Maps.create();

    private final SingleJsoImplData jsoData;

    private final TypeOracle typeOracle;

    public RewriteSingleJsoImplDispatches(ClassVisitor v, TypeOracle typeOracle, SingleJsoImplData jsoData) {
        super(v);
        this.typeOracle = typeOracle;
        this.jsoData = jsoData;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        assert currentTypeName == null;
        super.visit(version, access, name, signature, superName, interfaces);
        if (name.equals(HostedModeClassRewriter.JAVASCRIPTOBJECT_IMPL_DESC)) {
            return;
        }
        currentTypeName = name;
        inSingleJsoImplInterfaceType = jsoData.getSingleJsoIntfTypes().contains(name);
        if (interfaces != null && (access & Opcodes.ACC_INTERFACE) == 0) {
            Set<String> toStub = computeAllInterfaces(interfaces);
            toStub.retainAll(jsoData.getSingleJsoIntfTypes());
            for (String stubIntr : toStub) {
                writeTrampoline(stubIntr);
            }
        }
    }

    @Override
    public void visitEnd() {
        if (inSingleJsoImplInterfaceType) {
            for (Map.Entry<String, List<Method>> entry : toImplement(currentTypeName).entrySet()) {
                for (Method method : entry.getValue()) {
                    writeEmptyMethod(entry.getKey(), method);
                }
            }
        }
        super.visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (inSingleJsoImplInterfaceType && !"<clinit>".equals(name)) {
            name = currentTypeName.replace('/', '_') + "_" + name;
            implementedMethods.add(name);
        }
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) {
            return null;
        }
        return new MyMethodVisitor(mv);
    }

    private Set<String> computeAllInterfaces(String intfName) {
        Set<String> toReturn = intfNamesToAllInterfaces.get(intfName);
        if (toReturn != null) {
            return toReturn;
        }
        toReturn = Sets.create();
        List<JClassType> q = new LinkedList<JClassType>();
        JClassType intf = typeOracle.findType(intfName.replace('/', '.').replace('$', '.'));
        assert intf != null : "Could not find interface " + intfName;
        q.add(intf);
        while (!q.isEmpty()) {
            intf = q.remove(0);
            String resourceName = getResourceName(intf);
            if (!toReturn.contains(resourceName)) {
                toReturn = Sets.add(toReturn, resourceName);
                Collections.addAll(q, intf.getImplementedInterfaces());
            }
        }
        intfNamesToAllInterfaces = Maps.put(intfNamesToAllInterfaces, intfName, toReturn);
        return toReturn;
    }

    private Set<String> computeAllInterfaces(String[] interfaces) {
        Set<String> toReturn = new HashSet<String>();
        for (String intfName : interfaces) {
            toReturn.addAll(computeAllInterfaces(intfName));
        }
        return toReturn;
    }

    private String getResourceName(JClassType type) {
        if (type.getEnclosingType() != null) {
            return getResourceName(type.getEnclosingType()) + "$" + type.getSimpleSourceName();
        }
        return type.getQualifiedSourceName().replace('.', '/');
    }

    /**
   * Given a resource name of a class, find all mangled method names that must
   * be implemented.
   */
    private SortedMap<String, List<Method>> toImplement(String typeName) {
        String name = typeName.replace('/', '_');
        String prefix = name + "_";
        String suffix = name + "`";
        SortedMap<String, List<Method>> toReturn = new TreeMap<String, List<Method>>();
        for (String mangledName : jsoData.getMangledNames().subSet(prefix, suffix)) {
            toReturn.put(mangledName, jsoData.getImplementations(mangledName));
        }
        toReturn.keySet().removeAll(implementedMethods);
        return toReturn;
    }

    private void writeEmptyMethod(String mangledMethodName, Method method) {
        assert method.getArgumentTypes().length > 0;
        String descriptor = "(" + method.getDescriptor().substring(1 + method.getArgumentTypes()[0].getDescriptor().length());
        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, mangledMethodName, descriptor, null, null);
        mv.visitEnd();
    }

    /**
   * For regular Java objects that implement a SingleJsoImpl interface, write
   * instance trampoline dispatchers for mangled method names to the
   * implementing method.
   */
    private void writeTrampoline(String stubIntr) {
        for (Map.Entry<String, List<Method>> entry : toImplement(stubIntr).entrySet()) {
            for (Method method : entry.getValue()) {
                String mangledName = entry.getKey();
                String descriptor = "(" + method.getDescriptor().substring(1 + method.getArgumentTypes()[0].getDescriptor().length());
                String localName = method.getName().substring(0, method.getName().length() - 1);
                Method toCall = new Method(localName, descriptor);
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, mangledName, descriptor, null, null);
                if (mv != null) {
                    mv.visitCode();
                    int var = 1;
                    int size = 1;
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    for (Type t : toCall.getArgumentTypes()) {
                        size += t.getSize();
                        mv.visitVarInsn(t.getOpcode(Opcodes.ILOAD), var);
                        var += t.getSize();
                    }
                    size = Math.max(size, toCall.getReturnType().getSize());
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, currentTypeName, toCall.getName(), toCall.getDescriptor());
                    mv.visitInsn(toCall.getReturnType().getOpcode(Opcodes.IRETURN));
                    mv.visitMaxs(size, var);
                    mv.visitEnd();
                }
            }
        }
    }
}
