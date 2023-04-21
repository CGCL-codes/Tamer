package dsb.support.logging.cdi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.lang.reflect.Member;
import javax.enterprise.inject.spi.InjectionPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import dsb.support.cdi.InjectionPointAdapter;
import dsb.support.cdi.MemberAdapter;

public class SLF4JLoggerFactoryTest {

    /** Unit under test. */
    private SLF4JLoggerFactory factory;

    @Before
    public void setUp() {
        this.factory = new SLF4JLoggerFactory();
    }

    @After
    public void tearDown() {
        this.factory = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoggerWithNullInjectionPointThrowsException() {
        this.factory.getLogger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoggerWithNullInjectionPointMemberThrowsException() {
        this.factory.getLogger(new InjectionPointAdapter() {

            @Override
            public Member getMember() {
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoggerWithNullInjectionPointMemberDeclaringClassThrowsException() {
        this.factory.getLogger(new InjectionPointAdapter() {

            @Override
            public Member getMember() {
                return new MemberAdapter() {

                    @Override
                    public Class<?> getDeclaringClass() {
                        return null;
                    }
                };
            }
        });
    }

    /**
	 * Test that, when getting a {@link Logger} for a custom
	 * {@link InjectionPoint} which returns the class type of this test case
	 * class, the proper category Logger is created.
	 */
    @Test
    public void testGetLoggerReturnsCorrectCategoryLogger() {
        final InjectionPoint ip = new InjectionPointAdapter() {

            @Override
            public Member getMember() {
                return new MemberAdapter() {

                    @Override
                    public Class<?> getDeclaringClass() {
                        return SLF4JLoggerFactoryTest.class;
                    }
                };
            }
        };
        final Logger logger = this.factory.getLogger(ip);
        assertThat(logger.getName(), is(SLF4JLoggerFactoryTest.class.getCanonicalName()));
    }
}
