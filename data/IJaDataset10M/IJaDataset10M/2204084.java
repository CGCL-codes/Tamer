package net.sourceforge.javautil.bytecode.api.type.method.invocation;

import net.sourceforge.javautil.bytecode.api.BytecodeContext;
import net.sourceforge.javautil.bytecode.api.IBytecodeReferenceable;
import net.sourceforge.javautil.bytecode.api.BytecodeReferenceableAbstract;
import net.sourceforge.javautil.bytecode.api.IBytecodeResolvable;
import net.sourceforge.javautil.bytecode.api.TypeDescriptor;
import net.sourceforge.javautil.bytecode.api.type.method.BytecodeContextMethod;

/**
 * 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class CoersionOperation extends BytecodeReferenceableAbstract<BytecodeContextMethod> {

    protected final IBytecodeReferenceable value;

    public CoersionOperation(TypeDescriptor descriptor, IBytecodeReferenceable value) {
        super(descriptor);
        this.value = value;
    }

    public void load(BytecodeContextMethod context) {
        TypeDescriptor top = this.value.getType();
        if (top.isPrimitive() && descriptor.compareTo(TypeDescriptor.getFor(Object.class)) == 0) {
            context.invoke(context.getResolutionPool().resolve(top.getBoxedType().getClassName()), "valueOf", false, this.value);
        } else if (descriptor.isPrimitive() && !top.isPrimitive()) {
            String methodName = null;
            if (descriptor == TypeDescriptor.INTEGER) {
                methodName = "intValue";
            } else if (descriptor == TypeDescriptor.LONG) {
                methodName = "longValue";
            } else if (descriptor == TypeDescriptor.SHORT) {
                methodName = "shortValue";
            } else if (descriptor == TypeDescriptor.CHAR) {
                methodName = "charValue";
            } else if (descriptor == TypeDescriptor.DOUBLE) {
                methodName = "doubleValue";
            } else if (descriptor == TypeDescriptor.FLOAT) {
                methodName = "floatValue";
            } else if (descriptor == TypeDescriptor.BOOLEAN) {
                methodName = "booleanValue";
            } else if (descriptor == TypeDescriptor.BYTE) {
                methodName = "byteValue";
            }
            context.invoke(context.createCast(descriptor.getBoxedType(), value), methodName, false);
        } else if (top.isPrimitive() && descriptor.isPrimitive()) {
            if (top != descriptor) {
            } else {
                value.load(context);
            }
        } else {
            context.cast(descriptor, value);
        }
    }
}
