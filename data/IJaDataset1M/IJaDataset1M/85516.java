package net.sf.brightside.xlibrary.service.hibernate;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Expectation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.AssertJUnit.assertEquals;

public class GetByIdImplTest {

    private GetByIdImpl getByIdImplUnderTest;

    private Session session;

    private SessionFactory sessionFactory;

    private Serializable id;

    protected GetByIdImpl createTestedObject() {
        return new GetByIdImpl();
    }

    @BeforeMethod
    public void setUp() {
        getByIdImplUnderTest = createTestedObject();
        session = createStrictMock(Session.class);
        sessionFactory = createStrictMock(SessionFactory.class);
        getByIdImplUnderTest.setSessionFactory(sessionFactory);
        getByIdImplUnderTest.setType(Object.class);
        id = new Long(1);
        getByIdImplUnderTest.setId(id);
    }

    @Test
    public void testExecute() {
        expect(sessionFactory.getCurrentSession()).andReturn(session);
        Object object = new Object();
        expect(session.get(Object.class, id)).andReturn(object);
        List list = new LinkedList();
        list.add(object);
        replay(sessionFactory);
        replay(session);
        assertEquals(list, getByIdImplUnderTest.execute());
        verify(sessionFactory);
        verify(session);
    }
}
