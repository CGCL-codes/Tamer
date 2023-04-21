package net.sourceforge.smokestack.ejb.ex03;

import static org.hamcrest.MatcherAssert.assertThat;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.sourceforge.smokestack.ejb.MockEJBContainer;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataReaderImplTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testReadDataFromLocalStore() throws NamingException, InstantiationException, IllegalAccessException {
        Context context = new InitialContext();
        MockEJBContainer container = new MockEJBContainer();
        DataReaderImpl r = container.getInstance(DataReaderImpl.class);
        assertThat(r.readDataFromLocalStore(), Is.is("LOCAL:42"));
        context.close();
    }

    @Test
    public final void testReadDataFromRemoteStore() throws InstantiationException, IllegalAccessException, NamingException {
        Context context = new InitialContext();
        MockEJBContainer container = new MockEJBContainer();
        DataReaderImpl r = container.getInstance(DataReaderImpl.class);
        assertThat(r.readDataFromRemoteStore(), Is.is("REMOTE:42"));
        context.close();
    }
}
