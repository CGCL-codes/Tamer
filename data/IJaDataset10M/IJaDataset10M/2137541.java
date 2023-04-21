package jw.bznetwork.client;

import java.util.HashMap;
import java.util.Map;
import jw.bznetwork.client.ui.Spacer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class VerticalScreen implements Screen {

    protected VerticalPanel widget = new VerticalPanel();

    protected MainScreen parent;

    private Spacer spacer = new Spacer("1px", "1px");

    private DockPanel wrapper;

    /**
     * Does nothing. Subclasses can override as needed.
     */
    public void historyChanged(Map<String, String> parameters) {
    }

    public Widget getWidget() {
        if (wrapper == null) {
            wrapper = new DockPanel();
            wrapper.setWidth("100%");
            wrapper.add(spacer, wrapper.WEST);
            wrapper.add(widget, wrapper.CENTER);
        }
        return wrapper;
    }

    protected void setSpacing(String spacing) {
        spacer.setWidth(spacing);
    }

    @Override
    public void reselect(Map<String, String> params) {
        if (params == null) addToHistory(null);
        reselect();
    }

    @Override
    public void select(Map<String, String> params) {
        if (params == null) addToHistory(null);
        select();
    }

    protected void select() {
    }

    protected void reselect() {
    }

    /**
     * Does nothing. Subclasses can override this method if they need to get
     * tick information from BZNetwork.
     */
    public void tick(int number) {
    }

    protected void addToHistory(Map<String, String> params) {
        if (params == null) params = new HashMap<String, String>();
        parent.addToHistory(this, params);
    }

    public void setParent(MainScreen screen) {
        this.parent = screen;
    }
}
