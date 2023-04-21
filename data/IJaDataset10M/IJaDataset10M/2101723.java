package mas.shared.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import mas.shared.Request;
import mas.shared.Response;

/**
 *
 * @author liese
 */
public class RequestHandler {

    private String host;

    private int port;

    private Socket socket;

    public RequestHandler(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public Response sendRequest(Request request) throws Exception {
        System.err.println("Connecting " + host + ":" + port);
        Socket clientSocket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        System.err.println(">>> " + request.getCommand());
        out.writeObject(request);
        out.flush();
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        Response response = (Response) in.readObject();
        System.err.println("<<< " + response.getCommand());
        clientSocket.close();
        return response;
    }

    public Request readRequest() throws Exception {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        Request request = (Request) in.readObject();
        System.err.println("<<< " + request.getCommand());
        return request;
    }

    public void sendResponse(Response response) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        System.err.println(">>> " + response.getCommand());
        out.writeObject(response);
        out.flush();
    }
}
