package com.ontotext.ordi.sar.test;

import gate.FeatureMap;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentImpl;
import gate.util.InvalidOffsetException;
import gate.util.SimpleFeatureMapImpl;
import java.net.URL;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.pipeline.PipelineCreationException;
import org.apache.log4j.Logger;
import com.ontotext.ordi.sar.SarPipeline;
import com.ontotext.platform.rdf.RdfQueryClient;
import com.ontotext.platform.rdf.RdfStoreClient;

public class PipelineTests extends TestCase {

    static final String DOC_CONTENT = "The little brown fox jumps all over the big red lake";

    static final String DOC_NAME = "the_test_document_name";

    Logger log = Logger.getLogger(PipelineTests.class);

    static DocumentImpl createTestDocument(String name) {
        DocumentContentImpl content = new DocumentContentImpl(DOC_CONTENT);
        DocumentImpl doc = new DocumentImpl();
        doc.setContent(content);
        doc.getFeatures().put("title", name);
        FeatureMap fm = new SimpleFeatureMapImpl();
        fm.put("test_feature", "val_of_feature1");
        fm.put("test_uri_feature", "http://ontotext.com");
        try {
            doc.getAnnotations().add(0l, 3l, "test_annot", fm);
        } catch (InvalidOffsetException e) {
        }
        return doc;
    }

    public void testLoad() throws Exception {
        SarPipeline sar = SarPipeline.create(RdfStoreClient.connect(), RdfQueryClient.connect());
        sar.start();
        DocumentImpl src = createTestDocument(DOC_NAME);
        sar.store(src);
        DocumentImpl doc = new DocumentImpl();
        doc.getFeatures().put("title", DOC_NAME);
        sar.load(doc);
        sar.finish();
        Assert.assertEquals(src.getFeatures().size(), doc.getFeatures().size());
        Assert.assertEquals(src.getContent().toString(), doc.getContent().toString());
        Assert.assertEquals(src.getAnnotations().size(), doc.getAnnotations().size());
    }

    public void testStore() throws Exception {
        SarPipeline sar = SarPipeline.create(RdfStoreClient.connect(), RdfQueryClient.connect());
        sar.start();
        for (int c = 0; c < 500; c++) {
            DocumentImpl doc = createTestDocument(DOC_NAME + String.valueOf(c));
            sar.store(doc);
        }
        sar.finish();
    }

    public void testCreate() throws Exception {
        SarPipeline sar = SarPipeline.create(RdfStoreClient.connect(), RdfQueryClient.connect());
        log.info(sar);
    }

    public void testCreateNoBranchErr() {
        URL confURL = SarPipeline.class.getClassLoader().getResource("no_store_branch.xml");
        try {
            SarPipeline.create(RdfStoreClient.connect(), RdfQueryClient.connect(), confURL);
        } catch (PipelineCreationException e) {
            return;
        }
        Assert.fail("A PipelineCreationException should have been thrown!");
    }
}
