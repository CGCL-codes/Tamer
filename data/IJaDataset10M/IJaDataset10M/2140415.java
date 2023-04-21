package net.etherstorm.jopenrpg.swing.nodehandlers;

import org.jdom.Element;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 1.10 $
 * $Date: 2004/04/01 03:19:38 $
 */
public class map_miniature_handler extends BaseNodehandler {

    public static final String NODE_MINIATURE = "miniature";

    /**
	 *
	 */
    public map_miniature_handler(org.jdom.Element e) {
        super(e);
    }

    public void fromXML(Element e) {
        super.fromXML(e);
        Element elem = handleVersioning(e).getChild(NODE_MINIATURE);
        setAction(elem.getAttributeValue(ATTRIBUTE_ACTION, "new"));
        setAlign(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_ALIGN, "0")));
        setFacing(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_FACING, "0")));
        setHeading(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_HEADING, "0")));
        setHide(stringToBool(elem.getAttributeValue(ATTRIBUTE_HIDE, "0")));
        setId(elem.getAttributeValue(ATTRIBUTE_ID, ""));
        setLabel(elem.getAttributeValue(ATTRIBUTE_LABEL, ""));
        setLocked(stringToBool(elem.getAttributeValue(ATTRIBUTE_LOCKED, "0")));
        setUrl(elem.getAttributeValue(ATTRIBUTE_URL, ""));
        setPosX(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_POSX, "0")));
        setPosY(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_POSY, "0")));
        setZorder(Integer.parseInt(elem.getAttributeValue(ATTRIBUTE_ZORDER, "0")));
    }

    public Element toXML() {
        Element e = super.toXML();
        Element elem = new Element(NODE_MINIATURE);
        e.addContent(elem);
        elem.setAttribute(ATTRIBUTE_ACTION, getAction());
        elem.setAttribute(ATTRIBUTE_ALIGN, String.valueOf(getAlign()));
        elem.setAttribute(ATTRIBUTE_FACING, String.valueOf(getFacing()));
        elem.setAttribute(ATTRIBUTE_HEADING, String.valueOf(getHeading()));
        elem.setAttribute(ATTRIBUTE_HIDE, boolToString(getHide()));
        elem.setAttribute(ATTRIBUTE_ID, getId());
        elem.setAttribute(ATTRIBUTE_LABEL, getLabel());
        elem.setAttribute(ATTRIBUTE_LOCKED, boolToString(getLocked()));
        elem.setAttribute(ATTRIBUTE_URL, getUrl());
        elem.setAttribute(ATTRIBUTE_POSX, String.valueOf(getPosX()));
        elem.setAttribute(ATTRIBUTE_POSY, String.valueOf(getPosY()));
        elem.setAttribute(ATTRIBUTE_ZORDER, String.valueOf(getZorder()));
        return e;
    }

    public static final String ATTRIBUTE_ACTION = "action";

    public static final String ATTRIBUTE_ALIGN = "align";

    public static final String ATTRIBUTE_FACING = "face";

    public static final String ATTRIBUTE_HEADING = "heading";

    public static final String ATTRIBUTE_HIDE = "hide";

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_LABEL = "label";

    public static final String ATTRIBUTE_LOCKED = "locked";

    public static final String ATTRIBUTE_URL = "path";

    public static final String ATTRIBUTE_POSX = "posx";

    public static final String ATTRIBUTE_POSY = "posy";

    public static final String ATTRIBUTE_ZORDER = "zorder";

    protected String _action;

    public static final String PROPERTY_ACTION = "action";

    public void setAction(String val) {
        try {
            if (val.equals(_action)) return;
        } catch (Exception ex) {
            return;
        }
        String oldval = _action;
        _action = val;
        firePropertyChange(PROPERTY_ACTION, oldval, _action);
    }

    public String getAction() {
        if (_action == null) setAction(new String());
        return _action;
    }

    protected int _align;

    public static final String PROPERTY_ALIGN = "align";

    public void setAlign(int val) {
        if (val == _align) return;
        int oldval = _align;
        _align = val;
        firePropertyChange(PROPERTY_ALIGN, oldval, _align);
    }

    public int getAlign() {
        return _align;
    }

    protected int _facing;

    public static final String PROPERTY_FACING = "facing";

    public void setFacing(int val) {
        if (val == _facing) return;
        int oldval = _facing;
        _facing = val;
        firePropertyChange(PROPERTY_FACING, oldval, _facing);
    }

    public int getFacing() {
        return _facing;
    }

    protected int _heading;

    public static final String PROPERTY_HEADING = "heading";

    public void setHeading(int val) {
        if (val == _heading) return;
        int oldval = _heading;
        _heading = val;
        firePropertyChange(PROPERTY_HEADING, oldval, _heading);
    }

    public int getHeading() {
        return _heading;
    }

    protected boolean _hide;

    public static final String PROPERTY_HIDE = "hide";

    public void setHide(boolean val) {
        if (val == _hide) return;
        boolean oldval = _hide;
        _hide = val;
        firePropertyChange(PROPERTY_HIDE, oldval, _hide);
    }

    public boolean getHide() {
        return _hide;
    }

    public boolean isHide() {
        return _hide;
    }

    protected String _id;

    public static final String PROPERTY_ID = "id";

    public void setId(String val) {
        try {
            if (val.equals(_id)) return;
        } catch (Exception ex) {
            return;
        }
        String oldval = _id;
        _id = val;
        firePropertyChange(PROPERTY_ID, oldval, _id);
    }

    public String getId() {
        if (_id == null) setId(new String());
        return _id;
    }

    protected String _label;

    public static final String PROPERTY_LABEL = "label";

    public void setLabel(String val) {
        try {
            if (val.equals(_label)) return;
        } catch (Exception ex) {
            return;
        }
        String oldval = _label;
        _label = val;
        firePropertyChange(PROPERTY_LABEL, oldval, _label);
    }

    public String getLabel() {
        if (_label == null) setLabel(new String());
        return _label;
    }

    protected boolean _locked;

    public static final String PROPERTY_LOCKED = "locked";

    public void setLocked(boolean val) {
        if (val == _locked) return;
        boolean oldval = _locked;
        _locked = val;
        firePropertyChange(PROPERTY_LOCKED, oldval, _locked);
    }

    public boolean getLocked() {
        return _locked;
    }

    public boolean isLocked() {
        return _locked;
    }

    protected String _Url;

    public static final String PROPERTY_URL = "Url";

    public void setUrl(String val) {
        try {
            if (val.equals(_Url)) return;
        } catch (Exception ex) {
            return;
        }
        String oldval = _Url;
        _Url = val;
        firePropertyChange(PROPERTY_URL, oldval, _Url);
    }

    public String getUrl() {
        if (_Url == null) setUrl(new String());
        return _Url;
    }

    protected int _posX;

    public static final String PROPERTY_POSX = "posX";

    public void setPosX(int val) {
        if (val == _posX) return;
        int oldval = _posX;
        _posX = val;
        firePropertyChange(PROPERTY_POSX, oldval, _posX);
    }

    public int getPosX() {
        return _posX;
    }

    protected int _posY;

    public static final String PROPERTY_POSY = "posY";

    public void setPosY(int val) {
        if (val == _posY) return;
        int oldval = _posY;
        _posY = val;
        firePropertyChange(PROPERTY_POSY, oldval, _posY);
    }

    public int getPosY() {
        return _posY;
    }

    protected int _zorder;

    public static final String PROPERTY_ZORDER = "zorder";

    public void setZorder(int val) {
        if (val == _zorder) return;
        int oldval = _zorder;
        _zorder = val;
        firePropertyChange(PROPERTY_ZORDER, oldval, _zorder);
    }

    public int getZorder() {
        return _zorder;
    }
}
