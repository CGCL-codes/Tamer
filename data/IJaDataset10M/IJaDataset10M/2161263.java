package net.sf.javascribe.generator.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.generator.SourceFile;
import net.sf.javascribe.generator.accessor.ProcessorTypesAccessor;
import net.sf.javascribe.generator.context.processor.ComponentProcessorContext;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavaSourceFile extends SourceFile {

    protected JavaClassDefinition publicClass = null;

    protected List<String> imports = null;

    String packageName = null;

    ProcessorTypesAccessor types = null;

    ComponentProcessorContext ctx = null;

    public ComponentProcessorContext getComponentProcessorContext() {
        return ctx;
    }

    public void addImport(String importStatement) {
        if (!imports.contains(importStatement)) {
            imports.add(importStatement);
        }
    }

    public JavaSourceFile(ComponentProcessorContext c) {
        if (c == null) {
            throw new IllegalArgumentException("JavaScribe internal error: Tried to create Java file without processor context.");
        }
        imports = new ArrayList<String>();
        ctx = c;
        publicClass = new JavaClassDefinition(this);
    }

    public JavaClassDefinition getPublicClass() {
        return publicClass;
    }

    public StringBuffer getSource() throws ProcessingException {
        StringBuffer ret = new StringBuffer();
        String value = null;
        ret.append('\n');
        ret.append("package " + getPackageName() + ";\n\n");
        for (int i = 0; i < imports.size(); i++) {
            value = imports.get(i);
            ret.append("import " + value + ";\n");
        }
        ret.append(publicClass.getSource());
        return ret;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPath() {
        return ctx.getProperty("java.source") + File.separatorChar + getRelativePath();
    }

    public String getRelativePath() {
        String ret = null;
        if (packageName == null) packageName = "";
        ret = packageName + '.' + publicClass.getClassName();
        ret = ret.replace('.', File.separatorChar) + ".java";
        return ret;
    }
}
