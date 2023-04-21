package fedora.server.security.servletfilters.xmluserfile;

import static org.junit.Assert.fail;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.junit.Test;

public class TestFedoraUsers {

    @Test
    public void testGetInstance() throws Exception {
        FedoraUsers fu = FedoraUsers.getInstance();
        org.junit.Assert.assertNotNull(fu);
        Writer outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        fu.write(outputWriter);
        outputWriter.close();
    }
}
