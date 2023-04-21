package org.jxul;

/**
 * A container element which can contain any number of child elements. If the box has an orient attribute that is set to horizontal, the child elements are laid out from left to right in the order that they appear in the box. If orient is set to vertical, the child elements are laid out from top to bottom. Child elements do not overlap. The default orientation is horizontal.
 * @author Will Etson
 */
public interface XulBox extends XulElement {

    public static final String ELEMENT = "box";

    public static final String ATTR_VALIGN = "valign";

    public static final String ATTR_AUTOSTRETCH = "autostretch";

    public static final String ATTR_ONCOMMAND = "oncommand";

    String getValign();

    void setValign(String valign);

    String getAutostretch();

    void setAutostretch(String autostretch);

    String getOnComamnd();

    void setOnCommand(String oncommand);
}
