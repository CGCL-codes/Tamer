package com.rk.jarjuggler.service;

import com.rk.jarjuggler.model.DirNode;
import com.rk.jarjuggler.model.LibNode;
import junit.framework.TestCase;

public class WorkItemComparatorTest extends TestCase {

    WorkItemComparator workItemComparator = new WorkItemComparator();

    public void testCompare() throws Exception {
        DirNode root = new DirNode();
        DirNode asm = new DirNode(root, "ASM", "http://localhost/asm");
        DirNode asm2 = new DirNode(asm, "ASM2", "http://localhost/asm/asm");
        DirNode asm14 = new DirNode(asm2, "1.4", "http://localhost/asm/asm/1.4");
        new LibNode(asm14, "ASM 1.4", "http://localhost/asm/asm/1.4/asm-1.4.jar");
        new DirNode(root, "logkit", "http://localhost/logkit");
        assertEquals(0, workItemComparator.compare(new WorkItem(root, "", ""), new WorkItem(root, "", "")));
        assertEquals(1, workItemComparator.compare(new WorkItem(root, "", ""), new WorkItem(asm, "", "")));
        assertEquals(1, workItemComparator.compare(new WorkItem(asm, "", ""), new WorkItem(asm2, "", "")));
        assertEquals(2, workItemComparator.compare(new WorkItem(asm, "", ""), new WorkItem(asm14, "", "")));
        assertEquals(-2, workItemComparator.compare(new WorkItem(asm14, "", ""), new WorkItem(asm, "", "")));
        assertEquals(0, workItemComparator.compare(new WorkItem(asm14, "", ""), new WorkItem(asm14, "", "")));
    }
}
