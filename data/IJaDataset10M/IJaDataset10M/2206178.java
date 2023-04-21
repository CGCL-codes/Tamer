package br.gov.frameworkdemoiselle.internal.proxy;

import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EntityManagerProxyTest {

    private EntityManagerProxy entityManagerProxy;

    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        this.entityManager = EasyMock.createMock(EntityManager.class);
        this.entityManagerProxy = new EntityManagerProxy(entityManager);
    }

    @Test
    public void testPersist() {
        this.entityManager.persist("teste");
        replay(this.entityManager);
        this.entityManagerProxy.persist("teste");
        verify(this.entityManager);
    }

    @Test
    public void testRemove() {
        this.entityManager.remove("teste");
        replay(this.entityManager);
        this.entityManagerProxy.remove("teste");
        verify(this.entityManager);
    }

    @Test
    public void testFlush() {
        this.entityManager.flush();
        replay(this.entityManager);
        this.entityManagerProxy.flush();
        verify(this.entityManager);
    }

    @Test
    public void testSetFlushMode() {
        FlushModeType flushModeType = null;
        this.entityManager.setFlushMode(flushModeType);
        replay(this.entityManager);
        this.entityManagerProxy.setFlushMode(flushModeType);
        verify(this.entityManager);
    }

    @Test
    public void testLockWithParamsStringAndLockModeType() {
        LockModeType lockMode = null;
        this.entityManager.lock("teste", lockMode);
        replay(this.entityManager);
        this.entityManagerProxy.lock("teste", lockMode);
        verify(this.entityManager);
    }

    @Test
    public void testLockWithParamsStringLockModeTypeAndMap() {
        LockModeType lockMode = null;
        Map<String, Object> map = null;
        this.entityManager.lock("teste", lockMode, map);
        replay(this.entityManager);
        this.entityManagerProxy.lock("teste", lockMode, map);
        verify(this.entityManager);
    }

    @Test
    public void testRefresh() {
        this.entityManager.refresh("teste");
        replay(this.entityManager);
        this.entityManagerProxy.refresh("teste");
        verify(this.entityManager);
    }

    @Test
    public void testRefreshWithParamsStringAndMap() {
        Map<String, Object> map = null;
        this.entityManager.refresh("teste", map);
        replay(this.entityManager);
        this.entityManagerProxy.refresh("teste", map);
        verify(this.entityManager);
    }

    @Test
    public void testRefreshWithParamsStringAndLockModeType() {
        LockModeType lockMode = null;
        this.entityManager.refresh("teste", lockMode);
        replay(this.entityManager);
        this.entityManagerProxy.refresh("teste", lockMode);
        verify(this.entityManager);
    }

    @Test
    public void testRefreshWithParamsStringLockModeTypeAndMap() {
        LockModeType lockMode = null;
        Map<String, Object> map = null;
        this.entityManager.refresh("teste", lockMode, map);
        replay(this.entityManager);
        this.entityManagerProxy.refresh("teste", lockMode, map);
        verify(this.entityManager);
    }

    @Test
    public void testClear() {
        this.entityManager.clear();
        replay(this.entityManager);
        this.entityManagerProxy.clear();
        verify(this.entityManager);
    }

    @Test
    public void testDetach() {
        this.entityManager.detach("teste");
        replay(this.entityManager);
        this.entityManagerProxy.detach("teste");
        verify(this.entityManager);
    }

    @Test
    public void testSetProperty() {
        this.entityManager.setProperty("teste", "teste");
        replay(this.entityManager);
        this.entityManagerProxy.setProperty("teste", "teste");
        verify(this.entityManager);
    }

    @Test
    public void testJoinTransaction() {
        this.entityManager.joinTransaction();
        replay(this.entityManager);
        this.entityManagerProxy.joinTransaction();
        verify(this.entityManager);
    }

    @Test
    public void testClose() {
        this.entityManager.close();
        replay(this.entityManager);
        this.entityManagerProxy.close();
        verify(this.entityManager);
    }

    @Test
    public void testMerge() {
        expect(this.entityManager.merge("teste")).andReturn("xxx");
        replay(this.entityManager);
        assertEquals("xxx", this.entityManagerProxy.merge("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testFindWithParamsClassAndObject() {
        expect(this.entityManager.find(String.class, "teste")).andReturn("retorno");
        replay(this.entityManager);
        assertEquals("retorno", this.entityManagerProxy.find(String.class, "teste"));
        verify(this.entityManager);
    }

    @Test
    public void testFindWithParamsClassObjectAndMap() {
        Map<String, Object> map = null;
        expect(this.entityManager.find(String.class, "teste", map)).andReturn("retorno");
        replay(this.entityManager);
        assertEquals("retorno", this.entityManagerProxy.find(String.class, "teste", map));
        verify(this.entityManager);
    }

    @Test
    public void testFindWithParamsClassObjectAndLockModeType() {
        LockModeType lock = null;
        expect(this.entityManager.find(String.class, "teste", lock)).andReturn("retorno");
        replay(this.entityManager);
        assertEquals("retorno", this.entityManagerProxy.find(String.class, "teste", lock));
        verify(this.entityManager);
    }

    @Test
    public void testFindWithParamsClassObjectLockModeTypeAndMap() {
        Map<String, Object> map = null;
        LockModeType lock = null;
        expect(this.entityManager.find(String.class, "teste", lock, map)).andReturn("retorno");
        replay(this.entityManager);
        assertEquals("retorno", this.entityManagerProxy.find(String.class, "teste", lock, map));
        verify(this.entityManager);
    }

    @Test
    public void testGetReference() {
        expect(this.entityManager.getReference(String.class, "teste")).andReturn("retorno");
        replay(this.entityManager);
        assertEquals("retorno", this.entityManagerProxy.getReference(String.class, "teste"));
        verify(this.entityManager);
    }

    @Test
    public void testGetFlushMode() {
        FlushModeType flushModeType = null;
        expect(this.entityManager.getFlushMode()).andReturn(flushModeType);
        replay(this.entityManager);
        assertEquals(flushModeType, this.entityManagerProxy.getFlushMode());
        verify(this.entityManager);
    }

    @Test
    public void testContains() {
        expect(this.entityManager.contains("teste")).andReturn(true);
        replay(this.entityManager);
        assertTrue(this.entityManagerProxy.contains("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testGetLockMode() {
        LockModeType lockModeType = null;
        expect(this.entityManager.getLockMode("teste")).andReturn(lockModeType);
        replay(this.entityManager);
        assertEquals(lockModeType, this.entityManagerProxy.getLockMode("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testGetProperties() {
        Map<String, Object> map = null;
        expect(this.entityManager.getProperties()).andReturn(map);
        replay(this.entityManager);
        assertEquals(map, this.entityManagerProxy.getProperties());
        verify(this.entityManager);
    }

    @Test
    public void testCreateQuery() {
        Query query = null;
        expect(this.entityManager.createQuery("teste")).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.createQuery("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testCreateQueryWithParamCriteria() {
        TypedQuery<Object> typeQuery = null;
        CriteriaQuery<Object> criteriaQuery = null;
        expect(this.entityManager.createQuery(criteriaQuery)).andReturn(typeQuery);
        replay(this.entityManager);
        assertEquals(typeQuery, this.entityManagerProxy.createQuery(criteriaQuery));
        verify(this.entityManager);
    }

    @Test
    public void testCreateQueryWithParamStringAndClass() {
        TypedQuery<String> typeQuery = null;
        expect(this.entityManager.createQuery("teste", String.class)).andReturn(typeQuery);
        replay(this.entityManager);
        assertEquals(typeQuery, this.entityManagerProxy.createQuery("teste", String.class));
        verify(this.entityManager);
    }

    @Test
    public void testCreateNamedQuery() {
        Query query = null;
        expect(this.entityManager.createNamedQuery("teste")).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.createNamedQuery("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testCreateNamedQueryWithParamsStringAndClass() {
        TypedQuery<String> typedQuery = null;
        expect(this.entityManager.createNamedQuery("teste", String.class)).andReturn(typedQuery);
        replay(this.entityManager);
        assertEquals(typedQuery, this.entityManagerProxy.createNamedQuery("teste", String.class));
        verify(this.entityManager);
    }

    @Test
    public void testCreateNativeQuery() {
        Query query = null;
        expect(this.entityManager.createNativeQuery("teste")).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.createNativeQuery("teste"));
        verify(this.entityManager);
    }

    @Test
    public void testCreateNativeQueryWithParamsStringAndClass() {
        Query query = null;
        expect(this.entityManager.createNativeQuery("teste", String.class)).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.createNativeQuery("teste", String.class));
        verify(this.entityManager);
    }

    @Test
    public void testCreateNativeQueryWithParamsStringAndString() {
        Query query = null;
        expect(this.entityManager.createNativeQuery("teste", "teste")).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.createNativeQuery("teste", "teste"));
        verify(this.entityManager);
    }

    @Test
    public void testUnwrap() {
        String query = null;
        expect(this.entityManager.unwrap(String.class)).andReturn(query);
        replay(this.entityManager);
        assertEquals(query, this.entityManagerProxy.unwrap(String.class));
        verify(this.entityManager);
    }

    @Test
    public void testGetDelegate() {
        Object obj = null;
        expect(this.entityManager.getDelegate()).andReturn(obj);
        replay(this.entityManager);
        assertEquals(obj, this.entityManagerProxy.getDelegate());
        verify(this.entityManager);
    }

    @Test
    public void testIsOpen() {
        expect(this.entityManager.isOpen()).andReturn(true);
        replay(this.entityManager);
        assertTrue(this.entityManagerProxy.isOpen());
        verify(this.entityManager);
    }

    @Test
    public void testGetTransaction() {
        EntityTransaction entityTransaction = null;
        expect(this.entityManager.getTransaction()).andReturn(entityTransaction);
        replay(this.entityManager);
        assertEquals(entityTransaction, this.entityManagerProxy.getTransaction());
        verify(this.entityManager);
    }

    @Test
    public void testGetEntityManagerFactory() {
        EntityManagerFactory entityManagerFactory = null;
        expect(this.entityManager.getEntityManagerFactory()).andReturn(entityManagerFactory);
        replay(this.entityManager);
        assertEquals(entityManagerFactory, this.entityManagerProxy.getEntityManagerFactory());
        verify(this.entityManager);
    }

    @Test
    public void testGetCriteriaBuilder() {
        CriteriaBuilder criteriaBuilder = null;
        expect(this.entityManager.getCriteriaBuilder()).andReturn(criteriaBuilder);
        replay(this.entityManager);
        assertEquals(criteriaBuilder, this.entityManagerProxy.getCriteriaBuilder());
        verify(this.entityManager);
    }

    @Test
    public void testGetMetamodel() {
        Metamodel metamodel = null;
        expect(this.entityManager.getMetamodel()).andReturn(metamodel);
        replay(this.entityManager);
        assertEquals(metamodel, this.entityManagerProxy.getMetamodel());
        verify(this.entityManager);
    }

    @Test
    @Ignore
    public void testEquals() {
        Object obj = null;
        expect(this.entityManager.equals(obj)).andReturn(true);
        replay(this.entityManager);
        assertTrue(this.entityManagerProxy.equals(obj));
        verify(this.entityManager);
    }

    @Test
    @Ignore
    public void testHashCode() {
        expect(this.entityManager.hashCode()).andReturn(0);
        replay(this.entityManager);
        assertEquals(0, this.entityManagerProxy.hashCode());
        verify(this.entityManager);
    }

    @Test
    @Ignore
    public void testToString() {
        expect(this.entityManager.toString()).andReturn("teste");
        replay(this.entityManager);
        assertEquals("teste", this.entityManagerProxy.toString());
        verify(this.entityManager);
    }
}
