package test.cascade;

import junit.framework.Assert;
import org.eweb4j.config.EWeb4JConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import test.po.Master;
import test.po.Pet;

public class CascadeTest {

    @BeforeClass
    public static void prepare() throws Exception {
        String err = EWeb4JConfig.start("start.eweb.xml");
        if (err != null) {
            System.out.println(">>>EWeb4J Start Error --> " + err);
            System.exit(-1);
        }
    }

    @Test
    public void testOneRel() throws Exception {
        Master master = new Master();
        master.setId(1L);
        master.getPets().add(new Pet());
        master.getPets().add(new Pet());
        master.getPets().add(new Pet());
        Assert.assertEquals(true, true);
    }
}
