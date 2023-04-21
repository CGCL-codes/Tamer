package org.identifylife.key.engine.core.service.descriptions.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.identifylife.key.engine.core.model.descriptions.Value;
import org.identifylife.key.engine.core.model.taxonomy.Taxon;
import org.identifylife.key.engine.core.service.descriptions.DescriptionService;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dbarnier
 *
 */
public class RemoteDescriptionsServiceTests {

    private static final Log logger = LogFactory.getLog(RemoteDescriptionsServiceTests.class);

    private DescriptionService service;

    public RemoteDescriptionsServiceTests() {
        service = new RemoteDescriptionService();
    }

    @Test
    public void testGetScoredTaxa() throws Exception {
        List<Taxon> taxa = new ArrayList<Taxon>();
        List<Taxon> results = service.getScoredTaxa(taxa);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() == 0);
        taxa.add(new Taxon("invalid_uuid"));
        results = service.getScoredTaxa(taxa);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() == 0);
        taxa.add(new Taxon("urn:uuid:identifylife.org:taxon:e4:root", "e4"));
        results = service.getScoredTaxa(taxa);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() == 1);
    }

    @Test
    public void testGetScoresByStateAndTaxa() throws Exception {
        String stateId = "urn:uuid:identifylife.org:state:s51:root";
        List<String> taxonIds = new ArrayList<String>();
        taxonIds.add("urn:uuid:identifylife.org:taxon:7718:col2009ac");
        Map<String, Value> scoreMap = service.getScoresForStateAndTaxa(stateId, taxonIds.toArray(new String[0]));
        Assert.assertNotNull(scoreMap);
        Assert.assertTrue(scoreMap.containsKey(taxonIds.get(0)));
    }
}
