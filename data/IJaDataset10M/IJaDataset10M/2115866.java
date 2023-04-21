package uk.org.sgj.OHCApparatus;

import uk.org.sgj.OHCApparatus.ImportExport.*;
import uk.org.sgj.SGJNifty.Settings.Settings;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import uk.org.sgj.SGJNifty.LightweightWindowListener;

class ProjectMenu {

    OHCApparatus owner;

    private JMenuItem menuLoad, menuSave, menuSaveAs, menuNew, menuImport, menuClose;

    private JFrame frame;

    private boolean projectUpdated;

    private File importedFile;

    private void saveLibrary() {
        if (importedFile != null) {
            owner.saveProject(importedFile);
        } else {
            File f = owner.saveProjectAs(importedFile);
            if (f != null) {
                importedFile = f;
                OHCApparatus.addRecentFile(importedFile.getPath());
                OHCApparatus.setFileInTitle(importedFile);
            }
        }
    }

    protected OHCApparatusProject loadProjectByName(String projectPath, OpenProjectAbstract op) {
        OHCProjectImport pi = new OHCProjectImport(op);
        OHCApparatusProject vv = pi.openProjectByName(projectPath);
        importedFile = pi.getImportedFileName();
        OHCApparatus.setFileInTitle(importedFile);
        return (vv);
    }

    protected OHCApparatusProject loadProject(OpenProjectAbstract op) {
        OHCProjectImport pi = new OHCProjectImport(op);
        OHCApparatusProject vv = pi.openProject();
        if (vv != null) {
            importedFile = pi.getImportedFileName();
            OHCApparatus.addRecentFile(importedFile.getPath());
            OHCApparatus.setFileInTitle(importedFile);
        }
        return (vv);
    }

    protected OHCApparatusProject importFromAnotherProject(OpenProjectAbstract op) {
        OHCProjectImport pi = new OHCProjectImport(op);
        OHCApparatusProject vv = pi.openProject();
        if (vv != null) {
            File tmp = pi.getImportedFileName();
            OHCApparatus.addRecentFile(tmp.getPath());
        }
        return (vv);
    }

    private boolean closeProject() {
        boolean closed = false;
        if (projectUpdated) {
            int retVal = JOptionPane.showConfirmDialog(null, "This library has changed since you last saved it.\n" + "Do you want to save your changes?\n" + "\"Yes\" will save and close the library.\n" + "\"No\" will close the library without saving.\n" + "\"Cancel\" will return to the library without closing or saving.");
            switch(retVal) {
                case javax.swing.JOptionPane.YES_OPTION:
                    {
                        saveLibrary();
                        if (!projectUpdated) {
                            owner.reallyCloseProject();
                            closed = true;
                        }
                        break;
                    }
                case javax.swing.JOptionPane.NO_OPTION:
                    {
                        owner.reallyCloseProject();
                        closed = true;
                        break;
                    }
                case javax.swing.JOptionPane.CANCEL_OPTION:
                    {
                        break;
                    }
            }
        } else {
            owner.reallyCloseProject();
            closed = true;
        }
        return (closed);
    }

