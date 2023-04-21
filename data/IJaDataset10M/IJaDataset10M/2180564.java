package pl.xperios.rdk.client.dictionaries;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.filters.BooleanFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.DateFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.NumericFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import pl.xperios.rdk.client.commons.AbstractDictionary;
import pl.xperios.rdk.client.commons.DefaultDictionaryEditPanel;
import pl.xperios.rdk.client.commons.DictionaryEditPanel;
import pl.xperios.rdk.client.commons.XColumnConfig;
import pl.xperios.rdk.client.rpcservices.RoleRpcService;
import pl.xperios.rdk.client.rpcservices.RoleRpcServiceAsync;
import pl.xperios.rdk.client.commons.GenericRpcServiceAsync;
import pl.xperios.rdk.client.widgets.Fields.XBooleanField;
import pl.xperios.rdk.client.widgets.Fields.XDateField;
import pl.xperios.rdk.client.widgets.Fields.XIntegerField;
import pl.xperios.rdk.client.widgets.Fields.XLongField;
import pl.xperios.rdk.client.widgets.Fields.XTextField;
import pl.xperios.rdk.shared.validators.ValidIsNotNull;
import pl.xperios.rdk.shared.validators.ValidLengthMax;
import pl.xperios.rdk.shared.validators.ValidatorCondition;
import pl.xperios.rdk.shared.beans.Role;

public class RoleDictionary extends AbstractDictionary<Role> {

    @Override
    public GenericRpcServiceAsync<Role> initAsyncService() {
        return (RoleRpcServiceAsync) GWT.create(RoleRpcService.class);
    }

    @Override
    public String getFullName() {
        return "Role";
    }

    @Override
    public Role getNewBean() {
        return new Role();
    }

    @Override
    public ArrayList<ColumnConfig> getGridColumns() {
        ArrayList<ColumnConfig> out = new ArrayList<ColumnConfig>();
        out.add(new XColumnConfig(this, "role_id", "Role id", 75, true, null, false));
        out.add(new XColumnConfig(this, "full_name", "Full name", 200, true, null, true));
        out.add(new XColumnConfig(this, "code", "Code", 200, true, null, true));
        out.add(new XColumnConfig(this, "description", "Description", 200, true, null, true));
        out.add(new XColumnConfig(this, "entity_id", "Entity id", 75, true, null, true));
        out.add(new XColumnConfig(this, "parent_role_id", "Parent role id", 75, true, null, false));
        out.add(new XColumnConfig(this, "visible", "Visible", 75, true, null, false));
        out.add(new XColumnConfig(this, "active", "Active", 75, true, null, false));
        out.add(new XColumnConfig(this, "start_dt", "Start dt", 100, true, null, false));
        out.add(new XColumnConfig(this, "stop_dt", "Stop dt", 100, true, null, false));
        return out;
    }

    @Override
    public GridFilters getGridFilters() {
        GridFilters out = new GridFilters();
        out.addFilter(new NumericFilter("role_id"));
        out.addFilter(new StringFilter("code"));
        out.addFilter(new StringFilter("full_name"));
        out.addFilter(new StringFilter("description"));
        out.addFilter(new NumericFilter("entity_id"));
        out.addFilter(new NumericFilter("parent_role_id"));
        out.addFilter(new BooleanFilter("visible"));
        out.addFilter(new BooleanFilter("active"));
        out.addFilter(new DateFilter("start_dt"));
        out.addFilter(new DateFilter("stop_dt"));
        return out;
    }

    @Override
    public ArrayList<String> getExtraColumnsToDbIncluded() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("role_id");
        out.add("code");
        out.add("full_name");
        out.add("description");
        out.add("entity_id");
        out.add("parent_role_id");
        out.add("visible");
        out.add("active");
        out.add("start_dt");
        out.add("stop_dt");
        return out;
    }

    @Override
    public String getTemplate() {
        return "<b>Role:</b> " + "<b>Role id</b>: {role_id}," + "<b>Code</b>: {code}," + "<b>Full name</b>: {full_name}," + "<b>Description</b>: {description}," + "<b>Entity id</b>: {entity_id}," + "<b>Parent role id</b>: {parent_role_id}," + "<b>Visible</b>: {visible}," + "<b>Active</b>: {active}," + "<b>Start dt</b>: {start_dt}," + "<b>Stop dt</b>: {stop_dt}," + "";
    }

    @Override
    public String getAutoExpandColumn() {
        return "code";
    }

    @Override
    public DictionaryEditPanel<Role> getEditPanel() {
        DefaultDictionaryEditPanel<Role> panel = new DefaultDictionaryEditPanel<Role>();
        panel.addField(new XTextField("code", "Code", null, new ValidatorCondition[] { new ValidIsNotNull(), new ValidLengthMax(20) }));
        panel.addField(new XTextField("full_name", "Full name", null, new ValidatorCondition[] { new ValidLengthMax(255) }));
        panel.addField(new XTextField("description", "Description", null, new ValidatorCondition[] { new ValidLengthMax(1024) }));
        panel.addField(new XLongField("entity_id", "Entity id", null, new ValidatorCondition[] {}));
        panel.addField(new XLongField("parent_role_id", "Parent role id", null, new ValidatorCondition[] {}));
        panel.addField(new XBooleanField("visible", "Visible", null, new ValidatorCondition[] {}));
        panel.addField(new XBooleanField("active", "Active", null, new ValidatorCondition[] {}));
        panel.addField(new XDateField("start_dt", "Start dt", null, new ValidatorCondition[] {}));
        panel.addField(new XDateField("stop_dt", "Stop dt", null, new ValidatorCondition[] {}));
        return panel;
    }

    @Override
    public String getTreeColumnRender() {
        return "full_name";
    }
}
