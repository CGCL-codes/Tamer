package unbbayes.gui.umpst;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import unbbayes.model.umpst.entities.EntityModel;
import unbbayes.model.umpst.groups.GroupsModel;
import unbbayes.model.umpst.project.SearchModelEntity;
import unbbayes.model.umpst.project.SearchModelGoal;
import unbbayes.model.umpst.project.SearchModelGroup;
import unbbayes.model.umpst.project.UMPSTProject;
import unbbayes.model.umpst.requirements.GoalModel;

public class GroupsAdd extends IUMPSTPanel {

    private GridBagConstraints constraint = new GridBagConstraints();

    private JLabel titulo = new JLabel();

    private JButton buttonAdd = new JButton();

    private JButton buttonCancel = new JButton("Cancel");

    private JButton buttonBackEntities = new JButton("Add entity backtracking ");

    private JButton buttonBackAtributes = new JButton("Add atribute backtracking");

    private JButton buttonBackRelationship = new JButton("Add relationship backtracking");

    private JTextField dateText, authorText;

    private JTextField groupText;

    private JTextArea commentsText;

    private GroupsModel group;

    private static final long serialVersionUID = 1L;

    private JList list, listAux, listAtributeAux, listRelationshipAux;

    private DefaultListModel listModel = new DefaultListModel();

    private DefaultListModel listModelAux = new DefaultListModel();

    private DefaultListModel listModelAtrAux = new DefaultListModel();

    private DefaultListModel listModelRltAux = new DefaultListModel();

    private Object[][] dataBacktracking = {};

    private Object[][] dataFrame = {};

    public GroupsAdd(UmpstModule janelaPai, UMPSTProject umpstProject, GroupsModel group) {
        super(janelaPai);
        this.setUmpstProject(umpstProject);
        this.group = group;
        this.setLayout(new GridBagLayout());
        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.weightx = 0.5;
        constraint.weighty = 0.3;
        panelText();
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.weightx = 0.5;
        constraint.weighty = 0.7;
        add(getBacktrackingPanel(), constraint);
        listeners();
        if (group == null) {
            titulo.setText("Add new group");
            buttonAdd.setText(" Add ");
        } else {
            titulo.setText("Update Group");
            buttonAdd.setText(" Update ");
            groupText.setText(group.getGroupName());
            commentsText.setText(group.getComments());
            authorText.setText(group.getAuthor());
            dateText.setText(group.getDate());
        }
    }

