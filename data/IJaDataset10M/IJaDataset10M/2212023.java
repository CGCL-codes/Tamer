package jfun.yan.monitoring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * <p>
 * This class provides empty implementation for {@link ComponentMonitor}.
 * It can be used as a base class so that subclass can override only the events of interest. 
 * </p>
 * @author Michelle Lei
 *
 */
public class NopComponentMonitor implements ComponentMonitor {

    public void invocationFailed(Object obj, Method mtd, Object[] args, Throwable err, long duration) {
    }

    public void invoked(Object obj, Method mtd, Object[] args, Object result, long duration) {
    }

    public void invoking(Object obj, Method mtd, Object[] args) {
    }

    public void propertySet(Object obj, PropertyDescriptor desc, Object val, long duration) {
    }

    public void propertySetFailed(Object obj, PropertyDescriptor desc, Object val, Throwable err, long duration) {
    }

    public void propertySetting(Object obj, PropertyDescriptor desc, Object val) {
    }

    public void propertyGetFailed(Object obj, PropertyDescriptor desc, int ind, Throwable err, long duration) {
    }

    public void propertyGetting(Object obj, PropertyDescriptor desc, int ind) {
    }

    public void propertyGot(Object obj, PropertyDescriptor desc, int ind, Object val, long duration) {
    }

    public void propertyGetFailed(Object obj, PropertyDescriptor desc, Throwable err, long duration) {
    }

    public void propertyGetting(Object obj, PropertyDescriptor desc) {
    }

    public void propertyGot(Object obj, PropertyDescriptor desc, Object result, long duration) {
    }

    public void propertySet(Object obj, PropertyDescriptor desc, int ind, Object val, long duration) {
    }

    public void propertySetFailed(Object obj, PropertyDescriptor desc, int ind, Object val, Throwable err, long duration) {
    }

    public void propertySetting(Object obj, PropertyDescriptor desc, int ind, Object val) {
    }

    public void constructed(Constructor ctor, Object[] args, Object instance, long duration) {
    }

    public void constructing(Constructor ctor, Object[] args) {
    }

    public void constructionFailed(Constructor ctor, Object[] args, Throwable e, long duration) {
    }
}
