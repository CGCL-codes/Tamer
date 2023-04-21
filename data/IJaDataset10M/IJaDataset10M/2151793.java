package org.gbif.checklistbank.api.model.vocabulary;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaxonomicStatusTest {

    @Test
    public void testIsSynonym() {
        assertFalse(TaxonomicStatus.ACCEPTED.isSynonym());
        assertTrue(TaxonomicStatus.DETERMINATION_SYNONYM.isSynonym());
    }
}
