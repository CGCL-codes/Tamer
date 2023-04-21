package gui;

import dataStructure.Person;
import dataStructure.PersonList;
import dataStructure.database.DbQuery;
import gui.mainScreen.Calendar.MainScreenCalendar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the GUI where we add new members of the family.
 * This class has a data structure part at the bottom that
 * sends a string to DbQuery with a INSERT INTO Person query
 * and also fetches an array holding the names of the Person
 * objects fetched from the database.
 * @author scav
 *
 * TODO: Move datastructure out of the GUI class.
 *
 */
public class AddData extends JFrame {

    private JFrame frame;

    private JPanel pnlMain;

    private JLabel lblName, lblData, lblDate, lblTime, lblDetails;

    private JComboBox cmbPerson;

    private JTextField txtData, txtDate, txtTime;

    private JTextArea txtDetails;

    private JScrollPane spDetails;

    private JButton btnAddData, btnCancel;

    private JSpinner dataDate;

    private static final String windowName = "Create new data";

    private Dimension windowDimension = new Dimension(400, 320);

    private static final long serialVersionUID = 1L;

    private DbQuery db;

    private PersonList pl;

    private Person p;

    private String name;

    private String story;

    private String details;

    private String dataDateString;

    private String date;

    private String time;

    private String[] names;

    private String selectedName;

    private Events fb;

    private MainScreenCalendar ms;

    private GridBagLayout layout = new GridBagLayout();

    public AddData(Events fb) {
        this.fb = fb;
        initComponents();
        createComponents();
        createTooltips();
    }

    private void initComponents() {
        pnlMain = new JPanel();
        pnlMain.setLayout(layout);
        createDetailsField();
        createDateSpinner();
        populateComboBox();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        lblName = new JLabel();
        layout.setConstraints(lblName, c);
        c.gridx = 1;
        c.gridy = 0;
        txtTime = new JTextField();
        txtTime.setText("HH:MM:SS");
        layout.setConstraints(txtTime, c);
        c.gridx = 1;
        c.gridy = 1;
        txtDate = new JTextField();
        txtDate.setText("YYYY-MM-DD");
        layout.setConstraints(txtDate, c);
        c.gridx = 0;
        c.gridy = 1;
        cmbPerson = new JComboBox(names);
        layout.setConstraints(cmbPerson, c);
        c.gridx = 0;
        c.gridy = 2;
        lblData = new JLabel();
        layout.setConstraints(lblData, c);
        c.gridx = 1;
        c.gridy = 2;
        txtData = new JTextField();
        layout.setConstraints(txtData, c);
        c.gridx = 0;
        c.gridy = 3;
        lblDetails = new JLabel();
        layout.setConstraints(lblDetails, c);
        c.gridx = 1;
        c.gridy = 3;
        c.ipady = 120;
        layout.setConstraints(spDetails, c);
        c.gridx = 0;
        c.gridy = 4;
        c.ipady = 0;
        c.ipadx = 0;
        btnAddData = new JButton();
        layout.setConstraints(btnAddData, c);
        c.gridx = 1;
        c.gridy = 4;
        btnCancel = new JButton();
        layout.setConstraints(btnCancel, c);
    }

    private void createComponents() {
        lblName.setText("Person ");
        cmbPerson.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getPersonName();
            }
        });
        lblData.setText("Data ");
        lblDetails.setText("Details");
        btnAddData.setText("Add");
        btnAddData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                isFormCorrect();
                fb.fb.refreshCalendar();
                refreshCalendar();
            }
        });
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        pnlMain.add(lblName);
        pnlMain.add(cmbPerson);
        pnlMain.add(lblData);
        pnlMain.add(txtData);
        pnlMain.add(lblDetails);
        pnlMain.add(spDetails);
        pnlMain.add(btnAddData);
        pnlMain.add(btnCancel);
        pnlMain.add(txtTime);
        pnlMain.add(txtDate);
    }

    private void createDateSpinner() {
    }

    private void createDetailsField() {
        txtDetails = new JTextArea();
        spDetails = new JScrollPane(txtDetails);
        spDetails.setBounds(3, 3, 300, 200);
        txtDetails.setLineWrap(rootPaneCheckingEnabled);
        txtDetails.setWrapStyleWord(true);
        txtDetails.invalidate();
    }

    private void isFormCorrect() {
        if (txtData.getText().equals("")) {
            txtData.setBackground(Color.red);
            txtData.setText("Please fill in this field.");
        } else {
            addDataQuery();
            frame.dispose();
        }
    }

    /**
         * This method sets up the tooltips that will be used on different
         * components throughtout the JFrame.
         */
    private void createTooltips() {
        String cmbPersonString = "<html>Select the person whom you want<br /> to create the data for</html>";
        String txtDataString = "<html>Set the ingress text for the data here.</html>";
        String txtDetailsString = "<html>Sets a detailed description for the data<br />" + "which later can be viewed by clicking on the<br />" + "given data in the calender main view.</html>";
        cmbPerson.setToolTipText(cmbPersonString);
        txtData.setToolTipText(txtDataString);
        txtDetails.setToolTipText(txtDetailsString);
    }

    public void createAndShowGUI() {
        frame = new JFrame();
        ;
        frame.setContentPane(pnlMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(windowDimension);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setName(windowName);
        frame.setTitle(windowName);
    }

    private void refreshCalendar() {
        ms.validateCalendar();
        System.out.println("Revalidated");
    }

    /**
         * This method fetches the correct Person object from the
         * combobox so that it can be used for further queries.
         */
    private void getPersonName() {
        this.selectedName = names[cmbPerson.getSelectedIndex()];
    }

    /**
	 * This is where we send the information from the
	 * JTextField's over to the DbQuery class to add data.
         *
	 */
    private void addDataQuery() {
        this.time = txtTime.getText();
        this.date = txtDate.getText();
        this.story = txtData.getText();
        this.details = txtDetails.getText();
        db = new DbQuery();
        db.addData(selectedName, date, time, story, details);
    }

    /**
         * This long and messy method gets the names from the
         * DbQuery pName arrayList and moves it into an array
         * so that it can be displayed inside the JComboBox.
         */
    private void populateComboBox() {
        db = new DbQuery();
        db.getPerson();
        db.getName();
        this.names = new String[db.pName.size() + 1];
        this.names[0] = "Select person...";
        int j = 0;
        for (int i = 1; i < db.pName.size() + 1; i++) {
            this.names[i] = db.pName.get(j);
            j++;
        }
    }
}
