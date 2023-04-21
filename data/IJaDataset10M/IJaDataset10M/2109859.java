package org.dspace.content.packager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.crosswalk.CrosswalkException;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Create EPersons and Groups from a file of external representations.
 * 
 * @author mwood
 */
public class RoleIngester implements PackageIngester {

    private static final Logger log = LoggerFactory.getLogger(RoleIngester.class);

    /**
     * Common code to ingest roles from a Document.
     * 
     * @param context
     *          DSpace Context
     * @param parent
     *          the Parent DSpaceObject
     * @param document
     *          the XML Document
     * @throws SQLException
     * @throws AuthorizeException
     * @throws PackageException
     */
    static void ingestDocument(Context context, DSpaceObject parent, PackageParameters params, Document document) throws SQLException, AuthorizeException, PackageException {
        String myEmail = context.getCurrentUser().getEmail();
        String myNetid = context.getCurrentUser().getNetid();
        NodeList users = document.getElementsByTagName(RoleDisseminator.EPERSON);
        for (int i = 0; i < users.getLength(); i++) {
            Element user = (Element) users.item(i);
            NodeList emails = user.getElementsByTagName(RoleDisseminator.EMAIL);
            NodeList netids = user.getElementsByTagName(RoleDisseminator.NETID);
            EPerson eperson;
            EPerson collider;
            String email = null;
            String netid = null;
            String identity;
            if (emails.getLength() > 0) {
                email = emails.item(0).getTextContent();
                if (email.equals(myEmail)) {
                    continue;
                }
                identity = email;
                collider = EPerson.findByEmail(context, identity);
            } else if (netids.getLength() > 0) {
                netid = netids.item(0).getTextContent();
                if (netid.equals(myNetid)) {
                    continue;
                }
                identity = netid;
                collider = EPerson.findByNetid(context, identity);
            } else {
                throw new PackageException("EPerson has neither email nor netid.");
            }
            if (null != collider) if (params.replaceModeEnabled()) {
                eperson = collider;
            } else if (params.keepExistingModeEnabled()) {
                log.warn("Existing EPerson {} was not restored from the package.", identity);
                continue;
            } else {
                throw new PackageException("EPerson " + identity + " already exists.");
            } else {
                eperson = EPerson.create(context);
                log.info("Created EPerson {}.", identity);
            }
            eperson.setEmail(email);
            eperson.setNetid(netid);
            NodeList data;
            data = user.getElementsByTagName(RoleDisseminator.FIRST_NAME);
            if (data.getLength() > 0) {
                eperson.setFirstName(data.item(0).getTextContent());
            } else {
                eperson.setFirstName(null);
            }
            data = user.getElementsByTagName(RoleDisseminator.LAST_NAME);
            if (data.getLength() > 0) {
                eperson.setLastName(data.item(0).getTextContent());
            } else {
                eperson.setLastName(null);
            }
            data = user.getElementsByTagName(RoleDisseminator.LANGUAGE);
            if (data.getLength() > 0) {
                eperson.setLanguage(data.item(0).getTextContent());
            } else {
                eperson.setLanguage(null);
            }
            data = user.getElementsByTagName(RoleDisseminator.CAN_LOGIN);
            eperson.setCanLogIn(data.getLength() > 0);
            data = user.getElementsByTagName(RoleDisseminator.REQUIRE_CERTIFICATE);
            eperson.setRequireCertificate(data.getLength() > 0);
            data = user.getElementsByTagName(RoleDisseminator.SELF_REGISTERED);
            eperson.setSelfRegistered(data.getLength() > 0);
            data = user.getElementsByTagName(RoleDisseminator.PASSWORD_HASH);
            if (data.getLength() > 0) {
                eperson.setPasswordHash(data.item(0).getTextContent());
            } else {
                eperson.setPasswordHash(null);
            }
            eperson.update();
        }
        NodeList groups = document.getElementsByTagName(RoleDisseminator.GROUP);
        for (int groupx = 0; groupx < groups.getLength(); groupx++) {
            Element group = (Element) groups.item(groupx);
            String name = group.getAttribute(RoleDisseminator.NAME);
            try {
                name = PackageUtils.translateGroupNameForImport(context, name);
            } catch (PackageException pe) {
                log.warn("Skipping group named '" + name + "' as it seems to correspond to a Community or Collection that does not exist in the system.  " + "If you are performing an AIP restore, you can ignore this warning as the Community/Collection AIP will likely create this group once it is processed.");
                continue;
            }
            Group groupObj = null;
            Group collider = Group.findByName(context, name);
            if (null != collider) {
                if (params.replaceModeEnabled()) {
                    for (Group member : collider.getMemberGroups()) {
                        collider.removeMember(member);
                    }
                    for (EPerson member : collider.getMembers()) {
                        if (!(collider.equals(Group.find(context, 1)) && member.equals(context.getCurrentUser()))) {
                            collider.removeMember(member);
                        }
                    }
                    log.info("Existing Group {} was cleared. Its members will be replaced.", name);
                    groupObj = collider;
                } else if (params.keepExistingModeEnabled()) {
                    log.warn("Existing Group {} was not replaced from the package.", name);
                    continue;
                } else {
                    throw new PackageException("Group " + name + " already exists");
                }
            } else {
                String type = group.getAttribute(RoleDisseminator.TYPE);
                if (type != null && !type.isEmpty() && parent != null) {
                    if (parent.getType() == Constants.COLLECTION) {
                        Collection collection = (Collection) parent;
                        if (type.equals(RoleDisseminator.GROUP_TYPE_ADMIN)) {
                            groupObj = collection.createAdministrators();
                        } else if (type.equals(RoleDisseminator.GROUP_TYPE_SUBMIT)) {
                            groupObj = collection.createSubmitters();
                        } else if (type.equals(RoleDisseminator.GROUP_TYPE_WORKFLOW_STEP_1)) {
                            groupObj = collection.createWorkflowGroup(1);
                        } else if (type.equals(RoleDisseminator.GROUP_TYPE_WORKFLOW_STEP_2)) {
                            groupObj = collection.createWorkflowGroup(2);
                        } else if (type.equals(RoleDisseminator.GROUP_TYPE_WORKFLOW_STEP_3)) {
                            groupObj = collection.createWorkflowGroup(3);
                        }
                    } else if (parent.getType() == Constants.COMMUNITY) {
                        Community community = (Community) parent;
                        if (type.equals(RoleDisseminator.GROUP_TYPE_ADMIN)) {
                            groupObj = community.createAdministrators();
                        }
                    }
                }
                if (groupObj == null) {
                    groupObj = Group.create(context);
                }
                groupObj.setName(name);
                log.info("Created Group {}.", groupObj.getName());
            }
            NodeList members = group.getElementsByTagName(RoleDisseminator.MEMBER);
            for (int memberx = 0; memberx < members.getLength(); memberx++) {
                Element member = (Element) members.item(memberx);
                String memberName = member.getAttribute(RoleDisseminator.NAME);
                EPerson memberEPerson = EPerson.findByEmail(context, memberName);
                if (null != memberEPerson) groupObj.addMember(memberEPerson); else throw new PackageValidationException("EPerson " + memberName + " not found, not added to " + name);
            }
            groupObj.update();
        }
        for (int groupx = 0; groupx < groups.getLength(); groupx++) {
            Element group = (Element) groups.item(groupx);
            String name = group.getAttribute(RoleDisseminator.NAME);
            try {
                name = PackageUtils.translateGroupNameForImport(context, name);
            } catch (PackageException pe) {
                continue;
            }
            Group groupObj = Group.findByName(context, name);
            NodeList members = group.getElementsByTagName(RoleDisseminator.MEMBER_GROUP);
            for (int memberx = 0; memberx < members.getLength(); memberx++) {
                Element member = (Element) members.item(memberx);
                String memberName = member.getAttribute(RoleDisseminator.NAME);
                memberName = PackageUtils.translateGroupNameForImport(context, memberName);
                Group memberGroup = Group.findByName(context, memberName);
                groupObj.addMember(memberGroup);
            }
            groupObj.update();
        }
    }

