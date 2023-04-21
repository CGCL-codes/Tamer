package gameslave.util;

import gameslave.db.Entity;
import groovy.lang.GroovyClassLoader;
import java.util.ArrayList;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.Phases;
import org.codehaus.groovy.control.SourceUnit;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * @author Dobes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CacheableScript {

    final transient Entity myEntity;

    String script;

    byte[][] scriptClassBytes;

    Class scriptClass;

    public CacheableScript(Entity myEntity) {
        this.myEntity = myEntity;
    }

    public byte[][] getScriptClassBytes() {
        return scriptClassBytes;
    }

    public void setScriptClassBytes(byte[][] classBytes) {
        this.scriptClassBytes = classBytes;
    }

    public Class getScriptClass() {
        return scriptClass;
    }

    public void setScriptClass(Class scriptClass) {
        this.scriptClass = scriptClass;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        if (this.script != null && this.scriptClassBytes != null && (script == null || !script.equals(this.script))) {
            this.scriptClassBytes = null;
            this.scriptClass = null;
        }
        this.script = script;
    }

    public void compileScript() throws CompilationFailedException {
        final CompilationUnit unit = new CompilationUnit();
        final String scriptName = getScriptName() + ".groovy";
        StringBuffer scriptBuf = new StringBuffer();
        scriptBuf.append("import gameslave.d20.system.*;\n");
        scriptBuf.append("import gameslave.dnd35.db.*;\n");
        scriptBuf.append("import gameslave.dnd35.system.*;\n");
        scriptBuf.append("import gameslave.dnd35.summary.*;\n");
        scriptBuf.append(script);
        unit.addSource(new SourceUnit(scriptName, scriptBuf.toString(), null, unit.getClassLoader()));
        final ArrayList classBytes = new ArrayList();
        unit.setClassgenCallback(new CompilationUnit.ClassgenCallback() {

            public void call(ClassVisitor writer, ClassNode node) throws CompilationFailedException {
                ClassWriter cw = (ClassWriter) writer;
                byte[] code = cw.toByteArray();
                classBytes.add(code);
            }
        });
        unit.compile(Phases.CLASS_GENERATION);
        byte[][] bs = (byte[][]) classBytes.toArray(new byte[classBytes.size()][]);
        setScriptClassBytes(bs);
    }

    public void defineScriptClass() {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        for (int i = 0; i < scriptClassBytes.length; i++) {
            byte[] classBytes = scriptClassBytes[i];
            Class theClass = groovyClassLoader.defineClass(null, classBytes);
            if (i == 0) scriptClass = theClass;
        }
    }

    public String getScriptName() {
        return "gameslave/scripts/runtime/" + myEntity.getClass().getName().replace('.', '/') + "Script" + myEntity.getId();
    }
}
