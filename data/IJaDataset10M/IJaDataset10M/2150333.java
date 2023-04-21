package org.osmius.model;

import java.util.HashSet;
import java.util.Set;

/**
 * User data for way type
 */
public class OsmNTypnotifiwayUserdata implements java.io.Serializable {

    private OsmNTypnotifiwayUserdataId id;

    private OsmNTypnotifiway osmNTypnotifiway;

    private String desData;

    private String txtDatahelp;

    private int numOrder;

    private int indMandatory;

    private Set osmNUserTypnotifiwaies = new HashSet(0);

    /**
    * Default constructor
    */
    public OsmNTypnotifiwayUserdata() {
    }

    /**
    * Constructor with parameters
    * @param id The PK
    * @param osmNTypnotifiway Way type to send notifications
    * @param desData Description
    * @param txtDatahelp Default text         
    * @param numOrder Numeric order in render view
    */
    public OsmNTypnotifiwayUserdata(OsmNTypnotifiwayUserdataId id, OsmNTypnotifiway osmNTypnotifiway, String desData, String txtDatahelp, int numOrder, int indMandatory) {
        this.id = id;
        this.osmNTypnotifiway = osmNTypnotifiway;
        this.desData = desData;
        this.txtDatahelp = txtDatahelp;
        this.numOrder = numOrder;
        this.indMandatory = indMandatory;
    }

    /**
    * Constructor with parameters
    * @param id The PK
    * @param osmNTypnotifiway Way type to send notifications
    * @param desData Description
    * @param txtDatahelp Default text
    * @param numOrder Numeric order in render view
    * @param osmNUserTypnotifiwaies
    */
    public OsmNTypnotifiwayUserdata(OsmNTypnotifiwayUserdataId id, OsmNTypnotifiway osmNTypnotifiway, String desData, String txtDatahelp, int numOrder, int indMandatory, Set osmNUserTypnotifiwaies) {
        this.id = id;
        this.osmNTypnotifiway = osmNTypnotifiway;
        this.desData = desData;
        this.txtDatahelp = txtDatahelp;
        this.numOrder = numOrder;
        this.indMandatory = indMandatory;
        this.osmNUserTypnotifiwaies = osmNUserTypnotifiwaies;
    }

    public OsmNTypnotifiwayUserdataId getId() {
        return this.id;
    }

    public void setId(OsmNTypnotifiwayUserdataId id) {
        this.id = id;
    }

    public OsmNTypnotifiway getOsmNTypnotifiway() {
        return this.osmNTypnotifiway;
    }

    public void setOsmNTypnotifiway(OsmNTypnotifiway osmNTypnotifiway) {
        this.osmNTypnotifiway = osmNTypnotifiway;
    }

    public String getDesData() {
        return this.desData;
    }

    public void setDesData(String desData) {
        this.desData = desData;
    }

    public String getTxtDatahelp() {
        return this.txtDatahelp;
    }

    public void setTxtDatahelp(String txtDatahelp) {
        this.txtDatahelp = txtDatahelp;
    }

    public int getNumOrder() {
        return this.numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
    }

    public Set getOsmNUserTypnotifiwaies() {
        return this.osmNUserTypnotifiwaies;
    }

    public void setOsmNUserTypnotifiwaies(Set osmNUserTypnotifiwaies) {
        this.osmNUserTypnotifiwaies = osmNUserTypnotifiwaies;
    }

    public int getIndMandatory() {
        return indMandatory;
    }

    public void setIndMandatory(int indMandatory) {
        this.indMandatory = indMandatory;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmNTypnotifiwayUserdata that = (OsmNTypnotifiwayUserdata) o;
        if (indMandatory != that.indMandatory) return false;
        if (numOrder != that.numOrder) return false;
        if (desData != null ? !desData.equals(that.desData) : that.desData != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (osmNTypnotifiway != null ? !osmNTypnotifiway.equals(that.osmNTypnotifiway) : that.osmNTypnotifiway != null) return false;
        if (txtDatahelp != null ? !txtDatahelp.equals(that.txtDatahelp) : that.txtDatahelp != null) return false;
        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (osmNTypnotifiway != null ? osmNTypnotifiway.hashCode() : 0);
        result = 31 * result + (desData != null ? desData.hashCode() : 0);
        result = 31 * result + (txtDatahelp != null ? txtDatahelp.hashCode() : 0);
        result = 31 * result + numOrder;
        result = 31 * result + indMandatory;
        result = 31 * result + (osmNUserTypnotifiwaies != null ? osmNUserTypnotifiwaies.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "OsmNTypnotifiwayUserdata{" + "id=" + id + ", osmNTypnotifiway=" + osmNTypnotifiway + ", desData='" + desData + '\'' + ", txtDatahelp='" + txtDatahelp + '\'' + ", numOrder=" + numOrder + ", indMandatory=" + indMandatory + '}';
    }
}
