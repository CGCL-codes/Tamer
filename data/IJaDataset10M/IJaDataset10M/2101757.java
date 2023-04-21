package de.unikoblenz.isweb.xcosima.examples;

import java.util.List;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.n3.N3Writer;
import de.unikoblenz.isweb.xcosima.adapter.SesameAdapter;

/**
 * @author Thomas Franz, http://isweb.uni-koblenz.de
 *
 */
public class ConnectionTest {

    public static void main(String[] args) throws RepositoryException, de.unikoblenz.isweb.xcosima.storage.RepositoryException, RDFHandlerException {
        SesameAdapter r;
        Repository rep;
        r = new SesameAdapter();
        rep = new HTTPRepository("http://warsteiner.uni-koblenz.de:9011/openrdf-sesame/repositories/httpcolonslashslashwww.x-media-project.comslashrepositoriesslashshared_kb");
        rep.initialize();
        r.setRep(rep);
        rep.getConnection().export(new N3Writer(System.out));
        List<Statement> statements = r.getStatements(new URIImpl("http://www.x-media-project.org/fiat/segments#P"), null, null, true);
        System.out.println("Statements about Segment P:\n" + statements);
    }
}
