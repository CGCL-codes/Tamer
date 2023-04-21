package org.equanda.tapestry.components.userAdmin.table.rightsInput;

import org.equanda.tapestry.components.EquandaBaseComponent;
import org.equanda.tapestry.selectionModel.EditableRightsSelectionModel;
import org.equanda.validation.EditableRights;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.ValidatableField;
import java.util.Map;

/**
 * Description!!!
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
@ComponentClass(allowInformalParameters = true, allowBody = true)
public abstract class RightsInput extends EquandaBaseComponent implements ValidatableField {

    @Parameter(name = "key", required = true)
    public abstract String getKey();

    @Parameter(name = "fieldRights", required = true)
    public abstract Map<String, String> getFieldRights();

    @Parameter(name = "displayName", required = true)
    public abstract String getDisplayName();

    @Parameter(name = "showDisplayName", required = true)
    public abstract boolean getShowDisplayName();

    @Asset(value = "/org/equanda/tapestry/components/userAdmin/table/rightsInput/RightsInput.html")
    public abstract IAsset get$template();

    @Component(type = "PropertySelection", id = "rightsChoices", inheritInformalParameters = true, bindings = { "value       = rights", "displayName = displayName", "model       = getSelectionModel()", "id          = getItemId()" })
    public abstract PropertySelection getRightsChoices();

    public EditableRightsSelectionModel getSelectionModel() {
        return EditableRightsSelectionModel.getInstance(this);
    }

    public String getRights() {
        String right = getFieldRights().get(getKey());
        if (right == null) right = EditableRights.EDIT;
        return right;
    }

    public void setRights(String right_) {
        getFieldRights().put(getKey(), right_);
    }

    public String getItemId() {
        return "ID_ua" + getKey();
    }
}
