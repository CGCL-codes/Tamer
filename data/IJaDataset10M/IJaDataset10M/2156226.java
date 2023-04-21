package de.uni_hamburg.golem.control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.uni_hamburg.golem.model.GAbstractRecord;
import de.uni_hamburg.golem.model.GGroup;
import de.uni_hamburg.golem.model.GInstitution;
import de.uni_hamburg.golem.model.GMembership;
import de.uni_hamburg.golem.model.GPerson;
import de.uni_hamburg.golem.model.PackageFactoryBean;
import de.uni_hamburg.golem.target.TargetUtils;

/**
 * CAS / SSL Setup:
 *
 * ggf.:
 *
 * keytool -delete -alias golem -keystore %JAVA_HOME%/jre/lib/security/cacerts
 *
 * dann:
 *
 * keytool -genkey -alias golem -keystore keystore -keypass changeit -keyalg RSA
 * (note that your firstname and lastname MUST be hostname of your server and cannot be a IP address; this is very important as an IP address will fail client hostname verification even if it is correct)
 *
 * keytool -export -alias golem -keystore keystore -keypass changeit -file casserver.crt
 *
 * keytool -import -trustcacerts -alias golem -file casserver.crt -keypass changeit -keystore %JAVA_HOME%/jre/lib/security/cacerts
 *
 * @author rz0a022
 *
 */
public class SecurityController {

    public static final String REDIRECTURL = "golem.redirecturl";

    public static final String GOLEM_USER = "golem.user";

    public static final String SUBJECT_PERSON = "person";

    public static final String SUBJECT_GROUP = "group";

    public static final String SUBJECT_INSTITUTION = "institution";

    public static final String SUBJECT_DEVICE = "device";

    public static final String SYSADMIN = "isSysAdmin";

    public static final String INSTADMIN = "isInstitutionalAdmin";

    private Log log = LogFactory.getLog(this.getClass());

    private GRepository repository;

    private PackageFactoryBean packager;

    /**
	 * @return the repository
	 */
    public GRepository getRepository() {
        return repository;
    }

    /**
	 * @param repository the repository to set
	 */
    public void setRepository(GRepository repository) {
        this.repository = repository;
    }

    /**
	 * Checks if accesscode matches.
	 * @param groupid
	 * @param accesscode
	 * @return
	 */
    public boolean checkEnrolment(String groupid, String accesscode) {
        try {
            GGroup group = repository.getGroup(groupid);
            if (group != null) {
                return group.getAccessCode().equals(accesscode);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }

    /**
	 * Checks Credentials against external and internal store
	 * @param userid
	 * @param password
	 * @return
	 */
    public GPerson authenticate(String userid, String password) {
        if (userid == null || password == null) return null;
        try {
            GPerson user = repository.getPerson(userid);
            String cryptPassword = password;
            if (user.getEncryptiontype().equals(GPerson.ENC_MD5)) {
                cryptPassword = TargetUtils.getMD5(password);
            }
            user.setPassword(cryptPassword);
            if (repository.checkInternal(user)) {
                return user;
            } else {
                if (repository.checkExternal(user)) {
                    return user;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * @param obj
	 * @return
	 */
    public static HashMap<String, GAbstractRecord> createSubjects(GAbstractRecord[] obj) {
        HashMap<String, GAbstractRecord> subjects = new HashMap<String, GAbstractRecord>();
        for (int i = 0; i < obj.length; i++) {
            subjects.put(obj[i].getContext(), obj[i]);
        }
        return subjects;
    }

    /**
	 * @param t
	 * @return
	 */
    public static HashSet<String> createTests(String[] t) {
        HashSet<String> tests = new HashSet<String>();
        for (int i = 0; i < t.length; i++) {
            tests.add(t[i]);
        }
        return tests;
    }

    /**
	 * @param user
	 * @return
	 * TODO: NIY
	 */
    public boolean isSysAdmin(GPerson user) {
        try {
            GPerson admin = repository.getSysAdmin();
            if (user.getUserid().equals(admin.getUserid())) {
                return true;
            }
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }

    /**
	 * True iff user is admin of given institution.
	 * @param user
	 * @param institution
	 * @return
	 */
    public boolean isInstitutionalAdmin(GPerson user, GInstitution institution) {
        if (institution.getOwner().equals(user.getUserid())) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * True iff user has role "manager".
	 * @param user
	 * @return
	 */
    public boolean isManager(GPerson user) {
        if (user.isManager()) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @param subjects
	 * @return
	 */
    public boolean isStaff(GPerson user) {
        if (user.getRoletype().indexOf(GPerson.ROLE_STAFF) > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @param subjects
	 * @return
	 */
    public boolean isMember(GPerson user, GGroup group) {
        try {
            List<GMembership> ms = repository.getMemberships(user.getUserid(), group.getGroupid());
            if (ms.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn(e);
            return false;
        }
    }

    /**
	 * @return the packager
	 */
    public PackageFactoryBean getPackager() {
        return packager;
    }

    /**
	 * @param packager the packager to set
	 */
    public void setPackager(PackageFactoryBean packager) {
        this.packager = packager;
    }

    /**
	 * Returns person associated with a user id.
	 * @param userid
	 * @return
	 * @throws Exception
	 */
    public GPerson getUser(String userid) throws Exception {
        return this.repository.getPerson(userid);
    }

    public GPerson getSysAdmin() throws Exception {
        GPerson admin = repository.getSysAdmin();
        return admin;
    }
}
