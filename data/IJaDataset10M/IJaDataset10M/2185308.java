package org.multihelp.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import org.multihelp.HelpViewer;

/**
 * HTMLFile
 *
 * FileNode class responsible for modeling HTML files.
 *
 * @author Daniel McEnnis
 */
public class HTMLFile extends FileNode {

    private File fileLocation;

    private boolean isDirectory;

    private Icon icon = null;

    private String text = "";

    private Filter filter = new Filter();

    /**
	 * Constructor setting the text and icon for HTML file display.
	 */
    public HTMLFile(File file) {
        super(file);
        fileLocation = file;
        isDirectory = file.isDirectory();
        if (file.getName().endsWith(".html")) {
            icon = new ImageIcon("icons/html.png");
            text = file.getName().substring(0, file.getName().lastIndexOf(".html"));
        } else if (file.getName().endsWith(".xml")) {
            icon = new ImageIcon("icons/xml.png");
            text = file.getName().substring(0, file.getName().lastIndexOf(".xml"));
        } else {
            text = file.getName();
        }
    }

    /**
	 * Display the HTML page
	 *
	 * @see org.multihelp.file.FileNode#setPage(org.multihelp.HelpViewer)
	 */
    public void setPage(HelpViewer viewer) {
        try {
            if (isDirectory) {
                File loc = new File(fileLocation.getCanonicalPath() + File.pathSeparator + "index.html");
                if (loc.exists()) {
                    viewer.setPage(loc.toURI().toURL());
                } else {
                    HTMLDocument doc = new HTMLDocument();
                    try {
                        doc.insertString(0, generateDefaultIndex(), null);
                        doc.setBase(this.fileLocation.toURI().toURL());
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                    viewer.setContentType("text/html");
                    viewer.setDocument(doc);
                }
            } else {
                viewer.setPage(fileLocation.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
	 * Inner class implenting a custom file filter class used to filter entries in the FileTreeReader
	 */
    public class Filter implements FileFilter {

        /** 
		 * Filters out non-HTML and non-XML files as well as excluding the index.html file.
		 * @see java.io.FileFilter#accept(java.io.File)
		 */
        public boolean accept(File pathname) {
            if (pathname.getName().endsWith(".html") || pathname.getName().endsWith(".htm")) {
                if (pathname.getName().equals("index.html")) {
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    /**
	 * Provide a default entry if the file or directory is malformed or missing the appropriate entries.
	 */
    protected String generateDefaultIndex() {
        if (fileLocation.isDirectory() && (fileLocation.listFiles().length > 0)) {
            File[] list = fileLocation.listFiles();
            StringBuffer ret = new StringBuffer("<html><body><ul>");
            for (int i = 0; i < list.length; ++i) {
                ret.append("<li><a href=\"" + list[i].getName() + "\">" + list[i].getName() + "</a></li>");
            }
            ret.append("<ul></body></html>");
            return ret.toString();
        } else if (fileLocation.isDirectory()) {
            return "<html><body>ERROR: Empty directory in the help system.</body></html>";
        } else {
            return "<html><body>INTERNAL ERROR: Loading non-existant file</body></html>";
        }
    }

    /**
	 * Descend directories, populating the FileNode hierarchy.
	 *
	 * @see org.multihelp.file.FileNode#traverseFileSystem(java.io.File, int)
	 */
    public void traverseFileSystem(File root, int depth) {
        if (depth < 512) {
            File[] children = root.listFiles();
            if ((children != null) && (children.length != 0)) {
                for (int i = 0; i < children.length; ++i) {
                    if (filter.accept(children[i])) {
                        FileNode childNode = FileNode.determineType(children[i]);
                        this.add(childNode);
                        childNode.setParent(this);
                        childNode.traverseFileSystem(children[i], depth + 1);
                    }
                }
            }
        }
    }

    /**
	 * Provide the icon loaded in the constructor.
	 *
	 * @see org.multihelp.file.FileNode#getIcon()
	 */
    public Icon getIcon() {
        return icon;
    }

    /**
	 * Provides the text loaded in the constructor.
	 *
	 * @see org.multihelp.file.FileNode#getText()
	 */
    public String getText() {
        return text;
    }
}
