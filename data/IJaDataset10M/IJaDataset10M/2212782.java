package net.sf.javagimmicks.collections.diff;

import java.util.List;

public interface Difference<T> {

    public static final int NONE = -1;

    public int getDeleteStartIndex();

    public int getDeleteEndIndex();

    public List<T> getDeleteList();

    public boolean isDelete();

    public int getAddStartIndex();

    public int getAddEndIndex();

    public List<T> getAddList();

    public boolean isAdd();

    public Difference<T> invert();
}
