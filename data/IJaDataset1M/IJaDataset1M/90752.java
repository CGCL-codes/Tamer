package uk.ac.cisban.saint.querying.sbo;

import org.junit.Test;
import uk.ac.cisban.saint.client.structure.ConstantsConverter;
import uk.ac.ebi.miriam.lib.MiriamLink;
import static org.junit.Assert.*;

/**
 * This file is part of the Saint package. Saint is covered under the GNU Lesser General Public License (LGPL).
 * <p/>
 * Copyright (C) 2009, Allyson Lister, Morgan Taschuk and their employers
 * <p/>
 * Modifications to the initial code base are copyright of their respective authors, or their employers as appropriate.
 * Authorship of the modifications may be determined from the Subversion history.
 * <p/>
 * To view the full licensing information for this software and ALL other packages contained in this distribution,
 * please see LICENSE.txt
 */
public class MiriamTest {

    @Test
    public void basicMiriamTest() {
        MiriamLink link = new MiriamLink();
        assertEquals("urn:miriam:obo.chebi", link.getDataTypeURI("ChEBI"));
        assertEquals(241, link.getDataTypesName().length);
        assertNotNull(link.getLocations("Gene Ontology", "GO:0000001"));
        assertEquals(4, link.getLocations("Gene Ontology", "GO:0000001").length);
        assertNull(link.getLocations("Gene Ontology", ConstantsConverter.getReplaceString()));
        assertNotNull(link.getLocationsWithToken("Gene Ontology", ConstantsConverter.getReplaceString()));
        assertEquals("http://www.ebi.ac.uk/QuickGO/GTerm?id=GO%3A0008150", link.getLocations("Gene Ontology", "GO:0008150")[0]);
    }
}
