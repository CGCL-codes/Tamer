package net.detailedbalance.tivohme.bananas;

import com.tivo.hme.bananas.BHighlight;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.sdk.View;
import net.detailedbalance.tivohme.HmeTools;
import org.apache.log4j.Logger;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.Point;

/**
 * A generalized version of {@link com.tivo.hme.bananas.BList}, capable of handling a list of views that are not
 * all the same height.
 * @author Rhett Sutphin
 */
public class BVerticalScroller extends BView {

    private static final Logger log = Logger.getLogger(BVerticalScroller.class);

    private SortedMap viewsByY;

    private int topY;

    public BVerticalScroller(BView parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        viewsByY = new TreeMap();
        BHighlights h = getHighlights();
        h.setPageHint(H_PAGEUP, A_RIGHT, A_TOP);
        h.setPageHint(H_PAGEDOWN, A_RIGHT, A_BOTTOM);
    }

    public void add(BView view) {
        if (view.parent != this) {
            throw new IllegalArgumentException("Every BView added to a vertical scroller must have the scroller as its parent");
        }
        int nextY = calculateHeightOfAllViews();
        view.setBounds(0, nextY, width, view.height);
        viewsByY.put(new Integer(nextY), view);
    }

    private BView findFirstEntirelyVisibleView() {
        Map afterTop = viewsByY.tailMap(new Integer(topY));
        if (afterTop.size() > 0) {
            return (BView) afterTop.values().iterator().next();
        } else {
            return findLastView();
        }
    }

    private BView findLastPartiallyVisibleView() {
        SortedMap visible = viewsByY.subMap(new Integer(topY), new Integer(topY + height));
        BView first = (BView) visible.get(visible.firstKey());
        BView last = (BView) visible.get(visible.lastKey());
        if (last != first) {
            return last;
        } else {
            SortedMap afterLast = visible.tailMap(new Integer(last.y + 1));
            if (afterLast.size() > 0) {
                return (BView) afterLast.get(afterLast.firstKey());
            } else {
                return last;
            }
        }
    }

    private BView findLastView() {
        if (viewsByY.size() == 0) return null; else return (BView) viewsByY.get(viewsByY.lastKey());
    }

    private int calculateHeightOfAllViews() {
        BView lastView = findLastView();
        if (lastView == null) {
            return 0;
        } else {
            return lastView.y + lastView.height;
        }
    }

    public void scrollTo(int y) {
        int newY = Math.max(0, y);
        newY = Math.min(newY, calculateHeightOfAllViews() - height);
        topY = newY;
        refresh();
    }

    public boolean handleKeyPress(int code, long rawcode) {
        switch(code) {
            case KEY_CHANNELUP:
                scrollTo(topY - height + 10);
                break;
            case KEY_CHANNELDOWN:
                BView lastVis = findLastPartiallyVisibleView();
                scrollTo(lastVis.y);
                break;
            default:
                return super.handleKeyPress(code, rawcode);
        }
        if (KEY_CHANNELUP == code || !isEntirelyVisible(getScreen().focus)) {
            BView firstFull = findFirstEntirelyVisibleView();
            if (firstFull != null) {
                getScreen().setFocus(firstFull);
            }
        }
        getBApp().playSoundForKey(code, true, true);
        return true;
    }

    private boolean isEntirelyVisible(BView view) {
        Point location = HmeTools.getLocationInAncestorCoordinates(view, this);
        int top = location.y;
        int bottom = location.y + view.height;
        return top >= topY && bottom <= topY + height;
    }

    public boolean handleKeyRepeat(int code, long rawcode) {
        switch(code) {
            case KEY_CHANNELUP:
            case KEY_CHANNELDOWN:
                return handleKeyPress(code, rawcode);
        }
        return super.handleKeyRepeat(code, rawcode);
    }

    public boolean handleKeyRelease(int code, long rawcode) {
        switch(code) {
            case KEY_CHANNELUP:
            case KEY_CHANNELDOWN:
                return true;
        }
        return super.handleKeyRelease(code, rawcode);
    }

    public boolean handleFocus(boolean isGained, BView gained, BView lost) {
        if (isGained) {
            Point gainedLocation = HmeTools.getLocationInAncestorCoordinates(gained, this);
            int gainedTop = gainedLocation.y;
            int gainedBottom = gainedTop + gained.height;
            if (gainedTop < topY) {
                log.debug("Newly focused element is off the top: " + gainedTop + " < " + topY);
                scrollTo(gainedTop);
            } else if (gainedBottom > topY + height) {
                log.debug("Newly focused element is off the bottom: " + (gainedBottom) + " > " + (topY + height));
                scrollTo(gainedBottom - height);
            } else {
                log.debug("Newly focused element is within the viewport: " + topY + " <= " + gainedTop + " <= " + gainedBottom + " <= " + (topY + height));
            }
        }
        return super.handleFocus(isGained, gained, lost);
    }

    private void refresh() {
        getScreen().setPainting(false);
        try {
            Resource anim = getResource("*100");
            setTranslation(0, -1 * topY, anim);
            View focused = HmeTools.findFocusedWithin(this);
            if (focused != null && focused instanceof BView) {
                ((BView) focused).getHighlights().refresh();
            }
            BHighlights h = getHighlights();
            BHighlight pageup = h.get(H_PAGEUP);
            BHighlight pagedown = h.get(H_PAGEDOWN);
            if (pageup != null && pagedown != null) {
                pageup.setVisible((topY > 0) ? H_VIS_TRUE : H_VIS_FALSE);
                int heightOfAllViews = calculateHeightOfAllViews();
                pagedown.setVisible((topY + height < heightOfAllViews) ? H_VIS_TRUE : H_VIS_FALSE);
                h.refresh(anim);
            }
        } finally {
            getScreen().setPainting(true);
        }
    }
}
