package com.jxml.qare.qhome.db;

import java.sql.*;
import java.util.*;
import com.jxml.qare.qhome.*;
import com.jxml.qare.qhome.remoteAccount.*;
import java.io.*;

public final class DBLocalPrivileges extends DBBase implements DeleteListener, CreateListener {

    private static HashMap reference = new HashMap();

    static String tableName = "localPrivileges";

    static DBLocalPrivileges db = new DBLocalPrivileges();

    private DBLocalPrivileges() {
        DBBase.dbBases.put(tableName, this);
        DBAccount.db.addDeleteListener(this);
        DBAccount.db.addCreateListener(this);
    }

    public String tableName() {
        return tableName;
    }

    public void createTable() throws Exception {
        String query = "CREATE TABLE localPrivileges (" + "applicationId INT UNSIGNED NOT NULL," + "privilegeId INT UNSIGNED NOT NULL," + "parentId INT NOT NULL ," + "grantorId INT UNSIGNED NOT NULL," + "delegeeId INT UNSIGNED NOT NULL," + "projectId INT NOT NULL," + "localPrivilegeId INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (applicationId, privilegeId, grantorId, delegeeId, projectId, parentId)," + "aquisitionDate DATETIME NOT NULL," + "delegationLimit INT NOT NULL," + "delegationCount INT NOT NULL," + "delegationDepth INT NOT NULL," + "INDEX applicationIdIndex(applicationId)," + "INDEX privilegeIdIndex(privilegeId)," + "INDEX parentIdIndex(parentId)," + "INDEX grantorIdIndex(grantorId)," + "INDEX delegeeIdIndex(delegeeId)," + "INDEX projectIdIndex (projectId)," + "UNIQUE localPrivilegeIdIndex (localPrivilegeId))";
        int rc = executeUpdate(query);
    }

    public void deleteEvent(DeleteEvent deleteEvent) throws Exception {
        Object source = deleteEvent.getSource();
        if (source instanceof DBAccount.Row) {
            DBAccount.Row account = (DBAccount.Row) source;
            List list = getAllDelegeePrivileges(account.getUser());
            int i, s;
            s = list.size();
            for (i = 0; i < s; ++i) {
                DBLocalPrivileges.Row row = (DBLocalPrivileges.Row) list.get(i);
                row.delete();
                row.notifyUsers();
            }
        }
    }

    /**
     * Add remote privileges to a new user.
     */
    public void createEvent(CreateEvent createEvent) throws Exception {
        Object source = createEvent.getSource();
        if (source instanceof DBAccount.Row) {
            DBAccount.Row account = (DBAccount.Row) source;
            DBUser.Row user = account.getUser();
            List list = getDelegeePrivileges(Setup.remoteUser);
            int i, s;
            s = list.size();
            for (i = 0; i < s; ++i) {
                DBLocalPrivileges.Row row = (DBLocalPrivileges.Row) list.get(i);
                DBExportDelegation export = row.getExport(user);
                export.delegee = user.getName();
                createSimple(row.parent, export);
            }
        }
    }

    /**
     * Adds or updates a privilege; notifies users.
     */
    public DBLocalPrivileges.Row create(DBExportDelegation export) throws Exception {
        DBLocalPrivileges.Row parent = getParent(export);
        if (!valid(parent, export)) return null;
        DBLocalPrivileges.Row exist = createSimple(parent, export);
        DBUser.Row delegeeRow = Setup.dbUser.get(export.delegee);
        if (delegeeRow.equals(Setup.remoteUser)) {
            String oldDelegee = export.delegee;
            List accounts = Setup.dbAccount.get();
            int i;
            int ss = accounts.size();
            for (i = 0; i < ss; ++i) {
                DBAccount.Row anyAccount = (DBAccount.Row) accounts.get(i);
                DBUser.Row anyUser = anyAccount.getUser();
                if (!parent.matchUser(anyUser) && !anyUser.equals(Setup.nullUser) && !anyUser.equals(Setup.remoteUser)) {
                    export.delegee = anyUser.getName();
                    exist = createSimple(parent, export);
                }
            }
            export.delegee = oldDelegee;
        }
        return exist;
    }

