package org.opennms.report.availability.svclayer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.OnmsDatabaseReportConfigDao;
import org.opennms.report.availability.AvailabilityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:org/opennms/report/availability/svclayer/OnmsReportServiceTest.xml" })
public class OnmsReportServiceTest {

    @Autowired
    AvailabilityCalculator m_classicCalculator;

    @Autowired
    AvailabilityCalculator m_calendarCalculator;

    @Autowired
    OnmsDatabaseReportConfigDao m_configDao;

    @Test
    public void testWiring() {
        Assert.assertNotNull(m_classicCalculator);
        Assert.assertNotNull(m_calendarCalculator);
        Assert.assertNotNull(m_configDao);
    }
}
