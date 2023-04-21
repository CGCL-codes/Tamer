package logger.sd.examples.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws Exception {
        String sentenca;
        String sentencaModificada;
        BufferedReader entradaDoUsuario = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream saidaParaServidor = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader entradaDoServidor = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentenca = entradaDoUsuario.readLine();
        saidaParaServidor.writeBytes(sentenca + '\n');
        sentencaModificada = entradaDoServidor.readLine();
        System.out.println("SERVIDOR: " + sentencaModificada);
        clientSocket.close();
    }
}
