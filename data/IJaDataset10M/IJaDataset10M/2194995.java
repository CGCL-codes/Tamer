package fr.upemlv.transfile.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Scanner;
import fr.upemlv.transfile.command.Command;
import fr.upemlv.transfile.command.CommandHandler;
import fr.upemlv.transfile.command.impl.AbstractCommand;
import fr.upemlv.transfile.command.impl.ListCommand;
import fr.upemlv.transfile.reply.Reply;
import fr.upemlv.transfile.reply.ReplyHandler;
import fr.upemlv.transfile.server.FactoryCommand;
import fr.upemlv.transfile.utils.ApplicationParameters;

public class FileClient {

    private final Charset encodage;

    private final SocketChannel socket;

    private ReplyReader replyReader;

    public FileClient(String server, int port) throws IOException {
        socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(server, port));
        encodage = Charset.forName("ISO-8859-1");
        replyReader = new ReplyReader(socket);
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        ApplicationParameters params = new ApplicationParameters(args);
        String[] letters = { "p", "a" };
        int[] plength = { 1, 1 };
        String[] descr = { "port", "address" };
        String[] optional = {};
        String str = params.verifyParameters(letters, plength, descr, optional);
        if (!str.equals("")) {
            System.out.print(str);
            return;
        }
        FileClient client = new FileClient(params.getParameter("a")[0], Integer.valueOf(params.getParameter("p")[0]));
        client.run();
    }

    public void run() throws IOException {
        try {
            System.out.println("Connexion etablie avec : " + socket.socket().getRemoteSocketAddress());
            Scanner scan = new Scanner(System.in);
            String line = "";
            ByteBuffer b1;
            System.out.println("Enter command:");
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                if (line.equals(".")) break;
                b1 = FactoryCommand.createCommandFromClient(line).getPacket();
                if (b1 != null) {
                    socket.write(b1);
                    b1.clear();
                    Reply reply = replyReader.readReply();
                    ReplyHandler replyHandler = reply.createReplyHandler();
                    if (replyHandler != null) {
                        replyHandler.handle();
                    }
                    System.out.println("Enter command:");
                }
            }
        } finally {
            socket.close();
        }
    }
}
