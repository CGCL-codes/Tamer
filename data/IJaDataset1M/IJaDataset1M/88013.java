package br.com.visualmidia.core.server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import org.apache.log4j.Logger;
import br.com.visualmidia.business.FileDescriptorMD5;
import br.com.visualmidia.core.Constants;
import br.com.visualmidia.update.ListFilestoDownloadThroughCompareofMD5;

public class ClientUpdateStack12 {

    private Socket socket;

    private static final Logger logger = Logger.getLogger(ClientUpdateStack12.class);

    public ClientUpdateStack12(Socket socket) {
        this.socket = socket;
        initCommunication();
    }

    @SuppressWarnings("unchecked")
    private void initCommunication() {
        Communicate communicate = new Communicate(socket);
        try {
            communicate.send("316, vou te enviar a pasta e os arquivos");
            communicate.send(Constants.CURRENT_DIR + Constants.FILE_SEPARATOR + "GerenteDigital.jar");
            communicate.sendObject(new ListFilestoDownloadThroughCompareofMD5().getListOfFilesFromServer(Constants.CURRENT_DIR + Constants.FILE_SEPARATOR + "GerenteDigital.jar"));
            if (communicate.receive().equals("317")) {
                communicate.send("318, Me mande os arquivos para atualizar");
                List<FileDescriptorMD5> listOfFilesfromClientToBeUpdated = (List<FileDescriptorMD5>) communicate.receiveObject();
                for (FileDescriptorMD5 filetoUpdate : listOfFilesfromClientToBeUpdated) {
                    communicate.send("319, Enviando Arquivo: " + filetoUpdate.getLocation() + Constants.FILE_SEPARATOR + filetoUpdate.getName());
                    File file = new File(filetoUpdate.getLocation() + Constants.FILE_SEPARATOR + filetoUpdate.getName());
                    communicate.sendObject(filetoUpdate);
                    if (file.exists()) {
                        communicate.sendFile(file);
                    }
                }
                communicate.send("320, Acabou");
            }
            socket.close();
        } catch (IOException e) {
            logger.error("Close Soket Error: ", e);
        }
    }
}
