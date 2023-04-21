package aoetec.javalang._321iostreams;

import java.io.Console;

public class ConsoleTest {

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console is null");
            System.exit(-1);
        }
        String msg;
        while (!(msg = console.readLine("start input('Exit' for end):")).equalsIgnoreCase("Exit")) {
            System.out.println(">>>" + msg);
        }
    }
}
