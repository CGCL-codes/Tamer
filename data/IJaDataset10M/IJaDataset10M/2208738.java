package org.tripcom.kerneltests.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.Template;

/**
 * Sample test case. (Please note that there might be errors in this test case (because the
 * kernel didn't compile while I implemented it so I couldn't check.)
 *
 * @author Michael Lafite
 *
 */
public class SampleTest {

    /**
     * Create a space, then OUTs some triples and RDs them.
     *
     * @throws Exception
     *         if any error occurs.
     */
    public static void main(String[] args) throws Exception {
        SpaceURI rootspace = new SpaceURI("tsc://localhost:8080/testspace");
        TSClient tsclient = new TSClient("http", InetAddress.getByName(rootspace.getKernelAddressOfRootSpace().getHost()), rootspace.getKernelAddressOfRootSpace().getPort());
        tsclient.create(rootspace);
        Set<Statement> statements = new HashSet<Statement>();
        ValueFactory myFactory = new ValueFactoryImpl();
        for (int i = 0; i <= 10; i++) {
            URI mySubject = myFactory.createURI("http://example.com/book/book" + i);
            URI myPredicate = myFactory.createURI("http://purl.org/dc/elements/1.1/title");
            LiteralImpl myObject = (LiteralImpl) myFactory.createLiteral("Book " + i);
            statements.add(myFactory.createStatement(mySubject, myPredicate, myObject));
        }
        for (Statement s : statements) {
            tsclient.out(s, rootspace);
        }
        for (int i = 0; i < 10; i++) {
            String query = "CONSTRUCT { <http://example.com/book/book" + i + "> <http://purl.org/dc/elements/1.1/title> ?o } " + "WHERE { <http://example.com/book/book" + i + "> <http://purl.org/dc/elements/1.1/title> ?o}";
            Set<Statement> result = tsclient.rd(new Template(query), rootspace, 2000);
            assertNotNull(result);
            assertEquals(1, result.size());
            Statement s = result.iterator().next();
            assertNotNull(s);
            assertEquals("\"Book " + i + "\"", s.getObject().toString());
        }
    }
}
