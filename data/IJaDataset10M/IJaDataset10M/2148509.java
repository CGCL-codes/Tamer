package com.liferay.portlet.messageboards.model.impl;

import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBTreeWalker;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="MBTreeWalkerImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBTreeWalkerImpl implements MBTreeWalker {

    public MBTreeWalkerImpl(MBMessage message) {
        _messageIdsMap = new HashMap<Long, Integer>();
        try {
            _messages = MBMessageLocalServiceUtil.getThreadMessages(message.getThreadId());
            for (int i = 0; i < _messages.size(); i++) {
                MBMessage curMessage = _messages.get(i);
                long parentMessageId = curMessage.getParentMessageId();
                if (!curMessage.isRoot() && !_messageIdsMap.containsKey(parentMessageId)) {
                    _messageIdsMap.put(parentMessageId, i);
                }
            }
        } catch (Exception e) {
            _log.error(e);
        }
    }

    public MBMessage getRoot() {
        return _messages.get(0);
    }

    public List<MBMessage> getChildren(MBMessage message) {
        List<MBMessage> children = new ArrayList<MBMessage>();
        int[] range = getChildrenRange(message);
        for (int i = range[0]; i < range[1]; i++) {
            children.add(_messages.get(i));
        }
        return children;
    }

    public int[] getChildrenRange(MBMessage message) {
        long messageId = message.getMessageId();
        Integer pos = _messageIdsMap.get(messageId);
        if (pos == null) {
            return new int[] { 0, 0 };
        }
        int[] range = new int[2];
        range[0] = pos.intValue();
        for (int i = range[0]; i < _messages.size(); i++) {
            MBMessage curMessage = _messages.get(i);
            if (curMessage.getParentMessageId() == messageId) {
                range[1] = i + 1;
            } else {
                break;
            }
        }
        return range;
    }

    public List<MBMessage> getMessages() {
        return _messages;
    }

    public boolean isOdd() {
        _odd = !_odd;
        return _odd;
    }

    public boolean isLeaf(MBMessage message) {
        Long messageIdObj = new Long(message.getMessageId());
        if (_messageIdsMap.containsKey(messageIdObj)) {
            return false;
        } else {
            return true;
        }
    }

    private static Log _log = LogFactory.getLog(MBTreeWalkerImpl.class);

    private List<MBMessage> _messages;

    private Map<Long, Integer> _messageIdsMap;

    private boolean _odd;
}
