package net.entelijan.cobean.util;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.entelijan.cobean.core.ColumnDesc;
import net.entelijan.cobean.core.IMultiSelectionListCobean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class CobeanUtil {

    private static Log log = LogFactory.getLog(CobeanUtil.class);

    private CobeanUtil() {
        super();
    }

    public static <T> List<T> createListWithOneEntry(T entry) {
        ArrayList<T> re = new ArrayList<T>();
        re.add(entry);
        return re;
    }

    public static <T> void removeSelected(IMultiSelectionListCobean<T> compBean) {
        List<T> sel = compBean.getSelectedValue();
        if (!sel.isEmpty()) {
            List<T> data = compBean.getValue();
            if (!data.isEmpty()) {
                for (T bean : sel) {
                    data.remove(bean);
                }
            }
            compBean.setValue(data);
        }
    }

    public static <T> List<Interval> createSelectionIntervals(List<T> items, List<T> selectedItems) {
        ArrayList<Interval> re = new ArrayList<Interval>();
        if (selectedItems != null) {
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            for (T item : selectedItems) {
                int idx = items.indexOf(item);
                if (idx >= 0) {
                    indexes.add(idx);
                }
            }
            Collections.sort(indexes);
            if (indexes.size() > 0) {
                int start = indexes.get(0);
                int prev = start;
                for (int i = 1; i < indexes.size(); i++) {
                    int akt = indexes.get(i);
                    if (prev + 1 < akt) {
                        re.add(new Interval(start, prev));
                        start = akt;
                        prev = akt;
                    } else {
                        prev = akt;
                    }
                }
                re.add(new Interval(start, indexes.get(indexes.size() - 1)));
            }
        }
        return re;
    }

    public static void addPropertyChangeListenerUsingReflection(Object bean, PropertyChangeListener propertyChangeListener) {
        try {
            boolean found = false;
            for (Method method : bean.getClass().getMethods()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == PropertyChangeListener.class) {
                    log.debug("found method '" + method.getName() + "' for adding property change listener");
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(bean, propertyChangeListener);
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("Could not find method for adding property change listener in " + bean);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not [addPropertyChangeListenerUsingReflection] on " + bean + " because: " + e.getMessage(), e);
        }
    }

    public static String createText(Object object, List<ColumnDesc> columnDescs) {
        String text;
        if (columnDescs == null || columnDescs.isEmpty()) {
            if (object == null) {
                text = "";
            } else {
                text = "" + object;
            }
        } else {
            StringBuilder sb = new StringBuilder();
            if (object != null) {
                BeanWrapper wvalue = new BeanWrapperImpl(object);
                int cnt = 0;
                for (ColumnDesc cd : columnDescs) {
                    if (cnt++ > 0) {
                        sb.append(" ");
                    }
                    sb.append(wvalue.getPropertyValue(cd.getPropertyName()));
                }
            }
            text = sb.toString();
        }
        return text;
    }

    public static Icon loadIcon(String string) {
        Icon re = null;
        URL url = CobeanUtil.class.getClassLoader().getResource(string);
        if (url == null) {
            log.warn("Could not find requested icon '" + string + "' in classpath");
        } else {
            re = new ImageIcon(url);
        }
        return re;
    }

    public static Object createInstance(final String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create object from '" + className + "' because: " + e.getMessage(), e);
        }
    }

    /**
	 * Creates a distinct id for a component using its name and its parent
	 * components name
	 * 
	 * @param view
	 *            The component
	 * @return the generated id
	 */
    public static String createIdForComponent(Object view) {
        String re = "";
        if (view instanceof Component) {
            re = createIdForComponent(new StringBuilder(), (Component) view);
        }
        return re;
    }

    private static String createIdForComponent(StringBuilder stringBuilder, Component view) {
        String re;
        String name = view.getName();
        if (name == null) {
            name = view.getClass().getName();
        }
        stringBuilder.append(name);
        if (view.getParent() == null) {
            re = stringBuilder.toString();
        } else {
            re = createIdForComponent(stringBuilder, view.getParent());
        }
        return re;
    }
}
