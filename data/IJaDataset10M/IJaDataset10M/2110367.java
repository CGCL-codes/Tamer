package Logic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Connection {

    private Language language = null;

    private OutputStream outputStream;

    Logger log = Logger.getLogger(Connection.class);

    public Connection(Language varLanguage, Socket socket) {
        this.language = varLanguage;
        try {
            outputStream = socket.getOutputStream();
            log.trace(language.getTranslation("connection.opened"));
        } catch (IOException e) {
            log.warn(language.getTranslation("connection.openfailed"));
            try {
                socket.close();
            } catch (IOException ex) {
                log.warn(language.getTranslation("connection.closefailed"));
            }
        }
    }

    public boolean send(String message) {
        boolean successfull;
        try {
            outputStream.write((message + "\r\n").getBytes());
            successfull = true;
        } catch (IOException e) {
            successfull = false;
            log.warn(language.getTranslation("connection.failed"));
        }
        return successfull;
    }
}
