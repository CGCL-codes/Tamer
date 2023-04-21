package com.googlecode.javacv;

import java.beans.PropertyChangeListener;
import java.beans.beancontext.BeanContextSupport;
import java.util.Arrays;

/**
 *
 * @author Samuel Audet
 */
public class BaseSettings extends BeanContextSupport implements Comparable<BaseSettings> {

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcSupport.addPropertyChangeListener(listener);
        for (Object s : toArray()) {
            if (s instanceof BaseChildSettings) {
                ((BaseChildSettings) s).addPropertyChangeListener(listener);
            } else if (s instanceof BaseSettings) {
                ((BaseSettings) s).addPropertyChangeListener(listener);
            }
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcSupport.removePropertyChangeListener(listener);
        for (Object s : toArray()) {
            if (s instanceof BaseChildSettings) {
                ((BaseChildSettings) s).removePropertyChangeListener(listener);
            } else if (s instanceof BaseSettings) {
                ((BaseSettings) s).addPropertyChangeListener(listener);
            }
        }
    }

    public int compareTo(BaseSettings o) {
        return getName().compareTo(o.getName());
    }

    protected String getName() {
        return "";
    }

    @Override
    public Object[] toArray() {
        Object[] a = super.toArray();
        Arrays.sort(a);
        return a;
    }

    @Override
    public Object[] toArray(Object[] a) {
        a = super.toArray(a);
        Arrays.sort(a);
        return a;
    }
}
