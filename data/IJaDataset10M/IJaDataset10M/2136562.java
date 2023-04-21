package org.gwm.splice.client.icon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.gwm.splice.client.desktop.DesktopManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Manages desktop icons.
 * 
 * @author Andy Scholz (andy.scholz@gmail.com)
 *
 */
public class IconManager {

    public static final int ARRANGE_LEFT = 0;

    public static final int ARRANGE_TOP = 1;

    public static final int ARRANGE_RIGHT = 2;

    public static final int ARRANGE_BOTTOM = 3;

    public static final int SORT_ASC = 0;

    public static final int SORT_DESC = 1;

    private HashMap icons = new HashMap();

    private DesktopManager desktopManager;

    private int arrange = ARRANGE_LEFT;

    private int sort = SORT_ASC;

    private int maxIconHeight = 80;

    private int maxIconWidth = 64;

    public IconManager(DesktopManager desktopManager) {
        this.desktopManager = desktopManager;
    }

    public void addIcon(DesktopIcon icon) {
        icons.put(icon.getName(), icon);
        arrangeIcons(this.arrange);
    }

    public void addIcon(DesktopIcon icon, int left, int top) {
        icons.put(icon.getName(), icon);
        RootPanel.get().add(icon);
        icon.setPosition(left, top);
    }

    public DesktopIcon getIcon(String name) {
        return (DesktopIcon) icons.get(name);
    }

    public void removeIcon(String name) {
        DesktopIcon icon = (DesktopIcon) icons.get(name);
        if (icon != null) {
            RootPanel.get().remove(icon);
            icons.remove(icon.getName());
        }
    }

    public void arrangeIcons(int arrange) {
        arrangeIcons(arrange, this.sort);
    }

    public void arrangeIcons(int arrange, int sort) {
        removeIcons();
        List list = getSortedIcons(sort);
        int minLeft = desktopManager.getLeftMargin() + 1;
        int minTop = desktopManager.getTopMargin() + 1;
        int maxRight = (Window.getClientWidth() - desktopManager.getRightMargin()) + 1;
        int maxBottom = (Window.getClientHeight() - desktopManager.getBottomMargin()) + 1;
        int x = minLeft;
        int y = minTop;
        if (arrange == ARRANGE_RIGHT) {
            x = maxRight - maxIconWidth;
        }
        if (arrange == ARRANGE_BOTTOM) {
            y = maxBottom - maxIconHeight;
        }
        RootPanel rp = RootPanel.get();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            DesktopIcon icon = (DesktopIcon) iter.next();
            rp.add(icon);
            icon.setPosition(x, y);
            switch(arrange) {
                case ARRANGE_LEFT:
                    y += icon.getOffsetHeight() + 4;
                    if ((y + maxIconHeight) >= maxBottom) {
                        y = minTop;
                        x += maxIconWidth;
                    }
                    break;
                case ARRANGE_TOP:
                    x += maxIconWidth;
                    if ((x + maxIconWidth) >= maxRight) {
                        x = minLeft;
                        y += maxIconHeight;
                    }
                    break;
                case ARRANGE_RIGHT:
                    y += icon.getOffsetHeight() + 4;
                    if ((y + maxIconHeight) >= maxBottom) {
                        y = minTop;
                        x -= maxIconWidth;
                    }
                    break;
                case ARRANGE_BOTTOM:
                    x += maxIconWidth;
                    if ((x + maxIconWidth) >= maxRight) {
                        x = minLeft;
                        y -= maxIconHeight;
                    }
                    break;
                default:
            }
        }
    }

    private List getSortedIcons(final int sort) {
        ArrayList list = new ArrayList(icons.values());
        Collections.sort(list, new Comparator() {

            public int compare(Object arg0, Object arg1) {
                DesktopIcon icon1 = (DesktopIcon) arg0;
                DesktopIcon icon2 = (DesktopIcon) arg1;
                String s1 = icon1.getName();
                String s2 = icon2.getName();
                if (sort == SORT_DESC) {
                    return s2.compareTo(s1);
                }
                return s1.compareTo(s2);
            }
        });
        return list;
    }

    private void removeIcons() {
        RootPanel rp = RootPanel.get();
        for (Iterator iter = icons.values().iterator(); iter.hasNext(); ) {
            DesktopIcon icon = (DesktopIcon) iter.next();
            rp.remove(icon);
        }
    }
}
