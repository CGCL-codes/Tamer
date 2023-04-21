package de.unikoblenz.isweb.xcosima.adapter;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URI;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.sail.nativerdf.NativeStore;
import de.unikoblenz.isweb.xcosima.annotation.SkosConcept;
import de.unikoblenz.isweb.xcosima.communication.Conversation;
import de.unikoblenz.isweb.xcosima.communication.IMFileTransfer;
import de.unikoblenz.isweb.xcosima.communication.InstantMessage;
import de.unikoblenz.isweb.xcosima.communication.Mail;
import de.unikoblenz.isweb.xcosima.dns.Agent;
import de.unikoblenz.isweb.xcosima.dns.InformationObject;
import de.unikoblenz.isweb.xcosima.dns.Particular;
import de.unikoblenz.isweb.xcosima.dns.Quality;
import de.unikoblenz.isweb.xcosima.dns.Situation;
import de.unikoblenz.isweb.xcosima.realization.DigitalObject;
import de.unikoblenz.isweb.xcosima.realization.DigitalRealization;
import de.unikoblenz.isweb.xcosima.realization.FileSystemRealization;
import de.unikoblenz.isweb.xcosima.realization.InternalRealization;
import de.unikoblenz.isweb.xcosima.task.Action;
import de.unikoblenz.isweb.xcosima.task.ActionTask;
import de.unikoblenz.isweb.xcosima.task.BeginTask;
import de.unikoblenz.isweb.xcosima.task.ComplexTask;
import de.unikoblenz.isweb.xcosima.task.InformationGathering;
import de.unikoblenz.isweb.xcosima.task.PlanExecution;
import de.unikoblenz.isweb.xcosima.task.PlanningActivity;
import de.unikoblenz.isweb.xcosima.task.Task;
import de.unikoblenz.isweb.xcosima.task.TaskInput;
import de.unikoblenz.isweb.xcosima.task.TaskOutput;
import de.unikoblenz.isweb.xcosima.task.TaskOwner;

/**
 * @author Thomas Franz, http://isweb.uni-koblenz.de
 * 
 */
public class SesameTest {

    static SesameAdapter r;

