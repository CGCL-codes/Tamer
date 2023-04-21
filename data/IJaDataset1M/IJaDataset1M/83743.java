package gov.lanl.federator.tapefilter;

import gov.lanl.ockham.iesrdata.IESRCollection;
import gov.lanl.ockham.iesrdata.IESRService;
import gov.lanl.registryclient.RegistryRecord;
import gov.lanl.util.oai.oaiharvesterwrapper.ListIdentifiers;
import gov.lanl.util.oai.oaiharvesterwrapper.ListRecords;
import gov.lanl.util.oai.oaiharvesterwrapper.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import junit.framework.TestCase;

public class ProxyTest extends TestCase {

    String id = "info:lanl-repo/xmltape/123";

    String oaipmhid = "http://info.info/oaipmh/123";

    String openurlid = "http://info.info/openurl/123";

    String datestamp = "2000-01-01";

    Map<String, RegistryRecord<IESRCollection>> collections;

    Map<String, RegistryRecord<IESRService>> services;

    protected void setUp() throws Exception {
        super.setUp();
        IESRCollection coll = new IESRCollection();
        coll.setIdentifier(id);
        coll.setContentRange("2000-01-01");
        coll.setTitle("ISI 2000");
        HashSet<String> isPartOf = new HashSet<String>();
        isPartOf.add("info:sid/library.lanl.gov:ecd");
        coll.setIsPartOf(isPartOf);
        HashSet<String> types = new HashSet<String>();
        types.add("xmltape");
        coll.setTypes(types);
        HashSet<String> serves = new HashSet<String>();
        serves.add(oaipmhid);
        serves.add(openurlid);
        coll.setServices(serves);
        IESRService oaiservice = new IESRService();
        oaiservice.setIdentifier("http://info.info/oaipmh/123");
        oaiservice.setLocator("http://barracuda.lanl.gov:8080/project/moai2/ECD_DUBLIN-2006-09-04_2ec3f14a-41bd-11db-8d79-b0a8c271beda");
        oaiservice.setServes("info:lanl-repo/xmltape/123");
        oaiservice.setSupportsStandard("OAI-PMH");
        oaiservice.setTitle("oaipmh interface");
        oaiservice.setType("oai-pmh");
        IESRService openurlservice = new IESRService();
        openurlservice.setIdentifier("http://info.info/openurl/123");
        openurlservice.setLocator("http://barracuda.lanl.gov:8080/project/moai2/ECD_DUBLIN-2006-09-04_2ec3f14a-41bd-11db-8d79-b0a8c271beda");
        openurlservice.setServes("info:lanl-repo/xmltape/123");
        openurlservice.setSupportsStandard("OpenURL");
        openurlservice.setTitle("openurl interface");
        openurlservice.setType("openurl");
        collections = new HashMap<String, RegistryRecord<IESRCollection>>();
        collections.put(id, new RegistryRecord(id, datestamp, coll));
        services = new HashMap<String, RegistryRecord<IESRService>>();
        services.put(oaipmhid, new RegistryRecord(oaipmhid, datestamp, oaiservice));
        services.put(openurlid, new RegistryRecord(openurlid, datestamp, openurlservice));
    }

    public void testLRMatch() throws Exception {
        Proxy proxy = new Proxy(Proxy.Operation.LISTRECORDS, collections, services, datestamp, datestamp, null);
        assertNotNull(proxy.getResult());
    }

    public void testLRNoMatch() throws Exception {
        Proxy proxy = new Proxy(Proxy.Operation.LISTRECORDS, collections, services, "2005-01-01", "2005-01-01", null);
        assertNull(proxy.getResult());
    }

    public void testLRSet() throws Exception {
        Sets sets = new Sets(Sets.COLLECTION, "info:sid/library.lanl.gov:ecd");
        Proxy proxy = new Proxy(Proxy.Operation.LISTRECORDS, collections, services, datestamp, datestamp, sets.getSetSpec());
        Object obj = proxy.getResult();
        assertNotNull(obj);
        assertTrue(obj instanceof ListRecords);
    }

    public void testLRWrongSet() throws Exception {
        Sets sets = new Sets(Sets.COLLECTION, "info:sid/library.lanl.gov:isi");
        Proxy proxy = new Proxy(Proxy.Operation.LISTRECORDS, collections, services, datestamp, datestamp, sets.getSetSpec());
        assertNull(proxy.getResult());
    }

    public void testLIMatch() throws Exception {
        Proxy proxy = new Proxy(Proxy.Operation.LISTIDENTIFIERS, collections, services, datestamp, datestamp, null);
        Object obj = proxy.getResult();
        assertNotNull(obj);
        assertTrue(obj instanceof ListIdentifiers);
    }

    public void testLINoMatch() throws Exception {
        Proxy proxy = new Proxy(Proxy.Operation.LISTIDENTIFIERS, collections, services, "2005-01-01", "2005-01-01", null);
        assertNull(proxy.getResult());
    }

    public void testLISet() throws Exception {
        Sets sets = new Sets(Sets.COLLECTION, "info:sid/library.lanl.gov:ecd");
        Proxy proxy = new Proxy(Proxy.Operation.LISTIDENTIFIERS, collections, services, datestamp, datestamp, sets.getSetSpec());
        assertNotNull(proxy.getResult());
    }

    public void testLIWrongSet() throws Exception {
        Sets sets = new Sets(Sets.COLLECTION, "info:sid/library.lanl.gov:isi");
        Proxy proxy = new Proxy(Proxy.Operation.LISTIDENTIFIERS, collections, services, datestamp, datestamp, sets.getSetSpec());
        assertNull(proxy.getResult());
    }
}
