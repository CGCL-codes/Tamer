package net.sf.brightside.eterminals.service.hibernate;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.AssertJUnit.assertTrue;
import java.util.LinkedList;
import java.util.List;
import net.sf.brightside.eterminals.facade.hibernate.GetFacadeImpl;
import net.sf.brightside.eterminals.metamodel.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetByUsernameImplTest {

    private GetByUsernameImpl getByUsernameImplUnderTest;

    private Session session;

    private SessionFactory sessionFactory;

    private Criteria criteria;

    private User user;

    protected GetByUsernameImpl createTestedObject() {
        return new GetByUsernameImpl();
    }

    @BeforeMethod
    public void setUp() {
        session = createStrictMock(Session.class);
        sessionFactory = createStrictMock(SessionFactory.class);
        getByUsernameImplUnderTest = createTestedObject();
        criteria = createStrictMock(Criteria.class);
        user = createStrictMock(User.class);
        getByUsernameImplUnderTest.setSessionFactory(sessionFactory);
        getByUsernameImplUnderTest.setType(User.class);
        GetFacadeImpl getFacade = new GetFacadeImpl();
        getFacade.setSessionFactory(sessionFactory);
        getByUsernameImplUnderTest.setGetFacade(getFacade);
    }

    @Test
    public void testExecute() {
        expect(sessionFactory.getCurrentSession()).andReturn(session);
        expect(session.createCriteria(User.class)).andReturn(criteria);
        expect(criteria.add(isA(Criterion.class))).andReturn(criteria);
        List list = new LinkedList();
        list.add(user);
        expect(criteria.list()).andReturn(list);
        replay(session);
        replay(sessionFactory);
        replay(criteria);
        assertTrue(getByUsernameImplUnderTest.execute().equals(list.get(0)));
        verify(session);
        verify(criteria);
        verify(sessionFactory);
    }
}
