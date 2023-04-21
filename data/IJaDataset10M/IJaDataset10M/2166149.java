package desktop;

import org.wings.SComponent;
import java.beans.PropertyChangeListener;

public interface DesktopItem {

    public static final String KEY = "Key";

    public static final String NAME = "Name";

    public static final String ICON = "Icon";

    public static String FIRST_FREE_INDEX = "firstFreeIndex";

    public static String TOOL = "tool";

    public static String DESKTOPPANE = "desktoppane";

    public static String POSITION_ON_PANE = "positionOnPane";

    public static final String TEXT = "Text";

    public static final String FEED = "Feed";

    public SComponent getComponent();

    public Object getValue(String key);

    public void putValue(String key, Object value);

    public void activated();

    public void destroyed();

    public void setContainer(ItemContainer container);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void fixContent();
}
