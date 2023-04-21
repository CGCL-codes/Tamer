package com.organic.maynard.outliner.menus.file;

import com.organic.maynard.outliner.model.DocumentInfo;
import com.organic.maynard.outliner.model.propertycontainer.*;
import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.io.*;
import java.awt.event.*;
import javax.swing.*;

public class OpenFileMenuItem extends AbstractOutlinerMenuItem implements ActionListener {

    private FileProtocol protocol = null;

    public OpenFileMenuItem(FileProtocol protocol) {
        setProtocol(protocol);
        addActionListener(this);
    }

    public FileProtocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(FileProtocol protocol) {
        this.protocol = protocol;
        setText(protocol.getName());
    }

    public void actionPerformed(ActionEvent e) {
        openOutlinerDocument(getProtocol());
    }

    protected static void openOutlinerDocument(FileProtocol protocol) {
        DocumentInfo docInfo = new DocumentInfo();
        PropertyContainerUtil.setPropertyAsString(docInfo, DocumentInfo.KEY_PROTOCOL_NAME, protocol.getName());
        if (!protocol.selectFileToOpen(docInfo, FileProtocol.OPEN)) {
            return;
        }
        FileMenu.openFile(docInfo, protocol);
    }
}
