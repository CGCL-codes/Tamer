package gwtm.server.services.users;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwtm.client.services.tm.virtual.LocatorVirtual;
import gwtm.client.services.users.UsersException;
import gwtm.client.services.users.RsUsers;
import gwtm.client.services.users.User;
import gwtm.client.ui.utils.Str;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VLocatorVirtuals;
import gwtm.client.services.tm.virtual.VTopicVirtuals;
import gwtm.client.services.users.UsersStatics;
import gwtm.server.tm.AssociationHelper;
import gwtm.server.tm.RoleHelper;
import gwtm.server.tm.TopicHelper;
import gwtm.server.tm.TopicMapSystemHelper;
import gwtm.server.users.UsersHelper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.Vector;
import org.tmapi.core.Association;
import org.tmapi.core.AssociationRole;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 *
 * @author Yorgos
 */
public class RsUsersImpl extends RemoteServiceServlet implements RsUsers {

    public void save(TopicMap tm) throws Exception {
        String loc = tm.getBaseLocator().getReference();
        TopicMapSystemHelper.saveToXtm(tm, loc);
    }

    public TopicVirtual login(String username, String password) throws UsersException {
        try {
            if (username.equals(UsersStatics.ROOT_USERNAME) && password.equals(UsersStatics.ROOT_PASSWORD)) {
                Topic tRootUser = UsersHelper.getTopicRootUser();
                TopicVirtual tRootUserV = TopicHelper.createVirtual(tRootUser, true);
                return tRootUserV;
            }
            if (!Str.onlyUkOrNum(username)) throw new Exception("incorrect character in username.");
            Topic tUser = UsersHelper.getUserByUsername(username);
            if (tUser == null) throw new Exception("Unknown username.");
            TopicVirtual tUserV = TopicHelper.createVirtual(tUser, true);
            User user = new User(tUserV);
            if (!password.equals(user.getPassword())) throw new Exception("Wrong password.");
            return tUserV;
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void logout(String username) throws UsersException {
        try {
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public String registerUser(String username, String password, String fullName, String eMail, String moduleBaseURL) {
        try {
            if (!Str.onlyUkOrNum(username)) return "Invalid character in username";
            if (password.indexOf(" ") != -1) return "Invalid character in password";
            if (UsersHelper.getUsernameExists(username)) return "The user " + username + " is already registered."; else if (UsersHelper.getEmailExists(eMail)) return "The email " + eMail + " is already used.";
            UsersHelper.postForRegistration(username, password, fullName, eMail, moduleBaseURL);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public void remindByUsername(String username) throws UsersException {
        try {
            Topic tUser = UsersHelper.getUserByUsername(username);
            if (tUser == null) throw new Exception("Unknown username");
            postPasswordReminderToUser(tUser);
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void remindByEmail(String email) throws UsersException {
        try {
            Topic tUser = UsersHelper.getUserByEmail(email);
            if (tUser == null) throw new Exception("Unknown email");
            postPasswordReminderToUser(tUser);
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void postPasswordReminderToUser(Topic tUser) {
        TopicVirtual tvUser = TopicHelper.createVirtual(tUser, true);
        User user = new User(tvUser);
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();
        UsersHelper.postPasswordReminder(email, username, password);
    }

    public TopicVirtual getStrippedUser(String username) throws UsersException {
        try {
            if (!Str.onlyUkOrNum(username)) throw new Exception("incorrect character in username.");
            Topic tUser = UsersHelper.getUserByUsername(username);
            if (tUser == null) throw new Exception("Unknown username.");
            TopicVirtual tUserV = TopicHelper.createVirtual(tUser, false);
            return tUserV;
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public String getStackTrace(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(out));
        return out.toString();
    }

    public VLocatorVirtuals getTmsLocatorsForUser(TopicVirtual tUserV) throws UsersException {
        try {
            VLocatorVirtuals v = new VLocatorVirtuals();
            Topic tUser = TopicHelper.getTopic(tUserV);
            if (tUser == null) return v;
            boolean bRoot = UsersHelper.isRoot(tUser);
            if (bRoot) {
                String[] locators = TopicMapSystemHelper.getBaseLocators(bRoot);
                for (String loc : locators) {
                    LocatorVirtual locV = new LocatorVirtual(loc);
                    v.add(locV);
                }
            } else {
                Topic aTypeOwnerTm = UsersHelper.getAssociationType_OwnerTm();
                Topic aTypeEditorTm = UsersHelper.getAssociationType_EditorTm();
                Topic rTypeTm = UsersHelper.getRoleType_Tm();
                Set<AssociationRole> roles = tUser.getRolesPlayed();
                roles = RoleHelper.copySet(roles);
                for (AssociationRole r : roles) {
                    Association a = r.getAssociation();
                    Topic aType = a.getType();
                    if (aType == aTypeOwnerTm || aType == aTypeEditorTm) {
                        Set<AssociationRole> _roles = a.getAssociationRoles();
                        _roles = RoleHelper.copySet(_roles);
                        for (AssociationRole _r : _roles) {
                            Topic _rType = _r.getType();
                            if (_rType == rTypeTm) {
                                Topic tTm = _r.getPlayer();
                                Set<Locator> locs = tTm.getSubjectIdentifiers();
                                for (Locator loc : locs) {
                                    String name = loc.getReference();
                                    if (UsersHelper.actualTmExists(name)) {
                                        LocatorVirtual locV = new LocatorVirtual(loc.getReference());
                                        v.add(locV);
                                    } else {
                                        _r.remove();
                                        a.remove();
                                        tTm.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return v;
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void setRightsToOwner(String tmLocator, TopicVirtual tUserV) throws UsersException {
        try {
            Topic tUser = TopicHelper.getTopic(tUserV);
            TopicMap tmUsers = tUser.getTopicMap();
            Topic tTm = UsersHelper.createGetTmRefference(tmLocator);
            Association a = tmUsers.createAssociation();
            a.setType(UsersHelper.getAssociationType_OwnerTm());
            a.createAssociationRole(tUser, UsersHelper.getRoleType_Owner());
            a.createAssociationRole(tTm, UsersHelper.getRoleType_Tm());
            save(tmUsers);
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void setRightsToEditor(String tmLocator, TopicVirtual tUserV) throws UsersException {
        try {
            Topic tUser = TopicHelper.getTopic(tUserV);
            TopicMap tmUsers = tUser.getTopicMap();
            Topic tTm = UsersHelper.createGetTmRefference(tmLocator);
            Association a = tmUsers.createAssociation();
            a.setType(UsersHelper.getAssociationType_EditorTm());
            a.createAssociationRole(tUser, UsersHelper.getRoleType_Editor());
            a.createAssociationRole(tTm, UsersHelper.getRoleType_Tm());
            save(tmUsers);
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void removeRights(String tmLocator, TopicVirtual tUserV) throws UsersException {
        try {
            TopicMap tmUsers = UsersHelper.getTopicMapUsers();
            Topic tTm = UsersHelper.getTmRefference(tmLocator);
            if (tTm == null) return;
            Topic tUser = TopicHelper.getTopic(tUserV);
            Association a = AssociationHelper.getAssociationThatConnectsPlaysers(tTm, tUser);
            if (a != null) {
                Set<AssociationRole> roles = a.getAssociationRoles();
                Set<AssociationRole> vRoles = RoleHelper.copySet(roles);
                for (AssociationRole r : vRoles) r.remove();
                a.remove();
                save(tmUsers);
            }
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public VTopicVirtuals getOwners(String tmLocator) throws UsersException {
        try {
            VTopicVirtuals owners = new VTopicVirtuals();
            Topic typeOwner = UsersHelper.getRoleType_Owner();
            Topic tTm = UsersHelper.createGetTmRefference(tmLocator);
            Set<AssociationRole> roles = tTm.getRolesPlayed();
            for (AssociationRole r : roles) {
                Association a = r.getAssociation();
                Set<AssociationRole> _roles = a.getAssociationRoles();
                for (AssociationRole _r : _roles) {
                    if (_r.getType() == typeOwner) {
                        Topic tOwner = _r.getPlayer();
                        TopicVirtual tOwnerV = TopicHelper.createVirtual(tOwner, true);
                        if (!owners.hasTopic(tOwnerV)) owners.add(tOwnerV);
                    }
                }
            }
            return owners;
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public VTopicVirtuals getEditors(String tmLocator) throws UsersException {
        try {
            VTopicVirtuals editors = new VTopicVirtuals();
            Topic typeEditor = UsersHelper.getRoleType_Editor();
            Topic tTm = UsersHelper.createGetTmRefference(tmLocator);
            Set<AssociationRole> roles = tTm.getRolesPlayed();
            for (AssociationRole r : roles) {
                Association a = r.getAssociation();
                Set<AssociationRole> _roles = a.getAssociationRoles();
                for (AssociationRole _r : _roles) {
                    if (_r.getType() == typeEditor) {
                        Topic tEditor = _r.getPlayer();
                        TopicVirtual tEditorV = TopicHelper.createVirtual(tEditor, true);
                        if (!editors.hasTopic(tEditorV)) editors.add(tEditorV);
                    }
                }
            }
            return editors;
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void deleteTmRefference(String tmLocator) throws UsersException {
        try {
            TopicMap tmUsers = UsersHelper.getTopicMapUsers();
            Topic tTm = UsersHelper.getTmRefference(tmLocator);
            if (tTm == null) return;
            Set<AssociationRole> roles = tTm.getRolesPlayed();
            Set<AssociationRole> vRoles = RoleHelper.copySet(roles);
            for (AssociationRole r : vRoles) {
                Association a = r.getAssociation();
                r.remove();
                Set<AssociationRole> _roles = a.getAssociationRoles();
                for (AssociationRole _r : _roles) _r.remove();
                a.remove();
            }
            tTm.remove();
            save(tmUsers);
        } catch (Exception ex) {
            throw new UsersException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }
}
