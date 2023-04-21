package hu.scytha.action;

import hu.scytha.common.Messages;
import hu.scytha.main.Scytha;
import hu.scytha.plugin.IFile;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;

public class InvertSelectionAction extends Action {

    public InvertSelectionAction() {
        setText(Messages.getString("InvertSelectionAction.action"));
        setToolTipText(Messages.getString("InvertSelectionAction.tooltip"));
    }

    @Override
    public void run() {
        List<IFile> listOfAllFiles = Scytha.getWindow().getSelectedFilePanel().getFileViewer().getFiles();
        List<IFile> listOfSelectedFiles = Scytha.getWindow().getSelectedFilePanel().getFileViewer().getSelectedFiles();
        List<IFile> newSelection = new ArrayList<IFile>();
        for (IFile file : listOfAllFiles) {
            if (!file.getName().equals("..") && !listOfSelectedFiles.contains(file)) {
                newSelection.add(file);
            }
        }
        Scytha.getWindow().getSelectedFilePanel().getFileViewer().setSelectedItems(newSelection);
    }
}
