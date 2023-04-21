package net.sf.iwant.core;

import java.io.File;
import java.io.FileWriter;
import java.security.Permission;

public class IwantTest extends WorkspaceBuilderTestBase {

    private SecurityManager origSecman;

    private String iwantLibs;

    @Override
    public void setUp() {
        super.setUp();
        origSecman = System.getSecurityManager();
        System.setSecurityManager(new ExitCatcher());
        iwantLibs = testarea() + "/.internal/iwant/iwant-bootstrapper/phase2/iw/cached/.internal/bin";
    }

    private static class ExitCalledException extends SecurityException {

        private final int status;

        public ExitCalledException(int status) {
            this.status = status;
        }

        public int status() {
            return status;
        }
    }

    private static class ExitCatcher extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkExit(int status) {
            throw new ExitCalledException(status);
        }
    }

    @Override
    public void tearDown() {
        System.setSecurityManager(origSecman);
        super.tearDown();
    }

    public void testMissingAsSomebodyIsAnInternalFailure() throws Exception {
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "", iwantLibs });
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Internal error: missing " + wsRoot() + "/as-x-developer/i-have", e.getMessage());
        }
        assertEquals("", out());
        assertEquals("perr:Internal error: missing " + wsRoot() + "/as-x-developer/i-have\n", err());
    }

    public void testMissingIHaveIsAnInternalFailure() throws Exception {
        directoryExists("as-x-developer");
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "", iwantLibs });
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Internal error: missing " + wsRoot() + "/as-x-developer/i-have", e.getMessage());
        }
        assertEquals("", out());
        assertEquals("perr:Internal error: missing " + wsRoot() + "/as-x-developer/i-have\n", err());
    }

    public void testMissingIHaveWsInfoGetsCreatedThenBuildAborts() throws Exception {
        directoryExists("as-x-developer/i-have");
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "", iwantLibs });
            fail();
        } catch (ExitCalledException e) {
            assertEquals(1, e.status());
        }
        assertEquals("", out());
        assertEquals("perr:I created " + wsRoot() + "/as-x-developer/i-have/ws-info.conf for you." + " Please edit it and rerun me.\n", err());
        assertEquals("# paths are relative to this file's directory\n" + "WSNAME=example\n" + "WSROOT=../..\n" + "WSDEF_SRC=wsdef\n" + "WSDEF_CLASS=com.example.wsdef.Workspace\n", contentOf(wsRoot() + "/as-x-developer/i-have/ws-info.conf"));
    }

    public void testMissingWsDefJavaGetsCreatedThenBuildAborts() throws Exception {
        directoryExists("as-x-developer/i-have");
        file("as-x-developer/i-have/ws-info.conf").withContent();
        line("WSNAME=test");
        line("WSROOT=../..");
        line("WSDEF_SRC=wsdef");
        line("WSDEF_CLASS=net.sf.iwant.test.wsdef.TestWorkspace");
        exists();
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "", iwantLibs });
            fail();
        } catch (ExitCalledException e) {
            assertEquals(1, e.status());
        }
        assertEquals("", out());
        assertEquals("perr:I created " + wsRoot() + "/as-x-developer/i-have/wsdef/net/sf/iwant/test/wsdef/TestWorkspace.java" + " for you. Please edit it and rerun me.\n", err());
        String content = contentOf(wsRoot() + "/as-x-developer/i-have/wsdef/net/sf/iwant/test/wsdef/TestWorkspace.java");
        assertTrue(content.contains("package net.sf.iwant.test.wsdef;"));
        assertTrue(content.contains("class TestWorkspace implements WorkspaceDefinition {"));
    }

    public void testEmptyWishCausesAHelpMessage() throws Exception {
        testMissingWsDefJavaGetsCreatedThenBuildAborts();
        startOfOutAndErrCapture();
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "", iwantLibs });
            fail();
        } catch (ExitCalledException e) {
            assertEquals(1, e.status());
        }
        assertEquals("", out());
        assertTrue("Shouldn't have been " + err(), err().startsWith("perr:Please tell what you want."));
    }

    public void testEmptyWishAlsoGeneratesWishScripts() throws Exception {
        testEmptyWishCausesAHelpMessage();
        assertEquals("#!/bin/bash\n" + "HERE=$(dirname \"$0\")\n" + "exec \"$HERE/../help.sh\" -D/target=list-of/targets\n", contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/list-of/targets"));
        assertEquals("#!/bin/bash\n" + "HERE=$(dirname \"$0\")\n" + "exec \"$HERE/../../help.sh\" -D/target=aConstant\n", contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/target/aConstant/as-path"));
        assertTrue(new File(wsRoot() + "/as-x-developer/with/bash/iwant/target/aConstant/as-path").canExecute());
    }

    public void testWishScriptsAreNotDeletedWhenTargetDetectionFails() throws Exception {
        testEmptyWishAlsoGeneratesWishScripts();
        file("as-x-developer/i-have/wsdef/net/sf/iwant/test/wsdef/TestWorkspace.java").withContent();
        line("package net.sf.iwant.iwant;\n");
        line("\n");
        line("import net.sf.iwant.core.ContainerPath;\n");
        line("import net.sf.iwant.core.Locations;\n");
        line("import net.sf.iwant.core.RootPath;\n");
        line("import net.sf.iwant.core.WorkspaceDefinition;\n");
        line("\n");
        line("public class IwantWorkspace implements WorkspaceDefinition {\n");
        line("\n");
        line("	@Override\n");
        line("	public ContainerPath wsRoot(Locations locations) {\n");
        line("		throw new IllegalStateException(\"broken\");\n");
        line("	}\n");
        line("}\n");
        exists();
        assertTrue(contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/list-of/targets").length() > 0);
        assertTrue(contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/target/aConstant/as-path").length() > 0);
    }

    public void testListOfTargetsWorksAndRenamesWishScriptAfterTargetRename() throws Exception {
        testEmptyWishAlsoGeneratesWishScripts();
        startOfOutAndErrCapture();
        sleep();
        String wsDefJava = wsRoot() + "/as-x-developer/i-have/wsdef/net/sf/iwant/test/wsdef/TestWorkspace.java";
        String wsDefJavaContent = contentOf(wsDefJava);
        new FileWriter(wsDefJava, false).append(wsDefJavaContent.replaceAll("aConstant", "constantWithModifiedName")).close();
        Iwant.main(new String[] { wsRoot() + "/as-x-developer", "list-of/targets", iwantLibs });
        assertEquals("pout:constantWithModifiedName\n" + "pout:eclipse-projects\n", out());
        assertEquals("", err());
        assertTrue(contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/target/constantWithModifiedName/as-path").length() > 0);
        assertFalse(new File(wsRoot() + "/as-x-developer/with/bash/iwant/target/aConstant/as-path").exists());
    }

    public void testListOfTargets() throws Exception {
        testMissingWsDefJavaGetsCreatedThenBuildAborts();
        startOfOutAndErrCapture();
        Iwant.main(new String[] { wsRoot() + "/as-x-developer", "list-of/targets", iwantLibs });
        assertEquals("pout:aConstant\n" + "pout:eclipse-projects\n", out());
        assertEquals("", err());
    }

    public void testRefreshingNonExistentTargetCausesErrorMessage() throws Exception {
        testMissingWsDefJavaGetsCreatedThenBuildAborts();
        startOfOutAndErrCapture();
        try {
            Iwant.main(new String[] { wsRoot() + "/as-x-developer", "nonExisting", iwantLibs });
            fail();
        } catch (ExitCalledException e) {
            assertEquals(1, e.status());
        }
        assertEquals("", out());
        assertEquals("perr:No such target: nonExisting\nperr:Refresh failed.\n", err());
    }

    public void testRefreshingOfAConstant() throws Exception {
        testMissingWsDefJavaGetsCreatedThenBuildAborts();
        startOfOutAndErrCapture();
        Iwant.main(new String[] { wsRoot() + "/as-x-developer", "aConstant", iwantLibs });
        assertEquals("pout:" + wsRoot() + "/as-x-developer/with/bash/iwant/cached/test/target/aConstant\n", out());
        assertEquals("", err());
        assertEquals("Constant generated content\n", contentOf(wsRoot() + "/as-x-developer/with/bash/iwant/cached/test/target/aConstant"));
    }
}
