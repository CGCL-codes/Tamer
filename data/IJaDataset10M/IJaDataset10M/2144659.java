package org.developerservices.moviedb.application.contextmenu.movie;

import org.developerservices.moviedb.application.utils.Icons;
import org.eclipse.jface.action.Action;

/**
 * @author rene
 *
 */
public class DeleteMovieAction extends Action {

    private IMovieListCtxMenu view;

    public DeleteMovieAction(IMovieListCtxMenu view) {
        this.view = view;
        this.setText("Delete movie");
        this.setImageDescriptor(Icons.getIconDescriptor(Icons.ICON_DELETE));
    }
}
