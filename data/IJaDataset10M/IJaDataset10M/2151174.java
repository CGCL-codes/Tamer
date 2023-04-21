package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatServerListener extends Thread {

    private BufferedReader m_Reader;

    public ChatServerListener(Socket socket) {
        try {
            m_Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String line = null;
        if (m_Reader == null) {
            return;
        }
        try {
            while (true) {
                while ((line = m_Reader.readLine()) != null) {
                    System.out.println(line);
                    SimpleChatClient.m_messageList.offer(line);
                }
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            System.out.println("Runlistener exited. " + ex.toString());
        }
    }
}
