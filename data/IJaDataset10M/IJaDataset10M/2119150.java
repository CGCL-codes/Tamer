package net.community.chest.swing.component;

import javax.swing.JColorChooser;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <C> The reflected {@link JColorChooser} type
 * @author Lyor G.
 * @since Jan 29, 2009 9:59:36 AM
 */
public class JColorChooserReflectiveProxy<C extends JColorChooser> extends JComponentReflectiveProxy<C> {

    protected JColorChooserReflectiveProxy(Class<C> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }

    public JColorChooserReflectiveProxy(Class<C> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    public static final JColorChooserReflectiveProxy<JColorChooser> CC = new JColorChooserReflectiveProxy<JColorChooser>(JColorChooser.class, true);
}
