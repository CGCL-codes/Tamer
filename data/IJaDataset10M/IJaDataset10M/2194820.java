package issrg.policytester;

import issrg.utils.gui.xml.NodeItemList;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.w3c.dom.Element;

public class FileLoader extends NodeItemList implements KeyListener {

    /**
     * File selected, so that the next time the file chooser is opened, 
     * it will remember the path.
     */
    File currentDirectory = null;

    String PARENT_TAG, fileExt, CHILD_TAG, fdesc;

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/policytester/PTComponent_i18n");

    String errorHeader = rbl.getString("ErrorHeader");

    String error1 = rbl.getString("FileList_Dialog_Error1");

    public FileLoader(XMLEditor xmlED) {
        super(xmlED);
        refreshView();
    }

    public void refreshView() {
    }

    /**
     * Gets the Current Directory variable
     *
     * @return the file holding the current directory.
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * The File Filter to use with the File Chooser that selects wsdls.
     * <p>
     * Sets the filechooser to display directory folders or .wsdl files only
     */
    FileFilter ff = new FileFilter() {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(fileExt) || f.isDirectory();
        }

        public String getDescription() {
            return fdesc + " Files (." + fileExt + ")";
        }
    };

    /**
     * Sets the Current Directory variable
     *
     * @param f   the file holding the current directory.
     */
    public void setCurrentDirectory(File f) {
        currentDirectory = f;
    }

    public boolean isInList(String fname) {
        for (int i = 0; i < getListData().length; i++) {
            String toCompare = ((String[]) getListData())[i];
            if (toCompare.equals(fname)) {
                return true;
            }
        }
        return false;
    }

    public void addItem() {
        JFileChooser fc = new JFileChooser();
        JDialog.setDefaultLookAndFeelDecorated(true);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (getCurrentDirectory() == null) fc.setCurrentDirectory(new File(".")); else fc.setCurrentDirectory(getCurrentDirectory());
        fc.setFileFilter(ff);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            setCurrentDirectory(fc.getCurrentDirectory());
            String filename = fc.getSelectedFile().getAbsolutePath();
            if (isInList(filename)) {
                JOptionPane.showMessageDialog(this, error1, errorHeader, JOptionPane.ERROR_MESSAGE);
                return;
            }
            Element child = xmlED.DOM.createElement(CHILD_TAG);
            child.setAttribute("Path", filename);
            xmlED.addItem(child, (Element) getParentNode());
        }
    }

    public void deleteItem() {
    }

    public void replaceItem() {
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}
