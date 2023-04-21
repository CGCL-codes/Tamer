package interface_layer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import date_layer.Coordinator;

public class panel_ingredient extends JPanel implements ActionListener {

    private Coordinator coordinator;

    private JTextField Description;

    private JLabel Image;

    private JLabel Information;

    private JLabel Ingredient;

    private JLabel Name;

    private JLabel Tipo;

    private JButton botonSearch;

    private JButton botonSave;

    private JTextField name;

    private JScrollPane scroll_area;

    private JTextArea show_result_area;

    private JComboBox supplier_combo;

    public panel_ingredient(Coordinator coordinator) {
        this.coordinator = coordinator;
        initComponents();
    }

    private void initComponents() {
        name = new JTextField();
        Description = new JTextField();
        botonSearch = new JButton();
        botonSave = new JButton();
        scroll_area = new JScrollPane();
        show_result_area = new JTextArea();
        Name = new JLabel();
        Information = new JLabel();
        Ingredient = new JLabel();
        Tipo = new JLabel();
        Image = new JLabel();
        supplier_combo = new JComboBox();
        setPreferredSize(new java.awt.Dimension(600, 380));
        setLayout(null);
        add(name);
        name.setBounds(20, 140, 190, 20);
        add(Description);
        Description.setBounds(20, 190, 190, 20);
        botonSearch.setText("Search");
        botonSearch.addActionListener(this);
        add(botonSearch);
        botonSearch.setBounds(20, 340, 75, 23);
        botonSave.setText("Save");
        botonSave.addActionListener(this);
        add(botonSave);
        botonSave.setBounds(140, 340, 70, 23);
        show_result_area.setColumns(20);
        show_result_area.setRows(5);
        scroll_area.setViewportView(show_result_area);
        add(scroll_area);
        scroll_area.setBounds(280, 20, 290, 320);
        Name.setText("Name");
        add(Name);
        Name.setBounds(20, 120, 90, 14);
        Information.setText("Information");
        add(Information);
        Information.setBounds(280, 350, 200, 14);
        Ingredient.setText("Description");
        add(Ingredient);
        Ingredient.setBounds(20, 170, 110, 14);
        Tipo.setText("Supplier");
        add(Tipo);
        Tipo.setBounds(20, 220, 100, 14);
        add(Image);
        Image.setBounds(20, 20, 250, 90);
        Image.setIcon(new ImageIcon("src\\images\\ingrediente.png"));
        supplier_combo.setModel(new DefaultComboBoxModel(new String[] { "A", "B", "C" }));
        supplier_combo.addActionListener(this);
        add(supplier_combo);
        supplier_combo.setBounds(20, 240, 190, 20);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonSave) {
            if (name.getText().equals("") || Description.getText().equals("")) {
                Information.setText("debe llenar todos los campos");
            } else {
                coordinator.manageIngredient(1, name.getText(), null, Description.getText());
            }
        } else {
            if (e.getSource() == botonSearch) {
                if (name.getText().equals("") && Description.getText().equals("")) {
                    Information.setText("debe llenar algun campo");
                } else {
                    coordinator.manageIngredient(2, name.getText(), null, Description.getText());
                }
            }
        }
    }
}
