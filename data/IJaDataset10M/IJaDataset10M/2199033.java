package org.logitest;

import java.net.*;
import java.util.*;

public class History {

    /** Return true if the history is empty.
	
		@return True if the history is empty
	*/
    public boolean isEmpty() {
        return getItems().size() == 0;
    }

    /** Return true if navigation forward through the history is
		possible (i.e. there is 1 or more recent items.)
		
		@return True If forward navigation is possible
	*/
    public boolean canGoForward() {
        return location < getItems().size() - 1;
    }

    /** Get the URL to go forward.
	
		@return The URL
	*/
    public URL forward() {
        location++;
        return (URL) getItems().get(location);
    }

    /** Return true if navigation backwards through the history is
		possible (i.e. there is 1 or more entries prior to the current
		entry.)
		
		@return True If backwards navigation is possible
	*/
    public boolean canGoBack() {
        return location > 0;
    }

    /** Ge the URL to go back.
	
		@return The URL
	*/
    public URL back() {
        location--;
        return (URL) getItems().get(location);
    }

    /** Get the first URL.
	
		@return The URL
	*/
    public URL first() {
        if (items.size() > 0) {
            return (URL) getItems().get(0);
        } else {
            return null;
        }
    }

    /** Get the last URL.
	
		@return The last URL
	*/
    public URL last() {
        List items = getItems();
        if (items.size() > 0) {
            return (URL) items.get(items.size() - 1);
        } else {
            return null;
        }
    }

    /** Add a URL to the history.
	
		@param url The URL to add
	*/
    public void add(URL url) {
        List items = getItems();
        items.add(url);
        location = items.size() - 1;
    }

    /** Get the current location in the history.
	
		@return The current location in the history
	*/
    public int getLocation() {
        return location;
    }

    /** Get a List of all items in the History.  This method should never return
		null.
	
		@return The list of all items
	*/
    protected List getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    private List items;

    private int location = -1;
}
