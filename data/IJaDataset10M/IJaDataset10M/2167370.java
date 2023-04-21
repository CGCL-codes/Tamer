package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public TCPClient() {
        super();
    }

    public static void main(String args[]) {
        try {
            Socket skt = new Socket("84.56.233.235", 43401);
            BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            PrintWriter print = new PrintWriter(skt.getOutputStream(), true);
            System.out.println("Connected...");
            print.println("Ultimativer Test! Die Antwort ist 42!!\n");
            System.out.println("verschickt!");
            while (!in.ready()) {
                Thread.sleep(1000);
                System.out.println("warten auf Antwort...");
            }
            System.out.println(in.readLine());
            print.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
