package net.sf.webwarp.modules.history.impl;

import net.sf.webwarp.modules.history.HistoryProvider;
import org.junit.Before;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import net.sf.webwarp.util.test.AbstractDependencyInjectionSpringContextTest;

public abstract class AHistoryTest extends AbstractDependencyInjectionSpringContextTest {

    private LocalSessionFactoryBean factoryBean;

    protected HistoryProvider historyProvider;

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "applicationContext.xml" };
    }

    @Before
    public void initDB() throws Exception {
        factoryBean.dropDatabaseSchema();
        factoryBean.createDatabaseSchema();
    }

    public void setFactoryBean(LocalSessionFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    public void setHistoryProvider(HistoryProvider historyProvider) {
        this.historyProvider = historyProvider;
    }
}
