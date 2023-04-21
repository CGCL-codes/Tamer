package com.firefun.FileUpload;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 这个类的功能等同于JSP内置的对象request。只所以提供这个类，是因为对于文件上传表单，
 * 通过request对象无法获得表单项的值，必须通过jspSmartUpload组件提供的Request对象
 * 来获取。
 * <p>Title:</p>
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2008</p>
 * Date: 2008-11-3
 * Time: 下午04:08:25
 * @author firefun
 */
public class Request {

    private Hashtable m_parameters;

    private int m_counter;

    public Request() {
        m_parameters = new Hashtable();
        m_counter = 0;
    }

    protected void putParameter(String s, String s1) {
        if (s == null) {
            throw new IllegalArgumentException("The name of an element cannot be null.");
        }
        if (m_parameters.containsKey(s)) {
            Hashtable hashtable = (Hashtable) m_parameters.get(s);
            hashtable.put(new Integer(hashtable.size()), s1);
        } else {
            Hashtable hashtable1 = new Hashtable();
            hashtable1.put(new Integer(0), s1);
            m_parameters.put(s, hashtable1);
            m_counter++;
        }
    }

    /**
     * 作用：获取指定参数之值。当参数不存在时，返回值为null。
     *
     * 创建时间: 2008-11-3下午04:09:39
     * @param s
     * @return
     * @author firefun
     * @version 1.0
     */
    public String getParameter(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        }
        Hashtable hashtable = (Hashtable) m_parameters.get(s);
        if (hashtable == null) {
            return null;
        } else {
            return (String) hashtable.get(new Integer(0));
        }
    }

    /**
     * 作用：取得Request对象中所有参数的名字，
     * 用于遍历所有参数。它返回的是一个枚举型的对象。 
     *
     * 创建时间: 2008-11-3下午04:10:40
     * @return
     * @author firefun
     * @version 1.0
     */
    public Enumeration getParameterNames() {
        return m_parameters.keys();
    }

    /**
     * 作用：当一个参数可以有多个值时，用此方法来取其值。它返回的是一个字符串数组。
     * 当参数不存在时，返回值为null。
     *
     * 创建时间: 2008-11-3下午04:10:04
     * @param s
     * @return
     * @author firefun
     * @version 1.0
     */
    public String[] getParameterValues(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        }
        Hashtable hashtable = (Hashtable) m_parameters.get(s);
        if (hashtable == null) {
            return null;
        }
        String as[] = new String[hashtable.size()];
        for (int i = 0; i < hashtable.size(); i++) {
            as[i] = (String) hashtable.get(new Integer(i));
        }
        return as;
    }
}
