package g4mfs.impl.org.peertrust.protege.plugin;

import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import edu.stanford.smi.protege.model.FrameSlotCombination;
import edu.stanford.smi.protege.model.Slot;

/**
 * <p>
 * Renderer for slot policy table. use it only for the Policy Info column.
 * </p><p>
 * $Id: PolicyTableCellRenderer.java,v 1.1 2005/11/30 10:35:15 ionut_con Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:15 $
 * by $Author: ionut_con $
 * </p>
 * @author Patrice Congo 
 */
public class PolicyTableCellRenderer extends JPanel implements TableCellRenderer {

    Icon noPolicyIcon;

    PolicyFrameworkModel policyFrameworkModel;

    JLabel policyInfoLabel;

    public PolicyTableCellRenderer(PolicyFrameworkModel policyFrameworkModel) {
        this.policyFrameworkModel = policyFrameworkModel;
        policyInfoLabel = new JLabel();
        try {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setPolicyFrameworkModel(PolicyFrameworkModel policyFrameworkModel) {
        this.policyFrameworkModel = policyFrameworkModel;
    }

    /**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof FrameSlotCombination) {
            FrameSlotCombination fsCombi = (FrameSlotCombination) value;
            String slotTypeName = fsCombi.getSlot().getDirectType().getName();
            return getTemplateSlotPolicyBox(table, value, isSelected, hasFocus, row, column);
        }
        return this;
    }

    public Component getTemplateSlotPolicyBox(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof FrameSlotCombination) {
            FrameSlotCombination fsCombi = (FrameSlotCombination) value;
            Slot aSlot = fsCombi.getSlot();
            if (policyFrameworkModel.isPolicyTaggedSlot(aSlot)) {
                Collection c = policyFrameworkModel.getAllPolicies(aSlot);
                int polCount = c.size();
                int mPolCount = 0;
                int dPolCount = 0;
                String aType;
                for (Iterator it = c.iterator(); it.hasNext(); ) {
                    if (((PolicyFrameworkModel.PolicyData) it.next()).policyType.equalsIgnoreCase("M")) {
                        mPolCount++;
                    } else {
                        dPolCount++;
                    }
                }
                String text = "<html><body><b><font color='red' >M=" + mPolCount + "</font> <font color='green'>D=" + dPolCount + "</font></b></body></html>";
                policyInfoLabel.setText(text);
            } else {
                policyInfoLabel.setIcon(noPolicyIcon);
                policyInfoLabel.setText("");
            }
        }
        return policyInfoLabel;
    }
}
