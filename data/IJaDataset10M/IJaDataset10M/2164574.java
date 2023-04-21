package net.sf.magicmap.db;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * @author msc
 * 
 * @jdo.persistence-capable 
 *   identity-type="application"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" 
 *   key="table-name"
 *   value="hardware"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" key="use-poid-generator"
 *   value="true"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" key="poid-class-generator"
 *   value="org.jpox.poid.SequenceTablePoidGenerator"
 */
public class Hardware {

    /**
     *
     */
    protected Hardware() {
        super();
    }

    /**
     * primary key
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   primary-key="true"
     */
    long id;

    /**
     * Name der Hardware
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    String name;

    /**
     * Type der Hardware
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    String type;

    /**
     * Client, die diese Hardware haben
     * 
     * @jdo.field 
     *   persistence-modifier="persistent" 
     *   collection-type="collection"
     *   element-type="Client" 
     *   mapped-by="hardware"
     */
    Collection clients = new HashSet();

    /**
     * AccessPoints, die diese Hardware haben
     * 
     * @jdo.field 
     *   persistence-modifier="persistent" 
     *   collection-type="collection"
     *   element-type="AccessPoint" 
     *   mapped-by="hardware"
     */
    Collection accessPoints = new HashSet();

    /**
     * 
     * @param name
     * @param type
     */
    public Hardware(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public Collection getClients() {
        return this.clients;
    }

    public void addClient(ScanResult client) {
        this.clients.add(client);
    }

    public void removeClient(Client client) {
        this.clients.remove(client);
    }

    public Collection getAccessPoints() {
        return this.clients;
    }

    public void addAccessPoint(AccessPoint ap) {
        this.clients.add(ap);
    }

    public void removeAccessPoint(AccessPoint ap) {
        this.clients.remove(ap);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return this.id;
    }
}
