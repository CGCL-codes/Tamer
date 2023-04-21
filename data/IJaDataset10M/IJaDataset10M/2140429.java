package org.sulweb.infureports;

import java.awt.Component;
import javax.swing.*;
import java.util.*;
import java.math.*;
import org.sulweb.infumon.common.Pump;
import org.sulweb.infumon.common.DBSchema;

/**
 * <p>Title: InfuGraph</p>
 * <p>Description: Frontend for Infumon database log display</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Elaborazione Dati Pinerolo srl</p>
 * @author Lucio Crusca
 * @version 1.0
 */
public class IdListRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Pump pval = (Pump) value;
        String pumpId = DBSchema.getHexStringFromVarchar(pval.getPumpID());
        String model = pval.getModel().getName();
        value = "ID: " + pumpId + " (" + model + ")";
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
