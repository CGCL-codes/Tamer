package net.sf.mvn4click.yui.control.button;

import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.click.util.HtmlStringBuffer;

/**
 * The Class Button.
 * 
 * @author Andre Bremer
 * @since 1.0
 * @version $Id$
 */
public class Button extends AbstractButton {

    private static final String YUI_VERSION = "2.5.1";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4213952661693211789L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Button.class);

    /** The JavaScript import statements. */
    public static final String HTML_IMPORTS = "<link type=\"text/css\" rel=\"stylesheet\" href=\"http://yui.yahooapis.com/{0}/build/button/assets/skins/sam/button.css\"/>\n" + "<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/{0}/build/yahoo-dom-event/yahoo-dom-event.js\"></script>\n" + "<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/{0}/build/element/element-beta-min.js\"></script>\n" + "<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/{0}/build/button/button-min.js\"></script>\n" + "<script type=\"text/javascript\">var oButton = new YAHOO.widget.Button(\"{1}\", '{'label: \"{2}\", checked: {3}'}');</script>\n";

    /**
	 * Create a button with no name defined. <p/> <b>Please note</b> the
	 * control's name must be defined before it is valid.
	 */
    public Button() {
        super();
    }

    /**
	 * Create a button with the given name.
	 * 
	 * @param name
	 *            the button name
	 */
    public Button(String name) {
        super(name);
    }

    /**
	 * Create a button with the given name and label. The button label is
	 * rendered as the HTML "value" attribute.
	 * 
	 * @param name
	 *            the button name
	 * @param label
	 *            the button label
	 */
    public Button(String name, String label) {
        super(name, label);
    }

    /**
	 * Return a HTML rendered Button string. Note the button label is rendered
	 * as the HTML "value" attribute.
	 * 
	 * @see Object#toString()
	 * 
	 * @return a HTML rendered Button string
	 */
    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(50);
        buffer.elementStart("span");
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("class", "yuibutton");
        buffer.closeTag();
        buffer.elementStart("span");
        buffer.appendAttribute("class", "first-child");
        buffer.closeTag();
        buffer.elementStart("input");
        buffer.appendAttribute("type", getType());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("value", getLabel());
        buffer.appendAttribute("title", getTitle());
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }
        appendAttributes(buffer);
        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }
        buffer.elementEnd();
        buffer.elementEnd("span");
        buffer.elementEnd("span");
        return buffer.toString();
    }

    /**
	 * Return the input type: '<tt>button</tt>'.
	 * 
	 * @return the input type: '<tt>button</tt>'
	 */
    @Override
    public String getType() {
        return "submit";
    }

    /**
	 * Return the HTML CSS and JavaScript includes.
	 * 
	 * @see net.sf.click.Control#getHtmlImports()
	 * 
	 * @return the HTML CSS and JavaScript includes
	 */
    @Override
    public String getHtmlImports() {
        String[] args = { YUI_VERSION, getId(), getLabel(), "false" };
        LOGGER.debug("YUI button html imports: " + args);
        return MessageFormat.format(HTML_IMPORTS, (Object[]) args);
    }
}
