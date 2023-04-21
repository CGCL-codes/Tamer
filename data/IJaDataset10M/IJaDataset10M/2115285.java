package org.torweg.pulse.util.streamscanner;

import java.io.FileInputStream;
import java.io.InputStream;
import junit.framework.TestCase;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.util.io.FastByteArrayOutputStream;

/**
 * @author Thomas Weber
 * @version $Revision: 2027 $
 */
public class TestOutputStreamFilterChain extends TestCase {

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        new TestingEnvironment();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for
	 * {@link org.torweg.pulse.util.streamscanner.OutputStreamScannerChain#OutputStreamFilterChain(java.io.OutputStream, java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 *             on errors
	 */
    public final void testOutputStreamFilterChain() throws Exception {
        InputStream in = new FileInputStream("conf/pulse.xml");
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        OutputStreamScannerChain filterChain = new OutputStreamScannerChain(out, "image/jpeg", User.getSuperUser(Lifecycle.getHibernateDataSource()));
        byte[] buffer = new byte[1024];
        int available = in.read(buffer);
        while (available > 0) {
            filterChain.write(buffer, 0, available);
            available = in.read(buffer);
        }
        out.close();
        in.close();
    }
}
