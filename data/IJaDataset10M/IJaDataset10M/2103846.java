package net.sf.chex4j.internal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

class ClassTransformerImpl implements ClassTransformer {

    private final Set<ClassInstrumentor> classInstrumentors;

    ClassTransformerImpl() {
        this.classInstrumentors = new HashSet<ClassInstrumentor>(3);
        this.classInstrumentors.add(new ConstructorInstrumentor());
        this.classInstrumentors.add(new ClassMethodsInstrumentor());
        this.classInstrumentors.add(new InterfaceMethodsInstrumentor());
    }

    public byte[] transformClassfile(byte[] classfileBuffer, String canonicalName) throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException {
        boolean workDone = false;
        CtClass ctClass = parseByteCode(canonicalName, classfileBuffer);
        if (!ctClass.isInterface()) {
            for (ClassInstrumentor classInstrumentor : this.classInstrumentors) {
                workDone |= classInstrumentor.instrumentClass(ctClass);
            }
        }
        byte[] returned = null;
        if (workDone) {
            returned = ctClass.toBytecode();
        }
        return returned;
    }

    private CtClass parseByteCode(String className, byte[] classfileBuffer) throws NotFoundException {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
        CtClass ctClass = cp.get(className);
        return ctClass;
    }
}
