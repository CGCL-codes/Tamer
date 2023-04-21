package backend.query.advanced;

import java.io.File;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import backend.ONDEX;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.exchange.xml.ConceptParser;
import backend.exchange.xml.XmlParser;
import backend.parser.ParserArguments;
import backend.parser.kegg.Parser;
import com.thoughtworks.xstream.XStream;

public class TestAdvancedQueryWI {

    private static Session s;

    private static AbstractONDEXGraph aog;

    public static void main(String[] args) {
        String ondexName = "TestWI";
        String ondexDir = System.getProperty("ondex.dir");
        String filename = System.getProperty("ondex.dir") + File.separator + "dbs" + File.separator + ondexName + File.separator + "ondex.dbs";
        String username = "test";
        String password = "12";
        ONDEX ondex = null;
        s = Session.getSession(username, password);
        ondex = new ONDEX(s, ondexName);
        String filterXML = "<ondexquery><searchterm><ondexmetadata><cv>EC</cv><unit/><attrname/><evidence/><cc/><rt/><rtsets/></ondexmetadata><operator>AND</operator><ondexconceptdata><conames/><coaccessions/><cogds/></ondexconceptdata><operator>AND</operator><ondexrelationdata><relationgds/></ondexrelationdata></searchterm></ondexquery>";
        filterXML = "";
        ParserArguments pa = new ParserArguments();
        pa.setInputDir(ondexDir + File.separator + "importdata" + File.separator + "kegg" + File.separator);
        pa.addOption("config", "ath");
        s = ondex.getSession();
        Parser parser = new backend.parser.kegg.Parser(s);
        parser.addParserListener(ondex.getLogger());
        parser.setParserArguments(pa);
        aog = ondex.getONDEXGraph();
        parser.setONDEXGraph(aog);
        ondex.getPersistentEnv().commit();
        AdvancedQuery query = new AdvancedQuery(s, aog);
        try {
            query.init("blast");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        System.out.println("Searchable Fields = " + query.getSearchableFields());
        System.out.println("NeededArguments : " + query.getNeededArguments());
        String outputDir = "D:" + File.separator + "Ondex_data" + File.separator + "dbs" + File.separator + "TestWI" + File.separator;
        String programDir = "D:" + File.separator + "bin" + File.separator;
        String searchString = "atgcttaatatatttaatttgatctgtattttttttaattctacccttttttcaagcacttttttagtcgccaaattgccagaggcctacgcctttttgaatccaatcgtagatgttatgcccgtaatacctcttttctttcttctcttagcctttgtttggcaagccgctgtaagttttcgataa";
        String smPara = "";
        AdvancedQueryParameters smp = new AdvancedQueryParameters();
        AdvancedQueryParameter sp = new AdvancedQueryParameter();
        sp.setName("Evalue");
        sp.setValue("10");
        smp.addParameter(sp);
        AdvancedQueryParameter sp1 = new AdvancedQueryParameter();
        sp1.setName("BlastType");
        sp1.setValue("blastn");
        smp.addParameter(sp1);
        XStream xstream = new XStream();
        smPara = xstream.toXML(smp);
        query.setArguments(outputDir, programDir, smPara);
        query.setSearchString(searchString);
        String result = query.start(filterXML, filename);
        try {
            xml2Objects(result);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static void xml2Objects(String xmlString) throws XMLStreamException {
        String ondexname = "TestWI_II";
        String username = "test";
        String password = "12";
        ONDEX ondex = null;
        s = Session.getSession(username, password);
        ondex = new ONDEX(s, ondexname);
        Session t = ondex.getSession();
        AbstractONDEXGraph og = ondex.getONDEXGraph();
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        StringReader sw = new StringReader(xmlString);
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(sw);
        XmlParser parser = new XmlParser();
        Hashtable<Integer, Integer> htable = new Hashtable<Integer, Integer>();
        parser.registerParser("concept", new ConceptParser(t, og, htable));
        parser.parse(xmlr);
        xmlr.close();
        Iterator<AbstractConcept> it = og.getConcepts(t);
        while (it.hasNext()) {
            System.out.println("	--> Concept Id: " + ((AbstractConcept) it.next()).getId(t));
        }
    }
}
