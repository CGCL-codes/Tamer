package DE.FhG.IGD.semoa.envision.plugins;

import java.util.*;
import javax.swing.*;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.InternalFrameListener;
import DE.FhG.IGD.semoa.envision.Envision;

/**
* Describes the panel for requirements. This class allows the removal and 
* edition of existing requirements and definition of new requirements.
* It is constructed from two inner components: one for the names of the roles, 
* one for the names of conditions for each role.
* <p>
* The visualization of the names of the roles is arranged in an inner class:
* <code>ReqList</code> as <code>JList</code>. The names of the roles cannot be
* edited.
* <p>
* The visualization of the names of the conditions is implemented in an 
* additional inner class: <code>ReqList</code> as <code>JList</code>.
* <p>
* For the edition of the existing requrements and the definition of new ones
* there are windows which pop up and guide the user how to do it. In order to
* be possible to cancel made changes an additional inner class is added:
* <code>DisplayList</code>. It is responsible only for displaying the list
* with conditions' names and a double clickover it initiates editing of the
* requirement with for the selected role.
*
* @author  Zaharina Velikova
* @version "$ Id: PolicyPlugIn.java $"
*/
public class PolicyRequirementPanel extends JPanel {

    /**
     * The <code>JScrollPane</code> for the names of the roles.
     */
    private JScrollPane namesScroll_;

    /**
     * The <code>JScrollPane</code> for the names of the conditions
     * in the edition window.
     */
    private JScrollPane listsScroll_;

    /**
     * The object which contains the parent panel and serves as a notificator
     * for special events to the other panels.
     */
    private PolicyPlugin parent_;

    /**
     * The <code>JScrollPane</code> for the <code>DisplayList</code>
     * objects with the names of the conditions for each role.
     */
    private HashMap reqDisplay_;

    /**
     * The <code>HashMap</code> object which maps a name of a condition
     * with the <code>ArrayList</code> of all names of roles with which
     * it constructs a requirement. This map is required from the fact
     * that during using this plugin the user can rename roles or
     * conditions in the other two panels. So the same changes must be
     * applied to the names of the roles and requirements which take part
     * in the requirements.
     */
    private HashMap condRoles_;

    /**
     * The map which maps the names of the roles with the lists of
     * conditions' names.
     */
    private Map requirements_;

    /**
     * The <code>ReqList</code> object for the names of the roles.
     */
    private ReqList reqList_;

