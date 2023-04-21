package test;

import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.Widget;

/**
 *
 * @author Matthias Mann
 */
public class ScrollPaneDemoDialog1 extends FadeFrame {

    private ScrollPane scrollPane;

    public ScrollPaneDemoDialog1() {
        Widget scrolledWidget = new Widget();
        scrolledWidget.setTheme("/scrollPaneDemoContent");
        scrollPane = new ScrollPane(scrolledWidget);
        scrollPane.setTheme("/scrollpane");
        setTheme("scrollPaneDemoDialog1");
        setTitle("ScrollPane");
        add(scrollPane);
    }

    public void centerScrollPane() {
        scrollPane.updateScrollbarSizes();
        scrollPane.setScrollPositionX(scrollPane.getMaxScrollPosX() / 2);
        scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY() / 2);
    }
}
