package booksandfilms.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import booksandfilms.client.PersistentServiceAsync;
import booksandfilms.client.entities.Quote;
import booksandfilms.client.event.QuoteDeleteEvent;
import booksandfilms.client.event.QuoteEditEvent;
import booksandfilms.client.helper.ClickPoint;
import booksandfilms.client.helper.RPCCall;

public class QuotePopupPresenter implements Presenter {

    public interface Display {

        HasClickHandlers getEditButton();

        HasClickHandlers getDeleteButton();

        HasText getQuoteNameLabel();

        void hide();

        void setName(String displayName);

        void setNameAndShow(String displayName, ClickPoint location);

        Widget asWidget();
    }

    private Quote quote;

    private final PersistentServiceAsync persistentService;

    private final SimpleEventBus eventBus;

    private Display display;

    public QuotePopupPresenter(PersistentServiceAsync persistentService, SimpleEventBus eventBus, Display display, Quote quote) {
        this.persistentService = persistentService;
        this.eventBus = eventBus;
        this.display = display;
        this.quote = quote;
        bind();
    }

    public void bind() {
        this.display.getEditButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.hide();
                eventBus.fireEvent(new QuoteEditEvent(quote.getId()));
            }
        });
        this.display.getDeleteButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.hide();
                if (Window.confirm("Are you sure?")) {
                    deleteQuote(quote);
                }
            }
        });
    }

    private void deleteQuote(final Quote quote) {
        new RPCCall<Void>() {

            @Override
            protected void callService(AsyncCallback<Void> cb) {
                persistentService.deleteQuote(quote, cb);
            }

            @Override
            public void onSuccess(Void result) {
                eventBus.fireEvent(new QuoteDeleteEvent(quote));
            }

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof booksandfilms.shared.exception.NotLoggedInException) {
                    Window.alert("You need to login first");
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
