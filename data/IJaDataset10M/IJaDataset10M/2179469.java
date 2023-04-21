package vse.core;

import com.conicsoft.bdkJ.core.Variant.enumVariantType;
import com.conicsoft.bdkJ.gui.propertyitem_data;
import com.conicsoft.bdkJ.gui.propertyitem_single;
import com.conicsoft.bdkJ.gui.propertyitem_single.enumPropertyItemType;

public class SpringControl extends WidgetControl {

    protected String str_layout = "";

    protected int d_layout_eid;

    public SpringControl(IControl parent) {
        super(parent);
        __register_properties();
    }

    protected void __register_properties() {
        get_properties().add(new propertyitem_single("layout", enumPropertyItemType.EPIT_NULL, new propertyitem_data(enumVariantType.EVT_STRING, this) {

            @Override
            public boolean change_value(String arg0) {
                str_layout = arg0;
                return true;
            }

            @Override
            public String get_value() {
                return str_layout;
            }
        }));
        get_properties().add(new propertyitem_single("layouteid", enumPropertyItemType.EPIT_NULL, new propertyitem_data(enumVariantType.EVT_INT, this) {

            @Override
            public boolean change_value(String arg0) {
                d_layout_eid = Integer.parseInt(arg0);
                return true;
            }

            @Override
            public String get_value() {
                return ((Integer) d_layout_eid).toString();
            }
        }));
    }
}
