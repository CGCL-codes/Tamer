package com.loribel.commons.util;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.loribel.commons.abstraction.GB_IconType;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIcon2Owner;
import com.loribel.commons.abstraction.GB_LabelIconDefault;
import com.loribel.commons.abstraction.GB_LabelIconLngSet;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_LabelIconSet;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.util.impl.GB_LabelIconDefaultImpl;
import com.loribel.commons.util.impl.GB_LabelIconImpl;
import com.loribel.commons.util.impl.GB_LabelIconLngImpl;

/**
 * Tools for GB_LabelIcon.
 *
 * @author Gregory Borelli
 */
public final class GB_LabelIconTools {

    public static void appendToLabel(GB_LabelIcon a_labelIcon, String a_labelToAppend) {
        String l_label = a_labelIcon.getLabel() + a_labelToAppend;
        updateLabel(a_labelIcon, l_label);
    }

    /**
     * Build a label icon to represent an object o.
     * <ul>
     *   <li>o instanceOf GB_LabelIcon => return o</li>
     *   <li>o instanceOf GB_LabelIconOwner => return o.getLabelIcon()</li>
     *   <li>o instanceOf Icon => new GB_LabelIconImpl("", (Icon)o)</li>
     *   <li>otherwise => return new GB_LabelIconImpl(o.toString())</li>
     * </ul>
     */
    public static GB_LabelIcon buildLabelIcon(Object o) {
        if (o == null) {
            return newLabelIcon("");
        }
        if (o instanceof GB_LabelIcon) {
            return (GB_LabelIcon) o;
        }
        if (o instanceof GB_LabelIconOwner) {
            return ((GB_LabelIconOwner) o).getLabelIcon();
        }
        if (o instanceof Icon) {
            return newLabelIcon("", (Icon) o);
        }
        String l_label = o.toString();
        GB_LabelIcon retour = newLabelIcon(l_label);
        return retour;
    }

    /**
     * Create a new Label icon using two points for label.
     */
    public static GB_LabelIcon decoreWithTwoPoints(GB_LabelIcon a_labelIcon) {
        if (a_labelIcon == null) {
            return null;
        }
        String l_label = a_labelIcon.getLabel();
        if ((l_label != null) && (!l_label.endsWith(": "))) {
            l_label += ": ";
        }
        Icon l_icon = a_labelIcon.getIcon();
        String l_description = a_labelIcon.getDescription();
        return newLabelIcon(l_label, l_icon, l_description);
    }

    public static String getDescription(GB_LabelIcon o) {
        if (o == null) {
            return null;
        }
        return o.getDescription();
    }

    public static String getDescription(GB_LabelIconOwner o) {
        if (o == null) {
            return null;
        }
        return getDescription(o.getLabelIcon());
    }

    public static Icon getIcon(GB_LabelIcon o) {
        if (o == null) {
            return null;
        }
        return o.getIcon();
    }

    public static Icon getIcon(GB_LabelIcon a_labelIcon, String a_type, boolean a_returnDefaultIfNotFound) {
        if (a_labelIcon == null) {
            return null;
        }
        Icon retour = a_labelIcon.getIcon(a_type);
        if (a_returnDefaultIfNotFound && retour == null) {
            retour = a_labelIcon.getIcon();
        }
        return retour;
    }

    public static Icon getIcon(GB_LabelIconOwner o) {
        if (o == null) {
            return null;
        }
        return getIcon(o.getLabelIcon());
    }

    public static Image getImage(GB_LabelIcon a_labelIcon) {
        Icon l_icon = getIcon(a_labelIcon);
        if (l_icon == null) {
            return null;
        }
        if (l_icon instanceof ImageIcon) {
            return ((ImageIcon) l_icon).getImage();
        }
        return null;
    }

    public static String getLabel(GB_LabelIcon o) {
        if (o == null) {
            return null;
        }
        return o.getLabel();
    }

    public static String getLabel(GB_LabelIconOwner o) {
        if (o == null) {
            return null;
        }
        return getLabel(o.getLabelIcon());
    }

    public static List getLabels(Collection a_labelIcons) {
        int len = CTools.getSize(a_labelIcons);
        List retour = new ArrayList(len);
        for (Iterator it = a_labelIcons.iterator(); it.hasNext(); ) {
            GB_LabelIcon l_item = (GB_LabelIcon) it.next();
            retour.add(getLabel(l_item));
        }
        return retour;
    }

    /**
     * Voir buildLabelIcon().
     */
    public static String getLabelSafe(Object o) {
        return buildLabelIcon(o).getLabel();
    }

