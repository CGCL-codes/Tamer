package br.gov.frameworkdemoiselle.monitoring.internal.implementation.snmp.security;

import org.snmp4j.smi.OctetString;

/**
 * Holds information for a single <b>user entry</b>.
 * 
 * @author SERPRO
 */
public class UserEntry {

    private OctetString name;

    private SecModel model;

    public UserEntry(String name, String model) {
        this.name = new OctetString(name);
        this.model = SecModel.parseString(model);
    }

    public SecModel getModel() {
        return model;
    }

    public void setModel(SecModel model) {
        this.model = model;
    }

    public OctetString getName() {
        return name;
    }

    public void setName(OctetString name) {
        this.name = name;
    }

    public String toString() {
        return "UserEntry [model=" + model + ", name=" + name + "]";
    }
}
