package org.regola.dao.jpa;

public class OpenJpaGenericDaoTest extends AbstractJpaGenericDaoTest {

    @Override
    public String getConfigPath() {
        return "applicationContext-openjpa.xml";
    }

    @Override
    public boolean isDisabledInThisEnvironment(String testMethodName) {
        if ("testExecuteFinder_byAddress".equals(testMethodName)) {
            return true;
        }
        return super.isDisabledInThisEnvironment(testMethodName);
    }
}
