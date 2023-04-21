package org.nightlabs.jfire.scripting.editor2d.impl;

import org.nightlabs.editor2d.impl.DrawComponentImpl;
import org.nightlabs.jdo.ObjectIDUtil;
import org.nightlabs.jfire.scripting.editor2d.ScriptDrawComponent;
import org.nightlabs.jfire.scripting.id.ScriptRegistryItemID;

/**
 * @author Daniel.Mazurek [at] NightLabs [dot] de
 *
 */
public abstract class ScriptDrawComponentImpl extends DrawComponentImpl implements ScriptDrawComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private transient ScriptRegistryItemID scriptRegistryItemID = null;

    private String scriptRegistryItemIDKeyStr = null;

    public ScriptRegistryItemID getScriptRegistryItemID() {
        if (scriptRegistryItemID == null && scriptRegistryItemIDKeyStr != null) scriptRegistryItemID = (ScriptRegistryItemID) ObjectIDUtil.createObjectID(scriptRegistryItemIDKeyStr);
        return scriptRegistryItemID;
    }

    public void setScriptRegistryItemID(ScriptRegistryItemID scriptRegistryItemID) {
        ScriptRegistryItemID oldID = this.scriptRegistryItemID;
        this.scriptRegistryItemID = scriptRegistryItemID;
        this.scriptRegistryItemIDKeyStr = scriptRegistryItemID == null ? null : scriptRegistryItemID.toString();
        firePropertyChange(PROP_SCRIPT_REGISTRY_ITEM_ID, oldID, scriptRegistryItemID);
    }

    private transient Object scriptValue;

    public Object getScriptValue() {
        return scriptValue;
    }

    public void setScriptValue(Object scriptValue) {
        this.scriptValue = scriptValue;
    }
}