    public boolean valid(DBLocalPrivileges.Row parent, DBExportDelegation export) throws Exception {
        if (parent == null) {
            return false;
        }
        DBUser.Row delegee = Setup.dbUser.get(export.delegee);
        if (Setup.dbAccount.get(delegee) == null) {
            return false;
        }
        DBUser.Row grantor = Setup.dbUser.get(export.grantor);
        if (delegee.equals(Setup.remoteUser)) return Setup.dbLocalPrivileges.hasPrivilege(grantor, Setup.qhomeApplication, Setup.remotePrivilege);
        if (delegee.equals(Setup.homeUser)) return Setup.dbLocalPrivileges.hasPrivilege(grantor, Setup.qhomeApplication, Setup.homePrivilege);
        return true;
    }

    public DBLocalPrivileges.Row createSimple(DBLocalPrivileges.Row parent, DBExportDelegation export) throws Exception {
        DBLocalPrivileges.Row exist = get(export);
        if (exist == null) {
            exist = create(parent, export);
            if (exist != null) {
                exist.notifyUsers();
                parent.notifyUsers();
            }
        } else exist.updatePrivilege(export.delegationDepth, export.delegationLimit);
        return exist;
    }

    public DBLocalPrivileges.Row create(DBLocalPrivileges.Row parent, DBExportDelegation export) throws Exception {
        DBApplication.Row application = Setup.dbApplication.get(export.application);
        DBPrivilege.Row privilege = Setup.dbPrivilege.get(export.privilege);
        DBUser.Row grandGrantor = Setup.dbUser.get(export.grandGrantor);
        DBUser.Row grantor = Setup.dbUser.get(export.grantor);
        DBUser.Row delegee = Setup.dbUser.get(export.delegee);
        DBProject.Row project = Setup.dbProject.get(export.project);
        DBLocalPrivileges.Row row = create(application, privilege, parent, grantor, delegee, project, export.aquisitionDate, export.delegationLimit, export.delegationCount, export.delegationDepth);
        return row;
    }

    public DBLocalPrivileges.Row create(DBApplication.Row application, DBPrivilege.Row privilege, DBLocalPrivileges.Row parent, DBUser.Row grantor, DBUser.Row delegee, DBProject.Row project, java.util.Date aquisitionDate, int delegationLimit, int delegationCount, int delegationDepth) throws Exception {
        int parentId = -1;
        if (parent != null) {
            parentId = parent.getLocalPrivilegeId();
            if (!parent.valid(delegationDepth, delegationLimit, 0)) return null;
            if (!parent.delegee.equals(grantor)) return null;
            if (!project.getName().startsWith(parent.project.getName())) return null;
            int newCount = parent.delegationCount + delegationLimit;
            parent.delegationCount = newCount;
            parent.update();
            parent.notifyUsers();
        }
        int projectId = 1;
        if (project != null) projectId = project.getProjectId();
        String od = Setup.dateFormater.format(aquisitionDate);
        String query = "insert into localPrivileges (" + "applicationId, " + "privilegeId, " + "parentId, " + "grantorId, " + "delegeeId, " + "projectId, " + "aquisitionDate, " + "delegationLimit, " + "delegationCount, " + "delegationDepth) " + "values(" + application.getApplicationId() + ", " + privilege.getPrivilegeId() + ", " + parentId + ", " + grantor.getUserId() + ", " + delegee.getUserId() + ", " + projectId + ", " + "'" + od + "', " + delegationLimit + ", " + delegationCount + ", " + delegationDepth + ")";
        int rc = executeUpdate(query);
        DBLocalPrivileges.Row row = get(application, privilege, parent, grantor, delegee, project);
        sendCreateEvent(row);
        return row;
    }

