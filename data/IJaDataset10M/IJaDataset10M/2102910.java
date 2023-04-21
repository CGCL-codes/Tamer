package org.equanda.tapestry.components.masterDetail.tableDescriptionEntity;

import org.equanda.tapestry.components.EquandaBaseComponent;
import org.equanda.tapestry.components.equandaField.EquandaField2;
import org.equanda.tapestry.components.equandaTable.EquandaTable;
import org.equanda.tapestry.model.GMField;
import org.equanda.tapestry.util.DisplayMode;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.ForBean;

/**
 * Description!!!
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
@ComponentClass(allowInformalParameters = true, allowBody = true)
public abstract class TableDescriptionEntityComponent extends EquandaBaseComponent {

    /**
     * ********************************* Parameter definitions: ***********************************
     */
    @Parameter(name = "parameter", required = true)
    public abstract org.equanda.tapestry.pages.masterDetail.parameter.TableParameter getParameter();

    @Parameter(name = "displayMode", required = true)
    public abstract DisplayMode getDisplayMode();

    @Parameter(name = "master", required = true)
    public abstract boolean isMaster();

    /**
     * ********************************** Assets definitions: *************************************
     */
    @Asset(value = "/org/equanda/tapestry/components/masterDetail/tableDescriptionEntity/TableDescriptionEntityComponent.html")
    public abstract IAsset get$template();

    /**
     * ********************************* Component definitions: **********************************
     */
    @Component(type = "EquandaTable", id = "master", bindings = { "proxy            = getParameter().getProxy()", "displayMode      = getDisplayMode()", "tableDescription = getParameter().getTableDescription()", "checkAccess      = isCheckAccess()", "checkPrefs       = isCheckPrefs()", "displayequandaType  = isDisplayequandaType()", "pagination       = true" })
    public abstract EquandaTable getEquandaTable();

    /**
     * For rendering the fields.
     */
    @Component(type = "EquandaField2", id = "equandaField", inheritInformalParameters = true, bindings = { "proxy               = getParameter().getProxy()", "displayMode         = getDisplayMode()", "checkAccess         = isCheckAccess()", "checkPrefs          = isCheckPrefs()", "fieldDescription    = fieldDescription" })
    public abstract EquandaField2 getEquandaField();

    /**
     * For each field.
     */
    @Component(type = "For", id = "foreachField", bindings = { "source       = getParameter().getTableDescription().getPage('default').getFields()", "value        = fieldDescription", "index        = index", "volatile     = true" })
    public abstract ForBean getForeachField();

    public abstract GMField getFieldDescription();

    public abstract int getIndex();

    public boolean isCheckAccess() {
        return true;
    }

    public boolean isCheckPrefs() {
        return true;
    }

    public boolean isDisplayequandaType() {
        return false;
    }
}
