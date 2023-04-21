package org.mss.quartzjobs.model.hibernateview;

import java.lang.reflect.Method;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.mss.quartzjobs.CorePlugin;

/**
 * 30.11.2008 MK
 * Hibernate Label Provider
 * Gets Label description from Hibernate Persistence Object through Reflection
 * @author Administrator
 *
 */
public class HibernateLabelProvider extends LabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int index) {
        return super.getImage(element);
    }

    public String getColumnText(Object element, int index) {
        Method method = CorePlugin.getDefault().getReflectionUtil().getMethodbyNameandNumber(element.getClass(), "get", index);
        Object retobject = CorePlugin.getDefault().getReflectionUtil().invokeMethodbyName(element, method, new Object[] {});
        return retobject.toString();
    }
}