    public boolean hasPrivilege(DBUser.Row delegee, DBApplication.Row application, DBPrivilege.Row privilege) throws Exception {
        DBUser.Row remote = Setup.remoteUser;
        String query = "select * " + " from localPrivileges where " + "       (delegeeId=" + delegee.getUserId() + "	  AND  applicationId=" + Setup.qhomeApplication.getApplicationId() + "        AND  privilegeId=" + Setup.superuserPrivilege.getPrivilegeId() + "       ) OR" + "       (   (delegeeId=" + delegee.getUserId() + "             OR delegeeId=" + remote.getUserId() + "           )" + "	  AND applicationId=" + application.getApplicationId() + "        AND (privilegeId=" + privilege.getPrivilegeId() + "             OR" + "             privilegeId=" + Setup.allPrivilege.getPrivilegeId() + "             )" + "       )";
        return getRow(query) != null;
    }

    public DBLocalPrivileges.Row get(int localPrivilegeId) throws Exception {
        DBLocalPrivileges.Row localPrivilege = (DBLocalPrivileges.Row) getWeak(new Integer(localPrivilegeId));
        if (localPrivilege != null) return localPrivilege;
        String query = "select * " + "from localPrivileges where" + " localPrivilegeId=" + localPrivilegeId;
        return (DBLocalPrivileges.Row) getRow(query);
    }

    public DBLocalPrivileges.Row get(DBApplication.Row application, DBPrivilege.Row privilege, DBLocalPrivileges.Row parent, DBUser.Row grantor, DBUser.Row delegee, DBProject.Row project) throws Exception {
        int parentId = -1;
        if (parent != null) parentId = parent.getLocalPrivilegeId();
        int projectId = 1;
        if (project != null) projectId = project.getProjectId();
        String query = "select * " + "from localPrivileges where" + " applicationId=" + application.getApplicationId() + " and privilegeId=" + privilege.getPrivilegeId() + " and parentId=" + parentId + " and grantorId=" + grantor.getUserId() + " and delegeeId=" + delegee.getUserId() + " and projectId=" + projectId;
        return (DBLocalPrivileges.Row) getRow(query);
    }

    public DBLocalPrivileges.Row get(DBLocalPrivileges.Row parent, DBExportDelegation export) throws Exception {
        DBApplication.Row application = Setup.dbApplication.get(export.application);
        DBPrivilege.Row privilege = Setup.dbPrivilege.get(export.privilege);
        DBUser.Row grandGrantor = Setup.dbUser.get(export.grandGrantor);
        DBUser.Row grantor = Setup.dbUser.get(export.grantor);
        DBUser.Row delegee = Setup.dbUser.get(export.delegee);
        DBProject.Row project = Setup.dbProject.get(export.project);
        return get(application, privilege, parent, grantor, delegee, project);
    }

    public DBLocalPrivileges.Row get(DBExportDelegation export) throws Exception {
        DBApplication.Row application = Setup.dbApplication.get(export.application);
        DBPrivilege.Row privilege = Setup.dbPrivilege.get(export.privilege);
        DBUser.Row grandGrantor = Setup.dbUser.get(export.grandGrantor);
        DBUser.Row grantor = Setup.dbUser.get(export.grantor);
        DBUser.Row delegee = Setup.dbUser.get(export.delegee);
        DBProject.Row project = Setup.dbProject.get(export.project);
        DBProject.Row parentProject = Setup.dbProject.get(export.parentProject);
        String query = "select priv.* from localPrivileges priv, localPrivileges parPriv" + " where priv.parentId=parPriv.localPrivilegeId" + " and priv.applicationId=" + application.getApplicationId() + " and priv.privilegeId=" + privilege.getPrivilegeId() + " and priv.grantorId=" + grantor.getUserId() + " and parPriv.grantorId=" + grandGrantor.getUserId() + " and priv.delegeeId=" + delegee.getUserId() + " and priv.projectId=" + project.getProjectId() + " and parPriv.projectId=" + parentProject.getProjectId();
        DBLocalPrivileges.Row old = (DBLocalPrivileges.Row) getRow(query);
        if (old == null) return null;
        DBLocalPrivileges.Row p = old.getParent();
        int oldLimit = old.delegationLimit;
        if (p.valid(export.delegationDepth, export.delegationLimit, oldLimit)) return old;
        return null;
    }

