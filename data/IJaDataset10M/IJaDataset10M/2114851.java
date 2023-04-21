package com.zhouwei.dozer;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import net.sf.dozer.util.mapping.DozerBeanMapper;

public class MainTest {

    static Logger logger = Logger.getLogger(MainTest.class.getName());

    private net.sf.dozer.util.mapping.DozerBeanMapper mapper;

    public void setMapper(net.sf.dozer.util.mapping.DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

    /**
	 * @param args
	 */
    public void start() {
        A a = new A();
        B b = new B();
        C c = new C();
        D d = new D();
        d.setStr("d");
        c.setStr("c");
        c.setD(d);
        b.setC(c);
        b.setStr("b");
        a.setB(b);
        a.setStr("a");
        A1 a1 = (A1) mapper.map(a, A1.class);
        System.out.println(a1.getStr());
        System.out.println(a1.getB1().getStr());
        System.out.println(a1.getB1().getC1().getStr());
        System.out.println(a1.getB1().getC1().getD1().getStr1());
        logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:QQ");
    }

    public void stop() {
    }
}
