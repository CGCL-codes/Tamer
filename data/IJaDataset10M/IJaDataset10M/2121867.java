package com.acarter.scenemonitor.propertydescriptor.renderer;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableCellRenderer;
import com.acarter.scenemonitor.propertydescriptor.propertyobject.Vector3fPropertyObject;
import com.jme.math.Vector3f;

/**
 * @author Carter
 * 
 */
public class Vector3fRenderer extends DefaultTableCellRenderer {

    /**	 */
    private static final long serialVersionUID = 1L;

    /** A decimal formatter */
    protected DecimalFormat decimal = new DecimalFormat("#####.####");

    /**
	 * 
	 */
    @Override
    public void setValue(Object value) {
        if (value instanceof Vector3fPropertyObject) {
            Vector3f vector = ((Vector3fPropertyObject) value).getValue();
            if (vector != null) setText("( " + decimal.format(vector.x) + ", " + decimal.format(vector.y) + ", " + decimal.format(vector.z) + " )"); else setText("null");
        } else setText("");
    }
}