    static Repository rep;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        r = new SesameAdapter();
        rep = new SailRepository(new NativeStore(new File("/home/franz/tmp")));
        rep.initialize();
        r.setRep(rep);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        System.out.println("Clearing repository ...");
        RepositoryConnection con = r.getRep().getConnection();
        con.clear();
        con.close();
        System.out.println("Repository cleared.");
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        System.out.println("==== Repository Content START ====");
        r.getRep().getConnection().export(new N3Writer(System.out));
        System.out.println("\n==== Repository Content END ====");
    }

    @Test
    public void testAgent() throws Exception {
        Agent thomas = new Agent();
        thomas.addMailAddress("franz@uni-koblenz.de");
        thomas.save(r);
        Agent copy = Agent.createOrConstructFromMailAddress("franz@uni-koblenz.de", r);
        Assert.assertNotNull("reconstruction failed", copy);
        assertEquals("URI of reconstructed Agent differs from created one!", thomas.getURI(), copy.getURI());
        assertEquals("Constructed Agent differs from created one!", thomas, copy);
        copy = Particular.construct(thomas.getURI(), r);
        Assert.assertNotNull("reconstruction failed", copy);
        assertEquals("URI of reconstructed Agent differs from created one!", thomas.getURI(), copy.getURI());
        assertEquals("Constructed Agent differs from created one!", thomas, copy);
        String thomasJID = "tom@kater.uni-koblenz.de";
        thomas.addJabberAddress(thomasJID);
        thomas.save(r);
        copy = Agent.createOrConstructFromJID(thomasJID, r);
        assertEquals("URI of reconstructed Agent differs from created one!", thomas.getURI(), copy.getURI());
        assertEquals("Constructed Agent differs from created one!", thomas, copy);
        Agent steffen = new Agent();
        steffen.addMailAddress("staab@uni-koblenz.de");
        Assert.assertFalse("Steffen and Thomas should not be the same!", steffen.equals(thomas));
        steffen.save(r);
        Assert.assertFalse("Steffen and Thomas should not be the same!", steffen.equals(thomas));
        r.getRep().getConnection().export(new N3Writer(System.out));
        List<Agent> agents = Agent.constructAllAgents(r);
        Assert.assertTrue("Reconstruction of the two created Agents failed!", agents.size() == 2);
        for (Agent a : agents) {
            System.out.println(a);
        }
        agents = Particular.constructAll(Agent.class, r);
        Assert.assertTrue("Reconstruction of the two created Agents failed!", agents.size() == 2);
        for (Agent a : agents) {
            System.out.println(a);
        }
    }

    @Test
    public void testUpdate() {
        String mail = "tester@example.com";
        String jid = "tester@dom.uni-koblenz.de";
        Agent a1 = new Agent();
        a1.addMailAddress(mail);
        a1.save(r);
        Agent a2 = Agent.createOrConstructFromMailAddress(mail, r);
        a2.addJabberAddress(jid);
        a2.save(r);
        Agent a3 = Agent.createOrConstructFromMailAddress(mail, r);
        List<Quality> q = a3.getQualities(r);
        assertEquals("didn't recieve 2 qualities", 2, q.size());
        System.out.println(q);
    }

    public static <T extends Particular> void print(List<T> objects, String text) {
        System.out.println("Retrieved " + objects.size() + " " + text + "(s):");
        for (T o : objects) {
            System.out.println("\t" + o);
        }
    }

    @Test
    public void testMail() {
        Agent thomas = Agent.createOrConstructFromMailAddress("franz@uni-koblenz.de", r);
        Agent steffen = Agent.createOrConstructFromMailAddress("staab@uni-koblenz.de", r);
        DigitalRealization dr = new FileSystemRealization(new File("/home/franz/tmp/MyTag.pdf"), new InformationObject(), r);
        dr.save(r);
        Conversation conv = new Conversation();
        Mail mail1 = Mail.constructNewMail(r);
        mail1.setBody("This is a sample email body");
        mail1.setConversation(conv, r);
        mail1.setSubject("TestMessage");
        mail1.setMessageId("3484982819@mail.uni-koblenz.de");
        mail1.setRecipient(thomas, r);
        mail1.setSender(steffen, r);
        mail1.addAttachment(dr, r);
        mail1.save(r);
        Mail mailCopy = Mail.getByMessageId("3484982819@mail.uni-koblenz.de", r);
        Assert.assertTrue(mailCopy != null);
        System.out.println("Information about mailCopy " + mailCopy);
        System.out.println("Classifiers: " + mailCopy.getClassifiers(r));
        System.out.println("Situations: " + mailCopy.getSituations(r));
        System.out.println("Roles: " + mailCopy.getRoles(r));
        System.out.println("URI: " + mailCopy.getURI());
        System.out.println("Events: " + mailCopy.getEvents(r));
        Agent thomas2 = Agent.construct(thomas.getURI(), r);
        Assert.assertTrue(thomas2 != null);
        System.out.println("Agent information for " + thomas2);
        System.out.println("Situations: " + thomas2.getSituations(r));
        System.out.println("Qualities: " + thomas2.getQualities(r));
        System.out.println("Classifiers: " + thomas2.getClassifiers(r));
        System.out.println("Events: " + thomas2.getEvents(r));
        System.out.println("Roles: " + thomas2.getRoles(r));
        System.out.println("URI: " + thomas2.getURI());
    }

    @Test
    public void testIM() {
        Agent thomas = new Agent();
        thomas.addJabberAddress("tom@kater.uni-koblenz.de");
        Agent simon = new Agent();
        simon.addMailAddress("sschenk@kater.uni-koblenz.de");
        Conversation conv = new Conversation();
        InstantMessage im = InstantMessage.constructNewInstantMessage(r);
        im.setMessageText("hi simon, what's up?");
        im.setSender(thomas, r);
        im.setRecipient(simon, r);
        im.setConversation(conv, r);
        im.save(r);
        DigitalRealization dr = new FileSystemRealization(new File("/home/franz/tmp/MyTag.pdf"), new InformationObject(), r);
        dr.save(r);
        IMFileTransfer fileTransfer = new IMFileTransfer();
        fileTransfer.setMessageRealization(dr);
        fileTransfer.setSender(thomas, r);
        fileTransfer.setRecipient(simon, r);
        fileTransfer.setConversation(conv, r);
        fileTransfer.save(r);
    }

    @Test
    public void testScenario() {
        Agent thomas = new Agent();
        thomas.addJabberAddress("tom@kater.uni-koblenz.de");
        thomas.addMailAddress("franz@uni-koblenz.de");
        Agent simon = new Agent();
        simon.addJabberAddress("sschenk@kater.uni-koblenz.de");
        Agent steffen = new Agent();
        steffen.addMailAddress("staab@uni-koblenz.de");
        Conversation conv = new Conversation();
        InstantMessage im = InstantMessage.constructNewInstantMessage(r);
        im.setMessageText("hi thomas, can you send me the mytag paper?");
        im.setSender(simon, r);
        im.setRecipient(thomas, r);
        im.setConversation(conv, r);
        im.save(r);
        SkosConcept todo = new SkosConcept();
        todo.setPreferredLabel("todo", null);
        im.annotate(todo);
        im.save(r);
        DigitalRealization dr = new FileSystemRealization(new File("/home/franz/tmp/MyTag.pdf"), new InformationObject(), r);
        dr.save(r);
        Mail mail1 = Mail.constructNewMail(r);
        mail1.setBody("Hi Simon, Steffen, i attached the mytag paper ...");
        mail1.setConversation(conv, r);
        mail1.setSubject("mytag paper");
        mail1.setMessageId("3484982819@mail.uni-koblenz.de");
        mail1.setRecipient(steffen, r);
        mail1.setRecipient(simon, r);
        mail1.setSender(thomas, r);
        mail1.addAttachment(dr, r);
        mail1.save(r);
        SkosConcept myTag = new SkosConcept();
        myTag.setPreferredLabel("myTag", null);
        mail1.annotate(myTag);
        mail1.save(r);
    }

    @Test
    public void testAnnotation() {
        Agent thomas = new Agent();
        thomas.addJabberAddress("tom@kater.uni-koblenz.de");
        Agent simon = new Agent();
        simon.addMailAddress("sschenk@kater.uni-koblenz.de");
        InstantMessage im = InstantMessage.constructNewInstantMessage(r);
        im.setMessageText("hi simon, what's up?");
        im.setSender(thomas, r);
        im.setRecipient(simon, r);
        im.save(r);
        SkosConcept c2 = new SkosConcept();
        c2.setPreferredLabel("Message to Simon", null);
        im.annotate(c2);
        im.save(r);
    }

    @Test
    public void testTaskScenario() {
        PlanExecution resolveCustomerIssue = new PlanExecution();
        resolveCustomerIssue.setURI(new URIImpl("urn:planExec"));
        BeginTask btask = BeginTask.getInstance();
        Action initialMeeting = new PlanningActivity();
        initialMeeting.setURI(new URIImpl("urn:initMeeting"));
        initialMeeting.addCourse(btask);
        btask.add2Situation(resolveCustomerIssue, r);
        Agent engineer1 = Agent.createOrConstructFromMailAddress("engineer1@example.com", r);
        Agent engineer2 = Agent.createOrConstructFromMailAddress("engineer2@example.com", r);
        Agent expert = Agent.createOrConstructFromMailAddress("expert@example.com", r);
        Agent customer = Agent.createOrConstructFromMailAddress("issues@superbicycles.com", r);
        initialMeeting.addParticipant(engineer1);
        initialMeeting.addParticipant(engineer2);
        initialMeeting.addParticipant(customer);
        ComplexTask resolveIssues = new ComplexTask();
        btask.addSuccessor(resolveIssues);
        ActionTask webSearch = new ActionTask();
        Action xmediaSearch = new InformationGathering();
        xmediaSearch.addParticipant(engineer1);
        TaskOwner searchOwner = new TaskOwner();
        engineer1.addRole(searchOwner);
        searchOwner.add2Situation(resolveCustomerIssue, r);
        webSearch.addActivity(xmediaSearch);
        ActionTask expertConsultation = new ActionTask();
        Conversation conv = new Conversation();
        Mail askMail = Mail.constructNewMail(r);
        askMail.setBody("Hi expert, can you please give me details on operation temperatures for brake xyz?");
        askMail.setConversation(conv, r);
        askMail.setSubject("operation temps braky xyz");
        askMail.setMessageId("3484982819@example.com");
        askMail.setRecipient(expert, r);
        askMail.setSender(engineer2, r);
        askMail.save(r);
        Mail replyMail = Mail.constructNewMail(r);
        replyMail.setBody("Dear engineer2, valid operation temperatures are between 5-300 celsius, see also the manual i attached. Take a closer look also at the properties of the brake fluid.");
        DigitalRealization manual = new FileSystemRealization(new File("/home/engineer2/manuals/xyz.odf"), new InformationObject(), r);
        replyMail.setSender(expert, r);
        replyMail.setRecipient(engineer2, r);
        replyMail.addAttachment(manual, r);
        replyMail.setConversation(conv, r);
        replyMail.save(r);
        expertConsultation.addActivity(conv);
        resolveIssues.addSubTask(webSearch);
        resolveIssues.addSubTask(expertConsultation);
        resolveIssues.add2Situation(resolveCustomerIssue, r);
        TaskInput input1 = new TaskInput();
        input1.setURI(new URIImpl("urn:taskInput1"));
        input1.addPlayer(manual.getIO());
        input1.add2Situation(resolveCustomerIssue, r);
        TaskOutput planOutput = new TaskOutput();
        InformationObject solutionReport = new InformationObject();
        solutionReport.addRealization(new FileSystemRealization(new File("/home/engineer1/reports/solution-final.odf"), new InformationObject(), r));
        solutionReport.addRole(planOutput);
        planOutput.add2Situation(resolveCustomerIssue, r);
        resolveCustomerIssue.save(r);
        ActionTask expertConsultationCopy = ActionTask.construct(expertConsultation.getURI(), r);
        System.out.println("*** Expert consultation copy: ***");
        expertConsultationCopy.printInfo(System.out, r);
    }

    @Test
    public void testTasks() {
        ComplexTask t1 = new ComplexTask();
        ActionTask t1_1 = new ActionTask();
        t1.addSubTask(t1_1);
        ComplexTask t2 = new ComplexTask();
        t1.addSuccessor(t2);
        t1.save(r);
        t1.printInfo(System.out, r);
        Task t1_r = Task.construct(t1.getURI(), r);
        Assert.assertNotNull(t1_r);
        t1_r.printInfo(System.out, r);
        Task t2_r = Task.construct(t2.getURI(), r);
        Assert.assertNotNull(t2_r);
        t2_r.printInfo(System.out, r);
    }

    @Test
    public void testConstructAll() {
        testScenario();
        List<Particular> parts = Particular.constructAll(Particular.class, r);
        print(parts, "Particular");
        List<Situation> sits = Particular.constructAll(Situation.class, r);
        print(sits, "Situation");
        List<DigitalRealization> reals = Particular.constructAll(DigitalRealization.class, r);
        print(reals, "Digital Realization");
        List<DigitalObject> dos = Particular.constructAll(DigitalObject.class, r);
        print(dos, "Digital Object");
        List<InformationObject> ios = Particular.constructAll(InformationObject.class, r);
        print(ios, "Information Object");
        List<Agent> agents = Particular.constructAll(Agent.class, r);
        print(agents, "Agent");
    }

    @Test
    public void testFileRealization() throws Exception {
        File f = new File(new URI("file:///tmp/Vacura_Troncy-ICIP08.pdf"));
        FileSystemRealization fsr = new FileSystemRealization(f, new InformationObject(), r);
        DigitalObject dobj = fsr.getDigitalObject(r);
        fsr.save(r);
        FileSystemRealization constructed1 = Particular.construct(fsr.getURI(), r);
        assertEquals(fsr, constructed1);
        constructed1.printInfo(System.out, r);
        DigitalObject do1 = constructed1.getDigitalObject(r);
        assertEquals("digital object of reconstructed file system realization does not equal original one!", dobj, do1);
        do1.printInfo(System.out, r);
        FileSystemRealization constructed2 = FileSystemRealization.constructByFile(new File(new URI("file:///tmp/Vacura_Troncy-ICIP08.pdf")), r);
        if (constructed2 != null) {
            assertEquals(fsr, constructed2);
            constructed2.printInfo(System.out, r);
            constructed2.changeFileUrl(new File(new URI("file:///tmp/changedFileName.pdf")), r);
            constructed2.save(r);
            DigitalObject do2 = constructed2.getDigitalObject(r);
            do2.printInfo(System.out, r);
            assertEquals(do1, do2);
        } else System.out.println("No realization for file ... found!");
        FileSystemRealization constructed3 = Particular.construct(fsr.getURI(), r);
        constructed3.getDigitalObject(r).printInfo(System.out, r);
        List<FileSystemRealization> fsrs = Particular.constructAll(FileSystemRealization.class, r);
        for (FileSystemRealization fsrx : fsrs) {
            fsrx.printInfo(System.out, r);
        }
    }

    @Test
    public void testInternalRealization() {
        InternalRealization ir1 = new InternalRealization("test text");
        InformationObject io1 = new InformationObject();
        io1.addRealization(ir1);
        io1.save(r);
    }

    @Test
    public void DavidsTest() {
        String filepath = "/home/franz/tmp/x-cosim.html";
        File file = null;
        String messageId = "12345@example.org";
        Mail mail = Mail.constructNewMail(r);
        mail.setMessageId(messageId);
        mail.setBody("This is just a test mail");
        mail.save(r);
        try {
            file = new File(filepath);
        } catch (Exception e) {
            System.out.println("File " + filepath + " not found on local system!");
        }
        try {
            System.out.println(filepath + " received to store...");
            Mail mailCopy = Mail.getByMessageId(messageId, r);
            if (mailCopy == null) {
                System.out.println("DEBUG INFO: Message with id " + messageId + "could not be found!");
            }
            System.out.println("DEBUG INFO: Mail with id " + messageId + " was found in repository. Storing file " + filepath + "...");
            DigitalRealization dr = new FileSystemRealization(file, new InformationObject(), r);
            dr.save(r);
            mailCopy.addAttachment(dr, r);
            mailCopy.save(r);
        } catch (Exception e) {
            System.out.println("DEBUG INFO: Message with id " + messageId + ": error on storing attachment");
            e.printStackTrace();
        }
    }
}
