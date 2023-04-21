package com.mscg.jmp3.ui.listener.filelist;

import javax.swing.JList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericFileListListener {

    protected Logger LOG;

    protected JList filesList;

    public GenericFileListListener(JList filesList) {
        LOG = LoggerFactory.getLogger(this.getClass());
        this.filesList = filesList;
    }
}
