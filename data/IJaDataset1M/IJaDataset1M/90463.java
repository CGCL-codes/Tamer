package org.foafrealm.manage;

import static org.foafrealm.auth.PrivilegeClass.NONE;
import static org.foafrealm.manage.FoafomaticState.UNKNOWN;
import static org.foafrealm.manage.FoafomaticState.UPDATED;
import static org.foafrealm.manage.FoafomaticState.USER_ERRORREG;
import static org.foafrealm.manage.FoafomaticState.USER_EXISTS;
import static org.foafrealm.manage.FoafomaticState.USER_NOTFOUNDREG;
import static org.foafrealm.manage.FoafomaticState.USER_SUCCESSREG1;
import static org.foafrealm.manage.FoafomaticState.USER_SUCCESSREG2;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.foafrealm.auth.PrivilegeClass;
import org.foafrealm.beans.ConfigKeeper;
import org.foafrealm.beans.Context;
import org.foafrealm.beans.FieldValueWrapperMap;
import org.foafrealm.tools.FoafRealmHelper;

/**
 * 
 * 
 * @author Sebastian Ryszard Kruk, Tomek Woroniecki,
 * @created 15.11.2005
 */
public class Foafomatic {

    private static final Logger log = Logger.getLogger(Foafomatic.class.getName());

    /**
	 * @param request
	 * @param realm
	 * @param mapPerson
	 * @param wperson
	 * @return
	 */
    public FoafomaticState process(FieldValueWrapperMap requestMap, HttpServletRequest request, PrivilegeClass realm, Map<String, Object> mapPerson, PersonWrapper wperson) {
        FoafomaticState state = UNKNOWN;
        List<Map<String, Object>> lstKnows = new ArrayList<Map<String, Object>>();
        cleanMapPerson(mapPerson, lstKnows);
        String action = requestMap.getString("action");
        if (requestMap.get("regcode") != null) {
            state = registerPersonStep2(mapPerson, wperson);
        } else {
            if (!NONE.isequal(realm) && !"load".equals(action) && !"delete".equals(action)) {
                wperson.setPerson(loadPerson(requestMap, request, mapPerson));
            }
            if ("update".equals(action) && !"".equals(requestMap.getString("email_sha1sum")) && (wperson.getPerson() != null && wperson.getPerson().getUri().toString().equals(requestMap.getString("personuri")))) {
                updatePerson(requestMap, request, mapPerson, wperson, lstKnows);
                state = UPDATED;
            } else if ("update".equals(action) && !"".equals(requestMap.getString("email_sha1sum")) && (wperson.getPerson() == null && !PersonFactory.isPersonRegistered(null, requestMap.getString("email_sha1sum")))) {
                registerPersonStep1(requestMap, mapPerson, wperson, request);
                state = USER_SUCCESSREG1;
            } else if ("update".equals(action) && wperson.getPerson() == null && PersonFactory.isPersonRegistered(null, requestMap.getString("email_sha1sum"))) {
                state = USER_EXISTS;
            } else if ("extfoaf".equals(action) && NONE.isequal(realm)) {
                state = checkFoafRegistration(requestMap, mapPerson, wperson);
            }
            if ("load".equals(action) && PrivilegeClass.ADMIN.equals(realm)) {
                wperson.setPerson(PersonFactory.getPerson(null, requestMap.getString("mbox_sha1sum"), false));
                if (wperson.getPerson() == null) {
                    log.warning("Cannot get person with mbox = " + requestMap.get("mbox_sha1sum"));
                } else {
                    wperson.getPerson().writeToMap(mapPerson);
                }
            }
            if ("delete".equals(action) && PrivilegeClass.ADMIN.equals(realm)) {
                String[] asDelPersons = request.getParameterValues("delPerson");
                PersonFactory.deletePersons(asDelPersons);
            }
        }
        return state;
    }

    /**
	 * @param mapPerson
	 * @param lstKnows
	 *            Description of Parameter
	 */
    private void cleanMapPerson(Map<String, Object> mapPerson, List lstKnows) {
        mapPerson.put("title", "");
        mapPerson.put("name", "");
        mapPerson.put("givenname", "");
        mapPerson.put("family_name", "");
        mapPerson.put("nick", "");
        mapPerson.put("mbox", "");
        mapPerson.put("mbox_sha1sum", "");
        mapPerson.put("phone", "");
        mapPerson.put("password_sha1sum", "");
        mapPerson.put("bio", "");
        mapPerson.put("homepage", "");
        mapPerson.put("depiction", "");
        mapPerson.put("workplaceHomepage", "");
        mapPerson.put("workInfoHomepage", "");
        mapPerson.put("schoolHomepage", "");
        mapPerson.put("additionalEmails", "");
        mapPerson.put("additionalPhones", "");
        mapPerson.put("additionalNicks", "");
        mapPerson.put("hideEmail", Boolean.TRUE);
        mapPerson.put("knows", lstKnows);
        lstKnows.clear();
    }

