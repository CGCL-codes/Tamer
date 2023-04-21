package org.rubypeople.rdt.refactoring.tests.core.inlinemethod;

import org.jruby.ast.MethodDefNode;
import org.rubypeople.rdt.refactoring.core.inlinemethod.MethodFinder;
import org.rubypeople.rdt.refactoring.tests.core.MultipleDocumentsInOneProvider;

public class TC_MethodFinder extends FinderTestsBase {

    public void testFind() {
        MethodDefNode node = findDefinition(61, "test1");
        assertEquals("test", node.getName());
        assertNotNull(node.getNameNode());
        assertNotNull(node.getArgsNode());
        assertNotNull(node.getBodyNode());
    }

    public void testFind2() {
        MethodDefNode node = findDefinition(83, "test2");
        assertEquals("test", node.getName());
        assertNotNull(node.getNameNode());
        assertNotNull(node.getArgsNode());
        assertNotNull(node.getBodyNode());
    }

    public void testFind3() {
        MethodDefNode node = findDefinition(165, "test3");
        assertEquals("test", node.getName());
        assertNotNull(node.getNameNode());
        assertNotNull(node.getArgsNode());
        assertNotNull(node.getBodyNode());
    }

    public void testFindInherited() {
        MethodDefNode node = findDefinition(155, "test4");
        assertEquals("method_to_find", node.getName());
        assertNotNull(node.getNameNode());
        assertNotNull(node.getArgsNode());
        assertNull(node.getBodyNode());
    }

    public void testFindSimplyInherited() {
        MethodDefNode node = findDefinition(100, "test5");
        assertEquals("method_to_find", node.getName());
        assertNotNull(node.getNameNode());
        assertNotNull(node.getArgsNode());
        assertNull(node.getBodyNode());
    }

    public void testFindUnknownClass() {
        doc.setActive("test3");
        assertNull(new MethodFinder().find("Tes", "", doc));
    }

    public void testFindUnknownMethod() {
        doc.setActive("test3");
        assertNull(new MethodFinder().find("Test", "", doc));
    }

    @Override
    protected void setUp() throws Exception {
        doc = new MultipleDocumentsInOneProvider("TC_MethodFinder.source.rb", this.getClass());
    }
}
