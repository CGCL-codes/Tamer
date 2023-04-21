package edu.lcmi.grouppac.gui;

import javax.swing.JOptionPane;
import org.omg.CosNaming.NameComponent;
import edu.lcmi.grouppac.ParsedIOGR;

/**
 * Retrieve all members of an object group from the Replication Manager.  Creation date:
 * (08/04/2002 10:57:56)
 * 
 * @version $Revision: 1.6 $
 * @author <a href="mailto:padilha@das.ufsc.br">Ricardo Sangoi Padilha</a>, <a
 *         href="http://www.das.ufsc.br/">UFSC, Florian�polis, SC, Brazil</a>
 */
public class RefreshMembersAction extends AbstractGroupPacAction {

    private JFTAdmin2 admin;

    /**
	 * Creates a new RefreshMembersAction object.
	 * 
	 * @param admin
	 */
    public RefreshMembersAction(JFTAdmin2 admin) {
        super("Members...", null, 'm', null, "Manage the members of the selected group");
        this.admin = admin;
    }

    /**
	 * @see AbstractGroupPacAction#execute()
	 */
    public void execute() throws Exception {
        admin.getMemberList().clear();
        Object o;
        try {
            o = admin.getGroupList().get(admin.getJListGroupKnown().getSelectedIndex());
        } catch (Exception e) {
            return;
        }
        org.omg.CORBA.Object group_ref = admin.findGroup((NameComponent[]) o);
        if (group_ref != null) {
            NameComponent[][] ncs = null;
            ncs = admin.getRM().locations_of_members(group_ref);
            if (ncs != null) {
                String type_id = ParsedIOGR.toIOR(group_ref).type_id;
                for (int i = 0; i < ncs.length; i++) admin.getMemberList().addElement(ncs[i]);
                if (!admin.getTypeList().contains(type_id)) admin.getTypeList().addElement(type_id);
            }
        } else {
            JOptionPane.showMessageDialog(admin, "Member list not available.\nGroup does not exist anymore.", "Refresh member list", JOptionPane.ERROR_MESSAGE);
        }
        admin.getTabbedPane().setSelectedIndex(2);
    }
}
