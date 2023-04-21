package br.com.caelum.testslicer.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.Opcode;
import br.com.caelum.testslicer.registry.Testslicer;

public class JavassistBytecodeModifier implements Opcode {

    private final AntTaskLogWriter log;

    private final boolean shouldDefineTestName;

    private final ClassPool pool;

    public JavassistBytecodeModifier(AntTaskLogWriter log, boolean defineTestName, ClassPool pool) {
        this.pool = pool;
        this.log = log;
        this.shouldDefineTestName = defineTestName;
    }

    public void enhance(File from, File to) throws FileNotFoundException, IOException, RuntimeException, CannotCompileException {
        CtClass type = pool.makeClass(new FileInputStream(from));
        for (CtMethod method : type.getDeclaredMethods()) {
            if (method.getDeclaringClass().equals(type) && (!isAbstract(method))) {
                if (Modifier.isStatic(method.getModifiers()) || !(shouldDefineTestName)) {
                    method.insertBefore(onlyRegister(method, type));
                } else {
                    method.insertBefore(registerAndIncrease(method, type));
                    method.insertAfter(decreaseCode(method), true);
                }
            }
        }
        log.debug("Instrumenting to file " + to.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(to);
        fos.write(type.toBytecode());
        fos.close();
    }

    private String decreaseCode(CtMethod method) {
        return "{" + debugCode(method) + Testslicer.class.getName() + ".getRegistry().decrease();}";
    }

    private String debugCode(CtMethod method) {
        return "";
    }

    private String onlyRegister(CtMethod method, CtClass type) {
        String code = "{" + debugCode(method) + Testslicer.class.getName() + ".getRegistry().simplyRegister(\"" + type.getName() + "\");}";
        return code;
    }

    private String registerAndIncrease(CtMethod method, CtClass type) {
        String code = "{" + debugCode(method) + Testslicer.class.getName() + ".getRegistry().register(\"" + type.getName() + "\" ,this);}";
        return code;
    }

    private boolean isAbstract(CtMethod method) {
        return Modifier.isAbstract(method.getModifiers());
    }
}
