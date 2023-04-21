package examples.das;

import edu.mit.wi.omnigene.das.*;
import java.net.*;
import java.util.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * We will connect to Jim Kent's
 * GoldenPath das server and execute the
 * DSN, EntryPoints,Features queries for SNPs
 * located between bases 20,000 and 30,000 on chromosome 1 
 * Then print everything to STDOUT
 *@author Brian Gilman 
 *@version $Revision: 1.1 $
 */
public class UCSCDASExample {

    private URL url = null;

    private Vector entryPoints = null;

    private DASHTTPConnection conn = null;

    private DSN dsn = null;

    private EntryPoint ep = null;

    private Feature feature = null;

    private GFF gff = null;

    private ResultCollection results = null;

    public UCSCDASExample(URL url) {
        this.url = url;
    }

    /**
     *Contructor takes
     *@param prefix the url to connect to 
     *@param dsn the dsn to use
     *@param query the query object to use @see DASQuery
     */
    public void connect(URL prefix, DSN dsn, DASQuery query) throws Exception {
        if (dsn != null) {
            conn = new DASHTTPConnection(prefix, dsn, query);
        } else {
            conn = new DASHTTPConnection(prefix);
        }
    }

    /**
     *Method which executes the DSN command against the UCSC test
     *browser
     */
    public void getDSN() throws Exception {
        connect(this.url, null, null);
        DSNQuery q = new DSNQuery();
        conn.setQuery(q);
        conn.doQuery();
        results = (ResultCollection) conn.getResult();
        Enumeration e = results.getResults();
        while (e.hasMoreElements()) {
            DSN _dsn = (DSN) e.nextElement();
            String id = _dsn.getID();
            System.out.println("DSN's are " + id);
            if (id.equals("hg7")) {
                System.out.println("Choosing " + id + " as the DSN to execute entry_points query from");
                this.dsn = _dsn;
            }
        }
        conn = null;
    }

    public void getEntryPoints() throws Exception {
        EntryPointsQuery epQuery = new EntryPointsQuery();
        connect(this.url, this.dsn, epQuery);
        conn.setQuery(epQuery);
        conn.doQuery();
        ep = (EntryPoint) conn.getResult();
        if (ep == null) {
            System.out.println("ep is null");
        }
        System.out.println("EntryPoint ID: " + ep.getID());
        conn = null;
    }

    /**
     *Method which executes the Fetures query against
     *the ucsc das server and prints out SNP features 
     *with associated annotations
     */
    public void getFeatures() throws Exception {
        FeaturesQuery fQuery = new FeaturesQuery();
        System.out.println("Getting snps from GoldenPath on Chromosome 22 from 20000-30000 bp's");
        fQuery.setRef("chr1");
        fQuery.setStart("20000");
        fQuery.setStop("30000");
        fQuery.setType("snpTsc");
        connect(this.url, this.dsn, fQuery);
        conn.doQuery();
        GFF gff = (GFF) conn.getResult();
        Enumeration segEnum = gff.getSegments();
        while (segEnum.hasMoreElements()) {
            Segment result = (Segment) segEnum.nextElement();
            Enumeration annots = result.getAnnots();
            while (annots.hasMoreElements()) {
                Feature feat = (Feature) annots.nextElement();
                Element el = feat.getContents();
                NodeList nl = el.getElementsByTagName("START");
                Node nd = nl.item(0);
                Node fch = nd.getFirstChild();
                CharacterData chData = (CharacterData) fch;
                String start = chData.getData();
                System.out.println("TSC-ID: " + "<" + feat.getLabel() + ">" + " Start Position: " + "<" + start + ">");
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Connecting to http://genome-test.cse.ucsc.edu/cgi-bin/das");
            UCSCDASExample ex = new UCSCDASExample(new URL("http://genome-test.cse.ucsc.edu/cgi-bin/das"));
            System.out.println("Getting the DSN's");
            ex.getDSN();
            System.out.println("Getting entry points for selected DSN");
            ex.getEntryPoints();
            System.out.println("Getting SNP features from the selected entry point");
            ex.getFeatures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