    /**
	 * @param request
	 * @param mapPerson
	 * @return
	 */
    private Person loadPerson(FieldValueWrapperMap requestMap, HttpServletRequest request, Map<String, Object> mapPerson) {
        Person person = FoafRealmHelper.getInstance().getLoggedPerson(request);
        String action = requestMap.getString("action");
        if (person == null) {
            log.info("Cannot get null person");
        } else {
            log.finer("Loading profile with email/sha1 = " + person.getMbox_sha1sum());
            if ("addfriend".equals(action)) {
                String mbox_sha1sum = requestMap.getString("friend");
                org.foafrealm.manage.Person friend = PersonFactory.getPerson(null, mbox_sha1sum, false);
                person.addKnown(friend, 0.5F);
            }
            person.writeToMap(mapPerson);
        }
        return person;
    }

    /**
	 * Checking if the registration can be don
	 * 
	 * @param request
	 * @param mapPerson
	 * @param wperson
	 */
    private void registerPersonStep1(FieldValueWrapperMap requestMap, Map<String, Object> mapPerson, PersonWrapper wperson, HttpServletRequest request) {
        mapRequestToPerson(requestMap, mapPerson, request);
        Person p = PersonFactory.getTemporalPerson(mapPerson);
        createRegistrationFoaf(wperson, p);
        p.setTemporalRegcode(wperson.getRegcode());
    }

    /**
	 * @param wperson
	 * @param p
	 */
    private void createRegistrationFoaf(PersonWrapper wperson, Person p) {
        wperson.setPerson(p);
        if (p.getMbox() != null) {
            wperson.setMbox(p.getMbox().toString());
        }
        wperson.setRegcode(Sha1sum.getInstance().calc("REGCODE" + p.getMbox_sha1sum() + p.getMbox() + p.getPassword_sha1sum() + (new Date().getTime())));
        wperson.setXmlPerson(p.toRDF(true, true, true));
        wperson.setPassword_sha1sum(p.getPassword_sha1sum());
    }

    /**
	 * @param request
	 * @param mapPerson
	 * @param wperson
	 * @param lstKnows
	 */
    private void updatePerson(FieldValueWrapperMap requestMap, HttpServletRequest request, Map<String, Object> mapPerson, PersonWrapper wperson, List<Map<String, Object>> lstKnows) {
        mapRequestToPerson(requestMap, mapPerson, request);
        if (request.getRemoteUser() != null) {
            int friends_count = Integer.parseInt(requestMap.getString("friends_count"));
            for (int i = 1; i <= friends_count; i++) {
                String sFName = requestMap.get("friend_" + i + "_name").toString();
                String sFEmail = requestMap.get("friend_" + i + "_mbox").toString();
                if (!sFEmail.startsWith("mailto:") && sFEmail.indexOf('@') > 0) {
                    sFEmail = "mailto:" + sFEmail;
                }
                String sFSeeAlso = (requestMap.get("friend_" + i + "_seealso") != null) ? requestMap.get("friend_" + i + "_seealso").toString() : null;
                String sFFsLevel = (requestMap.get("friend_" + i + "_friendshiplevel") != null) ? requestMap.get("friend_" + i + "_friendshiplevel").toString() : null;
                String sFDelete = (requestMap.get("friend_" + i + "_delete") != null) ? requestMap.get("friend_" + i + "_delete").toString() : null;
                java.util.Map<String, Object> _map = new java.util.HashMap<String, Object>();
                lstKnows.add(_map);
                _map.put("mbox", sFEmail);
                _map.put("name", sFName);
                _map.put("seeAlso", sFSeeAlso);
                _map.put("friendshipLevel", sFFsLevel);
                _map.put("delete", sFDelete);
                log.finest("[DEBUG] friend: " + sFName + " " + sFEmail);
            }
            log.finest("I have load " + friends_count + " friends");
        }
        wperson.setPerson(org.foafrealm.manage.PersonFactory.getPerson(mapPerson, false));
    }

