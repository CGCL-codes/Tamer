package net.sf.dozer.util.mapping.classmap;

import java.util.ArrayList;
import java.util.List;
import net.sf.dozer.util.mapping.AbstractDozerTest;
import net.sf.dozer.util.mapping.fieldmap.GenericFieldMap;

/**
 * @author tierney.matt
 */
public class ClassMapTest extends AbstractDozerTest {

    public void testAddFieldMappings() throws Exception {
        ClassMap cm = new ClassMap(null);
        GenericFieldMap fm = new GenericFieldMap(cm);
        cm.addFieldMapping(fm);
        assertNotNull(cm.getFieldMaps());
        assertTrue(cm.getFieldMaps().size() == 1);
        assertEquals(cm.getFieldMaps().get(0), fm);
    }

    public void testSetFieldMappings() throws Exception {
        ClassMap cm = new ClassMap(null);
        GenericFieldMap fm = new GenericFieldMap(cm);
        List fmList = new ArrayList();
        fmList.add(fm);
        cm.setFieldMaps(fmList);
        assertNotNull(cm.getFieldMaps());
        assertTrue(cm.getFieldMaps().size() == fmList.size());
        assertEquals(cm.getFieldMaps().get(0), fmList.get(0));
    }
}
