package projectviewer.importer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.gjt.sp.jedit.jEdit;
import projectviewer.ProjectViewer;
import projectviewer.vpt.VPTFile;
import projectviewer.vpt.VPTNode;
import projectviewer.vpt.VPTProject;
import projectviewer.vpt.VPTDirectory;

/**
 *	Re-imports files and/or directories from the project root and from other
 *	nodes that are not under the root. Re-importing from nodes not under the
 *	root works as following: if the directory does not exist, the file nodes
 *	below it are checked to see if they still exist, and removed if they don't;
 *	if the directory exists, the importing method chosen by the user is used
 *	to re-import the directory. These actions take place recursively.
 *
 *	@author		Marcelo Vanzin
 *	@version	$Id: ReImporter.java 6270 2004-07-24 19:39:14Z vanza $
 */
public class ReImporter extends RootImporter {

    /**
	 *	Creates a ReImport object. Most of the functionality is inherited from
	 *	the RootImporter class.
	 */
    public ReImporter(VPTNode node, ProjectViewer viewer) {
        super(node, viewer, true);
    }

    /**
	 *	Uses the user options from the RootImporter and re-imports the nodes
	 *	not under the root.
	 */
    protected Collection internalDoImport() {
        if (selected.isProject()) {
            super.internalDoImport();
            for (int i = 0; i < project.getChildCount(); i++) {
                VPTNode node = (VPTNode) project.getChildAt(i);
                String path = node.getNodePath();
                if (!path.startsWith(project.getRootPath())) {
                    if (node.isFile()) {
                        if (!((VPTFile) node).getFile().exists()) {
                            unregisterFile((VPTFile) node);
                            project.remove(i--);
                        }
                    } else if (node.isDirectory()) {
                        reimportDirectory((VPTDirectory) node);
                    }
                }
            }
        } else if (defineFileFilter(selected.getName(), false)) {
            String state = viewer.getFolderTreeState(selected);
            reimportDirectory((VPTDirectory) selected);
            postAction = new NodeStructureChange(selected, state);
        }
        return null;
    }

    private void reimportDirectory(VPTDirectory dir) {
        if (dir.getFile().exists()) {
            unregisterDir(dir);
            addTree(dir.getFile(), dir, fnf);
        } else {
            ArrayList toRemove = null;
            for (int i = 0; i < dir.getChildCount(); i++) {
                VPTNode node = (VPTNode) dir.getChildAt(i);
                if (node.isFile()) {
                    if (!((VPTFile) node).getFile().exists()) {
                        unregisterFile((VPTFile) node);
                        dir.remove(i--);
                    }
                } else if (node.isDirectory()) {
                    reimportDirectory((VPTDirectory) node);
                }
            }
        }
    }

    /**
	 *	Unregisters all files in the directory from the project, recursively,
	 *	and removes the child nodes from the parent.
	 */
    protected void unregisterDir(VPTDirectory dir) {
        for (int i = 0; i < dir.getChildCount(); i++) {
            VPTNode n = (VPTNode) dir.getChildAt(i);
            if (n.isDirectory()) {
                VPTDirectory cdir = (VPTDirectory) n;
                if (cdir.getFile().exists() && cdir.getFile().getParent().equals(dir.getNodePath())) {
                    unregisterFiles((VPTDirectory) n);
                    dir.remove(i--);
                } else {
                    reimportDirectory(cdir);
                }
            } else if (n.isFile()) {
                unregisterFile((VPTFile) n);
                dir.remove(i--);
            }
        }
    }
}
