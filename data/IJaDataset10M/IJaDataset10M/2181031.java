package org.stikiweb.contentMagnager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Allen L (BigLee) Haslup
 *
 */
public class PermissionManager implements PageChangeListener {

    public static final int NO_ACCESS = 0;

    public static final int READ = 1;

    public static final int COMMENT = 2;

    public static final int AUDIT = 3;

    public static final int ATTACH = 4;

    public static final int EDIT = 5;

    public static final int EXTEND = 6;

    public static final int ADMIN = 7;

    protected HashMap userList;

    public class PageSpec {

        public HashMap userPermLevel = new HashMap();

        public int maxStorage = 0;

        public boolean reservedStorage = false;

        public int maxStoragePerAttach = 0;

        public String templateName = null;

        public String toString() {
            return "Max Storage " + maxStorage + (reservedStorage ? " (reserved) " : " ") + " Max per Attachment " + maxStoragePerAttach + " Template=" + templateName;
        }
    }

    protected TreeMap pageSpecs;

    private PermissionManager() {
    }

    ;

    private static PermissionManager solo = null;

    public static synchronized PermissionManager solo() {
        if (solo == null) {
            solo = new PermissionManager();
            solo.initialize();
        }
        return solo;
    }

    public static void resetPM() {
        if (solo != null) {
            solo.userList = null;
            solo.pageSpecs = null;
            solo = null;
        }
    }

    protected void initialize() {
        userList = readUserList();
        PageManager.solo().addPageChangeListener("Admin.Users", this);
        pageSpecs = readPageSpecs();
        PageManager.solo().addPageChangeListener("Admin.Pages", this);
    }

    protected HashMap readUserList() {
        HashMap retval = new HashMap();
        Vector usersVec = PageManager.solo().getConfigurationTable("Admin.Users");
        Iterator iUser = usersVec.iterator();
        while (iUser.hasNext()) {
            Vector userVec = (Vector) iUser.next();
            if (userVec.size() >= 2) {
                String user = (String) userVec.get(0);
                String password = (String) userVec.get(1);
                retval.put(user, password);
                String roles = "";
                if (userVec.size() > 2) {
                    roles = (String) userVec.get(2);
                    StringTokenizer st = new StringTokenizer(roles, ", ");
                    ArrayList roleAL = new ArrayList();
                    while (st.hasMoreTokens()) {
                        roleAL.add(st.nextToken());
                    }
                    retval.put(user + "$roles", roleAL);
                }
                StikiWebContext.mutter("User:" + user + " PW:" + password);
                StikiWebContext.mutter("Roles:" + user + " = " + roles);
            }
        }
        return retval;
    }

    protected TreeMap readPageSpecs() {
        TreeMap retval = new TreeMap();
        Vector pagesVec = PageManager.solo().getConfigurationTable("Admin.Pages");
        Iterator iPage = pagesVec.iterator();
        String currentPage = "";
        while (iPage.hasNext()) {
            Vector pageVec = (Vector) iPage.next();
            if (pageVec.size() >= 1) {
                String field1 = (String) pageVec.get(0);
                field1 = field1.trim();
                if (field1.length() > 0) {
                    if (field1.equals("..")) field1 = "";
                    currentPage = field1;
                    PageSpec ps = (PageSpec) retval.get(currentPage);
                    if (ps == null) {
                        ps = new PageSpec();
                        retval.put(currentPage, ps);
                    }
                    if (pageVec.size() >= 2) {
                        String field2 = (String) pageVec.get(1);
                        field2 = field2.trim();
                        if (field2.length() > 0) try {
                            ps.maxStorage = Integer.parseInt(field2);
                        } catch (NumberFormatException e) {
                            StikiWebContext.mutter(currentPage + " contains an illegal maxStorage value (" + field2 + ")");
                        }
                        if (pageVec.size() >= 3) {
                            String field3 = (String) pageVec.get(2);
                            field3 = field3.trim().toUpperCase();
                            ps.reservedStorage = field3.startsWith("Y") || field3.startsWith("T");
                            if (pageVec.size() >= 4) {
                                String field4 = (String) pageVec.get(3);
                                field4 = field4.trim();
                                if (field4.length() > 0) try {
                                    ps.maxStoragePerAttach = Integer.parseInt(field4);
                                } catch (NumberFormatException e) {
                                    StikiWebContext.mutter(currentPage + " contains an illegal maxStoragePerAttachment value (" + field2 + ")");
                                }
                                if (pageVec.size() >= 5) {
                                    String field5 = (String) pageVec.get(4);
                                    field5 = field5.trim();
                                    if (field5.length() > 0) {
                                        ps.templateName = field5;
                                    }
                                }
                            }
                        }
                    }
                    StikiWebContext.mutter("Page:" + currentPage + " Specs:" + ps.toString());
                } else {
                    if (pageVec.size() >= 3) {
                        String user = (String) pageVec.get(1);
                        user = user.trim();
                        String permLevelSt = (String) pageVec.get(2);
                        permLevelSt = permLevelSt.trim();
                        Integer permLevel = new Integer(permLevelFromString(permLevelSt));
                        PageSpec ps = (PageSpec) retval.get(currentPage);
                        if (ps == null) {
                            ps = new PageSpec();
                            retval.put(currentPage, ps);
                        }
                        ps.userPermLevel.put(user, permLevel);
                        StikiWebContext.mutter("User:" + user + " Level:" + permLevel);
                    }
                }
            }
        }
        return retval;
    }