    private void triedToCloseWindow() {
        boolean safeToClose = false;
        if (projectUpdated) {
            if (closeProject()) {
                safeToClose = true;
            }
        } else {
            safeToClose = true;
        }
        if (safeToClose) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispose();
        }
    }

    protected void projectSaved() {
        projectUpdated = false;
        menuNew.setEnabled(false);
        menuLoad.setEnabled(false);
        menuSave.setEnabled(false);
        menuImport.setEnabled(true);
        menuSaveAs.setEnabled(true);
        menuClose.setEnabled(true);
        removeRecentFileListFromMenu();
    }

    protected void projectClosed() {
        menuNew.setEnabled(true);
        menuLoad.setEnabled(true);
        menuSave.setEnabled(false);
        menuImport.setEnabled(false);
        menuSaveAs.setEnabled(false);
        menuClose.setEnabled(false);
        projectUpdated = false;
        refreshRecentFileList();
    }

    protected void projectUpdated() {
        projectUpdated = true;
        menuNew.setEnabled(false);
        menuLoad.setEnabled(false);
        menuImport.setEnabled(true);
        menuSave.setEnabled(true);
        menuClose.setEnabled(true);
    }

    JMenu menu;

    ProjectMenu(OHCApparatus o, JMenuBar menuBar, JFrame f) {
        owner = o;
        frame = f;
        ProjectMenuListener listen = new ProjectMenuListener(owner, this);
        OHCAWindowListener ohcawl = new OHCAWindowListener();
        frame.addWindowListener(ohcawl);
        menu = new JMenu("My Citations Library");
        menu.setMnemonic(KeyEvent.VK_L);
        menuBar.add(menu);
        menuLoad = new JMenuItem("Open library", KeyEvent.VK_O);
        menuLoad.addActionListener(listen);
        menu.add(menuLoad);
        menuNew = new JMenuItem("New library", KeyEvent.VK_N);
        menuNew.addActionListener(listen);
        menu.add(menuNew);
        menu.addSeparator();
        menuImport = new JMenuItem("Import records from another library", KeyEvent.VK_I);
        menuImport.addActionListener(listen);
        menuImport.setEnabled(false);
        menu.add(menuImport);
        menu.addSeparator();
        menuSave = new JMenuItem("Save library", KeyEvent.VK_S);
        menuSave.addActionListener(listen);
        menuSave.setEnabled(false);
        menu.add(menuSave);
        menuSaveAs = new JMenuItem("Save library as...", KeyEvent.VK_A);
        menuSaveAs.addActionListener(listen);
        menuSaveAs.setEnabled(false);
        menu.add(menuSaveAs);
        menuClose = new JMenuItem("Close library", KeyEvent.VK_C);
        menuClose.addActionListener(listen);
        menuClose.setEnabled(false);
        menu.add(menuClose);
        menu.addSeparator();
        addRecentFileListToMenu();
    }

    protected void refreshRecentFileList() {
        removeRecentFileListFromMenu();
        addRecentFileListToMenu();
    }

    private void removeRecentFileListFromMenu() {
        Settings sets = OHCApparatus.getRecentFileSettings();
        sets.removeRecentFilesFromMenu(menu);
    }

    private void addRecentFileListToMenu() {
        Settings sets = OHCApparatus.getRecentFileSettings();
        RecentFileMenuListener rfml = new RecentFileMenuListener(this);
        sets.addRecentFilesToMenu("Recently opened libraries", menu, rfml);
    }

    class RecentFileMenuListener implements ActionListener {

        ProjectMenu projectMenu;

        RecentFileMenuListener(ProjectMenu projectMenu) {
            this.projectMenu = projectMenu;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) (e.getSource());
            String menuText = source.getToolTipText();
            new OpenProjectByName(menuText, projectMenu).execute();
        }
    }

    class ProjectMenuListener implements ActionListener {

        OHCApparatus owner;

        ProjectMenu projectMenu;

        ProjectMenuListener(OHCApparatus o, ProjectMenu p) {
            owner = o;
            projectMenu = p;
        }

        private void createNewProject() {
            importedFile = null;
            OHCApparatus.setFileInTitle(importedFile);
            menuNew.setEnabled(false);
            menuLoad.setEnabled(false);
            menuImport.setEnabled(true);
            menuSave.setEnabled(true);
            menuSaveAs.setEnabled(true);
            menuClose.setEnabled(true);
            owner.newProject(new OHCApparatusProject());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) (e.getSource());
            String menuText = source.getText();
            if (menuText.equals("Open library")) {
                new OpenProject(projectMenu).execute();
            } else if (menuText.equals("New library")) {
                createNewProject();
            } else if (menuText.equals("Save library")) {
                projectMenu.saveLibrary();
            } else if (menuText.equals("Save library as...")) {
                File f = owner.saveProjectAs(importedFile);
                if (f != null) {
                    importedFile = f;
                    OHCApparatus.addRecentFile(importedFile.getPath());
                    OHCApparatus.setFileInTitle(importedFile);
                }
            } else if (menuText.equals("Import records from another library")) {
                new ImportFromAnotherProject(projectMenu).execute();
            } else if (menuText.equals("Close library")) {
                closeProject();
            }
        }
    }

    class OHCAWindowListener extends LightweightWindowListener {

        @Override
        public void windowClosing(WindowEvent e) {
            triedToCloseWindow();
        }
    }

    protected File getImportedFile() {
        return importedFile;
    }
}
