package cz.xf.tomason.tictactoe;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String args[]) throws Exception {
        int answer = 0;
        if (args.length > 0 && args[0].equals("-a")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("1: client - GUI (default)");
            System.out.println("2: server - GUI");
            System.out.println("3: server - console");
            answer = Integer.parseInt(br.readLine());
        }
        switch(answer) {
            case 2:
                cz.xf.tomason.tictactoe.multiplayer.TestServer.main(args);
                break;
            case 3:
                cz.xf.tomason.tictactoe.multiplayer.ConsoleServer.main(args);
                break;
            default:
                cz.xf.tomason.tictactoe.GUI.Main.main(args);
                break;
        }
    }
}
