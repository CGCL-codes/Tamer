package editorscheme;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.StandardUtilities;
import org.gjt.sp.util.Log;

/**
* Displays available jEdit 'Editor Schemes' and allows the user to switch
* between schemes and update writable schemes.
*/
public class EditorSchemeSelectorDialog extends EnhancedDialog implements ActionListener {

    private EditorScheme selectedScheme;

    private JList schemeList;

    ArrayList<EditorScheme> schemes;

    private JCheckBox autoApply;

    private JCheckBoxList groupsList;

    private JButton closeButton;

    private JButton newButton;

    private JButton saveButton;

    private JButton selectButton;

    public EditorSchemeSelectorDialog(View view) {
        super(view, jEdit.getProperty("editor-scheme.selector.title"), false);
        JPanel schemesPanel = new JPanel(new BorderLayout());
        JPanel main = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);
        EditorSchemePlugin.loadSchemes();
        schemes = new ArrayList<EditorScheme>(EditorSchemePlugin.getSchemes());
        EditorScheme original = new EditorScheme();
        original.getFromCurrent();
        original.setName(jEdit.getProperty("editor-scheme.selector.currentscheme"));
        schemes.add(0, original);
        schemeList = new JList(schemes.toArray(new EditorScheme[0]));
        schemeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        schemeList.setCellRenderer(new EditorSchemeListCellRenderer());
        ListSelectionHandler listSelectionHandler = new ListSelectionHandler();
        schemeList.addListSelectionListener(listSelectionHandler);
        schemeList.addMouseListener(listSelectionHandler);
        schemesPanel.add(new JLabel(jEdit.getProperty("editor-scheme.schemes")), BorderLayout.NORTH);
        schemesPanel.add(new JScrollPane(schemeList), BorderLayout.CENTER);
        autoApply = new JCheckBox(jEdit.getProperty("editor-scheme.autoapply.label"), jEdit.getBooleanProperty("editor-scheme.autoapply", true));
        schemesPanel.add(autoApply, BorderLayout.SOUTH);
        main.add(schemesPanel);
        JPanel groupsPanel = new JPanel(new BorderLayout());
        ArrayList groups = EditorScheme.getPropertyGroups();
        JCheckBoxList.Entry[] entries = new JCheckBoxList.Entry[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            EditorScheme.PropertyGroup group = (EditorScheme.PropertyGroup) groups.get(i);
            entries[i] = new JCheckBoxList.Entry(group.apply, group);
        }
        groupsList = new JCheckBoxList(entries);
        groupsPanel.add(new JLabel(jEdit.getProperty("editor-scheme.usegroups")), BorderLayout.NORTH);
        groupsPanel.add(new JScrollPane(groupsList), BorderLayout.CENTER);
        main.add(groupsPanel);
        content.add(main, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createGlue());
        selectButton = new JButton(jEdit.getProperty("editor-scheme.select"));
        selectButton.setEnabled(false);
        selectButton.addActionListener(this);
        buttonsPanel.add(selectButton);
        buttonsPanel.add(Box.createHorizontalStrut(6));
        getRootPane().setDefaultButton(selectButton);
        newButton = new JButton(jEdit.getProperty("editor-scheme.new"));
        newButton.addActionListener(this);
        buttonsPanel.add(newButton);
        buttonsPanel.add(Box.createHorizontalStrut(6));
        saveButton = new JButton(jEdit.getProperty("editor-scheme.update"));
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(Box.createHorizontalStrut(6));
        closeButton = new JButton(jEdit.getProperty("editor-scheme.close"));
        closeButton.addActionListener(this);
        buttonsPanel.add(closeButton);
        buttonsPanel.add(Box.createGlue());
        content.add(buttonsPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(view);
        setVisible(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

    public void ok() {
    }

    public void cancel() {
        dispose();
    }

    public void selectScheme() {
        EditorScheme scheme = (EditorScheme) schemeList.getSelectedValue();
        Object[] entries = groupsList.getCheckedValues();
        for (int i = 0; i < entries.length; i++) {
            EditorScheme.PropertyGroup group = (EditorScheme.PropertyGroup) entries[i];
            group.apply(scheme);
        }
        jEdit.propertiesChanged();
        jEdit.saveSettings();
    }

    /**
	 * Updates the currently selected scheme with the properties from 
	 * '[Current Settings]', which may not be the current settings if
	 * the user has updated the settings.
	 */
    public void saveScheme() {
        String args[] = new String[] { selectedScheme.getName() };
        int response = GUIUtilities.confirm(this, "editor-scheme.confirm-update", args, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) return;
        try {
            selectedScheme.getFromCurrent();
            selectedScheme.save();
            if (autoApply.isSelected()) selectScheme();
        } catch (IOException ioe) {
            GUIUtilities.error(this, "editor-scheme.saveerror", new String[] { selectedScheme.getName(), ioe.toString() });
        }
    }

    void newScheme() {
        String name = "";
        String fileName = "";
        try {
            name = GUIUtilities.input(this, "editor-scheme.save-current", "");
            if (name == null) return;
            fileName = MiscUtilities.constructPath(EditorScheme.getDefaultDir(), name.replace(' ', '_') + EditorScheme.EXTENSION);
            File file = new File(fileName);
            if (file.exists()) {
                String[] args = new String[] { file.getPath() };
                GUIUtilities.error(this, "editor-scheme.schemeexists", args);
                return;
            }
            EditorScheme scheme = new EditorScheme();
            scheme.setName(name);
            scheme.setFilename(fileName);
            scheme.getFromCurrent();
            scheme.setReadOnly(false);
            scheme.save();
            int i;
            for (i = 0; i < schemes.size(); i++) {
                EditorScheme s = (EditorScheme) schemes.get(i);
                if (StandardUtilities.compareStrings(s.getName(), name, true) > 1) break;
            }
            schemes.add(i, scheme);
            schemeList.setListData(schemes.toArray(new EditorScheme[0]));
        } catch (IOException ioe) {
            String[] args = new String[] { name, ioe.toString() };
            GUIUtilities.error(this, "editor-scheme.saveerror", args);
            Log.log(Log.ERROR, EditorSchemePlugin.class, "error saving [" + name + "] to [" + fileName + "]: " + ioe.toString());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == closeButton) {
            cancel();
        } else if (source == newButton) {
            newScheme();
        } else if (source == selectButton) {
            selectScheme();
        }
        if (source == saveButton) {
            saveScheme();
        }
    }

    class ListSelectionHandler extends MouseAdapter implements ListSelectionListener {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) selectScheme();
        }

        public void valueChanged(ListSelectionEvent evt) {
            Object selected = schemeList.getSelectedValue();
            if (selected instanceof EditorScheme) {
                selectedScheme = (EditorScheme) selected;
                saveButton.setEnabled(selectedScheme.getReadOnly() == false);
                selectButton.setEnabled(true);
                if (autoApply.isSelected()) selectScheme();
            } else {
                selectButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        }
    }

    static class EditorSchemeListCellRenderer extends JLabel implements ListCellRenderer {

        EditorSchemeListCellRenderer() {
            setOpaque(true);
        }

        private ImageIcon readOnlyIcon = new ImageIcon(getClass().getResource("/icons/readonly.gif"));

        private ImageIcon normalIcon = new ImageIcon(getClass().getResource("/icons/normal.gif"));

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            EditorScheme scheme = (EditorScheme) value;
            String s = value.toString();
            setText(s);
            if (scheme.getReadOnly()) {
                setIcon(readOnlyIcon);
            } else {
                setIcon(normalIcon);
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
        }
    }
}
