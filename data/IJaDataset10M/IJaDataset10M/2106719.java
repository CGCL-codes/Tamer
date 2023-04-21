package com.google.gwt.eclipse.core.modules;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a GWT module resource in a JAR file.
 */
@SuppressWarnings("restriction")
public class ModuleJarResource extends AbstractModule {

    protected ModuleJarResource(IJarEntryResource jarResource) {
        super(jarResource);
    }

    public IJarEntryResource getJarEntryResource() {
        return (IJarEntryResource) storage;
    }

    public boolean isBinary() {
        return true;
    }

    @Override
    protected IDOMModel doGetModelForRead() throws IOException, CoreException {
        IModelManager modelManager = StructuredModelManager.getModelManager();
        InputStream moduleStream = storage.getContents();
        IDOMModel model = (IDOMModel) modelManager.getModelForRead(storage.getName(), moduleStream, null);
        moduleStream.close();
        return model;
    }

    @Override
    protected String doGetPackageName() {
        IPath modulePckgPath = storage.getFullPath().removeLastSegments(1).makeRelative();
        return modulePckgPath.toString().replace('/', '.');
    }
}
