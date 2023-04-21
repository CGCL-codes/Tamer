package net.cygeek.tech.client.ui.menu.admin;

import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.data.Node;
import com.gwtext.client.core.EventObject;
import net.cygeek.tech.client.ohrm;
import net.cygeek.tech.client.util.TS;
import net.cygeek.tech.client.adapters.*;

/**
 * @author Thilina Hasantha
 * @date: Sep 28, 2008
 * @time: 9:08:55 AM
 */
public class QualificationMenu extends TreePanel {

    public QualificationMenu() {
        TreeNode root = new TreeNode("Root");
        TreeNode tnEdu = new TreeNode(TS.gi().get("L0124"));
        TreeNode tnLis = new TreeNode(TS.gi().get("L0129"));
        root.appendChild(tnEdu);
        root.appendChild(tnLis);
        ohrm.MP_ADMIN_QUALIFICATION.addListener(new PanelListenerAdapter() {

            public void onExpand(Panel panel) {
                ohrm.hideAllCenterPanElements();
                ohrm.GR_Education.show();
                if (!ohrm.GR_Education.updatedOnce) {
                    EducationAdapter.getInstance().getEducations(ohrm.GR_Education);
                }
                ohrm.centerPanel.doLayout();
            }
        });
        tnEdu.addListener(new TreeNodeListenerAdapter() {

            public void onClick(Node node, EventObject eventObject) {
                ohrm.hideAllCenterPanElements();
                ohrm.GR_Education.show();
                if (!ohrm.GR_Education.updatedOnce) {
                    EducationAdapter.getInstance().getEducations(ohrm.GR_Education);
                }
                ohrm.centerPanel.doLayout();
            }
        });
        tnLis.addListener(new TreeNodeListenerAdapter() {

            public void onClick(Node node, EventObject eventObject) {
                ohrm.hideAllCenterPanElements();
                ohrm.GR_Licenses.show();
                if (!ohrm.GR_Licenses.updatedOnce) {
                    LicensesAdapter.getInstance().getLicensess(ohrm.GR_Licenses);
                }
                ohrm.centerPanel.doLayout();
            }
        });
        this.setRootNode(root);
        this.setRootVisible(false);
    }
}