    /**
     * Class Constructor which specifies the initial map of requirements.
     *
     * @param reqs the initial map of requirements
     */
    public PolicyRequirementPanel(Map reqs) {
        super();
        GridBagConstraints c;
        GridBagLayout layout;
        JLabel namesText;
        JLabel condsText;
        namesText = new JLabel("Names of Roles:");
        namesText.setMaximumSize(new Dimension(20, 20));
        condsText = new JLabel("Conditions:");
        condsText.setMaximumSize(new Dimension(20, 20));
        namesScroll_ = new JScrollPane();
        namesScroll_.setMinimumSize(new Dimension(200, 200));
        namesScroll_.setMaximumSize(new Dimension(400, 500));
        namesScroll_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        namesScroll_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listsScroll_ = new JScrollPane();
        listsScroll_.setMinimumSize(new Dimension(200, 150));
        listsScroll_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listsScroll_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setPreferredSize(new Dimension(700, 500));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 400));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        layout = new GridBagLayout();
        c = new GridBagConstraints();
        setLayout(layout);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridy = 0;
        layout.setConstraints(namesText, c);
        add(namesText);
        c.gridy = 2;
        layout.setConstraints(condsText, c);
        add(condsText);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridy = 1;
        layout.setConstraints(namesScroll_, c);
        add(namesScroll_);
        c.gridy = 3;
        layout.setConstraints(listsScroll_, c);
        add(listsScroll_);
        loadData(reqs);
    }

    /**
     * Describes the window for definition of new requirement. This window pops
     * up when the user clicks the "New" Button of the main panel. If the user
     * chooses to make a new requirement for a role, for which a requirement
     * already exist a message pops up and explains that this is not possible.
     */
    public void newRequirement() {
        final JInternalFrame frame;
        final JComboBox names;
        final NamesList roles;
        final NamesList conds;
        JButton cancelButton;
        GridBagLayout layout;
        GridBagConstraints c;
        JScrollPane scroll;
        JPanel buttonView;
        JLabel nameLabel;
        JLabel condLabel;
        JButton okButton;
        JPanel compose;
        if (parent_ == null) {
            throw new NullPointerException("The parent panel must be specified!");
        }
        nameLabel = new JLabel("Roles: ");
        condLabel = new JLabel("Conditions: ");
        roles = new NamesList(parent_.getRolesMap());
        conds = new NamesList(parent_.getConditionsMap());
        names = new JComboBox(roles.getData());
        names.setSelectedIndex(0);
        scroll = new JScrollPane(conds);
        conds.setToolTipText("Select conditions to add");
        scroll.setPreferredSize(new Dimension(400, 300));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout = new GridBagLayout();
        c = new GridBagConstraints();
        compose = new JPanel();
        compose.setLayout(layout);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridy = 0;
        layout.setConstraints(nameLabel, c);
        compose.add(nameLabel);
        c.gridy = 1;
        layout.setConstraints(condLabel, c);
        compose.add(condLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        layout.setConstraints(names, c);
        compose.add(names);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        layout.setConstraints(scroll, c);
        compose.add(scroll);
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 25));
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(75, 25));
        buttonView = new JPanel();
        buttonView.setLayout(new BoxLayout(buttonView, BoxLayout.X_AXIS));
        buttonView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonView.add(Box.createHorizontalGlue());
        buttonView.add(okButton);
        buttonView.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonView.add(cancelButton);
        try {
            frame = Envision.showInternalFrame(new JInternalFrame("New Requirement", true, true, true, true));
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Object[] selection;
                    String name;
                    name = names.getSelectedItem().toString();
                    selection = conds.getSelectedValues();
                    try {
                        if (requirements_.containsKey(name)) {
                            Envision.showMessage("A requirement for this role already exists!");
                        }
                        saveNewRequirement(name, selection);
                    } catch (Exception ee) {
                        Envision.showException(ee);
                        return;
                    }
                    reqList_.setSelectedIndex(reqList_.getModel().getSize() - 1);
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }
            });
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }
            });
            frame.addInternalFrameListener(new InternalFrameListener() {

                public void internalFrameActivated(InternalFrameEvent e) {
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                }

                public void internalFrameOpened(InternalFrameEvent e) {
                }
            });
            frame.getContentPane().add(buttonView, BorderLayout.PAGE_END);
            frame.getContentPane().add(compose, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            Envision.showException(e);
        }
    }

    /**
     * Describes the window for editing a requirement. It initiates 
     * editing of the selected requirement and gives the user the 
     * possibility to add new existing conditions, to remove added
     * conditions.If the selection is empty a message pops up to ask 
     * the user to select a name of a role.
     */
    public void editSelectedRequirement() {
        final NamesList condNames_out;
        final NamesList condNames_in;
        final JInternalFrame frame;
        final DisplayList backUp;
        final JComboBox names;
        final String name;
        final Map conds;
        JButton cancelButton;
        GridBagLayout layout;
        GridBagConstraints c;
        JScrollPane scroll;
        JPanel buttonView1;
        JPanel buttonView2;
        JButton addButton;
        JButton remButton;
        JLabel nameLabel;
        JLabel condLabel;
        JButton okButton;
        JPanel compose;
        Vector v;
        if (parent_ == null) {
            throw new NullPointerException("The parent panel must be specified!");
        }
        name = reqList_.getSelectedItem();
        if (name == null) {
            Envision.showMessage("Select a role to edit the reqirement!");
            return;
        }
        v = new Vector();
        v.add(name);
        conds = parent_.getConditionsMap();
        backUp = (DisplayList) reqDisplay_.get(name);
        condNames_in = new NamesList(backUp.getData());
        condNames_out = new NamesList(conds);
        names = new JComboBox(v);
        names.setSelectedIndex(0);
        names.setEnabled(false);
        nameLabel = new JLabel("Roles: ");
        condLabel = new JLabel("Conditions: ");
        scroll = new JScrollPane(condNames_in);
        scroll.setPreferredSize(new Dimension(400, 300));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout = new GridBagLayout();
        c = new GridBagConstraints();
        compose = new JPanel();
        compose.setLayout(layout);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridy = 0;
        layout.setConstraints(nameLabel, c);
        compose.add(nameLabel);
        c.gridy = 1;
        layout.setConstraints(condLabel, c);
        compose.add(condLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        layout.setConstraints(names, c);
        compose.add(names);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        layout.setConstraints(scroll, c);
        compose.add(scroll);
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 25));
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(75, 25));
        buttonView1 = new JPanel();
        buttonView1.setLayout(new BoxLayout(buttonView1, BoxLayout.X_AXIS));
        buttonView1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonView1.add(Box.createHorizontalGlue());
        buttonView1.add(okButton);
        buttonView1.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonView1.add(cancelButton);
        addButton = new JButton("Add Condition");
        addButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        remButton = new JButton("Remove Condition");
        remButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        remButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonView2 = new JPanel();
        buttonView2.setLayout(new BoxLayout(buttonView2, BoxLayout.Y_AXIS));
        buttonView2.setBorder(BorderFactory.createEmptyBorder(70, 10, 10, 10));
        buttonView2.add(Box.createHorizontalGlue());
        buttonView2.add(addButton);
        buttonView2.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonView2.add(remButton);
        try {
            frame = Envision.showInternalFrame(new JInternalFrame("Edit Requirement", true, true, true, true));
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ArrayList alist;
                    alist = condNames_in.getListData();
                    if (alist == null || alist.isEmpty()) {
                        Envision.showMessage("You cannot leave a role without conditions!");
                        return;
                    }
                    saveEditedRequirement(name, condNames_in, backUp);
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }
            });
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }
            });
            addButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    addConditions(name, condNames_in, condNames_out);
                }
            });
            remButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    Object[] obj;
                    int i;
                    obj = condNames_in.getSelectedValues();
                    if (obj == null || obj.length == 0) {
                        Envision.showMessage("Your selection is empty!");
                        return;
                    }
                    for (i = 0; i < obj.length; i++) {
                        condNames_in.remove(obj[i].toString());
                    }
                }
            });
            frame.addInternalFrameListener(new InternalFrameListener() {

                public void internalFrameActivated(InternalFrameEvent e) {
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                    changedListIndex();
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                }

                public void internalFrameOpened(InternalFrameEvent e) {
                }
            });
            frame.getContentPane().add(buttonView1, BorderLayout.PAGE_END);
            frame.getContentPane().add(buttonView2, BorderLayout.EAST);
            frame.getContentPane().add(compose, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            Envision.showException(e);
        }
    }

    /**
     * Removes the selected requirement. If the selection is empty it 
     * does nothing.
     */
    public void removeSelectedRequirement() {
        ArrayList al_conds;
        ArrayList al_roles;
        Iterator it;
        String role;
        String cond;
        int size;
        int row;
        size = reqList_.getModel().getSize();
        row = reqList_.getSelectedIndex();
        role = reqList_.getSelectedItem();
        al_conds = (ArrayList) requirements_.get(role);
        it = al_conds.iterator();
        while (it.hasNext()) {
            cond = it.next().toString();
            al_roles = (ArrayList) condRoles_.get(cond);
            al_roles.remove(role);
            if (al_roles.isEmpty()) {
                condRoles_.remove(cond);
            }
        }
        reqList_.removeItem(role);
        reqDisplay_.remove(role);
        requirements_.remove(role);
        if (size > 1 && row == size - 1) {
            reqList_.setSelectedIndex(row - 1);
        } else if (size > 1 && row >= 0) {
            reqList_.setSelectedIndex(row);
        }
    }

    /**
     * Notificates for a removal of a condition in order to remove it if
     * there is a requirement  .
     *
     * @param  name the name of the condition which must be removed
     * @return 0    if the user accepts the removal of a requirement which
     *              contains only  the condition which must be removed if
     *              there is such requirement or if there is no such
     *              requirement and the wanted changes have been made.
     *         -1   if the user do not accept such a change and refuses
     *              to delete the condition
     */
    public int conditionRemoved(String name) {
        ArrayList al_roles;
        ArrayList al_conds;
        String roleName;
        DisplayList dl;
        Iterator it;
        String s;
        int size;
        int i;
        int k;
        Object[] options = { "Yes", "No" };
        if (condRoles_.containsKey(name)) {
            al_roles = (ArrayList) condRoles_.get(name);
            if (al_roles.size() == 1) {
                s = al_roles.get(0).toString();
                if (((ArrayList) requirements_.get(s)).size() == 1) {
                    i = JOptionPane.showOptionDialog(parent_, "If you remove this condition, a requirement\n" + "which contains only this condition will\n" + "be deleted! Do you want to proceed removing?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (i == JOptionPane.NO_OPTION || i == JOptionPane.CLOSED_OPTION) {
                        return -1;
                    } else {
                        k = reqList_.getIndexOf(s);
                        size = reqList_.getModel().getSize();
                        condRoles_.remove(name);
                        reqList_.removeItem(s);
                        reqDisplay_.remove(s);
                        requirements_.remove(s);
                        if (size > 1 && k == size - 1) {
                            reqList_.setSelectedIndex(k - 1);
                        } else if (size > 1 && k >= 0) {
                            reqList_.setSelectedIndex(k);
                        }
                        return 0;
                    }
                }
            }
            it = al_roles.iterator();
            while (it.hasNext()) {
                roleName = it.next().toString();
                dl = (DisplayList) reqDisplay_.get(roleName);
                dl.remove(name);
                al_conds = (ArrayList) requirements_.get(roleName);
                al_conds.remove(name);
            }
        }
        return 0;
    }

    /**
     * Notificates if a condition is renamed so that the same change to be done
     * in the name of the condition in the requirements if the specified 
     * condition is included in a requirement.
     *
     * @param oldName the old name of the condition
     * @param newName the new name of the condition
     */
    public void conditionRenamed(String oldName, String newName) {
        ArrayList al_roles;
        ArrayList al_conds;
        Iterator it;
        DisplayList dl;
        String roleName;
        if (condRoles_.containsKey(oldName)) {
            al_roles = (ArrayList) condRoles_.get(oldName);
            it = al_roles.iterator();
            while (it.hasNext()) {
                roleName = it.next().toString();
                dl = (DisplayList) reqDisplay_.get(roleName);
                dl.remove(oldName);
                dl.add(newName);
                al_conds = (ArrayList) requirements_.get(roleName);
                al_conds.remove(oldName);
                al_conds.add(newName);
            }
            condRoles_.remove(oldName);
            condRoles_.put(newName, al_roles);
        }
    }

    /**
     * Notificates for a removal of a role so that if a requirement for this
     * role is defined then it also must be deleted.
     *
     * @param  name the name of the role which must be removed
     * @return 0    if the user accepts the removal of a requirement which
     *              is defined for this role if there is such a requirement
     *              or if there is no such requirement and the wanted changes
     *              have been made.
     *         -1   if the user do not accept such a change and refuses
     *              to delete the role
     */
    public int roleRemoved(String name) {
        int ind;
        int i;
        Object[] options = { "Yes", "No" };
        ind = reqList_.getIndexOf(name);
        if (ind != -1) {
            i = JOptionPane.showOptionDialog(parent_, "There is a defined requirement for this role!\n" + "If you delete the role then also the requirement\n" + "for this role will be deleted! Do you want to proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (i == JOptionPane.NO_OPTION || i == JOptionPane.CLOSED_OPTION) {
                return -1;
            } else {
                reqList_.setSelectedIndex(ind);
                removeSelectedRequirement();
            }
        }
        return 0;
    }

    /**
     * Notificates if a role is renamed so that the same change to be done
     * in the name of the role in the requirements if there is a defined
     * requirement for the specified role.
     *
     * @param oldName the old name of the role
     * @param newName the new name of the role
     */
    public void roleRenamed(String oldName, String newName) {
        ArrayList al_roles;
        ArrayList al_conds;
        DisplayList dl;
        Iterator it;
        String s;
        int ind;
        ind = reqList_.getSelectedIndex();
        if (requirements_.containsKey(oldName)) {
            al_conds = (ArrayList) requirements_.get(oldName);
            it = al_conds.iterator();
            while (it.hasNext()) {
                s = it.next().toString();
                al_roles = (ArrayList) condRoles_.get(s);
                al_roles.remove(oldName);
                al_roles.add(newName);
            }
            requirements_.remove(oldName);
            requirements_.put(newName, al_conds);
            reqList_.removeItem(oldName);
            reqList_.add(newName);
            dl = (DisplayList) reqDisplay_.get(oldName);
            reqDisplay_.remove(oldName);
            reqDisplay_.put(newName, dl);
        }
        reqList_.setSelectedIndex(ind);
    }

    /**
     * @return the map with all existing requirements.
     */
    public Map getReqirementsMap() {
        return requirements_;
    }

    /**
     * Reinitilizes all the maps using the information in the 
     * specified map.
     * 
     * @param reqs the requirements' <code>Map</code>.
     */
    public void loadData(Map reqs) {
        Iterator values;
        Iterator keys;
        requirements_ = reqs;
        reqDisplay_ = new HashMap();
        reqList_ = new ReqList();
        condRoles_ = new HashMap();
        keys = reqs.keySet().iterator();
        values = reqs.values().iterator();
        while (keys.hasNext() && values.hasNext()) {
            ArrayList al;
            String s;
            s = keys.next().toString();
            al = (ArrayList) values.next();
            processList(s, al);
            reqList_.add(s);
            reqDisplay_.put(s, new DisplayList(al.toArray()));
        }
        namesScroll_.getViewport().setView(reqList_);
        if (reqList_.getModel().getSize() > 0) {
            reqList_.setSelectedIndex(0);
        }
    }

    /**
     * Sets the parent for this panel.
     *
     * @param pp the parent 
     */
    public void setParent(PolicyPlugin pp) {
        parent_ = pp;
    }

    /**
     * This function is called when the index in the names table was changed
     * in order to be shown the list of conditions, which corresponds to the
     * new index.
     */
    private void changedListIndex() {
        JList list;
        String s;
        s = reqList_.getSelectedItem();
        list = (JList) reqDisplay_.get(s);
        listsScroll_.getViewport().setView(list);
    }

    /**
     * Describes the window for adding a condition to a role in a requirement.
     */
    private void addConditions(String toRole, NamesList toList, NamesList fromList) {
        final JInternalFrame frame;
        JButton cancelButton;
        JScrollPane scroll;
        JPanel buttonView;
        JButton okButton;
        final NamesList source = fromList;
        final NamesList dest = toList;
        final String roleName = toRole;
        scroll = new JScrollPane(source);
        scroll.setPreferredSize(new Dimension(300, 200));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 0, 10), new EtchedBorder()));
        okButton = new JButton("OK");
        okButton.setToolTipText("Click to add the selected conditions");
        okButton.setPreferredSize(new Dimension(75, 25));
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 25));
        buttonView = new JPanel();
        buttonView.setLayout(new BoxLayout(buttonView, BoxLayout.X_AXIS));
        buttonView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonView.add(Box.createHorizontalGlue());
        buttonView.add(okButton);
        buttonView.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonView.add(cancelButton);
        try {
            frame = Envision.showInternalFrame(new JInternalFrame("Add Condition", true, true, true, true));
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Object[] obj;
                    String s;
                    int i;
                    obj = source.getSelectedValues();
                    if (obj == null || obj.length == 0) {
                        Envision.showMessage("Your selection is empty!");
                        return;
                    }
                    for (i = 0; i < obj.length; i++) {
                        s = obj[i].toString();
                        if (!dest.getListData().contains(s)) {
                            dest.add(s);
                        }
                    }
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                }
            });
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                }
            });
            frame.addInternalFrameListener(new InternalFrameListener() {

                public void internalFrameActivated(InternalFrameEvent e) {
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                    frame.setVisible(false);
                    frame.getContentPane().removeAll();
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                }

                public void internalFrameOpened(InternalFrameEvent e) {
                }
            });
            frame.getContentPane().add(buttonView, BorderLayout.PAGE_END);
            frame.getContentPane().add(scroll, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            Envision.showException(e);
        }
    }

    /**
     * Saves the specified parameters as requirement.
     *
     * @param name the name of the role.
     * @param data the array with the chosen conditions
     * @throws Exception
     *              if no role name was chosen
     *              if no conditions for the role were chosen
     */
    private void saveNewRequirement(String name, Object[] data) throws Exception {
        ArrayList al_conds;
        ArrayList al_roles;
        Iterator it;
        int i;
        if (name == null) {
            throw new Exception("Select a role in order to create a requirement!");
        }
        if (data == null || data.length == 0) {
            throw new Exception("You cannot create a requirement without conditions!");
        }
        al_conds = new ArrayList();
        for (i = 0; i < data.length; i++) {
            al_conds.add(data[i]);
            if (condRoles_.containsKey(data[i])) {
                al_roles = (ArrayList) condRoles_.get(data[i]);
                al_roles.add(name);
            } else {
                al_roles = new ArrayList();
                al_roles.add(name);
                condRoles_.put(data[i], al_roles);
            }
        }
        reqList_.add(name);
        reqDisplay_.put(name, new DisplayList(al_conds.toArray()));
        requirements_.put(name, al_conds);
    }

    /**
     * Saves the made changes in a requirement and updates all the needed
     * map according to the changes.
     *
     * @param roleName  the name of the role in the edited requirement
     * @param edited    the list with the conditions for the role after
     *                  edition
     * @param notEdited the list with the conditions for the role before
     *                  edition
     */
    private void saveEditedRequirement(String roleName, NamesList edited, DisplayList notEdited) {
        ArrayList al_roles;
        Iterator it;
        Vector v;
        String s;
        int n;
        int i;
        it = notEdited.getVectorData().iterator();
        v = edited.getVectorData();
        while (it.hasNext()) {
            s = it.next().toString();
            if (!v.contains(s)) {
                al_roles = (ArrayList) condRoles_.get(s);
                al_roles.remove(roleName);
                if (al_roles.isEmpty()) {
                    condRoles_.remove(s);
                }
            }
        }
        it = edited.getVectorData().iterator();
        v = notEdited.getVectorData();
        while (it.hasNext()) {
            s = it.next().toString();
            if (!v.contains(s)) {
                if (condRoles_.containsKey(s)) {
                    al_roles = (ArrayList) condRoles_.get(s);
                    al_roles.add(roleName);
                } else {
                    al_roles = new ArrayList();
                    al_roles.add(roleName);
                    condRoles_.put(s, al_roles);
                }
            }
        }
        n = reqList_.getIndexOf(roleName);
        reqDisplay_.put(roleName, new DisplayList(edited.getData()));
        requirements_.put(roleName, edited.getListData());
        reqList_.setSelectedIndex(n);
    }

    /**
     * Adds all specified list of conditions and the corresponding role to
     * the map <code>condRoles_</code> This function is used only in the
     * initializing of this panel. All next changes in the map are made
     * from other functions in the class.
     *
     * @param roleName the name of the role.
     * @param list     the list with the names of the conditions defined
     *                 for the specified role in the requirement.
     */
    private void processList(String roleName, ArrayList list) {
        Iterator it;
        ArrayList al;
        String s;
        it = list.iterator();
        while (it.hasNext()) {
            s = it.next().toString();
            if (condRoles_.containsKey(s)) {
                al = (ArrayList) condRoles_.get(s);
                al.add(roleName);
            } else {
                al = new ArrayList();
                al.add(roleName);
                condRoles_.put(s, al);
            }
        }
    }

    /**
     * This list visualizes the names of the roles.
     */
    protected class ReqList extends JList implements ListSelectionListener {

        /**
         * The model of the list.
         */
        DefaultListModel model_;

        /**
         * The <code>Vector</code> object with all values
         * in this list.
         */
        Vector data_;

        /**
         * Class Constructor.
         */
        public ReqList() {
            Iterator it;
            data_ = new Vector();
            model_ = new DefaultListModel();
            setModel(model_);
            addListSelectionListener(this);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setPreferredSize(new Dimension(400, 500));
        }

        /**
         * Adds a new row in the list.
         *
         * @param name the value which must be added
         */
        public void add(String name) {
            data_.addElement(name);
            model_.addElement(name);
        }

        /**
         * Removes the selected row from the list.
         */
        public void removeItem(String item) {
            if (model_.contains(item)) {
                model_.removeElement(item);
                data_.remove(item);
            }
        }

        /**
         * Gets the selected value from the list as <code>String</code>.
         *
         * @return the slected value in the list
         */
        public String getSelectedItem() {
            String s;
            Object o;
            int row;
            row = getSelectedIndex();
            if (row < 0 || row > model_.size() - 1) {
                return null;
            }
            o = getSelectedValue();
            if (o == null) {
                return null;
            }
            s = o.toString();
            return s;
        }

        /**
         * Gets the values in the list as a <code>Vector</code>.
         *
         * @return the <code>Vector</code> object with all the values
         */
        public Vector getDataVector() {
            return data_;
        }

        /**
         *  Tests if the specified name is a component in this list.
         *
         * @param name   a name
         * @return true  if and only if the specified object is the same as
         *               a component in this list
         *         false otherwise.
         */
        public boolean contains(String name) {
            return model_.contains(name);
        }

        /**
         * Returns the index of the last occurrence of elem.
         *
         * @param elem the desired component
         * @return the index of the last occurrence of elem in the list;
         *             returns -1 if the object is not found
         */
        public int getIndexOf(String elem) {
            return model_.indexOf(elem);
        }

        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            changedListIndex();
        }
    }

    /**
     * This class is responsible for visualizing of the list with 
     * conditions' names.
     */
    protected class DisplayList extends JList implements MouseListener {

        /**
         * The model of the list.
         */
        DefaultListModel model_;

        /**
         * The <code>Vector</code> object with all values
         * in this list.
         */
        Vector data_;

        /**
         * Class Constructor which specifies the values in the list.
         *
         * @param data the array with the values for this list
         */
        public DisplayList(Object[] data) {
            int i;
            data_ = new Vector();
            model_ = new DefaultListModel();
            for (i = 0; i < data.length; i++) {
                model_.addElement(data[i]);
                data_.add(data[i]);
            }
            setModel(model_);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        /**
         * Gets the values in the list in an array.
         *
         * @return the array withe values in the list.
         */
        public Object[] getData() {
            return model_.toArray();
        }

        /**
         * Gets the values in the list in a <code>Vector</code>.
         *
         * @return the <code>Vector</code> object  with the values
         *         in the list.
         */
        public Vector getVectorData() {
            return data_;
        }

        /**
         * Adds a new row in the list.
         *
         * @param name the value which must be added
         */
        public void add(String name) {
            if (!data_.contains(name)) {
                model_.addElement(name);
                data_.add(name);
            }
        }

        /**
         * Removes the specified row in the list.
         *
         * @param name the value which must be removed.
         */
        public void remove(String name) {
            if (data_.contains(name)) {
                model_.removeElement(name);
                data_.remove(name);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
         */
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                editSelectedRequirement();
            }
        }

        /**
         * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * This class describes the list with conditions' 
     * names in the edition window.
     */
    protected class NamesList extends JList {

        /**
         * The model of the list.
         */
        DefaultListModel model_;

        /**
         * The <code>Vector</code> object with all values
         * in this list.
         */
        Vector data_;

        /**
         * Class Constructor which specifies the values in the list.
         *
         * @param data the array with the values
         */
        public NamesList(Object[] data) {
            String s;
            int i;
            model_ = new DefaultListModel();
            data_ = new Vector();
            for (i = 0; i < data.length; i++) {
                s = data[i].toString();
                model_.addElement(s);
                data_.add(s);
            }
            setModel(model_);
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }

        /**
         * Class Constructor which specifies the conditions' map, whose
         * keys must be the values in the list.
         *
         * @param map the map of conditions
         */
        public NamesList(Map map) {
            Iterator it;
            String s;
            model_ = new DefaultListModel();
            data_ = new Vector();
            it = map.keySet().iterator();
            while (it.hasNext()) {
                s = it.next().toString();
                model_.addElement(s);
                data_.add(s);
            }
            setModel(model_);
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }

        /**
         * Gets the values in the list in an array.
         *
         * @return the array withe values in the list.
         */
        public Object[] getData() {
            return model_.toArray();
        }

        /**
         * Gets the values in the list in a <code>ArrayList</code>.
         *
         * @return the <code>ArrayList</code> object  with the values
         *         in the list.
         */
        public ArrayList getListData() {
            ArrayList alist;
            Object[] obj;
            int i;
            obj = model_.toArray();
            if (obj == null || obj.length == 0) {
                return null;
            }
            alist = new ArrayList();
            for (i = 0; i < obj.length; i++) {
                alist.add(obj[i]);
            }
            return alist;
        }

        /**
         * Gets the values in the list in a <code>Vector</code>.
         *
         * @return the <code>Vector</code> object  with the values
         *         in the list.
         */
        public Vector getVectorData() {
            return data_;
        }

        /**
         * Adds a new row in the list.
         *
         * @param name the value which must be added
         */
        public void add(String name) {
            model_.addElement(name);
            data_.add(name);
        }

        /**
         * Removes the specified row in the list.
         *
         * @param name the value which must be removed.
         */
        public void remove(String name) {
            model_.removeElement(name);
            data_.remove(name);
        }

        /**
         *  Tests if the specified name is a component in this list.
         *
         * @param name   a name
         * @return true  if and only if the specified object is the same as
         *               a component in this list
         *         false otherwise.

         */
        public boolean contains(String name) {
            return model_.contains(name);
        }
    }
}
