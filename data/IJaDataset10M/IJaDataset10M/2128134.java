package org.remus.infomngmnt.search.builder;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.remus.infomngmnt.search.service.LuceneSearchService;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class SearchVisitor implements IResourceDeltaVisitor {

    public static final String INFO_FILE_EXTENSION = "info";

    private List<IFile> filesTodeleteFromIndex;

    private List<IFile> filesToAddToIndex;

    private final IProject project;

    public SearchVisitor(IProject project) {
        this.project = project;
    }

    public boolean visit(IResourceDelta delta) throws CoreException {
        this.filesTodeleteFromIndex = new ArrayList<IFile>();
        this.filesToAddToIndex = new ArrayList<IFile>();
        visitRecursively(delta);
        handleIndexRefresh();
        return true;
    }

    private void handleIndexRefresh() {
        getSearchService().addToIndex(this.filesTodeleteFromIndex, this.filesToAddToIndex, this.project);
        this.filesToAddToIndex.clear();
        this.filesTodeleteFromIndex.clear();
    }

    private LuceneSearchService getSearchService() {
        return LuceneSearchService.getInstance();
    }

    public void visitRecursively(final IResourceDelta delta) {
        IResourceDelta[] affectedChildren = delta.getAffectedChildren();
        for (IResourceDelta resourceDelta : affectedChildren) {
            if (resourceDelta.getResource() != null && resourceDelta.getResource().getType() == IResource.FILE && resourceDelta.getResource().getFileExtension().equals(INFO_FILE_EXTENSION) && resourceDelta.getResource().getParent().getType() == IResource.PROJECT) {
                switch(resourceDelta.getKind()) {
                    case IResourceDelta.CHANGED:
                        if (resourceDelta.getResource().getType() == IResource.FILE && resourceDelta.getResource().getFileExtension().equals(INFO_FILE_EXTENSION)) {
                            this.filesTodeleteFromIndex.add((IFile) resourceDelta.getResource());
                            this.filesToAddToIndex.add((IFile) resourceDelta.getResource());
                        }
                        break;
                    case IResourceDelta.ADDED:
                        if (resourceDelta.getResource().getType() == IResource.FILE && resourceDelta.getResource().getFileExtension().equals(INFO_FILE_EXTENSION)) {
                            this.filesToAddToIndex.add((IFile) resourceDelta.getResource());
                        }
                        break;
                    case IResourceDelta.REMOVED:
                        if (resourceDelta.getResource().getType() == IResource.FILE && resourceDelta.getResource().getFileExtension().equals(INFO_FILE_EXTENSION)) {
                            this.filesTodeleteFromIndex.add((IFile) resourceDelta.getResource());
                        }
                        break;
                    case IResourceDelta.REPLACED:
                        if (resourceDelta.getResource().getType() == IResource.FILE && resourceDelta.getResource().getFileExtension().equals(INFO_FILE_EXTENSION)) {
                            this.filesTodeleteFromIndex.add((IFile) resourceDelta.getResource());
                            this.filesToAddToIndex.add((IFile) resourceDelta.getResource());
                        }
                        break;
                }
            }
            visitRecursively(resourceDelta);
        }
    }
}
