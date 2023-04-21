package org.castafiore.searchengine.back;

import org.castafiore.searchengine.EventDispatcher;
import org.castafiore.ui.Container;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.js.JMap;

public class EXWindow extends EXContainer implements EventDispatcher {

    public EXWindow(String name, String title) {
        super(name, "div");
        addClass("ex-window");
        addChild(new EXContainer("header", "div").addClass("ex-header").addChild(new EXContainer("icon", "img").addClass("ex-icon").setAttribute("src", "icons-2/fugue/icons/sql-join-right.png")).addChild(new EXContainer("title", "h5").setText(title)).addChild(new EXContainer("close", "a").setAttribute("href", "#").addClass("minimize").addEvent(DISPATCHER, Event.CLICK)));
        addChild(new EXContainer("bodyContainer", "div").addClass("ex-body"));
        setDraggable(true);
    }

    public void setTitle(String title) {
        getDescendentByName("title").setText(title);
    }

    public String getTitle() {
        return getDescendentByName("title").getText();
    }

    public void setIcon(String icon) {
        getDescendentByName("icon").setAttribute("src", icon);
    }

    public String getIcon() {
        return getDescendentByName("icon").getAttribute("src");
    }

    public Container getBody() {
        return getChild("bodyContainer").getChildByIndex(0);
    }

    public EXWindow setBody(Container body) {
        getChild("bodyContainer").getChildren().clear();
        getChild("bodyContainer").setRendered(false);
        getChild("bodyContainer").addChild(body);
        return this;
    }

    @Override
    public void executeAction(Container source) {
        if (source.getName().equals("close")) getAncestorOfType(OSDeskTop.class).removeWindow(getName());
    }

    public Container setDraggable(boolean draggable) {
        if (draggable) {
            JMap options = new JMap().put("opacity", 0.35).put("handle", "#" + getDescendentByName("header").getId());
            options.put("containment", "document");
            setDraggable(true, options);
            setStyle("position", "absolute");
            setStyle("top", "10%");
            setStyle("left", "10%");
        } else {
            super.setDraggable(false);
            setStyle("position", "static");
        }
        return this;
    }
}
