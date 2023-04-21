package com.nokia.ats4.appmodel.model.domain.flags.impl;

import com.nokia.ats4.appmodel.model.domain.Transition;
import java.util.ArrayList;
import java.util.List;

/**
 * Flag implementation.
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class KendoFlag {

    /** Flag name */
    private String name = "";

    /** The description of the flag */
    private String description = "";

    /** The default value of the flag */
    private boolean defaultValue;

    /** This list contains all the transitions that set the value for this flag **/
    private List<Transition> settingTransitions = new ArrayList<Transition>();

    /**
     * Creates a new instance of KendoFlag
     */
    public KendoFlag(String name, String description, boolean value) {
        this.name = name;
        this.description = description;
        this.defaultValue = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getDefaultValue() {
        return this.defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Transition> getSettingTransitions() {
        return this.settingTransitions;
    }

    public void addSettingTransition(Transition setter) {
        if (setter != null && !this.settingTransitions.contains(setter)) {
            this.settingTransitions.add(setter);
        }
    }

    public void removeSettingTransition(Transition toBeRemoved) {
        if (this.settingTransitions.contains(toBeRemoved)) {
            this.settingTransitions.remove(toBeRemoved);
        }
    }

    public void setDefaultValue(boolean isEnabled) {
        this.defaultValue = isEnabled;
    }
}
