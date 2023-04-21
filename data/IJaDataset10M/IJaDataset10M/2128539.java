package messenger;

import java.util.logging.Logger;

public class MessengerServerDeamon implements Runnable {

    private MessengerServer server = null;

    private Logger log = Logger.getLogger("MessengerServer");

    public MessengerServerDeamon(MessengerServer server) {
        this.server = server;
    }

    public void run() {
        log.info("MessengerServerDeamon is started");
        while (server != null && server.isAction()) {
            MessengerSession session;
            try {
                session = server.waitConnection();
                session.setServerSession(true);
            } catch (MessengerException e) {
                e.printStackTrace();
                break;
            }
            Thread tr = new Thread(new MessengerThread(session, server));
            tr.start();
        }
        log.info("MessengerServerDeamon is stopped");
    }
}
