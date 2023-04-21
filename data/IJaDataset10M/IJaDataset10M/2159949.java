package org.webdocwf.util.loader.graphicaleditor.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.webdocwf.util.loader.graphicaleditor.model.Database;
import org.webdocwf.util.loader.graphicaleditor.util.DBVendors;
import org.webdocwf.util.loader.graphicaleditor.util.JarClassLoader;
import org.webdocwf.util.loader.graphicaleditor.util.JarFileFilter;
import org.webdocwf.util.loader.graphicaleditor.util.Project;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ProjectFormsPanel extends JPanel {

    private static final long serialVersionUID = 8153398594909994190L;

    /**
     * Register with this propterty name, if you want to be notified when
     * validation state changes;
     */
    public static final String VALIDATION_PROP = "validation";

    private JPanel targetPanel;

    private JButton browseButton;

    private JTextField locationField;

    private JTextField nameField;

    private JLabel locationLabel;

    private JLabel nameLabel;

    private JLabel sourcePassLabel;

    private JPasswordField sourcePassField;

    private JTextField sourceUserField;

    private JLabel sourceUserLabel;

    private JTextField sourceUrlSuffixField;

    private JLabel targetUserLabel;

    private JLabel sourceUrlLabel;

    private JComboBox sourceVendorCombo;

    private JLabel targetLibLabel;

    private JTextField targetUrlField;

    private JButton addJarBtn;

    private JButton removeJarBtn;

    private JPanel libBtnPanel;

    private JList libList;

    private JPanel libPanel;

    private JLabel targetUrlPrefixLabel;

    private JTextField targetClassNameField;

    private JLabel targetClassPathLabel;

    private JTextField sourceUrlField;

    private JTextField sourceClassNameField;

    private JComboBox targetDriverCombo;

    private JComboBox sourceDriverCombo;

    private JLabel targetDriverLabel;

    private JLabel sourceUrlPrefixLabel;

    private JLabel sourceClassPathLabel;

    private JLabel targetValidLabel;

    private JLabel sourceValidLabel;

    private JButton targetConnectButton;

    private JButton sourceConnectButton;

    private JLabel sourceDatabaseLabel;

    private JPanel sourcePanel;

    private JPasswordField targetPassField;

    private JLabel targetPassLabel;

    private JTextField targetUserField;

    private JTextField targetUrlSuffixField;

    private JLabel sourceLibLabel;

    private JLabel targetUrlLabel;

    private JComboBox targetVendorCombo;

    private JLabel targetDatabaseLabel;

    private JPanel nameLocationPanel;

    private Component owner = null;

    private String projectName;

    private String projectLocation;

    private String sourceUrl;

    private String targetUrl;

    private boolean sourceIsValid;

    private boolean targetIsValid;

    private Properties targetProps;

    private Properties sourceProps;

    private DBVendors vendors = null;

    private transient PropertyChangeSupport observeable = new PropertyChangeSupport(this);

    private boolean isValid = false;

    public ProjectFormsPanel(Component owner) {
        this(owner, null);
    }

    public ProjectFormsPanel(Component owner, Project project) {
        vendors = new DBVendors();
        this.owner = owner;
        this.initialize();
        readFromProject(project);
    }

    private void readFromProject(Project project) {
        if (project == null) {
            setProjectLocation(new File(System.getProperty("user.dir")).getAbsolutePath());
            setProjectName(nameField.getText());
            return;
        }
        setProjectLocation(project.getLocation());
        setProjectName(project.getName());
    }

    private void initialize() {
        BoxLayout jPanel4Layout = new BoxLayout(this, javax.swing.BoxLayout.Y_AXIS);
        this.setLayout(jPanel4Layout);
        {
            nameLocationPanel = new JPanel();
            this.add(nameLocationPanel);
            FormLayout jPanel2Layout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), 119dlu, 5dlu, 44dlu", "max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
            nameLocationPanel.setLayout(jPanel2Layout);
            nameLocationPanel.setBorder(BorderFactory.createTitledBorder("Name and Location"));
            {
                nameLabel = new JLabel();
                nameLocationPanel.add(nameLabel, new CellConstraints("2, 2, 1, 1, default, default"));
                nameLabel.setText("Project Name");
            }
            {
                locationLabel = new JLabel();
                nameLocationPanel.add(locationLabel, new CellConstraints("2, 3, 1, 1, default, default"));
                locationLabel.setText("Project Location");
            }
            {
                nameField = new JTextField("NewProject");
                nameField.addCaretListener(new CaretListener() {

                    public void caretUpdate(CaretEvent e) {
                        setProjectName(nameField.getText());
                    }
                });
                nameLocationPanel.add(nameField, new CellConstraints("4, 2, 1, 1, default, default"));
            }
            {
                locationField = new JTextField();
                locationField.setEditable(false);
                nameLocationPanel.add(locationField, new CellConstraints("4, 3, 1, 1, default, default"));
            }
            {
                browseButton = new JButton();
                nameLocationPanel.add(browseButton, new CellConstraints("6, 3, 1, 1, default, default"));
                browseButton.setText("browse");
                browseButton.addActionListener(new BrowseButtonActionListener());
            }
        }
        {
            sourcePanel = new JPanel();
            this.add(sourcePanel);
            FormLayout jPanel7Layout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), 135dlu, 5dlu, max(p;15dlu)", "max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), 5dlu, max(p;5dlu), max(p;15dlu), max(p;15dlu)");
            sourcePanel.setBorder(BorderFactory.createTitledBorder("Source Database"));
            sourcePanel.setLayout(jPanel7Layout);
            {
                sourceDatabaseLabel = new JLabel();
                sourcePanel.add(sourceDatabaseLabel, new CellConstraints("2, 1, 1, 1, default, default"));
                sourceDatabaseLabel.setText("Database");
            }
            {
                ComboBoxModel vendorModel = new DefaultComboBoxModel(vendors.vendorNameToArray());
                sourceVendorCombo = new JComboBox();
                sourceVendorCombo.addActionListener(new SourceDatabaseComboActionListener());
                sourcePanel.add(sourceVendorCombo, new CellConstraints("4, 1, 1, 1, default, default"));
                sourceVendorCombo.setModel(vendorModel);
            }
            {
                sourceUrlLabel = new JLabel();
                sourcePanel.add(sourceUrlLabel, new CellConstraints("2, 6, 1, 1, default, default"));
                sourceUrlLabel.setText("URL Suffix");
            }
            {
                sourceUrlSuffixField = new JTextField("localhost/loaderinput");
                sourceUrlSuffixField.getDocument().addDocumentListener(new SourceDocumentListener());
                sourceUrlSuffixField.addCaretListener(new CaretListener() {

                    public void caretUpdate(CaretEvent e) {
                        updateSourceUrlField();
                    }
                });
                sourcePanel.add(sourceUrlSuffixField, new CellConstraints("4, 6, 1, 1, default, default"));
            }
            {
                sourceUserLabel = new JLabel();
                sourcePanel.add(sourceUserLabel, new CellConstraints("2, 7, 1, 1, default, default"));
                sourceUserLabel.setText("Username");
            }
            {
                sourceUserField = new JTextField("root");
                sourceUserField.getDocument().addDocumentListener(new SourceDocumentListener());
                sourcePanel.add(sourceUserField, new CellConstraints("4, 7, 1, 1, default, default"));
            }
            {
                sourcePassLabel = new JLabel();
                sourcePanel.add(sourcePassLabel, new CellConstraints("2, 8, 1, 1, default, default"));
                sourcePassLabel.setText("Password");
            }
            {
                sourcePassField = new JPasswordField();
                sourcePassField.getDocument().addDocumentListener(new SourceDocumentListener());
                sourcePanel.add(sourcePassField, new CellConstraints("4, 8, 1, 1, default, default"));
            }
            {
                sourceConnectButton = new JButton();
                sourcePanel.add(sourceConnectButton, new CellConstraints("6, 8, 1, 1, default, default"));
                sourceConnectButton.setText("connect");
                sourceConnectButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (Database.getConnection(createSourceProps()) != null) setSourceIsValid(true);
                        } catch (SQLException ex) {
                            setSourceIsValid(false);
                        }
                    }
                });
            }
            {
                sourceValidLabel = new JLabel();
                sourcePanel.add(sourceValidLabel, new CellConstraints("6, 1, 1, 1, default, default"));
            }
            {
                sourceClassPathLabel = new JLabel();
                sourcePanel.add(sourceClassPathLabel, new CellConstraints("2, 3, 1, 1, default, default"));
                sourceClassPathLabel.setText("ClassPath");
            }
            {
                sourceUrlPrefixLabel = new JLabel();
                sourcePanel.add(sourceUrlPrefixLabel, new CellConstraints("2, 4, 1, 1, default, default"));
                sourceUrlPrefixLabel.setText("URL");
            }
            {
                sourceClassNameField = new JTextField();
                sourcePanel.add(sourceClassNameField, new CellConstraints("4, 3, 1, 1, default, default"));
                sourceClassNameField.setEditable(false);
            }
            {
                sourceUrlField = new JTextField();
                sourcePanel.add(sourceUrlField, new CellConstraints("4, 4, 1, 1, default, default"));
                sourceUrlField.setEditable(false);
            }
            {
                sourceLibLabel = new JLabel();
                sourcePanel.add(sourceLibLabel, new CellConstraints("2, 2, 1, 1, default, default"));
                sourceLibLabel.setText("Driver");
            }
            {
                sourceDriverCombo = new JComboBox();
                sourceDriverCombo.setEnabled(false);
                sourceDriverCombo.addActionListener(new SourceDriverComboActionListener());
                sourcePanel.add(sourceDriverCombo, new CellConstraints("4, 2, 1, 1, default, default"));
            }
        }
        {
            targetPanel = new JPanel();
            this.add(targetPanel);
            FormLayout jPanel6Layout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), 133dlu, 5dlu, max(p;15dlu)", "max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;5dlu), max(p;5dlu), 15dlu, max(p;15dlu)");
            targetPanel.setLayout(jPanel6Layout);
            targetPanel.setBorder(BorderFactory.createTitledBorder("Target Database"));
            {
                targetDatabaseLabel = new JLabel();
                targetPanel.add(targetDatabaseLabel, new CellConstraints("2, 1, 1, 1, default, default"));
                targetDatabaseLabel.setText("Database");
            }
            {
                ComboBoxModel targetDriverModel = new DefaultComboBoxModel(vendors.vendorNameToArray());
                targetVendorCombo = new JComboBox();
                targetVendorCombo.addActionListener(new TargetDatabaseComboActionListener());
                targetPanel.add(targetVendorCombo, new CellConstraints("4, 1, 1, 1, default, default"));
                targetVendorCombo.setModel(targetDriverModel);
            }
            {
                targetUrlLabel = new JLabel();
                targetPanel.add(targetUrlLabel, new CellConstraints("2, 6, 1, 1, default, default"));
                targetUrlLabel.setText("URL Suffix");
            }
            {
                targetUrlSuffixField = new JTextField("localhost/loaderoutput");
                targetUrlSuffixField.getDocument().addDocumentListener(new TargetDocumentListener());
                targetUrlSuffixField.addCaretListener(new CaretListener() {

                    public void caretUpdate(CaretEvent e) {
                        updateTargetUrlField();
                    }
                });
                targetPanel.add(targetUrlSuffixField, new CellConstraints("4, 6, 1, 1, default, default"));
            }
            {
                targetUserLabel = new JLabel();
                targetPanel.add(targetUserLabel, new CellConstraints("2, 7, 1, 1, default, default"));
                targetUserLabel.setText("Username");
            }
            {
                targetUserField = new JTextField("root");
                targetUserField.getDocument().addDocumentListener(new TargetDocumentListener());
                targetPanel.add(targetUserField, new CellConstraints("4, 7, 1, 1, default, default"));
            }
            {
                targetPassLabel = new JLabel();
                targetPanel.add(targetPassLabel, new CellConstraints("2, 8, 1, 1, default, default"));
                targetPassLabel.setText("Password");
            }
            {
                targetPassField = new JPasswordField();
                targetPassField.getDocument().addDocumentListener(new TargetDocumentListener());
                ;
                targetPanel.add(targetPassField, new CellConstraints("4, 8, 1, 1, default, default"));
            }
            {
                targetConnectButton = new JButton();
                targetPanel.add(targetConnectButton, new CellConstraints("6, 8, 1, 1, default, default"));
                targetConnectButton.setText("connect");
                targetConnectButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (Database.getConnection(createTargetProps()) != null) setTargetIsValid(true);
                        } catch (Exception ex) {
                            setTargetIsValid(false);
                        }
                    }
                });
            }
            {
                targetValidLabel = new JLabel();
                targetPanel.add(targetValidLabel, new CellConstraints("6, 1, 1, 1, default, default"));
            }
            {
                targetClassPathLabel = new JLabel();
                targetPanel.add(targetClassPathLabel, new CellConstraints("2, 3, 1, 1, default, default"));
                targetClassPathLabel.setText("ClassPath");
            }
            {
                targetClassNameField = new JTextField();
                targetPanel.add(targetClassNameField, new CellConstraints("4, 3, 1, 1, default, default"));
                targetClassNameField.setEditable(false);
            }
            {
                targetUrlPrefixLabel = new JLabel();
                targetPanel.add(targetUrlPrefixLabel, new CellConstraints("2, 4, 1, 1, default, default"));
                targetUrlPrefixLabel.setText("URL");
            }
            {
                targetUrlField = new JTextField();
                targetPanel.add(targetUrlField, new CellConstraints("4, 4, 1, 1, default, default"));
                targetUrlField.setEditable(false);
            }
            {
                targetLibLabel = new JLabel();
                targetPanel.add(targetLibLabel, new CellConstraints("2, 2, 1, 1, default, default"));
                targetLibLabel.setText("Driver");
                {
                    targetDriverLabel = new JLabel();
                    targetLibLabel.add(targetDriverLabel, new CellConstraints("2, 2, 1, 1, default, default"));
                    targetDriverLabel.setText("Driver Name");
                }
            }
            {
                targetDriverCombo = new JComboBox();
                targetDriverCombo.setEnabled(false);
                targetDriverCombo.addActionListener(new TargetDriverComboActionListener());
                targetPanel.add(targetDriverCombo, new CellConstraints("4, 2, 1, 1, default, default"));
            }
        }
        {
            libPanel = new JPanel();
            this.add(libPanel);
            FormLayout externalLibsPanelLayout = new FormLayout("max(p;5dlu), 168dlu, 5dlu, max(p;5dlu)", "top:max(p;5dlu), 5dlu");
            libPanel.setLayout(externalLibsPanelLayout);
            libPanel.setBorder(BorderFactory.createTitledBorder("External Jars"));
            {
                libList = new JList(new DefaultListModel());
                libPanel.add(new JScrollPane(libList), new CellConstraints("2, 1, 1, 1, default, default"));
            }
            {
                libBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                FormLayout jPanel1Layout = new FormLayout("max(p;5dlu)", "max(p;5dlu), max(p;5dlu)");
                libBtnPanel.setLayout(jPanel1Layout);
                libPanel.add(libBtnPanel, new CellConstraints("4, 1, 1, 1, default, default"));
                {
                    removeJarBtn = new JButton();
                    libBtnPanel.add(removeJarBtn, new CellConstraints("1, 1, 1, 1, default, default"));
                    removeJarBtn.addActionListener(new RemoveJarBtnActionListener());
                    removeJarBtn.setText("remove jar");
                }
                {
                    addJarBtn = new JButton();
                    libBtnPanel.add(addJarBtn, new CellConstraints("1, 2, 1, 1, default, default"));
                    addJarBtn.addActionListener(new AddJarBtnActionListener());
                    addJarBtn.setText("add jar");
                }
            }
        }
    }

    private Properties createSourceProps() {
        if (sourceProps == null) sourceProps = new Properties();
        sourceProps.put("dbVendor", (String) sourceVendorCombo.getSelectedItem());
        sourceProps.put("driverName", (String) sourceDriverCombo.getSelectedItem());
        sourceProps.put("JdbcDriver", sourceClassNameField.getText());
        sourceProps.put("Connection.Url", sourceUrlField.getText());
        sourceProps.put("User", sourceUserField.getText() != null ? sourceUserField.getText() : "");
        String passwd = String.valueOf(sourcePassField.getPassword());
        sourceProps.put("Password", passwd != null ? passwd : "");
        return sourceProps;
    }

    private Properties createTargetProps() {
        if (targetProps == null) targetProps = new Properties();
        targetProps.put("dbVendor", (String) targetVendorCombo.getSelectedItem());
        targetProps.put("driverName", (String) targetDriverCombo.getSelectedItem());
        targetProps.put("JdbcDriver", targetClassNameField.getText());
        targetProps.put("Connection.Url", targetUrlField.getText());
        targetProps.put("User", targetUserField.getText() != null ? targetUserField.getText() : "");
        String passwd = String.valueOf(targetPassField.getPassword());
        targetProps.put("Password", passwd != null ? passwd : "");
        return targetProps;
    }

    /**
     * Sets the naem of the project. <b>NOTE:</b> the path is build with this
     * name and the location.
     * 
     * @param name
     *            The name to set.
     */
    private void setProjectName(String name) {
        this.projectName = name + ".oge";
        this.locationField.setText(this.projectLocation + "/" + this.projectName);
    }

    private void setProjectLocation(String location) {
        this.projectLocation = location;
        this.locationField.setText(this.projectLocation + File.separatorChar + this.projectName);
    }

    /**
     * @param sourceIsValid
     *            The sourceIsValid to set.
     */
    private void setSourceIsValid(boolean sourceIsValid) {
        this.sourceIsValid = sourceIsValid;
        if (sourceIsValid) sourceValidLabel.setText("connected"); else sourceValidLabel.setText("not connected");
        updateValidation();
    }

    /**
     * @param targetIsValid
     *            The targetIsValid to set.
     */
    private void setTargetIsValid(boolean targetIsValid) {
        this.targetIsValid = targetIsValid;
        if (targetIsValid) targetValidLabel.setText("connected"); else targetValidLabel.setText("not connected");
        updateValidation();
    }

    private void updateValidation() {
        boolean oldIsValid = this.isValid;
        if (sourceIsValid && targetIsValid) this.isValid = true; else this.isValid = false;
        observeable.firePropertyChange(ProjectFormsPanel.VALIDATION_PROP, oldIsValid, this.isValid);
    }

    /**
     * @return Returns the projectLocation.
     */
    public String getProjectLocation() {
        return projectLocation;
    }

    /**
     * @return Returns the projectName.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @return Returns the sourceProps.
     */
    public Properties getSourceProps() {
        return createSourceProps();
    }

    /**
     * @return Returns the targetProps.
     */
    public Properties getTargetProps() {
        return createTargetProps();
    }

    public Project createProject() throws IllegalStateException {
        if (!isValid) {
            throw new IllegalStateException("Project properties invalid or incomplete!");
        }
        return new Project(getProjectName(), getProjectLocation(), getSourceProps(), getTargetProps());
    }

    /**
     * @param sourceUrl
     *            The sourceUrl to set.
     */
    private void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        updateSourceUrlField();
    }

    private void updateSourceUrlField() {
        sourceUrlField.setText(sourceUrl + sourceUrlSuffixField.getText());
    }

    /**
     * @param targetUrl
     *            The targetUrl to set.
     */
    private void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        updateTargetUrlField();
    }

    private void updateTargetUrlField() {
        targetUrlField.setText(targetUrl + targetUrlSuffixField.getText());
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        observeable.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        observeable.removePropertyChangeListener(propertyName, listener);
    }

    private class BrowseButtonActionListener implements ActionListener {

        JFileChooser fileChooser = null;

        public void actionPerformed(ActionEvent e) {
            getFileChooser().showOpenDialog(ProjectFormsPanel.this.owner);
            File file = getFileChooser().getSelectedFile();
            if (file != null) setProjectLocation(file.getPath());
        }

        /**
         * @return Returns the fileChooser.
         */
        private JFileChooser getFileChooser() {
            if (fileChooser == null) {
                fileChooser = new JFileChooser(".");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            return fileChooser;
        }
    }

    private class SourceDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            setSourceIsValid(false);
        }

        public void removeUpdate(DocumentEvent e) {
            setSourceIsValid(false);
        }

        public void changedUpdate(DocumentEvent e) {
            setSourceIsValid(false);
        }
    }

    private class TargetDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            setTargetIsValid(false);
        }

        public void removeUpdate(DocumentEvent e) {
            setTargetIsValid(false);
        }

        public void changedUpdate(DocumentEvent e) {
            setTargetIsValid(false);
        }
    }

    private class SourceDatabaseComboActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setSourceIsValid(false);
            String selectedVendor = (String) sourceVendorCombo.getSelectedItem();
            sourceDriverCombo.setModel(new DefaultComboBoxModel(vendors.driverNameToArray(selectedVendor)));
            sourceDriverCombo.setSelectedIndex(0);
            if (!sourceDriverCombo.isEnabled()) sourceDriverCombo.setEnabled(true);
        }
    }

    private class TargetDatabaseComboActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setTargetIsValid(false);
            String selectedVendor = (String) targetVendorCombo.getSelectedItem();
            targetDriverCombo.setModel(new DefaultComboBoxModel(vendors.driverNameToArray(selectedVendor)));
            targetDriverCombo.setSelectedIndex(0);
            if (!targetDriverCombo.isEnabled()) targetDriverCombo.setEnabled(true);
        }
    }

    private class SourceDriverComboActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String vendorName = (String) sourceVendorCombo.getSelectedItem();
            String driverName = (String) sourceDriverCombo.getSelectedItem();
            sourceClassNameField.setText(vendors.getClassName(vendorName, driverName));
            setSourceUrl(vendors.getConnectionPrefix(vendorName, driverName));
        }
    }

    private class TargetDriverComboActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String vendorName = (String) targetVendorCombo.getSelectedItem();
            String driverName = (String) targetDriverCombo.getSelectedItem();
            targetClassNameField.setText(vendors.getClassName(vendorName, driverName));
            setTargetUrl(vendors.getConnectionPrefix(vendorName, driverName));
        }
    }

    private class AddJarBtnActionListener implements ActionListener {

        private JFileChooser chooser = null;

        public void actionPerformed(ActionEvent e) {
            File path = null;
            if (chooser == null) chooser = new JFileChooser(path == null ? "." : path.toString());
            chooser.setFileFilter(new JarFileFilter());
            chooser.showOpenDialog(ProjectFormsPanel.this);
            File file = chooser.getSelectedFile();
            if (file == null) return;
            try {
                if (!JarClassLoader.contains(file) || !((DefaultListModel) libList.getModel()).contains(file.getPath())) {
                    JarClassLoader.addFile(file);
                    ((DefaultListModel) libList.getModel()).addElement(file.getPath());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class RemoveJarBtnActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((DefaultListModel) libList.getModel()).removeElement(libList.getSelectedValue());
        }
    }
}
