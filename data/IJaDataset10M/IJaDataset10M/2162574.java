package test.ordi.ota;

import junit.framework.TestCase;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import com.ontotext.ordi.Factory;
import com.ontotext.ordi.iterator.CloseableIterator;
import com.ontotext.ordi.tripleset.TConnection;
import com.ontotext.ordi.tripleset.TSource;
import com.ontotext.ordi.tripleset.TStatement;

public class ContextTest extends TestCase {

    private TSource source;

    protected void setUp() throws Exception {
        if (source == null) {
            source = Factory.createDefaultTSource();
        }
        source.getConnection().removeStatement(null, null, null, null);
    }

    protected void tearDown() throws Exception {
        source.shutdown();
    }

    public void test() throws Exception {
        TConnection con = source.getConnection();
        URI uri = new URIImpl("urn:test");
        con.addStatement(uri, uri, uri, uri);
        CloseableIterator<? extends TStatement> iter = con.search(null, null, null, null, null);
        while (iter.hasNext()) {
            TStatement st = iter.next();
            System.out.println(st.toString());
        }
    }
}
