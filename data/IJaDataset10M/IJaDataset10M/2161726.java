package net.sf.opendf.eclipse.plugin.debug.breakpoints;

import net.sf.opendf.eclipse.plugin.editors.CALEditor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Creates a toggle breakpoint adapter
 * 
 * @author Rob Esser
 * @version 14th April 2009
 */
public class ActorEditorAdapterFactory implements IAdapterFactory {

    /**
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof CALEditor) {
            ITextEditor editorPart = (ITextEditor) adaptableObject;
            IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
            if (resource != null) {
                String extension = resource.getFileExtension();
                if (extension != null && extension.equals("cal")) {
                    if (adapterType.equals(IToggleBreakpointsTarget.class)) {
                        return new ActorBreakpointAdapter();
                    }
                    if (adapterType.equals(IRunToLineTarget.class)) {
                        return new ActorRunToLineAdapter();
                    }
                }
            }
        }
        return null;
    }

    /** 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
    @SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
        return new Class[] { IToggleBreakpointsTarget.class, IRunToLineTarget.class };
    }
}
