package org.modelibra.modeler.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Vincent Dussault
 * @author Dzenan Ridjanovic
 * @version 2006-12-10
 */
public final class BoxModelUtil {

    public static String getLongName(BoxModel boxModel) {
        String packageName = boxModel.getDiagramModel().getName().toLowerCase();
        StringBuffer sb = new StringBuffer(packageName);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == ' ') {
                sb.replace(i, i + 1, "");
                i--;
            }
        }
        sb.append("." + boxModel.getName());
        return sb.toString();
    }

    public static Map getAssociationItems12(BoxModel boxModel) {
        return BoxModelUtil.getAssociationItems(boxModel, 12);
    }

    public static Map getAssociationItems21(BoxModel boxModel) {
        return BoxModelUtil.getAssociationItems(boxModel, 21);
    }

    private static Map<LineModel, BoxModel> getAssociationItems(BoxModel boxModel, int dir) {
        HashMap<LineModel, BoxModel> nameTypeMap = new HashMap<LineModel, BoxModel>();
        BoxModel neighbor = null;
        Collection children = null;
        if (dir == 12) {
            children = boxModel.getChildren12();
        } else {
            children = boxModel.getChildren21();
        }
        LineModel child = null;
        for (Iterator x = children.iterator(); x.hasNext(); ) {
            child = (LineModel) x.next();
            if (dir == 12) {
                neighbor = child.getBoxModel2();
            } else {
                neighbor = child.getBoxModel1();
            }
            if (!neighbor.isAbstractDef()) {
                nameTypeMap.put(child, neighbor);
            }
        }
        return nameTypeMap;
    }
}
