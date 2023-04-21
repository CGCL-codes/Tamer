package org.azrul.epice.db4o.daoimpl;

import org.azrul.epice.dao.PersonDAO;
import com.db4o.Db4oIOException;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.azrul.epice.domain.Item;
import org.azrul.epice.domain.UserLink;
import org.azrul.epice.domain.Person;
import org.azrul.epice.exception.UserAlreadyExistException;
import org.azrul.epice.exception.UserNotExistException;
import org.azrul.epice.util.DBUtil;
import org.azrul.epice.util.LogUtil;
import org.azrul.epice.util.SendMailUtil;

/**
 *
 * @author Azrul Hasni MADISA
 */
public class DB4OPersonDAO implements PersonDAO {

    DB4OItemDAO itemDAO = new DB4OItemDAO();

    @Override
    public void removeFromBuddies(final Person _user, String buddyEmail) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            Person userToBeRemoved = this.findPersonByEmail(buddyEmail, db);
            if (userToBeRemoved != null) {
                user.getBuddies().remove(userToBeRemoved);
                db.set(user);
                db.commit();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    @Override
    public void removeFromSupervisor(final Person _user, String supEmail) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            Person userToBeRemoved = this.findPersonByEmail(supEmail, db);
            if (userToBeRemoved != null) {
                user.getSupervisors().remove(userToBeRemoved);
                db.set(user);
                db.commit();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Person update(String email, String name, String userName, String password, String icNumber, String phone, String department, String officeAddress, Boolean okToReceiveEmail) throws UserNotExistException {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = this.findPersonByEmail(email, db);
            if (user != null) {
                user.setDepartment(department);
                user.setPassword(password);
                user.setEmail(email);
                user.setIcNumber(icNumber);
                user.setName(name);
                user.setOfficeAddress(officeAddress);
                user.setPhone(phone);
                user.setUserName(userName);
                if (okToReceiveEmail == null) {
                    user.setOkToReceiveEmail(false);
                } else {
                    user.setOkToReceiveEmail(okToReceiveEmail);
                }
                db.set(user);
                db.commit();
                return user;
            } else {
                throw new UserNotExistException();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
            return null;
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Person create(String email, String name, String userName, String password, String icNumber, String phone, String department, String officeAddress) throws UserAlreadyExistException {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person p = this.findPersonByEmail(email, db);
            if (p == null) {
                Person user = new Person();
                user.setDepartment(department);
                user.setEmail(email);
                user.setIcNumber(icNumber);
                user.setPassword(password);
                user.setName(name);
                user.setOfficeAddress(officeAddress);
                user.setPhone(phone);
                user.setUserName(userName);
                db.set(user);
                db.commit();
                return user;
            } else {
                throw new UserAlreadyExistException();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
            return null;
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public void persistNew(final Person _user) throws UserAlreadyExistException {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            if (user == null) {
                db.set(_user);
                db.commit();
            } else {
                throw new UserAlreadyExistException();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public void persistNewWithRandomPassword(final Person _user) throws UserAlreadyExistException {
        ResourceBundle props = ResourceBundle.getBundle("epice");
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            if (user == null) {
                if (("true").equals(props.getString("DEMO_MODE"))) {
                    _user.setPassword("abc123");
                } else {
                    _user.setPassword(UUID.randomUUID().toString());
                }
                db.set(_user);
                db.commit();
            } else {
                throw new UserAlreadyExistException();
            }
        } catch (UserAlreadyExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public String setNewRandomPassword(final Person user) throws UserNotExistException {
        String password = UUID.randomUUID().toString();
        setNewPassword(user, password);
        return password;
    }

    public void setNewPassword(final Person _user, String newPassword) throws UserNotExistException {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            if (user != null) {
                user.setPassword(newPassword);
                db.set(user);
                db.commit();
            } else {
                throw new UserNotExistException();
            }
        } catch (UserNotExistException ex) {
            if (db != null) {
                db.rollback();
            }
            throw ex;
        } catch (Db4oIOException ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Person findPersonByEmailPassword(String email, String password) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person person = new Person();
            person.setEmail(email);
            person.setPassword(password);
            ObjectSet<Person> res = db.get(person);
            if (res.hasNext()) {
                return res.next();
            } else {
                return null;
            }
        } catch (Exception ex) {
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
            return null;
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Person findPersonByEmail(String email, ObjectContainer db) {
        Person person = new Person();
        person.setEmail(email);
        ObjectSet<Person> res = db.get(person);
        if (res.hasNext()) {
            return res.next();
        } else {
            return null;
        }
    }

    public Person findPersonByEmail(String email) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            return findPersonByEmail(email, db);
        } catch (Exception ex) {
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
            return null;
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Person refresh(final Person person) {
        return findPersonByEmail(person.getEmail());
    }

    public Person refresh(final Person person, ObjectContainer db) {
        if (person == null) {
            return null;
        }
        return findPersonByEmail(person.getEmail(), db);
    }

    public void addBuddiesByEmails(final Person user, Set<String> buddyEmailSet) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            addBuddiesByEmails(user, buddyEmailSet, db);
            db.commit();
        } catch (Exception ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public void addBuddies(final Person _user, Set<Person> _buddies) {
        ResourceBundle props = ResourceBundle.getBundle("epice");
        String emailInviteTextProto = props.getString("EMAIL_INVITE_TEXT");
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            if (user == null) {
                throw new UserNotExistException();
            }
            _buddies.addAll(user.getBuddies());
            Set<Person> buddies = new HashSet<Person>();
            for (Person _buddy : _buddies) {
                if (_buddy == null) {
                    continue;
                }
                Person buddy = refresh(_buddy, db);
                if (buddy != null) {
                    buddies.add(buddy);
                } else {
                    buddy = new Person();
                    String randPassword = UUID.randomUUID().toString();
                    buddy.setPassword(randPassword);
                    String emailInviteText = new String(emailInviteTextProto);
                    emailInviteText = emailInviteText.replace("%%Link%%", props.getString("EPICE_URL"));
                    emailInviteText = emailInviteText.replace("%%email%%", buddy.getEmail());
                    emailInviteText = emailInviteText.replace("%%key%%", buddy.getPassword());
                    emailInviteText = emailInviteText.replace("%%Sender%%", user.getEmail());
                    SendMailUtil.send(user.getEmail(), buddy.getEmail(), emailInviteText, props.getString("EMAIL_INVITE_TITLE"));
                    db.set(buddy);
                    buddies.add(buddy);
                }
            }
            user.setBuddies(buddies);
            db.set(user);
            db.commit();
        } catch (Exception ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    private void addBuddiesByEmails(final Person _user, Set<String> buddyEmailSet, ObjectContainer db) throws UserNotExistException {
        ResourceBundle props = ResourceBundle.getBundle("epice");
        String emailInviteTextProto = props.getString("EMAIL_INVITE_TEXT");
        Set<Person> buddies = new HashSet<Person>();
        Person user = refresh(_user, db);
        if (user == null) {
            throw new UserNotExistException();
        }
        buddies.addAll(user.getBuddies());
        for (String buddyEmail : buddyEmailSet) {
            Person buddy = findPersonByEmail(buddyEmail, db);
            if (buddy != null) {
                buddies.add(buddy);
            } else {
                buddy = new Person();
                String randPassword = UUID.randomUUID().toString();
                buddy.setPassword(randPassword);
                buddy.setEmail(buddyEmail);
                String emailInviteText = new String(emailInviteTextProto);
                emailInviteText = emailInviteText.replace("%%Link%%", props.getString("EPICE_URL"));
                emailInviteText = emailInviteText.replace("%%email%%", buddy.getEmail());
                emailInviteText = emailInviteText.replace("%%key%%", buddy.getPassword());
                emailInviteText = emailInviteText.replace("%%Sender%%", user.getEmail());
                SendMailUtil.send(user.getEmail(), buddy.getEmail(), emailInviteText, props.getString("EMAIL_INVITE_TITLE"));
                db.set(buddy);
                buddies.add(buddy);
            }
        }
        user.setBuddies(buddies);
        db.set(user);
    }

    public void addSupervisorsByEmails(final Person user, Set<String> supervisorEmailSet) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            addSupervisorsByEmails(user, supervisorEmailSet, db);
            db.commit();
        } catch (Exception ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    private void addSupervisorsByEmails(final Person _user, Set<String> supervisorEmailSet, ObjectContainer db) throws UserNotExistException {
        ResourceBundle props = ResourceBundle.getBundle("epice");
        String emailInviteTextProto = props.getString("EMAIL_INVITE_TEXT");
        Set<Person> supervisors = new HashSet<Person>();
        Person user = refresh(_user, db);
        if (user == null) {
            throw new UserNotExistException();
        }
        for (String supervisorEmail : supervisorEmailSet) {
            Person sup = findPersonByEmail(supervisorEmail);
            if (sup != null) {
                supervisors.add(sup);
            } else {
                sup = new Person();
                String randPassword = UUID.randomUUID().toString();
                sup.setPassword(randPassword);
                String emailInviteText = new String(emailInviteTextProto);
                emailInviteText = emailInviteText.replace("%%Link%%", props.getString("EPICE_URL"));
                emailInviteText = emailInviteText.replace("%%email%%", sup.getEmail());
                emailInviteText = emailInviteText.replace("%%key%%", sup.getPassword());
                emailInviteText = emailInviteText.replace("%%Sender%%", user.getEmail());
                SendMailUtil.send(user.getEmail(), sup.getEmail(), emailInviteText, props.getString("EMAIL_INVITE_TITLE"));
                db.set(sup);
                supervisors.add(sup);
            }
        }
        user.setSupervisors(supervisors);
        db.set(user);
    }

    public void addBuddiesAndSupervisorsByEmails(final Person user, Set<String> buddyEmailSet, Set<String> supervisorEmailSet) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            addSupervisorsByEmails(user, supervisorEmailSet, db);
            addBuddiesByEmails(user, buddyEmailSet, db);
            db.commit();
        } catch (Exception ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }

    public Map<Date, List<Item>> syncCalendars(List<Person> users) {
        return null;
    }

    public void addLinks(final Person _user, final List<UserLink> links) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            Person user = refresh(_user, db);
            if (user == null) {
                throw new UserNotExistException();
            }
            Set<UserLink> linksSet = new HashSet<UserLink>();
            if (user.getUserLinks() != null) {
                linksSet.addAll(user.getUserLinks());
            }
            linksSet.addAll(links);
            user.setUserLinks(linksSet);
            db.set(user);
            db.commit();
        } catch (Exception ex) {
            if (db != null) {
                db.rollback();
            }
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }
}
