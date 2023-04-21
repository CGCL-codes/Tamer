package dialog;

public class ClientSender implements Runnable {

    private Client client;

    public ClientSender(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        out("gui");
        while (true) {
            out(client.popMessage());
        }
    }

    private synchronized void out(String msg) {
        if (!client.user.socket.isClosed()) {
            client.user.pw.println(msg);
            client.user.pw.flush();
        } else {
            System.out.println("Client socket is closed");
        }
    }
}
