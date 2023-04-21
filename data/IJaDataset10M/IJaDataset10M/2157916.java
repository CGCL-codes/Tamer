package org.eclipse.mylyn.internal.bugzilla.ui.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaPlugin;
import org.eclipse.mylyn.internal.bugzilla.core.IBugzillaConstants;
import org.eclipse.mylyn.internal.bugzilla.core.internal.Favorite;
import org.eclipse.mylyn.internal.bugzilla.ui.BugzillaImages;
import org.eclipse.mylyn.internal.bugzilla.ui.FavoritesView;
import org.eclipse.mylyn.internal.bugzilla.ui.search.BugzillaSearchResultView;

/**
 * Bookmark a returned Bugzilla Search result.
 */
public class AddFavoriteAction extends AbstractFavoritesAction {

    /** Selected objects */
    private List<Favorite> selected;

    /** The view where the Bugzilla search results are displayed */
    private BugzillaSearchResultView resultView;

    /**
	 * Constructor
	 * 
	 * @param text
	 *            The label for the context menu item
	 * @param resultView
	 *            The view where the Bugzilla search results are displayed
	 */
    public AddFavoriteAction(String text, BugzillaSearchResultView resultView) {
        setText(text);
        setImageDescriptor(BugzillaImages.IMG_TOOL_ADD_TO_FAVORITES);
        this.resultView = resultView;
        selected = null;
    }

    /**
	 * Bookmark the selected items
	 * 
	 * @see org.eclipse.ui.actions.ActionDelegate#run(IAction)
	 */
    @Override
    public void run() {
        FavoritesView.checkWindow();
        selected = new ArrayList<Favorite>();
        buildSelection();
        for (int i = 0; i < selected.size(); i++) {
            BugzillaPlugin.getDefault().getFavorites().add(selected.get(i));
        }
        FavoritesView.add();
        FavoritesView.updateActionEnablement();
    }

    /**
	 * Gets the entire selection and puts each bug report into a list. Only the
	 * relevent parts of each bug report are put into the list.
	 */
    @SuppressWarnings("unchecked")
    private void buildSelection() {
        ISelection s = resultView.getViewer().getSelection();
        if (s instanceof IStructuredSelection) {
            IStructuredSelection selection = (IStructuredSelection) s;
            for (Iterator<IMarker> it = selection.iterator(); it.hasNext(); ) {
                IMarker marker = it.next();
                try {
                    Integer attributeId = (Integer) marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_ID);
                    String repositoryUrl = (String) marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_REPOSITORY);
                    String description = (String) marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_DESC);
                    String query = (String) marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_QUERY);
                    HashMap<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_ID, attributeId);
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_REPOSITORY, repositoryUrl);
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_PRIORITY, marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_PRIORITY));
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_SEVERITY, marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_SEVERITY));
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_STATE, marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_STATE));
                    attributes.put(IBugzillaConstants.HIT_MARKER_ATTR_RESULT, marker.getAttribute(IBugzillaConstants.HIT_MARKER_ATTR_RESULT));
                    Favorite favorite = new Favorite(repositoryUrl, attributeId.intValue(), description, query, attributes);
                    selected.add(favorite);
                } catch (CoreException ignored) {
                }
            }
        }
    }
}
