package pt.isel.meic.agendaagent.ontology.impl;

import pt.isel.meic.agendaagent.ontology.*;

/**
* Protege name: OriginalEvent
* @author OntologyBeanGenerator v4.1
* @version 2011/09/1, 01:51:12
*/
public class DefaultOriginalEvent implements OriginalEvent {

    private static final long serialVersionUID = 1653532723714014168L;

    private String _internalInstanceName = null;

    public DefaultOriginalEvent() {
        this._internalInstanceName = "";
    }

    public DefaultOriginalEvent(String instance_name) {
        this._internalInstanceName = instance_name;
    }

    public String toString() {
        return _internalInstanceName;
    }

    /**
   * Protege name: prop:id
   */
    private String prop_id;

    public void setProp_id(String value) {
        this.prop_id = value;
    }

    public String getProp_id() {
        return this.prop_id;
    }

    /**
   * Specifies the entry URI. If the nested entry is embedded and not linked, this attribute may be omitted.
   * Protege name: prop:href
   */
    private String prop_href;

    public void setProp_href(String value) {
        this.prop_href = value;
    }

    public String getProp_href() {
        return this.prop_href;
    }
}
