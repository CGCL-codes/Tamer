package jvs.vfs.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import jvs.vfs.FileSystem;
import x.java.io.File;

/**
 * @author qiangli
 * 
 */
public class PropertiesDialog extends BasicDialog {

    private static final long serialVersionUID = 1L;

    private File file;

    private class OkAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent ae) {
            hide();
            option = APPROVE_OPTION;
        }
    }

    public PropertiesDialog(Frame owner, File file) {
        super(owner, true);
        this.file = file;
        buildUI();
    }

    protected void buildUI() {
        setTitle("Properties");
        JPanel custom = new JPanel();
        custom.setLayout(new BoxLayout(custom, BoxLayout.Y_AXIS));
        JLabel[] labels = { new JLabel("Path:"), new JLabel("Readable:"), new JLabel("Writable:"), new JLabel("Size:"), new JLabel("Modified:"), new JLabel("URI:"), new JLabel("Mount Point:") };
        JTextField[] fields = { new JTextField(file.getPath()), new JTextField(file.canRead() + ""), new JTextField(file.canWrite() + ""), new JTextField(file.length() + ""), new JTextField(new Date(file.lastModified()) + ""), new JTextField(file.toRealURI().toString()), new JTextField(FileSystem.getFileSystem().isMountPoint(file) + "") };
        for (int i = 0; i < fields.length; i++) {
            fields[i].setEditable(false);
        }
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        addLabelTextRows(labels, fields, gridbag, panel);
        custom.add(panel);
        custom.add(Box.createRigidArea(VGAP10));
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalGlue());
        okay = new JButton("Ok");
        okay.setEnabled(true);
        buttons.add(okay);
        buttons.add(Box.createRigidArea(HGAP10));
        buttons.add(Box.createHorizontalGlue());
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        custom.add(buttons);
        custom.add(Box.createRigidArea(VGAP10));
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        custom.setBorder(border);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(custom, BorderLayout.CENTER);
        getRootPane().setDefaultButton(okay);
        okay.addActionListener(new OkAction());
        pack();
        setLocationRelativeTo(getOwner());
    }
}