    /**
	 * @param request
	 * @param mapPerson
	 */
    private void mapRequestToPerson(FieldValueWrapperMap requestMap, Map<String, Object> mapPerson, HttpServletRequest request) {
        mapPerson.put("title", requestMap.getString("title"));
        mapPerson.put("name", requestMap.get("name") == null || ("".equals(requestMap.getString("name"))) ? (requestMap.getString("firstName") + " " + requestMap.getString("surname")) : requestMap.getString("name"));
        mapPerson.put("givenname", requestMap.getString("firstName"));
        mapPerson.put("family_name", requestMap.getString("surname"));
        mapPerson.put("nick", requestMap.getString("nick"));
        mapPerson.put("mbox", (requestMap.getString("email").startsWith("mailto:")) ? (requestMap.getString("email")) : ("mailto:" + requestMap.getString("email")));
        mapPerson.put("bio", requestMap.getString("bio"));
        mapPerson.put("mbox_sha1sum", requestMap.getString("email_sha1sum"));
        mapPerson.put("phone", requestMap.getString("phone"));
        mapPerson.put("password_sha1sum", requestMap.getString("password_sha1sum"));
        mapPerson.put("homepage", requestMap.getString("homepage"));
        mapPerson.put("additionalEmails", requestMap.getString("additionalEmails"));
        mapPerson.put("additionalNicks", requestMap.getString("additionalNicks"));
        mapPerson.put("additionalPhones", requestMap.getString("additionalPhones"));
        mapPerson.put("workplaceHomepage", requestMap.getString("workplaceHomepage"));
        mapPerson.put("workInfoHomepage", requestMap.getString("workInfoHomepage"));
        mapPerson.put("schoolHomepage", requestMap.getString("schoolHomepage"));
        mapPerson.put("hideEmail", new Boolean("true".equals(requestMap.getString("hideemail"))));
        FileItem fi = requestMap.getValueFI("depiction");
        if (fi != null) {
            if (!fi.isFormField()) {
                File f;
                try {
                    String postFix = ".jpg";
                    if (fi.getName().indexOf('.') == -1) postFix = fi.getName().substring(fi.getName().indexOf('.'));
                    f = File.createTempFile("personpic", postFix, new File(ConfigKeeper.getInstallDir() + ConfigKeeper.getStoragePath()));
                    String picUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath() + "/showpicture?pic=" + f.getName();
                    mapPerson.put("depiction", picUrl);
                    fi.write(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
	 * First step in registration process
	 * 
	 * @param request
	 *            Description of Parameter
	 * @param mapPerson
	 *            Description of Parameter
	 * @param wperson
	 *            Description of Parameter
	 * @return
	 */
    private FoafomaticState checkFoafRegistration(FieldValueWrapperMap reqparam, Map<String, Object> mapPerson, PersonWrapper wperson) {
        String password_sha1sum = reqparam.getValueS("password_sha1sum");
        String mbox = reqparam.getValueS("mbox");
        String mboxsha1sum = Sha1sum.getInstance().calc((mbox.startsWith("mailto:") ? mbox : ("mailto:" + mbox)));
        FileItem fiFoafFile = reqparam.getValueFI("foaffile");
        String foafurl = reqparam.getValueS("foafurl");
        Person pPerson = null;
        if (PersonFactory.isPersonRegistered(null, mboxsha1sum)) {
            return USER_EXISTS;
        }
        if (fiFoafFile != null) {
            try {
                java.io.InputStream isFoafFile = fiFoafFile.getInputStream();
                PersonFactory.readXml(mbox, isFoafFile);
                isFoafFile.close();
            } catch (IOException ioex) {
                log.log(Level.WARNING, "An error occured when loading foaffile", ioex);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading foaffile", peex);
                return USER_ERRORREG;
            }
        } else if (foafurl != null && !"".equals(foafurl)) {
            try {
                java.net.URL url = new java.net.URL(foafurl);
                java.io.InputStream isFoafUrl = url.openStream();
                PersonFactory.readXml(mbox, isFoafUrl);
                isFoafUrl.close();
            } catch (java.net.MalformedURLException muex) {
                log.log(Level.INFO, "An error occured when registering new user with FOAF file", muex);
            } catch (java.io.IOException ioex) {
                log.log(Level.INFO, "An error occured when registering new user with FOAF file", ioex);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading FOAF file", peex);
                return USER_ERRORREG;
            }
        } else {
            String foaftext = reqparam.get("foaftext").getValueS();
            try {
                PersonFactory.readXml(mbox, foaftext);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading foaftext", peex);
                return USER_ERRORREG;
            }
        }
        pPerson = PersonFactory.getPersonIfExists(mbox, mboxsha1sum);
        if (pPerson != null) {
            pPerson.setPassword_sha1sum(password_sha1sum);
            if (pPerson.getMbox() == null) {
                wperson.setMbox(mbox);
            }
            createRegistrationFoaf(wperson, pPerson);
            pPerson.setTemporalRegcode(wperson.getRegcode());
            return USER_SUCCESSREG1;
        }
        return USER_NOTFOUNDREG;
    }

    /**
	 * @param mapPerson
	 * @param wperson
	 * @return
	 */
    private FoafomaticState registerPersonStep2(Map<String, Object> mapPerson, PersonWrapper wperson) {
        String mbox = wperson.getMbox();
        if (mbox == null) {
            return USER_ERRORREG;
        }
        String mboxsha1sum = Sha1sum.getInstance().calc((mbox.startsWith("mailto:") ? mbox : ("mailto:" + mbox)));
        Person pPerson = null;
        pPerson = PersonFactory.getPersonIfExists(mbox, mboxsha1sum);
        if (pPerson != null) {
            wperson.setPerson(pPerson);
            wperson.getPerson().writeToMap(mapPerson);
            return USER_SUCCESSREG2;
        }
        return USER_ERRORREG;
    }
}
