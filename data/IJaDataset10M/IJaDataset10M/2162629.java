package com.avatal.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import com.avatal.Constants;
import com.avatal.RightProfileConstants;
import com.avatal.business.StartKonfiguration;
import com.avatal.business.ejb.entity.rightmanagement.AccessLockEntityLocal;
import com.avatal.business.ejb.entity.rightmanagement.AccessLockKeyEntityLocalHome;
import com.avatal.business.ejb.entity.rightmanagement.RightProfileEntityLocalHome;
import com.avatal.business.ejb.entity.user.UserEntityLocal;
import com.avatal.business.ejb.entity.user.UserEntityLocalHome;
import com.avatal.business.exception.AccessCheckFailedException;
import com.avatal.business.exception.EJBException;
import com.avatal.business.exception.ServiceLocatorException;
import com.avatal.business.facade.util.AvatalNCGGraph;
import com.avatal.business.facade.util.SessionWatchDog;
import com.avatal.business.facade.valuelisthandler.IteratorException;
import com.avatal.business.facade.valuelisthandler.ListHandlerException;
import com.avatal.business.facade.valuelisthandler.Singleton;
import com.avatal.business.facade.valuelisthandler.UserListHandler;
import com.avatal.business.util.ArrayListVo;
import com.avatal.business.util.Encryption;
import com.avatal.business.util.HomeInterfaceFinder;
import com.avatal.content.business.ejb.entity.course.CourseEntityLocal;
import com.avatal.content.vo.course.CourseVo;
import com.avatal.util.AvatalProperties;
import com.avatal.view.base.IndexView;
import com.avatal.vo.rightmanagement.RightProfileVo;
import com.avatal.vo.user.GroupVo;
import com.avatal.vo.user.UserVo;

/**
 * Diese Klasse stellt die Methoden zum Aendern, Anlegen und Suchen von Userobjekten und den zugehoerigen Personen
 * bereit.
 * @ejb.bean name="UserManagement"
 *  jndi-name="UserManagementBean"
 *  type="Stateless" 
 */
public abstract class UserManagementBean implements SessionBean {

    public static final String ADDUSER = new String("UserManagement.addUser(UserVo,UserVo,RightProfileVo)");

    public static final String GETASSIGNEDUSERFORCOURSE = new String("UserManagement.getAssignedUsersForCourse(UserVo,Integer)");

    public static final String ACTIVATEUSER = new String("UserManagement.activate(UserVo,Integer)");

    public static final String DEACTIVATEUSER = new String("UserManagement.deactivateUser(UserVo,Integer)");

    public static final String DELETEUSER = new String("UserManagement.delete(UserVo,Integer)");

    public static final String UPDATEPROFILE = new String("UserManagement.update(UserVo)");

    public static final String UPDATEUSER = new String("UserManagement.updateUser(UserVo,UserVo)");

    public static final String SEARCHUSER = new String("UserManagement.getGroupsByNameAndState(String,Integer)");

    public static final String GETASSIGNEDUSERFORCOURSEWITHGROUPUSERS = new String("getAssignedUserForCourseWithGroupUsers(UserVo actor, Integer courseId)");

    /**
     * Checks whether login exists and if password is correct
     * @return true, if login exists and password is correct. false, if either
     * login does not exist or login exists and password is wrong.
     * @ejb.interface-method view-type="both" 
     */
    public boolean checkUserLogin(String login, String password) throws EJBException {
        try {
            AvatalNCGGraph.initialize();
            UserEntityLocal userEntity = com.avatal.business.util.HomeInterfaceFinder.getUserHome().findActiveByLogin(login);
            password = Encryption.getMD5fromString(password);
            return (userEntity.getPassword().equals(password));
        } catch (FinderException e) {
            return false;
        }
    }

    /**
	 * @author c.ferdinand
	* @ejb.interface-method view-type="both" 
	*/
    public void reloadProperties() {
        AvatalProperties.setProperties(null);
    }

