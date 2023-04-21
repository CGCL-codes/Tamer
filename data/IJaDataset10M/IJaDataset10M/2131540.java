package unbbayes.gui.mebn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import unbbayes.controller.IconController;
import unbbayes.controller.MEBNController;
import unbbayes.gui.mebn.auxiliary.FocusListenerTextField;
import unbbayes.gui.mebn.auxiliary.ListCellRenderer;
import unbbayes.gui.mebn.auxiliary.ToolKitForGuiMebn;
import unbbayes.prs.mebn.entity.Entity;
import unbbayes.prs.mebn.entity.ObjectEntity;
import unbbayes.prs.mebn.entity.exception.ObjectEntityHasInstancesException;
import unbbayes.prs.mebn.entity.exception.TypeException;

/**
 * Pane for edition of object entities: 
 *       - Create, 
 *       - Delete
 *       - Edit 
 *       - View
 *  Atributes editables: 
 *       - Name
 *       - isOrdenable property      
 *       
 *  @author Laécio Lima dos Santos (laecio@gmail.com)     
 */
public class EntityEditionPane extends JPanel {

    private MEBNController mebnController;

    private List<ObjectEntity> listEntity;

    private JPanel jpInformation;

    private JTextField txtName;

    private JTextField txtType;

    private JCheckBox checkIsOrdereable;

    private JButton jbNew;

    private JButton jbDelete;

    private JList jlEntities;

    private DefaultListModel listModel;

    private ObjectEntity selected;

    private final Pattern wordPattern = Pattern.compile("[a-zA-Z_0-9]*");

    private Matcher matcher;

    private final IconController iconController = IconController.getInstance();

    /** Load resource file from this package */
    private static ResourceBundle resource = ResourceBundle.getBundle("unbbayes.gui.resources.GuiResources");

    /**
     * 
     * @param mebnController Controller for objects of this pane
     */
    public EntityEditionPane(MEBNController mebnController) {
        super();
        this.mebnController = mebnController;
        this.setBorder(ToolKitForGuiMebn.getBorderForTabPanel(resource.getString("EntityTitle")));
        setLayout(new BorderLayout());
        buildJlEntities();
        JScrollPane listScrollPane = new JScrollPane(jlEntities);
        buildJpInformation();
        this.add(BorderLayout.SOUTH, jpInformation);
        this.add(BorderLayout.CENTER, listScrollPane);
        selected = null;
        update();
        addListListener();
        addButtonsListeners();
    }

    private void buildJlEntities() {
        listModel = new DefaultListModel();
        jlEntities = new JList(listModel);
        jlEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlEntities.setLayoutOrientation(JList.VERTICAL);
        jlEntities.setVisibleRowCount(-1);
        jlEntities.setCellRenderer(new ListCellRenderer(iconController.getObjectEntityIcon()));
    }

