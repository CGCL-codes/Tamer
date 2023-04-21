package net.sf.simplecq;

import javax.sql.DataSource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DataSourceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        super.prepareTestInstance(testContext);
        Object testInstance = testContext.getTestInstance();
        if (testInstance instanceof SimpleCQDataSourceAware) {
            DataSource dataSource = (DataSource) testContext.getApplicationContext().getBean("simpleCQDataSource");
            SimpleCQDataSourceAware simpleCQDataSourceAware = (SimpleCQDataSourceAware) testInstance;
            simpleCQDataSourceAware.setSimpleCQDataSource(dataSource);
        }
    }
}
