package com.acarter.scenemonitor.propertydescriptor.renderer;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableCellRenderer;
import com.acarter.scenemonitor.propertydescriptor.propertyobject.QuaternionPropertyObject;
import com.jme.math.Quaternion;

/**
 * @author Carter
 * 
 */
public class QuaternionRenderer extends DefaultTableCellRenderer {

    /**	 */
    private static final long serialVersionUID = 1L;

    /** A decimal formatter */
    protected DecimalFormat decimal = new DecimalFormat("#####.####");

    /**
	 * 
	 */
    @Override
    public void setValue(Object value) {
        if (value instanceof QuaternionPropertyObject) {
            Quaternion quat = ((QuaternionPropertyObject) value).getValue();
            if (quat != null) setText("( " + decimal.format(quat.x) + ", " + decimal.format(quat.y) + ", " + decimal.format(quat.z) + ", " + decimal.format(quat.w) + " )"); else setText("null");
        } else setText("");
    }
}
