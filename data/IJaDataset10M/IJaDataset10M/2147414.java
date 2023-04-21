package cnaf.sidoc.ide.publishers.core.internal.resources;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.resource.AbstractResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;

public class XSLBaseDirRequestor extends AbstractResourceRequestor {

    public static final IResourceRequestor INSTANCE = new XSLBaseDirRequestor();

    private static final String XSL_EXT = "xsl";

    @Override
    protected boolean accept(Object selectedNode, IResource rootContainer, IFile file, IURIResolver resolver, String matching, boolean fullMatch) {
        return resolver.accept(selectedNode, rootContainer, file, matching, fullMatch);
    }

    @Override
    public boolean acceptContainer() {
        return true;
    }

    @Override
    public boolean acceptFile() {
        return false;
    }
}
