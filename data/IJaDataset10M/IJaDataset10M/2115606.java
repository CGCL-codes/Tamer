package com.tibell.lucene.swestemmer.prepare.idxbld;

import static org.junit.Assert.assertTrue;
import java.util.Set;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import com.tibell.lucene.swestemmer.engine.LemmaEngine;

/**
 * @author rasse
 * 
 */
public class TestBuildAnalyzerIndex {

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpClass() throws Exception {
        createIndex();
    }

    public static void createIndex() throws Exception {
        long startTM = System.currentTimeMillis();
        BuildAnalyzerIndex bai = new BuildAnalyzerIndex();
        bai.createAllIndexes("data/dsso-1.39.utf8");
        System.out.println("setUp" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testContent() {
        Set<String> lemmas = null;
        long startTM = System.currentTimeMillis();
        String token = "korven";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 1);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
        startTM = System.currentTimeMillis();
        token = "skorna";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 1);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
        startTM = System.currentTimeMillis();
        token = "agnostiska";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 1);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
        startTM = System.currentTimeMillis();
        token = "bistås";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 1);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
        startTM = System.currentTimeMillis();
        token = "sås";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 2);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
        startTM = System.currentTimeMillis();
        token = "hästarnas";
        lemmas = LemmaEngine.GetInstance().getLemma(token);
        printList(lemmas, token);
        assertTrue(lemmas.size() == 1);
        System.out.println("testContent" + " process time " + (System.currentTimeMillis() - startTM) + " msec");
    }

    private void printList(Set<String> list, String token) {
        System.out.print("lemma: " + token + " --> ");
        for (String str : list) System.out.print(" " + str);
        System.out.println(".");
    }
}
