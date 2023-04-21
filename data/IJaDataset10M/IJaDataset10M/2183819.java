package org.waffel.jscf.gpg;

import java.io.FileInputStream;
import java.util.Properties;
import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;
import junit.framework.TestCase;

/**
 * @author waffel
 *  
 */
public final class GPGEncryptTest extends TestCase {

    public void testSmallFileExcryption() throws Exception {
        JSCFConnection pgpCon = null;
        JSCFResultSet res = null;
        JSCFDriverManager.registerJSCFDriver(new GPGDriver());
        Properties props = new Properties();
        props.put("RECIPIENTS", "testid");
        pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg:testid:test", props);
        JSCFStatement stmt = pgpCon.createStatement();
        FileInputStream fin = new FileInputStream(this.getClass().getClassLoader().getResource("org/waffel/jscf/gpg/build.xml").getFile());
        res = stmt.executeEncrypt(fin);
        assertEquals(false, res.isError());
        assertEquals(0, ((GPGResultSet) res).getReturnValue());
        assertEquals(true, (res.getResultStream() != null));
    }

    public void testBigFileEncryption() throws Exception {
        JSCFConnection pgpCon = null;
        JSCFResultSet res = null;
        JSCFDriverManager.registerJSCFDriver(new GPGDriver());
        Properties props = new Properties();
        props.put("RECIPIENTS", "testid");
        pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg:testid:test", props);
        JSCFStatement stmt = pgpCon.createStatement();
        FileInputStream fin = new FileInputStream(this.getClass().getClassLoader().getResource("org/waffel/jscf/gpg/jscf-0.2.zip").getFile());
        res = stmt.executeEncrypt(fin);
        assertEquals(false, res.isError());
        assertEquals(0, ((GPGResultSet) res).getReturnValue());
        assertEquals(true, (res.getResultStream() != null));
        JSCFResultSet resDecrypt = stmt.executeDecrypt(res.getResultStream(), "test");
        assertEquals(0, ((GPGResultSet) resDecrypt).getReturnValue());
        assertEquals(true, (resDecrypt.getResultStream() != null));
    }
}
