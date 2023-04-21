package com.google.gwt.dev.shell.rewrite;

import com.google.gwt.dev.asm.ClassAdapter;
import com.google.gwt.dev.asm.ClassVisitor;
import com.google.gwt.dev.asm.MethodAdapter;
import com.google.gwt.dev.asm.MethodVisitor;
import com.google.gwt.dev.asm.Opcodes;
import com.google.gwt.dev.asm.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A general Class Visitor which will take any of the method calls in it's
 * list and replace them with static calls to another method (the "mirrored"
 * method) in another class (the "mirrored" class). This method should
 * take the original object as it's first argument, followed by the rest of
 * the arguments to the method.  The "mirrored" class will not be rewritten,
 * allowing the "mirrored" method to do whatever modifications are necessary
 * before calling the original method (if desired).  Methods which should be
 * rewritten are listed in the mirroredMethods map below. Note that our
 * mirroring process is not robust enough to rewrite methods on subtypes.
 */
public class UseMirroredClasses extends ClassAdapter {

    private static class MethodInterceptor extends MethodAdapter {

        private static HashMap<String, HashMap<String, String>> mirrorMap;

        static {
            mirrorMap = new HashMap<String, HashMap<String, String>>();
            HashMap<String, String> logRecordMethods = new HashMap<String, String>();
            logRecordMethods.put("getLoggerName", "com/google/gwt/logging/impl/DevModeLoggingFixes:getLoggerName");
            mirrorMap.put("java/util/logging/LogRecord", logRecordMethods);
            HashMap<String, String> logManagerMethods = new HashMap<String, String>();
            logManagerMethods.put("getLogger", "com/google/gwt/logging/impl/DevModeLoggingFixes:logManagerGetLogger");
            logManagerMethods.put("getLoggerNames", "com/google/gwt/logging/impl/DevModeLoggingFixes:logManagerGetLoggerNames");
            mirrorMap.put("java/util/logging/LogManager", logManagerMethods);
            HashMap<String, String> loggerMethods = new HashMap<String, String>();
            loggerMethods.put("getName", "com/google/gwt/logging/impl/DevModeLoggingFixes:getName");
            loggerMethods.put("getLogger", "com/google/gwt/logging/impl/DevModeLoggingFixes:loggerGetLogger");
            mirrorMap.put("java/util/logging/Logger", loggerMethods);
        }

        private String className;

        protected MethodInterceptor(MethodVisitor mv, String className) {
            super(mv);
            this.className = className;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            Map<String, String> mirroredMethods = mirrorMap.get(owner);
            if (mirroredMethods == null) {
                super.visitMethodInsn(opcode, owner, name, desc);
                return;
            }
            String mirrorClassMethod = mirroredMethods.get(name);
            if (mirrorClassMethod == null) {
                super.visitMethodInsn(opcode, owner, name, desc);
                return;
            }
            String[] temp = mirrorClassMethod.split(":");
            if (temp.length < 2) {
                super.visitMethodInsn(opcode, owner, name, desc);
                return;
            }
            String mirrorClass = temp[0];
            String mirrorMethod = temp[1];
            if (className.equals(mirrorClass.replace("/", "."))) {
                super.visitMethodInsn(opcode, owner, name, desc);
                return;
            }
            if (opcode == Opcodes.INVOKESTATIC) {
                super.visitMethodInsn(opcode, mirrorClass, mirrorMethod, desc);
                return;
            }
            final Type[] argTypes = Type.getArgumentTypes(desc);
            final Type[] newArgTypes = new Type[argTypes.length + 1];
            newArgTypes[0] = Type.getType("L" + owner + ";");
            System.arraycopy(argTypes, 0, newArgTypes, 1, argTypes.length);
            String newDesc = Type.getMethodDescriptor(Type.getReturnType(desc), newArgTypes);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, mirrorClass, mirrorMethod, newDesc);
            return;
        }
    }

    private String className;

    public UseMirroredClasses(ClassVisitor cv, String className) {
        super(cv);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) {
            return null;
        }
        return new MethodInterceptor(mv, className);
    }
}
