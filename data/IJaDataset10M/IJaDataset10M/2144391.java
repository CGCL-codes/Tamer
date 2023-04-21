package com.magic.magicstore.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import com.magic.magicstore.core.protocol.Protocol;

/**
 * �ͻ���Socket
 * 
 * @author Administrator
 * 
 */
public class SocketClient {

    private Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    private String host = "localhost";

    public SocketClient() {
    }

    public SocketClient(String host) {
        this.host = host;
    }

    public void createConnection() throws UnknownHostException, IOException {
        socket = new Socket(host, 7777);
        System.out.println("Established a connection...");
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String sendData(String data) throws IOException {
        out.println(data);
        out.flush();
        return in.readLine();
    }

    public void closeConnection() throws IOException {
        out.println("bye");
        out.flush();
        out.close();
        in.close();
        socket.close();
    }

    public String login(String code, String password) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Protocol.ACTION_KEY, "loginaction.login");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", code);
        data.put("password", password);
        map.put(Protocol.DATA_KEY, data);
        return sendData(JSONObject.toJSONString(map));
    }

    public String logout() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Protocol.ACTION_KEY, "loginaction.logout");
        Map<String, Object> data = new HashMap<String, Object>();
        map.put(Protocol.DATA_KEY, data);
        return sendData(JSONObject.toJSONString(map));
    }

    /**
	 * ������
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            SocketClient client = new SocketClient();
            client.createConnection();
            System.out.println(client.login("U001", "123456"));
            client.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
