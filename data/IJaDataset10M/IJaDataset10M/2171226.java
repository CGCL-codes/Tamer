package allensoft.javacvs.client.ui.swing;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;
import java.io.*;
import allensoft.gui.*;
import allensoft.javacvs.client.*;
import allensoft.util.*;

/**
 * A dislog which displays a selection of files that the user can select to add and detrmine whether the
 * file is binary or not. The supplied classifier is setup with the choices the user made if the dialog is not cancelled.
 * It can then be used as the classifier for any add requests.
 * @author Nicholas Allen
 */
public class SelectFilesToAddDialog extends SelectFileTypesDialog {

    private static Preferences prefs = Preferences.userNodeForPackage(SelectFilesToAddDialog.class);

    public SelectFilesToAddDialog(Frame owner, Contents contents) {
        super(owner, contents);
    }

    public SelectFilesToAddDialog(Frame owner, Collection files, DefaultKeywordSubstitutionModeClassifier classifier) {
        this(owner, new Contents(files, classifier));
    }

    public SelectFilesToAddDialog(Frame owner, File[] files, DefaultKeywordSubstitutionModeClassifier classifier) {
        this(owner, Arrays.asList(files), classifier);
    }

    public File[] getSelectedFiles() {
        return ((Contents) getContents()).getSelectedFiles();
    }

    public File[] getUnselectedFiles() {
        return ((Contents) getContents()).getUnselectedFiles();
    }

    /** Defines the contents for this dialog. */
    public static class Contents extends SelectFileTypesDialog.Contents {

        public Contents(Collection files, DefaultKeywordSubstitutionModeClassifier classifier) {
            super(files, classifier);
        }

        protected Component createMainPanel(Component filesPanel) {
            StripPanel mainPanel = new StripPanel(false);
            mainPanel.add(new JLabel("<html>JavaCVS has found some files which do not exist in the repository that you may wish to add.<br>" + "Please select any files that you would like to add below and specify the types of these files."));
            mainPanel.add(filesPanel);
            StripPanel buttonPanel = new StripPanel(true);
            JButton selectAll = new JButton("Select All");
            selectAll.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectAll();
                }
            });
            JButton selectNone = new JButton("Select None");
            selectNone.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectNone();
                }
            });
            buttonPanel.add(selectAll);
            buttonPanel.add(selectNone);
            mainPanel.add(buttonPanel);
            mainPanel.add(m_PerformCheckOnCommit = new JCheckBox("Perform this check whenever I commit files", prefs.getBoolean("checkForFilesToAddOnCommit", true)));
            mainPanel.add(m_DontAskAgainForUnselectedFiles = new JCheckBox("Ignore unselected files in the future"));
            return mainPanel;
        }

        protected Component createLabel(File file) {
            JCheckBox checkBox = new JCheckBox(file.getAbsolutePath());
            if (m_Checkboxes == null) m_Checkboxes = new HashMap();
            m_Checkboxes.put(file, checkBox);
            return checkBox;
        }

        public boolean areContentsValid() {
            if (!super.areContentsValid()) return false;
            prefs.putBoolean("checkForFilesToAddOnCommit", m_PerformCheckOnCommit.isSelected());
            if (m_DontAskAgainForUnselectedFiles.isSelected()) {
                Iterator i = m_Files.iterator();
                while (i.hasNext()) {
                    File file = (File) i.next();
                    if (!isSelected(file)) {
                        File cvsignore = new File(file.getParentFile(), ".cvsignore");
                        if (!cvsignore.exists()) try {
                            cvsignore.createNewFile();
                        } catch (IOException e) {
                            continue;
                        }
                        CVSIgnoreFile ignoreFile = CVSIgnoreFile.getCVSIgnoreFile(cvsignore);
                        if (ignoreFile != null) ignoreFile.addPattern(file.getName());
                    }
                }
            }
            return true;
        }

        protected boolean isFileValid(File file) {
            if (isSelected(file)) return super.isFileValid(file);
            return true;
        }

        public void selectAll() {
            Iterator i = m_Checkboxes.values().iterator();
            while (i.hasNext()) {
                JCheckBox c = (JCheckBox) i.next();
                c.setSelected(true);
            }
        }

        public void selectNone() {
            Iterator i = m_Checkboxes.values().iterator();
            while (i.hasNext()) {
                JCheckBox c = (JCheckBox) i.next();
                c.setSelected(false);
            }
        }

        public File[] getSelectedFiles() {
            return getFiles(true);
        }

        public File[] getUnselectedFiles() {
            return getFiles(false);
        }

        /** Gets the files that are either selected or not selected as specified. */
        private File[] getFiles(boolean bSelected) {
            Collection files = new ArrayList(m_Files.size());
            Iterator i = m_Files.iterator();
            while (i.hasNext()) {
                File file = (File) i.next();
                if (isSelected(file) == bSelected) files.add(file);
            }
            return (File[]) files.toArray(new File[files.size()]);
        }

        public boolean isSelected(File file) {
            JCheckBox c = (JCheckBox) m_Checkboxes.get(file);
            if (c != null) return c.isSelected();
            return false;
        }

        public void setSelected(File file, boolean b) {
            JCheckBox c = (JCheckBox) m_Checkboxes.get(file);
            if (c != null) c.setSelected(b);
        }

        private Map m_Checkboxes;

        private JCheckBox m_PerformCheckOnCommit;

        private JCheckBox m_DontAskAgainForUnselectedFiles;

        private static final String[] g_FileTypes = { "Text", "Binary" };
    }
}
