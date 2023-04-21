package com.mscg.jmp3.ui.listener.encode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.EncodeFilesDialog;
import com.mscg.jmp3.util.Util;

public class StartEncodingListener implements ActionListener {

    private EncodeFileCard encodeFileCard;

    public StartEncodingListener(EncodeFileCard encodeFileCard) {
        this.encodeFileCard = encodeFileCard;
    }

    public void actionPerformed(ActionEvent ev) {
        if (Util.isEmptyOrWhiteSpaceOnly(encodeFileCard.getDestinationFolder().getValue())) {
            MainWindowInterface.showError(new Exception(Messages.getString("operations.file.encode.destination.choose.empty")));
        } else {
            try {
                new EncodeFilesDialog(encodeFileCard, MainWindowInterface.getInstance()).setVisible(true);
            } catch (FileNotFoundException e) {
            }
        }
    }
}
