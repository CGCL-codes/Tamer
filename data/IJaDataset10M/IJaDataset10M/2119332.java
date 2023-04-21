package net.sf.beatrix.ui.detectoreditor.wizards;

import java.util.Collection;
import net.sf.beatrix.core.module.Module;
import net.sf.beatrix.ui.viewers.outline.ContentOutlineNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ModuleFilter extends ViewerFilter {

    private Collection<Module> modules;

    public ModuleFilter(Collection<Module> modules) {
        this.modules = modules;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof ContentOutlineNode) {
            element = ((ContentOutlineNode) element).getContent();
        }
        return !(element instanceof Module) || !modules.contains(element);
    }
}
