package portal.presentation.cafe.web;

import java.util.*;
import org.w3c.dom.Element;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.app.base.ProtectedPortalPage;
import hambo.community.CommunityApplication;
import hambo.community.chatengine.CoffeeMaker;
import hambo.community.chatengine.Dialog;
import hambo.community.chatengine.Snapshot;
import hambo.community.chatengine.Member;
import hambo.util.Device;

/**
 * This is the fun stuff: display the chatroom
 * (c) 2001 Zeed AB
 */
public class cfchatcontents extends ProtectedPortalPage {

    private static final int refreshWait = 15;

    private static long counter = 0;

    public cfchatcontents() {
        super("cfchatcontents");
    }

    public void processPage() {
        long state = 0;
        try {
            String stateStr = getParameter("state");
            if (stateStr != null) state = Long.parseLong(stateStr);
        } catch (Exception e) {
        }
        long wait = 0;
        try {
            String waitStr = getParameter("wait");
            if (waitStr != null) wait = Long.parseLong(waitStr);
        } catch (Exception e) {
        }
        String room = getParameter("room");
        String nickname;
        nickname = getContext().getSessionAttributeAsString("current_nick");
        if (nickname == null) nickname = " ";
        String user_uid = getContext().getSessionAttributeAsString("user_uid");
        CoffeeMaker.memberIsAlive(room, nickname, user_uid, getContext().getDevice().getGroup());
        Snapshot snap = CoffeeMaker.getChatContents(room, state, wait);
        Element theBody = getElement("thebody");
        Element roomname = getElement("roomname");
        Element topic = getElement("topic");
        DOMUtil.setFirstNodeText(roomname, CoffeeMaker.getRoomName(room));
        Element row = getElement("row");
        if ((snap == null || snap.contents == null) || (snap.contents.size() == 0)) {
        } else if (CommunityApplication.TOPDOWN) for (int i = 0; i < snap.contents.size(); i++) displayLine(row, snap.contents, i); else for (int i = snap.contents.size() - 1; i >= 0; i--) displayLine(row, snap.contents, i);
        row.getParentNode().removeChild(row);
        Element script = getElement("script");
        Link here = new Link("cfchatcontents", false);
        if (snap != null) here.addParam("state", "" + snap.state);
        here.addParam("wait", "" + refreshWait);
        here.addParam("room", room);
        String heres = Link.addSessionKey(here.toString(), getContext().getSessionKey());
        DOMUtil.setFirstNodeText(script, "window.location.href=\"" + heres + "\"");
    }

    private void displayLine(Element row, Vector v, int i) {
        Element nrow = (Element) row.cloneNode(true);
        Element name = getElement(nrow, "name");
        Element contents = getElement(nrow, "message");
        Dialog msg = (Dialog) v.elementAt(i);
        if (msg != null) {
            if ((msg.htmlName == null) || (msg.htmlName.length() == 0)) DOMUtil.setFirstNodeText(name, "-"); else {
                if (msg.name.equals("---")) {
                    DOMUtil.setFirstNodeText(name, "-");
                } else {
                    DOMUtil.setFirstNodeText(name, msg.name);
                }
            }
            DOMUtil.setFirstNodeText(contents, msg.text);
            row.getParentNode().insertBefore(nrow, row);
        }
    }
}
