package org.personalsmartspace.spm.negotiation.api.platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.personalsmartspace.spm.preference.api.platform.Action;
import org.personalsmartspace.spm.preference.api.platform.Condition;
import org.personalsmartspace.spm.preference.api.platform.Resource;

/**
 * @author Elizabeth
 *
 */
public class RequestItem implements Serializable {

    private Resource resource;

    private List<Action> actions;

    private List<Condition> conditions;

    private boolean optional;

    private RequestItem() {
        this.actions = new ArrayList<Action>();
        this.conditions = new ArrayList<Condition>();
        this.optional = false;
    }

    public RequestItem(Resource r, List<Action> actions, List<Condition> conditions) {
        this.resource = r;
        this.actions = actions;
        this.conditions = conditions;
        this.optional = false;
    }

    public RequestItem(Resource r, List<Action> actions, List<Condition> conditions, boolean isOptional) {
        this.resource = r;
        this.actions = actions;
        this.conditions = conditions;
        this.optional = isOptional;
    }

    public Resource getResource() {
        return this.resource;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public String toXMLString() {
        String str = "\n<Target>";
        str = str.concat(this.resource.toXMLString());
        for (Action action : actions) {
            str = str.concat(action.toXMLString());
        }
        for (Condition con : conditions) {
            str = str.concat(con.toXMLString());
        }
        str = str.concat(this.printOptional());
        str = str.concat("\n</Target>");
        return str;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public void setOptional(boolean isOptional) {
        this.optional = isOptional;
    }

    private String printOptional() {
        return "\n<optional>" + this.optional + "</optional>";
    }

    public String toString() {
        return this.toXMLString();
    }
}
