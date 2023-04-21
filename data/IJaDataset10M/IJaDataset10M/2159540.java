package booksandfilms.client.event;

import booksandfilms.client.entities.Director;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FilmAddEvent extends GwtEvent<FilmAddEventHandler> implements EventHandler {

    public static Type<FilmAddEventHandler> TYPE = new Type<FilmAddEventHandler>();

    private final Director director;

    public FilmAddEvent(Director director) {
        this.director = director;
    }

    public Director getDirector() {
        return director;
    }

    @Override
    public Type<FilmAddEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FilmAddEventHandler handler) {
        handler.onAddFilm(this);
    }
}
