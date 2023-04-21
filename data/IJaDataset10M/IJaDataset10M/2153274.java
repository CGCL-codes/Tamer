package com.loribel.tools.sa.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.abstraction.GB_TextOwnerSet;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.gui.GB_ErrorDialog;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.swing.GB_ToolButton;
import com.loribel.commons.swing.tools.GB_SwingTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.GB_StringActionTools;
import com.loribel.tools.AA;
import com.loribel.tools.sa.vm.GB_StringActionVMFactory;

/**
 * Tool button to display a dialog with a specific String Action.
 */
public class GB_TButtonSA extends GB_ToolButton implements ActionListener {

    private GB_TextOwnerSet textOwner;

    private GB_StringAction sa;

    public GB_TButtonSA(GB_StringAction a_sa, GB_TextOwnerSet a_textOwner) {
        super();
        textOwner = a_textOwner;
        setStringAction(a_sa);
        this.addActionListener(this);
    }

    private void setStringAction(GB_StringAction a_sa) {
        sa = a_sa;
        if (sa instanceof GB_LabelIconOwner) {
            GB_LabelIconOwner l_labelIconOwner = (GB_LabelIconOwner) sa;
            Icon l_icon = GB_LabelIconTools.getIcon(l_labelIconOwner);
            if (l_icon == null) {
                l_icon = GB_IconTools.get(AA.ICON.X16_PROCESS);
            }
            setIcon(l_icon);
            setToolTipText(GB_LabelIconTools.getLabel(l_labelIconOwner));
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        GB_StringActionVMFactory l_factory = GB_StringActionVMFactory.getInstance();
        GB_ViewManager l_vm = l_factory.newViewManager(sa, true);
        JFrame l_parent = GB_SwingTools.getRootFrame(this);
        int r = GB_VMTools.showOKCancelDialog(l_parent, l_vm, null, null, true);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            GB_StringActionTools.doAction(textOwner, sa, true);
        } catch (Exception ex) {
            GB_ErrorDialog.showErrorMsg(l_parent, ex);
        }
    }
}
