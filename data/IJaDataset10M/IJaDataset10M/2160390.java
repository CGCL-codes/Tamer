package org.expasy.jpl.commons.base.record;

import junit.framework.Assert;
import org.expasy.jpl.commons.base.record.DataTableFactory.TypeManager;
import org.expasy.jpl.commons.base.record.DataTableFactory.TypeManager.IType;
import org.junit.Before;
import org.junit.Test;

public class TypeManagerTest {

    TypeManager typeManager;

    @Before
    public void setUp() throws Exception {
        typeManager = TypeManager.getInstance();
    }

    @Test
    public void test() {
        System.out.println(typeManager.toString());
    }

    @Test
    public void testParseInt() {
        IType type = typeManager.getType(Integer.class);
        int i = (Integer) type.transform("36");
        Assert.assertEquals(36, i);
    }
}
