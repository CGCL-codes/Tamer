package org.rascalli.mbe.mmg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedList;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import junit.framework.TestCase;
import org.apache.commons.lang.StringEscapeUtils;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.rascalli.framework.config.ConfigService;
import org.rascalli.mbe.RdfData;
import org.rascalli.mbe.mmg.MMGInputRepresentation.ContentType;
import org.rascalli.mbe.mmg.MMGTool.MMGOutput;
import org.xml.sax.SAXException;

public class MMGTest extends TestCase {

    private final String LOCAL = "http://www.ofai.at/rascalli/2007/02/22/RascalliInnate.owl#";

    private SchemaFactory factory;

    private Validator validator;

    private Schema schema;

    private ConfigService cs;

    public MMGTest() throws Exception {
        factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        URL schemaUrl = getClass().getClassLoader().getResource("ClientSchema.xsd");
        File schemaLocation = new File(schemaUrl.toURI());
        schema = factory.newSchema(schemaLocation);
        validator = schema.newValidator();
        cs = new MockConfigService();
    }

    public void testGenerateOutput() throws Exception {
        MMGTool mmg = new MMGTool();
        mmg.noPost();
        mmg.setRascalliWSImpl(null);
        mmg.setQuestionAnalysis(new MockQuestionAnalysis());
        mmg.setConfigService(cs);
        mmg.setTemplateFile("templates/master.vm");
        MockAgent agent = new MockAgent();
        agent.setId(1);
        agent.setName("Marvin");
        agent.setLastToolUsed("T-NALQI");
        mmg.setMBEAgent(agent);
        MockMMGInputRepresentation ir = new MockMMGInputRepresentation();
        System.out.println("tesing simple output...");
        ir.setCommunicationChannel("ECA");
        ir.setContentType(ContentType.STRING);
        ir.setDialogueAct(LOCAL + "R-ToolOutput");
        ir.setTextValue("Guy Richie, Sean Penn");
        System.out.println(mmg.generateOutput(ir));
        agent.setLastToolUsed("T-QA");
        ir.setTextValue("http://www.google.com or http://slashdot.org/");
        System.out.println(mmg.generateOutput(ir));
        agent.setLastToolUsed("T-NALQI");
        System.out.println("Testing Nalqi output in ECA\n");
        ir.setTextValue("Rescue,Betty Buckley, Whitney Kershaw," + "Britney Spears,Youssou N'Dour Duet with Nenah Cherry," + "The Highwaymen,Queen,Low,Usher,Styles,Perfect," + "Evita Soundtrack-Antonio Banderas," + "Spirit,America,Case,Baby,Slave," + "Heart," + "Flatt Scruggs with The Foggy Mountain Boys," + "Evita Soundtrack-Madonna,Five," + "Junior Walker," + "Soundtrack,Point,Lloyd,Ray Charles And His Orchestra," + "Madonna,Travis Tritt duet with Marty Stuart," + "Black Eyed Peas,DEATH,Flame,Acceptance,M,Evita Soundtrack-Jimmy Nail," + "Common,Rainbow,Player," + "Collin Walcott, Nana Juvenal Vasconcelos, Don Cherry");
        MMGOutput out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        System.out.println("Testing Nalqi output in Jabber");
        ir.setCommunicationChannel("JABBER");
        System.out.println(mmg.generateOutput(ir));
        System.out.println("Testing empty tool output.");
        ir.setCommunicationChannel("ECA");
        ir.setTextValue("");
        ir.setContentType(ContentType.EMPTY);
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        System.out.println("Testing various dialogue acts");
        ir.setTextValue("");
        ir.setContentType(ContentType.EMPTY);
        String[] classes = new String[] { "R-DoYouKnow", "R-Ontology", "R-ReplyOnNegative", "R-ReplyOnPositive", "R-Busy", "R-Hello", "R-Goodbye" };
        for (String uttClass : classes) {
            ir.setDialogueAct(LOCAL + uttClass);
            out = mmg.generateOutput(ir);
            System.out.println(out.getContent());
        }
        System.out.println("Testing busy, hello, goodbye for Jabber");
        classes = new String[] { "R-Busy", "R-Hello", "R-Greeting", "R-Goodbye" };
        ir.setCommunicationChannel("JABBER");
        for (String uttClass : classes) {
            ir.setDialogueAct(LOCAL + uttClass);
            System.out.println(mmg.generateOutput(ir));
        }
        System.out.println("Testing R-RequireFeedback Dialogue Act");
        ir.setDialogueAct(LOCAL + "R-RequireFeedback");
        ir.setCommunicationChannel("ECA");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        ir.setCommunicationChannel("ECA");
        ir.setTextValue("");
        ir.setContentType(ContentType.STRING);
        ir.setDialogueAct(LOCAL + "R-ChatBot");
        ir.setTextValue("Hello, my name is Kaiser Franz and I am a chat bot.");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        System.out.println("Testing URL class");
        ir.setDialogueAct(LOCAL + "R-ToolOutput");
        LinkedList<String> linkList = new LinkedList<String>();
        linkList.add("http://www.ofai.at");
        linkList.add("http://www.ofai.at/~gregor.sieber");
        ir.setLinkList(linkList);
        ir.setTextValue("This is just a test.");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        System.out.println("Testing URL class with empty string input");
        ir.setTextValue("");
        ir.setContentType(ContentType.EMPTY);
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        System.out.println("Testing R-NewRss Dialogue Act");
        ir.setDialogueAct(LOCAL + "R-NewRss");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        System.out.println("Testing R-NewRss Dialogue Act for Jabber");
        ir.setCommunicationChannel("JABBER");
        System.out.println(mmg.generateOutput(ir));
        System.out.println("Testing template selection using useTemplate property");
        ir.setCommunicationChannel("ECA");
        ir.setContentType(ContentType.STRING);
        ir.setTemplateName("nametest.vm");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertTrue(validate(out.getContent()));
        ir.setTemplateName(null);
        System.out.println("Testing general Jabber output, string plus links");
        ir.setCommunicationChannel("JABBER");
        ir.setTextValue("This is just a test.");
        ir.setLinkList(linkList);
        System.out.println(mmg.generateOutput(ir));
        System.out.println("Testing passing through of BML data");
        ir.setCommunicationChannel("ECA");
        ir.setPassData(StringEscapeUtils.escapeXml("<session id=\"2\" " + " xmlns=\"http://www.ofai.at/rascalli/ClientSchema/\">" + "<bml><act id=\"a_1\" actor=\"4711\" audience=\"4\" type=\"discourse\">" + "<utterance id=\"u_1\" start=\"1000ms\">" + "Madonna's boyfriends are Carlos Leon, John \"Jellybean\" Benitez, " + "Jean-Michel Basquiat, John Enos III, Jenny Shimizu, David Blaine, Dennis Rodman, " + "Vanilla Ice and Mark Kamins.</utterance>		</act>		</bml>		<media>	<content type=\"thought_bubble\" id=\"media_0\" action=\"open\">http://rascalli.dfki.de/test/memory.html?aid=1</content><content type=\"internal_window\" id=\"media_1\" action=\"open\">http://rascalli.dfki.de/test/blank-screen.html</content></media>" + "</session>"));
        out = mmg.generateOutput(ir);
        System.out.println(out.toString());
        LinkedList<String> li = new LinkedList<String>();
        li.add("screenUrl");
        ir.setLinkList(li);
        ir.setTextValue("test data passing");
        ir.setCommunicationChannel("JABBER");
        out = mmg.generateOutput(ir);
        System.out.println(out.getContent());
        assertEquals("test data passing\n\nscreenUrl", out.getContent());
        ir.clearPassData();
        System.out.println("testing emotions...\n");
        ir.setCommunicationChannel("ECA");
        ir.setTextValue("");
        String[] emotions = new String[] { "R-Joy", "R-Distress", "R-Hope", "R-Fear", "R-Satisfaction", "R-Disappointment", "R-Relief", "R-FearsConfirmed" };
        for (String s : emotions) {
            ir.setDialogueAct(LOCAL + s);
            System.out.println("testing " + s);
            out = mmg.generateOutput(ir);
            System.out.println(out.getContent());
        }
        System.out.println("testing -PlaySong");
        li.clear();
        li.add("http://foo.bar/bla.mp3");
        ir.setLinkList(li);
        ir.setCommunicationChannel("ECA");
        ir.setTextValue("bla bla song");
        ir.setDialogueAct(LOCAL + "R-PlaySong");
        out = mmg.generateOutput(ir);
        assertTrue(out.getContent().contains("<media><content type=\"audioref\" id=\"media_0>http://foo.bar/bla.mp3</content></media>"));
    }

