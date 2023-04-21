package g4mfs.impl.org.peertrust.protege.plugin;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.ClsesPanel;
import edu.stanford.smi.protege.util.AllowableAction;

/**
 * <p>
 * This class is used to show the class with there policies.
 * The classes are shown in a tree; with the selected class as root
 * and it parents as "child". The class policies are shown in a table.
 * </p><p>
 * $Id: PolicyClsesPanel.java,v 1.1 2005/11/30 10:35:15 ionut_con Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:15 $
 * by $Author: ionut_con $
 * </p>
 * @author Patrice Congo 
 */
public class PolicyClsesPanel extends ClsesPanel {

    protected PolicyFrameworkModel policyFrameworkModel;

    protected AllowableAction createPolicyClassAction;

    /**
	 * @param project
	 */
    public PolicyClsesPanel(Project project) {
        super(project);
        policyFrameworkModel = new PolicyFrameworkModel(project.getKnowledgeBase());
    }

    /**
	 * Utility methode to create an action that make a PolicyTaggedCls
	 * using the policyFramework model.
	 * @return an Action that responds to action events by creating a PolicyTaggedCls
	 */
    public Action getCreatePolicyClassAction() {
        Icon icon;
        try {
            icon = Icons.getCreateClsIcon();
        } catch (Throwable th) {
            URL url = getClass().getResource("/res/class.create.gif");
            icon = new ImageIcon(url);
        }
        createPolicyClassAction = new AllowableAction("Add class with policy", icon, null) {

            public void actionPerformed(ActionEvent arg0) {
                Collection parents = _subclassPane.getSelection();
                if (!parents.isEmpty()) {
                    Cls cls = getKnowledgeBase().createCls(null, parents, policyFrameworkModel.getPolicyMetaCls());
                    policyFrameworkModel.setPolicyType(cls, "D");
                    _subclassPane.extendSelection(cls);
                }
            }
        };
        return createPolicyClassAction;
    }

    /**
	 * Override to create an action that build a PolicyTaggedCls.
	 * @return
	 */
    protected AllowableAction getCreateClsAction() {
        return (AllowableAction) getCreatePolicyClassAction();
    }
}
