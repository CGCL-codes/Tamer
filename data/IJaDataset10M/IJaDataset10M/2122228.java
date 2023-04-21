package net.techwatch.guice;

import javax.persistence.EntityManager;
import net.techwatch.guice.aop.TimeSpentLog;
import net.techwatch.guice.aop.TimeSpentLogInterceptor;
import net.techwatch.guice.dao.EntityManagerProvider;
import net.techwatch.guice.dao.PersonDao;
import net.techwatch.guice.dao.PersonDaoJpaImpl;
import net.techwatch.guice.service.PersonService;
import net.techwatch.guice.service.PersonServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class JpaPersonModule extends AbstractModule {

    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(TimeSpentLog.class), new TimeSpentLogInterceptor());
        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(PersonDao.class).to(PersonDaoJpaImpl.class);
        bind(PersonService.class).to(PersonServiceImpl.class);
    }
}
