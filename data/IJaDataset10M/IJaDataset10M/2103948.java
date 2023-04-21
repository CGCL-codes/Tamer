package org.fpse.schedular;

/**
 * Created on Mar 24, 2007 3:47:09 PM by Ajay
 */
public interface Prioritizable {

    public static final int LOWEST_PRIORITY = Integer.MAX_VALUE;

    public static final int HIGHEST_PRIORITY = 0;

    public static final int NORMAL_PRIORITY = (LOWEST_PRIORITY + HIGHEST_PRIORITY) / 2;

    public int getPriority();
}
