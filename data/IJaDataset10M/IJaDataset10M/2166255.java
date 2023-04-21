package com.incendiaryblue.cmslite;

import com.incendiaryblue.appframework.AppComponentBase;
import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.appframework.UserAppComponent;
import com.incendiaryblue.config.XMLConfigurable;
import com.incendiaryblue.config.XMLConfigurationException;
import com.incendiaryblue.config.XMLContext;
import com.incendiaryblue.database.ConnectionException;
import com.incendiaryblue.database.Database;
import com.incendiaryblue.storage.BusinessObjectList;
import com.incendiaryblue.util.ConstantMap;
import org.w3c.dom.Element;
import java.sql.*;
import java.util.*;
import java.io.Serializable;

/**
 * <p>Title: LinkManager</p>
 * <p>Description: An object designed for optimised inserts into the database for
 * management of category link and sub associations.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: syzygy</p>
 * @author Giles Taylor
 * @version 1.0
 */
public class LinkManager extends AppComponentBase implements UserAppComponent, XMLConfigurable, Serializable {

    private static boolean debug = true;

    private Database db;

    private ConstantMap sqlConstants = new ConstantMap();

    {
        sqlConstants.add("INSERT_SUB_REL", "INSERT INTO CATEGORY_SUB (CATEGORY_ID, SUB_CATEGORY_ID, DIRECT, ROOT_ID, LINK_ID) " + "VALUES (?,?,?,?,?)");
        sqlConstants.add("DELETE_CAT_LINK_RELS", "DELETE FROM CATEGORY_SUB WHERE SUB_CATEGORY_ID = ? AND ROOT_ID = ? AND LINK_ID = ?");
        sqlConstants.add("DELETE_CAT_PERMS", "DELETE FROM syzcategory_syzgroup_syzrole WHERE category_id = ?");
        sqlConstants.add("INSERT_CAT_PERM", "INSERT INTO syzcategory_syzgroup_syzrole(category_id, group_id, " + "language_id, role_id) VALUES(?, ?, ?, ?)");
    }

    /**
	 * This method updates the link records relevant to a certain category. The process runs as follows:<P>
	 * <UL>
	 * <LI>1. Get all category links of which category <code>cat</code> is a descendant, and for each link:
	 * <LI>2. Delete all records pointing to <code>cat</code> via the link
	 * <LI>3. Add a path in records from the logical root through the link to <code>cat</code>
	 *  </UL>
	 * This will require several inserts per link depending on the link's depth
	 *
	 * @param cat The Category whose links require updating
	 */
    public void updateLinksTo(Category cat) {
        Category parent = cat.getParent();
        if (parent == null) {
            return;
        }
        List links = ConcreteCategoryLink.getLinksByDescendant(cat.getParent());
        List path = cat.getNodePath();
        int rootId, linkId, catId, subId, parentId;
        catId = ((Integer) parent.getPrimaryKey()).intValue();
        subId = ((Integer) cat.getPrimaryKey()).intValue();
        Connection conn = db.getConnection();
        PreparedStatement ps = null, ps2 = null;
        try {
            try {
                ps = conn.prepareStatement(sqlConstants.get("INSERT_SUB_REL"));
                ps.setInt(2, subId);
                for (Iterator i = links.iterator(); i.hasNext(); ) {
                    ConcreteCategoryLink link = (ConcreteCategoryLink) i.next();
                    List linkPath = link.getParentCategory().getNodePath();
                    rootId = ((Integer) link.getParentCategory().getPrimaryKey()).intValue();
                    linkId = ((Integer) link.getCategory().getPrimaryKey()).intValue();
                    if (debug) {
                        System.out.println("LinkManager: doing link, " + rootId + ":" + linkId);
                    }
                    try {
                        ps2 = conn.prepareStatement(sqlConstants.get("DELETE_CAT_LINK_RELS"));
                        ps2.setInt(1, subId);
                        ps2.setInt(2, rootId);
                        ps2.setInt(3, linkId);
                        ps2.executeUpdate();
                    } finally {
                        if (ps2 != null) {
                            ps2.close();
                        }
                    }
                    ps.setInt(4, rootId);
                    ps.setInt(5, linkId);
                    ListIterator jj = path.listIterator(path.size());
                    int currentCatId = -1;
                    do {
                        Node next = (Node) jj.previous();
                        currentCatId = ((Integer) next.getCategoryStub().getPrimaryKey()).intValue();
                        ps.setInt(1, currentCatId);
                        ps.setInt(3, (currentCatId == catId) ? 1 : 0);
                        ps.executeUpdate();
                    } while (jj.hasPrevious() && currentCatId != linkId);
                    ps.setInt(3, 0);
                    for (jj = linkPath.listIterator(linkPath.size()); jj.hasPrevious(); ) {
                        Node next = (Node) jj.previous();
                        currentCatId = ((Integer) next.getCategoryStub().getPrimaryKey()).intValue();
                        ps.setInt(1, currentCatId);
                        ps.executeUpdate();
                    }
                }
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
        } finally {
            db.releaseConnection(conn);
        }
    }

    public void propogatePermissions(Category c) {
        Connection conn = db.getConnection();
        PreparedStatement ps = null, ps2 = null;
        List rels = CatGroupRoleRel.getCatGroupRoleRels(c);
        int[][] relArray = new int[rels.size()][3];
        int count = 0;
        for (Iterator i = rels.iterator(); i.hasNext(); count++) {
            CatGroupRoleRel rel = (CatGroupRoleRel) i.next();
            relArray[count][0] = ((Integer) rel.getGroup().getPrimaryKey()).intValue();
            relArray[count][1] = ((Integer) rel.getLanguage().getPrimaryKey()).intValue();
            relArray[count][2] = ((Integer) rel.getRole().getPrimaryKey()).intValue();
        }
        try {
            try {
                ps = conn.prepareStatement(sqlConstants.get("INSERT_CAT_PERM"));
                ps2 = conn.prepareStatement(sqlConstants.get("DELETE_CAT_PERMS"));
                BusinessObjectList l = (BusinessObjectList) c.getDescendants();
                for (Iterator i = l.keyIterator(); i.hasNext(); ) {
                    Integer next = (Integer) i.next();
                    if (next.equals(c.getPrimaryKey())) {
                        continue;
                    }
                    ps2.setObject(1, next);
                    ps2.executeUpdate();
                    ps.setObject(1, next);
                    for (int ii = 0; ii < relArray.length; ii++) {
                        ps.setInt(2, relArray[ii][0]);
                        ps.setInt(3, relArray[ii][1]);
                        ps.setInt(4, relArray[ii][2]);
                        ps.executeUpdate();
                    }
                }
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
        } finally {
            db.releaseConnection(conn);
        }
    }

    public Object configure(Element element, XMLContext context) throws XMLConfigurationException {
        String database = element.getAttribute("database");
        if (database != null) {
            this.db = (Database) AppConfig.getComponent(Database.class, database);
            if (db == null) {
                throw new XMLConfigurationException("LinkManager: The database with the name '" + database + "' was not found.");
            }
        }
        return this;
    }

    public void registerChild(Object object) {
        return;
    }
}
