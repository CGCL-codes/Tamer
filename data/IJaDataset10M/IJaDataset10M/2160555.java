package com.volantis.mcs.eclipse.ab.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * SearchScope derived from a selection.
 */
public class SelectionScope implements SearchScope {

    /**
     * The prefix for resources messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "SelectionScope.";

    /**
     * The label for this SearchScope.
     */
    private static final String LABEL = SearchMessages.getString(RESOURCE_PREFIX + "label");

    /**
     * The ISelection associated with this scope.
     */
    private final ISelection selection;

    /**
     * The FileExtensions applicable to the search.
     */
    private final FileExtension[] fileExtensions;

    /**
     * Construct a new SelectionScope.
     * @param selection the ISelection for this SelectionScope.
     * @param fileExtensions the FileExtensions that are applicable to the
     * scope. If this parameter is null then all files are applicable.
     */
    SelectionScope(ISelection selection, FileExtension[] fileExtensions) {
        assert (selection != null);
        this.selection = selection;
        this.fileExtensions = fileExtensions;
    }

    public String getLabel() {
        return LABEL;
    }

    public IFile[] getFiles() {
        IFile files[];
        if (!selection.isEmpty()) {
            List fileMembers = new ArrayList();
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Iterator adaptables = structuredSelection.iterator();
            do {
                IAdaptable adaptable = (IAdaptable) adaptables.next();
                if (adaptable instanceof IResource) {
                    SearchSupport.populateFileMembers(fileMembers, (IResource) adaptable, fileExtensions);
                }
            } while (adaptables.hasNext());
            files = new IFile[fileMembers.size()];
            files = (IFile[]) fileMembers.toArray(files);
        } else {
            files = new IFile[0];
        }
        return files;
    }
}
