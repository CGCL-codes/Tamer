package com.prolix.editor.dialogs.services;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import com.prolix.editor.GlobalConstants;
import com.prolix.editor.LDT_Constrains;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.LearningDesign;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.SendMail;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.LearnerGroup;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.Role;
import uk.ac.reload.straker.datamodel.learningdesign.components.roles.StaffGroup;
import uk.ac.reload.straker.datamodel.learningdesign.types.EmailDataType;

public class SelectRolesWithEmailDataComposite {

    private static final int LEARNER_TYPE = 1 << 0;

    private static final int STAFF_TYPE = 1 << 1;

    private Tree tree;

    private LearningDesign learningDesign = null;

    int counter = 0;

    private SendMail sendMail;

    public SelectRolesWithEmailDataComposite(SendMail sendMail) {
        this.sendMail = sendMail;
        this.learningDesign = sendMail.getLearningDesign();
    }

    /**
	 * creates the view
	 */
    public void createView(Composite parent) {
        tree = new Tree(parent, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        TreeColumn tc1 = new TreeColumn(tree, SWT.NONE);
        tc1.setText("Role");
        tc1.setWidth(240);
        if (learningDesign != null) {
            LearnerGroup lg = learningDesign.getComponents().getRoles().getLearnerGroup();
            StaffGroup sg = learningDesign.getComponents().getRoles().getStaffGroup();
            showRoles(lg, LEARNER_TYPE);
            showRoles(sg, STAFF_TYPE);
        }
        initializeComponent();
        Label label = new Label(parent, SWT.WRAP);
        label.setFont(GlobalConstants.STANDARD_ITALIC_FONT);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label.setText("You may specify the email addresses and corresponding usernames at a later time, i.e. when preparing the exported unit of learning for run time in your learning management system.");
    }

    /**
	 * returns the selected roles from the table to asocciate them to the service
	 * called when a Send mail service is created or updated
	 */
    public Role[] getSelectedRoles() {
        ArrayList roles = new ArrayList();
        for (int i = 0; i < tree.getItemCount(); i++) {
            parseTree(tree.getItem(i), roles);
        }
        Role[] array = new Role[roles.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (Role) (roles.get(i));
        }
        return array;
    }

    /**
	 * parses the tree and its items
	 * @param item
	 */
    private void parseTree(TreeItem item, ArrayList roles) {
        if (item.getChecked()) {
            roles.add((Role) item.getData());
        }
        if (item.getItems().length > 0) {
            TreeItem[] items = item.getItems();
            for (int i = 0; i < items.length; i++) {
                parseTree(items[i], roles);
            }
        }
    }

    /**
	 * initializes the tree with roles
	 *
	 */
    private void initializeComponent() {
        EmailDataType[] types = sendMail.getEmailData();
        if (sendMail.getLearningDesign() == null) {
            System.out.println("SelectRolesWithEmailDataComp. --> sendMail.getLD == null; types: " + types.length);
        }
        if ((types != null) && (types.length > 0)) {
            Role[] roles = new Role[types.length];
            for (int i = 0; i < types.length; i++) {
                roles[i] = (Role) types[i].getRoleRef().getReferencedComponent();
            }
            for (int i = 0; i < tree.getItemCount(); i++) {
                setCheckedRoles(tree.getItem(i), roles);
            }
        }
    }

    /**
	 * checks if the roles referenced by this services are checked in the tree
	 * @param item
	 * @param roles
	 */
    private void setCheckedRoles(TreeItem item, Role[] roles) {
        if (item.getData() != null) {
            for (int i = 0; i < roles.length; i++) {
                if (((Role) item.getData()).getIdentifier().equals(roles[i].getIdentifier())) {
                    item.setChecked(true);
                }
            }
        }
        if (item.getItems().length > 0) {
            TreeItem[] items = item.getItems();
            for (int i = 0; i < items.length; i++) {
                setCheckedRoles(items[i], roles);
            }
        }
    }

    private void showRoles(DataComponent group, int type) {
        TreeItem root;
        if (type == LEARNER_TYPE) {
            root = new TreeItem(tree, SWT.NONE);
            root.setText("Learner");
            root.setGrayed(true);
            root.setImage(LDT_Constrains.ICON_LEARNER);
            root.setData(null);
        } else {
            root = new TreeItem(tree, SWT.NONE);
            root.setText("Staff");
            root.setImage(LDT_Constrains.ICON_STAFF);
            root.setGrayed(true);
            root.setData(null);
        }
        DataComponent[] roles = group.getChildren();
        for (int i = 0; i < roles.length; i++) {
            if (roles[i] instanceof Role) {
                Role role = (Role) roles[i];
                TreeItem item = new TreeItem(root, SWT.NONE);
                item.setData(role);
                item.setText(new String[] { role.getTitle(), role.getIdentifier(), "" });
                if (type == LEARNER_TYPE) item.setImage(LDT_Constrains.ICON_LEARNER); else item.setImage(LDT_Constrains.ICON_STAFF);
                if (role.hasChildren()) {
                    addRole(item, role, type);
                }
                item.setExpanded(true);
            }
        }
        root.setExpanded(true);
    }

    /**
	 * recursively adds new roles to the tree
	 * @param parent
	 * @param parent_role
	 * @param type
	 */
    private void addRole(TreeItem parent, Role parent_role, int type) {
        DataComponent[] children = parent_role.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Role) {
                Role role = (Role) children[i];
                TreeItem item = new TreeItem(parent, SWT.NONE);
                item.setText(new String[] { role.getTitle(), role.getIdentifier(), "" });
                if (type == LEARNER_TYPE) item.setImage(LDT_Constrains.ICON_LEARNER); else item.setImage(LDT_Constrains.ICON_STAFF);
                item.setData(role);
                if (role.hasChildren()) addRole(item, role, type);
                item.setExpanded(true);
            }
        }
    }
}
