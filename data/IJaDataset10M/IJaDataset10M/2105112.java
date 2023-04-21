package net.dromard.movies.gui.details;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import net.dromard.movies.gui.actions.LoadMovieDetailsPanelAction;
import net.dromard.movies.gui.beans.JBigButton;
import net.dromard.movies.gui.beans.JThumbnail;
import net.dromard.movies.model.Movie;

public class JMovieButton extends JBigButton {

    private static final long serialVersionUID = -6286481500347068992L;

    protected Movie movie;

    public JMovieButton(final Movie movie) {
        super(movie.getTitle());
        setToolTipText(movie.getTitle());
        this.movie = movie;
        add(new JThumbnail() {

            @Override
            protected Image loadThumbnail() {
                return movie.getCover();
            }
        }, BorderLayout.CENTER);
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        super.mouseReleased(evt);
        new LoadMovieDetailsPanelAction(movie).run();
    }
}
