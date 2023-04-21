package org.gbif.checklistbank.service.impl;

import org.gbif.checklistbank.service.ClbStatisticsService;
import org.gbif.checklistbank.utils.InjectingTestBase;
import org.gbif.checklistbank.utils.InjectingTestClassRunner;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectingTestClassRunner.class)
public class ClbStatisticsServicePgSqlIT extends InjectingTestBase {

    @Inject
    ClbStatisticsService service;

    @Test
    public void testUpdate() {
        service.refreshStatistics();
    }
}
