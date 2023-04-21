package org.hackystat.socnet.server.resource.users;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.db.UserDBImpl;
import org.hackystat.socnet.server.resource.registration.InvalidRegistrationRequestException;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUserIndex;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUserRef;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUsers;
import org.hackystat.socnet.utils.JAXBHelper;
import org.hackystat.utilities.email.ValidateEmailSyntax;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.utilities.tstamp.Tstamp;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.w3c.dom.Document;
import static org.hackystat.socnet.server.ServerProperties.XML_DIR_KEY;
import static org.hackystat.socnet.server.ServerProperties.ADMIN_EMAIL_KEY;
import static org.hackystat.socnet.server.ServerProperties.ADMIN_PASSWORD_KEY;
import static org.hackystat.socnet.server.ServerProperties.TEST_DOMAIN_KEY;

/**
 * 
 * @author Rachel Shadoan
 */
public class UserManager {

    JAXBContext jaxbContext;

    Server server;

    /** The UserDBImpl associated with this server. */
    UserDBImpl dbManager;

    /** The XMLUserIndex open tag. */
    public static final String userIndexOpenTag = "<XMLUserIndex>";

    /** The XMLUserIndex close tag. */
    public static final String userIndexCloseTag = "</XMLUserIndex>";

    /** The initial size for Collection instances that hold the XMLUsers. */
    private static final int userSetSize = 127;

    /** The in-memory repository of XMLUsers, keyed by Email. */
    private Map<String, XMLUser> email2user = new HashMap<String, XMLUser>(userSetSize);

    /** The in-memory repository of XMLUser XML strings, keyed by XMLUser. */
    private Map<XMLUser, String> user2xml = new HashMap<XMLUser, String>(userSetSize);

    /** The in-memory repository of XMLUserRef XML strings, keyed by XMLUser. */
    private Map<XMLUser, String> user2ref = new HashMap<XMLUser, String>(userSetSize);

    public UserManager(Server server) {
        this.server = server;
        this.dbManager = (UserDBImpl) server.getContext().getAttributes().get("UserDB");
        try {
            this.jaxbContext = JAXBContext.newInstance(org.hackystat.socnet.server.resource.users.jaxb.ObjectFactory.class);
            loadDefaultUsers();
            initializeCache();
            initializeAdminUser();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Exception during UserManager initialization processing";
            server.getLogger().warning(msg + "\n" + StackTrace.toString(e));
            throw new RuntimeException(msg, e);
        }
    }

    /**
   * Loads the default XMLUsers from the defaults file and adds them to the database.
   * 
   * @throws Exception If problems occur.
   */
    private final void loadDefaultUsers() throws Exception {
        File defaultsFile = findDefaultsFile();
        if (defaultsFile.exists()) {
            server.getLogger().info("Loading User defaults from " + defaultsFile.getPath());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            XMLUsers users = (XMLUsers) unmarshaller.unmarshal(defaultsFile);
            for (XMLUser user : users.getXMLUser()) {
                this.dbManager.storeUser(user, this.makeUser(user), this.makeUserRefString(user));
            }
        }
    }

