package org.zkoss.swifttab.event;

import java.util.Map;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.api.Tab;

/**
 * trigger when tab is moved by user's dragging.
 */
public class MoveTabEvent extends Event {

    public static final String NAME = "onTabMove";

    private Tab _movedTab = null;

    private int _startIndex = -1;

    private int _endIndex = -1;

    public int getStartIndex() {
        return _startIndex;
    }

    public int getEndIndex() {
        return _endIndex;
    }

    /**
	 * create a move tab event
	 *
	 * @param command
	 *            event name
	 * @param target
	 *            event to the tabs
	 * @param start
	 *            the index before remove
	 * @param end
	 *            the index after remove
	 */
    public MoveTabEvent(String command, Component target, int start, int end) {
        super(command, target);
        _startIndex = start;
        _endIndex = end;
        _movedTab = (Tab) target.getChildren().get(start);
    }

    /**
	 * the moved tab
	 *
	 * @return
	 */
    public Tab getMovedTab() {
        return _movedTab;
    }

    /**
	 * get the start/end paramter in the request and build a MoveTabEvent
	 * @param request
	 * @return
	 */
    public static final MoveTabEvent getMoveTabEvent(AuRequest request) {
        final Component tabs = request.getComponent();
        final Map data = request.getData();
        int startIndex = AuRequests.getInt(data, "start", -1);
        int endIndex = AuRequests.getInt(data, "end", -1);
        if (startIndex == -1 || endIndex == -1) {
            throw new IllegalArgumentException("startIndex/endIndex wrong.");
        }
        return new MoveTabEvent(request.getCommand(), tabs, startIndex, endIndex);
    }
}
