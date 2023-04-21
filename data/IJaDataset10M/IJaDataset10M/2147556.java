package com.hisham.util.collections;

import java.util.*;

/**
 *
 * <p>Title: IntegerList</p>
 * <p>Description: Helper class to ease of readability to create a vector list
 * of Integer class
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p> </p>
 *
 * @author Ali Hisham Malik
 * @version 2.0
 */
public class IntegerList extends DynamicList<Integer> {

    /**
	 *
	 */
    private static final long serialVersionUID = -3369285969275297885L;

    /**
	 *
	 */
    public IntegerList() {
        super();
    }

    /**
	 *
	 * @param p0 int
	 */
    public IntegerList(int p0) {
        super(p0);
    }

    /**
	 *
	 * @param p0 Collection
	 */
    public IntegerList(Collection<Integer> p0) {
        super(p0);
    }

    /**
	 *
	 * @param objects Object[]
	 */
    public IntegerList(Integer[] objects) {
        super(objects);
    }

    /**
	 *
	 * @param index int
	 * @return Integer
	 */
    public Integer getInteger(int index) {
        return super.get(index);
    }

    /**
	 *
	 * @param index int
	 * @return Integer
	 */
    public Integer removeInteger(int index) {
        return super.get(index);
    }

    public boolean addInteger(Integer value) {
        return this.add(value);
    }

    public void addInteger(int index, Integer value) {
        this.add(index, value);
    }

    public Integer getNewElement() {
        return new Integer(-1);
    }
}
