package com.zara.store.client.view.swing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.zara.store.client.controller.OfadController;
import com.zara.store.client.view.swing.table.OfadTableModel;
import com.zara.store.main.Main;
import framework.view.View;
import framework.view.swing.ViewJFrame;

public class OfadForm extends ViewJFrame {

    private static final long serialVersionUID = 1L;

    private final JPanel dummy;

    private final JLabel labelOfad;

    private final JScrollPane scrollOfad;

    private final JScrollPane scrollOfadNew;

    private final JTable tableOfad;

    private final JTable tableOfadNew;

    private final JTextField textOfadFile;

    private final JButton buttonOfad;

    private final JLabel labelOfadStatus;

    private final JLabel labelOfadFile;

    private final OfadTableModel ofadTableModel;

    private final OfadTableModel ofadNewTableModel;

    private final JFileChooser fcOfad;

    private final Container container;

    private final JTabbedPane tabOfad;

    public OfadForm(View view) {
        super(view);
        ofadTableModel = new OfadTableModel();
        ofadNewTableModel = new OfadTableModel();
        dummy = new JPanel(null);
        fcOfad = new JFileChooser();
        labelOfad = new JLabel();
        labelOfadStatus = new JLabel();
        scrollOfad = new JScrollPane();
        scrollOfadNew = new JScrollPane();
        tableOfad = new JTable(ofadTableModel);
        tableOfadNew = new JTable(ofadNewTableModel);
        textOfadFile = new JTextField();
        buttonOfad = new JButton();
        container = getContentPane();
        labelOfadFile = new JLabel();
        tabOfad = new JTabbedPane();
        initComponents();
    }

    private void initComponents() {
        setIconImage(Main.createImageIcon("/com/zara/store/client/resources/ofad.gif").getImage());
        setTitle("Oferta de Art�culos Disponibles - Zara Tienda");
        scrollOfad.setViewportView(tableOfad);
        scrollOfadNew.setViewportView(tableOfadNew);
        labelOfadStatus.setText(" ");
        labelOfad.setText("<html>Detalle para �Oferta de Art�culos Disponibles�</html>");
        labelOfadFile.setText(" ");
        textOfadFile.setText(" ");
        textOfadFile.setEditable(false);
        buttonOfad.setText("<html>Seleccionar XML</html>");
        tabOfad.addTab("Todos", scrollOfad);
        tabOfad.addTab("Nuevos", scrollOfadNew);
        fcOfad.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fcOfad.setFileFilter(new StoreFileFilter("Oferta de Art�culos Disponibles (.xml)"));
        addListeners();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        container.add(dummy, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        container.add(labelOfad, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        container.add(tabOfad, constraints);
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        container.add(buttonOfad, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 3;
        container.add(textOfadFile, constraints);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 4;
        container.add(labelOfadStatus, constraints);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void addListeners() {
        buttonOfad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ofadTableModel.clear();
                int returnVal = fcOfad.showOpenDialog(container);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fcOfad.getSelectedFile();
                    textOfadFile.setText(file.getAbsolutePath());
                    try {
                        ((OfadController) getView().getController()).processOfad(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        labelOfadStatus.setText("Error al procesar xml.");
                    }
                }
            }
        });
    }

    public OfadTableModel getOfadTableModel() {
        return ofadTableModel;
    }

    public OfadTableModel getOfadNewTableModel() {
        return ofadNewTableModel;
    }
}
