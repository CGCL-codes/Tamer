package org.cubictest.ui.gef.factory;

import org.cubictest.model.Page;
import org.cubictest.model.SubTest;
import org.eclipse.core.resources.IFile;
import org.eclipse.gef.requests.CreationFactory;

public class SubTestFactory implements CreationFactory {

    private IFile file;

    public SubTestFactory() {
    }

    public Object getNewObject() {
        SubTest subTest = new SubTest(file.getFullPath().toPortableString());
        return subTest;
    }

    public Object getObjectType() {
        return Page.class;
    }

    public void setFile(IFile file) {
        this.file = file;
    }
}
