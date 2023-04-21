package ar.com.temporis.fixture;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author matias.sulik
 * 
 */
public class FixtureService implements Fixture {

    private static final String FIXTURES = "fixtures";

    protected static FixtureService instance = new FixtureService();

    private static final String FIXTURE_CONTEXT = "/fixtureContext.xml";

    private ApplicationContext context = new ClassPathXmlApplicationContext(FIXTURE_CONTEXT);

    private FixtureService() {
        super();
    }

    public static FixtureService getInstance() {
        return instance;
    }

    public void install() {
        Fixture fixture = (Fixture) context.getBean(FIXTURES);
        fixture.install();
    }
}
