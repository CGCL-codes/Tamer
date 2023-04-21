package edu.uiuc.jdbv.imports.sql;

import edu.uiuc.jdbv.gui.*;
import edu.uiuc.jdbv.util.SuffixFileFilter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Gui which populates SqlImportDefinition with the correct file to parse
 * @author Ross Paul
 * @author Brian Sidharta
 * @version $Revision: 1.10 $
 */
public class SqlGuiPanel extends JPanel {

    /** Description for SQL Files */
    public static final String SQL_FILE_DESC = "SQL Script Files";

    /** Extensions for SQL filenames */
    public static final String[] SQL_FILE_EXTENSIONS = { "sql" };

    private static final JFileChooser fileChooser;

    private SqlImportDefinition sqlImport;

    static {
        SuffixFileFilter fileFilter = new SuffixFileFilter(SQL_FILE_DESC, SQL_FILE_EXTENSIONS);
        fileChooser = new JFileChooser(".");
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setControlButtonsAreShown(false);
    }

    /** Constructor needs to take in the importer it is tied to */
    public SqlGuiPanel(SqlImportDefinition sqlDef) {
        super(new BorderLayout());
        sqlImport = sqlDef;
        fileChooser.setAccessory(createFileOptionsPanel());
        add(new JLabel("Select a File to Import"), BorderLayout.NORTH);
        add(fileChooser, BorderLayout.CENTER);
    }

    /** Adds the list of sql file Types */
    private JPanel createFileOptionsPanel() {
        JPanel sqlTypesPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton hackButton;
        hackButton = new JRadioButton("SQL92", true);
        buttonGroup.add(hackButton);
        sqlTypesPanel.add(hackButton);
        hackButton = new JRadioButton("MS SQL Server");
        hackButton.setEnabled(false);
        buttonGroup.add(hackButton);
        sqlTypesPanel.add(hackButton);
        hackButton = new JRadioButton("Oracle SQL");
        hackButton.setEnabled(false);
        buttonGroup.add(hackButton);
        sqlTypesPanel.add(hackButton);
        sqlTypesPanel.setBorder(BorderFactory.createTitledBorder("SQL Type"));
        return sqlTypesPanel;
    }

    /** Gets the file selected by the user */
    File getSelectedFile() {
        return (fileChooser.getSelectedFile());
    }
}
