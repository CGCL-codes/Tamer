package com.controltier.ctl.types;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.controltier.ctl.common.CmdModule;
import com.controltier.ctl.tools.CtlTest;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Feb 10, 2006
 * Time: 10:49:16 AM
 */
public class TestFilenameTokenizer extends CtlTest {

    public TestFilenameTokenizer(final String name) {
        super(name);
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestFilenameTokenizer.class);
    }

    public void testValidate() {
        final FilenameTokenizer tokenizer = new FilenameTokenizer();
        tokenizer.addRegex(tokenizer.createRegex());
        try {
            tokenizer.validate();
            fail("tokenizer validate should have failed.");
        } catch (BuildException e) {
        }
    }

    public void testExec() {
        final CmdModule mod = getFrameworkInstance().getModuleLookup().getCmdModule("Managed-Entity");
        final FilenameTokenizer tokenizer = new FilenameTokenizer();
        tokenizer.setProject(new Project());
        tokenizer.addRegex(tokenizer.createRegex());
        tokenizer.getRegex().setPattern("(.*)-(.*).(xml)");
        FilenameTokenizer.Token token = tokenizer.getRegex().createToken();
        token.setName("filename");
        token.setMatchgroup(0);
        token = tokenizer.getRegex().createToken();
        token.setName("verb");
        token.setMatchgroup(1);
        token = tokenizer.getRegex().createToken();
        token.setName("noun");
        token.setMatchgroup(2);
        token = tokenizer.getRegex().createToken();
        token.setName("extension");
        token.setMatchgroup(3);
        final FileSet fs = tokenizer.createFileset();
        fs.setDir(mod.getBaseDir());
        fs.setIncludes("**/*.xml");
        final FunctionMapperInput input = tokenizer.exec();
        assertNotNull(input);
        for (Iterator i = input.getKeyedParamValues().iterator(); i.hasNext(); ) {
            final KeyedParamValues first = (KeyedParamValues) i.next();
            final Collection pairs = first.getParamValuePairs();
            for (Iterator j = pairs.iterator(); j.hasNext(); ) {
                final ParamValuePair pvp = (ParamValuePair) j.next();
                final String param = pvp.getParam();
                final String value = pvp.getValue();
                if ("extension".equals(param)) {
                    assertEquals("value did not match", value, "xml");
                }
            }
        }
    }

    public void testExe2() {
        final CmdModule mod = getFrameworkInstance().getModuleLookup().getCmdModule("Managed-Entity");
        final FilenameTokenizer tokenizer = new FilenameTokenizer();
        tokenizer.setProject(new Project());
        tokenizer.addRegex(tokenizer.createRegex());
        tokenizer.getRegex().setPattern("(.*)-(.*).(xml)");
        FilenameTokenizer.Token token = tokenizer.getRegex().createToken();
        token.setName("filename");
        token.setMatchgroup(0);
        token = tokenizer.getRegex().createToken();
        token.setName("verb");
        token.setMatchgroup(1);
        token = tokenizer.getRegex().createToken();
        token.setName("noun");
        token.setMatchgroup(2);
        token = tokenizer.getRegex().createToken();
        token.setName("extension");
        token.setMatchgroup(3);
        token = tokenizer.getRegex().createToken();
        token.setName("bogus");
        token.setMatchgroup(4);
        final FileSet fs = tokenizer.createFileset();
        fs.setDir(mod.getBaseDir());
        fs.setIncludes("**/*.xml");
        try {
            final FunctionMapperInput input = tokenizer.exec();
            fail("tokenizer should have thrown an exception since tokens do not match regex");
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
