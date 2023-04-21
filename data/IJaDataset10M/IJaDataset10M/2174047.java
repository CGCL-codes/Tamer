package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import com.mscg.jmp3.ui.listener.ListIndexAwareListener;

public class RemoveTransformationsListener extends GenericFilenameToTagListener implements ListIndexAwareListener {

    protected Integer index;

    public RemoveTransformationsListener(JList actionsList) {
        this(actionsList, null);
    }

    public RemoveTransformationsListener(JList actionsList, Integer index) {
        super(actionsList);
        setListIndex(index);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultListModel model = (DefaultListModel) actionsList.getModel();
        if (index == null) {
            int indexes[] = actionsList.getSelectedIndices();
            for (int i = indexes.length - 1; i >= 0; i--) {
                model.remove(indexes[i]);
            }
        } else {
            model.remove(index);
        }
    }

    @Override
    public void setListIndex(Integer index) {
        this.index = index;
    }
}
