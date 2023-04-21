package net.sf.raptor.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.type.Type;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SaveOrUpdateInterceptor implements Interceptor {

    private static Vector unsavedEntities = new Vector();

    public boolean onLoad(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
        return false;
    }

    public boolean onFlushDirty(Object arg0, Serializable arg1, Object[] arg2, Object[] arg3, String[] arg4, Type[] arg5) throws CallbackException {
        return false;
    }

    public boolean onSave(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
        unsavedEntities.add(arg0);
        return false;
    }

    public void onDelete(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
    }

    public void preFlush(Iterator arg0) throws CallbackException {
    }

    public void postFlush(Iterator arg0) throws CallbackException {
        for (; arg0.hasNext(); ) {
            Object element = arg0.next();
            unsavedEntities.remove(element);
        }
    }

    public Boolean isUnsaved(Object arg0) {
        for (int i = 0; i < unsavedEntities.size(); i++) {
            if (unsavedEntities.get(i).equals(arg0)) {
                return Boolean.TRUE;
            }
        }
        return null;
    }

    public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2, Object[] arg3, String[] arg4, Type[] arg5) {
        return null;
    }

    public Object instantiate(Class arg0, Serializable arg1) throws CallbackException {
        return null;
    }
}
