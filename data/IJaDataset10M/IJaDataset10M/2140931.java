package uk.org.sgj.YAT;

import uk.org.sgj.YAT.ImportExport.*;
import uk.org.sgj.SGJNifty.Settings.Settings;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import uk.org.sgj.SGJNifty.FontUtils.FontAndColor;
import uk.org.sgj.YAT.Tests.VocabTestGroup;

class ProjectMenu {

    YAT yat;

    private JMenu menu;

    private JMenuItem menuLoad, menuSave, menuSaveAs, menuNew, menuClose, menuPlunder;

    private JFrame frame;

    private boolean projectUpdated;

    private File importedFile;

    protected void refreshRecentFileList() {
        removeRecentFileListFromMenu();
        addRecentFileListToMenu();
    }

    void setEnabled(boolean b) {
        menu.setEnabled(b);
    }

    private void removeRecentFileListFromMenu() {
        Settings sets = YAT.getRecentFileSettings();
        sets.removeRecentFilesFromMenu(menu);
    }

    private void addRecentFileListToMenu() {
        Settings sets = YAT.getRecentFileSettings();
        RecentFileMenuListener rfml = new RecentFileMenuListener();
        sets.addRecentFilesToMenu("Recent files", menu, rfml);
    }

    protected String getImportedFileName() {
        String path = "";
        if (importedFile != null) {
            path = importedFile.getPath();
        }
        return (path);
    }