    public void testRdfStuff() throws Exception {
        MMGTool mmg = new MMGTool();
        mmg.noPost();
        mmg.setRascalliWSImpl(null);
        mmg.setQuestionAnalysis(new MockQuestionAnalysis());
        mmg.setConfigService(cs);
        mmg.setTemplateFile("templates/master.vm");
        MockAgent agent = new MockAgent();
        agent.setId(1);
        agent.setName("Marvin");
        agent.setLastToolUsed("T-NALQI");
        mmg.setMBEAgent(agent);
        agent.setLastToolUsed("T-NALQI");
        Repository r = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
        r.initialize();
        RepositoryConnection con = r.getConnection();
        String[] files = new String[] { "gossipdb.owl", "music_domain.owl", "similar_artists.owl" };
        String[] bases = new String[] { "http://www.ontotext.com/rascalli/2008/04/gossipdb.owl", "http://www.ontotext.com/rascalli/2007/10/music_domain", "http://www.ontotext.com/rascalli/2007/10/similar_artists" };
        int[] ints = new int[] { 0, 1, 2 };
        for (int i : ints) {
            InputStream in = getClass().getClassLoader().getResourceAsStream(files[i]);
            con.add(in, bases[i], RDFFormat.RDFXML);
        }
        ValueFactory f = r.getValueFactory();
        URI album6067 = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6067");
        URI album6068 = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6068");
        URI person415 = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossip_data#Person.415");
        URI name = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#name");
        URI prize1 = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossip_data#Prize.1");
        URI person = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#Person");
        URI album = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#Album");
        URI prize = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#Prize");
        URI gender = f.createURI("http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#gender");
        URI genderMale = f.createURI("http://proton.semanticweb.org/2007/10/proton_top#Male");
        Literal person415Name = f.createLiteral("Chris Lewis");
        Literal album6067Name = f.createLiteral("Live");
        Literal album6068Name = f.createLiteral("Blue");
        Literal pname = f.createLiteral("SomeAward");
        con.add(person415, RDF.TYPE, person);
        con.add(person415, name, person415Name);
        con.add(person415, gender, genderMale);
        con.add(album6067, RDF.TYPE, album);
        con.add(album6067, name, album6067Name);
        con.add(album6068, RDF.TYPE, album);
        con.add(album6068, name, album6068Name);
        con.add(prize1, RDF.TYPE, prize);
        con.add(prize1, name, pname);
        con.close();
        mmg.setQuestionAnalysis(new MockQuestionAnalysis());
        mmg.setRascalliRepository(r);
        String testString = "<rdf:RDF xmlns=\"http://www.ofai.at/rascalli/2007/02/22/RascalliInnate.owl#\" " + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" " + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + " xmlns:gossipdata=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#\" " + "xmlns:gossipdb=\"http://www.ontotext.com/rascalli/2008/04/gossipdb.owl#\" " + "xmlns:similar=\"http://www.ontotext.com/rascalli/2007/10/similar_artists#\">" + "<T-MMG rdf:about=\"http://www.ofai.at/rascalli/2007/02/22/RascalliInnate.owl#T-MMG-1\"> " + "<realizeAction>\n <MultimodalSay rdf:about=\"http://www.ofai.at/rascalli/2007/02/22/RascalliInnate.owl#MultimodalSay-5\"/>\n " + "</realizeAction>\n<hasInput>" + "<Utterance>" + "<hasDialogueAct>" + "<R-ToolOutput rdf:about=\"http://www.ofai.at/rascalli/2007/02/22/RascalliInnate.owl#R-ToolOutput-H45QSF14-B6AV-23UB-7321-6DE2354506QT\"/>" + "</hasDialogueAct>" + "<hasUtteranceRepresentation>" + "<rdf:Description rdf:about=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Person.415\">" + "<gossipdb:hasAlbum rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6067\"/>" + "<gossipdb:hasAlbum rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6068\"/>" + "<gossipdb:hasPrize rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Prize.1\"/>" + "<gossipdb:deathDay>12-12-1964</gossipdb:deathDay>" + "</rdf:Description></hasUtteranceRepresentation>" + "<hasUtteranceRepresentation rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6067\"/>" + "<hasUtteranceRepresentation rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Album.6068\"/>" + "<hasUtteranceRepresentation rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Person.415\"/>" + "<hasUtteranceRepresentation rdf:resource=\"http://www.ontotext.com/rascalli/2008/04/gossip_data#Prize.1\"/>" + "</Utterance>" + "</hasInput></T-MMG></rdf:RDF>";
        MMGOutput out = mmg.generateOutput(new RdfData(testString));
        System.out.println(out);
        assertTrue(out.getContent().contains("Chris Lewis has the albums Live and Blue, and he won the prize SomeAward.") || out.getContent().contains("Chris Lewis has the albums Blue and Live, and he won the prize SomeAward.") || out.getContent().contains("Chris Lewis won the prize SomeAward, and he has the albums Live and Blue.") || out.getContent().contains("Chris Lewis won the prize SomeAward, and he has the albums Blue and Live."));
    }

    public void testPlaySong() {
    }

    public void testGenerateLinks() {
        MMGTool mmg = new MMGTool();
        mmg.setQuestionAnalysis(new MockQuestionAnalysis());
        String out = mmg.generateLinks("http://www.ofai.at http://www.ofai.at/rascalli http://abc.def.ghi/foo.bar?ha&ha&ha;");
        assertEquals("<a href=\"http://www.ofai.at\">http://www.ofai.at</a> " + "<a href=\"http://www.ofai.at/rascalli\">http://www.ofai.at/rascalli</a> " + "<a href=\"http://abc.def.ghi/foo.bar?ha&ha&ha;\">http://abc.def.ghi/foo.bar?ha&ha&ha;</a>", out);
    }

    private boolean validate(String xml) {
        Source source = new StreamSource(new StringReader(xml));
        try {
            validator.validate(source);
            return true;
        } catch (SAXException ex) {
            System.out.println("data not valid because: ");
            System.out.println(ex.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
