package net.woodstock.rockapi.jsp.test;

import junit.framework.TestCase;
import net.woodstock.rockapi.jsp.taglib.common.TLDCreator;
import net.woodstock.rockapi.jsp.taglib.html.table.ColumnTag;
import net.woodstock.rockapi.jsp.taglib.html.table.TableTag;

public class CreateTld extends TestCase {

    public void testTLD() throws Exception {
        System.out.println(TLDCreator.create(ColumnTag.class));
        System.out.println(TLDCreator.create(TableTag.class));
    }
}
