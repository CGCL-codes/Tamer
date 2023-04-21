package com.ibm.bx.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ibm
 * Date: 2006-12-5
 * Time: 11:21:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseObject implements Serializable {

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}
