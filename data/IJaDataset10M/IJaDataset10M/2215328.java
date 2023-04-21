package pl.xperios.rdk.client.dictionaries;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.filters.BooleanFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.DateFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.NumericFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import pl.xperios.rdk.client.common.AbstractCrudDictionary;
import pl.xperios.rdk.client.common.XColumnConfig;
import pl.xperios.rdk.client.rpc.PermissionRpcService;
import pl.xperios.rdk.client.rpc.PermissionRpcServiceAsync;
import pl.xperios.rdk.client.common.DefaultDictionaryEditPanel;
import pl.xperios.rdk.client.common.DictionaryEditPanel;
import pl.xperios.rdk.client.common.GenericCrudRpcService;
import pl.xperios.rdk.client.widgets.Fields.XBooleanField;
import pl.xperios.rdk.client.widgets.Fields.XDateField;
import pl.xperios.rdk.client.widgets.Fields.XIntegerField;
import pl.xperios.rdk.client.widgets.Fields.XLongField;
import pl.xperios.rdk.client.widgets.Fields.XTextField;
import pl.xperios.rdk.shared.validators.ValidIsNotNull;
import pl.xperios.rdk.shared.validators.ValidLengthMax;
import pl.xperios.rdk.shared.validators.ValidatorCondition;
import pl.xperios.rdk.client.common.GenericCrudRpcServiceAsync;
import pl.xperios.rdk.client.common.Treeable;
import pl.xperios.rdk.shared.domains.Permission;

public class PermissionDictionary extends AbstractCrudDictionary<Permission> {

    @Override
    public GenericCrudRpcServiceAsync<Permission> initAsyncServiceCrud() {
        return (PermissionRpcServiceAsync) GWT.create(PermissionRpcService.class);
    }

    @Override
    public String getFullName() {
        return "Permission";
    }

    @Override
    public Permission getNewBean() {
        return new Permission();
    }

    @Override
    public ArrayList<ColumnConfig> getGridColumns() {
        ArrayList<ColumnConfig> out = new ArrayList<ColumnConfig>();
        out.add(new XColumnConfig(this, "permission_id", "Permission id", 75, true, null, true));
        out.add(new XColumnConfig(this, "name", "Name", 200, true, null, true));
        out.add(new XColumnConfig(this, "description", "Description", 200, true, null, true));
        out.add(new XColumnConfig(this, "parent_permission_id", "Parent permission id", 75, true, null, true));
        out.add(new XColumnConfig(this, "visible", "Visible", 75, true, null, true));
        out.add(new XColumnConfig(this, "active", "Active", 75, true, null, false));
        out.add(new XColumnConfig(this, "start_dt", "Start dt", 100, true, null, false));
        out.add(new XColumnConfig(this, "stop_dt", "Stop dt", 100, true, null, false));
        return out;
    }

    @Override
    public GridFilters getGridFilters() {
        GridFilters out = new GridFilters();
        out.addFilter(new NumericFilter("permission_id"));
        out.addFilter(new StringFilter("name"));
        out.addFilter(new StringFilter("description"));
        out.addFilter(new NumericFilter("parent_permission_id"));
        out.addFilter(new BooleanFilter("visible"));
        out.addFilter(new BooleanFilter("active"));
        out.addFilter(new DateFilter("start_dt"));
        out.addFilter(new DateFilter("stop_dt"));
        return out;
    }

    @Override
    public ArrayList<String> getExtraColumnsToDbIncluded() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("permission_id");
        out.add("name");
        out.add("description");
        out.add("parent_permission_id");
        out.add("visible");
        out.add("active");
        out.add("start_dt");
        out.add("stop_dt");
        return out;
    }

    @Override
    public String getTemplate() {
        return "<b>Permission:</b> " + "<b>Permission id</b>: {permission_id}, " + "<b>Name</b>: {name}, " + "<b>Description</b>: {description}, " + "<b>Parent permission id</b>: {parent_permission_id}, " + "<b>Visible</b>: {visible}, " + "<b>Active</b>: {active}, " + "<b>Start dt</b>: {start_dt}, " + "<b>Stop dt</b>: {stop_dt}" + "";
    }

    @Override
    public String getAutoExpandColumn() {
        return "name";
    }

    @Override
    public DictionaryEditPanel<Permission> getEditPanel() {
        DefaultDictionaryEditPanel<Permission> panel = new DefaultDictionaryEditPanel<Permission>();
        panel.addField(new XTextField("name", "Name", null, new ValidatorCondition[] { new ValidLengthMax(255) }));
        panel.addField(new XTextField("description", "Description", null, new ValidatorCondition[] { new ValidLengthMax(1024) }));
        panel.addField(new XLongField("parent_permission_id", "Parent permission id", null, new ValidatorCondition[] {}));
        panel.addField(new XBooleanField("visible", "Visible", null, new ValidatorCondition[] {}));
        panel.addField(new XBooleanField("active", "Active", null, new ValidatorCondition[] {}));
        panel.addField(new XDateField("start_dt", "Start dt", null, new ValidatorCondition[] {}));
        panel.addField(new XDateField("stop_dt", "Stop dt", null, new ValidatorCondition[] {}));
        return panel;
    }

    @Override
    public String getTreeColumnRender() {
        return "name";
    }
}
