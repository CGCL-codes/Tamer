package net.sf.brightside.aikido.service.hibernate;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.AssertJUnit.assertEquals;
import java.util.LinkedList;
import java.util.List;
import net.sf.brightside.aikido.metamodel.Practice;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetByDateCommandImpTest {

    private GetByDateCommandImp getByDateCommandImp;

    private Session session;

    private SessionFactory sessionFactory;

    private Criteria criteria;

    private Practice practice;

    @BeforeMethod
    public void setUp() {
        getByDateCommandImp = new GetByDateCommandImp<Practice>();
        session = createStrictMock(Session.class);
        sessionFactory = createStrictMock(SessionFactory.class);
        criteria = createStrictMock(Criteria.class);
        practice = createStrictMock(Practice.class);
        getByDateCommandImp.setSessionFactory(sessionFactory);
        getByDateCommandImp.setType(Practice.class);
    }

    @Test
    public void testExecute() {
        expect(sessionFactory.getCurrentSession()).andReturn(session);
        expect(session.createCriteria(Practice.class)).andReturn(criteria);
        expect(criteria.add(isA(Criterion.class))).andReturn(criteria);
        List list = new LinkedList();
        list.add(practice);
        expect(criteria.list()).andReturn(list);
        replay(session);
        replay(sessionFactory);
        replay(criteria);
        assertEquals(list.get(0), getByDateCommandImp.execute());
        verify(session);
        verify(sessionFactory);
        verify(criteria);
    }
}
