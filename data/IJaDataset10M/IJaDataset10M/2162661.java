package booksandfilms.client.presenter;

import booksandfilms.client.PersistentServiceAsync;
import booksandfilms.client.entities.Film;
import booksandfilms.client.event.FilmDeleteEvent;
import booksandfilms.client.event.FilmEditEvent;
import booksandfilms.client.helper.ClickPoint;
import booksandfilms.client.helper.RPCCall;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class FilmPopupPresenter implements Presenter {

    public interface Display {

        HasClickHandlers getEditButton();

        HasClickHandlers getDeleteButton();

        HasText getFilmTitleLabel();

        void hide();

        void setTitle(String displayName);

        void setNameAndShow(String displayName, ClickPoint location);

        Widget asWidget();
    }

    private Film film;

    private final PersistentServiceAsync persistentService;

    private final SimpleEventBus eventBus;

    private Display display;

    public FilmPopupPresenter(PersistentServiceAsync persistentService, SimpleEventBus eventBus, Display display, Film film) {
        this.persistentService = persistentService;
        this.eventBus = eventBus;
        this.display = display;
        this.film = film;
        bind();
    }

    public void bind() {
        this.display.getEditButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.hide();
                eventBus.fireEvent(new FilmEditEvent(film.getId()));
            }
        });
        this.display.getDeleteButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.hide();
                if (Window.confirm("Are you sure?")) {
                    deleteFilm(film);
                }
            }
        });
    }

    private void deleteFilm(final Film film) {
        new RPCCall<Void>() {

            @Override
            protected void callService(AsyncCallback<Void> cb) {
                persistentService.deleteFilm(film, cb);
            }

            @Override
            public void onSuccess(Void result) {
                eventBus.fireEvent(new FilmDeleteEvent(film));
            }

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof booksandfilms.shared.exception.NotLoggedInException) {
                    Window.alert("You need to login first");
                } else if (caught instanceof booksandfilms.shared.exception.CannotDeleteException) {
                    Window.alert("Cannot delete a Film that has Quotes");
                } else {
                    Window.alert("An error occurred: " + caught.toString());
                }
            }
        }.retry(3);
    }

    public void go() {
    }

    @Override
    public void go(HasWidgets container) {
    }
}
