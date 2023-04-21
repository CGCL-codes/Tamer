package net.sourceforge.comeback.java;

import net.sourceforge.comeback.asm.MethodGenerator;
import org.objectweb.asm.Type;

/**
 * Resolves accesses to local variables.
 *
 * @author Michael Rudolf
 */
public class LocalVariableResolver extends VariableResolver {

    /**
     * The local variable slot in the current method.
     */
    private final int local;

    /**
     * Creates a new resolver for the local variable with the given type and 
     * slot.
     * 
     * @param type  the type of the local variable
     * @param local the slot of the local variable in the current method
     */
    public LocalVariableResolver(Type type, int local) {
        super(type);
        this.local = local;
    }

    public void generateReadCode(MethodGenerator generator) {
        generator.loadLocal(local);
    }

    public void generateWriteCode(MethodGenerator generator) {
        generator.storeLocal(local);
    }
}