    private void buildJpInformation() {
        jpInformation = new JPanel(new GridLayout(4, 0));
        JLabel label;
        JToolBar toolBar;
        toolBar = new JToolBar();
        toolBar.setLayout(new GridLayout(0, 2));
        jbNew = new JButton(iconController.getMoreIcon());
        jbNew.setToolTipText(resource.getString("newEntityToolTip"));
        toolBar.add(jbNew);
        jbDelete = new JButton(iconController.getLessIcon());
        jbDelete.setToolTipText(resource.getString("delEntityToolTip"));
        toolBar.add(jbDelete);
        toolBar.setFloatable(false);
        jpInformation.add(toolBar);
        toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());
        label = new JLabel(resource.getString("nameLabel"));
        label.setPreferredSize(new Dimension(50, 5));
        toolBar.add(label, BorderLayout.LINE_START);
        txtName = new JTextField(10);
        txtName.setEditable(false);
        toolBar.add(txtName, BorderLayout.CENTER);
        toolBar.setFloatable(false);
        jpInformation.add(toolBar);
        toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());
        label = new JLabel(resource.getString("typeLabel"));
        label.setPreferredSize(new Dimension(50, 5));
        toolBar.add(label, BorderLayout.LINE_START);
        txtType = new JTextField(10);
        txtType.setEditable(false);
        toolBar.add(txtType, BorderLayout.CENTER);
        toolBar.setFloatable(false);
        jpInformation.add(toolBar);
        toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());
        checkIsOrdereable = new JCheckBox();
        checkIsOrdereable.setEnabled(false);
        toolBar.add(checkIsOrdereable, BorderLayout.LINE_START);
        label = new JLabel(resource.getString("ordereableLabel"));
        toolBar.add(label, BorderLayout.CENTER);
        toolBar.setFloatable(false);
        jpInformation.add(toolBar);
    }

    /**
	 *  update the list of entities 
	 **/
    private void update() {
        ObjectEntity antSelected = selected;
        listModel.clear();
        listEntity = mebnController.getMultiEntityBayesianNetwork().getObjectEntityContainer().getListEntity();
        listModel = new DefaultListModel();
        for (Entity entity : listEntity) {
            listModel.addElement(entity);
        }
        jlEntities.setModel(listModel);
        selected = antSelected;
    }

    private void addListListener() {
        jlEntities.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                selected = (ObjectEntity) jlEntities.getSelectedValue();
                if (selected != null) {
                    txtName.setText(selected.getName());
                    txtName.setEditable(true);
                    checkIsOrdereable.setEnabled(true);
                    checkIsOrdereable.setSelected(selected.isOrdereable());
                    txtType.setText(selected.getType().getName());
                }
            }
        });
    }

    private void addButtonsListeners() {
        txtName.addFocusListener(new FocusListenerTextField());
        txtName.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (txtName.getText().length() > 0)) {
                    try {
                        String nameValue = txtName.getText(0, txtName.getText().length());
                        matcher = wordPattern.matcher(nameValue);
                        if (matcher.matches()) {
                            try {
                                mebnController.renameObjectEntity(selected, nameValue);
                                jlEntities.setSelectedValue(selected, true);
                                txtName.setText(selected.getName());
                                txtName.setEditable(false);
                                checkIsOrdereable.setEnabled(false);
                                txtType.setText(selected.getType().getName());
                                update();
                            } catch (TypeException typeException) {
                                JOptionPane.showMessageDialog(null, resource.getString("nameDuplicated"), resource.getString("nameException"), JOptionPane.ERROR_MESSAGE);
                                txtName.selectAll();
                            }
                        } else {
                            txtName.setBackground(ToolKitForGuiMebn.getColorTextFieldError());
                            txtName.setForeground(Color.WHITE);
                            JOptionPane.showMessageDialog(null, resource.getString("nameError"), resource.getString("nameException"), JOptionPane.ERROR_MESSAGE);
                            txtName.selectAll();
                        }
                    } catch (javax.swing.text.BadLocationException ble) {
                        System.out.println(ble.getMessage());
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                try {
                    String name = txtName.getText(0, txtName.getText().length());
                    matcher = wordPattern.matcher(name);
                    if (!matcher.matches()) {
                        txtName.setBackground(ToolKitForGuiMebn.getColorTextFieldError());
                        txtName.setForeground(Color.WHITE);
                    } else {
                        txtName.setBackground(ToolKitForGuiMebn.getColorTextFieldSelected());
                        txtName.setForeground(Color.BLACK);
                    }
                } catch (Exception efd) {
                }
            }
        });
        checkIsOrdereable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                try {
                    mebnController.setIsOrdereableObjectEntityProperty(selected, checkBox.isSelected());
                } catch (ObjectEntityHasInstancesException e1) {
                    JOptionPane.showMessageDialog(null, resource.getString("objectEntityHasInstance"), resource.getString("operationFail"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jbNew.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    selected = mebnController.createObjectEntity();
                    update();
                    jlEntities.setSelectedValue(selected, true);
                    txtType.setText(selected.getType().getName());
                    txtName.setEditable(true);
                    checkIsOrdereable.setEnabled(true);
                    txtName.setText(selected.getName());
                    txtName.selectAll();
                    txtName.requestFocus();
                } catch (TypeException e) {
                    JOptionPane.showMessageDialog(null, resource.getString("nameDuplicated"), resource.getString("nameException"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jbDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (selected != null) {
                    try {
                        mebnController.removeObjectEntity(selected);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    update();
                    txtName.setText(" ");
                    txtType.setText(" ");
                    txtName.setEditable(false);
                    checkIsOrdereable.setEnabled(false);
                }
            }
        });
    }
}
