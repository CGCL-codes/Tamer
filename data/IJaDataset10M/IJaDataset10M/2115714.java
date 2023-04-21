package dk.hewison.client.slideshow;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import dk.hewison.client.mvc.Action;
import dk.hewison.client.mvc.widgets.MButton;

/**
 * @author John Hewison
 * @author $LastChangedBy: john.hewison $:  $ $LastChangedDate: 2009-02-09 07:39:39 -0500 (Mon, 09 Feb 2009) $:
 * @version $Revision: 363 $:
 */
public class SlideshowControlView extends PopupPanel {

    private Timer timer;

    private static final int HIDE_AFTER_MS = 3000;

    public SlideshowControlView(Action... actions) {
        super(false, true);
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);
        panel.setStyleName("slideshowcontroller");
        setStyleName("slideshowcontroller");
        for (Action action : actions) {
            panel.add(new MButton(action));
        }
        setWidget(panel);
        timer = new Timer() {

            @Override
            public void run() {
                hide();
            }
        };
    }

    @Override
    public void hide() {
        super.hide();
        timer.cancel();
    }

    public void show() {
        super.show();
        int a = getParent().getOffsetWidth();
        int b = getOffsetWidth();
        int x = (a - b) / 2;
        setPopupPosition(x, 4);
        timer.cancel();
        timer.schedule(HIDE_AFTER_MS);
    }
}
