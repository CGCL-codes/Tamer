package net.sf.raptor.ui.swing.actions;

import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.sf.raptor.logging.Trace;
import org.apache.commons.beanutils.MethodUtils;

/**
 * Used as an adaptor to call ActionMethods exposed by objects.
 * 
 * @author Thomas Goertz &lt;Thomas DOT Goertz AT xcom DOT de&gt;
 * @author Klaus Zimmermann &lt;Klaus DOT Zimmermann AT xcom DOT de&gt;
 * 
 * @see net.sf.raptor.ui.swing.actions.ActionUtils
 */
public class ReflectionAction extends AbstractAction {

    /** key used to lookup the displayname */
    private static final String DISPLAY_NAME_KEY = Action.NAME;

    /** the {@link Method} to invoke */
    private Method method;

    /** the Object to invoke the {@link #method} on */
    private Object object;

    /**
	 * Constructs a new {@link ReflectionAction} which will invoke the
	 * given {@link Method} on the given Object when triggered.
	 * @param objectValue the Object the Method is to be invoked upon
	 * @param methodValue the Method to invoke
	 */
    public ReflectionAction(Object objectValue, Method methodValue) {
        super();
        method = methodValue;
        object = objectValue;
        Method objMethod = MethodUtils.getAccessibleMethod(object.getClass(), method.getName(), new Class[0]);
        if (objMethod == null) {
            throw new IllegalArgumentException("Given object of type \"" + object.getClass().getName() + "\"does not provide given method \"" + method.getName() + "\"");
        }
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(object.getClass());
        } catch (IntrospectionException e) {
            Trace.error(e);
        }
        MethodDescriptor[] methodDescriptors = info.getMethodDescriptors();
        for (int i = 0; i < methodDescriptors.length; i++) {
            if (methodDescriptors[i].getMethod().equals(method)) {
                if (methodDescriptors[i].getDisplayName().equals(method.getName())) {
                    String defDisplayName = displayify();
                    setDisplayName(defDisplayName);
                } else {
                    setDisplayName(methodDescriptors[i].getDisplayName());
                }
                break;
            }
        }
    }

    /**
	 * Converts the name of the ActionMethod to a simple default displayname.
	 * To achieve this
	 * <ol>
	 * <li>the 'do' prefix and the 'Action' suffix are removed</li>
	 * <li>in front of every capital letter a space is introduced.</li>
	 * </ol>
	 * @return the converted String
	 */
    private String displayify() {
        String defDisplayName = method.getName().substring(2, method.getName().lastIndexOf("Action"));
        for (int i = 1; i < defDisplayName.length(); i++) {
            if (Character.isUpperCase(defDisplayName.charAt(i))) {
                defDisplayName = defDisplayName.substring(0, i) + ' ' + defDisplayName.substring(i);
                i++;
            }
        }
        return defDisplayName;
    }

    /** 
	 * Invokes the {@link Method} passed in the Constructor on the Object passed therein.
	 * @param event the {@link ActionEvent} that triggered the call
	 * @see javax.swing.AbstractAction#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent event) {
        try {
            method.invoke(object, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Provides the displayname as set in the {@link Action}.
	 * @return the displayname
	 * @see Action
	 */
    public String getDisplayName() {
        return getValue(DISPLAY_NAME_KEY).toString();
    }

    /**
	 * Sets the displayname in the {@link Action}.
	 * @see Action
	 */
    public void setDisplayName(String displayName) {
        putValue(DISPLAY_NAME_KEY, displayName);
    }
}
