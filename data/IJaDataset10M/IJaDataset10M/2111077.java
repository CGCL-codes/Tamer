package saadadb.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.meta.AttributeHandler;
import saadadb.meta.MetaClass;
import saadadb.sqltable.SQLQuery;
import saadadb.util.Messenger;

/**
 * @author michel
 * * @version $Id: SaadaClass.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class SaadaClass extends SaadaDMBrik {

    private final MetaClass metaclass;

    /**
     * @param saadadb
     * @param name
     * @throws CollectionException 
     * @throws SaadaException 
     */
    public SaadaClass(String name) throws FatalException {
        super(name);
        if (this.name.indexOf(".") >= 0) {
            this.name = this.name.substring(this.name.lastIndexOf(".") + 1);
        }
        metaclass = Database.getCachemeta().getClass(this.name);
    }

    /**
     * @return
     */
    public String[] getAttributeNames() {
        AttributeHandler[] hdls = metaclass.getClassAttributes();
        if (hdls != null) {
            String[] retour = new String[hdls.length];
            for (int i = 0; i < hdls.length; i++) {
                retour[i] = hdls[i].getNameattr();
            }
            return retour;
        }
        Messenger.printMsg(Messenger.ERROR, "Can't get attributes for class  " + this.name);
        return null;
    }

    /**
     * @param name
     * @return
     * @throws SaadaException 
     */
    public SaadaAttribute getAttribute(String name) throws SaadaException {
        return new SaadaAttribute(this, name);
    }

    /**
     * @return 
     */
    public SaadaCollection getCollection() {
        try {
            return new SaadaCollection(metaclass.getCollection_name());
        } catch (SaadaException e) {
            Messenger.printStackTrace(e);
            return null;
        }
    }

    /**
     * @return
     */
    public int getCategory() {
        return metaclass.getCategory();
    }

    /**
     * @return
     */
    public String[] getStartingRelationNames() {
        return Database.getCachemeta().getRelationNamesStartingFromColl(metaclass.getCollection_name(), this.getCategory());
    }

    /**
     * @param rel_name
     * @return
     */
    public SaadaRelation getStartingRelation(String rel_name) {
        return new SaadaRelation(rel_name);
    }

    /**
     * @return
     * @throws SQLException 
     * @throws QueryException 
     */
    public Long[] getInstanceOIDs() throws Exception {
        SQLQuery squery = new SQLQuery();
        ResultSet rs = squery.run("select oidsaada from " + this.name);
        TreeSet<Long> retour = new TreeSet<Long>();
        while (rs.next()) {
            retour.add(new Long(rs.getLong(1)));
        }
        squery.close();
        return (retour.toArray(new Long[retour.size()]));
    }

    /**
	 * @param category
	 * @return
	 */
    public int getNumberOfInstances() {
        ResultSet rs;
        try {
            SQLQuery squery = new SQLQuery();
            rs = squery.run("SELECT count(oidsaada) FROM " + this.name);
            while (rs.next()) {
                int retour = rs.getInt(1);
                squery.close();
                return retour;
            }
            squery.close();
        } catch (Exception e) {
            Messenger.printStackTrace(e);
        }
        return -1;
    }

    @Override
    public void explains() throws SaadaException {
        System.out.println("Class : " + this.name);
        String att_names[] = this.getAttributeNames();
        for (int i = 0; i < att_names.length; i++) {
            this.getAttribute(att_names[i]).explains();
        }
    }

    /**
	 * @param meta
	 * @return
	 * @throws SQLException 
	 * @throws QueryException 
	 */
    public String[] getProducts() throws Exception {
        SQLQuery squery = new SQLQuery();
        ResultSet rs = squery.run("Select newname From saada_file where class_id =" + this.metaclass.getId());
        rs.last();
        if (rs.getRow() == 0) {
            return null;
        }
        String[] name = new String[rs.getRow()];
        int i = 0;
        rs.beforeFirst();
        while (rs.next()) {
            name[i] = rs.getString(1).trim();
            i++;
        }
        squery.close();
        return name;
    }

    /**
     * @return
     * @throws SaadaException
     * @throws SQLException
     */
    public String[] getProductsAssocied() throws Exception {
        int class_id = Database.getCachemeta().getClass(this.name).getId();
        SQLQuery squery = new SQLQuery();
        ResultSet rs = squery.run("Select newname From saada_file where class_id =" + class_id);
        rs.last();
        if (rs.getRow() == 0) {
            return null;
        }
        String[] name = new String[rs.getRow()];
        int i = 0;
        rs.beforeFirst();
        while (rs.next()) {
            name[i] = rs.getString(1).trim();
            i++;
        }
        squery.close();
        return name;
    }
}
