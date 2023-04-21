package org.hibernate.test.generated;

import junit.framework.Test;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.junit.functional.FunctionalTestClassTestSuite;

/**
 * Implementation of TriggerGeneratedValuesWithoutCachingTest.
 *
 * @author Steve Ebersole
 */
public class TriggerGeneratedValuesWithoutCachingTest extends AbstractGeneratedPropertyTest {

    public TriggerGeneratedValuesWithoutCachingTest(String x) {
        super(x);
    }

    public final String[] getMappings() {
        return new String[] { "generated/GeneratedPropertyEntity.hbm.xml" };
    }

    public boolean appliesTo(Dialect dialect) {
        return (dialect instanceof Oracle9Dialect);
    }

    public String getCacheConcurrencyStrategy() {
        return null;
    }

    public static Test suite() {
        return new FunctionalTestClassTestSuite(TriggerGeneratedValuesWithoutCachingTest.class);
    }
}
