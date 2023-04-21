package net.deytan.tagger.ui.directories;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import net.deytan.tagger.controller.ExceptionController;
import net.deytan.tagger.controller.FileSystemController;
import net.deytan.tagger.controller.PropertiesController;
import net.deytan.tagger.controller.SongController;
import net.deytan.tagger.exception.FileException;
import net.deytan.tagger.exception.SongException;
import net.deytan.tagger.ui.MainPanel;
import net.deytan.tagger.ui.local.LocalPanel;
import net.deytan.tagger.ui.net.NetPanel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TreePopupMenu extends JPopupMenu {

    @Autowired
    private PropertiesController propertiesController;

    @Autowired
    private ExceptionController exceptionController;

    @Autowired
    private FileSystemController fileSystemController;

    @Autowired
    private SongController songController;

    @Autowired
    private DirectoriesPanel directoriesPanel;

    @Autowired
    private MainPanel mainPanel;

    @Autowired
    private NetPanel netPanel;

    @Autowired
    private LocalPanel localPanel;

    public void init() throws FileException {
        JMenuItem menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.set_root.text"), this.fileSystemController.getIcon("directoriesPanel.button.set_root"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                TreePopupMenu.this.directoriesPanel.changeRootNode(TreePopupMenu.this.directoriesPanel.getSelectedNode());
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.refresh.text"), this.fileSystemController.getIcon("directoriesPanel.button.refresh"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                TreePopupMenu.this.directoriesPanel.refresh(TreePopupMenu.this.directoriesPanel.getSelectedNode());
                try {
                    TreePopupMenu.this.localPanel.refreshFiles(TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory());
                } catch (SongException exception) {
                    TreePopupMenu.this.exceptionController.addException("refresh failed", exception);
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.delete.text"), this.fileSystemController.getIcon("directoriesPanel.button.delete"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final DirectoryTreeNode node = TreePopupMenu.this.directoriesPanel.getSelectedNode();
                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(TreePopupMenu.this.mainPanel.getComponent(), TreePopupMenu.this.propertiesController.loadString0("directoriesPanel.button.delete.warning", node.getDirectoryPath()))) {
                    try {
                        TreePopupMenu.this.fileSystemController.deleteFile(node.getDirectory());
                        TreePopupMenu.this.directoriesPanel.setSelectedNode((DirectoryTreeNode) node.getParent());
                        TreePopupMenu.this.directoriesPanel.deleteNode(node);
                    } catch (FileException exception) {
                        TreePopupMenu.this.exceptionController.addException("Delete operation failed", exception);
                    }
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.explorer.text"), this.fileSystemController.getIcon("directoriesPanel.button.explorer"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Runtime.getRuntime().exec(new String[] { TreePopupMenu.this.propertiesController.loadString("directoriesPanel.button.explorer.exe"), TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectoryPath() });
                } catch (IOException exception) {
                    TreePopupMenu.this.exceptionController.addException("explorer failed", exception);
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.rename.text"), this.fileSystemController.getIcon("directoriesPanel.button.rename"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final File oldDirectory = TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory();
                final String newName = JOptionPane.showInputDialog(TreePopupMenu.this.propertiesController.loadString("directoriesPanel.button.rename.info"), oldDirectory.getName());
                if (StringUtils.isNotBlank(newName) && !oldDirectory.getName().equals(newName)) {
                    final File newDirectory = new File(oldDirectory.getParent(), newName);
                    try {
                        TreePopupMenu.this.fileSystemController.moveFile(TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory(), newDirectory);
                        TreePopupMenu.this.directoriesPanel.getSelectedNode().setUserObject(newDirectory);
                        TreePopupMenu.this.directoriesPanel.renameNode(TreePopupMenu.this.directoriesPanel.getSelectedNode());
                    } catch (FileException exception) {
                        TreePopupMenu.this.exceptionController.addException("renaming failed", exception);
                    }
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.create.text"), this.fileSystemController.getIcon("directoriesPanel.button.create"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final File parent = TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory();
                final String name = JOptionPane.showInputDialog(TreePopupMenu.this.propertiesController.loadString("directoriesPanel.button.create.info"));
                if (StringUtils.isNotBlank(name)) {
                    final File newDirectory = new File(parent, name);
                    if (newDirectory.exists()) {
                        JOptionPane.showMessageDialog(TreePopupMenu.this.mainPanel.getComponent(), TreePopupMenu.this.propertiesController.loadString("directoriesPanel.button.create.error"), "", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            TreePopupMenu.this.fileSystemController.createDirectory(newDirectory);
                            TreePopupMenu.this.directoriesPanel.refresh(TreePopupMenu.this.directoriesPanel.getSelectedNode());
                        } catch (FileException exception) {
                            TreePopupMenu.this.exceptionController.addException("renaming failed", exception);
                        }
                    }
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.create_discs.text"), this.fileSystemController.getIcon("directoriesPanel.button.create_discs"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final File parent = TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory();
                final String number = JOptionPane.showInputDialog(TreePopupMenu.this.propertiesController.loadString("directoriesPanel.button.create_discs.info"));
                if (StringUtils.isNumeric(number)) {
                    for (int i = 1; i <= new Integer(number); i++) {
                        final File newDirectory = new File(parent, TreePopupMenu.this.songController.getDisc(i));
                        try {
                            TreePopupMenu.this.fileSystemController.createDirectory(newDirectory);
                        } catch (FileException exception) {
                            TreePopupMenu.this.exceptionController.addException("renaming failed", exception);
                        }
                    }
                    TreePopupMenu.this.directoriesPanel.refresh(TreePopupMenu.this.directoriesPanel.getSelectedNode());
                }
            }
        });
        this.add(menuItem);
        menuItem = new JMenuItem(this.propertiesController.loadString("directoriesPanel.button.create_m3u.text"), this.fileSystemController.getIcon("directoriesPanel.button.create_m3u"));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final File parent = TreePopupMenu.this.directoriesPanel.getSelectedNode().getDirectory();
                try {
                    TreePopupMenu.this.songController.writeM3uFileForMultipleCDs(TreePopupMenu.this.netPanel.getSelectedAlbum(), parent);
                } catch (SongException exception) {
                    TreePopupMenu.this.exceptionController.addException("renaming failed", exception);
                }
            }
        });
        this.add(menuItem);
    }
}
