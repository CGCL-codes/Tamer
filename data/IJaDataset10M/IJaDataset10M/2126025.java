package iwork.smartpresenter;

import org.w3c.dom.*;
import swig.util.*;
import iwork.eheap2.*;

/**
 *
 * $Id: ScriptButler.java,v 1.1 2002/04/09 07:45:59 emrek Exp $
 *
 * $Log: ScriptButler.java,v $
 * Revision 1.1  2002/04/09 07:45:59  emrek
 * release 1.0, initial release to sourceforge
 *
 * Revision 1.2  2001/12/09 01:05:50  penka
 * *** empty log message ***
 *
 * Revision 1.1  2001/12/08 10:24:57  penka
 * *** empty log message ***
 *
 * Revision 1.2  2001/05/31 23:29:28  penka
 *
 * These are a bunch of changes, of which i remember:
 * DisplayService: eheap moved to a class var; recognize & handle <cmd> sent from ButlerScript
 * ScriptButler: sends an event to the display;toXML, setDisplay fixed
 * ScriptEngine: don't go to time 0 on load; getLastTime(), some null check
 * SmartPresenterConstants: EHEAP_FIELD_COMMAND
 * test: newer tests, incl with subobject
 * UniversalDisplayService: lots
 *
 * Revision 1.1  2001/03/07 03:03:18  emrek
 * initial checkin
 *
 *
 */
public class ScriptButler extends ScriptItem {

    static final String XML_DISPLAY = "display";

    static final String XML_CMD = "cmd";

    String display;

    String cmd;

    public ScriptButler() {
    }

    public ScriptButler(Element xml) throws XMLException {
        setDisplay(XMLHelper.GetChildText(xml, XML_DISPLAY));
        setCmd(XMLHelper.GetChildText(xml, XML_CMD));
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String displayname) {
        this.display = displayname;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void doItem() throws ScriptException {
        try {
            System.err.println("putting command event to'" + display + "'");
            Event ev = new Event();
            ev.setPostValue(Event.EVENTTYPE, SmartPresenterConstants.EHEAP_CMD_EVENTNAME);
            if ((display != null) && (cmd != null)) {
                ev.addField(SmartPresenterConstants.EHEAP_FIELD_DISPLAYNAME, display);
                ev.addField(SmartPresenterConstants.EHEAP_FIELD_COMMAND, cmd);
                eheap.putEvent(ev);
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public String toXML() {
        String ret = "<" + XML_SCRIPTITEM + " " + SmartPresenterConstants.ATTR_SCRIPTCLASS + "=\"iwork.smartpresenter.ScriptButler\">\n";
        ret += "<" + XML_DISPLAY + ">" + display + "</" + XML_DISPLAY + ">\n";
        ret += "<" + XML_CMD + ">" + cmd + "</" + XML_CMD + ">\n";
        ret += "</" + XML_SCRIPTITEM + ">\n";
        return ret;
    }
}
