package cn.myapps.core.workflow.utility;

import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;

public class NameListTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse() throws Exception {
        String namelist = "{A001|a;B001|b;}";
        NameList nameList = NameList.parser(namelist);
        System.out.println(nameList.getOption());
        Collection colls = nameList.getData();
        for (Iterator iter = colls.iterator(); iter.hasNext(); ) {
            System.out.println(iter.next() instanceof NameNode);
        }
        System.out.println();
    }
}
