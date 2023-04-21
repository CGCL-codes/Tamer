package com.loribel.commons.util.simplenode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.loribel.commons.abstraction.GB_SimpleNode;
import com.loribel.commons.abstraction.GB_SimpleNodeOptions;
import com.loribel.commons.abstraction.GB_SimpleNodeOptionsSet;
import com.loribel.commons.abstraction.GB_SimpleNodeSet;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_StringTools;

/**
 * Test GB_SimpleNodeTools.
 * 
 * @author Gregory Borelli
 */
public class GB_SimpleNodeToolsTest extends TestCase {

    public GB_SimpleNodeToolsTest(String a_name) {
        super(a_name);
    }

    private GB_SimpleNodeSet parseTree(String a_name, int a_type) throws IOException {
        String l_content = FTools.readResource(getClass(), a_name);
        String[] l_lines = GB_StringTools.toLinesArray(l_content);
        GB_SimpleNodeOptionsSet l_options = GB_SimpleNodeTools.newOptions();
        l_options.setType(a_type);
        GB_SimpleNodeParsorImpl l_parsor = new GB_SimpleNodeParsorImpl(l_options);
        GB_SimpleNodeSet l_node = l_parsor.parseTree("TREE", l_lines);
        GB_SimpleNodeTools.toSystemOut(a_name, l_node, l_options);
        return l_node;
    }

    public void test_compare2() throws Exception {
        String l_name = "tree3.txt";
        GB_SimpleNodeSet l_node = parseTree(l_name, GB_SimpleNodeOptions.TYPE.VALUE_NAME);
        l_name = "tree3b.txt";
        GB_SimpleNodeSet l_node2 = parseTree(l_name, GB_SimpleNodeOptions.TYPE.VALUE_NAME);
        GB_SimpleNode l_nodeCompare = GB_SimpleNodeTools.getCompare(l_node, l_node2);
        GB_SimpleNodeOptionsSet l_options = GB_SimpleNodeTools.newOptions();
        l_options.setUseRoot(false);
        l_options.setType(GB_SimpleNodeOptions.TYPE.VALUE_NAME);
        l_options.setTypeWrite(GB_SimpleNodeOptions.TYPE.NAME_VALUE);
        GB_SimpleNodeTools.toSystemOut("TREE3-VALUE_NAME", l_nodeCompare, l_options);
    }

    public void test_parseCsv(String a_racine, int a_ignoreCols) {
        List l_lines = new ArrayList();
        String[] l_csv = CTools.toArrayOfString(l_lines);
        GB_SimpleNodeOptionsSet l_options = new GB_SimpleNodeOptionsImpl();
        l_options.setIgnoreCols(a_ignoreCols);
        GB_SimpleNode l_node = GB_SimpleNodeTools.parseTree("ROOT", l_csv, l_options);
        assertNull("1.0", l_node);
        l_lines.add(a_racine + "root");
        l_csv = CTools.toArrayOfString(l_lines);
        l_node = GB_SimpleNodeTools.parseTree("ROOT", l_csv, l_options);
        assertNotNull("2.0", l_node);
        assertEquals("2.1", 1, l_node.getChildCount());
        assertEquals("2.2", a_racine + "root", l_node.getChildAt(0).getValue());
        l_lines.add(a_racine + "\tchild1");
        l_csv = CTools.toArrayOfString(l_lines);
        l_node = GB_SimpleNodeTools.parseTree("ROOT", l_csv, l_options);
        assertNotNull("3.0", l_node);
        assertEquals("3.1", 1, l_node.getChildCount());
        l_node = l_node.getChildAt(0);
        assertEquals("3.2", a_racine + "root", l_node.getValue());
        assertEquals("3.3", 1, l_node.getChildCount());
        assertEquals("3.4", (a_racine + "\tchild1").trim(), l_node.getChildAt(0).getValue());
        l_lines.add(a_racine + "\t\tchild1.1");
        l_lines.add(a_racine + "\tchild2");
        l_lines.add(a_racine + "\t\tchild2.1");
        l_lines.add(a_racine + "\t\tchild2.2");
        l_lines.add(a_racine + "\t\tchild2.3");
        l_csv = CTools.toArrayOfString(l_lines);
        l_node = GB_SimpleNodeTools.parseTree("ROOT", l_csv, l_options);
        assertNotNull("4.0", l_node);
        assertEquals("4.1", 1, l_node.getChildCount());
        l_node = l_node.getChildAt(0);
        assertEquals("4.2", a_racine + "root", l_node.getValue());
        assertEquals("4.3", 2, l_node.getChildCount());
        assertEquals("4.4", (a_racine + "\tchild1").trim(), l_node.getChildAt(0).getValue());
        assertEquals("4.5", 1, l_node.getChildAt(0).getChildCount());
        assertEquals("4.6", 3, l_node.getChildAt(1).getChildCount());
    }

    public void ztest_compare() throws Exception {
        String l_name = "tree2.txt";
        GB_SimpleNodeSet l_node = parseTree(l_name, GB_SimpleNodeOptions.TYPE.NAME_VALUE);
        l_name = "tree2b.txt";
        GB_SimpleNodeSet l_node2 = parseTree(l_name, GB_SimpleNodeOptions.TYPE.NAME_VALUE);
        GB_SimpleNode l_nodeCompare = GB_SimpleNodeTools.getCompare(l_node, l_node2);
        GB_SimpleNodeOptionsSet l_options = GB_SimpleNodeTools.newOptions();
        l_options.setUseRoot(false);
        l_options.setType(GB_SimpleNodeOptions.TYPE.NAME_VALUE);
        GB_SimpleNodeTools.toSystemOut("TREE2-VALUE_NAME", l_nodeCompare, l_options);
    }

    public void ztest_parseCsv() {
        test_parseCsv("", 0);
        test_parseCsv("AAA\t", 1);
        test_parseCsv("AAA\tBBB\t", 2);
    }
}
