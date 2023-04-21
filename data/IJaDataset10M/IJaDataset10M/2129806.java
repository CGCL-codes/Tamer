package org.w3c.dom.html.events;

import org.w3c.dom.events.EventTarget;

/**
 * Only valid on form elements
 * 
 * @see http://www.w3schools.com/Html/html_eventattributes.asp
 *
 * @author Nathan Crause <ncrause at clarkesolomou.com>
 */
public interface FormElementEventTarget extends EventTarget {

    public static final String EVENT_ONCHANGE = "onchange";

    public static final String EVENT_ONSUBMIT = "onsubmit";

    public static final String EVENT_ONRESET = "onreset";

    public static final String EVENT_ONSELECT = "onselect";

    public static final String EVENT_ONBLUR = "onblur";

    public static final String EVENT_ONFOCUS = "onfocus";

    public static final String[] EVENTS = { EVENT_ONCHANGE, EVENT_ONSUBMIT, EVENT_ONRESET, EVENT_ONSELECT, EVENT_ONBLUR, EVENT_ONFOCUS };

    public String getOnchange();

    public void setOnchange(String script);

    public String getOnsubmit();

    public void setOnsubmit(String script);

    public String getOnreset();

    public void setOnreset(String script);

    public String getOnselect();

    public void setOnselect(String script);

    public String getOnblur();

    public void setOnblur(String script);

    public String getOnfocus();

    public void setOnfocus(String script);
}
