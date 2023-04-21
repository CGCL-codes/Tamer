package flames2d.gui.projectlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import flames2d.core.Project;
import flames2d.util.Messages;

/**
 * This class removes the selected items in the JList after an Action occurred.
 * @author AnjoVahldiek
 *
 */
public class RemoveFileFromListActionListener implements ActionListener {

    /**
	 * List where the selected elements will be deleted.
	 */
    private JList mList;

    /**
	 * Project will be kept updated during the deleting process to ensure
	 * that the list holds the same elements as the project object.
	 */
    private Project mProject;

    private JLabel count;

    /**
	 * Constructs an action listener which removes items from Jlist
	 * @param project Project will be kept updated during the deleting process to ensure that the list holds the same elements as the project object.
	 * @param list List where the selected elements will be deleted.
	 * @param count the file count label
	 */
    public RemoveFileFromListActionListener(Project project, JList list, JLabel count) {
        this.mList = list;
        this.mProject = project;
        this.count = count;
    }

    /**
	 * Removes the selected items of the JList (GUI) and also in the Project.
	 * @param e the action event
	 */
    public void actionPerformed(ActionEvent e) {
        int[] selected = this.mList.getSelectedIndices();
        DefaultListModel model = (DefaultListModel) this.mList.getModel();
        for (int i = 0; i < selected.length; i++) {
            model.remove(selected[i] - i);
        }
        this.mList.setModel(model);
        File[] inputFiles = new File[model.getSize()];
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            inputFiles[i] = ((FileComponent) model.get(i)).getFile();
        }
        this.count.setText(inputFiles.length + Messages.getString("InputProjectPanel.FileCount"));
        this.mProject.setInputImages(inputFiles);
    }
}
