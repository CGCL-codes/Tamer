package net.sourceforge.parser.mkv.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import net.sourceforge.parser.mkv.matroska;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: $
 */
public class FileActionListener implements ActionListener {

    private MkvViewer mkvViewer;

    private File current_dir = null;

    FileActionListener(MkvViewer mkvViewer) {
        this.mkvViewer = mkvViewer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String command = null;
        if (source instanceof JMenuItem) {
            JMenuItem item = (JMenuItem) e.getSource();
            command = item.getActionCommand();
        } else if (source instanceof JButton) {
            JButton item = (JButton) e.getSource();
            command = item.getActionCommand();
        }
        if (command != null) {
            if (command.equals(MkvViewer.OPEN_FILE_COMMAND)) {
                JFileChooser chooser = new JFileChooser(current_dir);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.addChoosableFileFilter(new MediaFileFilter());
                int ret = chooser.showOpenDialog(mkvViewer);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    current_dir = chooser.getCurrentDirectory();
                    final File file = chooser.getSelectedFile();
                    try {
                        new FileReadProgress(mkvViewer, file.length());
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                matroska matroska = mkvViewer.getMatroska();
                                DefaultMutableTreeNode root;
                                try {
                                    matroska.close();
                                    root = matroska.parseAll(file);
                                    DefaultTreeModel model = (DefaultTreeModel) mkvViewer.getTree().getModel();
                                    model.setRoot(root);
                                    mkvViewer.enableOpenFile(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    class MediaFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".mkv");
        }

        @Override
        public String getDescription() {
            return "*.mkv files";
        }
    }
}
