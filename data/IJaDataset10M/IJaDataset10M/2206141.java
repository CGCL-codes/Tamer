package test.org.spark.util;

import junit.framework.Assert;
import org.spark.util.BeanUtils;
import org.spark.util.StringUtils;
import test.OsgiTestCase;

public class BeanUtilsTest extends OsgiTestCase {

    public class TestBean {

        int var1;

        String var2;

        public int getVar1() {
            return var1;
        }

        public void setVar1(int var1) {
            this.var1 = var1;
        }

        public String getVar2() {
            return var2;
        }

        public void setVar2(String var2) {
            this.var2 = var2;
        }

        public String echo(String _req) {
            return _req;
        }
    }

    public void testGetPropertyNames() {
        TestBean bean1 = new TestBean();
        String[] names = BeanUtils.getPropertyNames(bean1);
        System.out.println(StringUtils.merge(names, ","));
    }

    public void testSetAndGetValue() {
        TestBean bean1 = new TestBean();
        BeanUtils.setValue(bean1, "var1", 1);
        Assert.assertEquals(1, BeanUtils.getValue(bean1, "var1"));
        BeanUtils.setValue(bean1, "var2", "Hello,World!");
        Assert.assertEquals("Hello,World!", BeanUtils.getValue(bean1, "var2"));
    }

    public void testInvoke() {
        TestBean bean1 = new TestBean();
        Assert.assertEquals("Hello,World!", BeanUtils.invoke(bean1, "echo", "Hello,World!"));
    }
}
