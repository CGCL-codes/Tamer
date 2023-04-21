package jeplus.gui;

import java.util.Enumeration;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import jeplus.data.ParameterItem;
import jeplus.util.DNDTree;

/**
 *
 * @author yzhang
 */
public class JPanel_ParameterTree extends javax.swing.JPanel implements TitledJPanel {

    protected String Title = "Parameter Tree";

    protected ParameterItem CurrentItem = null;

    protected DefaultMutableTreeNode ParamTreeRoot = null;

    protected DefaultTreeModel ParamTreeModel = null;

    protected JTree jTreeParams = null;

    private DocumentListener DL = null;

    /** Creates new form JPanel_ParameterTree */
    public JPanel_ParameterTree() {
        initComponents();
        setParameterTree(null);
    }

    /**
     * Get title of this panel
     * @return Title of this panel instance
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Set title to this panel
     * @param title new title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * Set the root of the parameter tree
     * @param Root Root node
     */
    public final void setParameterTree(DefaultMutableTreeNode Root) {
        ParamTreeRoot = Root;
        initParamTree();
    }

    /**
     * Get the parameter tree
     * @return Root node of the current tree
     */
    public DefaultMutableTreeNode getParameterTree() {
        return ParamTreeRoot;
    }

    /**
     * initialises the parameter tree, by setting up tree nodes and tree model
     */
    protected void initParamTree() {
        if (ParamTreeRoot == null) {
            ParamTreeRoot = new DefaultMutableTreeNode(new ParameterItem());
        }
        ParamTreeModel = new DefaultTreeModel(ParamTreeRoot);
        jTreeParams = new DNDTree(DNDTree.createTree());
        jTreeParams.setModel(ParamTreeModel);
        jTreeParams.setEditable(false);
        jTreeParams.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTreeParams.setShowsRootHandles(true);
        jTreeParams.setExpandsSelectedPaths(true);
        jTreeParams.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeParamsValueChanged(evt);
            }
        });
        jTreeParams.setSelectionPath(new TreePath(ParamTreeRoot.getLastLeaf().getPath()));
        this.jScroll.setViewportView(jTreeParams);
        DL = new DocumentListener() {

            Document DocShortName = txtShortName.getDocument();

            Document DocName = txtName.getDocument();

            Document DocDescript = txtDescript.getDocument();

            Document DocSearchString = txtSearchString.getDocument();

            Document DocAltValues = txtAltValues.getDocument();

            public void insertUpdate(DocumentEvent e) {
                Document src = e.getDocument();
                if (src == DocShortName) {
                    CurrentItem.setID(txtShortName.getText());
                } else if (src == DocName) {
                    CurrentItem.setName(txtName.getText());
                } else if (src == DocDescript) {
                    CurrentItem.setDescription(txtDescript.getText());
                } else if (src == DocSearchString) {
                    CurrentItem.setSearchString(txtSearchString.getText());
                } else if (src == DocAltValues) {
                    CurrentItem.setValuesString(txtAltValues.getText());
                    txpPreview.setText(getAltValuesPreview());
                    resetAltValueNumbers();
                }
                jTreeParams.update(jTreeParams.getGraphics());
            }

            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            public void changedUpdate(DocumentEvent e) {
            }
        };
        txtShortName.getDocument().addDocumentListener(DL);
        txtName.getDocument().addDocumentListener(DL);
        txtDescript.getDocument().addDocumentListener(DL);
        txtSearchString.getDocument().addDocumentListener(DL);
        txtAltValues.getDocument().addDocumentListener(DL);
    }

    /**
     * Handles the event that a new tree node is selected
     * @param evt not in use
     */
    private void jTreeParamsValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeParams.getLastSelectedPathComponent();
        if (node == null) {
            CurrentItem = null;
        } else {
            Object nodeInfo = node.getUserObject();
            if (node.isLeaf()) {
                CurrentItem = (ParameterItem) nodeInfo;
            } else {
                CurrentItem = (ParameterItem) nodeInfo;
            }
        }
        displayParamDetails();
    }

    /**
     * Updates the preview of the alternative values of the parameter item
     * @return A string to be set to the label where the preview is displayed
     */
    protected String getAltValuesPreview() {
        StringBuilder buf = new StringBuilder("{");
        if (CurrentItem != null) {
            String[] list = CurrentItem.getAlternativeValues();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    if (i > 0) buf.append(", ");
                    buf.append(list[i]);
                }
            } else {
                buf.append("!Error!");
            }
        } else {
            buf.append("...");
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * Displays details of the selected parameter item in the relevant text fields
     * and combo boxes. If no item is selected, this function disables the text
     * fields and boxes.
     */
    protected void displayParamDetails() {
        if (DL != null) {
            txtShortName.getDocument().removeDocumentListener(DL);
            txtName.getDocument().removeDocumentListener(DL);
            txtDescript.getDocument().removeDocumentListener(DL);
            txtSearchString.getDocument().removeDocumentListener(DL);
            txtAltValues.getDocument().removeDocumentListener(DL);
        }
        if (CurrentItem != null) {
            txtName.setEnabled(true);
            txtShortName.setEnabled(true);
            txtDescript.setEnabled(true);
            txtSearchString.setEnabled(true);
            txtAltValues.setEnabled(true);
            cboParamType.setEnabled(true);
            cboType.setEnabled(true);
            cboPlatform.setEnabled(true);
            cboFixValue.setEnabled(true);
            txtName.setText(CurrentItem.Name);
            txtShortName.setText(CurrentItem.ID);
            txtDescript.setText(CurrentItem.Description);
            txtSearchString.setText(CurrentItem.SearchString);
            txtAltValues.setText(CurrentItem.getValuesString());
            cboParamType.setSelectedIndex(CurrentItem.ParamType);
            cboType.setSelectedIndex(CurrentItem.Type);
            cboPlatform.setSelectedIndex(CurrentItem.getPlatform());
            txpPreview.setText(getAltValuesPreview());
            resetAltValueNumbers();
            cboFixValue.setSelectedIndex(CurrentItem.getSelectedAltValue());
        } else {
            txtName.setEnabled(false);
            txtShortName.setEnabled(false);
            txtDescript.setEnabled(false);
            txtSearchString.setEnabled(false);
            txtAltValues.setEnabled(false);
            cboParamType.setEnabled(false);
            cboType.setEnabled(false);
            cboPlatform.setEnabled(false);
            cboFixValue.setEnabled(false);
            txtName.setText("");
            txtShortName.setText("");
            txtDescript.setText("");
            txtSearchString.setText("");
            txtAltValues.setText("");
            cboParamType.setSelectedIndex(0);
            cboType.setSelectedIndex(0);
            cboPlatform.setSelectedIndex(0);
            txpPreview.setText("");
            cboFixValue.setSelectedIndex(0);
        }
        if (DL != null) {
            txtShortName.getDocument().addDocumentListener(DL);
            txtName.getDocument().addDocumentListener(DL);
            txtDescript.getDocument().addDocumentListener(DL);
            txtSearchString.getDocument().addDocumentListener(DL);
            txtAltValues.getDocument().addDocumentListener(DL);
        }
    }

    /**
     * Reads information from the text fields and combo-boxes to update the
     * currently selected parameter item
     */
    protected void updateParamDetails() {
        if (CurrentItem != null) {
            CurrentItem.Name = txtName.getText().trim();
            CurrentItem.ID = txtShortName.getText().trim();
            CurrentItem.Description = txtDescript.getText().trim();
            CurrentItem.SearchString = txtSearchString.getText().trim();
            txpPreview.setText(getAltValuesPreview());
            resetAltValueNumbers();
            CurrentItem.setValuesString(txtAltValues.getText().trim());
            CurrentItem.Type = cboType.getSelectedIndex();
        }
    }

    private void resetAltValueNumbers() {
        if (CurrentItem != null) {
            String[] idx = new String[CurrentItem.getNAltValues() + 1];
            idx[0] = "-";
            for (int i = 1; i < idx.length; i++) {
                idx[i] = Integer.toString(i);
            }
            cboFixValue.setModel(new DefaultComboBoxModel(idx));
        } else {
            cboFixValue.setModel(new DefaultComboBoxModel(new String[] { "-" }));
        }
    }

    /**
     * Move the selected item up one level
     * @return The node being moved
     */
    public DefaultMutableTreeNode moveUpParameterItem() {
        DefaultMutableTreeNode node = null, parent = null;
        TreePath path = jTreeParams.getSelectionPath();
        if (path != null) {
            int len = path.getPathCount();
            Object[] paths = path.getPath();
            if (len >= 3) {
                node = (DefaultMutableTreeNode) (path.getLastPathComponent());
                ParamTreeModel.removeNodeFromParent(node);
                path = path.getParentPath().getParentPath();
                parent = (DefaultMutableTreeNode) paths[len - 3];
                ParamTreeModel.insertNodeInto(node, parent, parent.getChildCount());
                jTreeParams.scrollPathToVisible(path);
                jTreeParams.setSelectionPath(path);
                return node;
            }
        }
        return null;
    }

    /**
     * Remove the selected node from the tree
     * @return the node being removed
     */
    public DefaultMutableTreeNode removeParameterItem() {
        DefaultMutableTreeNode node = null, parent = null;
        TreePath path = jTreeParams.getSelectionPath();
        if (path != null) {
            node = (DefaultMutableTreeNode) (path.getLastPathComponent());
            path = path.getParentPath();
            if (path != null) {
                parent = (DefaultMutableTreeNode) (path.getLastPathComponent());
                for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                    ParamTreeModel.insertNodeInto((MutableTreeNode) e.nextElement(), parent, parent.getChildCount());
                }
                ParamTreeModel.removeNodeFromParent(node);
                jTreeParams.scrollPathToVisible(path);
                jTreeParams.setSelectionPath(path);
                return node;
            }
        }
        return null;
    }

    /**
     * Delete the whole branch of the selected node from the tree
     * @return the root node of the branch being removed
     */
    public DefaultMutableTreeNode deleteParameterBranch() {
        int n = JOptionPane.showConfirmDialog(this, "Are you sure that you want to delete the whole branch?", "Deleting branch", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.NO_OPTION) {
            return null;
        }
        DefaultMutableTreeNode node = null;
        TreePath path = jTreeParams.getSelectionPath();
        if (path != null) {
            node = (DefaultMutableTreeNode) (path.getLastPathComponent());
            path = path.getParentPath();
            if (path != null) {
                ParamTreeModel.removeNodeFromParent(node);
                jTreeParams.scrollPathToVisible(path);
                jTreeParams.setSelectionPath(path);
                return node;
            }
        }
        return null;
    }

    /**
     * Make a copy of the selected item and insert it at the same level.
     * @return The copy of the item being inserted
     */
    public DefaultMutableTreeNode copyParameterItem() {
        if (CurrentItem != null) {
            ParameterItem child = new ParameterItem(CurrentItem);
            DefaultMutableTreeNode parentNode = null;
            TreePath parentPath = jTreeParams.getSelectionPath();
            if (parentPath != null) {
                parentPath = parentPath.getParentPath();
                if (parentPath != null) {
                    parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                } else {
                    parentNode = ParamTreeRoot;
                }
            } else {
                parentNode = null;
            }
            return addObject(parentNode, child, true);
        }
        return null;
    }

    /**
     * Add a new item to the tree
     * @param child The ParameterItem to be store the node-to-add
     * @return The new node
     */
    public DefaultMutableTreeNode addParameterItem(ParameterItem child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = jTreeParams.getSelectionPath();
        if (parentPath == null) {
            parentNode = ParamTreeRoot;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
        }
        return addObject(parentNode, child, true);
    }

    /**
     * Utility function for inserting a node into the tree
     * @param parent The parent location where the child is to be inserted
     * @param child The child user object to be added
     * @param shouldBeVisible Adjust display to show the new insertion if set true
     * @return The new node of the tree
     */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        if (parent == null) return null;
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        ParamTreeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        if (shouldBeVisible) {
            TreePath path = new TreePath(childNode.getPath());
            jTreeParams.scrollPathToVisible(path);
            jTreeParams.setSelectionPath(path);
        }
        return childNode;
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        cboPlatform = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cmdMoveUp = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtDescript = new javax.swing.JTextField();
        cboType = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtShortName = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtSearchString = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtAltValues = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txpPreview = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        cboFixValue = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cboParamType = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jScroll = new javax.swing.JScrollPane();
        cmdAdd = new javax.swing.JButton();
        cmdDeleteBranch = new javax.swing.JButton();
        cmdDuplicate = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cboPlatform.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any platform", "Windows only", "Mac/Linux only" }));
        cboPlatform.setToolTipText("Some simulation options require a particular version/platform of E+. This option has no effect in the current version of jEPlus. ");
        cboPlatform.setEnabled(false);
        cboPlatform.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPlatformActionPerformed(evt);
            }
        });
        jLabel1.setText("Version requirement");
        cmdMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jeplus/images/swap_up.png")));
        cmdMoveUp.setToolTipText("Move the selected item up one level.");
        cmdMoveUp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdMoveUpActionPerformed(evt);
            }
        });
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parameter item", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(102, 102, 102)));
        jLabel5.setText("Name");
        jLabel19.setText("Description");
        txtName.setText("parameter1");
        txtName.setToolTipText("Memorable name of the parameter");
        txtName.setEnabled(false);
        txtName.setMinimumSize(new java.awt.Dimension(120, 20));
        txtName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        txtName.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        txtDescript.setToolTipText("Description of the parameter");
        txtDescript.setEnabled(false);
        txtDescript.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescriptActionPerformed(evt);
            }
        });
        txtDescript.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDescriptFocusLost(evt);
            }
        });
        cboType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Integer", "Double", "Discrete" }));
        cboType.setToolTipText("Parameter type");
        cboType.setEnabled(false);
        cboType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTypeActionPerformed(evt);
            }
        });
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel20.setText("Type");
        jLabel21.setText("ID");
        txtShortName.setText("PAR1");
        txtShortName.setToolTipText("Parameter ID is used in Job ID");
        txtShortName.setEnabled(false);
        txtShortName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShortNameActionPerformed(evt);
            }
        });
        txtShortName.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShortNameFocusLost(evt);
            }
        });
        jLabel22.setText("Search string");
        txtSearchString.setToolTipText("A string tag to be inserted into the model template");
        txtSearchString.setEnabled(false);
        txtSearchString.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchStringActionPerformed(evt);
            }
        });
        txtSearchString.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSearchStringFocusLost(evt);
            }
        });
        jLabel24.setText("Alternative values");
        txtAltValues.setToolTipText("Specify a list of alternative values. For detailed syntax, please refer to users guide.");
        txtAltValues.setEnabled(false);
        txtAltValues.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAltValuesActionPerformed(evt);
            }
        });
        txtAltValues.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAltValuesFocusLost(evt);
            }
        });
        jLabel26.setText("Preview");
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBar(null);
        txpPreview.setContentType("text/html");
        txpPreview.setEditable(false);
        txpPreview.setToolTipText("Preview of the list of alternative values. Please note if sampling (@sample) is used, the preview list is NOT that to be used in simulations.");
        jScrollPane2.setViewportView(txpPreview);
        jLabel2.setText("Fix option on the ");
        cboFixValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "1", "2", "3", "4", "" }));
        cboFixValue.setToolTipText("Select the i-th value for this batch of simulations. This is useful for maintain consistent job indexes for partial runs.");
        cboFixValue.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFixValueActionPerformed(evt);
            }
        });
        jLabel3.setText("-th value in this batch");
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel23.setText("Value type");
        cboParamType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Parametrics" }));
        cboParamType.setToolTipText("Parameter type");
        cboParamType.setEnabled(false);
        cboParamType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboParamTypeActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup().addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(4, 4, 4).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cboParamType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).addComponent(txtAltValues, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(txtSearchString, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(cboFixValue, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)).addComponent(txtDescript, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel21).addComponent(txtShortName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel20).addComponent(cboParamType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtDescript, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19)).addGap(8, 8, 8).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtSearchString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel22).addComponent(jLabel23).addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtAltValues, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel24)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel26).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(cboFixValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jScroll.setToolTipText("Drag&Drop to edit the parameter tree. Hold 'Ctrl' key to copy a branch.");
        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jeplus/images/plus1.png")));
        cmdAdd.setToolTipText("Add a new item under the current item.");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        cmdDeleteBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jeplus/images/cross.png")));
        cmdDeleteBranch.setToolTipText("Delete the whole branch");
        cmdDeleteBranch.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteBranchActionPerformed(evt);
            }
        });
        cmdDuplicate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jeplus/images/page_copy.png")));
        cmdDuplicate.setToolTipText("Make a copy of the current item.");
        cmdDuplicate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDuplicateActionPerformed(evt);
            }
        });
        cmdRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jeplus/images/minus.png")));
        cmdRemove.setToolTipText("Remove the selected item.");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScroll).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(cmdRemove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addComponent(cmdDuplicate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addComponent(cmdAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmdDeleteBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(cmdAdd).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdDuplicate).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdRemove).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdDeleteBranch).addGap(0, 0, Short.MAX_VALUE)).addComponent(jScroll));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {
        ParameterItem item = new ParameterItem();
        this.addParameterItem(item);
        this.CurrentItem = item;
        this.displayParamDetails();
    }

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        removeParameterItem();
    }

    private void cmdMoveUpActionPerformed(java.awt.event.ActionEvent evt) {
        moveUpParameterItem();
    }

    private void cmdDuplicateActionPerformed(java.awt.event.ActionEvent evt) {
        copyParameterItem();
    }

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void txtDescriptActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtDescriptFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void cboTypeActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentItem.setType(cboType.getSelectedIndex());
        txpPreview.setText(getAltValuesPreview());
        this.resetAltValueNumbers();
    }

    private void txtShortNameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtShortNameFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void txtSearchStringActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtSearchStringFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void txtAltValuesActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtAltValuesFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void cmdDeleteBranchActionPerformed(java.awt.event.ActionEvent evt) {
        deleteParameterBranch();
    }

    private void cboPlatformActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentItem.setPlatform(cboPlatform.getSelectedIndex());
    }

    private void cboFixValueActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentItem.setSelectedAltValue(cboFixValue.getSelectedIndex());
    }

    private void cboParamTypeActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentItem.setParamType(cboParamType.getSelectedIndex());
    }

    private javax.swing.JComboBox cboFixValue;

    private javax.swing.JComboBox cboParamType;

    private javax.swing.JComboBox cboPlatform;

    private javax.swing.JComboBox cboType;

    private javax.swing.JButton cmdAdd;

    private javax.swing.JButton cmdDeleteBranch;

    private javax.swing.JButton cmdDuplicate;

    private javax.swing.JButton cmdMoveUp;

    private javax.swing.JButton cmdRemove;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScroll;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextPane txpPreview;

    private javax.swing.JTextField txtAltValues;

    private javax.swing.JTextField txtDescript;

    private javax.swing.JTextField txtName;

    private javax.swing.JTextField txtSearchString;

    private javax.swing.JTextField txtShortName;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame main = new JFrame("Test window");
                main.setSize(800, 600);
                main.getContentPane().add(new JPanel_ParameterTree());
                main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                main.setVisible(true);
            }
        });
    }
}
