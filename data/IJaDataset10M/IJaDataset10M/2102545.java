package net.community.chest.awt.attributes;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Dec 30, 2008 8:33:24 AM
 */
public interface Textable {

    public static final String ATTR_NAME = "text";

    public static final Class<?> ATTR_TYPE = String.class;

    String getText();

    void setText(String t);
}
