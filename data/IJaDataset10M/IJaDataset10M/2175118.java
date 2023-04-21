package terpword;

import java.awt.Container;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Internal FileDialog class used for opening/saving documents.
 */
public class FileDialog extends JDialog implements ActionListener {

    private EkitCore ParentEkit;

    private JList FileList;

    private String FileDir;

    private String[] Files;

    private String SelectedFile;

    /**
	 * @param parentEkit - the main EkitCore object.
	 * @param fileDir - the directory to look in.
	 * @param fileList - the list of selected files.
	 * @param title - the title of the dialog.
	 * @param modal - [boolean] whether or not the dialog is modal.
	 */
    public FileDialog(EkitCore parentEkit, String fileDir, String[] fileList, String title, boolean modal) {
        super(parentEkit.getFrame(), title, modal);
        FileDir = fileDir;
        Files = fileList;
        ParentEkit = parentEkit;
        init();
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getActionCommand().equals("save")) {
            setVisible(false);
        } else if (e.getActionCommand().equals("cancel")) {
            SelectedFile = null;
            setVisible(false);
        }
    }

    /**
	 *  Initialization function.
	 */
    public void init() {
        SelectedFile = "";
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setBounds(100, 100, 300, 200);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        FileList = new JList(Files);
        FileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        FileList.clearSelection();
        ListSelectionModel lsm = FileList.getSelectionModel();
        lsm.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ListSelectionModel sm = FileList.getSelectionModel();
                    if (!sm.isSelectionEmpty()) {
                        SelectedFile = Files[sm.getMinSelectionIndex()];
                    }
                }
            }
        });
        JScrollPane fileScrollPane = new JScrollPane(FileList);
        fileScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(fileScrollPane);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Files"));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        JButton saveButton = new JButton("Accept");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPane.add(centerPanel);
        contentPane.add(buttonPanel);
        setVisible(true);
    }

    /**
	 * @return the selected file.
	 */
    public String getSelectedFile() {
        if (SelectedFile != null) {
            SelectedFile = FileDir + "/" + SelectedFile;
        }
        return SelectedFile;
    }
}
