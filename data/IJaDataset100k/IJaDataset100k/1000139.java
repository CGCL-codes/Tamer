package org.helianto.process.filter.classic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.Serializable;
import org.helianto.core.Entity;
import org.helianto.core.filter.classic.AbstractEntityBackedFilter;
import org.helianto.process.InheritanceType;
import org.helianto.process.Operation;
import org.helianto.process.Process;
import org.helianto.process.ProcessDocument;
import org.helianto.process.filter.classic.ProcessDocumentFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Maur�cio Fernandes de Castro
 */
public class ProcessDocumentFilterTests {

    @Test
    public void constructor() {
        assertTrue(filter instanceof Serializable);
        assertTrue(filter instanceof AbstractEntityBackedFilter);
        assertSame(filter.getEntity(), entity);
        assertEquals(filter.getDocCode(), "");
        ProcessDocument document = new Process();
        filter = new ProcessDocumentFilter(document);
        assertEquals(filter.getParent(), document);
    }

    public static String OB = "order by alias.docCode ";

    public static String C0 = "alias.entity.id = 0 ";

    public static String C1 = "AND alias.class=Operation ";

    public static String C2 = "AND alias.docCode = 'DOCCODE' ";

    public static String C3 = "AND lower(alias.docName) like '%name%' ";

    public static String C4 = "AND alias.inheritanceType = 'C' ";

    @Test
    public void empty() {
        filter.setDocCode("");
        assertEquals(C0 + OB, filter.createCriteriaAsString(false));
    }

    @Test
    public void filterClazz() {
        filter.setClazz(Operation.class);
        assertEquals(C0 + C1 + OB, filter.createCriteriaAsString(false));
    }

    @Test
    public void select() {
        filter.setDocCode("DOCCODE");
        assertEquals(C0 + C2, filter.createCriteriaAsString(false));
    }

    @Test
    public void filterName() {
        filter.setDocNameLike("NAME");
        assertEquals(C0 + C3 + OB, filter.createCriteriaAsString(false));
    }

    @Test
    public void filterInheritance() {
        filter.setInheritanceType(InheritanceType.CONCRETE.getValue());
        assertEquals(C0 + C4 + OB, filter.createCriteriaAsString(false));
    }

    private ProcessDocumentFilter filter;

    private Entity entity = new Entity();

    @Before
    public void setUp() {
        filter = new ProcessDocumentFilter(entity, "");
    }
}
