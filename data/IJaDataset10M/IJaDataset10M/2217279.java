package org.zeroturnaround.jrebel.mybatis.cbp;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtConstructor;
import org.zeroturnaround.bundled.javassist.CtField;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;
import org.zeroturnaround.jrebel.mybatis.SqlMapReloader;

public class DefaultSqlSessionFactoryCBP extends JavassistClassBytecodeProcessor {

    public void process(ClassPool cp, ClassLoader cl, CtClass ctClass) throws Exception {
        ctClass.addField(new CtField(cp.get(SqlMapReloader.class.getName()), "reloader", ctClass));
        CtConstructor[] constructors = ctClass.getConstructors();
        for (int i = 0; i < constructors.length; ++i) {
            CtConstructor constructor = constructors[i];
            constructor.insertAfter("reloader = " + SqlMapReloader.class.getName() + ".getInstance();reloader.setDefaultSqlSessionFactory($0);");
        }
        ctClass.getDeclaredMethod("openSessionFromDataSource").insertBefore("if (reloader != null) {  reloader.reload();}");
    }
}
