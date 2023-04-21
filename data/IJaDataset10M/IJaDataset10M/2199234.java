package org.gbif.portal.web.filter;

import java.util.LinkedList;
import junit.framework.TestCase;

/**
 *
 * @author Dave Martin
 */
public class CriterionTest extends TestCase {

    public void testEquals() {
        CriterionDTO criterion1 = new CriterionDTO("1", "0", "1.0");
        CriterionDTO criterion2 = new CriterionDTO("1", "0", "1.0");
        assertTrue(criterion1.equals(criterion2));
    }

    @SuppressWarnings("unchecked")
    public void testContains() {
        CriterionDTO criterion1 = new CriterionDTO("1", "0", "1.0");
        CriterionDTO criterion2 = new CriterionDTO("1", "0", "1.0");
        LinkedList list = new LinkedList();
        list.add(criterion1);
        assertTrue(list.contains(criterion2));
    }
}
