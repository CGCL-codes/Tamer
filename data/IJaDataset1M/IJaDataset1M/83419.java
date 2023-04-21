package com.ontotext.ordi.sar.test;

import gate.AnnotationSet;
import gate.Document;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import com.ontotext.ordi.iterator.CloseableIterator;
import com.ontotext.ordi.sar.Sar;
import com.ontotext.ordi.sar.core.CoreRepositoryConnection;
import com.ontotext.ordi.sar.exception.SARException;
import com.ontotext.ordi.sar.gate.GateRepositoryClient;
import com.ontotext.ordi.sar.gate.GateRepositoryConnection;
import com.ontotext.ordi.sar.server.LocalRepository;
import com.ontotext.ordi.sar.server.LocalRepositoryConfig;
import com.ontotext.sar.test.GateDocumentHelper;
import com.ontotext.sar.test.SARTestCase;

public class IssuesFromNiraj extends SARTestCase {

    GateRepositoryClient client;

    GateRepositoryConnection repository;

    /**
	 * SAM-75
	 */
    public void test03() throws Exception {
        LocalRepositoryConfig repoConfig = LocalRepositoryConfig.getDefault();
        repoConfig.putSetting(LocalRepositoryConfig.SETTING_REPOSITORY_DIR, "D:/Dev/Projz/ordi/trunk/sar/sar-test/src/test/resources/niraj_issue_03");
        LocalRepository repo = new LocalRepository(repoConfig);
        String sparql = "SELECT * " + " WHERE {" + " GRAPH ?graph_uri {" + "?mention0 <http://ordi.ontotext.com/sar#belongsTo> ?doc.\n" + "?instance0 <http://ordi.ontotext.com/sar#hasMention> ?mention0.\n" + "\n" + "}\n" + "}\n" + "LIMIT 352\n" + "OFFSET 0";
        CloseableIterator<? extends BindingSet> result = repo.query(sparql);
        dump(result);
        LocalRepository.releaseOrdi();
    }

    private int ask(CoreRepositoryConnection conn) throws SARException {
        String path = Sar.getHomeDir() + RES_DIR + "problematic_query2.txt";
        String queryStr = getContents(new File(path));
        CloseableIterator<? extends BindingSet> result = this.repository.query(queryStr);
        int count = dump(result);
        return count;
    }

    private void fill(GateDocumentHelper hlp, CoreRepositoryConnection conn) throws Exception {
        int annot_count = 0;
        for (int c = 0; c < 40; c++) {
            long start = System.currentTimeMillis();
            Document doc = hlp.generate(5, 10);
            hlp.addDocument(doc);
            hlp.annotate();
            conn.store(doc, null);
            System.out.println("-------------------");
            long end = System.currentTimeMillis();
            System.out.println(String.format("%d completed in %d ms", c, end - start));
            int count = countAnnots(doc);
            annot_count += count;
            System.out.print(String.format("added %d annotation(s), total %d in repository...", count, annot_count));
            doc.cleanup();
            System.out.println("commiting...");
            start = System.currentTimeMillis();
            conn.commit();
            end = System.currentTimeMillis();
            System.out.println(String.format("...completed in %d ms", end - start));
        }
    }

    private int countAnnots(Document doc) {
        int count = 0;
        AnnotationSet set = doc.getAnnotations();
        count += set.size();
        Set names = doc.getAnnotationSetNames();
        if (names != null) {
            for (Object name : names) {
                set = doc.getAnnotations(String.valueOf(name));
                count += set.size();
            }
        }
        return count;
    }

    private int dump(CloseableIterator<? extends BindingSet> result) {
        int count = 0;
        log("\ndump ------------");
        while (result.hasNext()) {
            BindingSet solution = result.next();
            for (Iterator<Binding> i = solution.iterator(); i.hasNext(); ) {
                Binding binding = i.next();
                log(binding.toString());
            }
            log("-----------------");
            count++;
        }
        log("-----------------");
        log(String.format("total %d entries\n", count));
        return count;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.client = new GateRepositoryClient();
        this.repository = client.getRepositoryConnection();
        super.init();
        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put(GateRepositoryConnection.PARAM_LAZY_LOAD, false);
        context.put(GateRepositoryConnection.PARAM_DOCUMENT_FEATURES_IN_BAG, true);
        context.put(GateRepositoryConnection.PARAM_ANNOTATION_FEATURES_IN_BAG, true);
    }

    @Override
    protected void tearDown() throws Exception {
        this.repository.close();
        this.client.shutdown();
        super.tearDown();
    }
}