    public static GB_LabelIcon getUserDescription(GB_StringAction a_sa) {
        GB_LabelIcon retour = null;
        if (a_sa instanceof GB_LabelIcon2Owner) {
            retour = ((GB_LabelIcon2Owner) a_sa).getLabelIcon2();
        }
        if ((retour == null) && (a_sa instanceof GB_LabelIconOwner)) {
            retour = ((GB_LabelIconOwner) a_sa).getLabelIcon();
        }
        return retour;
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconSet newLabelIcon(String a_label) {
        return new GB_LabelIconImpl(a_label);
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconSet newLabelIcon(String a_label, Icon a_icon) {
        return new GB_LabelIconImpl(a_label, a_icon);
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconSet newLabelIcon(String a_label, Icon a_icon, String a_description) {
        return new GB_LabelIconImpl(a_label, a_icon, a_description);
    }

    public static GB_LabelIcon newLabelIcon(String a_label, String a_icon) {
        Icon l_icon = GB_IconTools.get(a_icon);
        return newLabelIcon(a_label, l_icon);
    }

    public static GB_LabelIcon newLabelIcon(String a_label, String a_icon, String a_description) {
        Icon l_icon = GB_IconTools.get(a_icon);
        return newLabelIcon(a_label, l_icon, a_description);
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconDefault newLabelIconDefault(String a_label) {
        return new GB_LabelIconDefaultImpl(a_label);
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconDefault newLabelIconDefault(String a_label, Icon a_icon) {
        return new GB_LabelIconDefaultImpl(a_label, a_icon);
    }

    /**
     * Create a new GB_LabelIcon.
     */
    public static GB_LabelIconSet newLabelIconDefault(String a_label, Icon a_icon, String a_description) {
        return new GB_LabelIconDefaultImpl(a_label, a_icon, a_description);
    }

    public static GB_LabelIconLngSet newLabelIconLng() {
        return new GB_LabelIconLngImpl();
    }

    public static GB_LabelIconSet newLabelIconWithInfo(GB_LabelIcon a_labelIcon, int a_info) {
        return newLabelIconWithInfo(a_labelIcon, "" + a_info);
    }

    /**
     * Create a new Label Icon with an info between '[..]'.
     */
    public static GB_LabelIconSet newLabelIconWithInfo(GB_LabelIcon a_labelIcon, String a_info) {
        return new GB_LabelIconImpl(a_labelIcon.getLabel() + " [" + a_info + "]", a_labelIcon.getIcon(), a_labelIcon.getDescription());
    }

    public static void putIconX32(GB_LabelIconSet a_labelIcon, Icon a_icon) {
        if (a_labelIcon == null) {
            return;
        }
        a_labelIcon.putIcon(GB_IconType.X32, a_icon);
    }

    public static void putIconX32(GB_LabelIconSet a_labelIcon, String a_icon) {
        Icon l_icon = GB_IconTools.get(a_icon);
        putIconX32(a_labelIcon, l_icon);
    }

    public static GB_LabelIcon tryToLabelIcon(Object o) {
        if (o instanceof GB_LabelIcon) {
            return (GB_LabelIcon) o;
        }
        if (o instanceof GB_LabelIconOwner) {
            return ((GB_LabelIconOwner) o).getLabelIcon();
        }
        if (o instanceof Icon) {
            return newLabelIcon("", (Icon) o);
        }
        return null;
    }

    public static void updateDescription(GB_LabelIcon a_labelIcon, String a_desc) {
        if (!(a_labelIcon instanceof GB_LabelIconSet)) {
            return;
        }
        ((GB_LabelIconSet) a_labelIcon).setDescription(a_desc);
    }

    public static void updateIcon(GB_LabelIcon a_labelIcon, Icon a_icon) {
        if (!(a_labelIcon instanceof GB_LabelIconSet)) {
            return;
        }
        ((GB_LabelIconSet) a_labelIcon).setIcon(a_icon);
    }

    public static void updateLabel(GB_LabelIcon a_labelIcon, String a_label) {
        if (!(a_labelIcon instanceof GB_LabelIconSet)) {
            return;
        }
        ((GB_LabelIconSet) a_labelIcon).setLabel(a_label);
    }

    public static void updateWithDecoratedIcon(GB_LabelIcon a_labelIcon, Icon a_decoreIcon) {
        if (a_labelIcon == null) {
            return;
        }
        Icon l_icon = GB_IconDecoratorTools.getDecoratedIcon(a_labelIcon.getIcon(), a_decoreIcon);
        updateIcon(a_labelIcon, l_icon);
    }

    private GB_LabelIconTools() {
    }
}