    /**
     * Ingest roles from an InputStream.
     *
     * @param context
     *          DSpace Context
     * @param parent
     *          the Parent DSpaceObject
     * @param stream
     *          the XML Document InputStream
     * @throws PackageException
     * @throws SQLException
     * @throws AuthorizeException
     */
    public static void ingestStream(Context context, DSpaceObject parent, PackageParameters params, InputStream stream) throws PackageException, SQLException, AuthorizeException {
        Document document;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(stream);
        } catch (ParserConfigurationException e) {
            throw new PackageException(e);
        } catch (SAXException e) {
            throw new PackageException(e);
        } catch (IOException e) {
            throw new PackageException(e);
        }
        ingestDocument(context, parent, params, document);
    }

    @Override
    public DSpaceObject ingest(Context context, DSpaceObject parent, File pkgFile, PackageParameters params, String license) throws PackageException, CrosswalkException, AuthorizeException, SQLException, IOException {
        Document document;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(pkgFile);
        } catch (ParserConfigurationException e) {
            throw new PackageException(e);
        } catch (SAXException e) {
            throw new PackageException(e);
        }
        ingestDocument(context, parent, params, document);
        return null;
    }

    @Override
    public List<DSpaceObject> ingestAll(Context context, DSpaceObject parent, File pkgFile, PackageParameters params, String license) throws PackageException, UnsupportedOperationException, CrosswalkException, AuthorizeException, SQLException, IOException {
        throw new PackageException("ingestAll() is not implemented, as ingest() method already handles ingestion of all roles from an external file.");
    }

    @Override
    public DSpaceObject replace(Context context, DSpaceObject dso, File pkgFile, PackageParameters params) throws PackageException, UnsupportedOperationException, CrosswalkException, AuthorizeException, SQLException, IOException {
        return ingest(context, dso, pkgFile, params, null);
    }

    @Override
    public List<DSpaceObject> replaceAll(Context context, DSpaceObject dso, File pkgFile, PackageParameters params) throws PackageException, UnsupportedOperationException, CrosswalkException, AuthorizeException, SQLException, IOException {
        throw new PackageException("replaceAll() is not implemented, as replace() method already handles replacement of all roles from an external file.");
    }

    /**
     * Returns a user help string which should describe the
     * additional valid command-line options that this packager
     * implementation will accept when using the <code>-o</code> or
     * <code>--option</code> flags with the Packager script.
     *
     * @return a string describing additional command-line options available
     * with this packager
     */
    @Override
    public String getParameterHelp() {
        return "No additional options available.";
    }
}
