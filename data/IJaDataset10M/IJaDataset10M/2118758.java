package rila.mygui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import rila.gui.ConnectionInfo;
import rila.mygui.Entity.EntityType;

/**
 * Panel that show the Tree of the db.Table and column
 * @author Utku
 */
public class JTreePanel extends JPanel {

    private Settingsparam params = new Settingsparam();

    private List<String> dimTableValue = new ArrayList<String>();

    private JPanel mainPanel;

    private InputPanel inpPanel;

    /**
     * Constructor
     * @param con
     * @param conInfo
     * @param panle
     */
    public JTreePanel(Connection con, ConnectionInfo conInfo, JPanel panle, JLabel percLabel) {
        Object hierarchy[] = null;
        this.mainPanel = panle;
        Border etchedBdr = BorderFactory.createEtchedBorder();
        Border titledBdr = BorderFactory.createTitledBorder(etchedBdr, "Settings");
        this.setBorder(titledBdr);
        try {
            java.util.List<Object> list = new ArrayList<Object>();
            String URL = conInfo.url;
            String dbName = URL.substring(URL.lastIndexOf("/") + 1, URL.length());
            list.add(dbName);
            DatabaseMetaData dbMetaData = con.getMetaData();
            String[] types = { "TABLE" };
            ResultSet resultSet = dbMetaData.getTables(null, null, "%", types);
            ResultSet temResultSet = dbMetaData.getTables(null, null, "%", types);
            temResultSet.last();
            int total = temResultSet.getRow();
            while (resultSet.next()) {
                int done = resultSet.getRow();
                List<Entity> tempList = new ArrayList<Entity>();
                String tableName = resultSet.getString(3);
                Entity table = new Entity();
                table.setValue(tableName);
                table.setType(EntityType.TABLE);
                tempList.add(table);
                ResultSet rsColumns = null;
                rsColumns = dbMetaData.getColumns(null, null, tableName, null);
                while (rsColumns.next()) {
                    String columnName = rsColumns.getString("COLUMN_NAME");
                    Entity column = new Entity();
                    column.setValue(columnName);
                    column.setType(EntityType.COLUMN);
                    tempList.add(column);
                }
                System.out.println((((double) done / total) * 100) + "% Completes");
                percLabel.setText((((double) done / total) * 100) + "% Completes");
                list.add(tempList.toArray());
            }
            hierarchy = list.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode root = processHierarchy(hierarchy);
        JTree tree = new JTree(root);
        this.add(new JScrollPane(tree), BorderLayout.EAST);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new BorderLayout());
        JButton nextButton = new JButton("next >");
        MouseAdapter ma = new MouseAdapter() {

            private void myPopupEvent(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree) e.getSource();
                TreePath path = tree.getPathForLocation(x, y);
                if (path == null) {
                    return;
                }
                tree.setSelectionPath(path);
                DefaultMutableTreeNode obj = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (obj.getParent() == null) {
                    return;
                }
                Entity entity = (Entity) obj.getUserObject();
                if (entity.getType().equals(EntityType.TABLE)) {
                    ActionListener actionListener = new PopupActionListener(entity);
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Select as hubtable");
                    item.addActionListener(actionListener);
                    popup.add(item);
                    popup.addSeparator();
                    JMenuItem item1 = new JMenuItem("Select as ClassTable");
                    item1.addActionListener(actionListener);
                    popup.add(item1);
                    popup.addSeparator();
                    JMenuItem item2 = new JMenuItem("Select as Dimension Table");
                    item2.addActionListener(actionListener);
                    popup.add(item2);
                    popup.addSeparator();
                    popup.show(tree, x, y);
                } else if (entity.getType().equals(EntityType.COLUMN)) {
                    ActionListener actionListener = new PopupActionListener(entity);
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Select as PrimaryKey");
                    item.addActionListener(actionListener);
                    popup.add(item);
                    popup.addSeparator();
                    JMenuItem item1 = new JMenuItem("Select as Target Attribute");
                    item1.addActionListener(actionListener);
                    popup.add(item1);
                    popup.addSeparator();
                    popup.show(tree, x, y);
                }
            }

            class PopupActionListener implements ActionListener {

                private Entity entity;

                public PopupActionListener(Entity ent) {
                    this.entity = ent;
                }

                public void actionPerformed(ActionEvent actionEvent) {
                    String action = actionEvent.getActionCommand();
                    if (action.equals("Select as Target Attribute")) {
                        params.setTargetAttribute(this.entity.getValue());
                        inpPanel.setValues("ATTR", this.entity.getValue());
                    } else if (action.equals("Select as hubtable")) {
                        params.setHubTable(this.entity.getValue());
                        inpPanel.setValues("HUB", this.entity.getValue());
                    } else if (action.equals("Select as ClassTable")) {
                        params.setClassTable(this.entity.getValue());
                        inpPanel.setValues("CLASS", this.entity.getValue());
                    } else if (action.equals("Select as PrimaryKey")) {
                        params.setPrimaryKey(this.entity.getValue());
                        inpPanel.setValues("KEY", this.entity.getValue());
                    } else if (action.equals("Select as Dimension Table")) {
                        if (!dimTableValue.contains(this.entity.getValue())) {
                            dimTableValue.add(this.entity.getValue());
                            inpPanel.setValues("DIM", this.entity.getValue());
                        } else {
                            JOptionPane.showMessageDialog(null, "You already add this table as dimension table.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    myPopupEvent(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    myPopupEvent(e);
                }
            }
        };
        tree.addMouseListener(ma);
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (params.getHubTable() == null) {
                    JOptionPane.showMessageDialog(null, "Please select the HubTable", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (params.getClassTable() == null) {
                    JOptionPane.showMessageDialog(null, "Please select the Class Table", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (params.getPrimaryKey() == null) {
                    JOptionPane.showMessageDialog(null, "Please select the Primary Key", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (params.getTargetAttribute() == null) {
                    JOptionPane.showMessageDialog(null, "Please select the Target Attribute", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (dimTableValue.size() <= 0) {
                    JOptionPane.showMessageDialog(null, "Please select the dimTable", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
    }

    /**
     * method to add the nodes to the tree
     * @param hierarchy
     * @return
     */
    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[]) {
                child = processHierarchy((Object[]) nodeSpecifier);
            } else {
                child = new DefaultMutableTreeNode(nodeSpecifier);
            }
            node.add(child);
        }
        return (node);
    }

    public Settingsparam getParams() {
        return params;
    }

    public void setParams(Settingsparam params) {
        this.params = params;
    }

    public List<String> getDimTableValue() {
        return dimTableValue;
    }

    public void setDimTableValue(List<String> dimTableValue) {
        this.dimTableValue = dimTableValue;
    }

    public void setInputPanel(InputPanel pan) {
        this.inpPanel = pan;
    }

    public InputPanel getInputpanel() {
        return this.inpPanel;
    }
}
