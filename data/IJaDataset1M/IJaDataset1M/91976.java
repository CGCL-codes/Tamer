package br.usjt.smartzap.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EscutaTcpServer extends Thread {

    Socket socket;

    private ObjectInputStream objectInput;

    private ObjectOutputStream objectOutput;

    EscutaTcpServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.flush();
            objectInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Exception creating new Input/output Streams: " + e);
            return;
        }
        try {
            String message = (String) objectInput.readObject();
            System.out.println("Recebido: " + message);
            MensagemRequisicao request = new MensagemRequisicao(message);
            MensagemResposta mensagemResposta = SmartZapServer.processarRequisicao(request);
            objectOutput.writeObject(mensagemResposta.messageToString());
            objectOutput.flush();
        } catch (IOException e) {
            System.err.println("Exception reading/writing  Streams: " + e);
            return;
        } catch (ClassNotFoundException o) {
            System.err.println("CLASS NOT FOUND: " + o);
        } finally {
            try {
                objectOutput.close();
                objectInput.close();
            } catch (Exception e) {
                System.err.println("Exception closing  Streams: " + e);
            }
        }
    }
}
