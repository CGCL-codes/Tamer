package org.lnicholls.galleon.apps.musicOrganizer;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppConfiguration;
import org.lnicholls.galleon.app.AppConfigurationPanel;
import org.lnicholls.galleon.gui.OptionsTable;
import org.lnicholls.galleon.util.NameValue;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class MusicOrganizerOptionsPanel extends AppConfigurationPanel implements ActionListener {

    private static Logger log = Logger.getLogger(MusicOrganizerOptionsPanel.class.getName());

    static class FileTransferHandler extends TransferHandler {

        public FileTransferHandler(OptionsTable optionsTable) {
            mOptionsTable = optionsTable;
        }

        public boolean importData(JComponent c, Transferable t) {
            if (!canImport(c, t.getTransferDataFlavors())) {
                return false;
            }
            try {
                if (hasFileFlavor(t.getTransferDataFlavors())) {
                    String str = null;
                    java.util.List files = (java.util.List) t.getTransferData(mFileFlavor);
                    for (int i = 0; i < files.size(); i++) {
                        File file = (File) files.get(i);
                        if (!file.isDirectory()) return false;
                        int rows = mOptionsTable.getModel().getRowCount();
                        mOptionsTable.getModel().setValueAt(file.getAbsolutePath(), rows, 0);
                    }
                    return true;
                }
            } catch (UnsupportedFlavorException ufe) {
                log.error("importData: unsupported data flavor");
            } catch (IOException ieo) {
                log.error("importData: I/O exception");
            }
            return false;
        }

        protected Transferable createTransferable(JComponent c) {
            return null;
        }

        public int getSourceActions(JComponent c) {
            return COPY;
        }

        protected void exportDone(JComponent c, Transferable data, int action) {
        }

        public boolean canImport(JComponent c, DataFlavor[] flavors) {
            return hasFileFlavor(flavors);
        }

        private boolean hasFileFlavor(DataFlavor[] flavors) {
            for (int i = 0; i < flavors.length; i++) {
                if (mFileFlavor.equals(flavors[i])) {
                    return true;
                }
            }
            return false;
        }

        private DataFlavor mFileFlavor = DataFlavor.javaFileListFlavor;

        private OptionsTable mOptionsTable;
    }

    public MusicOrganizerOptionsPanel(AppConfiguration appConfiguration) {
        super(appConfiguration);
        setLayout(new GridLayout(0, 1));
        MusicOrganizerConfiguration musicConfiguration = (MusicOrganizerConfiguration) appConfiguration;
        setLayout(new GridLayout(0, 1));
        mTitleField = new JTextField(musicConfiguration.getName());
        mSharedField = new JCheckBox("Share");
        mSharedField.setSelected(musicConfiguration.isShared());
        mSharedField.setToolTipText("Share this app");
        mPathField = new JTextField("");
        JButton button = new JButton("...");
        button.setActionCommand("pick");
        button.addActionListener(this);
        mCategoryCombo1 = new JComboBox();
        populateCombo(mCategoryCombo1);
        mCategoryCombo2 = new JComboBox();
        populateCombo(mCategoryCombo2);
        mCategoryCombo3 = new JComboBox();
        populateCombo(mCategoryCombo3);
        mCategoryCombo4 = new JComboBox();
        populateCombo(mCategoryCombo4);
        mCategoryCombo5 = new JComboBox();
        populateCombo(mCategoryCombo5);
        mCategoryCombo6 = new JComboBox();
        populateCombo(mCategoryCombo6);
        mCategoryCombo7 = new JComboBox();
        populateCombo(mCategoryCombo7);
        mCategoryCombo8 = new JComboBox();
        populateCombo(mCategoryCombo8);
        mCategoryCombo9 = new JComboBox();
        populateCombo(mCategoryCombo9);
        mCategoryCombo10 = new JComboBox();
        populateCombo(mCategoryCombo10);
        Iterator iterator = musicConfiguration.getGroups().iterator();
        if (iterator.hasNext()) defaultCombo(mCategoryCombo1, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo2, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo3, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo4, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo5, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo6, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo7, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo8, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo9, (String) iterator.next());
        if (iterator.hasNext()) defaultCombo(mCategoryCombo10, (String) iterator.next());
        FormLayout layout = new FormLayout("right:pref, 3dlu, 50dlu:g, 3dlu, left:pref:grow", "pref, " + "9dlu, " + "pref, " + "3dlu, " + "pref, " + "9dlu, " + "pref, " + "9dlu, " + "pref, " + "3dlu, " + "pref," + "9dlu, " + "pref, " + "9dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref," + "3dlu, " + "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("General", cc.xyw(1, 1, 5));
        builder.addLabel("Title", cc.xy(1, 3));
        builder.add(mTitleField, cc.xyw(3, 3, 1));
        builder.add(mSharedField, cc.xyw(3, 5, 1));
        builder.addSeparator("Directories", cc.xyw(1, 7, 5));
        mColumnValues = new ArrayList();
        int counter = 0;
        for (Iterator i = musicConfiguration.getPaths().iterator(); i.hasNext(); ) {
            String value = (String) i.next();
            ArrayList values = new ArrayList();
            values.add(0, value);
            mColumnValues.add(counter++, values);
        }
        ArrayList columnNames = new ArrayList();
        columnNames.add(0, "Path");
        ArrayList fields = new ArrayList();
        fields.add(mPathField);
        mOptionsTable = new OptionsTable(this, columnNames, mColumnValues, fields);
        mOptionsTable.setTransferHandler(new FileTransferHandler(mOptionsTable));
        builder.addLabel("Path", cc.xy(1, 9));
        builder.add(mPathField, cc.xy(3, 9));
        builder.add(button, cc.xy(5, 9));
        builder.add(mOptionsTable, cc.xyw(1, 11, 5));
        builder.addSeparator("Categories", cc.xyw(1, 13, 5));
        builder.addLabel("Category 1", cc.xy(1, 15));
        builder.add(mCategoryCombo1, cc.xyw(3, 15, 1));
        builder.addLabel("Category 2", cc.xy(1, 17));
        builder.add(mCategoryCombo2, cc.xyw(3, 17, 1));
        builder.addLabel("Category 3", cc.xy(1, 19));
        builder.add(mCategoryCombo3, cc.xyw(3, 19, 1));
        builder.addLabel("Category 4", cc.xy(1, 21));
        builder.add(mCategoryCombo4, cc.xyw(3, 21, 1));
        builder.addLabel("Category 5", cc.xy(1, 23));
        builder.add(mCategoryCombo5, cc.xyw(3, 23, 1));
        builder.addLabel("Category 6", cc.xy(1, 25));
        builder.add(mCategoryCombo6, cc.xyw(3, 25, 1));
        builder.addLabel("Category 7", cc.xy(1, 27));
        builder.add(mCategoryCombo7, cc.xyw(3, 27, 1));
        builder.addLabel("Category 8", cc.xy(1, 29));
        builder.add(mCategoryCombo8, cc.xyw(3, 29, 1));
        builder.addLabel("Category 9", cc.xy(1, 31));
        builder.add(mCategoryCombo9, cc.xyw(3, 31, 1));
        builder.addLabel("Category 10", cc.xy(1, 33));
        builder.add(mCategoryCombo10, cc.xyw(3, 33, 1));
        JPanel panel = builder.getPanel();
        add(panel);
    }

    private void populateCombo(JComboBox combo) {
        combo.addItem(new ComboWrapper("None", ""));
        MusicOrganizerConfiguration musicConfiguration = (MusicOrganizerConfiguration) mAppConfiguration;
        for (Iterator categoryIterator = musicConfiguration.getDefaultCategories().iterator(); categoryIterator.hasNext(); ) {
            String category = (String) categoryIterator.next();
            String name = category.substring(0, category.indexOf("\\"));
            combo.addItem(new ComboWrapper(name, category));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ("pick".equals(e.getActionCommand())) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.addChoosableFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "Directories";
                }
            });
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mPathField.setText(file.getAbsolutePath());
                mOptionsTable.checkButtonStates();
            }
        }
    }

    public void load() {
    }

    public boolean valid() {
        if (mTitleField.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Invalid title.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mColumnValues.size() == 0) {
            JOptionPane.showMessageDialog(this, "No directories configured.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ((((NameValue) mCategoryCombo1.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo2.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo3.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo4.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo5.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo6.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo7.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo8.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo9.getSelectedItem()).getValue().length() == 0) && (((NameValue) mCategoryCombo10.getSelectedItem()).getValue().length() == 0)) {
            JOptionPane.showMessageDialog(this, "No categories configured.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void save() {
        log.debug("save()");
        MusicOrganizerConfiguration musicConfiguration = (MusicOrganizerConfiguration) mAppConfiguration;
        boolean first = musicConfiguration.getPaths().size() == 0 || musicConfiguration.getGroups().size() == 0;
        musicConfiguration.setName(mTitleField.getText());
        musicConfiguration.setShared(mSharedField.isSelected());
        ArrayList newItems = new ArrayList();
        Iterator iterator = mColumnValues.iterator();
        while (iterator.hasNext()) {
            ArrayList rows = (ArrayList) iterator.next();
            log.debug("Path=" + rows.get(0));
            newItems.add((String) rows.get(0));
        }
        musicConfiguration.setPaths(newItems);
        ArrayList categories = new ArrayList();
        int count = 0;
        if (((NameValue) mCategoryCombo1.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo1.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo2.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo2.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo3.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo3.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo4.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo4.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo5.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo5.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo6.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo6.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo7.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo7.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo8.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo8.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo9.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo9.getSelectedItem()).getValue());
        if (((NameValue) mCategoryCombo10.getSelectedItem()).getValue().length() > 0) categories.add(((NameValue) mCategoryCombo10.getSelectedItem()).getValue());
        musicConfiguration.setGroups(categories);
        if (categories.size() == 0) JOptionPane.showMessageDialog(this, "You have not selected any categories", "Warning", JOptionPane.WARNING_MESSAGE); else if (first) JOptionPane.showMessageDialog(this, "Depending on the size of your MP3 collection, it will take some time for the organizer to categorize your collection.\nYou will be able to use the organizer immediately and the categorized files will grow over time.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private JTextComponent mTitleField;

    private JTextComponent mPathField;

    private JComboBox mCategoryCombo1;

    private JComboBox mCategoryCombo2;

    private JComboBox mCategoryCombo3;

    private JComboBox mCategoryCombo4;

    private JComboBox mCategoryCombo5;

    private JComboBox mCategoryCombo6;

    private JComboBox mCategoryCombo7;

    private JComboBox mCategoryCombo8;

    private JComboBox mCategoryCombo9;

    private JComboBox mCategoryCombo10;

    private OptionsTable mOptionsTable;

    private ArrayList mColumnValues;

    private JCheckBox mSharedField;
}
