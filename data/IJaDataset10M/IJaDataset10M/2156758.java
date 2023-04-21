package touchitem;

import touchcomponent.TouchComponentAdapter;
import touchcomponent.TouchComponentEvent;

/**
 * action item
 * @author TouchBox Team
 */
public class ActionItem extends touchitem.DefaultItem {

    private static final String DEFAULT_TEXT = "Action";

    private static final String DEFAULT_ICON = "default.png";

    public ActionItem() {
        this(DEFAULT_TEXT, DEFAULT_ICON);
    }

    public ActionItem(String text, String icon) {
        super();
        super.setText(text);
        super.setIcon(icon);
        super.setPressed(false);
        addTouchComponentListener(new TouchComponentAdapter() {

            @Override
            public void TouchPressed(TouchComponentEvent event) {
                if (!isActive()) {
                    return;
                }
                setPressed(true);
            }

            @Override
            public void TouchReleased(TouchComponentEvent event) {
                if (!isActive()) {
                    return;
                }
                setPressed(false);
            }
        });
    }
}
