package de.pannenleiter.client;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * ElementNode -- a xml element
 *
 *
 */
public class ElementNode extends TextNode {

    int dbid;

    AttributeListImpl attr;

    String childs;

    boolean canAddChilds;

    public ElementNode(TreeNode current, String element, AttributeList attr) {
        super(current, true, "", -1);
        init(element, attr);
    }

    public void init(String element, AttributeList attr) {
        this.attr = new AttributeListImpl();
        this.attr.setAttributeList(attr);
        String tmp = "<" + element;
        int l = attr.getLength();
        for (int i = 0; i < l; i++) {
            if (attr.getName(i).equals("plid")) {
                try {
                    dbid = Integer.parseInt(attr.getValue(i));
                } catch (Exception ex) {
                }
                ;
            } else if (attr.getName(i).equals("plchilds")) {
                childs = attr.getValue(i);
            } else {
                tmp += " " + attr.getName(i) + "=\"" + attr.getValue(i) + "\"";
            }
        }
        tmp += ">";
        label = tmp;
        icon = -1;
        if ("inner".equals(childs)) {
            setCanOpened(true);
            icon = ICON_FOLDER;
            canAddChilds = true;
        } else if ("leaf".equals(childs)) {
            setCanOpened(false);
            icon = ICON_DOC;
            canAddChilds = true;
        } else if ("packed".equals(childs)) {
            setCanOpened(false);
            icon = ICON_PACKAGE;
            canAddChilds = false;
        } else {
            setCanOpened(false);
            icon = -1;
            canAddChilds = false;
        }
    }
}