    public void panelText() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        panel.add(new JLabel("Entity Description: "), c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        panel.add(new JLabel("Author Name: "), c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        panel.add(new JLabel("Date: "), c);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        panel.add(new JLabel("Comments: "), c);
        GridBagConstraints d = new GridBagConstraints();
        d.gridx = 0;
        d.gridy = 0;
        d.fill = GridBagConstraints.PAGE_START;
        d.gridwidth = 3;
        d.insets = new Insets(0, 0, 0, 0);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setBackground(new Color(0x4169AA));
        panel.add(titulo, d);
        groupText = new JTextField(20);
        commentsText = new JTextArea(5, 21);
        authorText = new JTextField(20);
        dateText = new JTextField(20);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(groupText, c);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        panel.add(authorText, c);
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 2;
        panel.add(dateText, c);
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        panel.add(commentsText, c);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        panel.add(buttonCancel, c);
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 1;
        panel.add(buttonAdd, c);
        panel.setBorder(BorderFactory.createTitledBorder("group's details"));
        add(panel, constraint);
    }

    public void listeners() {
        buttonAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (group == null) {
                    try {
                        if (groupText.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Group's name is empty!");
                        } else {
                            GroupsModel groupAdd = updateMapGroups();
                            updateMapSearch(groupAdd);
                            updateTableGroups();
                            JOptionPane.showMessageDialog(null, "group successfully added", null, JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Error while creating group", "UnBBayes", JOptionPane.WARNING_MESSAGE);
                        UmpstModule pai = getFatherPanel();
                        changePanel(pai.getMenuPanel());
                    }
                } else {
                    if (JOptionPane.showConfirmDialog(null, "Do you want to update this group?", "UnBBayes", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try {
                            Set<GroupsModel> aux = new HashSet<GroupsModel>();
                            GroupsModel groupBeta;
                            String[] strAux = group.getGroupName().split(" ");
                            for (int i = 0; i < strAux.length; i++) {
                                if (getUmpstProject().getMapSearchGroups().get(strAux[i]) != null) {
                                    getUmpstProject().getMapSearchGroups().get(strAux[i]).getRelatedGroups().remove(group);
                                    aux = getUmpstProject().getMapSearchGroups().get(strAux[i]).getRelatedGroups();
                                    for (Iterator<GroupsModel> it = aux.iterator(); it.hasNext(); ) {
                                        groupBeta = it.next();
                                    }
                                }
                            }
                            group.setGroupName(groupText.getText());
                            group.setComments(commentsText.getText());
                            group.setAuthor(authorText.getText());
                            group.setDate(dateText.getText());
                            updateMapSearch(group);
                            updateTableGroups();
                            JOptionPane.showMessageDialog(null, "group successfully updated", "UnBBayes", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception e2) {
                            JOptionPane.showMessageDialog(null, "Error while ulpating group", "UnBBayes", JOptionPane.WARNING_MESSAGE);
                            UmpstModule pai = getFatherPanel();
                            changePanel(pai.getMenuPanel());
                        }
                    }
                }
            }
        });
        buttonBackRelationship.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createFrameRelationship();
            }
        });
        buttonBackAtributes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createFrameAtributes();
            }
        });
        buttonBackEntities.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createFrame();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                UmpstModule pai = getFatherPanel();
                changePanel(pai.getMenuPanel());
            }
        });
        groupText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                commentsText.requestFocus();
            }
        });
        authorText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dateText.requestFocus();
            }
        });
        dateText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                buttonAdd.requestFocus();
            }
        });
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainPanel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public GroupsModel updateMapGroups() {
        String idAux = "";
        int tamanho = getUmpstProject().getMapGroups().size() + 1;
        if (getUmpstProject().getMapGroups().size() != 0) {
            idAux = tamanho + "";
        } else {
            idAux = "1";
        }
        GroupsModel groupAdd = new GroupsModel(idAux, groupText.getText(), commentsText.getText(), authorText.getText(), dateText.getText(), null, null, null, null, null, null);
        getUmpstProject().getMapGroups().put(groupAdd.getId(), groupAdd);
        return groupAdd;
    }

    public void updateTableGroups() {
        String[] columnNames = { "ID", "Group", "", "" };
        Object[][] data = new Object[getUmpstProject().getMapGroups().size()][4];
        Integer i = 0;
        Set<String> keys = getUmpstProject().getMapGroups().keySet();
        TreeSet<String> sortedKeys = new TreeSet<String>(keys);
        for (String key : sortedKeys) {
            data[i][0] = getUmpstProject().getMapGroups().get(key).getId();
            data[i][1] = getUmpstProject().getMapGroups().get(key).getGroupName();
            data[i][2] = "";
            data[i][3] = "";
            i++;
        }
        UmpstModule pai = getFatherPanel();
        changePanel(pai.getMenuPanel());
        TableGroups groupTable = pai.getMenuPanel().getGroupsPane().getGroupsTable();
        JTable table = groupTable.createTable(columnNames, data);
        groupTable.getScrollPanePergunta().setViewportView(table);
        groupTable.getScrollPanePergunta().updateUI();
        groupTable.getScrollPanePergunta().repaint();
        groupTable.updateUI();
        groupTable.repaint();
    }

    public void updateMapSearch(GroupsModel groupAdd) {
        String[] strAux = {};
        strAux = groupAdd.getGroupName().split(" ");
        Set<GroupsModel> groupSetSearch = new HashSet<GroupsModel>();
        for (int i = 0; i < strAux.length; i++) {
            if (!strAux[i].equals(" ")) {
                if (getUmpstProject().getMapSearchGroups().get(strAux[i]) == null) {
                    groupSetSearch.add(groupAdd);
                    SearchModelGroup searchModel = new SearchModelGroup(strAux[i], groupSetSearch);
                    getUmpstProject().getMapSearchGroups().put(searchModel.getKeyWord(), searchModel);
                } else {
                    getUmpstProject().getMapSearchGroups().get(strAux[i]).getRelatedGroups().add(groupAdd);
                }
            }
        }
    }

    public JPanel getBacktrackingPanel() {
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        if (group != null) {
            listAux = group.getBacktrackingEntities();
            for (int i = 0; i < listAux.getModel().getSize(); i++) {
                listModelAux.addElement((listAux.getModel().getElementAt(i)));
            }
            listAtributeAux = group.getBacktrackingAtributes();
            for (int i = 0; i < listAtributeAux.getModel().getSize(); i++) {
                listModelAtrAux.addElement((listAtributeAux.getModel().getElementAt(i)));
            }
            listRelationshipAux = group.getBacktrackingRelationship();
            for (int i = 0; i < listRelationshipAux.getModel().getSize(); i++) {
                listModelRltAux.addElement((listRelationshipAux.getModel().getElementAt(i)));
            }
            listAux = new JList(listModelAux);
            listAtributeAux = new JList(listModelAtrAux);
            listRelationshipAux = new JList(listModelRltAux);
            dataBacktracking = new Object[listAux.getModel().getSize() + listAtributeAux.getModel().getSize() + listRelationshipAux.getModel().getSize()][3];
            int i;
            for (i = 0; i < listAux.getModel().getSize(); i++) {
                dataBacktracking[i][0] = listAux.getModel().getElementAt(i);
                dataBacktracking[i][1] = "Entity";
                dataBacktracking[i][2] = "";
            }
            int j;
            for (j = 0; j < listAtributeAux.getModel().getSize(); j++) {
                dataBacktracking[j + i][0] = listAtributeAux.getModel().getElementAt(j);
                dataBacktracking[j + i][1] = "Atribute";
                dataBacktracking[j + i][2] = "";
            }
            int k;
            for (k = 0; k < listRelationshipAux.getModel().getSize(); k++) {
                dataBacktracking[k + j + i][0] = listRelationshipAux.getModel().getElementAt(k);
                dataBacktracking[k + j + i][1] = "Relationship";
                dataBacktracking[k + j + i][2] = "";
            }
            String[] columns = { "Name", "Type", "" };
            DefaultTableModel model = new DefaultTableModel(dataBacktracking, columns);
            JTable table = new JTable(model);
            TableButton buttonDel = new TableButton(new TableButton.TableButtonCustomizer() {

                public void customize(JButton button, int row, int column) {
                    button.setIcon(new ImageIcon("images/del.gif"));
                }
            });
            TableColumn buttonColumn1 = table.getColumnModel().getColumn(columns.length - 1);
            buttonColumn1.setMaxWidth(28);
            buttonColumn1.setCellRenderer(buttonDel);
            buttonColumn1.setCellEditor(buttonDel);
            buttonDel.addHandler(new TableButton.TableButtonPressedHandler() {

                public void onButtonPress(int row, int column) {
                    if (row < listAux.getModel().getSize()) {
                        String key = dataBacktracking[row][0].toString();
                        listModelAux.remove(listModelAux.indexOf(key));
                        listAux = new JList(listModelAux);
                        group.setBacktrackingEntities(listAux);
                    } else {
                        if (row < (listAux.getModel().getSize() + listAtributeAux.getModel().getSize())) {
                            String keyAtr = dataBacktracking[row][0].toString();
                            listModelAtrAux.remove(listModelAtrAux.indexOf(keyAtr));
                            listAtributeAux = new JList(listModelAtrAux);
                            group.setBacktrackingAtributes(listAtributeAux);
                        } else {
                            String keyAtr = dataBacktracking[row][0].toString();
                            listModelRltAux.remove(listModelRltAux.indexOf(keyAtr));
                            listRelationshipAux = new JList(listModelRltAux);
                            group.setBacktrackingRelationship(listRelationshipAux);
                        }
                    }
                    UmpstModule father = getFatherPanel();
                    changePanel(father.getMenuPanel().getGroupsPane().getGroupsPanel().getGroupsAdd(group));
                }
            });
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            if (group != null) {
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 1;
                panel.add(buttonBackEntities, c);
                buttonBackEntities.setToolTipText("Add backtracking from entities");
                c.gridx = 1;
                c.gridy = 0;
                c.gridwidth = 1;
                panel.add(buttonBackAtributes, c);
                buttonBackAtributes.setToolTipText("Add backtracking from atributes");
                c.gridx = 2;
                c.gridy = 0;
                c.gridwidth = 1;
                panel.add(buttonBackRelationship, c);
                buttonBackRelationship.setToolTipText("Add backtracking from relationship");
            }
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0.9;
            c.weighty = 0.9;
            c.gridwidth = 6;
            scrollPane = new JScrollPane(table);
            panel.add(scrollPane, c);
        }
        return panel;
    }

    public void createFrame() {
        JFrame frame = new JFrame("Adding Backtracking from entities");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] columnNames = { "ID", "Entity", "" };
        dataFrame = new Object[getUmpstProject().getMapEntity().size()][3];
        Integer i = 0;
        Set<String> keys = getUmpstProject().getMapEntity().keySet();
        TreeSet<String> sortedKeys = new TreeSet<String>(keys);
        for (String key : sortedKeys) {
            dataFrame[i][0] = getUmpstProject().getMapEntity().get(key).getId();
            dataFrame[i][1] = getUmpstProject().getMapEntity().get(key).getEntityName();
            dataFrame[i][2] = "";
            i++;
        }
        DefaultTableModel model = new DefaultTableModel(dataFrame, columnNames);
        JTable table = new JTable(model);
        TableButton buttonEdit = new TableButton(new TableButton.TableButtonCustomizer() {

            public void customize(JButton button, int row, int column) {
                button.setIcon(new ImageIcon("images/add.gif"));
            }
        });
        TableColumn buttonColumn1 = table.getColumnModel().getColumn(columnNames.length - 1);
        buttonColumn1.setMaxWidth(28);
        buttonColumn1.setCellRenderer(buttonEdit);
        buttonColumn1.setCellEditor(buttonEdit);
        buttonEdit.addHandler(new TableButton.TableButtonPressedHandler() {

            public void onButtonPress(int row, int column) {
                String key = dataFrame[row][1].toString();
                list = group.getBacktrackingEntities();
                listModel.addElement(key);
                list = new JList(listModel);
                group.setBacktrackingEntities(list);
                Set<String> keys = getUmpstProject().getMapEntity().keySet();
                TreeSet<String> sortedKeys = new TreeSet<String>(keys);
                for (String keyAux : sortedKeys) {
                    if (getUmpstProject().getMapEntity().get(keyAux).getEntityName().equals(key)) {
                        getUmpstProject().getMapEntity().get(keyAux).getFowardTrackingGroups().add(group);
                    }
                }
                UmpstModule father = getFatherPanel();
                changePanel(father.getMenuPanel().getGroupsPane().getGroupsPanel().getGroupsAdd(group));
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scroll, c);
        panel.setPreferredSize(new Dimension(400, 200));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    public void createFrameAtributes() {
        JFrame frame = new JFrame("Adding Backtracking from atributes");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] columnNames = { "ID", "Atribute", "" };
        dataFrame = new Object[getUmpstProject().getMapAtribute().size()][3];
        Integer i = 0;
        Set<String> keys = getUmpstProject().getMapAtribute().keySet();
        TreeSet<String> sortedKeys = new TreeSet<String>(keys);
        for (String key : sortedKeys) {
            dataFrame[i][0] = getUmpstProject().getMapAtribute().get(key).getId();
            dataFrame[i][1] = getUmpstProject().getMapAtribute().get(key).getAtributeName();
            dataFrame[i][2] = "";
            i++;
        }
        DefaultTableModel model = new DefaultTableModel(dataFrame, columnNames);
        JTable table = new JTable(model);
        TableButton buttonEdit = new TableButton(new TableButton.TableButtonCustomizer() {

            public void customize(JButton button, int row, int column) {
                button.setIcon(new ImageIcon("images/add.gif"));
            }
        });
        TableColumn buttonColumn1 = table.getColumnModel().getColumn(columnNames.length - 1);
        buttonColumn1.setMaxWidth(28);
        buttonColumn1.setCellRenderer(buttonEdit);
        buttonColumn1.setCellEditor(buttonEdit);
        buttonEdit.addHandler(new TableButton.TableButtonPressedHandler() {

            public void onButtonPress(int row, int column) {
                String key = dataFrame[row][1].toString();
                list = group.getBacktrackingAtributes();
                listModel.addElement(key);
                list = new JList(listModel);
                group.setBacktrackingAtributes(list);
                Set<String> keys = getUmpstProject().getMapAtribute().keySet();
                TreeSet<String> sortedKeys = new TreeSet<String>(keys);
                for (String keyAux : sortedKeys) {
                    if (getUmpstProject().getMapAtribute().get(keyAux).getAtributeName().equals(key)) {
                        getUmpstProject().getMapAtribute().get(keyAux).getFowardTrackingGroups().add(group);
                    }
                }
                UmpstModule father = getFatherPanel();
                changePanel(father.getMenuPanel().getGroupsPane().getGroupsPanel().getGroupsAdd(group));
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scroll, c);
        panel.setPreferredSize(new Dimension(400, 200));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    public void createFrameRelationship() {
        JFrame frame = new JFrame("Adding Backtracking from relationship");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] columnNames = { "ID", "Relationship", "" };
        dataFrame = new Object[getUmpstProject().getMapRelationship().size()][3];
        Integer i = 0;
        Set<String> keys = getUmpstProject().getMapRelationship().keySet();
        TreeSet<String> sortedKeys = new TreeSet<String>(keys);
        for (String key : sortedKeys) {
            dataFrame[i][0] = getUmpstProject().getMapRelationship().get(key).getId();
            dataFrame[i][1] = getUmpstProject().getMapRelationship().get(key).getRelationshipName();
            dataFrame[i][2] = "";
            i++;
        }
        DefaultTableModel model = new DefaultTableModel(dataFrame, columnNames);
        JTable table = new JTable(model);
        TableButton buttonEdit = new TableButton(new TableButton.TableButtonCustomizer() {

            public void customize(JButton button, int row, int column) {
                button.setIcon(new ImageIcon("images/add.gif"));
            }
        });
        TableColumn buttonColumn1 = table.getColumnModel().getColumn(columnNames.length - 1);
        buttonColumn1.setMaxWidth(28);
        buttonColumn1.setCellRenderer(buttonEdit);
        buttonColumn1.setCellEditor(buttonEdit);
        buttonEdit.addHandler(new TableButton.TableButtonPressedHandler() {

            public void onButtonPress(int row, int column) {
                String key = dataFrame[row][1].toString();
                list = group.getBacktrackingRelationship();
                listModel.addElement(key);
                list = new JList(listModel);
                group.setBacktrackingRelationship(list);
                Set<String> keys = getUmpstProject().getMapRelationship().keySet();
                TreeSet<String> sortedKeys = new TreeSet<String>(keys);
                for (String keyAux : sortedKeys) {
                    if (getUmpstProject().getMapRelationship().get(keyAux).getRelationshipName().equals(key)) {
                        getUmpstProject().getMapRelationship().get(keyAux).getFowardtrackingGroups().add(group);
                    }
                }
                UmpstModule father = getFatherPanel();
                changePanel(father.getMenuPanel().getGroupsPane().getGroupsPanel().getGroupsAdd(group));
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scroll, c);
        panel.setPreferredSize(new Dimension(400, 200));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
