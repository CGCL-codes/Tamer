package pt.isel.meic.agendaagent.ontology.impl;

import pt.isel.meic.agendaagent.ontology.*;

/**
* Location of the event or nearby necessities such as parking. If a <gd:where> element is specified at the feed level, but there's no <gd:where> element at the entry level, then the entries inherit the feed-level <gd:where> value.
* Protege name: Where
* @author OntologyBeanGenerator v4.1
* @version 2011/09/1, 01:51:12
*/
public class DefaultWhere implements Where {

    private static final long serialVersionUID = 1653532723714014168L;

    private String _internalInstanceName = null;

    public DefaultWhere() {
        this._internalInstanceName = "";
    }

    public DefaultWhere(String instance_name) {
        this._internalInstanceName = instance_name;
    }

    public String toString() {
        return _internalInstanceName;
    }

    /**
   * Specifies the relationship between the containing entity and the contained location. Possible values (see below) are defined by other elements. For example, <gd:when> defines http://schemas.google.com/g/2005#event.
   * Protege name: prop:rel
   */
    private Relationship prop_rel;

    public void setProp_rel(Relationship value) {
        this.prop_rel = value;
    }

    public Relationship getProp_rel() {
        return this.prop_rel;
    }

    /**
   * Specifies a user-readable label to distinguish this location from other locations.
   * Protege name: prop:label
   */
    private String prop_label;

    public void setProp_label(String value) {
        this.prop_label = value;
    }

    public String getProp_label() {
        return this.prop_label;
    }

    /**
   * A simple string value
   * Protege name: prop:valueString
   */
    private String prop_valueString;

    public void setProp_valueString(String value) {
        this.prop_valueString = value;
    }

    public String getProp_valueString() {
        return this.prop_valueString;
    }
}
