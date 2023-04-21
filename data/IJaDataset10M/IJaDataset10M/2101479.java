package pl.xperios.rdk.client.main.menu;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

/**
 *
 * @author Praca
 */
@Events(startView = MenuView.class, module = MenuModule.class)
public interface MenuEventBus extends EventBus {

    @Event(handlers = { MenuPresenter.class })
    public void loadMenu();

    @Event(forwardToParent = true)
    public void loadStartModule();

    @Event(forwardToParent = true)
    public void setMenu(Widget asWidget);

    @Event(forwardToParent = true)
    public void loadBlogModule();

    @Event(forwardToParent = true)
    public void changeBody(Widget contentPanel);
}