    class RecentFileMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) (e.getSource());
            String menuText = source.getToolTipText();
            openProject(menuText);
        }
    }

    protected void openProject() {
        YATProject project = loadProject();
        if (null != project) {
            projectSaved();
            yat.loadedProject(project);
            YAT.addRecentFile(importedFile.getPath());
        }
    }

    private void plunderProject() {
        ProjectLoad pi = new ProjectLoad();
        YATProject projectToPlunder = pi.selectAndLoadProject();
        if (null != projectToPlunder) {
            yat.plunderProject(projectToPlunder);
        }
    }

    protected void openProject(String filename) {
        YATProject project = loadProject(filename);
        if (null != project) {
            projectSaved();
            yat.loadedProject(project);
            YAT.addRecentFile(importedFile.getPath());
        } else {
            YAT.removeRecentFile(filename);
        }
    }

    private YATProject loadProject() {
        ProjectLoad pi = new ProjectLoad();
        YATProject vv = pi.selectAndLoadProject();
        importedFile = pi.getImportedFileName();
        return (vv);
    }

    private YATProject loadProject(String filename) {
        ProjectLoad pi = new ProjectLoad();
        YATProject vv = pi.loadProjectByName(filename);
        importedFile = pi.getImportedFileName();
        return (vv);
    }

    private boolean closeProject() {
        boolean closed = false;
        if (projectUpdated) {
            int retVal = JOptionPane.showConfirmDialog(null, "This vocab project has changed since you last saved it.\n" + "Do you want to save your changes?\n" + "\"Yes\" will save and close the vocab project.\n" + "\"No\" will close the vocab project without saving.\n" + "\"Cancel\" will return to the vocab project without closing or saving.");
            switch(retVal) {
                case javax.swing.JOptionPane.YES_OPTION:
                    {
                        saveProject();
                        if (!projectUpdated) {
                            yat.reallyCloseProject();
                            closed = true;
                        }
                        break;
                    }
                case javax.swing.JOptionPane.NO_OPTION:
                    {
                        yat.reallyCloseProject();
                        closed = true;
                        break;
                    }
                case javax.swing.JOptionPane.CANCEL_OPTION:
                    {
                        break;
                    }
            }
        } else {
            yat.reallyCloseProject();
            closed = true;
        }
        return (closed);
    }

    private void triedToCloseWindow() {
        boolean safeToClose = false;
        if (projectUpdated) {
            if (VocabPair.isHebrewUnpointed()) {
                JOptionPane.showMessageDialog(null, "You cannot exit while Hebrew is Unpointed: it isn't safe to save the project.\nWe're turning pointing back on for you.", "Unpointed Hebrew!", JOptionPane.INFORMATION_MESSAGE);
                VocabPair.pointHebrew();
            }
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
        menuPlunder.setEnabled(true);
        menuSave.setEnabled(false);
        menuSaveAs.setEnabled(true);
        menuClose.setEnabled(true);
        removeRecentFileListFromMenu();
    }

    protected void projectClosed() {
        menuNew.setEnabled(true);
        menuLoad.setEnabled(true);
        menuPlunder.setEnabled(false);
        menuSave.setEnabled(false);
        menuSaveAs.setEnabled(false);
        menuClose.setEnabled(false);
        projectUpdated = false;
        refreshRecentFileList();
    }

    protected void projectUpdated() {
        projectUpdated = true;
        menuNew.setEnabled(false);
        menuLoad.setEnabled(false);
        menuSave.setEnabled(true);
        menuClose.setEnabled(true);
    }

    ProjectMenu(YAT o, JMenuBar menuBar, JFrame f) {
        yat = o;
        frame = f;
        ProjectMenuListener listen = new ProjectMenuListener(yat);
        YATWindowListener yatwl = new YATWindowListener();
        frame.addWindowListener(yatwl);
        menu = new JMenu("My Vocab Project");
        menu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(menu);
        menuLoad = new JMenuItem("Open vocab project", KeyEvent.VK_O);
        menuLoad.addActionListener(listen);
        menu.add(menuLoad);
        menuPlunder = new JMenuItem("Plunder context from existing project", KeyEvent.VK_P);
        menuPlunder.addActionListener(listen);
        menu.add(menuPlunder);
        menuNew = new JMenuItem("New vocab project", KeyEvent.VK_N);
        menuNew.addActionListener(listen);
        menu.add(menuNew);
        menuSave = new JMenuItem("Save vocab project", KeyEvent.VK_S);
        menuSave.addActionListener(listen);
        menuSave.setEnabled(false);
        menu.add(menuSave);
        menuSaveAs = new JMenuItem("Save vocab project as...", KeyEvent.VK_A);
        menuSaveAs.addActionListener(listen);
        menuSaveAs.setEnabled(false);
        menu.add(menuSaveAs);
        menuClose = new JMenuItem("Close vocab project", KeyEvent.VK_C);
        menuClose.addActionListener(listen);
        menuClose.setEnabled(false);
        menu.add(menuClose);
        menu.addSeparator();
        menu.addSeparator();
        addRecentFileListToMenu();
    }

    private void saveProject() {
        if (importedFile != null) {
            yat.saveProject(importedFile);
        } else {
            File f = yat.saveProjectAs(importedFile);
            if (f != null) {
                importedFile = f;
                YAT.addRecentFile(importedFile.getPath());
            }
        }
    }

    class ProjectMenuListener implements ActionListener {

        YAT yat;

        ProjectMenuListener(YAT o) {
            yat = o;
        }

        private void createNewProject() {
            importedFile = null;
            menuNew.setEnabled(false);
            menuLoad.setEnabled(false);
            menuSave.setEnabled(true);
            menuSaveAs.setEnabled(true);
            menuClose.setEnabled(true);
            YATFontSet table = new YATFontSet(new FontAndColor("Tahoma", Font.PLAIN, 24, Color.BLUE), new FontAndColor("Tahoma", Font.PLAIN, 24, Color.CYAN), new FontAndColor("Tahoma", Font.PLAIN, 24, Color.BLACK), new FontAndColor("Tahoma", Font.PLAIN, 24, Color.GRAY), new FontAndColor("Tahoma", Font.PLAIN, 24, Color.MAGENTA));
            YATFontSet test = new YATFontSet(new FontAndColor("Tahoma", Font.PLAIN, 36, Color.BLUE), new FontAndColor("Tahoma", Font.PLAIN, 36, Color.CYAN), new FontAndColor("Tahoma", Font.PLAIN, 36, Color.BLACK), new FontAndColor("Tahoma", Font.PLAIN, 36, Color.GRAY), new FontAndColor("Tahoma", Font.PLAIN, 36, Color.MAGENTA));
            Vocab vocab = new Vocab();
            VocabTestGroup vtg = new VocabTestGroup();
            yat.newProject(new YATProject(vocab, vtg, table, test));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) (e.getSource());
            String menuText = source.getText();
            if (menuText.equals("Open vocab project")) {
                openProject();
            } else if (menuText.equals(menuPlunder.getText())) {
                plunderProject();
            } else if (menuText.equals("New vocab project")) {
                createNewProject();
            } else if (menuText.equals("Save vocab project")) {
                saveProject();
            } else if (menuText.equals("Save vocab project as...")) {
                File f = yat.saveProjectAs(importedFile);
                if (f != null) {
                    importedFile = f;
                    YAT.addRecentFile(importedFile.getPath());
                }
            } else if (menuText.equals("Close vocab project")) {
                closeProject();
            }
        }
    }

    class YATWindowListener implements WindowListener {

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            triedToCloseWindow();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }
}
