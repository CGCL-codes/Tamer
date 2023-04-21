package org.internna.ossmoney.model;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.transaction.TransactionConfiguration;

@Transactional
@RooIntegrationTest(entity = Widget.class)
@TransactionConfiguration(defaultRollback = true)
public class WidgetIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
