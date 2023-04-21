package com.joe.test.serializable;

import java.io.Serializable;
import com.joe.common.tool.JoeUtil;

public class C extends A implements Serializable {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        C c = new C();
        System.out.println(c.b);
        JoeUtil.serialize("f:\\joe", c);
        C cc = (C) JoeUtil.deserialize("f:\\joe");
        System.out.println(cc.b);
    }
}
