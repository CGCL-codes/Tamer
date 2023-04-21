package com.nexirius.framework.htmlview.function;

import com.nexirius.framework.htmlview.HTMLParser;
import com.nexirius.framework.htmlview.HTMLSessionVariable;

/**
 * This class handles the $!button("event") function which is translated into
 * &lt;INPUT type='submit' name='button@event@state' value='$!translate("event")/&gt;
 * <br>
 * <INPUT type='submit' name='button@event@state' value='$!translate("event.buttonText")'/>
 */
public class ButtonHTMLFunction extends DefaultHTMLFunction {

    public String getFunctionName() {
        return "button";
    }

    public String getEvent() {
        return getArgument(0);
    }

    public void translate(HTMLSessionVariable sessionVariable, HTMLParser parser) throws Exception {
        String event = getEvent();
        if (event == null) {
            throw new Exception("button function needs at least one parameter (event)");
        }
        StringBuffer buf = new StringBuffer();
        buf.append("<INPUT type='submit' name='");
        buf.append(HTMLFunction.PARAMETER_BUTTON);
        buf.append(event);
        buf.append("@$(STATE)' value='$!translate(\"");
        buf.append(event);
        buf.append(".buttonText\")' />");
        parser.getOut().write(buf.toString().getBytes());
    }
}
