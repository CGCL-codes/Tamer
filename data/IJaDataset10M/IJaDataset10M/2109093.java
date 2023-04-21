package org.datanucleus.tests.directory.embedded;

import java.util.Iterator;
import java.util.List;
import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.datanucleus.tests.JDOPersistenceTestCase;

public class EmbeddedAsChildTest extends JDOPersistenceTestCase {

    public EmbeddedAsChildTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        clean(Person.class);
    }

    protected void tearDown() throws Exception {
        clean(Person.class);
        super.tearDown();
    }

    public void testPersistPersonWithoutRef() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            pm.makePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNull(daffyDuck.getNotebook());
            assertNotNull(daffyDuck.getComputers());
            assertEquals(0, daffyDuck.getComputers().size());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void testPersistPersonWithEmbeddedAndNestedEmbedded() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Notebook notebook = new Notebook("M-0001", "Daffy's notebook", daffyDuck);
            ComputerCard notebookCard = new ComputerCard("card1", "Foo bar1");
            notebook.getCards().add(notebookCard);
            daffyDuck.setNotebook(notebook);
            Computer apple = new Computer("PC-0001", "Apple");
            Computer sunfire = new Computer("PC-9999", "Sun Fire");
            ComputerCard graphics = new ComputerCard("Graphics", "Matrox");
            ComputerCard sound = new ComputerCard("Sound", "Creative");
            ComputerCard network = new ComputerCard("Network", "Intel");
            apple.getCards().add(graphics);
            apple.getCards().add(sound);
            apple.getCards().add(network);
            daffyDuck.getComputers().add(apple);
            daffyDuck.getComputers().add(sunfire);
            pm.makePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            notebook = daffyDuck.getNotebook();
            assertNotNull(notebook);
            assertEquals("M-0001", notebook.getSerialNumber());
            assertEquals("Daffy's notebook", notebook.getName());
            assertNotNull(notebook.getOwner());
            assertNotNull(notebook.getCards());
            assertEquals(1, notebook.getCards().size());
            assertEquals("card1", notebook.getCards().iterator().next().getName());
            assertEquals(daffyDuck, notebook.getOwner());
            List<Computer> computers = daffyDuck.getComputers();
            assertNotNull(computers);
            assertEquals(2, computers.size());
            for (Computer computer : computers) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals(3, computer.getCards().size());
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals(0, computer.getCards().size());
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void testUpdateEmbeddedObjectOneOne() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Notebook notebook = new Notebook("M-0001", "IBM", daffyDuck);
            ComputerCard notebookCard = new ComputerCard("card1", "Foo bar1");
            notebook.getCards().add(notebookCard);
            daffyDuck.setNotebook(notebook);
            OperatingSystem os = new OperatingSystem("Windows", "Vista");
            notebook.setOperatingSystem(os);
            pm.makePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("IBM", daffyDuck.getNotebook().getName());
            assertNotNull(daffyDuck.getNotebook().getOperatingSystem());
            assertEquals("Vista", daffyDuck.getNotebook().getOperatingSystem().getVersion());
            daffyDuck.getNotebook().setName("Lenovo");
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("Lenovo", daffyDuck.getNotebook().getName());
            assertNotNull(daffyDuck.getNotebook().getOperatingSystem());
            assertEquals("Vista", daffyDuck.getNotebook().getOperatingSystem().getVersion());
            daffyDuck.getNotebook().getOperatingSystem().setVersion("XP");
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("Lenovo", daffyDuck.getNotebook().getName());
            assertNotNull(daffyDuck.getNotebook().getOperatingSystem());
            assertEquals("XP", daffyDuck.getNotebook().getOperatingSystem().getVersion());
            daffyDuck.getNotebook().setOperatingSystem(null);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("Lenovo", daffyDuck.getNotebook().getName());
            assertNull(daffyDuck.getNotebook().getOperatingSystem());
            OperatingSystem newOs = new OperatingSystem("Ubuntu", "9.04");
            daffyDuck.getNotebook().setOperatingSystem(newOs);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("Lenovo", daffyDuck.getNotebook().getName());
            assertNotNull(daffyDuck.getNotebook().getOperatingSystem());
            assertEquals("9.04", daffyDuck.getNotebook().getOperatingSystem().getVersion());
            daffyDuck.setNotebook(null);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNull(daffyDuck.getNotebook());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void testUpdateEmbeddedObjectOneMany() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Computer apple = new Computer("PC-0001", "Apple");
            Computer sunfire = new Computer("PC-9999", "Sun Fire");
            ComputerCard graphics = new ComputerCard("Graphics", "Matrox");
            ComputerCard sound = new ComputerCard("Sound", "Creative");
            ComputerCard network = new ComputerCard("Network", "Intel");
            apple.getCards().add(graphics);
            apple.getCards().add(sound);
            apple.getCards().add(network);
            daffyDuck.getComputers().add(apple);
            daffyDuck.getComputers().add(sunfire);
            pm.makePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getComputers());
            assertEquals(2, daffyDuck.getComputers().size());
            ComputerCard cardToUpdate = null;
            for (Computer computer : daffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Apple", computer.getName());
                    assertEquals(3, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("Graphics")) {
                            cardToUpdate = card;
                            assertEquals("Matrox", card.getDescription());
                        } else if (card.getName().equals("Sound")) {
                            assertEquals("Creative", card.getDescription());
                        } else if (card.getName().equals("Network")) {
                            assertEquals("Intel", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals("Sun Fire", computer.getName());
                    assertEquals(0, computer.getCards().size());
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            cardToUpdate.setDescription("AMD");
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getComputers());
            assertEquals(2, daffyDuck.getComputers().size());
            Computer appleToModify = null;
            ComputerCard soundToDelete = null;
            for (Computer computer : daffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Apple", computer.getName());
                    assertEquals(3, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("Graphics")) {
                            assertEquals("AMD", card.getDescription());
                        } else if (card.getName().equals("Sound")) {
                            appleToModify = computer;
                            soundToDelete = card;
                            assertEquals("Creative", card.getDescription());
                        } else if (card.getName().equals("Network")) {
                            assertEquals("Intel", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals("Sun Fire", computer.getName());
                    assertEquals(0, computer.getCards().size());
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            appleToModify.getCards().remove(soundToDelete);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getComputers());
            assertEquals(2, daffyDuck.getComputers().size());
            Computer sunfireToModify = null;
            for (Computer computer : daffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Apple", computer.getName());
                    assertEquals(2, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("Graphics")) {
                            assertEquals("AMD", card.getDescription());
                        } else if (card.getName().equals("Network")) {
                            assertEquals("Intel", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals("Sun Fire", computer.getName());
                    assertEquals(0, computer.getCards().size());
                    sunfireToModify = computer;
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            ComputerCard scsi = new ComputerCard("SCSI", "Adaptec");
            sunfireToModify.getCards().add(scsi);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getComputers());
            assertEquals(2, daffyDuck.getComputers().size());
            for (Computer computer : daffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Apple", computer.getName());
                    assertEquals(2, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("Graphics")) {
                            assertEquals("AMD", card.getDescription());
                        } else if (card.getName().equals("Network")) {
                            assertEquals("Intel", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals("Sun Fire", computer.getName());
                    assertEquals(1, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("SCSI")) {
                            assertEquals("Adaptec", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void testAttachDetach() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Account account = new Account("dduck", "secret12");
            daffyDuck.setAccount(account);
            Address address = new Address(12345, "D-City", "D-Street");
            ContactData contactData = new ContactData(address, null);
            contactData.getPhoneNumbers().add("+49-123-456-789");
            contactData.getPhoneNumbers().add("+49-987-654-321");
            contactData.setPerson(daffyDuck);
            daffyDuck.setContactData(contactData);
            Notebook notebook = new Notebook("M-0001", "IBM", daffyDuck);
            ComputerCard isdnCard = new ComputerCard("ISDN", "Fritz");
            notebook.getCards().add(isdnCard);
            OperatingSystem os = new OperatingSystem("Windows", "Vista");
            notebook.setOperatingSystem(os);
            daffyDuck.setNotebook(notebook);
            Computer apple = new Computer("PC-0001", "Apple");
            ComputerCard graphics = new ComputerCard("Graphics", "Matrox");
            ComputerCard sound = new ComputerCard("Sound", "Creative");
            ComputerCard network = new ComputerCard("Network", "Intel");
            apple.getCards().add(graphics);
            apple.getCards().add(sound);
            apple.getCards().add(network);
            OperatingSystem macosx = new OperatingSystem("MacOSX", "10");
            apple.setOperatingSystem(macosx);
            Computer sunfire = new Computer("PC-9999", "Sun Fire");
            OperatingSystem solaris = new OperatingSystem("Solaris", "8");
            sunfire.setOperatingSystem(solaris);
            daffyDuck.getComputers().add(apple);
            daffyDuck.getComputers().add(sunfire);
            pm.makePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            Person detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertEquals("Daffy Duck", detachedDaffyDuck.getFullName());
            assertEquals("Daffy", detachedDaffyDuck.getFirstName());
            assertEquals("Duck", detachedDaffyDuck.getLastName());
            assertNotNull(detachedDaffyDuck.getAccount());
            assertEquals("dduck", detachedDaffyDuck.getAccount().getUid());
            assertEquals("secret12", detachedDaffyDuck.getAccount().getPassword());
            assertNotNull(detachedDaffyDuck.getContactData());
            assertNotNull(detachedDaffyDuck.getContactData().getAddress());
            assertEquals(12345, detachedDaffyDuck.getContactData().getAddress().getZip());
            assertEquals("D-City", detachedDaffyDuck.getContactData().getAddress().getCity());
            assertEquals("D-Street", detachedDaffyDuck.getContactData().getAddress().getStreet());
            assertNotNull(detachedDaffyDuck.getContactData().getPhoneNumbers());
            assertEquals(2, detachedDaffyDuck.getContactData().getPhoneNumbers().size());
            assertTrue(detachedDaffyDuck.getContactData().getPhoneNumbers().contains("+49-123-456-789"));
            assertTrue(detachedDaffyDuck.getContactData().getPhoneNumbers().contains("+49-987-654-321"));
            assertEquals(detachedDaffyDuck, detachedDaffyDuck.getContactData().getPerson());
            assertNotNull(detachedDaffyDuck.getNotebook());
            assertEquals("IBM", detachedDaffyDuck.getNotebook().getName());
            assertEquals("M-0001", detachedDaffyDuck.getNotebook().getSerialNumber());
            assertNotNull(detachedDaffyDuck.getNotebook().getOperatingSystem());
            assertEquals("Windows", detachedDaffyDuck.getNotebook().getOperatingSystem().getName());
            assertEquals("Vista", detachedDaffyDuck.getNotebook().getOperatingSystem().getVersion());
            assertNotNull(detachedDaffyDuck.getNotebook().getCards());
            assertEquals(1, detachedDaffyDuck.getNotebook().getCards().size());
            for (ComputerCard card : detachedDaffyDuck.getNotebook().getCards()) {
                if (card.getName().equals("ISDN")) {
                    assertEquals("Fritz", card.getDescription());
                } else {
                    fail("Unexpected card " + card.getName());
                }
            }
            assertNotNull(detachedDaffyDuck.getComputers());
            assertEquals(2, detachedDaffyDuck.getComputers().size());
            for (Computer computer : detachedDaffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Apple", computer.getName());
                    assertEquals(3, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("Graphics")) {
                            assertEquals("Matrox", card.getDescription());
                        } else if (card.getName().equals("Sound")) {
                            assertEquals("Creative", card.getDescription());
                        } else if (card.getName().equals("Network")) {
                            assertEquals("Intel", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                    assertNotNull(computer.getOperatingSystem());
                    assertEquals("MacOSX", computer.getOperatingSystem().getName());
                    assertEquals("10", computer.getOperatingSystem().getVersion());
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    assertEquals("Sun Fire", computer.getName());
                    assertEquals(0, computer.getCards().size());
                    assertNotNull(computer.getOperatingSystem());
                    assertEquals("Solaris", computer.getOperatingSystem().getName());
                    assertEquals("8", computer.getOperatingSystem().getVersion());
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            detachedDaffyDuck.getContactData().getAddress().setZip(23456);
            detachedDaffyDuck.getContactData().getAddress().setStreet("D-Street2");
            detachedDaffyDuck.getContactData().getPhoneNumbers().remove("+49-123-456-789");
            detachedDaffyDuck.getContactData().getPhoneNumbers().add("+49-000-000-000");
            detachedDaffyDuck.getAccount().setPassword("secret90");
            detachedDaffyDuck.setFirstName("Daffy2");
            detachedDaffyDuck.getNotebook().setName("Lenovo");
            detachedDaffyDuck.getNotebook().setOperatingSystem(new OperatingSystem("Ubuntu", "9.04"));
            detachedDaffyDuck.getNotebook().getCards().iterator().next().setDescription("AVM");
            detachedDaffyDuck.getNotebook().getCards().add(new ComputerCard("Modem", "56k"));
            Iterator<Computer> computerIterator = detachedDaffyDuck.getComputers().iterator();
            while (computerIterator.hasNext()) {
                Computer computer = computerIterator.next();
                if (computer.getSerialNumber().equals("PC-0001")) {
                    computer.setName("Pear");
                    computer.getCards().clear();
                    computer.getOperatingSystem().setVersion("11");
                } else if (computer.getSerialNumber().equals("PC-9999")) {
                    computerIterator.remove();
                }
            }
            Computer desktop = new Computer("PC-0002", "self-made");
            desktop.setOperatingSystem(new OperatingSystem("FreeBSD", "7.2"));
            desktop.getCards().add(new ComputerCard("SCSI", "Adaptec"));
            detachedDaffyDuck.getComputers().add(desktop);
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().setGroup(FetchPlan.ALL);
            pm.getFetchPlan().setMaxFetchDepth(5);
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedDaffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy2", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNotNull(daffyDuck.getAccount());
            assertEquals("dduck", daffyDuck.getAccount().getUid());
            assertEquals("secret90", daffyDuck.getAccount().getPassword());
            assertNotNull(daffyDuck.getContactData());
            assertNotNull(daffyDuck.getContactData().getAddress());
            assertEquals(23456, daffyDuck.getContactData().getAddress().getZip());
            assertEquals("D-City", daffyDuck.getContactData().getAddress().getCity());
            assertEquals("D-Street2", daffyDuck.getContactData().getAddress().getStreet());
            assertNotNull(daffyDuck.getContactData().getPhoneNumbers());
            assertEquals(2, daffyDuck.getContactData().getPhoneNumbers().size());
            assertTrue(daffyDuck.getContactData().getPhoneNumbers().contains("+49-000-000-000"));
            assertTrue(daffyDuck.getContactData().getPhoneNumbers().contains("+49-987-654-321"));
            assertEquals(daffyDuck, daffyDuck.getContactData().getPerson());
            assertNotNull(daffyDuck.getNotebook());
            assertEquals("Lenovo", daffyDuck.getNotebook().getName());
            assertEquals("M-0001", daffyDuck.getNotebook().getSerialNumber());
            assertNotNull(daffyDuck.getNotebook().getOperatingSystem());
            assertEquals("Ubuntu", daffyDuck.getNotebook().getOperatingSystem().getName());
            assertEquals("9.04", daffyDuck.getNotebook().getOperatingSystem().getVersion());
            assertNotNull(daffyDuck.getNotebook().getCards());
            assertEquals(2, daffyDuck.getNotebook().getCards().size());
            for (ComputerCard card : daffyDuck.getNotebook().getCards()) {
                if (card.getName().equals("ISDN")) {
                    assertEquals("AVM", card.getDescription());
                } else if (card.getName().equals("Modem")) {
                    assertEquals("56k", card.getDescription());
                } else {
                    fail("Unexpected card " + card.getName());
                }
            }
            assertNotNull(daffyDuck.getComputers());
            assertEquals(2, daffyDuck.getComputers().size());
            for (Computer computer : daffyDuck.getComputers()) {
                if (computer.getSerialNumber().equals("PC-0001")) {
                    assertEquals("Pear", computer.getName());
                    assertEquals(0, computer.getCards().size());
                    assertNotNull(computer.getOperatingSystem());
                    assertEquals("MacOSX", computer.getOperatingSystem().getName());
                    assertEquals("11", computer.getOperatingSystem().getVersion());
                } else if (computer.getSerialNumber().equals("PC-0002")) {
                    assertEquals("self-made", computer.getName());
                    assertEquals(1, computer.getCards().size());
                    for (ComputerCard card : computer.getCards()) {
                        if (card.getName().equals("SCSI")) {
                            assertEquals("Adaptec", card.getDescription());
                        } else {
                            fail("Unexpected card " + card.getName());
                        }
                    }
                    assertNotNull(computer.getOperatingSystem());
                    assertEquals("FreeBSD", computer.getOperatingSystem().getName());
                    assertEquals("7.2", computer.getOperatingSystem().getVersion());
                } else {
                    fail("Unexpected computer " + computer.getName());
                }
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
