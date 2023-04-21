package booksandfilms.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface QuoteListChangedEventHandler extends EventHandler {

    void onChangeQuoteList(QuoteListChangedEvent event);
}
