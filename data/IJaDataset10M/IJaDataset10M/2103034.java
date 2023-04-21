package gemini.castor.ui.client.page.content.customer.customerlist;

import com.google.gwt.event.shared.EventHandler;

public interface CustomerHandler extends EventHandler {

    void onEdit(CustomerEvent levelEvent);

    void onBackToList(CustomerEvent levelEvent);

    void onNew(CustomerEvent levelEvent);
}
