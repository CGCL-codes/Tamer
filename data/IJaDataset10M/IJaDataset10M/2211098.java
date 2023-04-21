package vse.core;

import com.conicsoft.bdkJ.core.Variant.enumVariantType;
import com.conicsoft.bdkJ.gui.propertyitem_data;
import com.conicsoft.bdkJ.gui.propertyitem_group;
import com.conicsoft.bdkJ.gui.propertyitem_single;
import com.conicsoft.bdkJ.gui.propertyitem_single.enumPropertyItemType;
import com.conicsoft.bdkJ.parser.IConfigAttribute;
import com.conicsoft.bdkJ.parser.IConfigNode;

public class ComboBoxControl extends WidgetControl {

    protected boolean b_selection = false;

    protected boolean b_simple = false;

    protected boolean b_sort = false;

    protected boolean b_readonly = false;

    protected boolean b_dropdown = false;

    protected boolean b_process_enter = false;

    protected IControlProperty p_prop = null;

    public ComboBoxControl(IControl parent) {
        super(parent);
        __register_properties();
    }

    @Override
    public boolean serial_load(IConfigNode node) {
        return super.serial_load(node);
    }

    @Override
    public boolean serial_save(IConfigNode node) {
        PropAccess prop_child = new PropAccess(this.get_properties());
        p_prop = this.get_properties();
        IConfigNode p_node = null;
        IConfigAttribute p_attr = null;
        p_node = node.append_node("control");
        p_attr = p_node.append_attr("type");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.ESTRING);
        String t_name = "xgui_" + prop_child.get_name();
        p_attr.set_string(t_name);
        p_attr = p_node.append_attr("height");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.EINTEGER);
        p_attr.set_int(prop_child.get_height());
        p_attr = p_node.append_attr("width");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.EINTEGER);
        p_attr.set_int(prop_child.get_width());
        p_attr = p_node.append_attr("posx");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.EINTEGER);
        p_attr.set_int(prop_child.get_x());
        p_attr = p_node.append_attr("posy");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.EINTEGER);
        p_attr.set_int(prop_child.get_y());
        p_attr = p_node.append_attr("visible");
        p_attr.set_type(IConfigAttribute.EnumConfigAttrType.EBOOL);
        p_attr.set_bool(prop_child.get_visible());
        return true;
    }

    protected void __register_properties() {
        get_properties().add(new propertyitem_single("seletion", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public boolean change_value(String arg0) {
                b_selection = Boolean.parseBoolean(arg0);
                return true;
            }

            @Override
            public String get_value() {
                return ((Boolean) b_selection).toString();
            }
        }));
        get_properties().add(new propertyitem_group("style")).add(new propertyitem_single[] { new propertyitem_single("simple", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public String get_value() {
                return ((Boolean) b_simple).toString();
            }

            @Override
            public boolean change_value(String str) {
                b_simple = Boolean.parseBoolean(str);
                return true;
            }
        }), new propertyitem_single("sort", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public String get_value() {
                return ((Boolean) b_sort).toString();
            }

            @Override
            public boolean change_value(String str) {
                b_sort = Boolean.parseBoolean(str);
                return true;
            }
        }), new propertyitem_single("readonly", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public String get_value() {
                return ((Boolean) b_readonly).toString();
            }

            @Override
            public boolean change_value(String str) {
                b_readonly = Boolean.parseBoolean(str);
                return true;
            }
        }), new propertyitem_single("dropdown", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public String get_value() {
                return ((Boolean) b_dropdown).toString();
            }

            @Override
            public boolean change_value(String str) {
                b_dropdown = Boolean.parseBoolean(str);
                return true;
            }
        }), new propertyitem_single("process_enter", enumPropertyItemType.EPIT_BOOL, new propertyitem_data(enumVariantType.EVT_BOOL, this) {

            @Override
            public String get_value() {
                return ((Boolean) b_process_enter).toString();
            }

            @Override
            public boolean change_value(String str) {
                b_process_enter = Boolean.parseBoolean(str);
                return true;
            }
        }) });
    }
}
