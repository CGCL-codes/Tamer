package query;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import parser.FileParser;
import query.parsetree.ParsedQuery;
import storage.MemoryStorage;
import storage.RemoteStore;
import storage.Store;
import api.NetworkTest;

public class Test {

    void bla(File f, Store store) {
        FileParser p = new FileParser();
        long starttime = System.currentTimeMillis();
        p.getStatementSetFromString(f, store);
    }

    public static void main(String[] args) throws Exception {
        Test t = new Test();
        RemoteStore rs = new RemoteStore(InetAddress.getByName("0.0.0.0"), 4902);
        rs.init("");
        long startTime = System.currentTimeMillis();
        t.bla(new File("/home/marko/workspace/OWLIMTEST/ontology/example.rdfs"), rs);
        Thread.sleep(5000);
        Parser p = new Parser();
        ParsedQuery query2 = p.parse("SELECT ?X, ?Y WHERE {?Y <http://example.org/owlim#hasGender> <http://example.org/owlim#Male> . ?Y <http://example.org/owlim#hasGender> ?X . }");
        long starttime = System.currentTimeMillis();
        QueryExecutioner qe = new QueryExecutioner(rs);
        System.out.println(query2);
        Map<String, ArrayList<String>> result = qe.eval(qe.optimize(query2));
        System.out.println(System.currentTimeMillis() - starttime);
        System.out.println(result);
        rs.close();
    }
}
