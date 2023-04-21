package com.jogamp.gluegen.procaddress;

import com.jogamp.gluegen.MethodBinding;
import com.jogamp.gluegen.FunctionEmitter;
import com.jogamp.gluegen.JavaMethodBindingEmitter;
import java.io.*;
import com.jogamp.gluegen.*;

/** A specialization of JavaMethodBindingEmitter with knowledge of how
to call through a function pointer. */
public class ProcAddressJavaMethodBindingEmitter extends JavaMethodBindingEmitter {

    protected boolean callThroughProcAddress;

    protected boolean changeNameAndArguments;

    protected String getProcAddressTableExpr;

    protected ProcAddressEmitter emitter;

    public ProcAddressJavaMethodBindingEmitter(JavaMethodBindingEmitter methodToWrap, boolean callThroughProcAddress, String getProcAddressTableExpr, boolean changeNameAndArguments, ProcAddressEmitter emitter) {
        super(methodToWrap);
        this.callThroughProcAddress = callThroughProcAddress;
        this.getProcAddressTableExpr = getProcAddressTableExpr;
        this.changeNameAndArguments = changeNameAndArguments;
        this.emitter = emitter;
        if (callThroughProcAddress) {
            setCommentEmitter(new WrappedMethodCommentEmitter());
        }
        if (methodToWrap.getBinding().hasContainingType()) {
            throw new IllegalArgumentException("Cannot create proc. address wrapper; method has containing type: \"" + methodToWrap.getBinding() + "\"");
        }
    }

    public ProcAddressJavaMethodBindingEmitter(ProcAddressJavaMethodBindingEmitter methodToWrap) {
        this(methodToWrap, methodToWrap.callThroughProcAddress, methodToWrap.getProcAddressTableExpr, methodToWrap.changeNameAndArguments, methodToWrap.emitter);
    }

    @Override
    public String getName() {
        String res = super.getName();
        if (changeNameAndArguments) {
            return ProcAddressEmitter.WRAP_PREFIX + res;
        }
        return res;
    }

    @Override
    protected int emitArguments(PrintWriter writer) {
        int numEmitted = super.emitArguments(writer);
        if (callThroughProcAddress) {
            if (changeNameAndArguments) {
                if (numEmitted > 0) {
                    writer.print(", ");
                }
                writer.print("long procAddress");
                ++numEmitted;
            }
        }
        return numEmitted;
    }

    @Override
    protected String getImplMethodName() {
        String name = super.getImplMethodName();
        if (callThroughProcAddress) {
            return ProcAddressEmitter.WRAP_PREFIX + name;
        }
        return name;
    }

    @Override
    protected void emitPreCallSetup(MethodBinding binding, PrintWriter writer) {
        super.emitPreCallSetup(binding, writer);
        if (callThroughProcAddress) {
            String procAddressVariable = ProcAddressEmitter.PROCADDRESS_VAR_PREFIX + binding.getName();
            writer.println("    final long __addr_ = " + getProcAddressTableExpr + "." + procAddressVariable + ";");
            writer.println("    if (__addr_ == 0) {");
            writer.println("      throw new " + emitter.unsupportedExceptionType() + "(\"Method \\\"" + binding.getName() + "\\\" not available\");");
            writer.println("    }");
        }
    }

    @Override
    protected int emitCallArguments(MethodBinding binding, PrintWriter writer) {
        int numEmitted = super.emitCallArguments(binding, writer);
        if (callThroughProcAddress) {
            if (numEmitted > 0) {
                writer.print(", ");
            }
            writer.print("__addr_");
            ++numEmitted;
        }
        return numEmitted;
    }

    /** This class emits the comment for the wrapper method */
    public class WrappedMethodCommentEmitter extends JavaMethodBindingEmitter.DefaultCommentEmitter {

        @Override
        protected void emitBeginning(FunctionEmitter methodEmitter, PrintWriter writer) {
            writer.print("Entry point (through function pointer) to C language function: <br> ");
        }
    }
}
