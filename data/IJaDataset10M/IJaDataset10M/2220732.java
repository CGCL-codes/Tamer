package net.sf.brightside.mobilestock.core.spring;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import net.sf.brightside.mobilestock.metamodel.api.Share;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestShareBean {

    private ApplicationContext context;

    private Share share;

    @BeforeMethod
    public void prepare() {
        this.context = new ApplicationContextProvider().getContextInstance();
        this.share = (Share) context.getBean(Share.class.getName());
        this.share.setName("Coca Cola");
    }

    @Test
    public void testShareNotNull() {
        System.out.println("Testing Share from Spring");
        assertNotNull(this.share);
    }

    @Test
    public void testName() {
        this.share.setName("Limores");
        assertNotNull(this.share.getName());
        assertEquals(this.share.getName(), "Limores");
    }
}
