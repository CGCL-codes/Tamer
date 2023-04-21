package client;

import Send.SendStatus;
import java.net.*;
import java.io.*;

/**
 *
 * @author K.R.
 */
public class TEST {

    /**
     * Konstruktor zum Testen
     */
    TEST() throws IOException, ClassNotFoundException {
        Send.SendStatus CarePacket = new SendStatus("Tomas", "12345");
        Socket client = new Socket("localhost", 2332);
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        output.writeObject(CarePacket);
        output.flush();
        SendStatus answer = (Send.SendStatus) input.readObject();
        System.out.print(answer.isLoginSuccessfull());
        client.close();
        input.close();
        output.close();
    }

    public static void main(String[] args) {
        try {
            TEST test = new TEST();
        } catch (IOException e) {
            System.out.print(e);
        } catch (ClassNotFoundException e) {
            System.out.print(e);
        }
    }
}
