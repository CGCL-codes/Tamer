package project;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;
import org.eclipse.jface.dialogs.MessageDialog;
import blizzard.mpq.MPQArchive;
import core.Main;
import core.ColorManager;
import core.Globals;
import core.Builder;
import za.co.quirk.layout.LatticeLayout;
import za.co.quirk.layout.LatticeData;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import cache.ImageCache;
import events.EventHandler;
import memory.LibraryMemory;
import loaders.LibraryLoader;
import projecttree.ProjectTree;
import projecttree.NodeModel;
import projecttree.nodes.LibrarySubNode;

public class CreateProject {

    private static Image newProjImage = ImageCache.getImage("big_newprj_wiz.gif");

    private static Image dialogImage = ImageCache.getImage("newprj_wiz.gif");

    private static List list;

    private static Text projText;

    private static Text projLoc;

    private static boolean useWowResource;

    private CreateProject() {
    }

    public static void createProject(final boolean edit) {
        if (Globals.ACTIVE_PROJECT != null && edit == false) {
            MessageDialog.openInformation(Main.shell, "New Project", "Please close the current project first.");
            return;
        }
        if (edit && Globals.ACTIVE_PROJECT == null) return;
        final Shell dialog = new Shell(Main.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        if (!edit) dialog.setText("Create New Project"); else dialog.setText("Edit Project - " + Globals.ACTIVE_PROJECT.getProjectName());
        Rectangle bounds = Main.display.getBounds();
        int screen_x = bounds.width;
        int screen_y = bounds.height;
        double size[][] = new double[][] { { LatticeLayout.FILL, 100, 5, 100, 5 }, { LatticeLayout.PREFERRED, 5, LatticeLayout.PREFERRED, 25, 5 } };
        dialog.setLayout(new FillLayout());
        dialog.setImage(dialogImage);
        Composite comp = new Composite(dialog, SWT.NONE);
        comp.setLayout(new LatticeLayout(size));
        String title = "New Project";
        if (edit) title = "Edit Project";
        Composite top = getTopComposite(newProjImage, comp, title);
        top.setLayoutData(new LatticeData("0, 0, 4, 0"));
        CTabFolder tabFolder = new CTabFolder(comp, SWT.NONE);
        tabFolder.setLayoutData(new LatticeData("0, 2, 4, 2"));
        tabFolder.setSelectionBackground(new Color[] { ColorManager.get(164, 199, 232), Main.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND), Main.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND) }, new int[] { 90, 100 }, true);
        Button create = new Button(comp, SWT.PUSH);
        Button cancel = new Button(comp, SWT.PUSH);
        create.setLayoutData(new LatticeData("1, 3, 1, 3"));
        cancel.setLayoutData(new LatticeData("3, 3, 3, 3"));
        if (!edit) create.setText("Create"); else create.setText("Save");
        cancel.setText("Cancel");
        cancel.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                dialog.dispose();
            }
        });
        create.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                String pText = projText.getText();
                if (pText.length() == 0) {
                    MessageDialog.openError(dialog, "No Project Name", "Please enter a project name");
                    projText.setFocus();
                    return;
                }
                String pLoc = projLoc.getText();
                if (pLoc.length() == 0) {
                    MessageDialog.openError(dialog, "No Directory Selected", "Please select a directory");
                    projLoc.setFocus();
                    return;
                }
                String[] libs = list.getItems();
                for (int i = 0; i < libs.length; i++) {
                    File f = new File(libs[i]);
                    if (f == null || !f.exists()) {
                        MessageDialog.openError(dialog, "Error", "Library file '" + libs[i] + "' does not exist");
                        return;
                    }
                }
                File homeDir = new File(projLoc.getText());
                if (!homeDir.isDirectory()) {
                    MessageDialog.openError(dialog, "Error", "The directory path specified is not a directory.");
                    return;
                }
                String projectName = new String(projText.getText());
                if (!edit) {
                    File projFile = new File(projLoc.getText() + "/" + projText.getText() + (projText.getText().lastIndexOf(Globals.PROJECT_EXTENSION) == -1 ? Globals.PROJECT_EXTENSION : ""));
                    try {
                        projFile.createNewFile();
                    } catch (IOException io) {
                        MessageDialog.openError(dialog, "Error", "Could not create project file. Please check permissions for the target directory.");
                        return;
                    }
                    Project created = null;
                    try {
                        created = ProjectManager.createNewProject(projFile, homeDir, projectName, libs, useWowResource);
                        EventHandler.projectCreated(created);
                    } catch (Exception e) {
                        MessageDialog.openError(dialog, "Error", "Error creating project: " + e.toString());
                        e.printStackTrace();
                        return;
                    }
                    try {
                        ProjectManager.loadProject(created);
                        EventHandler.projectLoaded(created);
                    } catch (Exception e) {
                        MessageDialog.openError(dialog, "Error", "Error loading project: " + e.toString());
                        e.printStackTrace();
                        return;
                    }
                } else {
                    Globals.ACTIVE_PROJECT.setHomeDirectory(homeDir);
                    Globals.ACTIVE_PROJECT.setProjectName(projectName);
                    Vector libraries = new Vector();
                    Vector filesNew = new Vector();
                    for (int i = 0; i < libs.length; i++) {
                        File f = new File(libs[i]);
                        Library lib = new Library(f);
                        libraries.add(lib);
                        filesNew.add(f);
                    }
                    if (useWowResource) Globals.ACTIVE_PROJECT.setImportWowResource(Globals.WOW_PATH); else Globals.ACTIVE_PROJECT.setImportWowResource(null);
                    boolean librariesChanged = false;
                    if (Globals.ACTIVE_PROJECT.getLibraries().size() != libraries.size()) librariesChanged = true; else {
                        Vector filesOld = new Vector();
                        for (int i = 0; i < Globals.ACTIVE_PROJECT.getLibraries().size(); i++) {
                            Library lib = (Library) Globals.ACTIVE_PROJECT.getLibraries().get(i);
                            filesOld.add(lib.getFile());
                        }
                        if (!filesNew.equals(filesOld)) librariesChanged = true;
                    }
                    if (librariesChanged) {
                        Globals.ACTIVE_PROJECT.clearLibraries();
                        LibraryMemory.unloadAll();
                        Vector oldChildren = new Vector(ProjectTree.getLibraryNode().getChildren());
                        for (int i = 0; i < oldChildren.size(); i++) {
                            NodeModel model = (NodeModel) oldChildren.get(i);
                            if (model instanceof LibrarySubNode) {
                                EventHandler.libraryUnloaded(((LibrarySubNode) model).getLibrary());
                            }
                            ProjectTree.getLibraryNode().remove(model);
                        }
                        Globals.getProjectTree().refresh(ProjectTree.getLibraryNode());
                        for (int i = 0; i < libraries.size(); i++) {
                            Library lib = (Library) libraries.get(i);
                            try {
                                LibraryLoader.load(lib, true);
                                EventHandler.libraryLoaded(lib);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Globals.ACTIVE_PROJECT.addLibrary(lib);
                        }
                        ProjectTree.loadLibraryNodes(Globals.ACTIVE_PROJECT);
                        Globals.getProjectTree().refresh(ProjectTree.getLibraryNode());
                    }
                    if (!ProjectTree.getLibraryNode().hasChildren()) Globals.getProjectTree().setExpandedState(ProjectTree.getLibraryNode(), false); else Globals.getProjectTree().setExpandedState(ProjectTree.getLibraryNode(), true);
                    Builder.updateTitleBar();
                }
                dialog.dispose();
            }
        });
        addFirstPage(dialog, tabFolder, edit);
        dialog.pack();
        dialog.setLocation(screen_x / 2 - (dialog.getBounds().width / 2), screen_y / 2 - (dialog.getBounds().height / 2));
        dialog.open();
        projText.setFocus();
    }

    private static void addFirstPage(final Shell dialog, CTabFolder tf, boolean edit) {
        CTabItem tab = new CTabItem(tf, SWT.NONE);
        tab.setText("Project Details");
        Composite comp = new Composite(tf, SWT.NONE);
        double[][] size = new double[][] { { 5, LatticeLayout.FILL, 5 }, { 5, LatticeLayout.PREFERRED, 5 } };
        double[][] gsize = new double[][] { { 5, LatticeLayout.PREFERRED, 5, LatticeLayout.FILL, 5, 20, 5 }, { 5, 20, 2, 20, 10, 20, 2, 20, 10, 20, 2, 20, 2, 20, 108, 2, 20 } };
        comp.setLayout(new LatticeLayout(size));
        Group group = new Group(comp, SWT.NONE);
        group.setText("Names and Paths");
        group.setLayout(new LatticeLayout(gsize));
        group.setLayoutData(new LatticeData("1, 1, 1, 1"));
        CLabel iname = new CLabel(group, SWT.NONE);
        iname.setText("Project Name");
        iname.setLayoutData(new LatticeData("1, 1, 1, 1"));
        Globals.applyBoldFont(iname);
        projText = new Text(group, SWT.BORDER | SWT.FLAT);
        projText.setLayoutData(new LatticeData("1, 3, 3, 3"));
        if (edit) projText.setText(Globals.ACTIVE_PROJECT.getProjectName());
        CLabel ipath = new CLabel(group, SWT.NONE);
        ipath.setText("Project Root Directory");
        ipath.setLayoutData(new LatticeData("1, 5, 1, 5"));
        Globals.applyBoldFont(ipath);
        projLoc = new Text(group, SWT.BORDER | SWT.FLAT);
        projLoc.setLayoutData(new LatticeData("1, 7, 3, 7"));
        if (edit) projLoc.setText(Globals.ACTIVE_PROJECT.getHomeDirectory().getAbsolutePath());
        Button dot1 = new Button(group, SWT.FLAT);
        dot1.setText("...");
        dot1.setLayoutData(new LatticeData("5, 7, 5, 7"));
        dot1.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DirectoryDialog dd = new DirectoryDialog(Main.shell, SWT.NONE);
                dd.setMessage("Select directory where you want to store your project files");
                String path = dd.open();
                if (path != null) projLoc.setText(path);
            }
        });
        CLabel liblab = new CLabel(group, SWT.NONE);
        liblab.setText("Lua Libraries (Interface.zip, etc)");
        liblab.setLayoutData(new LatticeData("1, 9, 1, 9"));
        Globals.applyBoldFont(liblab);
        list = new List(group, SWT.MULTI | SWT.BORDER);
        list.setLayoutData(new LatticeData("1, 11, 3, 14"));
        if (edit) {
            java.util.List libraries = Globals.ACTIVE_PROJECT.getLibraries();
            for (int i = 0; i < libraries.size(); i++) {
                Library lib = (Library) libraries.get(i);
                list.add(lib.getFile().getAbsolutePath());
            }
        }
        Button plus = new Button(group, SWT.FLAT);
        plus.setText(" + ");
        plus.setLayoutData(new LatticeData("5, 11, 5, 11"));
        plus.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                FileDialog openDialog = new FileDialog(Main.shell, SWT.OPEN | SWT.MULTI);
                openDialog.setFilterNames(new String[] { "WoW Interface Zip (Interface.zip)", "LUA Files (*.lua)", "All Files (*.*)" });
                openDialog.setFilterExtensions(new String[] { "*.zip", "*.lua", "*.*" });
                openDialog.setText("Add Library File");
                String fileName = openDialog.open();
                String files[] = openDialog.getFileNames();
                if (fileName == null || files == null || files.length == 0) return;
                String existing[] = list.getItems();
                Vector ex = new Vector();
                for (int i = 0; i < existing.length; i++) {
                    ex.add(new File(existing[i]));
                }
                for (int i = 0; i < files.length; i++) {
                    String path = fileName.replaceAll(files[i], "");
                    File f = new File(path + files[i]);
                    if (!ex.contains(f)) list.add(f.getAbsolutePath()); else {
                        MessageDialog.openInformation(dialog, "Library Exists", "The library '" + f.getAbsolutePath() + "' already exists.");
                    }
                }
            }
        });
        Button minus = new Button(group, SWT.FLAT);
        minus.setText(" - ");
        minus.setLayoutData(new LatticeData("5, 13, 5, 13"));
        minus.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (list.getSelectionCount() == 0) return;
                int[] sel = list.getSelectionIndices();
                for (int i = 0; i < sel.length; i++) {
                    list.remove(sel[i]);
                }
            }
        });
        final Button addWow = new Button(group, SWT.CHECK);
        addWow.setText("Add World of Warcraft interface resource");
        addWow.setLayoutData(new LatticeData("1, 16, 1, 16"));
        addWow.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                useWowResource = addWow.getSelection();
            }
        });
        if (edit) {
            useWowResource = (Globals.ACTIVE_PROJECT.getImportWowResource() != null);
            addWow.setSelection(useWowResource);
        }
        tab.setControl(comp);
    }

    private static Composite getTopComposite(Image image, Composite parent, String text) {
        Composite comp = new Composite(parent, SWT.FLAT);
        comp.setBackground(ColorManager.getInstance().getWhite());
        double size[][] = new double[][] { { 10, 320, LatticeLayout.FILL, LatticeLayout.PREFERRED }, { LatticeLayout.PREFERRED, 2 } };
        comp.setLayout(new LatticeLayout(size));
        Label imLabel = new Label(comp, SWT.RIGHT);
        imLabel.setLayoutData(new LatticeData("3, 0, 3, 0"));
        imLabel.setImage(image);
        imLabel.setBackground(ColorManager.getInstance().getWhite());
        CLabel textLabel = new CLabel(comp, SWT.LEFT);
        textLabel.setLayoutData(new LatticeData("1, 0, 1, 0"));
        textLabel.setText(text);
        textLabel.setBackground(ColorManager.getInstance().getWhite());
        Globals.applyBoldFont(textLabel);
        Globals.applyFontSize(textLabel, 14);
        textLabel.setForeground(ColorManager.getInstance().getColor(new RGB(180, 180, 180)));
        Composite bborder = new Composite(comp, SWT.NONE);
        bborder.setBackground(ColorManager.getInstance().getBlack());
        bborder.setLayoutData(new LatticeData("0, 1, 3, 1"));
        return comp;
    }
}
