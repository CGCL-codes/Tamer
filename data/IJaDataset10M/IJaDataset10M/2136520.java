package managers;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditImage {

    JTextField term1;

    JTextField term2;

    JTextField term3;

    JComboBox option1;

    JComboBox option2;

    JButton searchButton;

    JTextField searchName;

    JButton searchNameButton;

    JEditorPane imageGallery;

    JButton editButton;

    JButton deleteButton;

    JButton cancelButton;

    public EditImage() {
        launch();
    }

    public void launch() {
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(650, 400);
        mainFrame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainFrame.add(mainPanel);
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Por t�rminos clave"));
        searchPanel.setLayout(new FlowLayout());
        term1 = new JTextField(10);
        term2 = new JTextField(10);
        term3 = new JTextField(10);
        option1 = new JComboBox();
        option1.addItem("AND");
        option1.addItem("OR");
        option2 = new JComboBox();
        option2.addItem("AND");
        option2.addItem("OR");
        searchButton = new JButton("Buscar");
        searchPanel.add(term1);
        searchPanel.add(option1);
        searchPanel.add(term2);
        searchPanel.add(option2);
        searchPanel.add(term3);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel);
        JPanel searchNamePanel = new JPanel();
        searchNamePanel.setBorder(BorderFactory.createTitledBorder("Por nombre de im�gen"));
        searchNamePanel.setLayout(new FlowLayout());
        JLabel s = new JLabel("Buscar:", JLabel.TRAILING);
        searchName = new JTextField(30);
        s.setLabelFor(searchName);
        searchNameButton = new JButton("Buscar");
        searchNamePanel.add(s);
        searchNamePanel.add(searchName);
        searchNamePanel.add(searchNameButton);
        mainPanel.add(searchNamePanel);
        JPanel imageGalleryPanel = new JPanel();
        imageGalleryPanel.setBorder(BorderFactory.createTitledBorder("Im�genes encontradas"));
        imageGallery = new JEditorPane();
        imageGallery.setEditable(false);
        JScrollPane editorScrollPane = new JScrollPane(imageGallery);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        mainPanel.add(editorScrollPane);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        editButton = new JButton("Modificar im�gen");
        deleteButton = new JButton("Eliminar im�gen");
        cancelButton = new JButton("Cancelar");
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);
        mainFrame.setVisible(true);
        mainFrame.pack();
    }
}
