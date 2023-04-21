package net.dromard.movies.gui.actions;

import net.dromard.movies.gui.beans.MainPanel;
import net.dromard.movies.gui.details.JMovie;
import net.dromard.movies.model.Movie;

/**
 * @author Gabriel Dromard
 */
public class LoadMovieDetailsPanelAction extends AbstractLoadPanelAction {

    private Movie movie;

    public LoadMovieDetailsPanelAction(final Movie movie) {
        setMessage(movie.getTitle());
        this.movie = movie;
    }

    @Override
    public MainPanel loadPanel() {
        return new JMovie(movie);
    }
}
