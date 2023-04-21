package gb.editor.file;

import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import com.loribel.commons.swing.GB_Frame;
import com.loribel.commons.swing.GB_Tree;
import com.loribel.commons.swing.tree.GB_DefaultTreeModel;

/**
 * Demo
 *
 * @author Gr�gory Borelli
 */
public abstract class GB_FileReadOnlyTNDemo {

    public static void main(String[] p) {
        JFileChooser l_fileChooser = new JFileChooser();
        l_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = l_fileChooser.showOpenDialog(null);
        if (r != JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        File l_file = l_fileChooser.getSelectedFile();
        GB_FileReadOnlyTN l_root = new GB_FileReadOnlyTN(l_file);
        GB_DefaultTreeModel l_model = new GB_DefaultTreeModel();
        l_model.setRoot(l_root);
        GB_Tree l_tree = new GB_Tree(l_model);
        l_tree.setRootVisible(true);
        GB_Frame l_frame = new GB_Frame();
        l_frame.setMainPanel(new JScrollPane(l_tree));
        l_frame.setSize(new Dimension(400, 600));
        l_frame.setVisible(true);
    }
}
