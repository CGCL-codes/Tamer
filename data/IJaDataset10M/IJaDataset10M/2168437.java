package org.xaware.designer.lint;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

/**
 * This class provides a common XALint to process all the selected resources
 * 
 * @author hcurtis
 * 
 */
public class XALintCheckXAScriptResourceVisitor implements IResourceVisitor {

    private final XALint lint = new XALint();

    public boolean visit(final IResource resource) {
        lint.checkXML(resource);
        return true;
    }
}
