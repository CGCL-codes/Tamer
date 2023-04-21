package net.sf.javascribe.generator.context.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.generator.SourceFile;
import net.sf.javascribe.generator.accessor.ExceptionTypeAccessor;
import net.sf.javascribe.generator.accessor.impl.TypesAccessorImpl;
import net.sf.javascribe.generator.platform.ApplicationPlatform;
import net.sf.javascribe.generator.util.TemplateManager;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractComponentProcessorContext implements ComponentProcessorContext {

    TemplateManager templates = null;

    HashMap<String, SourceFile> sourceFiles = new HashMap<String, SourceFile>();

    HashMap<String, Object> globalObjects = null;

    TypesAccessorImpl exceptionTypes = null;

    ApplicationPlatform applicationPlatform = null;

    public ExceptionTypeAccessor getExceptionType(String name) throws ProcessingException {
        ExceptionTypeAccessor ret = null;
        ret = (ExceptionTypeAccessor) exceptionTypes.getTypeAccessor(name);
        return ret;
    }

    public void addExceptionType(String name, ExceptionTypeAccessor a) {
        exceptionTypes.addType(a);
    }

    public Object getGlobalObject(String name) {
        return globalObjects.get(name);
    }

    public void setGlobalObject(String name, Object value) {
        globalObjects.put(name, value);
    }

    public SourceFile getSourceFile(String path) {
        SourceFile ret = null;
        ret = (SourceFile) sourceFiles.get(path);
        return ret;
    }

    public void addSourceFile(SourceFile f) {
        sourceFiles.put(f.getPath(), f);
    }

    public List<SourceFile> getSourceFiles() {
        ArrayList<SourceFile> ret = null;
        ret = new ArrayList<SourceFile>();
        Iterator<SourceFile> it = null;
        it = sourceFiles.values().iterator();
        while (it.hasNext()) {
            ret.add(it.next());
        }
        return ret;
    }

    public TemplateManager getTemplateManager() {
        return templates;
    }

    public void setTemplateManager(TemplateManager t) {
        templates = t;
    }
}
