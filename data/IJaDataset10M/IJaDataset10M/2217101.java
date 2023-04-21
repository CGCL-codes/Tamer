package org.zkoss.zkand.ui;

import org.zkoss.zkand.ResponseTag;

/**
 * The &lt;r> ResponseTag.
 * @author robbiecheng
 *
 */
public class RTag extends ResponseTag {

    private String _cmd;

    private String[] _data = new String[5];

    private int _dataLength;

    public void addKid(ResponseTag tag) {
        if (tag instanceof DTag) {
            _data[_dataLength++] = tag.getValue();
        } else if (tag instanceof CTag) {
            _cmd = tag.getValue();
        }
    }

    public String getCommand() {
        return _cmd;
    }

    public String[] getData() {
        return _data;
    }
}
