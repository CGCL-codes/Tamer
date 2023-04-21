package net.sf.antcontrib.cpptasks.compiler;

import junit.framework.TestCase;
import net.sf.antcontrib.cpptasks.CCTask;
import net.sf.antcontrib.cpptasks.ProcessorDef;
import net.sf.antcontrib.cpptasks.VersionInfo;

/**
 * Test for abstract compiler class
 * 
 * Override create to test concrete compiler implementions
 */
public class TestAbstractProcessor extends TestCase {

    private class DummyAbstractProcessor extends AbstractProcessor {

        public DummyAbstractProcessor() {
            super(new String[] { ".cpp", ".c" }, new String[] { ".hpp", ".h", ".inl" });
        }

        public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProvider, ProcessorDef specificProvider, net.sf.antcontrib.cpptasks.TargetDef targetPlatform, VersionInfo versionInfo) {
            return null;
        }

        public String getIdentifier() {
            return "dummy";
        }

        public Linker getLinker(LinkType type) {
            return null;
        }

        public String[] getOutputFileNames(String sourceFile, VersionInfo versionInfo) {
            return new String[0];
        }

        public String[][] getRuntimeLibraries(boolean debug, boolean multithreaded, boolean staticLink) {
            return new String[2][0];
        }
    }

    public TestAbstractProcessor(String name) {
        super(name);
    }

    protected AbstractProcessor create() {
        return new DummyAbstractProcessor();
    }

    public void testBid() {
        AbstractProcessor compiler = create();
        int bid = compiler.bid("c:/foo\\bar\\hello.c");
        assertEquals(100, bid);
        bid = compiler.bid("c:/foo\\bar/hello.c");
        assertEquals(100, bid);
        bid = compiler.bid("c:/foo\\bar\\hello.h");
        assertEquals(1, bid);
        bid = compiler.bid("c:/foo\\bar/hello.h");
        assertEquals(1, bid);
        bid = compiler.bid("c:/foo\\bar/hello.pas");
        assertEquals(0, bid);
        bid = compiler.bid("c:/foo\\bar/hello.java");
        assertEquals(0, bid);
    }

    public void testGetIdentfier() {
        AbstractProcessor compiler = create();
        String id = compiler.getIdentifier();
        assertEquals("dummy", id);
    }
}
