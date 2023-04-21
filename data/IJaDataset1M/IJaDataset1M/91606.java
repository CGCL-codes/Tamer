package org.zkoss.gwt.client.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.gwt.client.Utils;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class is used to wrap {@link com.google.gwt.user.client.ui.Widget}. 
 * It's concealed the ZK Client-side Javascript with Google JSNI technology.
 * Please do not inherit this class directly, instead you should consider: 
 * {@link org.zkoss.gwt.client.zul.Widget}
 * 
 * @author Ian Tsai(Zanyking)
 * 
 */
public abstract class Widget extends com.google.gwt.user.client.ui.Widget {

    private JavaScriptObject widget;

    private Container parent;

    private boolean isElementSet = false;

    private Map<String, List<EventListener>> listeners;

    /**
	 * while constructing, zk widget will be created and $init() method will 
	 * be called.
	 */
    public Widget() {
        widget = create();
        Utils.setAttribute(widget, "__gwtIns", this);
    }

    /**
	 * 
	 * @param parent the parent of this widget, must be {@link Container}
	 */
    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Container getParent() {
        return parent;
    }

    public void removeFromParent() {
        super.removeFromParent();
        if (parent != null) {
            parent.remove(this);
            parent = null;
        }
    }

    /**
	 * wrapper method of ZK client widget.getId() method.
	 * @return id of this widget.
	 */
    public native String getId();

    /**
	 * wrapper method of ZK client widget.getuuid() method.
	 * @return uuid of this widget
	 */
    public native String getUuid();

    /**
	 * add a event listener of this widget.
	 * @param eventName the name of this widget, please take a look at{@link Events}
	 *  for possible event name.
	 * @param eventListener the real event listener instance
	 * @return false if EventListener is null, or already been registered.  
	 */
    public boolean addEventListener(String eventName, EventListener eventListener) {
        if (eventListener == null) {
            return false;
        }
        if (listeners == null) {
            listeners = new HashMap<String, List<EventListener>>();
        }
        List<EventListener> evtList = listeners.get(eventName);
        if (evtList == null) {
            listeners.put(eventName, evtList = new ArrayList<EventListener>());
            nativeListen(eventName);
        }
        if (evtList.contains(eventListener)) {
            return false;
        }
        evtList.add(eventListener);
        return true;
    }

    private native void nativeListen(String evtName);

    /**
	 * 
	 * @param eventName
	 * @param nEvent
	 */
    public void fireEvent(String eventName, JavaScriptObject nEvent) {
        List<EventListener> list = listeners.get(eventName);
        Event event = new Event(eventName, nEvent, this);
        for (EventListener listener : list) {
            listener.onEvent(event);
        }
    }

    /**
	 * to unregister an Event Listener.
	 * 
	 * @param eventName the name of the event listener, better get it through Events. 
	 * @param eventListener
	 * @return
	 */
    public boolean removeEventListener(String eventName, EventListener eventListener) {
        if (listeners == null) {
            return false;
        }
        List<EventListener> evtList = listeners.get(eventName);
        if (evtList == null || !evtList.contains(eventListener)) {
            return false;
        }
        evtList.remove(eventListener);
        if (evtList.isEmpty()) {
            nativeUnlisten(eventName);
        }
        return true;
    }

    private native void nativeUnlisten(String evtName);

    public boolean isListen(String eventName) {
        return false;
    }

    /**
	 * this method should be implemented by Widget implementer, which will return a 
	 * ZK Widget instance.<br>
	 * 
	 * normally, this method should be implemented using JSNI technology, for example:
	 * 
	 * <i>return new $wnd.zul.wgt.Button();</i> 
	 *  
	 * @return the ZK Javascript Widget Instance 
	 */
    protected abstract JavaScriptObject create();

    /**
     * GWT required method, need to override.
     */
    protected void setElement(Element elem) {
        super.setElement(elem);
        isElementSet = true;
    }

    /**
     * GWT required method, need to override.
     */
    public Element getElement() {
        if (!isElementSet) {
            JavaScriptObject jsObj = getZkWidget();
            if (!isRendered()) {
                if (getParent() != null && getParent().getElement() != null) {
                    render(getParent().getElement());
                } else {
                    render(RootPanel.getBodyElement());
                }
            }
            setElement(getElement(jsObj));
        }
        return super.getElement();
    }

    private native void render(Element parent);

    /**
     * @return true if component has been rendered
     */
    public native boolean isRendered();

    private native Element getElement(JavaScriptObject jsObj);

    /**
	 * the getter method of this widget's ZK side Javascript Object.
	 * @return native ZK Javascript widget object. 
	 */
    public JavaScriptObject getZkWidget() {
        return widget;
    }

    /**
	 * 
	 * @return
	 */
    protected native Widget previousSibling();

    /**
	 * 
	 * @return
	 */
    protected native Widget nextSibling();

    /**
     * 
     * @param timeout
     * @return
     */
    public native boolean focus(int timeout);

    /**
     * 
     * @return
     */
    public native String getDraggable();

    /**
     * 
     * @param draggable
     * @return
     */
    public native Widget setDraggable(String draggable);

    /**
     * 
     * @return
     */
    public native String getDroppable();

    /**
     * 
     * @param droppable
     * @return
     */
    public native Widget setDroppable(String droppable);

    /**
     * 
     * @return
     */
    public native String getMold();

    /**
     * 
     * @param mold
     * @return
     */
    public native Widget setMold(String mold);

    /**
     * 
     * @return
     */
    public native String getSclass();

    /**
     * 
     * @param sclass
     * @return
     */
    public native Widget setSclass(String sclass);

    /**
     * 
     * @return
     */
    public native String getZclass();

    /**
     * 
     * @param zclass
     * @return
     */
    public native Widget setSzlass(String zclass);

    /**
     * 
     * @return
     */
    public native String getStyle();

    /**
     * 
     * @param style
     * @return
     */
    public native Widget setStyle(String style);

    /**
     * 
     * @return
     */
    public native String getVflex();

    /**
     * 
     * @param value
     * @return
     */
    public native Widget setVflex(String value);

    /**
     * 
     * @return
     */
    public native String getHflex();

    /**
     * 
     * @param value
     * @return
     */
    public native Widget setHflex(String value);

    /**
     * 
     * @return
     */
    public native String getWidth();

    /**
     * peter, //TODO: 
     * @return
     */
    public native void setWidth(String width);

    /**
     * peter, //TODO: 
     * @return
     */
    public native void setHeight(String height);

    /**
     * 
     * @return
     */
    public native int getZIndex();

    /**
     * 
     * @return
     */
    public native Widget hide();

    /**
     * 
     * @return
     */
    public native Widget show();

    /**
     * 
     * @return
     */
    public native String get();
}
