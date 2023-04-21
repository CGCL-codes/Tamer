package org.fao.fenix.persistence;

import org.fao.fenix.domain.testdata.Dog;
import org.springframework.test.jpa.AbstractJpaTests;

public abstract class BaseDaoTest extends AbstractJpaTests {

    public Dog dog = new Dog();

    public DaoTestDelegate delegate = new DaoTestDelegate();

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext-test.xml" };
    }
}