    /**
   * Read in all XMLUsers from the database and initialize the in-memory cache.
   */
    private final void initializeCache() {
        try {
            XMLUserIndex index = makeUserIndex(this.dbManager.getUserIndex());
            for (XMLUserRef ref : index.getXMLUserRef()) {
                String email = ref.getEmail();
                String userString = this.dbManager.getUser(email);
                XMLUser user = makeUser(userString);
                this.updateCache(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            server.getLogger().warning("Failed to initialize users " + StackTrace.toString(e));
        }
    }

    /**
   * Ensures a XMLUser exists with the admin role given the data in the sensorbase.properties file.
   * The admin password will be reset to what was in the sensorbase.properties file. Note that the
   * "admin" role is managed non-persistently: it is read into the cache from the
   * sensorbase.properties at startup, and any persistently stored values for it are ignored. This,
   * of course, will eventually cause confusion.
   * 
   * @throws Exception if problems creating the XML string representations of the admin user.
   */
    private final void initializeAdminUser() throws Exception {
        String adminEmail = server.getServerProperties().get(ADMIN_EMAIL_KEY);
        String adminPassword = server.getServerProperties().get(ADMIN_PASSWORD_KEY);
        for (XMLUser user : this.email2user.values()) {
            user.setRole("basic");
        }
        if (this.email2user.containsKey(adminEmail)) {
            XMLUser user = this.email2user.get(adminEmail);
            user.setPassword(adminPassword);
            user.setRole("admin");
        } else {
            XMLUser admin = new XMLUser();
            admin.setEmail(adminEmail);
            admin.setPassword(adminPassword);
            admin.setRole("admin");
            this.updateCache(admin);
        }
    }

    /**
   * Updates the in-memory cache with information about this XMLUser.
   * 
   * @param user The user to be added to the cache.
   * @throws Exception If problems occur updating the cache.
   */
    private final void updateCache(XMLUser user) throws Exception {
        if (user.getLastMod() == null) {
            user.setLastMod(Tstamp.makeTimestamp());
        }
        updateCache(user, this.makeUser(user), this.makeUserRefString(user));
    }

    /**
   * Updates the cache given all the XMLUser representations.
   * 
   * @param user The XMLUser.
   * @param userXml The XMLUser as an XML string.
   * @param userRef The XMLUser as an XML reference.
   */
    private void updateCache(XMLUser user, String userXml, String userRef) {
        this.email2user.put(user.getEmail(), user);
        this.user2xml.put(user, userXml);
        this.user2ref.put(user, userRef);
    }

    /**
   * Checks ServerProperties for the XML_DIR property. If this property is null, returns the File
   * for ./xml/defaults/sensordatatypes.defaults.xml.
   * 
   * @return The File instance (which might not point to an existing file.)
   */
    private File findDefaultsFile() {
        String defaultsPath = "/defaults/users.defaults.xml";
        String xmlDir = server.getServerProperties().get(XML_DIR_KEY);
        return (xmlDir == null) ? new File(System.getProperty("user.dir") + "/xml" + defaultsPath) : new File(xmlDir + defaultsPath);
    }

    /**
   * Returns the XML string containing the XMLUserIndex with all defined XMLUsers. Uses the
   * in-memory cache of XMLUserRef strings.
   * 
   * @return The XML string providing an index to all current XMLUsers.
   */
    public synchronized String getUserIndex() {
        StringBuilder builder = new StringBuilder(512);
        builder.append(userIndexOpenTag);
        for (String ref : this.user2ref.values()) {
            builder.append(ref);
        }
        builder.append(userIndexCloseTag);
        return builder.toString();
    }

    /**
   * Updates the Manager with this XMLUser. Any old definition is overwritten.
   * 
   * @param user The XMLUser.
   */
    public synchronized void putUser(XMLUser user) {
        try {
            user.setLastMod(Tstamp.makeTimestamp());
            String xmlUser = this.makeUser(user);
            String xmlRef = this.makeUserRefString(user);
            this.updateCache(user, xmlUser, xmlRef);
            this.dbManager.storeUser(user, xmlUser, xmlRef);
        } catch (Exception e) {
            e.printStackTrace();
            server.getLogger().warning("Failed to put User" + StackTrace.toString(e));
        }
    }

    /**
   * Ensures that the passed XMLUser is no longer present in this Manager, and deletes all Projects
   * associated with this user.
   * 
   * @param email The email address of the XMLUser to remove if currently present.
   */
    public synchronized void deleteUser(String email) {
        XMLUser user = this.email2user.get(email);
        if (user != null) {
            this.email2user.remove(email);
            this.user2xml.remove(user);
            this.user2ref.remove(user);
        }
        this.dbManager.deleteUser(email);
    }

    /**
   * Returns the XMLUser associated with this email address if they are currently registered, or
   * null if not found.
   * 
   * @param email The email address
   * @return The XMLUser, or null if not found.
   */
    public synchronized XMLUser getUser(String email) {
        return (email == null) ? null : email2user.get(email);
    }

    /**
   * Returns the XMLUser Xml String associated with this email address if they are registered, or
   * null if user not found.
   * 
   * @param email The email address
   * @return The XMLUser XML string, or null if not found.
   */
    public synchronized String getUserString(String email) {
        XMLUser user = email2user.get(email);
        return (user == null) ? null : user2xml.get(user);
    }

    /**
   * Returns a set containing the current XMLUser instances. For thread safety, a fresh Set of
   * XMLUsers is built each time this is called.
   * 
   * @return A Set containing the current XMLUsers.
   */
    public synchronized Set<XMLUser> getUsers() {
        Set<XMLUser> userSet = new HashSet<XMLUser>(userSetSize);
        userSet.addAll(this.email2user.values());
        return userSet;
    }

    /**
   * Returns true if the XMLUser as identified by their email address is known to this Manager.
   * 
   * @param email The email address of the XMLUser of interest.
   * @return True if found in this Manager.
   */
    public synchronized boolean isUser(String email) {
        return (email != null) && email2user.containsKey(email);
    }

    /**
   * Returns true if the XMLUser as identified by their email address and password is known to this
   * Manager.
   * 
   * @param email The email address of the XMLUser of interest.
   * @param password The password of this user.
   * @return True if found in this Manager.
   */
    public synchronized boolean isUser(String email, String password) {
        XMLUser user = this.email2user.get(email);
        return (user != null) && (password != null) && (password.equals(user.getPassword()));
    }

    /**
   * Returns true if email is a defined XMLUser with Admin privileges.
   * 
   * @param email An email address.
   * @return True if email is a XMLUser with Admin privileges.
   */
    public synchronized boolean isAdmin(String email) {
        return (email != null) && email2user.containsKey(email) && email.equals(server.getServerProperties().get(ADMIN_EMAIL_KEY));
    }

    /**
   * Returns true if the passed user is a test user. This is defined as a XMLUser whose email
   * address uses the TEST_DOMAIN.
   * 
   * @param user The user.
   * @return True if the user is a test user.
   */
    public synchronized boolean isTestUser(XMLUser user) {
        return user.getEmail().endsWith(server.getServerProperties().get(TEST_DOMAIN_KEY));
    }

    /**
   * Registers a XMLUser, given their email address. If a XMLUser with the passed email address
   * exists, then return the previously registered XMLUser. Otherwise create a new XMLUser and
   * return it. If the email address ends with the test domain, then the password will be the email.
   * Otherwise, a unique, randomly generated 12 character key is generated as the password. Defines
   * the Default Project for each new user.
   * 
   * @param email The email address for the user.
   * @return The retrieved or newly created XMLUser.
   */
    public synchronized XMLUser registerUser(Representation entity) throws JAXBException, IOException, Exception {
        XMLUser user = makeUser(entity.getText());
        validateEmail(user);
        String email = user.getEmail();
        for (XMLUser u : this.email2user.values()) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        if (user.getPassword() == null) {
            String password = email.endsWith(server.getServerProperties().get(TEST_DOMAIN_KEY)) ? email : PasswordGenerator.make();
            user.setPassword(password);
        }
        this.putUser(user);
        return user;
    }

    /**
   * Takes a String encoding of a XMLUser in XML format and converts it to an instance.
   * 
   * @param xmlString The XML string representing a XMLUser
   * @return The corresponding XMLUser instance.
   * @throws Exception If problems occur during unmarshalling.
   */
    public final synchronized XMLUser makeUser(String xmlString) throws Exception {
        return (XMLUser) JAXBHelper.unmarshall(xmlString, jaxbContext);
    }

    /**
   * Takes a String encoding of a XMLUserIndex in XML format and converts it to an instance.
   * 
   * @param xmlString The XML string representing a XMLUserIndex.
   * @return The corresponding XMLUserIndex instance.
   * @throws Exception If problems occur during unmarshalling.
   */
    public final synchronized XMLUserIndex makeUserIndex(String xmlString) throws Exception {
        Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        return (XMLUserIndex) unmarshaller.unmarshal(new StringReader(xmlString));
    }

    /**
   * Returns the passed XMLUser instance as a String encoding of its XML representation. Final
   * because it's called in constructor.
   * 
   * @param user The XMLUser instance.
   * @return The XML String representation.
   * @throws Exception If problems occur during translation.
   */
    public final synchronized String makeUser(XMLUser user) throws Exception {
        Marshaller marshaller = jaxbContext.createMarshaller();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        marshaller.marshal(user, doc);
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        String xmlString = writer.toString();
        xmlString = xmlString.substring(xmlString.indexOf('>') + 1);
        return xmlString;
    }

    /**
   * Returns the passed XMLUser instance as a String encoding of its XML representation as a
   * XMLUserRef object. Final because it's called in constructor.
   * 
   * @param user The XMLUser instance.
   * @return The XML String representation of it as a XMLUserRef
   * @throws Exception If problems occur during translation.
   */
    public final synchronized String makeUserRefString(XMLUser user) throws Exception {
        XMLUserRef ref = makeUserRef(user);
        Marshaller marshaller = jaxbContext.createMarshaller();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        marshaller.marshal(ref, doc);
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        String xmlString = writer.toString();
        xmlString = xmlString.substring(xmlString.indexOf('>') + 1);
        return xmlString;
    }

    /**
   * Returns a XMLUserRef instance constructed from a XMLUser instance.
   * 
   * @param user The XMLUser instance.
   * @return A XMLUserRef instance.
   */
    public synchronized XMLUserRef makeUserRef(XMLUser user) {
        XMLUserRef ref = new XMLUserRef();
        ref.setEmail(user.getEmail());
        ref.setHref(this.server.getHostName() + "users/" + user.getEmail());
        return ref;
    }

    public void validateEmail(XMLUser user) throws JAXBException, IOException, InvalidRegistrationRequestException {
        if (user.getEmail() == null || "".equals(user.getEmail())) {
            throw new InvalidRegistrationRequestException("Invalid registration " + "request: empty email");
        }
        if (!ValidateEmailSyntax.isValid(user.getEmail())) {
            throw new InvalidRegistrationRequestException("Invalid registration " + "request: email appears to be invalid.");
        }
        return;
    }

    public Representation getRepresentation(Object xmlObject) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
        return new StringRepresentation(JAXBHelper.marshall(xmlObject, jaxbContext));
    }
}
