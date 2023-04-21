package com.ontotext.sar.test;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.corpora.DocumentImpl;
import java.io.File;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import com.ontotext.ordi.sar.exception.SARSystemException;
import com.ontotext.ordi.sar.server.LocalRepository;
import com.ontotext.ordi.sar.server.LocalRepositoryConfig;
import com.ontotext.ordi.sar.server.ServerConfig;
import com.ontotext.ordi.sar.server.handlers.NamingUtility;
import com.ontotext.ordi.sar.server.handlers.gate.GateEntityHandler;
import com.ontotext.ordi.sar.utils.ContextUtils;

public class LocalRepositoryTests extends SARTestCase {

    LocalRepository repo;

    public void testDocumentPersistence() throws Exception {
        repo.store(gateDoc, null);
        LocalRepository.commit();
        Document loaded = new DocumentImpl();
        Map<Object, Object> params = ContextUtils.create();
        String uri = NamingUtility.documentUri(gateDoc);
        params.put(GateEntityHandler.PARAM_DOCUMENT_URI, uri);
        repo.load(loaded, params);
        assertEquals(gateDoc.getLRPersistenceId(), loaded.getLRPersistenceId());
        assertEquals(gateDoc.getName(), loaded.getName());
        checkAnnSet(gateDoc.getAnnotations(), loaded.getAnnotations());
        Set<?> names1 = gateDoc.getAnnotationSetNames();
        Set<?> names2 = loaded.getAnnotationSetNames();
        if (names1 != null && names2 != null) {
            assertEquals(names1.size(), names2.size());
            for (Object name : names1) {
                assertTrue(names2.contains(name));
                AnnotationSet set1 = gateDoc.getAnnotations(String.valueOf(name));
                AnnotationSet set2 = loaded.getAnnotations(String.valueOf(name));
                checkAnnSet(set1, set2);
            }
        }
    }

    private static void checkAnnSet(AnnotationSet expected, AnnotationSet actual) {
        if (expected == null && actual == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());
        for (Annotation expectedAnn : expected) {
            if (!actual.contains(expected)) {
                fail(String.format("Annotation set [%s] is missing in the loaded doc", expectedAnn.toString()));
            }
        }
    }

    public void testCreateBadConfig() throws Exception {
        ServerConfig config = LocalRepositoryConfig.fromResource("sar-config-bad1.xml");
        LocalRepository repo = new LocalRepository(config);
        try {
            repo.store(null, null);
        } catch (SARSystemException e) {
            return;
        }
        Assert.fail("this should fail with SARSystemException");
    }

    public void testCreateNoArgs() throws Exception {
        new LocalRepository();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File cdir = new File(".");
        String dir = cdir.getCanonicalPath();
        String repoDir = dir.concat("/src/test/resources/test_ds");
        init(repoDir, SAMPLE_DOCUMENT_ID);
        repo = new LocalRepository();
    }

    @Override
    protected void tearDown() throws Exception {
        gateDoc = null;
        gateStore = null;
        LocalRepository.releaseOrdi();
        repo = null;
        super.tearDown();
    }
}
