package org.opennms.netmgt.provision.detector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.opennms.netmgt.provision.support.Client;

/**
 * @author Donald Desloge
 *
 */
public class MultilineOrientedClient implements Client<LineOrientedRequest, MultilineOrientedResponse> {

    protected Socket m_socket;

    private OutputStream m_out;

    private BufferedReader m_in;

    public void close() {
        System.out.println("Closing Socket");
        Socket socket = m_socket;
        m_socket = null;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    public void connect(InetAddress address, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), timeout);
        socket.setSoTimeout(timeout);
        setInput(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        setOutput(socket.getOutputStream());
        m_socket = socket;
    }

    public MultilineOrientedResponse receiveBanner() throws IOException {
        return receiveResponse();
    }

    public MultilineOrientedResponse sendRequest(LineOrientedRequest request) throws IOException {
        request.send(getOutput());
        return receiveResponse();
    }

    /**
     * @return
     * @throws IOException
     */
    private MultilineOrientedResponse receiveResponse() throws IOException {
        MultilineOrientedResponse response = new MultilineOrientedResponse();
        response.receive(getInput());
        return response;
    }

    public void setInput(BufferedReader in) {
        m_in = in;
    }

    public BufferedReader getInput() {
        return m_in;
    }

    public void setOutput(OutputStream out) {
        m_out = out;
    }

    public OutputStream getOutput() {
        return m_out;
    }
}
