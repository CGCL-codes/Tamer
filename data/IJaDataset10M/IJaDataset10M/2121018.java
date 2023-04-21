package net.techwatch.jsni.client;

import net.techwatch.jsni.client.Raphael.Circle;
import net.techwatch.jsni.client.Raphael.Rectangle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gwt_jsni implements EntryPoint {

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        int width = Window.getClientWidth();
        int height = Window.getClientHeight();
        Raphael r = new Raphael(width, height);
        Circle c = r.new Circle(width / 2, height / 2, 20);
        Rectangle re = r.new Rectangle(width / 2 - 20, height / 2 - 20, 40, 40);
        RootPanel.get().add(r);
    }
}
