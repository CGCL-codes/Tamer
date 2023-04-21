package org.maximodeveloper.filedb;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.maximodeveloper.popup.actions.FileSelectionHelper;

public class DeleteClassListener implements IResourceChangeListener {

    public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta delta = event.getDelta();
        final List<String> deletedClasses = new ArrayList<String>();
        try {
            delta.accept(new IResourceDeltaVisitor() {

                public boolean visit(IResourceDelta delta) throws CoreException {
                    if (delta.getKind() != IResourceDelta.REMOVED) {
                        return true;
                    }
                    IResource res = delta.getResource();
                    if (res.getFileExtension() == null) {
                        return true;
                    }
                    if (res.getFileExtension().equals("java")) {
                        String className = new FileSelectionHelper().getClassNameFromSelection(res.getFullPath().toString());
                        deletedClasses.add(className);
                    }
                    return true;
                }
            });
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (!deletedClasses.isEmpty()) {
            new ClassDetacher().detach(true, deletedClasses);
        }
    }
}
