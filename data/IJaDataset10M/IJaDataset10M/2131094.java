package gwtip.sotu.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import gwtip.sotu.client.Controller;
import gwtip.sotu.client.remote.ConversationDescriptor;

/**
 *
 * @author rcooper
 */
public class ConversationList extends ListBox {

    private final Controller controller = Controller.getInstance();

    public ConversationDescriptor[] descriptors;

    private final AsyncCallback callback = new AsyncCallback() {

        public void onSuccess(Object result) {
            descriptors = (ConversationDescriptor[]) result;
            clear();
            for (int i = 0; i < descriptors.length; i++) {
                addItem(descriptors[i].name);
            }
        }

        public void onFailure(Throwable caught) {
            clear();
            addItem("Error fetching");
        }
    };

    /** Creates a new instance of ConversationList */
    public ConversationList() {
        super();
        this.setVisibleItemCount(20);
        controller.listConversations(callback);
        Timer t = new Timer() {

            public void run() {
                controller.listConversations(callback);
            }
        };
        t.scheduleRepeating(5000);
    }
}