    public synchronized void updatePageSpec(String page, PageSpec spec) {
        pageSpecs.put(page, spec);
        PageInfoNode.markAllForRecalc();
    }

    public synchronized void removePageSpec(String page) throws PageManagerException, IOException {
        PageSpec ps = getPageSpec(page);
        if (ps == null) return;
        getPageSpecs().remove(page);
        rewritePageSpecs();
        PageInfoNode.markAllForRecalc();
    }

    public synchronized void rewritePageSpecs() throws PageManagerException, IOException {
        Vector lines = new Vector();
        Iterator pSpecEntries = pageSpecs.entrySet().iterator();
        while (pSpecEntries.hasNext()) {
            Vector fields = new Vector();
            lines.add(fields);
            Map.Entry specEntry = (Map.Entry) pSpecEntries.next();
            String pageName = (String) specEntry.getKey();
            if (pageName == null || pageName.length() == 0) pageName = "..";
            fields.add(pageName);
            PageSpec spec = (PageSpec) specEntry.getValue();
            fields.add(Integer.toString(spec.maxStorage));
            fields.add(spec.reservedStorage ? "Y" : "N");
            fields.add(Integer.toString(spec.maxStoragePerAttach));
            fields.add(spec.templateName != null ? spec.templateName : "");
            HashMap userPerms = spec.userPermLevel;
            if (userPerms != null) {
                Iterator perms = userPerms.entrySet().iterator();
                while (perms.hasNext()) {
                    Vector permFields = new Vector();
                    lines.add(permFields);
                    Map.Entry perm = (Map.Entry) perms.next();
                    permFields.add("");
                    permFields.add(perm.getKey());
                    permFields.add(permStringFromLevel(((Integer) perm.getValue()).intValue()));
                }
            }
        }
        PageManager.solo().saveConfigurationTable("Admin.Pages", lines);
    }

    public int permLevelFromString(String permSt) {
        for (int i = NO_ACCESS; i <= ADMIN; i++) {
            if (levelNames[i].equalsIgnoreCase(permSt)) {
                return i;
            }
        }
        return READ;
    }

    protected static final String[] levelNames = { "NO_ACCESS", "READ", "COMMENT", "AUDIT", "ATTACH", "EDIT", "EXTEND", "ADMIN" };

    public String permStringFromLevel(int level) {
        return levelNames[level];
    }

    public boolean checkPassword(String username, String password) {
        if (password == null) return false;
        String realPw = (String) getUserList().get(username);
        return (password.equals(realPw));
    }

