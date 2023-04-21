package egu.plugin.preference.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;
import egu.plugin.preference.PropertyConstant;

public class CompositeListLibrary extends CompositeList {

    private CompositeListLibPath listLibPath = null;

    public CompositeListLibrary(IProject project, Composite parent, int style) {
        super(project, parent, style);
    }

    public void setListLibPath(CompositeListLibPath a) {
        listLibPath = a;
    }

    @Override
    String getPropertyKey() {
        return PropertyConstant.pathLibraryFiles;
    }

    public String getAddFileDialogTitle() {
        return "library file";
    }

    public String getAddFileDialogMsg() {
        return "Select a library";
    }

    @Override
    public void add(IPath resource) {
        list.add(resource.lastSegment());
        arrayPath.add(resource);
        if (listLibPath != null) {
            IPath libPath = resource.removeLastSegments(1);
            listLibPath.add(libPath);
        }
    }
}