    /**
     * Returns a parent with sufficient limit and depth, or null.
     */
    public DBLocalPrivileges.Row getParent(DBExportDelegation export) throws Exception {
        DBLocalPrivileges.Row old = get(export);
        if (old != null) return old.getParent();
        DBApplication.Row application = Setup.dbApplication.get(export.application);
        DBPrivilege.Row privilege = Setup.dbPrivilege.get(export.privilege);
        DBUser.Row grandGrantor = Setup.dbUser.get(export.grandGrantor);
        if ("".equals(export.grantor)) {
            System.out.println("lp grantor is null!");
            return null;
        }
        DBUser.Row grantor = Setup.dbUser.get(export.grantor);
        DBUser.Row delegee = Setup.dbUser.get(export.delegee);
        DBProject.Row project = Setup.dbProject.get(export.project);
        String query = "select * " + "from localPrivileges where" + " ( ( applicationId=" + Setup.qhomeApplication.getApplicationId() + " and privilegeId=" + Setup.superuserPrivilege.getPrivilegeId() + " ) or ( applicationId=" + application.getApplicationId() + " and ( privilegeId=" + privilege.getPrivilegeId() + " or privilegeId=" + Setup.allPrivilege.getPrivilegeId() + " ) ) ) and grantorId=" + grandGrantor.getUserId() + " and delegeeId=" + grantor.getUserId() + " and  projectId=" + Setup.dbProject.get(export.parentProject).getProjectId();
        List list = getRows(query);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            DBLocalPrivileges.Row p = (DBLocalPrivileges.Row) it.next();
            if (p.valid(export.delegationDepth, export.delegationLimit, 0)) return p;
        }
        return null;
    }

    public List getDelegeePrivileges(DBUser.Row delegee) throws Exception {
        String query = "select * " + "from localPrivileges where " + "delegeeId=" + delegee.getUserId();
        return getRows(query);
    }

    public List getAllDelegeePrivileges(DBUser.Row delegee) throws Exception {
        List todo = getDelegeePrivileges(delegee);
        ArrayList done = new ArrayList();
        if (todo.size() == 0) return done;
        while (todo.size() > 0) {
            DBLocalPrivileges.Row p = (DBLocalPrivileges.Row) todo.remove(todo.size() - 1);
            todo.addAll(p.getChildren());
            done.add(p);
        }
        return done;
    }

    public DBRow createObj(ResultSet rs) throws Exception {
        DBApplication.Row application = Setup.dbApplication.get(rs.getInt("applicationId"));
        DBPrivilege.Row privilege = Setup.dbPrivilege.get(rs.getInt("privilegeId"));
        DBLocalPrivileges.Row parent = null;
        int parentId = rs.getInt("parentId");
        if (parentId > -1) parent = get(parentId);
        DBUser.Row grantor = Setup.dbUser.get(rs.getInt("grantorId"));
        DBUser.Row delegee = Setup.dbUser.get(rs.getInt("delegeeId"));
        int projectId = rs.getInt("projectId");
        DBProject.Row project = Setup.dbProject.get(projectId);
        java.util.Date aquisitionDate = Setup.dateFormater.parse(rs.getString("aquisitionDate"));
        int delegationLimit = rs.getInt("delegationLimit");
        int delegationCount = rs.getInt("delegationCount");
        int delegationDepth = rs.getInt("delegationDepth");
        int localPrivilegeId = rs.getInt("localPrivilegeId");
        return new DBLocalPrivileges.Row(application, privilege, parent, grantor, delegee, project, aquisitionDate, delegationLimit, delegationCount, delegationDepth, localPrivilegeId);
    }

    public void sendUserUpdate(DBUser.Row user, DBLocalPrivileges.Row p) throws Exception {
        ArrayList prime = new ArrayList();
        if (user == null) return;
        DBAccount.Row usrAcc = user.getAccount();
        DBSession.Row theSession = usrAcc.session;
        if (theSession != null) {
            DBExportDelegation export = p.getExport(user);
            ArrayList list = new ArrayList();
            list.add(export.delegee);
            list.add(export.application);
            list.add(export.privilege);
            list.add(export.project);
            list.add(export.parentProject);
            list.add(export.grandGrantor);
            list.add(export.grantor);
            list.add(new Long((export.aquisitionDate).getTime()));
            list.add(new Integer(export.delegationLimit));
            list.add(new Integer(export.delegationCount));
            list.add(new Integer(export.delegationDepth));
            list.add(new Integer(export.delegationDistance));
            prime.add(list);
            ActivationMsgReply reply = new ActivationMsgReply(user.getName(), prime);
            reply.send(theSession);
        }
    }

    public void backup(DataOutputStream dos) throws Exception {
        System.out.println("Writing backup for table " + tableName());
        Connection con = Setup.con;
        Statement stmt = con.createStatement();
        DBLocalPrivileges.Row root = get(1);
        root.backup(dos);
        List todo = new ArrayList();
        todo.add(root);
        while (todo.size() > 0) {
            DBLocalPrivileges.Row parent = (DBLocalPrivileges.Row) todo.remove(todo.size() - 1);
            List kids = parent.getChildren();
            while (kids.size() > 0) {
                DBLocalPrivileges.Row child = (DBLocalPrivileges.Row) kids.remove(kids.size() - 1);
                child.backup(dos);
                todo.add(child);
            }
        }
        stmt.close();
    }

    public void restore(String fileNamePrefix, DataInputStream dis) throws Exception {
        restored = true;
        System.out.println("Restoring table " + tableName());
        boolean root = true;
        while (true) {
            DBExportDelegation x = new DBExportDelegation();
            try {
                x.application = restoreString(dis);
            } catch (EOFException eof) {
                return;
            }
            x.privilege = restoreString(dis);
            x.grandGrantor = restoreString(dis);
            x.grantor = restoreString(dis);
            x.delegee = restoreString(dis);
            x.project = restoreString(dis);
            x.parentProject = restoreString(dis);
            x.aquisitionDate = new java.util.Date(dis.readLong());
            x.delegationLimit = dis.readInt();
            x.delegationCount = dis.readInt();
            x.delegationDepth = dis.readInt();
            DBLocalPrivileges.Row parent = null;
            if (root) root = false; else {
                parent = getParent(x);
                if (parent == null) {
                    throw new Exception("Missing parent");
                }
            }
            create(parent, x);
        }
    }

    public class Row implements DBRow {

        private DBApplication.Row application;

        private DBPrivilege.Row privilege;

        private DBLocalPrivileges.Row parent;

        private DBUser.Row grantor;

        private DBUser.Row delegee;

        private DBProject.Row project;

        private int localPrivilegeId;

        private boolean closed = false;

        public java.util.Date aquisitionDate;

        public int delegationLimit;

        public int delegationCount;

        public int delegationDepth;

        public Row(DBApplication.Row application, DBPrivilege.Row privilege, DBLocalPrivileges.Row parent, DBUser.Row grantor, DBUser.Row delegee, DBProject.Row project, java.util.Date aquisitionDate, int delegationLimit, int delegationCount, int delegationDepth, int localPrivilegeId) throws Exception {
            this.application = application;
            this.privilege = privilege;
            this.parent = parent;
            this.grantor = grantor;
            this.delegee = delegee;
            this.project = project;
            this.aquisitionDate = aquisitionDate;
            this.delegationLimit = delegationLimit;
            this.delegationCount = delegationCount;
            this.delegationDepth = delegationDepth;
            this.localPrivilegeId = localPrivilegeId;
        }

        /**
	 * Returns -1 if user is not associated with this privilege
	 */
        public int distance(DBUser.Row user) {
            if (user.equals(delegee)) return 0;
            if (user.equals(grantor)) return 1;
            if (parent == null) return -1;
            return parent.distance(user) + 1;
        }

        public DBExportDelegation getExport(DBUser.Row user) {
            DBExportDelegation ex = new DBExportDelegation();
            ex.user = user.getName();
            ex.application = application.getName();
            ex.privilege = privilege.getName();
            ex.system = QConfig.returnAddress();
            ex.grantor = grantor.getName();
            ex.delegee = delegee.getName();
            ex.aquisitionDate = aquisitionDate;
            ex.delegationLimit = delegationLimit;
            ex.delegationCount = delegationCount;
            ex.delegationDepth = delegationDepth;
            ex.delegationDistance = distance(user);
            if (project == null) ex.project = ""; else ex.project = project.getName();
            if (parent == null) ex.parentProject = ""; else ex.parentProject = parent.project.getName();
            if (parent == null) ex.grandGrantor = ""; else ex.grandGrantor = parent.grantor.getName();
            return ex;
        }

        public void update() throws Exception {
            String od = Setup.dateFormater.format(aquisitionDate);
            int parentId = -1;
            if (parent != null) parentId = parent.getLocalPrivilegeId();
            int projectId = 1;
            if (project != null) projectId = project.getProjectId();
            String query = "update localPrivileges set " + "aquisitionDate=" + "'" + od + "'," + "delegationLimit=" + delegationLimit + "," + "delegationCount=" + delegationCount + "," + "delegationDepth=" + delegationDepth + " where" + " applicationId=" + application.getApplicationId() + " and privilegeId=" + privilege.getPrivilegeId() + " and parentId=" + parentId + " and grantorId=" + grantor.getUserId() + " and delegeeId=" + delegee.getUserId() + " and projectId=" + projectId;
            int rc = executeUpdate(query);
        }

        public void delete() throws Exception {
            if (closed) return;
            closed = true;
            delegationLimit = 0;
            delegationCount = 0;
            delegationDepth = 0;
            sendDeleteEvent(this);
            deleteWeak(this);
            int parentId = -1;
            if (parent != null) parentId = parent.getLocalPrivilegeId();
            int projectId = 1;
            if (project != null) projectId = project.getProjectId();
            String query = "delete from localPrivileges where" + " applicationId=" + application.getApplicationId() + " and privilegeId=" + privilege.getPrivilegeId() + " and parentId=" + parentId + " and grantorId=" + grantor.getUserId() + " and delegeeId=" + delegee.getUserId() + " and projectId=" + projectId;
            int rc = executeUpdate(query);
        }

        public boolean closed() {
            return closed;
        }

        public DBApplication.Row getApplicationId() {
            return application;
        }

        public DBPrivilege.Row getPrivilege() {
            return privilege;
        }

        public DBUser.Row getGrantor() {
            return grantor;
        }

        public DBUser.Row getDelegee() {
            return delegee;
        }

        public DBProject.Row getProject() {
            return project;
        }

        public DBLocalPrivileges.Row getParent() {
            return parent;
        }

        public int getLocalPrivilegeId() {
            return localPrivilegeId;
        }

        public int getDepth() {
            return delegationDepth;
        }

        public List getKeys() {
            List l = new ArrayList();
            l.add(getKey());
            return l;
        }

        public Object getKey() {
            return new Integer(localPrivilegeId);
        }

        public boolean equals(Object o) {
            if (!(o instanceof DBLocalPrivileges.Row)) return false;
            DBLocalPrivileges.Row lpr = (DBLocalPrivileges.Row) o;
            return localPrivilegeId == lpr.localPrivilegeId;
        }

        public void backup(DataOutputStream dos) throws Exception {
            backupString(dos, application.getName());
            backupString(dos, privilege.getName());
            if (parent == null) {
                backupString(dos, "");
            } else {
                backupString(dos, parent.grantor.getName());
            }
            backupString(dos, grantor.getName());
            backupString(dos, delegee.getName());
            backupString(dos, project.getName());
            if (parent == null) {
                backupString(dos, "");
            } else {
                backupString(dos, parent.project.getName());
            }
            dos.writeLong(aquisitionDate.getTime());
            dos.writeInt(delegationLimit);
            dos.writeInt(delegationCount);
            dos.writeInt(delegationDepth);
        }

        public void updateChildDepth() throws Exception {
            List step1 = getChildren();
            while (step1.size() > 0) {
                DBLocalPrivileges.Row child = (DBLocalPrivileges.Row) step1.remove(step1.size() - 1);
                int oldDepth = child.getDepth();
                int maxDepth = child.getParent().delegationDepth - 1;
                if (oldDepth > maxDepth) {
                    child.delegationDepth = maxDepth;
                    child.update();
                    child.notifyUsers();
                    List step2 = child.getChildren();
                    if (child.delegationDepth == 0) {
                        List subChild = child.getAllChildren();
                        for (int a = 0; a < subChild.size(); ++a) {
                            DBLocalPrivileges.Row child2 = (DBLocalPrivileges.Row) subChild.get(a);
                            child2.delete();
                            child2.notifyUsers();
                        }
                        child.delegationCount = 1;
                        child.update();
                    } else {
                        step1.addAll(step2);
                    }
                }
            }
        }

        public boolean valid(int delegationDepth, int newLimit, int oldLimit) {
            if (this.delegationDepth > -1 && this.delegationDepth < delegationDepth) {
                return false;
            } else {
                return (this.delegationLimit - this.delegationCount) >= (newLimit - oldLimit);
            }
        }

        public List getChildren() throws Exception {
            String query = "select * " + "from localPrivileges where" + " parentId=" + localPrivilegeId;
            return getRows(query);
        }

        public List getAllChildren() throws Exception {
            List todo = getChildren();
            List done = new ArrayList();
            if (todo.size() == 0) return done;
            while (todo.size() > 0) {
                DBLocalPrivileges.Row p = (DBLocalPrivileges.Row) todo.remove(todo.size() - 1);
                todo.addAll(p.getChildren());
                done.add(p);
            }
            return done;
        }

        public void updatePrivilege(int newDelegeeDepth, int newDelegeeLimit) throws Exception {
            int newCount = 1;
            newCount = parent.delegationCount - delegationLimit + newDelegeeLimit;
            parent.delegationCount = newCount;
            parent.update();
            parent.notifyUsers();
            List children = new ArrayList();
            if (delegationCount > 1) {
                children = getAllChildren();
            }
            if (newDelegeeLimit < delegationCount) {
                if (newDelegeeLimit == 0) {
                    delete();
                } else {
                    delegationCount = 1;
                    delegationLimit = newDelegeeLimit;
                    delegationDepth = newDelegeeDepth;
                    update();
                }
                for (int j = 0; j < children.size(); ++j) {
                    DBLocalPrivileges.Row p = (DBLocalPrivileges.Row) children.get(j);
                    p.delete();
                    p.notifyUsers();
                }
            } else {
                parent.delegationCount = newCount;
                parent.update();
                delegationLimit = newDelegeeLimit;
                int oldDepth = delegationDepth;
                delegationDepth = newDelegeeDepth;
                if (newDelegeeDepth < oldDepth) {
                    if (newDelegeeDepth == 0) {
                        delegationCount = 1;
                        for (int l = 0; l < children.size(); ++l) {
                            DBLocalPrivileges.Row p = (DBLocalPrivileges.Row) children.get(l);
                            p.delete();
                            p.notifyUsers();
                        }
                    } else {
                        updateChildDepth();
                    }
                }
                update();
            }
            notifyUsers();
        }

        public synchronized void notifyUsers() throws Exception {
            notifyUsers(this);
        }

        public synchronized void notifyUsers(DBLocalPrivileges.Row lp) throws Exception {
            DBUser.Row user = delegee;
            sendUserUpdate(user, lp);
            if (parent != null) parent.notifyUsers(lp);
        }

        public boolean matchUser(DBUser.Row u) {
            if (u.equals(getDelegee())) return true;
            DBLocalPrivileges.Row parent = getParent();
            if (parent != null) return parent.matchUser(u);
            if (u.equals(getGrantor())) return true;
            return false;
        }
    }
}
