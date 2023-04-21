package org.progeeks.view;

import java.awt.event.ActionEvent;
import java.lang.reflect.*;
import java.util.Arrays;
import javax.swing.Action;
import org.progeeks.util.MethodIndex;
import org.progeeks.util.swing.ActionList;

/**
 *  Utility method for creating standard types of ViewActions.
 *
 *  @version   $Revision: 4225 $
 *  @author    Paul Speed
 */
public class ViewActions {

    public static ViewAction method(Object obj, String methodName) {
        Class c = obj.getClass();
        MethodIndex mi = MethodIndex.getMethodIndex(c, false);
        Method m = mi.findMethod(methodName, null);
        if (m == null) throw new IllegalArgumentException("Method[" + methodName + "] not found on class:" + c.getName());
        return new MethodAction(obj, m);
    }

    public static ActionList list(String name, Action... actions) {
        ActionList list = new ActionList(name);
        if (actions != null && actions.length > 0) list.addAll(Arrays.asList(actions));
        return list;
    }

    protected static class MethodAction extends ViewAction {

        private Object object;

        private Method method;

        public MethodAction(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public void actionPerformed(ActionEvent event) {
            try {
                method.invoke(object, (Object[]) null);
            } catch (Exception e) {
                throw new RuntimeException("Error running action", e);
            }
        }
    }
}
