package au.gov.naa.digipres.xena.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import au.gov.naa.digipres.xena.litegui.LiteMainFrame;
import au.gov.naa.digipres.xena.litegui.NormalisationItemsListModel;
import au.gov.naa.digipres.xena.litegui.NormalisationItemsListRenderer;

public class FileAndDirectorySelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String LAST_DIR_VISITED_KEY = "dir/lastvisited";

    private NormalisationItemsListModel itemListModel;

    private JList itemList;

    private Preferences prefs;

    private FileFilter filter;

    /**
	 * This constructor will use a null FileFilter.
	 *
	 */
    public FileAndDirectorySelectionPanel() {
        this(null);
    }

    public FileAndDirectorySelectionPanel(FileFilter filter) {
        this.filter = filter;
        prefs = Preferences.userNodeForPackage(LiteMainFrame.class);
        initGUI();
    }

    private void initGUI() {
        setLayout(new GridBagLayout());
        itemListModel = new NormalisationItemsListModel();
        itemList = new JList(itemListModel);
        itemList.setCellRenderer(new NormalisationItemsListRenderer());
        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane itemListSP = new JScrollPane(itemList);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        Font buttonFont = new JButton().getFont().deriveFont(13.0f);
        JButton addFilesButton = new JButton("Add Files");
        addFilesButton.setFont(buttonFont);
        JButton addDirButton = new JButton("Add Directory");
        addDirButton.setFont(buttonFont);
        JButton removeButton = new JButton("Remove");
        removeButton.setFont(buttonFont);
        buttonPanel.add(addFilesButton);
        buttonPanel.add(addDirButton);
        buttonPanel.add(removeButton);
        addToGridBag(this, itemListSP, 0, 0, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        addToGridBag(this, buttonPanel, 1, 0, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 8), 0, 0);
        addFilesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doAddItems(true);
            }
        });
        addDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doAddItems(false);
            }
        });
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doRemoveItems();
            }
        });
    }

    /**
	 * Add items to the Normalisation Items List. If useFileMode is true,
	 * then the file chooser is set to FILES_ONLY, otherwise the file chooser
	 * is set to DIRECTORIES_ONLY.
	 * 
	 * @param useFileMode True if adding files, false if adding directories
	 */
    private void doAddItems(boolean useFileMode) {
        MemoryFileChooser fileChooser = new MemoryFileChooser();
        String memoryFileChooserFieldName = "";
        if (useFileMode) {
            if (filter != null) {
                fileChooser.setFileFilter(filter);
            }
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            memoryFileChooserFieldName = "FileChooser";
        } else {
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            memoryFileChooserFieldName = "DirChooser";
        }
        int retVal = fileChooser.showOpenDialog(this, this.getClass(), memoryFileChooserFieldName);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            if (useFileMode) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    itemListModel.addElement(file);
                }
            } else {
                itemListModel.addElement(fileChooser.getSelectedFile());
            }
            prefs.put(LAST_DIR_VISITED_KEY, fileChooser.getCurrentDirectory().getPath());
        }
    }

    /**
	 * Remove an item or items from the Normalise Items List
	 *
	 */
    private void doRemoveItems() {
        int[] selectedIndices = itemList.getSelectedIndices();
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            itemListModel.remove(selectedIndices[i]);
        }
    }

    /**
	 * Convenience method for adding a component to a container
	 * which is using a GridBagLayout
	 * 
	 * @param container
	 * @param component
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param weightx
	 * @param weighty
	 * @param anchor
	 * @param fill
	 * @param insets
	 * @param ipadx
	 * @param ipady
	 */
    private void addToGridBag(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady);
        container.add(component, gbc);
    }

    public List<File> getAllItems() {
        return itemListModel.getNormalisationItems();
    }

    public void clear() {
        itemListModel.removeAllElements();
    }
}
