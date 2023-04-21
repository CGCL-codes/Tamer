package org.phenoscape.tto;

import java.util.Collection;
import org.obo.datamodel.OBOClass;
import org.phenoscape.tto.catalog.CatalogReader;

public class HighCounter {

    private static int counter;

    static void init(Collection<OBOClass> termCollection) {
        int maxCounter = CatalogReader.HIGHBASE;
        for (OBOClass c : termCollection) {
            if (c.getID().startsWith(Builder.ONTOLOGYPREFIX)) {
                String id = c.getID().substring(Builder.ONTOLOGYPREFIX.length());
                try {
                    int index = Integer.parseInt(id);
                    if (index > maxCounter) maxCounter = index;
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Error with number parsed out of existing ID: " + c.getID());
                }
            }
            counter = maxCounter + 1;
        }
    }

    static String newID() {
        return Builder.ONTOLOGYPREFIX + Integer.toString(counter++);
    }
}
