package com.jogamp.gluegen.opengl.nativesig;

import com.jogamp.gluegen.FunctionEmitter;
import com.jogamp.gluegen.JavaMethodBindingEmitter;
import com.jogamp.gluegen.JavaType;
import com.jogamp.gluegen.MethodBinding;
import com.jogamp.gluegen.cgram.types.FunctionSymbol;
import com.jogamp.gluegen.opengl.GLEmitter;
import com.jogamp.gluegen.opengl.GLJavaMethodBindingEmitter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Emitter producing NativeSignature attributes.
 */
public class NativeSignatureEmitter extends GLEmitter {

    @Override
    protected List<? extends FunctionEmitter> generateMethodBindingEmitters(Set<MethodBinding> methodBindingSet, FunctionSymbol sym) throws Exception {
        List<? extends FunctionEmitter> res = super.generateMethodBindingEmitters(methodBindingSet, sym);
        for (Iterator<? extends FunctionEmitter> iter = res.iterator(); iter.hasNext(); ) {
            FunctionEmitter emitter = iter.next();
            if (!(emitter instanceof JavaMethodBindingEmitter)) {
                iter.remove();
            }
        }
        if (res.isEmpty()) {
            return res;
        }
        PrintWriter writer = (getConfig().allStatic() ? javaWriter() : javaImplWriter());
        List<FunctionEmitter> processed = new ArrayList<FunctionEmitter>();
        for (Iterator<? extends FunctionEmitter> iter = res.iterator(); iter.hasNext(); ) {
            FunctionEmitter emitter = iter.next();
            if (emitter.getDefaultOutput() != writer) {
                processed.add(emitter);
                iter.remove();
            }
        }
        while (!res.isEmpty()) {
            List<JavaMethodBindingEmitter> emittersForBinding = new ArrayList<JavaMethodBindingEmitter>();
            JavaMethodBindingEmitter emitter = (JavaMethodBindingEmitter) res.remove(0);
            emittersForBinding.add(emitter);
            MethodBinding binding = emitter.getBinding();
            for (Iterator<? extends FunctionEmitter> iter = res.iterator(); iter.hasNext(); ) {
                JavaMethodBindingEmitter emitter2 = (JavaMethodBindingEmitter) iter.next();
                if (emitter2.getBinding() == binding) {
                    emittersForBinding.add(emitter2);
                    iter.remove();
                }
            }
            generateNativeSignatureEmitters(binding, emittersForBinding);
            processed.addAll(emittersForBinding);
        }
        return processed;
    }

    protected void generateNativeSignatureEmitters(MethodBinding binding, List<JavaMethodBindingEmitter> allEmitters) {
        if (allEmitters.isEmpty()) {
            return;
        }
        PrintWriter writer = (getConfig().allStatic() ? javaWriter() : javaImplWriter());
        List<JavaMethodBindingEmitter> newEmitters = new ArrayList<JavaMethodBindingEmitter>();
        for (JavaMethodBindingEmitter javaEmitter : allEmitters) {
            NativeSignatureJavaMethodBindingEmitter newEmitter = null;
            if (javaEmitter instanceof GLJavaMethodBindingEmitter) {
                newEmitter = new NativeSignatureJavaMethodBindingEmitter((GLJavaMethodBindingEmitter) javaEmitter);
            } else {
                newEmitter = new NativeSignatureJavaMethodBindingEmitter(javaEmitter, this);
            }
            newEmitters.add(newEmitter);
        }
        allEmitters.clear();
        allEmitters.addAll(newEmitters);
        if (signatureContainsStrings(binding) && !haveEmitterWithBody(allEmitters)) {
            NativeSignatureJavaMethodBindingEmitter javaEmitter = findEmitterWithWriter(allEmitters, writer);
            NativeSignatureJavaMethodBindingEmitter emitter = new NativeSignatureJavaMethodBindingEmitter(javaEmitter);
            emitter.removeModifier(JavaMethodBindingEmitter.PUBLIC);
            emitter.addModifier(JavaMethodBindingEmitter.PRIVATE);
            emitter.setForImplementingMethodCall(true);
            emitter.setForDirectBufferImplementation(true);
            allEmitters.add(emitter);
            javaEmitter.removeModifier(JavaMethodBindingEmitter.NATIVE);
            javaEmitter.setEmitBody(true);
        }
    }

    protected boolean signatureContainsStrings(MethodBinding binding) {
        for (int i = 0; i < binding.getNumArguments(); i++) {
            JavaType type = binding.getJavaArgumentType(i);
            if (type.isString() || type.isStringArray()) {
                return true;
            }
        }
        JavaType retType = binding.getJavaReturnType();
        if (retType.isString() || retType.isStringArray()) {
            return true;
        }
        return false;
    }

    protected boolean haveEmitterWithBody(List<JavaMethodBindingEmitter> allEmitters) {
        for (JavaMethodBindingEmitter emitter : allEmitters) {
            if (!emitter.signatureOnly()) {
                return true;
            }
        }
        return false;
    }

    protected NativeSignatureJavaMethodBindingEmitter findEmitterWithWriter(List<JavaMethodBindingEmitter> allEmitters, PrintWriter writer) {
        for (JavaMethodBindingEmitter jemitter : allEmitters) {
            NativeSignatureJavaMethodBindingEmitter emitter = (NativeSignatureJavaMethodBindingEmitter) jemitter;
            if (emitter.getDefaultOutput() == writer) {
                return emitter;
            }
        }
        throw new RuntimeException("Unexpectedly failed to find an emitter with the given writer");
    }
}
