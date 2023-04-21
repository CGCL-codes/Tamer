package com.ibm.aglets.tahiti;

import java.awt.TextField;
import java.util.Vector;
import com.ibm.aglets.security.PolicyFileParsingException;
import com.ibm.aglets.security.PolicyPermission;

class PermissionPanel extends EditorPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -8376339851700076305L;

    protected static final String LABEL_ACTIONS = "Actions";

    private static final int LENGTH_ACTIONS = 15;

    protected TextField actions = new TextField(LENGTH_ACTIONS);

    public static final PolicyPermission toPermission(String className, String text) {
        PolicyPermission permission = null;
        try {
            permission = new PolicyPermission(className);
        } catch (ClassNotFoundException excpt) {
            return null;
        }
        if (permission != null) {
            Vector args = toVector(text);
            final int num = args.size();
            int idx = 0;
            for (idx = 0; idx < num; idx++) {
                final String str = (String) args.elementAt(idx);
                switch(idx) {
                    case 0:
                        if ((str != null) && !str.equals("")) {
                            permission.setTargetName(str);
                        }
                        break;
                    case 1:
                        if ((str != null) && !str.equals("")) {
                            permission.setActions(str);
                        }
                        break;
                }
            }
            try {
                permission.create();
            } catch (PolicyFileParsingException excpt) {
                return null;
            } catch (SecurityException excpt) {
                return null;
            }
        }
        return permission;
    }
}
