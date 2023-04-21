package com.c4j.workbench.dialog;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.c4j.composition.IComposition;
import com.c4j.workbench.IconSet;
import com.c4j.workbench.WorkbenchIface;
import com.c4j.workspace.IContainer;
import com.c4j.workspace.IFolder;
import com.c4j.workspace.IWorkspace;

@SuppressWarnings("serial")
public class CompositionDialog extends javax.swing.JDialog {

    private static final int DIALOG_WIDTH = 500;

    private static final int TYPE_HEIGTH = 24;

    private final WorkbenchIface.Neighborhood neighborhood;

    private final IComposition composition;

    private final Map<String, IContainer> containerMap;

    private DialogHeadPanel headPanel;

    private DialogButtonPanel buttonPanel;

    private JPanel panel;

    private JSeparator separator1;

    private JLabel folderLabel;

    private JLabel nameLabel;

    private JComboBox folderComboBox;

    private NameTextField nameTextField;

    private JLabel typeLabel;

    private TypeTextField typeTextField;

    /**
     * Method to display this JDialog.
     *
     * @param args
     *         The argument list.
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final JFrame jFrame = new JFrame();
                final CompositionDialog inst = new CompositionDialog(jFrame, null, null, null);
                inst.setVisible(true);
            }
        });
    }

    public CompositionDialog(final JFrame frame, final WorkbenchIface.Neighborhood neighborhood, final IWorkspace workspace, final IContainer container) {
        super(frame);
        this.neighborhood = neighborhood;
        setTitle("New Composition");
        this.composition = null;
        containerMap = new HashMap<String, IContainer>();
        containerMap.put("[ROOT]", workspace);
        initModel(workspace);
        initGUI();
        if (container != null) {
            nameTextField.setVerifier(container.getContainerElementNameVerifier());
            nameTextField.setCurrentName(container.getContainerElementNameVerifier().getUnusedName("NewComposition"));
            folderComboBox.setSelectedItem(container instanceof IFolder ? ((IFolder) container).getIdentifier() : "[ROOT]");
            nameTextField.selectAll();
        }
        typeTextField.check();
        headPanel.setTitel("New composition");
        headPanel.setDescription("Create a new composition.");
    }

    public CompositionDialog(final JFrame frame, final WorkbenchIface.Neighborhood neighborhood, final IComposition composition) {
        super(frame);
        this.neighborhood = neighborhood;
        setTitle("Edit Composition Properties");
        this.composition = composition;
        final IWorkspace workspace = composition.getWorkspace();
        final IContainer container = composition.getContainer();
        containerMap = new HashMap<String, IContainer>();
        initModel(workspace);
        initGUI();
        nameTextField.setVerifier(composition.getNameVerifier());
        nameTextField.setCurrentName(composition.getName());
        folderComboBox.setSelectedItem(container instanceof IFolder ? ((IFolder) container).getIdentifier() : "[ROOT]");
        headPanel.setTitel("Composition Properties");
        headPanel.setDescription("Edit properties of the composition.");
        folderComboBox.setEnabled(false);
        typeTextField.setText(composition.getType().getFullTypeString());
        nameTextField.setText(composition.getName());
    }

    private void initModel(final IWorkspace workspace) {
        containerMap.put("[ROOT]", workspace);
        if (workspace != null) for (final IFolder folder : workspace.getAllFolders()) containerMap.put(folder.getIdentifier(), folder);
    }

    private void initPanel() {
        panel = new JPanel();
        final GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        folderLabel = new JLabel("Folder:");
        final ComboBoxModel folderComboBoxModel = new DefaultComboBoxModel(new TreeSet<String>(containerMap.keySet()).toArray());
        folderComboBox = new JComboBox();
        folderComboBox.setModel(folderComboBoxModel);
        nameLabel = new JLabel("Name:");
        nameTextField = new NameTextField();
        nameTextField.setToolTipText("Enter the name of the composition.");
        nameTextField.addListener(headPanel);
        typeLabel = new JLabel("Type:");
        typeTextField = new TypeTextField(neighborhood == null ? null : neighborhood.use_type());
        typeTextField.setToolTipText("Enter the name of the composition java type.");
        typeTextField.addListener(headPanel);
        panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup().addContainerGap().addGroup(panelLayout.createParallelGroup().addComponent(folderLabel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addComponent(nameLabel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addComponent(typeLabel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE)).addGap(5).addGroup(panelLayout.createParallelGroup().addComponent(folderComboBox, 0, 200, MAX_VALUE).addComponent(nameTextField, 0, 200, MAX_VALUE).addComponent(typeTextField, 0, 200, MAX_VALUE)).addContainerGap());
        panelLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] { nameLabel, folderLabel });
        panelLayout.setVerticalGroup(panelLayout.createSequentialGroup().addContainerGap().addGroup(panelLayout.createParallelGroup(BASELINE).addComponent(folderLabel, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE).addComponent(folderComboBox, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE)).addGap(5).addGroup(panelLayout.createParallelGroup(BASELINE).addComponent(nameLabel, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE).addComponent(nameTextField, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE)).addGap(15).addGroup(panelLayout.createParallelGroup(BASELINE).addComponent(typeLabel, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE).addComponent(typeTextField, PREFERRED_SIZE, TYPE_HEIGTH, PREFERRED_SIZE)).addContainerGap());
    }

    private void initGUI() {
        try {
            final GroupLayout thisLayout = new GroupLayout(getContentPane());
            getContentPane().setLayout(thisLayout);
            headPanel = new DialogHeadPanel();
            try {
                headPanel.setDialogIcon(IconSet.C4J_LOGO_SMALL.getIcon());
            } catch (final Throwable e) {
                System.err.println("Could not find small logo.");
            }
            initPanel();
            separator1 = new JSeparator();
            buttonPanel = new DialogButtonPanel();
            buttonPanel.setCheckedComponents(nameTextField, typeTextField);
            nameTextField.addListener(buttonPanel);
            typeTextField.addListener(buttonPanel);
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addComponent(headPanel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addComponent(panel, PREFERRED_SIZE, PREFERRED_SIZE, MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(separator1, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonPanel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createParallelGroup().addComponent(headPanel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(panel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(separator1, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(buttonPanel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE));
            pack();
            nameTextField.requestFocusInWindow();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        buttonPanel.setOKAction(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                final IContainer container = containerMap.get(folderComboBox.getSelectedItem());
                if (composition == null) {
                    container.createComposition(nameTextField.getText(), neighborhood.use_type().parseType(typeTextField.getText()));
                } else {
                    composition.setName(nameTextField.getText());
                    composition.setType(neighborhood.use_type().parseType(typeTextField.getText()));
                }
                CompositionDialog.this.dispose();
            }
        });
        buttonPanel.setCancelAction(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                CompositionDialog.this.dispose();
            }
        });
        folderComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final IContainer container = containerMap.get(folderComboBox.getSelectedItem());
                nameTextField.setVerifier(container.getContainerElementNameVerifier());
            }
        });
    }
}
