package org.springframework.context.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.ACATest;
import org.springframework.context.AbstractApplicationContextTests;
import org.springframework.context.BeanThatListens;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Rod Johnson
 */
public class StaticMessageSourceTests extends AbstractApplicationContextTests {

    protected static final String MSG_TXT1_US = "At '{1,time}' on \"{1,date}\", there was \"{2}\" on planet {0,number,integer}.";

    protected static final String MSG_TXT1_UK = "At '{1,time}' on \"{1,date}\", there was \"{2}\" on station number {0,number,integer}.";

    protected static final String MSG_TXT2_US = "This is a test message in the message catalog with no args.";

    protected static final String MSG_TXT3_US = "This is another test message in the message catalog with no args.";

    protected StaticApplicationContext sac;

    /** Overridden */
    public void testCount() {
        assertCount(15);
    }

    public void testMessageSource() throws NoSuchMessageException {
    }

    public void testGetMessageWithDefaultPassedInAndFoundInMsgCatalog() {
        assertTrue("valid msg from staticMsgSource with default msg passed in returned msg from msg catalog for Locale.US", sac.getMessage("message.format.example2", null, "This is a default msg if not found in MessageSource.", Locale.US).equals("This is a test message in the message catalog with no args."));
    }

    public void testGetMessageWithDefaultPassedInAndNotFoundInMsgCatalog() {
        assertTrue("bogus msg from staticMsgSource with default msg passed in returned default msg for Locale.US", sac.getMessage("bogus.message", null, "This is a default msg if not found in MessageSource.", Locale.US).equals("This is a default msg if not found in MessageSource."));
    }

    /**
	 * We really are testing the AbstractMessageSource class here.
	 * The underlying implementation uses a hashMap to cache messageFormats
	 * once a message has been asked for.  This test is an attempt to
	 * make sure the cache is being used properly.
	 * @see org.springframework.context.support.AbstractMessageSource for more details.
	 */
    public void testGetMessageWithMessageAlreadyLookedFor() {
        Object[] arguments = { new Integer(7), new Date(System.currentTimeMillis()), "a disturbance in the Force" };
        sac.getMessage("message.format.example1", arguments, Locale.US);
        assertTrue("2nd search within MsgFormat cache returned expected message for Locale.US", sac.getMessage("message.format.example1", arguments, Locale.US).indexOf("there was \"a disturbance in the Force\" on planet 7.") != -1);
        Object[] newArguments = { new Integer(8), new Date(System.currentTimeMillis()), "a disturbance in the Force" };
        assertTrue("2nd search within MsgFormat cache with different args returned expected message for Locale.US", sac.getMessage("message.format.example1", newArguments, Locale.US).indexOf("there was \"a disturbance in the Force\" on planet 8.") != -1);
    }

    /**
	 * Example taken from the javadocs for the java.text.MessageFormat class
	 */
    public void testGetMessageWithNoDefaultPassedInAndFoundInMsgCatalog() {
        Object[] arguments = { new Integer(7), new Date(System.currentTimeMillis()), "a disturbance in the Force" };
        assertTrue("msg from staticMsgSource for Locale.US substituting args for placeholders is as expected", sac.getMessage("message.format.example1", arguments, Locale.US).indexOf("there was \"a disturbance in the Force\" on planet 7.") != -1);
        assertTrue("msg from staticMsgSource for Locale.UK substituting args for placeholders is as expected", sac.getMessage("message.format.example1", arguments, Locale.UK).indexOf("there was \"a disturbance in the Force\" on station number 7.") != -1);
        assertTrue("msg from staticMsgSource for Locale.US that requires no args is as expected", sac.getMessage("message.format.example2", null, Locale.US).equals("This is a test message in the message catalog with no args."));
    }

    public void testGetMessageWithNoDefaultPassedInAndNotFoundInMsgCatalog() {
        try {
            sac.getMessage("bogus.message", null, Locale.US);
            fail("bogus msg from staticMsgSource for Locale.US without default msg should have thrown exception");
        } catch (NoSuchMessageException tExcept) {
            assertTrue("bogus msg from staticMsgSource for Locale.US without default msg threw expected exception", true);
        }
    }

    public void testMessageSourceResolvable() {
        String[] codes1 = new String[] { "message.format.example3", "message.format.example2" };
        MessageSourceResolvable resolvable1 = new DefaultMessageSourceResolvable(codes1, null, "default");
        try {
            assertTrue("correct message retrieved", MSG_TXT3_US.equals(sac.getMessage(resolvable1, Locale.US)));
        } catch (NoSuchMessageException ex) {
            fail("Should not throw NoSuchMessageException");
        }
        String[] codes2 = new String[] { "message.format.example99", "message.format.example2" };
        MessageSourceResolvable resolvable2 = new DefaultMessageSourceResolvable(codes2, null, "default");
        try {
            assertTrue("correct message retrieved", MSG_TXT2_US.equals(sac.getMessage(resolvable2, Locale.US)));
        } catch (NoSuchMessageException ex) {
            fail("Should not throw NoSuchMessageException");
        }
        String[] codes3 = new String[] { "message.format.example99", "message.format.example98" };
        MessageSourceResolvable resolvable3 = new DefaultMessageSourceResolvable(codes3, null, "default");
        try {
            assertTrue("correct message retrieved", "default".equals(sac.getMessage(resolvable3, Locale.US)));
        } catch (NoSuchMessageException ex) {
            fail("Should not throw NoSuchMessageException");
        }
        String[] codes4 = new String[] { "message.format.example99", "message.format.example98" };
        MessageSourceResolvable resolvable4 = new DefaultMessageSourceResolvable(codes4);
        try {
            sac.getMessage(resolvable4, Locale.US);
            fail("Should have thrown NoSuchMessageException");
        } catch (NoSuchMessageException ex) {
        }
    }

    /** Run for each test */
    protected ConfigurableApplicationContext createContext() throws Exception {
        StaticApplicationContext parent = new StaticApplicationContext();
        Map m = new HashMap();
        m.put("name", "Roderick");
        parent.registerPrototype("rod", org.springframework.beans.TestBean.class, new MutablePropertyValues(m));
        m.put("name", "Albert");
        parent.registerPrototype("father", org.springframework.beans.TestBean.class, new MutablePropertyValues(m));
        parent.refresh();
        parent.addListener(parentListener);
        this.sac = new StaticApplicationContext(parent);
        sac.registerSingleton("beanThatListens", BeanThatListens.class, new MutablePropertyValues());
        sac.registerSingleton("aca", ACATest.class, new MutablePropertyValues());
        sac.registerPrototype("aca-prototype", ACATest.class, new MutablePropertyValues());
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(sac.getDefaultListableBeanFactory());
        reader.loadBeanDefinitions(new ClassPathResource("testBeans.properties", getClass()));
        sac.refresh();
        sac.addListener(listener);
        StaticMessageSource messageSource = sac.getStaticMessageSource();
        messageSource.addMessage("message.format.example1", Locale.US, MSG_TXT1_US);
        messageSource.addMessage("message.format.example2", Locale.US, MSG_TXT2_US);
        messageSource.addMessage("message.format.example3", Locale.US, MSG_TXT3_US);
        messageSource.addMessage("message.format.example1", Locale.UK, MSG_TXT1_UK);
        return sac;
    }
}
