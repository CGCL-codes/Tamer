package testExamples;

import java.net.*;
import java.io.*;

class Cliente {

    public static void main(String args[]) throws IOException {
        Socket s;
        OutputStream s1out;
        String cadena = new String("\nHOLA MUNDO STREAM\n");
        s = new Socket("localhost", 3244);
        s1out = s.getOutputStream();
        DataOutputStream cadenaStrim = new DataOutputStream(s1out);
        cadenaStrim.writeUTF(cadena);
        s.close();
    }
}
