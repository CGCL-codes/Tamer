package uk.ac.cisban.saint.querying.string;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import uk.ac.cisban.saint.client.structure.InteractorDetail;
import uk.ac.cisban.saint.client.structure.records.InteractorRecord;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * This file is part of the Saint package.
 * Saint is covered under the GNU Lesser General Public License (LGPL).
 * <p/>
 * Copyright (C) 2009, Allyson Lister, Morgan Taschuk and their employers
 * <p/>
 * Modifications to the initial code base are copyright of their respective authors, or their employers as appropriate.
 * Authorship of the modifications may be determined from the Subversion history.
 * <p/>
 * To view the full licensing information for this software and ALL other packages contained in this distribution,
 * please see LICENSE.txt
 * 
 */
public class StringServiceTest {

    @Test
    public void p53SearchTest() throws JAXBException, XMLStreamException, IOException {
        StringQuerier querier = new StringQuerier();
        ArrayList<String> responses = querier.runListQuery(querier.getSearchForIdsPrefix() + "p53" + querier.getSpeciesPhrase() + "9606");
        assertNotNull(responses);
        assertEquals(61, responses.size());
        for (String response : responses) {
            InteractorDetail iDetail = querier.getDescription(response, false);
        }
    }

    @Test
    public void cdc13SearchTest() throws JAXBException, XMLStreamException, IOException {
        StringQuerier querier = new StringQuerier();
        ArrayList<String> responses = querier.runListQuery(querier.getSearchForIdsPrefix() + "cdc13" + querier.getSpeciesPhrase() + "4932");
        assertNotNull(responses);
        assertEquals(7, responses.size());
        for (String response : responses) {
            InteractorDetail iDetail = querier.getDescription(response, false);
        }
    }

    @Test
    public void P04637SearchTest() throws JAXBException, XMLStreamException, IOException {
        StringQuerier querier = new StringQuerier();
        ArrayList<String> responses = querier.runListQuery(querier.getSearchForIdsPrefix() + "P04637");
        assertNotNull(responses);
        assertEquals(1, responses.size());
        for (String response : responses) {
            InteractorDetail iDetail = querier.getDescription(response, false);
        }
    }

    @Test
    public void P04637InteractorsTest() {
        String stringId = "9606.ENSP00000269305";
        StringQuerier querier = new StringQuerier();
        ArrayList<InteractorRecord> responses = querier.getInteractors(stringId);
        assertEquals(10, responses.size());
    }
}
