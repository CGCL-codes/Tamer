package net.sf.sasl.language.placeholder.aop.resolver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Assert;
import net.sf.sasl.language.placeholder.aop.resolver.DatePlaceholderResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.language.placeholder.aop.resolver.DatePlaceholderResolver
 * DatePlaceholderResolver} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class DatePlaceholderResolverTest {

    /**
	 * The unit under test.
	 */
    private DatePlaceholderResolver underTest;

    @Before
    public void setUp() {
        underTest = new DatePlaceholderResolver();
    }

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.aop.resolver.DatePlaceholderResolver#resolve(String, Object[], net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment)
	 * resolve(...)} method.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testResolve() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Assert.assertEquals(dateFormat.format(new Date()), underTest.resolve("date", new Object[] { "yyyy-MM-dd" }, null));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Assert.assertEquals(dateFormat.format(calendar.getTime()), underTest.resolve("date", new Object[] { "yyyy-MM-dd", calendar.getTime() }, null));
    }
}
