package org.perfectjpattern.jee.integration.dao;

import org.perfectjpattern.support.test.*;

/**
 * Test suite for the {@link EclipseLinkDaoFactory} implementation
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Mar 1, 2009 1:20:27 PM $
 */
public class TestEclipseLinkDaoFactory extends AbstractTestSingleton<EclipseLinkDaoFactory> {

    /**
     * Default {@link TestEclipseLinkDaoFactory} constructor
     */
    public TestEclipseLinkDaoFactory() {
        super(EclipseLinkDaoFactory.class, EclipseLinkDaoFactory.getInstance());
    }
}
