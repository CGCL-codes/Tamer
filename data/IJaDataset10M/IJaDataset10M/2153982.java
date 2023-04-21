package com.avatal.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import com.avatal.Constants;
import com.avatal.RightProfileConstants;
import com.avatal.business.ejb.entity.group.GroupEntityLocal;
import com.avatal.business.ejb.entity.group.GroupEntityLocalHome;
import com.avatal.business.ejb.entity.rightmanagement.AccessLockEntityLocal;
import com.avatal.business.ejb.entity.rightmanagement.AccessLockKeyEntityLocal;
import com.avatal.business.ejb.entity.rightmanagement.MandatoryAccessLockEntityLocal;
import com.avatal.business.ejb.entity.rightmanagement.RightProfileEntityLocal;
import com.avatal.business.ejb.entity.user.UserEntityLocal;
import com.avatal.business.ejb.entity.user.UserEntityLocalHome;
import com.avatal.business.exception.AccessCheckFailedException;
import com.avatal.business.exception.EJBException;
import com.avatal.business.util.ArrayListVo;
import com.avatal.business.util.HomeInterfaceFinder;
import com.avatal.vo.rightmanagement.RightProfileVo;
import com.avatal.vo.user.GroupVo;
import com.avatal.vo.user.UserVo;

/**
 * DIe Klasse regelt den Zugriff auf die Schloesser am Mandantenobjekt. Diese Schloesser werden auch vergeben, wenn
 * Nutzer oder Gruppen neue Schloesser bekommen. Es existiert immer nur ein Schloss am Mandantenobjekt pro 
 * Rechteprofile. Der Zugriff auf andere Schloesser wird spaeter in Stufe 2 dann ueber die Zuordnung der Objekte realisiert.
 * @ejb.bean name="MandatoryManagement"
 *	jndi-name="MandatoryManagementBean"
 *	type="Stateless" 
 * @author Olaf Siefart
 */
public abstract class MandatoryManagementBean implements SessionBean {

    public static final String GETASSIGNRIGHTPROFILES = "getAssignRightProfiles(UserVo)";

    public static final String ASSIGNUSERTORIGHTPROFILE = "assignUserToRightProfile(UserVo,HashMap)";

    public static final String ASSIGNGROUPSTORIGHTPROFILE = "assignGroupsToRightProfile(UserVo,HashMap)";

