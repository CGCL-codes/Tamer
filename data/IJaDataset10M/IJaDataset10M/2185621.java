package net.sf.refactorit.jbuilder.optionsui;

import com.borland.primetime.vfs.Url;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import net.sf.refactorit.common.util.FileExtensionFilter;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.jbuilder.vfs.JBSource;
import net.sf.refactorit.ui.dialog.RitDialog;
import net.sf.refactorit.ui.options.JSourcepathChooser;
import net.sf.refactorit.ui.options.Path;
import net.sf.refactorit.ui.options.SourcePath;
import net.sf.refactorit.ui.options.TreeChooser;
import net.sf.refactorit.vfs.Source;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Extension of JSourcepathChooser that makes it work with Source objects as
 * path items instead of Strings. The <code>getPath</code>, <code>setPath</code>
 * and <code>fillAddButtonPanel</code> are overriden accordingly.
 *
 * @author juri
 */
public class JBSourcepathChooser extends JSourcepathChooser {

    static File lastDirectory;

    public JBSourcepathChooser() {
        super();
    }

    protected void fillAddButtonPanel(JPanel panel) {
        JButton addButton = new JButton(resLocalizedStrings.getString("pathchooser.add"));
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileFilter zipFilter = new FileExtensionFilter(new String[] { ".jar", ".zip" }, "Jar and Zip archives");
                chooser.setFileFilter(zipFilter);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                int choice = RitDialog.showFileDialog(IDEController.getInstance().createProjectContext(), chooser);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File[] selection = chooser.getSelectedFiles();
                    for (int i = 0; i < selection.length; i++) {
                        addPathItem(JBSource.getSource(selection[i]));
                    }
                }
            }
        });
        panel.add(addButton);
        JButton addFromFilesystems = new JButton("Add From Project Paths");
        addFromFilesystems.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JbUrlsTreeModel jbUrlsModel = new JbUrlsTreeModel();
                Object[] urls = TreeChooser.getNewDataObjectReferences(IDEController.getInstance().createProjectContext(), "Add from project paths", jbUrlsModel, new JbUrlTreeCellRenderer(jbUrlsModel));
                if (urls != null) {
                    for (int i = 0; i < urls.length; i++) {
                        addPathItem(JBSource.getSource((Url) urls[i]));
                    }
                }
            }
        });
    }

    public Path getPath() {
        if (data.isEmpty()) {
            return SourcePath.EMPTY;
        }
        StringBuffer buf = new StringBuffer();
        Enumeration e = data.elements();
        String pathElement = ((Source) e.nextElement()).getAbsolutePath();
        buf.append(pathElement);
        while (e.hasMoreElements()) {
            buf.append(File.pathSeparator);
            pathElement = ((Source) e.nextElement()).getAbsolutePath();
            buf.append(pathElement);
        }
        String path = buf.toString();
        return new SourcePath(path);
    }

    public void setPath(Path path) {
        data.removeAllElements();
        mode = SOURCEPATH;
        StringTokenizer t = new StringTokenizer(path.toString(), File.pathSeparator);
        Set redundancyFilter = new HashSet(t.countTokens());
        while (t.hasMoreTokens()) {
            redundancyFilter.add(t.nextToken());
        }
        Iterator i = redundancyFilter.iterator();
        while (i.hasNext()) {
            data.addElement(JBSource.getSource(new File((String) i.next())));
        }
        if (data.getSize() > 0) {
            list.setSelectedIndex(0);
        }
    }
}
