package org.hlj.commons.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.hlj.commons.common.CommonUtil;

/**
 * List的帮助类
 * @author WD
 * @since JDK5
 * @version 1.0 2009-09-08
 */
public final class Lists {

    /**
	 * 获得List实例 默认初始化大小为10
	 * @return List
	 */
    public static final <E> List<E> getList() {
        return getArrayList();
    }

    /**
	 * 获得List实例
	 * @param size 初始化大小
	 * @return List
	 */
    public static final <E> List<E> getList(int size) {
        return getArrayList(size);
    }

    /**
	 * 获得List实例
	 * @param c 初始化的集合
	 * @return List
	 */
    public static final <E> List<E> getList(Collection<E> c) {
        return getArrayList(c);
    }

    /**
	 * 获得List实例
	 * @param c 初始化的集合
	 * @return List
	 */
    public static final <E> List<E> getList(Collection<E>... c) {
        List<E> list = getList();
        for (int i = 0; i < c.length; i++) {
            list.addAll(c[i]);
        }
        return list;
    }

    /**
	 * 获得List实例
	 * @param es 初始化的数组
	 * @return List
	 */
    public static final <E> List<E> getList(E... e) {
        return getList(Arrays.toList(e));
    }

    /**
	 * 获得List实例 实现类是ArrayList 默认初始化大小为10
	 * @return List
	 */
    public static final <E> ArrayList<E> getArrayList() {
        return getArrayList(10);
    }

    /**
	 * 获得List实例 实现类是ArrayList
	 * @param size 初始化大小
	 * @return List
	 */
    public static final <E> ArrayList<E> getArrayList(int size) {
        return new ArrayList<E>(size);
    }

    /**
	 * 获得List实例 实现类是ArrayList
	 * @param c 初始化的集合
	 * @return List
	 */
    public static final <E> ArrayList<E> getArrayList(Collection<E> c) {
        return new ArrayList<E>(c);
    }

    /**
	 * 获得List实例 实现类是ArrayList
	 * @param es 初始化的数组
	 * @return List
	 */
    public static final <E> List<E> getArrayList(E... e) {
        return getArrayList(Arrays.toList(e));
    }

    /**
	 * 获得并发的List实例 实现类是CopyOnWriteArrayList
	 * @return 同步的List
	 */
    public static final <E> CopyOnWriteArrayList<E> getConcurrenrList() {
        return new CopyOnWriteArrayList<E>();
    }

    /**
	 * 获得并发的List实例 实现类是CopyOnWriteArrayList
	 * @param e 初始化数组
	 * @return 同步的List
	 */
    public static final <E> CopyOnWriteArrayList<E> getConcurrenrList(E... e) {
        return new CopyOnWriteArrayList<E>(e);
    }

    /**
	 * 获得并发的List实例 实现类是CopyOnWriteArrayList
	 * @param c 初始化的集合
	 * @return 同步的List
	 */
    public static final <E> CopyOnWriteArrayList<E> getConcurrenrList(Collection<E> c) {
        return new CopyOnWriteArrayList<E>(c);
    }

    /**
	 * 获得同步的List实例 实现类是ArrayList 默认初始化大小为10
	 * @return 同步的List
	 */
    public static final <E> List<E> getSyncList() {
        return getSyncList(10);
    }

    /**
	 * 获得同步的List实例 实现类是ArrayList
	 * @param size 初始化大小
	 * @return 同步的List
	 */
    public static final <E> List<E> getSyncList(int size) {
        List<E> list = getList(size);
        return Collections.synchronizedList(list);
    }

    /**
	 * 获得同步的List实例 实现类是ArrayList
	 * @param c 初始化的集合
	 * @return 同步的List
	 */
    public static final <E> List<E> getSyncList(Collection<E> c) {
        return Collections.synchronizedList(getList(c));
    }

    /**
	 * 获得List实例 实现类是ArrayList
	 * @param es 初始化的数组
	 * @return List
	 */
    public static final <E> List<E> getSyncList(E... e) {
        return getSyncList(Arrays.toList(e));
    }

    /**
	 * 获得同步的List实例 实现类是LinkedList
	 * @return 同步的List
	 */
    public static final <E> LinkedList<E> getLinkedList() {
        return new LinkedList<E>();
    }

    /**
	 * 获得同步的List实例 实现类是LinkedList
	 * @param c 初始化的集合
	 * @return 同步的List
	 */
    public static final <E> LinkedList<E> getLinkedList(Collection<E> c) {
        return new LinkedList<E>(c);
    }

    /**
	 * 获得List实例 实现类是LinkedList
	 * @param es 初始化的数组
	 * @return List
	 */
    public static final <E> List<E> getLinkedList(E... e) {
        return getLinkedList(Arrays.toList(e));
    }

    /**
	 * 返回列表从firstResult开始返回maxResults个元素
	 * @param list 元素列表
	 * @param begin 重begin开始取元素
	 * @param end 到end为止
	 * @return 返回获得元素列表
	 */
    public static final <E> List<E> getList(List<E> list, int firstResult, int maxResults) {
        if (CommonUtil.isEmpty(list) || maxResults < 1) {
            return Collections.emptyList();
        }
        int size = list.size();
        if (maxResults >= size) {
            return Collections.emptyList();
        }
        firstResult = firstResult < 0 ? 0 : firstResult;
        maxResults = (firstResult + maxResults) > size ? size - firstResult : maxResults;
        int len = firstResult + maxResults;
        List<E> ls = getList(maxResults);
        for (int i = firstResult; i < len; i++) {
            ls.add(list.get(i));
        }
        return ls;
    }

    /**
	 * 给List排序
	 * @param list 要排序的List
	 * @return 排完序的List
	 */
    public static final <E extends Comparable<? super E>> List<E> sort(List<E> list) {
        Collections.sort(list);
        return list;
    }

    /**
	 * 获得一个不可变的空List
	 * @return 一个不可变的空List
	 */
    public static final <E> List<E> emptyList() {
        return Collections.emptyList();
    }

    /**
	 * 私有构造 禁止外部实例化
	 */
    private Lists() {
    }
}
