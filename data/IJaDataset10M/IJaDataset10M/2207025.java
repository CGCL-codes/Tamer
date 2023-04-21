package net.sf.autodao.impl.hibernate;

import java.lang.reflect.Method;
import java.util.Map;
import net.sf.autodao.Dao;
import net.sf.autodao.PersistentEntity;
import net.sf.autodao.QueryArgumentTransformer;
import net.sf.autodao.impl.AbstractDaoFactoryBean;
import net.sf.autodao.impl.FinderExecutor;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Hibernate DAO factory.
 * <p/>
 * NOT FOR PUBLIC USE.
 */
public final class HibernateDaoFactoryBean extends AbstractDaoFactoryBean {

    @NotNull
    private final SessionFactory sessionFactory;

    HibernateDaoFactoryBean(@NotNull Class<?> daoInterface, @Nullable Class<? extends PersistentEntity<?>> entityType, @NotNull PlatformTransactionManager transactionManager, @NotNull SessionFactory sessionFactory) {
        super(daoInterface, entityType, transactionManager);
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings({ "unchecked" })
    @NotNull
    protected FinderExecutor createDao(@Nullable final Class<?> entityType) {
        if (entityType == null) {
            return new HibernateDao(sessionFactory);
        } else {
            final String msg = entityType.getName() + " is not a persistent class (it is not registered in SessionFactory)";
            Assert.notNull(sessionFactory.getClassMetadata(entityType), msg);
            return new HibernateEntityDao(entityType, sessionFactory);
        }
    }

    /** Validates DAO interface. */
    protected void validate(@NotNull final Class<?> daoInterface, @NotNull final Map<Class<?>, QueryArgumentTransformer> transformers) {
        ReflectionUtils.doWithMethods(daoInterface, new ReflectionUtils.MethodCallback() {

            @Override
            public void doWith(final Method method) {
                if (method.getDeclaringClass().equals(Dao.class)) return;
                HibernateParametersChecker.check(sessionFactory, method, transformers);
            }
        });
    }
}
