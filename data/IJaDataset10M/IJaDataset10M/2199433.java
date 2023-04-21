package com.msli.rcp.view;

import com.msli.core.util.CoreUtils;
import com.msli.rcp.editor.ObjectEditorOpener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Abstract base class for showing a view of the contents of a target object.
 * The viewer type can be fixed, or dynamically resolved according to the target
 * object at run time.
 * <p>
 * Consistent with the concept of a view, no input is provided to the view.
 * Instead, it is presumed that the target data is, or will be, selected and
 * revealed (such as by a "new object" wizard), and the view will present the
 * selected target object accordingly.
 * @author jonb
 * @see ObjectEditorOpener
 * @param <T> The target object type.
 */
public abstract class ObjectViewShower<T> {

    /**
	 * Used for extension.
	 */
    public ObjectViewShower() {
    }

    /**
	 * Called by the system to open a view in the active workbench page for the
	 * target object. Calls resolveViewId() to resolve the view type. If no
	 * active page, or no view ID can be resolved, nothing happens.
	 * @param window Temp input workbench window. Never null.
	 * @param target Shared exposed target object. Never null.
	 * @return True If a view was successfully opened for the target.
	 */
    public boolean showView(IWorkbenchWindow window, T target) {
        CoreUtils.assertNonNullArg(window);
        CoreUtils.assertNonNullArg(target);
        IWorkbenchPage page = window.getActivePage();
        if (page == null) return false;
        String viewId = resolveViewId(target);
        if (viewId == null) return false;
        try {
            page.showView(viewId);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
	 * Called by the system to resolve which view will be used to open the
	 * specified object.
	 * @param target Temp input target object. Never null.
	 * @return RCP ID for the view. If null, the target will be ignored and no
	 * view will be opened for it.
	 */
    protected abstract String resolveViewId(T target);
}
