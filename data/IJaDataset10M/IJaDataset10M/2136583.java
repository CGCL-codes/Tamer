package demo.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import junit.framework.Assert;

/**
 * Junit3.8的方式更加简练，功能和j4也是一致的
 *
 * <pre>
 * ProfileValueSourceConfiguration - ProfileValue取值来源，默认为SystemProfileValueSource
 * Repeat - 重复执行次数
 * Timed - 超时时间
 * DirtiesContext - 在方法执行结束后重新创建Spring container
 * ExpectedException - 希望抛出的异常
 * IfProfileValue - 满足条件才执行测试
 * </pre>
 * @author Sun Xiaochen
 */
@ContextConfiguration(locations = { "/applicationContext-test.xml" }, inheritLocations = true)
@ProfileValueSourceConfiguration
public class SpringContextJunit38TestCase extends AbstractJUnit38SpringContextTests {

    @Autowired
    private MyBean myBean;

    @Repeat(10)
    @Timed(millis = 1000)
    @DirtiesContext
    @ExpectedException(value = IllegalAccessException.class)
    @IfProfileValue(name = "java.vendor", value = "Sun Microsystems Inc.")
    public void testGetName() {
        Assert.assertEquals("spring bean test", myBean.getName());
    }

    @IfProfileValue(name = "java.vendor", values = { "Sun Microsystems Inc.", "Ubuntu9.04" })
    public void testGetName1() {
        Assert.assertEquals("spring bean test", myBean.getName());
    }
}
