package org.azrul.epice.test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.azrul.epice.dao.AttachmentDAO;
import org.azrul.epice.dao.factory.AttachmentDAOFactory;
import org.azrul.epice.dao.FileRepositoryDAO;
import org.azrul.epice.dao.factory.FileRepositoryDAOFactory;
import org.azrul.epice.dao.ItemDAO;
import org.azrul.epice.dao.factory.ItemDAOFactory;
import org.azrul.epice.dao.PersonDAO;
import org.azrul.epice.dao.factory.PersonDAOFactory;
import org.azrul.epice.domain.Attachment;
import org.azrul.epice.domain.FileRepository;
import org.azrul.epice.domain.Item;
import org.azrul.epice.domain.Person;
import org.azrul.epice.manager.NewItemManager;
import org.azrul.epice.util.DBUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azrul
 */
public class PgNewItemTest {

    public PgNewItemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void doTestSentNewItems() {
        File file = null;
        try {
            file = File.createTempFile("epice", ".yap");
        } catch (IOException ex) {
            Logger.getLogger(PgLoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        DBUtil.setYap(file.getPath());
        PersonDAO personDAO = PersonDAOFactory.getInstance();
        Person goofy = personDAO.create("goofy.goof@company.com", "Goofy Goof", "goofy", "abc123", "770202-15-0666", "0179986720", "R&D", "Billadam, 63000 Cyberjaya");
        Person minnie = personDAO.create("minnie.mouse@company.com", "Minnie Mouse", "minnie", "abc123", "770202-15-0667", "0179986721", "R&D", "Billadam, 63000 Cyberjaya");
        Person donald = personDAO.create("donald.duck@company.com", "Donald Duck", "donald", "abc123", "770202-15-0668", "0179986722", "R&D", "Billadam, 63000 Cyberjaya");
        Person mickey = personDAO.create("mickey.mouse@company.com", "Mickey Mouse", "mickey", "abc123", "770202-15-0669", "0179986723", "R&D", "Billadam, 63000 Cyberjaya");
        ItemDAO itemDAO = ItemDAOFactory.getInstance();
        StringBuilder toUserEmails = new StringBuilder();
        toUserEmails.append(donald.getEmail());
        toUserEmails.append(",");
        toUserEmails.append(mickey.getEmail());
        GregorianCalendar calDeadline = (GregorianCalendar) GregorianCalendar.getInstance();
        calDeadline.clear();
        calDeadline.set(Calendar.DAY_OF_MONTH, 27);
        calDeadline.set(Calendar.MONTH, 5);
        calDeadline.set(Calendar.YEAR, 2008);
        calDeadline.set(Calendar.HOUR_OF_DAY, 18);
        FileRepositoryDAO fileRepoDAO = FileRepositoryDAOFactory.getInstance();
        AttachmentDAO attachmentDAO = AttachmentDAOFactory.getInstance();
        FileRepository fileRepo = null;
        try {
            File f1 = File.createTempFile("epice_attachment", ".txt");
            Attachment attachment1 = attachmentDAO.createWithoutPersisting(f1.getPath(), "minnie.mouse@company.com");
            File f2 = File.createTempFile("epice_attachment", ".txt");
            Attachment attachment2 = attachmentDAO.createWithoutPersisting(f2.getPath(), "minnie.mouse@company.com");
            fileRepo = fileRepoDAO.create(minnie, "A file repo");
            fileRepoDAO.addNewAttachment(fileRepo, attachment1);
            fileRepoDAO.addNewAttachment(fileRepo, attachment2);
        } catch (IOException ioe) {
            Logger.getLogger(PgLoginTest.class.getName()).log(Level.SEVERE, null, ioe);
        }
        List<String> linkList = new ArrayList<String>();
        linkList.add("http://www.yahoo.com");
        linkList.add("http://www.slashdot.org");
        NewItemManager newItemManager = new NewItemManager();
        Set<Item> itemsV = newItemManager.createNewItemsWithoutPersisting(minnie, toUserEmails.toString(), goofy.getEmail(), "Some, Tags", calDeadline.getTime(), "Do something", "Do something", fileRepo, null, linkList);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        for (Item item : itemsV) {
            try {
                Assert.assertTrue(item.getDeadLine().equals(df.parse("2008-06-27 06:00 PM")));
            } catch (ParseException ex) {
                Logger.getLogger(PgNewItemTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            Assert.assertTrue(item.getDescription().equals("Do something"));
            Assert.assertTrue(item.getFileRepository().equals(fileRepo));
            Assert.assertTrue(item.getFromUser().equals(minnie));
            Assert.assertTrue(item.getStatus().equals("SENT-UNCONFIRMED"));
            Assert.assertTrue(item.getSubject().equals("Do something"));
            Assert.assertTrue(item.getSupervisors().contains(goofy));
            Assert.assertTrue(item.getTags().contains("Some"));
            Assert.assertTrue(item.getTags().contains("Tags"));
            Assert.assertTrue(item.getToUser().equals(donald) || item.getToUser().equals(mickey));
        }
        Set<Item> itemsP = newItemManager.createAndSentNewItems(minnie, toUserEmails.toString(), goofy.getEmail(), "Some, Tags", calDeadline.getTime(), "Do something", "Do something", fileRepo, null, linkList);
        Set<Item> itemsPFromDB = new HashSet<Item>();
        for (Item item : itemsP) {
            itemsPFromDB.add(itemDAO.findItemById(item.getId()));
        }
        for (Item item : itemsPFromDB) {
            try {
                Assert.assertTrue(item.getDeadLine().equals(df.parse("2008-06-27 06:00 PM")));
            } catch (ParseException ex) {
                Logger.getLogger(PgNewItemTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            Assert.assertTrue(item.getDescription().equals("Do something"));
            Assert.assertTrue(item.getFileRepository().equals(fileRepo));
            Assert.assertTrue(item.getFromUser().equals(minnie));
            Assert.assertTrue(item.getStatus().equals("SENT-UNCONFIRMED"));
            Assert.assertTrue(item.getSubject().equals("Do something"));
            Assert.assertTrue(item.getSupervisors().contains(goofy));
            Assert.assertTrue(item.getTags().contains("Some"));
            Assert.assertTrue(item.getTags().contains("Tags"));
            Assert.assertTrue(item.getToUser().equals(donald) || item.getToUser().equals(mickey));
        }
    }
}
