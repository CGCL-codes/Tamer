package cz.zcu.kiv.jet.jpa.service;

import static org.junit.Assert.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.databene.contiperf.PerfTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import cz.zcu.kiv.jet.jpa.domain.Blip;
import cz.zcu.kiv.jet.jpa.domain.Group;
import cz.zcu.kiv.jet.jpa.domain.Message;
import cz.zcu.kiv.jet.jpa.domain.Participant;
import cz.zcu.kiv.jet.jpa.domain.User;
import cz.zcu.kiv.jet.jpa.exception.ObjectAlreadyExistsException;
import cz.zcu.kiv.jet.jpa.exception.BadAttributesException;
import cz.zcu.kiv.jet.jpa.exception.ObjectNotFoundException;
import cz.zcu.kiv.jet.jpa.exception.ObjectNotPersistException;

@ContextConfiguration({ "/appctx-main.xml", "/appctx-orm.xml" })
@PerfTest(invocations = 1, threads = 1)
public class MessageServiceTestTDD extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MessageService messageService;

    /** User service dependency. */
    @Autowired
    private UserService userService;

    private Set<Participant> particip = new HashSet<Participant>();

    private Random rand = new Random();

    @Test(expected = ObjectNotFoundException.class)
    public void testGetMessageObjectNotFound() {
        assertNull("Message isnt null", messageService.getMessage(-1));
    }

    @Test
    public void testGetMessageObjectFound() {
        User user = createUser("john1", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        Message msg = createMessage(particip);
        assertEquals(msg.getId(), (messageService.getMessage(msg.getId())).getId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetBlipObjectNotFound() {
        assertNull("Message isnt null", messageService.getBlip(-1));
    }

    @Test
    public void testGetBlipObjectFound() {
        User user = createUser("john1", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        Message msg = createMessage(particip);
        List<Blip> blips = createBlips(user, msg);
        assertEquals(blips.get(0).getId(), (messageService.getBlip(blips.get(0).getId()).getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHeadBlipsByUserIllegalArgument() {
        assertNull("Blips arent null", messageService.getHeadBlipsByUser(null));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetHeadBlipsByUserObjectsNotFound() {
        assertNull("Blips arent null", messageService.getHeadBlipsByUser(new User()));
    }

    @Test
    public void testGetHeadBlipsByUserObjectsFound() {
        User user = createUser("david1", "password", "Fore", "david.fore@example.org");
        particip.add(user);
        Message msg1 = createMessage(particip);
        Message msg2 = createMessage(particip);
        List<Blip> blips1 = createBlips(user, msg1);
        List<Blip> blips2 = createBlips(user, msg2);
        List<Blip> blipsZDatabaze = messageService.getHeadBlipsByUser(user);
        assertEquals(2, blipsZDatabaze.size());
        assertEquals(blips1.get(0).getId(), blipsZDatabaze.get(0).getId());
        assertEquals(blips2.get(0).getId(), blipsZDatabaze.get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHeadBlipsByGroupIllegalArgument() {
        assertNull("Blips arent null", messageService.getHeadBlipsByGroup(null));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetHeadBlipsByGroupObjectsNotFound() {
        assertNull("Blips arent null", messageService.getHeadBlipsByGroup(new Group()));
    }

    @Test
    public void testGetHeadBlipsByGroupObjectsFound() {
        User user1 = createUser("david2", "password", "Fore", "david.fore@example.org");
        User user2 = createUser("david3", "password", "Fore", "david.fore@example.org");
        Group group = createGroup("group1");
        particip.add(group);
        Message msg1 = createMessage(particip);
        Message msg2 = createMessage(particip);
        List<Blip> blips1 = createBlips(user1, msg1);
        List<Blip> blips2 = createBlips(user2, msg2);
        List<Blip> blipsZDatabaze = messageService.getHeadBlipsByGroup(group);
        assertEquals(2, blipsZDatabaze.size());
        assertEquals(blips1.get(0).getId(), blipsZDatabaze.get(0).getId());
        assertEquals(blips2.get(0).getId(), blipsZDatabaze.get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHeadBlipsMyGroupIllegalArgument() {
        assertNull("Blips arent null", messageService.getHeadBlipsByMyGroup(null));
    }

    @Test(expected = ObjectNotPersistException.class)
    public void testGetHeadBlipsMyGroupObjectsNotFound() {
        assertNull("Blips arent null", messageService.getHeadBlipsByMyGroup(new User()));
    }

    @Test
    public void testGetHeadBlipsMyGroupObjectsFound() {
        User user = createUser("david4", "password", "Fore", "david.fore@example.org");
        Group group = createGroup("group2");
        particip.add(group);
        userService.addUserToGroup(user, group);
        Message msg1 = createMessage(particip);
        Message msg2 = createMessage(particip);
        List<Blip> blips1 = createBlips(user, msg1);
        List<Blip> blips2 = createBlips(user, msg2);
        List<Blip> blipsZDatabaze = messageService.getHeadBlipsByMyGroup(user);
        assertEquals(2, blipsZDatabaze.size());
        assertEquals(blips1.get(0).getId(), blipsZDatabaze.get(0).getId());
        assertEquals(blips2.get(0).getId(), blipsZDatabaze.get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllBlipsByRootMessageIllegalArgument() {
        assertNull("Blips arent null", messageService.getAllBlipsByRootMessage(null));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetAllBlipsByRootMessageObjectsNotFound() {
        assertNull("Blips arent null", messageService.getAllBlipsByRootMessage(new Message()));
    }

    @Test
    public void testGetAllBlipsByRootMessageObjectFound() {
        List<User> users = new ArrayList<User>();
        User user = createUser("john3", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message msg = createMessage(particip);
        List<Blip> blips = createBlips(user, msg);
        List<Blip> blipsZDatabaze = messageService.getAllBlipsByRootMessage(msg);
        assertEquals(2, blipsZDatabaze.size());
        assertEquals(blips.get(0).getId(), blipsZDatabaze.get(0).getId());
        assertEquals(blips.get(1).getId(), blipsZDatabaze.get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFulltextSearchIllegalArgument() {
        System.out.println(messageService.fulltextSearch(null));
        assertNull("Argument cant be null.", messageService.fulltextSearch(null));
    }

    @Test
    public void testFulltextSearchOK() {
        User user = createUser("john3", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        Message msg = createMessage(particip);
        List<Blip> blips = new ArrayList<Blip>();
        int i = rand.nextInt() * rand.nextInt();
        Blip blip = new Blip();
        blip.setAutor(user);
        blip.setContents("test" + i);
        blip.setRootMessage(msg);
        blips.add(messageService.addBlip(blip));
        Blip blip1 = new Blip();
        blip1.setAutor(user);
        blip1.setContents("sdvstest" + i);
        blip1.setRootMessage(msg);
        blips.add(messageService.addBlip(blip1));
        assertEquals(blips.get(0).getId(), messageService.fulltextSearch("test" + i).get(0).getId());
        assertEquals(blips.get(1).getId(), messageService.fulltextSearch("test" + i).get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMessageIllegalArgument() {
        assertNull("Argument cant be null.", messageService.addMessage(null));
    }

    @Test(expected = BadAttributesException.class)
    public void testAddMessageBadAttributes() {
        assertNull("Attributes of Argument cant be null.", messageService.addMessage(new Message()));
    }

    @Test
    public void testAddMessageObjectAlreadyExists() {
        List<User> users = new ArrayList<User>();
        User user = createUser("john", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message msg = new Message();
        Date date = new Date(10, 10, 22);
        msg.setCreated(date);
        msg.setParticipants(particip);
        msg.setUpdated(date);
        messageService.addMessage(msg);
        Message msg1 = new Message();
        Date date1 = new Date(10, 10, 22);
        msg1.setCreated(date1);
        msg1.setParticipants(particip);
        msg1.setUpdated(date1);
        messageService.addMessage(msg1);
        assertEquals(msg1.getId(), (messageService.getMessage(msg1.getId())).getId());
    }

    @Test
    public void testAddMessageObjectAdded() {
        List<User> users = new ArrayList<User>();
        User user = createUser("john4", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message msg = createMessage(particip);
        assertNotNull(messageService.getMessage(msg.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBlipIllegalArgument() {
        assertNull("Argument cant be null.", messageService.addBlip(null));
    }

    @Test(expected = BadAttributesException.class)
    public void testAddBlipBadAttributes() {
        assertNull("Attribute Content of Argument cant be null.", messageService.addBlip(new Blip()));
    }

    @Test
    public void testAddBlipObjectAdded() {
        List<User> users = new ArrayList<User>();
        User user = createUser("john4", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message msg = createMessage(particip);
        List<Blip> blips = createBlips(user, msg);
        List<Blip> blipsZDatabaze = messageService.getAllBlipsByRootMessage(msg);
        assertEquals(2, blipsZDatabaze.size());
        assertEquals(blips.get(0).getId(), blipsZDatabaze.get(0).getId());
        assertEquals(blips.get(1).getId(), blipsZDatabaze.get(1).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteBlipsIllegalArgument() {
        messageService.deleteBlips(null);
    }

    @Test
    public void testDeleteBlipsObjectDeleted() {
        List<User> users = new ArrayList<User>();
        User user = createUser("john15", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message msg = createMessage(particip);
        List<Blip> blips = createBlips(user, msg);
        messageService.deleteBlips(blips);
        List<Blip> blipsZDatabeze = messageService.getAllBlipsByRootMessage(msg);
        assertTrue(blipsZDatabeze.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMessagesIllegalArgument() {
        messageService.deleteMessages(null);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testDeleteMessagesNotFound() {
        List<Message> mess = new ArrayList<Message>();
        mess.add(new Message());
        messageService.deleteMessages(mess);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testDeleteMessagesObjectDeleted() {
        List<Message> messages = new ArrayList<Message>();
        List<User> users = new ArrayList<User>();
        User user = createUser("john6", "password", "Doe", "john.doe@example.org");
        particip.add(user);
        users.add(user);
        Message mess1 = createMessage(particip);
        Message mess2 = createMessage(particip);
        messages.add(mess1);
        messages.add(mess2);
        messageService.deleteMessages(messages);
        assertNull(messageService.getMessage(mess1.getId()).getId());
        assertNull(messageService.getMessage(mess2.getId()).getId());
    }

    private Message createMessage(Set<Participant> part) {
        Message msg = new Message();
        Date date = new Date(System.currentTimeMillis());
        msg.setCreated(date);
        msg.setParticipants(part);
        msg.setUpdated(date);
        return messageService.addMessage(msg);
    }

    private User createUser(String username, String password, String name, String email) {
        User u = new User();
        u.setUsername(username + rand.nextInt());
        u.setPassword(password + rand.nextInt());
        u.setName(name + rand.nextInt());
        u.setEmail(email);
        u.setSurname("Jan" + rand.nextInt());
        return userService.storeUser(u);
    }

    private Group createGroup(String name) {
        Group g = new Group();
        g.setName(name + rand.nextInt());
        return userService.storeGroup(g);
    }

    private List<Blip> createBlips(final User user, Message rootMessage) {
        List<Blip> blips = new ArrayList<Blip>();
        Blip blip = new Blip();
        blip.setAutor(user);
        blip.setContents("testovaci zprava, head blip");
        blip.setRootMessage(rootMessage);
        blips.add(messageService.addBlip(blip));
        Blip blip1 = new Blip();
        blip1.setAutor(user);
        blip1.setContents("testovaci zprava, notHeadBlip");
        blip1.setParent(blip);
        blip1.setRootMessage(rootMessage);
        blips.add(messageService.addBlip(blip1));
        return blips;
    }
}
