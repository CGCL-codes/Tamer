package com.loribel.commons.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import com.loribel.commons.abstraction.GB_ComponentStyle;
import com.loribel.commons.swing.style.GB_ComponentStyleFactory;
import com.loribel.commons.util.GB_IconTools;

/**
 * Component MenuItem for the Swing FWK. <br />
 * This class is directly derivated from the Swing class JMenuItem. <br />
 * This class permits to extends the possibilities of the default Swing class keeping
 * full compatibility with Swing. <br />
 * Generally, you will use only this derivated components to your project.
 *
 * @author Gregory Borelli
 * @version 2003/11/20 - 15:15:30 - gen 7.12
 */
public class GB_MenuItem extends JMenuItem {

    /**
     * Default style of all GB_MenuItem.
     * You can access to this property with GB_MenuItem.defaultStyle = .....
     */
    public static GB_ComponentStyle defaultStyle;

    /**
     * Constructor of GB_MenuItem without parameter.
     */
    public GB_MenuItem() {
        super();
        init();
    }

    /**
     * Constructor of GB_MenuItem with parameter(s).
     *
     * @param a_action Action -
     */
    public GB_MenuItem(Action a_action) {
        super(a_action);
        init();
    }

    /**
     * Constructor of GB_MenuItem with parameter(s).
     *
     * @param a_icon Icon -
     */
    public GB_MenuItem(Icon a_icon) {
        super(a_icon);
        init();
    }

    /**
     * Constructor of GB_MenuItem with parameter(s).
     *
     * @param a_name String -
     * @param a_icon Icon -
     */
    public GB_MenuItem(String a_name, Icon a_icon) {
        super(a_name, a_icon);
        init();
    }

    /**
     * Constructor of GB_MenuItem with parameter(s).
     *
     * @param a_name String -
     * @param a_iconName String -
     */
    public GB_MenuItem(String a_name, String a_iconName) {
        super(a_name);
        setIcon(a_iconName);
        init();
    }

    /**
     * Constructor of GB_MenuItem with parameter(s).
     *
     * @param a_name String -
     */
    public GB_MenuItem(String a_name) {
        this(a_name, (String) null);
    }

    /**
     * Method setStyle.
     * <br />
     *
     * @param a_style GB_ComponentStyle - the style to apply
     */
    public void setStyle(GB_ComponentStyle a_style) {
        if (a_style == null) {
            return;
        }
        a_style.applyToComponent(this);
    }

    /**
     * Method setStyle.
     * <br />
     *
     * @param a_styleName String - the name of style to apply, use {@link GB_ComponentStyleFactory}
     */
    public void setStyle(String a_styleName) {
        GB_ComponentStyle l_style = GB_ComponentStyleFactory.getStyle(a_styleName);
        setStyle(l_style);
    }

    /**
     * Method init.
     */
    private void init() {
        setStyle(defaultStyle);
    }

    /**
     * Ajoute un icon au menu.<p>
     * On utilise la factory d'images pour charger l'image.
     *
     * See : GB_ImagesFactory
     *
     * @param a_icon String - the icon to use in this menuItem
     */
    public void setIcon(String a_icon) {
        if (a_icon == null) {
            return;
        }
        Icon l_icon = GB_IconTools.get(a_icon);
        if (l_icon != null) {
            this.setIcon(l_icon);
        }
    }
}