    /**
     * @author c.ferdinand
	* @ejb.interface-method view-type="both" 
	*/
    public boolean setupStartConfiguration(String host, String database, String user, String password) throws Exception {
        String[] args = { host, database, user, password };
        try {
            StartKonfiguration.main(args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Olaf Siefart
     * @date 25.08.2003
     * @ejb.interface-method 
     * Gibt die Liste aller User eines Kurses zurueck
     * @deprecated
     */
    public ArrayList getAssignedUsersForCourse(Integer courseId) throws EJBException {
        try {
            CourseEntityLocal course = HomeInterfaceFinder.getCourseHome().findByPrimaryKey(courseId);
            ArrayList userList = new ArrayListVo.MutableArrayListVo();
            for (Iterator iterator = course.getUsersVo(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT)).iterator(); iterator.hasNext(); ) {
                UserVo user = (UserVo) iterator.next();
                userList.add(user);
            }
            return userList;
        } catch (FinderException e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
    }

    /**
   * @author c.ferdinand
   * @date 29.09.2003
   * @ejb.interface-method 
   * Gibt die Liste aller User eines Kurses zurueck inklusive der User die ueber Gruppen zugeordnet sind
   */
    public ArrayList getAssignedUserForCourseWithGroupUsers(UserVo actor, Integer courseId) throws EJBException {
        try {
            CourseEntityLocal course = HomeInterfaceFinder.getCourseHome().findByPrimaryKey(courseId);
            ArrayList userList = new ArrayListVo.MutableArrayListVo();
            for (Iterator iterator = course.getUsersVo(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT)).iterator(); iterator.hasNext(); ) {
                UserVo user = (UserVo) iterator.next();
                userList.add(user);
            }
            CourseVo courseVo = HomeInterfaceFinder.getCourseHome().findByPrimaryKey(courseId).getCourseVo(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT));
            ArrayList courseGroups = courseVo.getGroups();
            ArrayList groupIds = new ArrayList();
            for (Iterator it = courseGroups.iterator(); it.hasNext(); ) {
                GroupVo currentGroup = (GroupVo) it.next();
                groupIds.add(currentGroup.getId());
            }
            AvatalNCGGraph graph = AvatalNCGGraph.getInstance();
            Integer[] intArray = new Integer[groupIds.size()];
            Object[] objectArray = groupIds.toArray();
            Integer[] array = (Integer[]) groupIds.toArray(new Integer[groupIds.size()]);
            Integer[] userIds = graph.getUser(array);
            ArrayList groupUsers = new ArrayList();
            for (int i = 0; i < userIds.length; i++) {
                groupUsers.add(HomeInterfaceFinder.getUserHome().findByPrimaryKey(userIds[i]).getUser());
            }
            if (!groupUsers.isEmpty()) {
                userList.addAll(groupUsers);
                return new ArrayList(new HashSet(userList));
            } else {
                return userList;
            }
        } catch (FinderException e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
    }

    /**
     * @param actor
     * @param newUser
     * @throws EJBException
     * @author Olaf Siefart
     * 
     * Diese Methode legt einen neuen User an. Dabei muss ihr als Actor der User
     * uebergeben werden, der diese Methode ausfuehren will
     * 
     * @ejb.interface-method view-type="both" 
     **/
    public boolean addUser(UserVo actor, UserVo newUser, RightProfileVo rightProfile) throws EJBException {
        try {
            if (checkLoginExistence(newUser)) {
                return false;
            }
            RightManagementLocal rightmanagement = HomeInterfaceFinder.getRightManagementHome();
            UserManagementLocal usermanagement = HomeInterfaceFinder.getUserManagementHome();
            UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            GroupManagementLocal groupManagement = HomeInterfaceFinder.getGroupManagementHome();
            if (!rightmanagement.checkRightForCallingMethodFromUser(actor, new LinkedList(), ADDUSER)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocal newUserEntity = userHome.create(actor, newUser, rightProfile);
            groupManagement.assignUserToGroupAll(newUserEntity.getUser());
            return true;
        } catch (CreateException e) {
            throw new EJBException(e);
        } catch (NamingException e) {
            throw new EJBException(e);
        } catch (ServiceLocatorException e) {
            throw new EJBException(e);
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Suchmethode zum Auffinden aller im System vorhandenen User. 
     * @ejb.interface-method tview-type="both" 
     */
    public ArrayListVo getAllUsers() throws EJBException {
        try {
            Collection collection = com.avatal.business.util.HomeInterfaceFinder.getUserHome().findAll();
            Iterator iterator = collection.iterator();
            ArrayListVo.MutableArrayListVo userArrayListVo = new ArrayListVo.MutableArrayListVo();
            while ((iterator != null) && (iterator.hasNext())) {
                UserVo u = ((UserEntityLocal) iterator.next()).getUser();
                userArrayListVo.add(u);
            }
            return userArrayListVo;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Suchmethode zum Auffinden aller im System vorhandenen User. 
     * @ejb.interface-method view-type="remote" 
     */
    public UserVo[] getAllUser() throws EJBException {
        try {
            UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            return collectionToVo(userHome.findAll());
        } catch (FinderException fe) {
            throw new EJBException(fe);
        }
    }

    /**
     * Suchmethode zum Auffinden eines Users anhand seines Loggins. 
     * @ejb.interface-method
     */
    public UserVo getUserByLogin(String login) throws EJBException {
        try {
            UserEntityLocal user = com.avatal.business.util.HomeInterfaceFinder.getUserHome().findByLogin(login);
            return user.getUser();
        } catch (FinderException e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
    }

    /**
     * Suchmethode zum Auffinden eines Users anhand seines Loggins.
     * @ejb.interface-method view-type="local" 
     */
    public UserEntityLocal findUserByLogin(String login) throws EJBException {
        try {
            UserEntityLocal user = com.avatal.business.util.HomeInterfaceFinder.getUserHome().findByLogin(login);
            return user;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Suchmethode zum Auffinden von Usern anhand ihres Loggins, Namens, Status und Gruppe.
	 * @ejb.interface-method
	 */
    public Collection findUserByLoginNameGroup(String login, String name, Integer state, Integer groupId, Integer count, HttpServletRequest request, IndexView indexView) throws EJBException {
        try {
            UserListHandler userListHandler = new UserListHandler(login, name, state, groupId);
            HashMap hp = new HashMap();
            hp.put("UserListHandler", userListHandler);
            if (Singleton.getMap().get(request.getSession().getId()) == null) {
                request.getSession().setAttribute("sessionWatchDog", new SessionWatchDog());
            }
            Singleton.getMap().put(request.getSession().getId(), hp);
            return userListHandler.getElements(count, indexView);
        } catch (ListHandlerException e) {
            throw new EJBException(e);
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Methode gibt weitere Ergebnisse der ValueList zur�ck.
	 * @ejb.interface-method
	 */
    public Collection next(Integer count, HttpServletRequest request) throws EJBException {
        try {
            HashMap hp1 = (HashMap) Singleton.getMap().get(request.getSession().getId());
            UserListHandler userListHandler = (UserListHandler) hp1.get("UserListHandler");
            return userListHandler.getNextElements(count);
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Methode gibt vorherige Ergebnisse der ValueList zur�ck.
	 * @ejb.interface-method
	 */
    public Collection previous(Integer count, HttpServletRequest request) throws EJBException {
        try {
            HashMap hp1 = (HashMap) Singleton.getMap().get(request.getSession().getId());
            UserListHandler userListHandler = (UserListHandler) hp1.get("UserListHandler");
            return userListHandler.getPreviousElements(count);
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Methode gibt die ersten Ergebnisse der ValueList zur�ck.
	 * @ejb.interface-method
	 */
    public Collection begin(Integer count, HttpServletRequest request) throws EJBException {
        try {
            HashMap hp1 = (HashMap) Singleton.getMap().get(request.getSession().getId());
            UserListHandler userListHandler = (UserListHandler) hp1.get("UserListHandler");
            return userListHandler.getFirstElements(count);
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Methode gibt die letzten Ergebnisse der ValueList zur�ck.
	 * @ejb.interface-method
	 */
    public Collection end(Integer count, HttpServletRequest request) throws EJBException {
        try {
            HashMap hp1 = (HashMap) Singleton.getMap().get(request.getSession().getId());
            UserListHandler userListHandler = (UserListHandler) hp1.get("UserListHandler");
            return userListHandler.getLastElements(count);
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Methode gibt den aktuellen Index der ValueList zur�ck.
	 * @ejb.interface-method
	 */
    public IndexView getListIndex(Integer count, HttpServletRequest request) throws EJBException {
        try {
            HashMap hp1 = (HashMap) Singleton.getMap().get(request.getSession().getId());
            UserListHandler userListHandler = (UserListHandler) hp1.get("UserListHandler");
            IndexView indexView = new IndexView();
            indexView.put(IndexView.SIZE, new Integer(userListHandler.getSize()));
            indexView.put(IndexView.FIRST, new Integer(userListHandler.indexOfActualFirst(count)));
            indexView.put(IndexView.LAST, new Integer(userListHandler.indexOfActualLast(count)));
            return indexView;
        } catch (IteratorException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Methode zum setzten des Status eines Users auf aktiv.
     * @ejb.interface-method view-type="remote" 
     */
    public void aktitvateUser(Integer userPk) throws EJBException, FinderException {
        try {
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.ACTIVE);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Methode zum setzten des Status eines Users auf deaktiv.
     * @ejb.interface-method view-type="remote" 
     */
    public void deaktitvateUser(Integer userPk) throws EJBException, FinderException {
        try {
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.INACTIVE);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Methode zum entfernen eines Users. Dabei wird sein Datensatz nicht aus dem System entfertn, sondern
     * sein Status auf geloescht gesetzt.
     * @ejb.interface-method view-type="remote" 
     */
    public void deleteUser(Integer userPk) throws EJBException, FinderException {
        try {
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.DELETED);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Methode zum aendern der Methadaten eines Users. Zu diesem Zweck wird zuerst die zugehoerige 
     * Entity gesucht und deren update Methode mit den neuen Daten aufgerufen.
     * @ejb.interface-method view-type="remote" 
     */
    public void updateUser(UserVo actor, UserVo user) throws EJBException, FinderException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(actor, new LinkedList(), new String("UserManagement.updateUser(UserVo,UserVo)"))) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(user.getId());
            userEntity.updateUser(user);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /** 
     * @ejb.interface-method view-type="remote" 
     */
    public void updateUser(UserVo user) throws EJBException, FinderException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(user, new LinkedList(), new String("UserManagement.updateUser(UserVo)"))) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(user.getId());
            userEntity.updateUser(user);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * @deprecated
     * @ejb.interface-method view-type="remote" 
     */
    public UserVo[] getGroupsByNameAndState(String name, Integer state) throws EJBException {
        try {
            if (state != null && name.equals("")) {
                UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
                return this.collectionToVo(userHome.findByObjectState(state));
            }
            if (state == null && !name.equals("")) {
                UserEntityLocalHome groupHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
                UserVo user[] = new UserVo[1];
                UserEntityLocal groupLocal = groupHome.findByLogin(name);
                user[0] = groupLocal.getUser();
                return user;
            }
            if (state == null && name.equals("")) {
                UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
                return this.collectionToVo(userHome.findAll());
            }
            if (state != null && !name.equals("")) {
                UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
                return this.collectionToVo(userHome.findByObjectStateAndName(state, name));
            }
            return null;
        } catch (FinderException fe) {
            throw new EJBException(fe);
        }
    }

    /**
     * @author c.ferdinand
     * @date 22.09.2003
     * @ejb.interface-method view-type="both" 
     */
    public UserVo getUserById(Integer id) throws EJBException {
        try {
            return HomeInterfaceFinder.getUserHome().findByPrimaryKey(id).getUser();
        } catch (FinderException e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
    }

    /**
     * @deprecated
     * @ejb.interface-method view-type="both" 
     */
    public void addAdmin(UserVo _newUser) throws EJBException {
        try {
            UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            RightProfileEntityLocalHome rightProfileHome = com.avatal.business.util.HomeInterfaceFinder.getRightProfileHome();
            AccessLockKeyEntityLocalHome accessLockKeyHome = com.avatal.business.util.HomeInterfaceFinder.getAccessLockKeyHome();
            userHome.create(_newUser);
            Iterator iterator = rightProfileHome.findByTitle(RightProfileConstants.ADMINISTRATION).getAccessLocks().iterator();
            AccessLockEntityLocal accessLock = (AccessLockEntityLocal) iterator.next();
            UserEntityLocal newUser = findUserByLogin(_newUser.getLogin());
            newUser.addKey(accessLock);
        } catch (CreateException e) {
            throw new EJBException(e);
        } catch (FinderException e) {
            throw new EJBException(e);
        } catch (NamingException e) {
            throw new EJBException(e);
        } catch (ServiceLocatorException e) {
            throw new EJBException(e);
        }
    }

    /**
     * @author Olaf Siefart
     * @date 25.08.2003
     * @ejb.interface-method 
     * Gibt die Liste aller User eines Kurses zurueck
     */
    public ArrayList getAssignedUsersForCourse(UserVo actor, Integer courseId) throws EJBException {
        try {
            RightManagementLocal rightmanagement = HomeInterfaceFinder.getRightManagementHome();
            if (!rightmanagement.checkRightForCallingMethodFromUser(actor, new LinkedList(), GETASSIGNEDUSERFORCOURSE)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            CourseEntityLocal course = HomeInterfaceFinder.getCourseHome().findByPrimaryKey(courseId);
            ArrayList userList = new ArrayListVo.MutableArrayListVo();
            for (Iterator iterator = course.getUsersVo(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT)).iterator(); iterator.hasNext(); ) {
                UserVo user = (UserVo) iterator.next();
                userList.add(user);
            }
            return userList;
        } catch (FinderException e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
    }

    /**
     * Methode zum setzten des Status eines Users auf aktiv.
     * @ejb.interface-method view-type="remote" 
     */
    public void activate(UserVo actor, Integer userPk) throws EJBException, FinderException {
        try {
            RightManagementLocal rightmanagement = HomeInterfaceFinder.getRightManagementHome();
            if (!rightmanagement.checkRightForCallingMethodFromUser(actor, new LinkedList(), ACTIVATEUSER)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.ACTIVE);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Methode zum setzten des Status eines Users auf deaktiv.
     * @ejb.interface-method view-type="remote" 
     */
    public void deactivate(UserVo actor, Integer userPk) throws EJBException, FinderException {
        try {
            RightManagementLocal rightmanagement = HomeInterfaceFinder.getRightManagementHome();
            if (!rightmanagement.checkRightForCallingMethodFromUser(actor, new LinkedList(), DEACTIVATEUSER)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.INACTIVE);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Methode zum entfernen eines Users. Dabei wird sein Datensatz nicht aus dem System entfertn, sondern
     * sein Status auf geloescht gesetzt.
     * @ejb.interface-method view-type="remote" 
     */
    public void delete(UserVo actor, Integer userPk) throws EJBException, FinderException {
        try {
            RightManagementLocal rightmanagement = HomeInterfaceFinder.getRightManagementHome();
            if (!rightmanagement.checkRightForCallingMethodFromUser(actor, new LinkedList(), DELETEUSER)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(userPk);
            userEntity.setObjectState(Constants.DELETED);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Ein User ver?ndert seine eigenen Metadaten
     * @ejb.interface-method view-type="remote" 
     */
    public void update(UserVo user) throws EJBException, FinderException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(user, new LinkedList(), UPDATEPROFILE)) {
                throw new EJBException(new AccessCheckFailedException());
            }
            UserEntityLocalHome userHome;
            UserEntityLocal userEntity;
            userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userEntity = userHome.findByPrimaryKey(user.getId());
            userEntity.updateUser(user);
        } catch (FinderException ce) {
            throw new EJBException(ce);
        }
    }

    /**
     * Wandelt eine collection in ein array um
     * @param collection
     * @return
     * @throws EJBException
     */
    private UserVo[] collectionToVo(Collection collection) throws EJBException {
        UserVo value[] = null;
        ArrayList list = new ArrayList();
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            UserEntityLocal user = (UserEntityLocal) iterator.next();
            list.add(user.getUser());
        }
        value = new UserVo[list.size()];
        for (int i = 0; i < value.length; i++) {
            value[i] = (UserVo) list.get(i);
        }
        return value;
    }

    /**
     * Ueberprueft, vor dem Anlegen eines neuen Benutzers ob der Login schon vergeben ist um die Eindeutigkeit zu sichern.
     * @param newUser
     * @return
     * @throws EJBException
     */
    private boolean checkLoginExistence(UserVo newUser) throws EJBException {
        try {
            UserEntityLocalHome userHome = com.avatal.business.util.HomeInterfaceFinder.getUserHome();
            userHome.findByLogin(newUser.getLogin());
            return true;
        } catch (FinderException e) {
            return false;
        }
    }
}
