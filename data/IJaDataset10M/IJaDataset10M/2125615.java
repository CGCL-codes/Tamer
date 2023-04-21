package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.commons.io.IOUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionImportComFile;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.threads.communicator.progress.CommunicatorDefaultProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;

public class CommunicatorClient extends Thread {

    private Socket socket;

    private BufferedReader reader;

    public CommunicatorClient(ThreadGroup group, Socket socket) {
        super(group, "CommunicatorClient");
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            this.socket = null;
            this.reader = null;
        }
    }

    @Override
    public void run() {
        if (this.socket == null) return;
        try {
            String data = null;
            StringBuffer buffer = new StringBuffer();
            while ((data = this.reader.readLine()) != null) {
                buffer.append(data.trim() + "\n");
                if (data.trim().endsWith("</com>")) {
                    this.handleMessage(buffer.toString());
                    buffer = new StringBuffer();
                }
            }
        } catch (IOException e) {
        } finally {
            this.socket = null;
            this.reader = null;
        }
    }

    private void handleMessage(final String message) {
        TUSwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    ComBean bean = ComBean.decodeFromXML(IOUtils.toInputStream(message, "UTF-8"));
                    ActionImportComFile.importComBean(bean);
                    return;
                } catch (Exception e) {
                }
                Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(Translations.getString("error.unknown_message_format")));
                GuiLogger.getLogger().warning("Unknown message format");
            }
        });
    }
}
