package org.aspentools.dormouse.vfs.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;

/**
 * This class contains the default implementation of the file tree model.
 * 
 * @author Mark Fortner
 * @version 1.0
 */
public class DefaultFileTreeModel extends DefaultTreeModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1904333963607025477L;

    protected DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    /**
	 * Use this contructor to create a tree with a root directory and a set of
	 * nodes for directories like Documents, Music, Pictures etc.
	 * 
	 * @param _root
	 *            The root directory
	 * @param _treeTypes
	 *            an array of directory constants defined in this class (i.e.
	 *            IMAGES, MUSIC, BOOKS, etc)
	 */
    public DefaultFileTreeModel(DefaultMutableTreeNode _root, int[] _treeTypes) {
        super(_root);
        init(_root, _treeTypes);
    }

    /**
	 * This constructor creates a file tree with the given directory.
	 * @param dir  a directory
	 */
    public DefaultFileTreeModel(FileObject dir) {
        super(new FileNode(dir));
        FileNode rootNode = (FileNode) this.getRoot();
        try {
            if (dir.getType() == FileType.FOLDER) {
                FileObject[] children = dir.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].getType() == FileType.FOLDER) {
                        rootNode.add(new FileNode(children[i]));
                    }
                }
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    public void init(DefaultMutableTreeNode root, int[] _treeTypes) {
        this.setRoot(root);
        FileNode home = createUserHomeNode();
        home.explore();
        root.add(home);
        String homeFolder = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        FileObject currFile = null;
        if (_treeTypes != null) {
            try {
                FileSystemManager mgr = VFS.getManager();
                for (int i = 0; i < _treeTypes.length; i++) {
                    switch(_treeTypes[i]) {
                        case IMAGES:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "images");
                                break;
                            }
                        case MUSIC:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "music");
                                break;
                            }
                        case BOOKS:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "ebooks");
                                break;
                            }
                        case NEWS:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "news");
                                break;
                            }
                        case BOOKMARKS:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "bookmarks");
                                break;
                            }
                        case MAIL:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "mail");
                                break;
                            }
                        case DOCS:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "documents");
                                break;
                            }
                        case MOVIES:
                            {
                                currFile = mgr.resolveFile(homeFolder + fileSeparator + "movies");
                                break;
                            }
                    }
                    if (currFile.exists()) {
                        root.add(new FileNode(currFile));
                    }
                }
            } catch (FileSystemException fse) {
                fse.printStackTrace();
            }
        }
    }

    /**
	 * This method creates the user home node in the tree.
	 * 
	 * @return
	 */
    protected FileNode createUserHomeNode() {
        FileNode home = null;
        FileObject homeFile = null;
        try {
            homeFile = VFS.getManager().resolveFile(System.getProperty("user.home"));
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        home = new FileNode(homeFile);
        return home;
    }

    /**
	 * An array of constants used to indicate the types of tree nodes
	 * that should be added to the tree.
	 */
    protected int[] treeTypes;

    /**
	 * This constant is used to specify that the IMAGES directory
	 * should be included in the tree.
	 */
    public static final int IMAGES = 0;

    /**
	 * This constant is used to specify that the MUSIC directory
	 * should be included in the tree.
	 */
    public static final int MUSIC = 1;

    /**
	 * This constant is used to specify that the MUSIC directory
	 * should be included in the tree.
	 */
    public static final int BOOKS = 2;

    /**
	 * This constant is used to specify that the NEWS directory
	 * should be included in the tree.
	 */
    public static final int NEWS = 3;

    /**
	 * This constant is used to specify that the BOOKMARKS directory
	 * should be included in the tree.
	 */
    public static final int BOOKMARKS = 4;

    /**
	 * This constant is used to specify that the MAIL directory
	 * should be included in the tree.
	 */
    public static final int MAIL = 5;

    /**
	 * This constant is used to specify that the FILES directory
	 * should be included in the tree.
	 */
    public static final int FILES = 6;

    /**
	 * This constant is used to specify that the MUSIC directory
	 * should be included in the tree.
	 */
    public static final int DOCS = 7;

    /**
	 * This constant is used to specify that the MUSIC directory
	 * should be included in the tree.
	 */
    public static final int MOVIES = 8;

    /**
	 * This constant is used to initialize the tree with all default
	 * root directories (i.e. IMAGES, MUSIC, etc).
	 */
    public static final int[] DEFAULT_ROOTS = { IMAGES, MUSIC, BOOKS, FILES, DOCS, MOVIES };
}
