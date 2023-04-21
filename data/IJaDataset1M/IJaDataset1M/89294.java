package com.vkdasari.youtube.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import com.vkdasari.youtube.model.Video;
import com.vkdasari.youtube.ui.MainPanel;
import com.vkdasari.youtube.ui.model.VideoUploadTableModel;
import com.vkdasari.youtube.util.FileUtil;

public class AddFileAction extends ZebraAction {

    private static final long serialVersionUID = 1L;

    final JFileChooser fc = new JFileChooser();

    private static final Logger log = Logger.getLogger(AddFileAction.class);

    public AddFileAction(String name, Icon icon) {
        super(name, icon);
        init();
    }

    private void init() {
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Video Files (avi, mp4)", "avi", "mp4");
        fc.setFileFilter(extensionFilter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VideoUploadTableModel uploadsTableModel = MainPanel.getInstance().getUploadsTableModel();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            for (File child : files) {
                if (log.isDebugEnabled()) {
                    log.debug("Adding file : " + child.getAbsolutePath());
                }
                if (".".equals(child.getName()) || "..".equals(child.getName())) {
                    continue;
                }
                Video video = new Video();
                video.setName(child.getName());
                video.setFilelocation(child.getAbsolutePath());
                video.setSize(FileUtil.getSize(child.length()));
                uploadsTableModel.addRow(video);
            }
        } else {
        }
        fc.setSelectedFiles(new File[0]);
        fc.setSelectedFile(new File(""));
    }
}