    /**
	 * Diese Methode holt alle Rechteprofile, die an einem Schloss eines Mandantenobjektes haengen.
	 * In diese Rechteprofile werden dann die User und Gruppen eingebettet, die einen Schluessel zu dem 
	 * Mandantenschloss besitzen. Diese Methode wird benutzt um Nutzern und Gruppen neue Schluessel
	 * zu geben. 
	 * @ejb.interface-method view-type="remote" 
	 * @author Olaf Siefart
	 */
    public List getAssignRightProfiles(UserVo actor) throws EJBException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(actor, new LinkedList(), GETASSIGNRIGHTPROFILES)) throw new EJBException(new AccessCheckFailedException());
            ArrayList rightProfiles = new ArrayListVo.MutableArrayListVo();
            Collection accessLocks = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findAll();
            for (Iterator iterator = accessLocks.iterator(); iterator.hasNext(); ) {
                AccessLockEntityLocal accessLock = ((MandatoryAccessLockEntityLocal) iterator.next()).getAccessLock();
                if (!accessLock.getRightProfile().getTitle().equals(RightProfileConstants.STUDENT)) {
                    rightProfiles.add(getFilledRightProfile(accessLock));
                }
            }
            return rightProfiles;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Ordnet User einem RechteProfile zu, das heisst sie bekommen einen entsprechenen Schluessel
	 * @ejb.interface-method view-type="remote" 
	 * @author Olaf Siefart
	 * changes 03/2004 c.ferdinand
	 */
    public void assignUserToRightProfile(UserVo actor, HashMap containerValues) throws EJBException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(actor, new LinkedList(), ASSIGNUSERTORIGHTPROFILE)) throw new EJBException(new AccessCheckFailedException());
            for (Iterator it = containerValues.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer rightProfileId = (Integer) entry.getKey();
                AccessLockEntityLocal accessLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(rightProfileId).getAccessLock();
                String values = (String) entry.getValue();
                ArrayList userIds = new ArrayList();
                if (values != null) {
                    for (StringTokenizer st = new StringTokenizer(values, ","); st.hasMoreElements(); ) {
                        userIds.add(new Integer(st.nextToken()));
                    }
                }
                Set oldUser = getUserIdsFromAccessLock(accessLock);
                removeUserFromAccessLock(accessLock, userIds, oldUser);
                userIds.removeAll(oldUser);
                for (Iterator iterator = userIds.iterator(); iterator.hasNext(); ) {
                    UserEntityLocal currentUser = HomeInterfaceFinder.getUserHome().findByPrimaryKey(((Integer) iterator.next()));
                    currentUser.addKey(accessLock);
                    Collection usersOpenLocks = currentUser.getOwnOpenAccessLocks();
                    RightProfileEntityLocal rightProfLocal = HomeInterfaceFinder.getRightProfileHome().findById(rightProfileId);
                    if (!rightProfLocal.getTitle().equals(RightProfileConstants.ADMINISTRATION)) {
                        Collection userLocks = currentUser.getAccessLocks();
                        boolean lockExists = false;
                        for (Iterator aIt = userLocks.iterator(); aIt.hasNext(); ) {
                            AccessLockEntityLocal currentLock = (AccessLockEntityLocal) aIt.next();
                            if (currentLock.getRightProfile().getTitle().equals(rightProfLocal.getTitle())) {
                                currentUser.addKey(currentLock);
                                lockExists = true;
                            }
                        }
                        if (!lockExists) {
                            AccessLockEntityLocal newLock = HomeInterfaceFinder.getAccessLockHome().create(rightProfLocal);
                            currentUser.addAccessLock(newLock);
                            currentUser.addKey(newLock);
                        }
                        for (Iterator iter = usersOpenLocks.iterator(); iter.hasNext(); ) {
                            AccessLockEntityLocal currentAccesslock = (AccessLockEntityLocal) iter.next();
                            if (!currentAccesslock.getRightProfile().getTitle().equals(RightProfileConstants.TEACHER)) {
                                currentUser.removeKey(currentAccesslock);
                            }
                        }
                        MandatoryAccessLockEntityLocal teacherManLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.TEACHER).getId());
                        MandatoryAccessLockEntityLocal studentManLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT).getId());
                        currentUser.removeKey(teacherManLock.getAccessLock());
                        currentUser.removeKey(studentManLock.getAccessLock());
                    } else {
                        for (Iterator iter = usersOpenLocks.iterator(); iter.hasNext(); ) {
                            AccessLockEntityLocal currentAccesslock = (AccessLockEntityLocal) iter.next();
                            if (!currentAccesslock.getRightProfile().getTitle().equals(RightProfileConstants.ADMINISTRATION)) {
                                currentUser.removeKey(currentAccesslock);
                            }
                        }
                        MandatoryAccessLockEntityLocal teacherManLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.TEACHER).getId());
                        currentUser.removeKey(teacherManLock.getAccessLock());
                    }
                }
            }
        } catch (FinderException e) {
            e.printStackTrace();
        } catch (CreateException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Ordnet Gruppen einem RechteProfile zu, das heisst sie bekommen einen entsprechenen Schluessel zu 
	 * dem Schloss auf dem Mandantenobjekt, das auf diesem Rechteprofil basiert.
	 * @ejb.interface-method view-type="remote" 
	 * @author Olaf Siefart
	 */
    public void assignGroupsToRightProfile(UserVo actor, HashMap containerValues) throws EJBException {
        try {
            if (!HomeInterfaceFinder.getRightManagementHome().checkRightForCallingMethodFromUser(actor, new LinkedList(), ASSIGNGROUPSTORIGHTPROFILE)) throw new EJBException(new AccessCheckFailedException());
            for (Iterator it = containerValues.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer rightProfileId = (Integer) entry.getKey();
                AccessLockEntityLocal accessLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(rightProfileId).getAccessLock();
                String values = (String) entry.getValue();
                ArrayList groupIds = new ArrayList();
                if (values != null) {
                    for (StringTokenizer st = new StringTokenizer(values, ","); st.hasMoreElements(); ) {
                        groupIds.add(new Integer(st.nextToken()));
                    }
                }
                Set oldGroups = getUserIdsFromAccessLock(accessLock);
                removeUserFromAccessLock(accessLock, groupIds, oldGroups);
                groupIds.removeAll(oldGroups);
                for (Iterator iterator = groupIds.iterator(); iterator.hasNext(); ) {
                    HomeInterfaceFinder.getGroupEntityHome().findByPrimaryKey(((Integer) iterator.next())).addKey(accessLock);
                }
            }
        } catch (FinderException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Sucht su dem ueergebenen Schloss alles Nutzer und Gruppen heraus, die einen Schluessel dafuer
	 * besitzen und verpackt sie in das zu dem Schloss gehoerende Rechtprofil
	 * @param accessLock
	 * @return Lister der User
	 * @author Olaf Siefart
	 */
    private RightProfileVo getFilledRightProfile(AccessLockEntityLocal accessLock) throws EJBException, FinderException {
        RightProfileVo rightProfile = accessLock.getRightProfile().getRightProfileVo();
        if (rightProfile.getTitle().equals(RightProfileConstants.ADMINISTRATION)) {
            Collection keys = accessLock.getKeys();
            rightProfile.setUsers(getUserFromAccessLock(accessLock));
            rightProfile.setGroups(getGroupsFromAccessLock(accessLock));
            return rightProfile;
        } else {
            Collection usersWithLock = HomeInterfaceFinder.getUserHome().findByOwningAccessLockBasedOnRightProfileId(rightProfile.getId());
            Collection usersWithKey = getUserWithKeyForOwnLock(usersWithLock, rightProfile.getId(), false);
            rightProfile.setUsers(usersWithKey);
            return rightProfile;
        }
    }

    /**
	 * @author c.ferdinand
	 * 	 
	 */
    private Set getUserWithKeyForOwnLock(Collection users, Integer rightProfileId, boolean onlyIds) {
        HashSet set = new HashSet();
        for (Iterator it = users.iterator(); it.hasNext(); ) {
            UserEntityLocal user = (UserEntityLocal) it.next();
            Collection locks = new ArrayList(user.getAccessLocks());
            for (Iterator lockIt = locks.iterator(); lockIt.hasNext(); ) {
                AccessLockEntityLocal currentLock = (AccessLockEntityLocal) lockIt.next();
                if (!currentLock.getRightProfile().getId().equals(rightProfileId)) {
                    lockIt.remove();
                }
            }
            Collection userKeys = user.getKeys();
            for (Iterator is = locks.iterator(); is.hasNext(); ) {
                AccessLockEntityLocal lockLocal = (AccessLockEntityLocal) is.next();
                Collection lockKeys = lockLocal.getKeys();
                for (Iterator kIt = lockKeys.iterator(); kIt.hasNext(); ) {
                    AccessLockKeyEntityLocal key = (AccessLockKeyEntityLocal) kIt.next();
                    if (userKeys.contains(key)) {
                        if (onlyIds) {
                            set.add(user.getId());
                        } else {
                            set.add(user.getUser());
                        }
                    }
                }
            }
        }
        return set;
    }

    /**
	 * Sucht zu einem Schloss alle Benutzer heraus, die einen Schluessel dafuer besitzen
	 * @param accessLock
	 * @return Lister der User als UserVo
	 * @author Olaf Siefart
	 */
    private Set getUserFromAccessLock(AccessLockEntityLocal accessLock) throws EJBException {
        try {
            Collection allUser = HomeInterfaceFinder.getUserHome().findByObjectState(Constants.ACTIVE);
            Collection keys = accessLock.getKeys();
            HashSet userSet = new HashSet();
            for (Iterator userIterator = allUser.iterator(); userIterator.hasNext(); ) {
                UserEntityLocal user = (UserEntityLocal) userIterator.next();
                Collection userKeys = user.getKeys();
                for (Iterator keyIterator = userKeys.iterator(); keyIterator.hasNext(); ) {
                    AccessLockKeyEntityLocal currentKey = ((AccessLockKeyEntityLocal) keyIterator.next());
                    if (keys.contains(currentKey)) {
                        userSet.add(user.getUser());
                    }
                }
            }
            return userSet;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Sucht zu einem Schloss alle Gruppen heraus, die einen Schluessel dafuer besitzen
	 * @param accessLock
	 * @return Lister der Gruppen als GroupVo
	 * @author Olaf Siefart
	 */
    private Set getGroupsFromAccessLock(AccessLockEntityLocal accessLock) throws EJBException {
        try {
            Collection groups = HomeInterfaceFinder.getGroupEntityHome().findByObjectState(Constants.ACTIVE);
            Collection keys = accessLock.getKeys();
            Set groupSet = new HashSet();
            for (Iterator groupIterator = groups.iterator(); groupIterator.hasNext(); ) {
                GroupEntityLocal group = (GroupEntityLocal) groupIterator.next();
                for (Iterator keyIterator = group.getKeys().iterator(); keyIterator.hasNext(); ) {
                    AccessLockEntityLocal userAccessLock = ((AccessLockKeyEntityLocal) keyIterator.next()).getAccessLock();
                    if (userAccessLock.equals(accessLock)) {
                        groupSet.add(group.getGroupVo());
                    }
                }
            }
            return groupSet;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Sucht zu einem Schloss alle Benutzer heraus, die einen Schluessel dafuer besitzen
	 * 
	 * [CF] changed 12.03.2004 sucht jetzt alle benutzer die schl�ssel zu dem �bergebenen schloss haben, 
	 * oder eigene schl�sser �ffnen k�nnen die auf dem selben rechteprofil basieren wie das �bergeben schloss
	 * oder einen schl�ssel f�r das mandantenschloss haben das auf dem selben rechteprofil basiert wie das 
	 * �bergeben schloss
	 * @param accessLock
	 * @return Lister der UserIds
	 * @author Olaf Siefart
	 */
    private Set getUserIdsFromAccessLock(AccessLockEntityLocal accessLock) throws EJBException {
        try {
            Collection allUser = HomeInterfaceFinder.getUserHome().findByObjectState(Constants.ACTIVE);
            Collection keys = accessLock.getKeys();
            HashSet userSet = new HashSet();
            MandatoryAccessLockEntityLocal mandatoryLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(accessLock.getRightProfile().getId());
            keys.addAll(mandatoryLock.getAccessLock().getKeys());
            for (Iterator userIterator = allUser.iterator(); userIterator.hasNext(); ) {
                UserEntityLocal user = (UserEntityLocal) userIterator.next();
                for (Iterator keyIterator = user.getKeys().iterator(); keyIterator.hasNext(); ) {
                    AccessLockKeyEntityLocal currentKey = (AccessLockKeyEntityLocal) keyIterator.next();
                    AccessLockEntityLocal userAccessLock = currentKey.getAccessLock();
                    if (userAccessLock.equals(accessLock)) {
                        userSet.add(user.getId());
                    }
                    if (keys.contains(currentKey)) {
                        userSet.add(user.getId());
                    }
                }
            }
            userSet.addAll(getUserWithKeyForOwnLock(allUser, accessLock.getRightProfile().getId(), true));
            return userSet;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Sucht zu einem Schloss alle Gruppen heraus, die einen Schluessel dafuer besitzen
	 * @param accessLock
	 * @return Lister der GruppenIds
	 * @author Olaf Siefart
	 */
    private Set getGroupIdsFromAccessLock(AccessLockEntityLocal accessLock) throws EJBException {
        try {
            Collection groups = HomeInterfaceFinder.getGroupEntityHome().findByObjectState(Constants.ACTIVE);
            Collection keys = accessLock.getKeys();
            HashSet groupSet = new HashSet();
            for (Iterator groupIterator = groups.iterator(); groupIterator.hasNext(); ) {
                GroupEntityLocal group = (GroupEntityLocal) groupIterator.next();
                for (Iterator keyIterator = group.getKeys().iterator(); keyIterator.hasNext(); ) {
                    AccessLockEntityLocal userAccessLock = ((AccessLockKeyEntityLocal) keyIterator.next()).getAccessLock();
                    if (userAccessLock.equals(accessLock)) {
                        groupSet.add(group.getId());
                    }
                }
            }
            return groupSet;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Entfernt von allen Nutzern aus der Collection die Schluessel ZUM �BERGEBENEN SCHLOSS
	 * @param accessLock, newUser, oldUser
	 * @param user
	 * @throws EJBException
	 * @author Olaf Siefart
	 */
    private void removeUserFromAccessLock(AccessLockEntityLocal accessLock, Collection newUser, Collection theoldUser) throws EJBException {
        try {
            Collection oldUser = new ArrayList(theoldUser);
            oldUser.removeAll(newUser);
            UserEntityLocalHome userHome = HomeInterfaceFinder.getUserHome();
            for (Iterator iterator = oldUser.iterator(); iterator.hasNext(); ) {
                Integer id = (Integer) iterator.next();
                UserEntityLocal user = userHome.findByPrimaryKey(id);
                user.removeKey(accessLock);
                String rightProfTitle = accessLock.getRightProfile().getTitle();
                if (rightProfTitle.equals(RightProfileConstants.ADMINISTRATION) || rightProfTitle.equals(RightProfileConstants.TEACHER)) {
                    RightProfileEntityLocal rightProfEnt = HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.STUDENT);
                    AccessLockEntityLocal lock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(rightProfEnt.getId()).getAccessLock();
                    user.addKey(lock);
                    AccessLockEntityLocal studentLock = user.getStudentAccessLock();
                    user.addKey(studentLock);
                    if (rightProfTitle.equals(RightProfileConstants.TEACHER)) {
                        Collection usersOpenLocks = user.getOwnOpenAccessLocks();
                        RightProfileEntityLocal teacherProfile = HomeInterfaceFinder.getRightProfileHome().findByTitle(RightProfileConstants.TEACHER);
                        AccessLockEntityLocal teacherMandatoryLock = HomeInterfaceFinder.getMandatoryAccessLockLocalHome().findByRightProfile(teacherProfile.getId()).getAccessLock();
                        if (getUserFromAccessLock(teacherMandatoryLock).contains(user.getUser())) {
                            user.removeKey(teacherMandatoryLock);
                        }
                        for (Iterator it = usersOpenLocks.iterator(); it.hasNext(); ) {
                            AccessLockEntityLocal currentLock = (AccessLockEntityLocal) it.next();
                            if (currentLock.getRightProfile().getTitle().equals(RightProfileConstants.TEACHER)) {
                                user.removeKey(currentLock);
                            }
                        }
                    }
                }
            }
        } catch (FinderException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

    /**
	 * Entfernt von allen Gruppen aus der Collection die Schluessel
	 * @param accessLock, newGrous, oldGroups
	 * @param user
	 * @throws EJBException
	 * @author Olaf Siefart
	 */
    private void removeGroupFromAccessLock(AccessLockEntityLocal accessLock, Collection newGroups, Collection oldGroups) throws EJBException {
        try {
            oldGroups.removeAll(newGroups);
            GroupEntityLocalHome groupHome = HomeInterfaceFinder.getGroupEntityHome();
            for (Iterator iterator = oldGroups.iterator(); iterator.hasNext(); ) {
                groupHome.findByPrimaryKey(((GroupVo) iterator.next()).getId()).removeKey(accessLock);
            }
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }
}
