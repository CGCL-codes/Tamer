package hu.ihash.apps.dupecompare.view.files;

import hu.ihash.common.model.folder.FolderModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class FileListView extends JList {

    public FileListView() {
        this(new FolderModel());
    }

    public FileListView(FolderModel model) {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setOpaque(false);
        setModel(model);
        setVisibleRowCount(0);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setCellRenderer(new FileModernRenderer());
        setDoubleBuffered(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                if (me.getClickCount() == 2) {
                    FolderModel fm = (FolderModel) getModel();
                    int selindex = locationToIndex(me.getPoint());
                    File selected = fm.getElementAt(selindex);
                    fm.changeDir(selected);
                }
            }
        });
    }
}
