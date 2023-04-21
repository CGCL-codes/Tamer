package org.sodbeans.compiler.hop.descriptors;

import org.sodbeans.compiler.api.descriptors.CompilerMethodDescriptor;
import org.sodbeans.controller.api.ScreenReaderInformation;
import org.sonify.vm.intermediate.symbol.MethodDescriptor;

/**
 * Represents a method that was processed by the Hop compiler.
 * 
 * @author Neelima Samsani
 */
public class HopMethodDescriptor extends CompilerMethodDescriptor {

    private MethodInfo info = new MethodInfo();

    private MethodDescriptor methodDescriptor;

    /**
     * Sets a method descriptor
     * 
     * @param methodDescriptor
     */
    public void setMethodDescriptor(MethodDescriptor methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }

    /**
     * Returns a method descriptor
     * @return
     */
    public MethodDescriptor getMethodDescriptor() {
        return methodDescriptor;
    }

    @Override
    public String getName() {
        return methodDescriptor.getName();
    }

    @Override
    public int getLine() {
        return methodDescriptor.getLineBegin();
    }

    @Override
    public int getColumn() {
        return methodDescriptor.getColumnBegin();
    }

    @Override
    public int getEndLine() {
        return methodDescriptor.getLineEnd();
    }

    @Override
    public int getEndColumn() {
        return methodDescriptor.getColumnEnd();
    }

    public ScreenReaderInformation getScreenReaderInformation() {
        return info;
    }

    public class MethodInfo implements ScreenReaderInformation {

        public String getDescription() {
            return methodDescriptor.getName() + " action";
        }
    }
}
