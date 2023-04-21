package pt.isel.meic.agendaagent.ontology.impl;

import pt.isel.meic.agendaagent.ontology.*;

/**
* Protege name: Visibility
* @author OntologyBeanGenerator v4.1
* @version 2011/09/1, 01:51:12
*/
public class DefaultVisibility implements Visibility {

    private static final long serialVersionUID = 1653532723714014168L;

    private String _internalInstanceName = null;

    public DefaultVisibility() {
        this._internalInstanceName = "";
    }

    public DefaultVisibility(String instance_name) {
        this._internalInstanceName = instance_name;
    }

    public String toString() {
        return _internalInstanceName;
    }
}
