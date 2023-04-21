package com.doculibre.intelligid.test.conversion;

import com.doculibre.intelligid.extraction.DocumentTexte;
import junit.framework.TestCase;

public class TestDocumentTexte extends TestCase {

    private String path = "C:/dev/workspace/fgd/gen/plan.txt";

    public void testAvecTampon() {
        int nbLignesTampon = 3;
        DocumentTexte documentTexte = new DocumentTexte(path, nbLignesTampon);
        int index = 2;
        String ligneCourante = documentTexte.getLigne(index);
        while (ligneCourante != null) {
            System.err.println(ligneCourante);
            ligneCourante = documentTexte.getLigne(++index);
        }
        System.err.println(documentTexte);
    }
}
