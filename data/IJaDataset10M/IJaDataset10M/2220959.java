package cc.sprite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * An element is an object that can be displayed on a form in a web frame.  
 * 
 * Elements typically implement a number of properties and a draw() method
 * to render themselves onto the generated html page.  
 * 
 * Elements may also implement a dispatch() method to respond to javascript
 * events originating from the elements rendered html.
 * 
 * @author Joe Mayer
 */
public abstract class WElement implements Serializable {

    /** A unique identifier for this element on its frame. */
    private String elementId;

    /** The name that the form element uses for this element. */
    private String alias;

    /** This elements css clas name. */
    private String style;

    /** This elements auto-size width.  In the form "x+y%". */
    private String width;

    /** This elements auto-size height. In the form "x+y%". */
    private String height;

    /** The form object for events dispatched by this element. */
    private WForm form;

    /** The parent element of this element. */
    private WElement parent;

    /** The index of this element in its parents children array. */
    private int parentIndex;

    /** The number of children elements of this element. */
    private int childCount;

    /** Children elements of this element. */
    private WElement[] children;

    /**
   * Serialization method.
   * @param out The output sream to write to.
   * @throws IOException
   */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(elementId);
        out.writeObject(alias);
        out.writeObject(style);
        out.writeObject(width);
        out.writeObject(height);
        out.writeObject(form);
        out.writeObject(parent);
        out.writeInt(parentIndex);
        out.writeInt(childCount);
        for (int i = 0; i < childCount; i++) {
            out.writeObject(children[i]);
        }
    }

    /**
   * Deserialization method.
   * @param in  The input stream to read from.
   * @throws IOException
   * @throws ClassNotFoundException
   */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        elementId = (String) in.readObject();
        alias = (String) in.readObject();
        style = (String) in.readObject();
        width = (String) in.readObject();
        height = (String) in.readObject();
        form = (WForm) in.readObject();
        parent = (WElement) in.readObject();
        parentIndex = in.readInt();
        childCount = in.readInt();
        children = new WElement[childCount];
        for (int i = 0; i < childCount; i++) {
            children[i] = (WElement) in.readObject();
        }
    }

    /**
   * This method recursivly walks the parent-child element tree starting
   * with this element.  First the parent and then each descendent is passed
   * to the walkers walk method in a depth first order.
   * 
   * @param walker The object that is called with each element in the tree.
   */
    public void walk(WElementWalker walker) {
        walker.walk(this);
        for (int i = 0; i < childCount; i++) {
            children[i].walk(walker);
        }
    }

    /**
   * Set the alias property for this element. 
   * @param alias The new alias property.
   */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
   * Get the alias property value.
   * @return The current alias property value.
   */
    public final String getAlias() {
        return alias;
    }

    /**
   * Get the width property value.
   * @return The width property. In the form "x+y%".
   */
    public final String getWidth() {
        return width;
    }

    /**
   * Set the width property value.
   * @param width  The new width property. In the form "x+y%".
   */
    public void setWidth(String width) {
        sendSetWidth(width);
        this.width = width;
    }

    /**
   * Get the height property value.
   * @return The height property. In the form "x+y%".
   */
    public final String getHeight() {
        return height;
    }

    /**
   * Set the height property value.
   * @param height  The new height property. In the form "x+y%".
   */
    public void setHeight(String height) {
        sendSetHeight(height);
        this.height = height;
    }

    /**
   * Lookup the locale specific text for the requested key.
   * Uses the current threads session to lookup a resource key.
   * @see cc.sprite.WSession#getResourceString(java.lang.String) 
   * @param key The resource key to lookup.
   * @return The resource keys value.
   */
    protected String getResourceString(String key) {
        return WEvent.current().session().getResourceString(key);
    }

    /**
   * Get the style property value.  The style property if specified
   * is used as the css class name of the html element.  The application
   * specific css file can then define the style for the given
   * class name.
   * @return The style property value.
   */
    public final String getStyle() {
        return style;
    }

    /**
   * Set the style property value.
   * @param style The new style property value.
   */
    public void setStyle(String style) {
        sendSetStyle(style);
        this.style = style;
    }

    /**
   * Get the previous sibling of this element.
   * @return This elements previous sibling, or null.
   */
    public final WElement prevSibling() {
        return (parent == null || parentIndex < 1) ? null : parent.children[parentIndex - 1];
    }

    /**
   * Get the next sibling of this element.
   * @return This elements next sibling, or null. 
   */
    public final WElement nextSibling() {
        return (parent == null || parentIndex + 1 >= parent.childCount) ? null : parent.children[parentIndex + 1];
    }

    /**
   * Get the first child of this element.
   * @return This elements first child, or null.
   */
    public final WElement firstChild() {
        return childCount < 1 ? null : children[0];
    }

    /**
   * Get the last child of this element.
   * @return This elements last child, or null.
   */
    public final WElement lastChild() {
        return childCount < 1 ? null : children[childCount - 1];
    }

    /**
   * Get the current event for this thread.
   * @see cc.sprite.WEvent#current() 
   * @return The current event for this thread. */
    protected final WEvent event() {
        return WEvent.current();
    }

    /**
   * Get the session for the current thread.  
   * The same as WEvent.current().session().
   * @return The current session for this thread. */
    protected final WSession session() {
        return WEvent.current().session();
    }

    /**
   * Get this elements parent.
   * @return This elements parent element, or null.
   */
    public final WElement parent() {
        return parent;
    }

    /**
   * Get the index of this element in its parents children. 
   * @return The index of this element in its parents children array. */
    public final int parentIndex() {
        return parentIndex;
    }

    /**
   * Get the number of children this element has.
   * @return The number of children this element has.
   */
    public final int childCount() {
        return childCount;
    }

    /**
   * Get the i'th child of this element.  If the index is out of bounds
   * it may throw an out of bounds exception or it may just return null.
   * @param i The index of the child to get, starting from 0.
   * @return The i'th child of this element.
   */
    public final WElement child(int i) {
        return children[i];
    }

    /** 
   * Get the 5-character unique element identifier for this element.
   * This id is generated by the sequence from WUtil.
   * @see cc.sprite.WUtil#nextSeq()
   * @see cc.sprite.WUtil#encodeId(int)
   * @return This elements id. */
    public final String elementId() {
        return elementId;
    }

    /** 
   * Draw this element.  The default implementation does nothing.
   * Concrete implementations of user interface elements will override
   * this method to render html to the page.
   * @param page The page to draw to.
   * @throws Exception
   */
    public void draw(WPage page) throws Exception {
    }

    /** 
   * Draw this elements children.  Calls the draw method on each 
   * of this elements children, in index order. 
   * @param page The page to draw to.
   * @throws Exception */
    protected void drawChildren(WPage page) throws Exception {
        for (int i = 0; i < childCount; i++) {
            children[i].draw(page);
        }
    }

    /** 
   * Handle an event. The default implementation fires the event on
   * this elements target form using this elements alias.  
   * Concrete element implementations can override this method to
   * provide their own event handling logic.
   * @param event The event to dispatch.
   * @throws Exception */
    public void dispatch(WEvent event) throws Exception {
        fire(event);
    }

    /**
   * Add a child to this element to the children array.  Resize the children
   * array if necessary.  Used only internally.
   * @param index The index to insert the new element at.
   * @param child  The new element to add as a child.
   * @return The actual index where the child was inserted.
   */
    private int insertChild0(int index, WElement child) {
        if (children == null) {
            children = new WElement[4];
        }
        if (childCount == children.length) {
            children = (WElement[]) WUtil.resizeArray(children, 2 * childCount);
        }
        if (index < 0 || index >= childCount) {
            index = childCount;
        } else {
            System.arraycopy(children, index, children, index + 1, childCount - index);
        }
        children[index] = child;
        childCount++;
        return index;
    }

    /**
   * Removes the child at the given index.  Used only internally. 
   * @param index
   * @return The removed child.
   */
    private WElement removeChild0(int index) {
        WElement child = children[index];
        childCount--;
        System.arraycopy(children, index + 1, children, index, childCount - index);
        children[childCount] = null;
        return child;
    }

    /**
   * Set the parent element for this element.  Used only internally.
   * 
   * If parent is null then index is ignored and this element is
   * removed from its current parent.
   * 
   * If this element already has a parent, it is first removed from
   * the current parent before it is attached to the new parent.
   * 
   * @param parent The new parent element.
   * @param index The index to insert at in the new parents children array,
   *              or -1 to append.
   */
    private void setParent0(WElement parent, int index) {
        this.parent = parent;
        this.parentIndex = index;
    }

    /** Enum constants for validateChange method. 
   * The BEFORE_* operations are for validation and the parent 
   * or the child can disallow the operation by returning false
   * when from the checkOperation method. */
    public enum Operation {

        /** Used to validate the removal of an existing child from the parent. */
        BEFORE_REMOVE_CHILD, /** Used to validate the insertion of a new child under the parent. */
        BEFORE_INSERT_CHILD, /** Used to notify after the child has been removed from the parent. */
        AFTER_REMOVE_CHILD, /** Used to notify after the child has been inserted under the parent. */
        AFTER_INSERT_CHILD, /** Used to notify a child before it is inserted into a parent. */
        BEFORE_INSERT, /** Used to notify a child before removal from a parent. */
        BEFORE_REMOVE, /** Used to notify a child after insertion into a parent. */
        AFTER_INSERT, /** Used to notify a child after removal from a parent. */
        AFTER_REMOVE
    }

    /**
   * Called to validate an add/remove child operation 
   * before it is executed, and to notify a parent and 
   * child after the operation is completed.
   * The default implementation disallows insertion of
   * any children. To allow an element to have children,
   * override this method with an implementation that 
   * returns true.  The return value is checked only 
   * when the BEFORE_* operations are used.  
   * 
   * @param oper    The operation about to be performed.
   * @param index   The index being inserted/removed.
   * @param element The element being inserted/removed.
   * 
   * @return true if the operation is allowed.
   */
    protected boolean checkOperation(Operation oper, int index, WElement element) {
        switch(oper) {
            case BEFORE_INSERT_CHILD:
                return false;
        }
        return true;
    }

    /**
   * Set parent element and parent index of this child element.
   * 
   * If parent is null then index is ignored and this element is
   * removed from its current parent.
   * 
   * If this element already has a parent, it is first removed from
   * the current parent before it is attached to the new parent.
   * 
   * @param parent The new parent element.
   * @param index The index to insert at in the new parents children array,
   *              or -1 to append.
   */
    public void setParent(WElement parent, int index) {
        if (this.parent == parent && this.parentIndex == index) {
            return;
        }
        if (this.parent != null) {
            boolean ok = this.checkOperation(Operation.BEFORE_REMOVE, parentIndex, parent);
            if (ok) {
                ok = parent.checkOperation(Operation.BEFORE_REMOVE_CHILD, parentIndex, this);
            }
            if (ok == false) {
                throw WRuntimeException.checkRemoveChildFailed(parent, this);
            }
        }
        if (parent != null) {
            boolean ok = this.checkOperation(Operation.BEFORE_INSERT, index, parent);
            if (ok) {
                ok = parent.checkOperation(Operation.BEFORE_INSERT_CHILD, index, this);
            }
            if (ok == false) {
                throw WRuntimeException.checkInsertChildFailed(parent, this);
            }
        }
        if (this.parent != null) {
            this.parent.removeChild0(parentIndex);
            setParent0(null, 0);
            this.parent.checkOperation(Operation.AFTER_REMOVE_CHILD, parentIndex, this);
            this.checkOperation(Operation.AFTER_REMOVE, parentIndex, this);
        }
        setParent0(parent, index);
        if (parent != null) {
            parent.insertChild0(index, this);
            parent.checkOperation(Operation.AFTER_INSERT_CHILD, index, this);
            this.checkOperation(Operation.AFTER_INSERT, parentIndex, parent);
        }
    }

    /** Insert the child element into this elements children array at the
   * specified index.
   * @param index The insertion index, or -1 for append.
   * @param child The new child element to insert.
   */
    public void insertChild(int index, WElement child) {
        child.setParent(this, index);
    }

    /** Remove the child from the specified index in this elements children array. 
   * @param index The index of the child to remove.
   * @return The removed child element. 
   */
    public WElement removeChild(int index) {
        WElement child = children[index];
        child.setParent(null, 0);
        return child;
    }

    /**
   * Resize the internal child array. If size is 
   * less than the current number of children then the current number
   * of children is used for the new size instead.
   * This method is used by the template loader to set the size of the
   * child array to the correct size before adding children to the element.
   * @param size  The new size of the child array.
   */
    public void ensureCapacity(int size) {
        if (size < childCount) {
            size = childCount;
        }
        if (size < 1) {
            children = null;
        } else {
            WElement[] newChildren = new WElement[size];
            if (childCount > 0) {
                System.arraycopy(children, 0, newChildren, 0, childCount);
            }
        }
    }

    /**
   * Set the form that owns this element. It will recieve events
   * thrown by this element.
   * @param form The target form.
   */
    public void setForm(WForm form) {
        this.form = form;
    }

    /**
   * Get the form that this element is on.  Any events that this element
   * throws are sent to this form.
   * @return The form that owns this element.
   */
    public final WForm form() {
        return form;
    }

    /**
   * Create a new window and show it. 
   * Position and sizing are relative to the outside of the window and not
   * the inner frame where the form is displayed.
   * 
   * @param title  The title of the new window.
   * @param form   The form to display in the new window.
   * @param left   The left coordinate in pixels from the left side of the screen.
   * @param top    The top coordinate in pixels from the top of the screen.
   * @param width  The width of the window.
   * @param height The height of the window.
   * @return The new window.
   */
    protected WWindow openWindow(String title, WForm form, int left, int top, int width, int height) {
        WWindow w = new WWindow();
        w.setTitle(title);
        w.setBody(form);
        w.move(left, top);
        w.resize(width, height);
        w.open();
        return w;
    }

    /**
   * Close the window that this element is in.  This element 
   * must be on a form that is contained by a closable window.
   * @see cc.sprite.WForm#closeWindow()
   */
    protected void closeWindow() {
        if (form != null) {
            form.closeWindow();
        }
    }

    /** 
   * End the modal state of this elements window.  This element
   * must be on a frame that is contained by the current top-most
   * modal window.  
   * 
   * @param value  The value to passed in the 'done' event.
   */
    protected void endModal(Object value) {
        session().endModal(form.frame(), value);
    }

    /** 
   * Constructor for a new element. 
   * Assigns a unique element id which is used to identify this element 
   * in generated html so that it can be manipulated with javascript. */
    protected WElement() {
        elementId = WUtil.encodeId(WUtil.nextSeq());
    }

    /**
   * Fires an event to this elements target form.
   * @param event The event to fire.
   * @throws WException
   */
    protected void fire(WEvent event) throws WException {
        WHandler.fire(form, alias, event.name(), event);
    }

    /**
   * Get a buffer that is setup to accept script from this element.
   * @return A script buffer targeting this elements frame, or null.
   */
    protected StringBuilder openScriptBuffer() {
        if (form != null) {
            WFrame frame = form.frame();
            if (frame != null) {
                return frame.openScriptBuffer();
            }
        }
        return null;
    }

    /**
   * Send a setValue command to the borwser.
   * Emits response to set the value property of an element.
   * @param value The value to set.
   */
    protected void sendSetValue(String value) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setValue('");
            out.append(elementId);
            out.append("','");
            if (value != null) {
                out.append(WUtil.escapeSQuote(value));
            }
            out.append("');\n");
        }
    }

    /**
   * Send a setChecked command to the browser.
   * @param checked  true to check the box.
   */
    protected void sendSetChecked(boolean checked) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setChecked('");
            out.append(elementId);
            out.append("',");
            out.append(checked ? "true" : "false");
            out.append(");\n");
        }
    }

    /**
   * Send a setSelectedIndex command to the browser.
   * @param idx The new selected index.
   */
    protected void sendSetSelectedIndex(int idx) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setSelectedIndex('");
            out.append(elementId);
            out.append("',");
            out.append(idx);
            out.append(");\n");
        }
    }

    /**
   * Send a refreshFrame command to the browser.
   */
    protected void sendRefreshFrame() {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.refreshFrame('");
            out.append(elementId);
            out.append("');\n");
        }
    }

    /**
   * Send a setOptions command to the browser.
   * 
   * The options string is used to create new html option
   * elements that are set to this select element.
   * 
   * The options string is a concatenation all the option
   * descriptions with each one terminated by 
   * an ascii 29 (Group Separator) character, and then the
   * last one followed by the new selected index or '-1'.
   * 
   * @param options The options string to send.
   */
    protected void sendSetOptions(String options) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setOptions('");
            out.append(elementId);
            out.append("','");
            out.append(WUtil.escapeSQuote(options));
            out.append("');\n");
        }
    }

    /**
   * Send a setDisplay command to the browser.  
   * Sets the display style of the element to either 'inline' or 'none'.
   * @param visible  true to display this element.
   */
    protected void sendSetDisplay(boolean visible) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setDisplay('");
            out.append(elementId);
            out.append("','");
            out.append(visible ? "inline" : "none");
            out.append("');\n");
        }
    }

    /**
   * Send a setDisabled command to the browser.
   * @param disabled true to disable this element.
   */
    protected void sendSetDisabled(boolean disabled) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setDisabled('");
            out.append(elementId);
            out.append("',");
            out.append(disabled ? "true" : "false");
            out.append(");\n");
        }
    }

    /**
   * Send a setTitle command to the browser.  The title
   * is the popup text that displays on mouse-over.
   * @param title The new element title property.
   */
    protected void sendSetTitle(String title) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setTitle('");
            out.append(elementId);
            out.append("',");
            if (title == null) {
                out.append("undefined");
            } else {
                out.append('\'');
                out.append(WUtil.escapeSQuote(title));
                out.append('\'');
            }
            out.append(");\n");
        }
    }

    /**
   * Send a setInnerHtml command to the browser.
   * This command sets the innerHTML property of the element.
   * @param innerHtml  The new inner html to set.
   */
    protected void sendSetInnerHtml(String innerHtml) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setInnerHtml('");
            out.append(elementId);
            out.append("','");
            if (innerHtml != null) {
                out.append(WUtil.escapeSQuote(innerHtml));
            }
            out.append("');\n");
        }
    }

    /**
   * Send a setTimer command to the browser.
   * 
   * Sets a timer to fire on the page after the specified timeout,
   * the element will then fire a 'tick' event.
   * 
   * @param millis  The milliseconds before the timer fires.
   * @param repeat  true for the timer to auto-repeat.
   */
    protected void sendSetTimer(int millis, boolean repeat) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setTimer('");
            out.append(elementId);
            out.append("',");
            out.append(String.valueOf(millis));
            out.append(",");
            out.append(String.valueOf(repeat));
            out.append(");\n");
        }
    }

    /**
   * Send a set width command to the browser.
   * @param width The new width in the form x+y%, or null.
   */
    protected void sendSetWidth(String width) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            String min;
            String pct;
            if (width == null || width.length() == 0) {
                min = "undefined";
                pct = "undefined";
            } else {
                int n = width.length();
                int i = width.indexOf('+');
                if (i >= 0) {
                    min = width.substring(0, i);
                    pct = width.substring(i + 1);
                } else if (width.charAt(n - 1) == '%') {
                    min = "1";
                    pct = width.substring(0, n - 1);
                } else {
                    min = width;
                    pct = "0";
                }
            }
            out.append("wa.setWidth('");
            out.append(elementId);
            out.append("',");
            out.append(min);
            out.append(",");
            out.append(pct);
            out.append(");\n");
        }
    }

    /**
   * Send a set height command to the browser.
   * @param height The new height in the form x+y%, or null.
   */
    protected void sendSetHeight(String height) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            String min;
            String pct;
            if (height == null || height.length() == 0) {
                min = "undefined";
                pct = "undefined";
            } else {
                int n = height.length();
                int i = height.indexOf('+');
                if (i >= 0) {
                    min = height.substring(0, i);
                    pct = height.substring(i + 1);
                } else if (height.charAt(n - 1) == '%') {
                    min = "1";
                    pct = height.substring(0, n - 1);
                } else {
                    min = height;
                    pct = "0";
                }
            }
            out.append("wa.setHeight('");
            out.append(elementId);
            out.append("',");
            out.append(min);
            out.append(',');
            out.append(pct);
            out.append(");\n");
        }
    }

    /**
   * Send a set style command to the browser.
   * @param style The new css class name for this element.
   */
    protected void sendSetStyle(String style) {
        StringBuilder out = openScriptBuffer();
        if (out != null) {
            out.append("wa.setStyle('");
            out.append(elementId);
            out.append("','");
            out.append(WUtil.escapeSQuote(style));
            out.append("');\n");
        }
    }
}
