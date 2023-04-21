package com.certesystems.swingforms.fields.renderers;

import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;
import com.certesystems.swingforms.fields.FieldTime;

public class TimeRenderer extends DefaultRenderer {

    private FieldTime fieldTime;

    public TimeRenderer(FieldTime ft) {
        this.fieldTime = ft;
    }

    public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        if (arg1 instanceof Date) {
            arg1 = fieldTime.getDateFormatter().format((Date) arg1);
        }
        return super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
    }
}