    public TreeMap userNames() {
        TreeMap retval = new TreeMap();
        Iterator it = getUserList().keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            retval.put(name, name);
        }
        return retval;
    }

    public PageSpec getPageSpec(String page) {
        return (PageSpec) getPageSpecs().get(page);
    }

    public String getUserName(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session == null) return "Visitor";
        String user = (String) session.getAttribute("username");
        if (user == null) return "Visitor";
        return user;
    }

    public String getGuestName(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session == null) return "";
        String ident = (String) session.getAttribute("guestname");
        if (ident == null) return "";
        return ident;
    }

    public boolean canDelete(String user, String page) {
        if ("Main".equalsIgnoreCase(page) || "..".equalsIgnoreCase(page) || page.length() == 0) {
            return false;
        }
        boolean isAttachment = page.indexOf('/') >= 0;
        if (isAttachment) {
            return userPermLevelForPage(user, page) >= PermissionManager.ATTACH;
        } else {
            return userPermLevelForPage(user, page) >= PermissionManager.EXTEND && userPermLevelForParentPage(user, page) >= PermissionManager.EXTEND;
        }
    }

    public int userPermLevelForPage(String user, String page) {
        if (page.startsWith("NullPage")) {
            return NO_ACCESS;
        }
        int parentPerm = userPermLevelForParentPage(user, page);
        if (parentPerm == ADMIN) return ADMIN;
        PageSpec ps = (PageSpec) pageSpecs.get(page);
        if (ps == null) return parentPerm;
        Integer perm = (Integer) ps.userPermLevel.get(user);
        if (perm == null) {
            ArrayList roleAL = (ArrayList) getUserList().get(user + "$roles");
            if (roleAL != null) {
                Iterator it = roleAL.iterator();
                while (it.hasNext()) {
                    String role = (String) it.next();
                    Integer permThisRole = (Integer) ps.userPermLevel.get(role);
                    if (perm == null) {
                        perm = permThisRole;
                    } else if (permThisRole != null && permThisRole.intValue() > perm.intValue()) {
                        perm = permThisRole;
                    }
                }
            }
        }
        if (perm == null) perm = (Integer) ps.userPermLevel.get("default");
        if (perm == null) return parentPerm;
        return perm.intValue();
    }

    public int maxStorageFor(String pageName) {
        if (pageName == null) return 100000000;
        PageSpec ps = (PageSpec) pageSpecs.get(pageName);
        if (ps != null && ps.maxStorage > 0) {
            return ps.maxStorage;
        }
        return maxStorageFor(parentOf(pageName));
    }

    public int maxStoragePerAttachFor(String pageName) {
        if (pageName == null) return 20;
        PageSpec ps = (PageSpec) pageSpecs.get(pageName);
        if (ps != null && ps.maxStoragePerAttach >= 0) {
            return ps.maxStoragePerAttach;
        }
        return maxStoragePerAttachFor(parentOf(pageName));
    }

    public String parentOf(String pageName) {
        if (pageName == null || pageName.length() == 0) {
            return null;
        }
        int sep = pageName.indexOf('/');
        if (sep < 0) {
            sep = pageName.lastIndexOf('.');
        }
        if (sep < 0) {
            return "";
        }
        return pageName.substring(0, sep);
    }

    public int userPermLevelForParentPage(String user, String pageName) {
        if (pageName == null || pageName.length() == 0) {
            return EXTEND;
        }
        return userPermLevelForPage(user, parentOf(pageName));
    }

    public String templateForPage(String page, HttpServletRequest req) {
        while (true) {
            PageSpec ps = (PageSpec) pageSpecs.get(page);
            if (ps != null && ps.templateName != null && ps.templateName.length() > 0) {
                return ps.templateName;
            }
            if (page == null || page.length() == 0) {
                return "Admin.Template";
            }
            page = parentOf(page);
        }
    }

    public synchronized void pageChanged(String page) {
        if (page.equals("Admin.Users")) {
            setUserList(readUserList());
        } else if (page.equals("Admin.Pages")) {
            setPageSpecs(readPageSpecs());
        }
    }

    protected synchronized HashMap getUserList() {
        return userList;
    }

    protected void setUserList(HashMap list) {
        userList = list;
    }

    protected synchronized TreeMap getPageSpecs() {
        return pageSpecs;
    }

    public String getAdminParent(String page) {
        if (page == null || page.length() == 0) {
            return null;
        }
        String parent = parentOf(page);
        if (pageSpecs.containsKey(parent)) {
            return parent;
        }
        return getAdminParent(parent);
    }

    public ArrayList getAdminChildren(String page) {
        if (page == null) {
            page = "";
        }
        Iterator it = pageSpecs.keySet().iterator();
        ArrayList retval = new ArrayList();
        while (it.hasNext()) {
            String candidate = (String) it.next();
            if (page.equals(getAdminParent(candidate))) {
                retval.add(candidate);
            }
        }
        return retval;
    }

    protected void setPageSpecs(TreeMap specs) {
        pageSpecs = specs;
        PageInfoNode.markAllForRecalc();
    }

    public static String debugDump() {
        String retval = "";
        PermissionManager inst = solo();
        TreeMap specs = inst.getPageSpecs();
        Iterator it = specs.keySet().iterator();
        retval += "Page Specs: (all storage sizes in 100k increments.)\n-----------\n";
        while (it.hasNext()) {
            String name = (String) it.next();
            String dispName = name.length() == 0 ? "(wikiBase)" : name;
            PageSpec ps = (PageSpec) specs.get(name);
            retval += dispName + " - storage= " + ps.maxStorage + (ps.reservedStorage ? "(reserved)" : "") + ", attachments<=" + ps.maxStoragePerAttach + ".";
            Iterator cousinIt = ps.userPermLevel.keySet().iterator();
            if (cousinIt.hasNext()) {
                retval += " (";
                String sep = "";
                while (cousinIt.hasNext()) {
                    String user = (String) cousinIt.next();
                    Integer perm = (Integer) ps.userPermLevel.get(user);
                    String permName = inst.permStringFromLevel(perm.intValue());
                    retval += sep + user + "=" + permName;
                    sep = ",";
                }
                retval += ")";
            }
            retval += "\n";
        }
        retval += "\n";
        return retval;
    }
}
