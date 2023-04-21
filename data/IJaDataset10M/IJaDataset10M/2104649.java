package org.sevaapp.security.framework.test;

import org.junit.runner.RunWith;
import org.sevaapp.framework.test.AbstractSpringBeanBaseTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *  This is the base class for the unit test classes that use Spring Beans.
 * 
 * @author
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-security.xml" })
public abstract class AbstractSecuritySpringBeanBaseTest extends AbstractSpringBeanBaseTest {
}
