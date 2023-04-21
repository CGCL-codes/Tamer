package org.esk.dablog.gwt.dablog.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.DOM;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.esk.dablog.gwt.dablog.client.model.ClientForumTopicEntry;
import org.esk.dablog.gwt.dablog.client.model.ClientForumPostEntry;
import org.esk.dablog.gwt.dablog.client.model.ClientForumComment;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TreeTest implements EntryPoint {

    public void onModuleLoad() {
        Tree t = new Tree() {

            public void setFocus(boolean focus) {
                Window.alert("setfocus does nothing");
            }
        };
        for (int i = 0; i < 50; ++i) {
            t.addItem("item" + i);
        }
        RootPanel.get("slot1").add(t);
    }
}
