package simplefix.quickfix;

import java.util.LinkedList;
import java.util.List;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import simplefix.MsgType;
import simplefix.Tag;

public class Message implements simplefix.Message {

    quickfix.Message _msg;

    MsgType _type;

    public Message(final MsgType type) {
        _type = type;
        _msg = new quickfix.Message();
        _msg.getHeader().setString(35, type.getTypeString());
    }

    public Message(final String msg) {
        try {
            _msg = new quickfix.Message(msg);
            try {
                _type = MsgType.fromString(_msg.getHeader().getString(35));
            } catch (FieldNotFound e) {
                e.printStackTrace();
            }
        } catch (InvalidMessage e) {
            e.printStackTrace();
        }
    }

    public Message(final quickfix.Message msg) {
        super();
        _msg = msg;
        try {
            _type = MsgType.fromString(msg.getHeader().getString(35));
        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
    }

    public MsgType getMsgType() {
        return _type;
    }

    public Object getValue(final Tag tag) {
        String str;
        try {
            str = _msg.getString(tag.getTagNum());
            if (str != null) {
                return str;
            }
        } catch (FieldNotFound e) {
        }
        try {
            str = _msg.getHeader().getString(tag.getTagNum());
            if (str != null) {
                return str;
            }
        } catch (FieldNotFound e) {
        }
        try {
            str = _msg.getTrailer().getString(tag.getTagNum());
            if (str != null) {
                return str;
            }
        } catch (FieldNotFound e) {
        }
        return null;
    }

    public void setValue(final Tag tag, final Object value) {
        if (quickfix.Message.isHeaderField(tag.getTagNum())) {
            _msg.getHeader().setString(tag.getTagNum(), value.toString());
            return;
        } else if (quickfix.Message.isTrailerField(tag.getTagNum())) {
            _msg.getTrailer().setString(tag.getTagNum(), value.toString());
            return;
        }
        _msg.setString(tag.getTagNum(), value.toString());
    }

    public List<simplefix.Group> getGroupValue(final Tag tag) {
        List<quickfix.Group> quickGroups = _msg.getGroups(tag.getTagNum());
        List<simplefix.Group> simpleGroups = new LinkedList<simplefix.Group>();
        for (quickfix.Group quickGroup : quickGroups) {
            simpleGroups.add(new Group(quickGroup));
        }
        return simpleGroups;
    }

    public void setGroupValue(final Tag tag, final List<simplefix.Group> value) {
        List<quickfix.Group> quickGroups = new LinkedList<quickfix.Group>();
        for (simplefix.Group simpleGroup : value) {
            if (simpleGroup instanceof Group) {
                quickGroups.add(((Group) simpleGroup)._group);
            }
        }
        _msg.setGroups(tag.getTagNum(), quickGroups);
    }
}
