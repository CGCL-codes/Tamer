package org.cyberaide.gridshell.commands.workflow.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.cyberaide.gridshell.commands.GSSystem;
import org.cyberaide.gridshell.commands.object.GSBasicObject;
import org.cyberaide.gridshell.commands.util.ObjectLoader;
import org.cyberaide.gridshell.commands.workflow.Asynchronous;
import org.cyberaide.gridshell.commands.workflow.CSP;
import org.cyberaide.gridshell.commands.workflow.CSPWorkflow;
import org.junit.Before;
import org.junit.Test;

public class CSPTest {

    @Before
    public void setUp() throws Exception {
        GSSystem.setupClient(new File("").getAbsolutePath(), null, null);
    }

    @Test
    public void testNormalExecute() {
        String name = "csptestnormalsyntax";
        String syntax = "(test ; test2) (test3 ; test4) (test5 ; test6)";
        String args = "-name " + name + " -syntax " + syntax;
        CSP csp = new CSP(args);
        csp.execute();
        GSBasicObject wf = null;
        wf = (GSBasicObject) ObjectLoader.loadObject(name, null);
        syntax = syntax.replaceAll("\\(", "");
        syntax = syntax.replaceAll("\\s?\\)\\s", "|");
        syntax = syntax.replaceAll("\\)", "");
        assertEquals(wf.getName(), name);
        assertEquals(wf.getAttrib(CSPWorkflow.SYNTAX), syntax);
    }

    @Test
    public void testPolishExecute() {
        String name = "csptestpolishsyntax";
        String syntax = "(; test test2) (; test3 test4) (; test5 test6) (; test7 test8)";
        String args = "-name " + name + " -polish " + syntax;
        CSP csp = new CSP(args);
        csp.execute();
        GSBasicObject wf = null;
        wf = (GSBasicObject) ObjectLoader.loadObject(name, null);
        syntax = syntax.replaceAll("\\(", "");
        syntax = syntax.replaceAll("\\s?\\)\\s", "|");
        syntax = syntax.replaceAll("\\)", "");
        assertEquals(wf.getName(), name);
        assertEquals(wf.getAttrib(CSPWorkflow.SYNTAX), syntax);
    }
}
