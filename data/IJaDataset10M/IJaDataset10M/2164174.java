package org.xfeep.alaya.orm.jpa;

import java.util.Collections;
import java.util.Map;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import org.xfeep.asura.core.ComponentType;
import org.xfeep.asura.core.CoreConsts;
import org.xfeep.asura.core.annotation.Activate;
import org.xfeep.asura.core.annotation.Config;
import org.xfeep.asura.core.annotation.Deactivate;
import org.xfeep.asura.core.annotation.Property;
import org.xfeep.asura.core.annotation.Ref;
import org.xfeep.asura.core.annotation.Service;

@Service(type = ComponentType.ON_DEMAND)
@Config("")
public class EntityManagerFactoryImp implements EntityManagerFactory {

    PersistenceProvider persistenceProvider;

    @Property
    String adaptor;

    public PersistenceProvider getPersistenceProvider() {
        return persistenceProvider;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public String getAdaptor() {
        return adaptor;
    }

    public void setAdaptor(String adaptor) {
        this.adaptor = adaptor;
    }

    public PersistenceUnitInfoImp getPersistenceUnitInfo() {
        return persistenceUnitInfo;
    }

    public void setPersistenceUnitInfo(PersistenceUnitInfoImp persistenceUnitInfo) {
        this.persistenceUnitInfo = persistenceUnitInfo;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    public EntityManagerFactory getDelegate() {
        return delegate;
    }

    public void setDelegate(EntityManagerFactory delegate) {
        this.delegate = delegate;
    }

    @Property
    PersistenceUnitInfoImp persistenceUnitInfo;

    @Ref
    TransactionManager transactionManager;

    @Ref(matcher = "_cid=$.persistenceUnitInfo.jtaDataSourceName" + CoreConsts.ON_DEMAND_SEPRATOR)
    DataSource datasource;

    EntityManagerFactory delegate;

    @Activate
    public void initDelegate() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        JpaAdaptor jpaAdaptor = (JpaAdaptor) Thread.currentThread().getContextClassLoader().loadClass(adaptor).newInstance();
        jpaAdaptor.setUpTransactionManager(transactionManager);
        persistenceProvider = jpaAdaptor.getPersistenceProvider();
        persistenceUnitInfo.jtaDataSource = datasource;
        delegate = persistenceProvider.createContainerEntityManagerFactory(jpaAdaptor.addAditionalInfo(persistenceUnitInfo), Collections.EMPTY_MAP);
    }

    @Deactivate
    public void close() {
        if (delegate != null) {
            delegate.close();
        }
    }

    public EntityManager createEntityManager() {
        return delegate.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public EntityManager createEntityManager(Map map) {
        return delegate.createEntityManager(map);
    }

    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return delegate.getPersistenceUnitUtil();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }
}
