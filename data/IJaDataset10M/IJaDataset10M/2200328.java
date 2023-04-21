package gov.lanl.xmltape.registry;

import java.net.URL;
import java.util.Iterator;
import ORG.oclc.oai.harvester.io.OAIReader;
import ORG.oclc.oai.harvester.verb.GetRecord;
import ORG.oclc.oai.harvester.verb.Record;
import gov.lanl.xmltape.TapeProperties;

/**
 * OAI based tape Registry
 *  
 */
public class OAIRegistry extends Registry {

    String harvurl;

    boolean initialized = false;

    private String mutex = "";

    /**
     * The baseurl of xmltape registry is passed in. The real xmltape registry
     * is not loaded at this time, due to the possible loading sequence problem
     * when we have all components runing in same tomcat.
     */
    public OAIRegistry(String baseurl) throws RegistryException {
        super();
        this.harvurl = baseurl;
    }

    /**
     * read tape configuration, implemented by xmltape registry the work flow is
     * like: (1) during the first time request, do a list records and load all
     * xmltape configuration (2) if registry about a tape is already registered,
     * return it. (3) otherwise try to contact taperegistry for the new xmltape
     * (by OAI GetRecord), if exists, put it into cache and return it. (4) in
     * all other cases, throw a registryexception.
     * 
     *  
     */
    protected TapeConfig readTapeConfig(String tapename) throws RegistryException {
        try {
            ORG.oclc.oai.harvester.verb.Record record = null;
            URL url = new URL(harvurl);
            if (tapename.indexOf(TapeProperties.getLocalXmlTapePrefix()) < 0) {
                tapename = TapeProperties.getLocalXmlTapePrefix() + tapename;
            }
            synchronized (mutex) {
                if (!initialized) {
                    OAIReader oaiReader = new OAIReader(url, null, null, (String) null, "tape");
                    while ((record = oaiReader.readNext()) != null) {
                        MetaHarvester mh = new MetaHarvester(record);
                        tapemap.put(record.getIdentifier(), new TapeConfig(mh.Meta2Prop()));
                    }
                    initialized = true;
                }
            }
            if (tapemap.get(tapename) == null) {
                GetRecord gr = new GetRecord(url, tapename, "tape");
                Iterator i = gr.iterator();
                if (i.equals(null) || (!i.hasNext())) throw new Exception("xmltape  does not exist");
                record = (Record) i.next();
                MetaHarvester mh = new MetaHarvester(record);
                tapemap.put(record.getIdentifier(), new TapeConfig(mh.Meta2Prop()));
            }
            return (TapeConfig) tapemap.get(tapename);
        } catch (Exception ex) {
            throw new RegistryException(ex);
        }
    }
}
