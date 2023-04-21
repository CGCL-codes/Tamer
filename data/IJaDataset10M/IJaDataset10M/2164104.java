package com.volantis.mcs.eclipse.ab.search;

import java.util.ArrayList;
import java.util.List;
import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * SearchScope whose scope is the entire workspace.
 */
public class WorkspaceScope implements SearchScope {

    /**
     * The prefix for resources messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "WorkspaceScope.";

    /**
     * The label for this SearchScope.
     */
    private static final String LABEL = SearchMessages.getString(RESOURCE_PREFIX + "label");

    /**
     * The FileExtensions that are applicable to this SearchScope.
     */
    private final FileExtension[] fileExtensions;

    /**
     * Construct a new WorkspaceScope.
     * @param fileExtensions the FileExtensions that are applicable to the
     * scope. If this parameter is null then all files are applicable.
     */
    WorkspaceScope(FileExtension[] fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public String getLabel() {
        return LABEL;
    }

    public IFile[] getFiles() {
        IContainer root = ResourcesPlugin.getWorkspace().getRoot();
        List fileMembers = new ArrayList();
        SearchSupport.populateFileMembers(fileMembers, root, fileExtensions);
        IFile files[] = new IFile[fileMembers.size()];
        return (IFile[]) fileMembers.toArray(files);
    }
}
