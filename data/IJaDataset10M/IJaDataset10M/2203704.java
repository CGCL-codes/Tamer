package pl.net.bluesoft.archetype.gwt.webapp.client.presenter;

import com.google.gwt.user.client.Window;
import pl.net.bluesoft.archetype.gwt.webapp.client.events.ChangeTabEvent;
import pl.net.bluesoft.archetype.gwt.webapp.client.events.ChangeTabEventHandler;
import pl.net.bluesoft.archetype.gwt.webapp.client.events.OrderSelectedEvent;
import pl.net.bluesoft.archetype.gwt.webapp.client.events.OrderSelectedEventHandler;
import pl.net.bluesoft.archetype.gwt.webapp.client.view.enums.Tabs;

/**
 * Presenter for the main panel
 * @author pstepaniak
 *
 */
public class MainPanelPresenter extends BasePresenter {

    public interface Display {

        void show(Tabs tab);
    }

    private Display display;

    public void bindDisplay(final Display display) {
        this.display = display;
        this.eventBus.addHandler(OrderSelectedEvent.TYPE, new OrderSelectedEventHandler() {

            public void onOrderSelected(final OrderSelectedEvent event) {
                Window.alert("Event recieved - changing tab");
                display.show(Tabs.ORDER_DETAILS);
            }
        });
        this.eventBus.addHandler(ChangeTabEvent.TYPE, new ChangeTabEventHandler() {

            public void onChangeTab(final Tabs tab) {
                Window.alert("Event recieved - changing tab for: " + tab);
                display.show(tab);
            }
        });
    }
}
