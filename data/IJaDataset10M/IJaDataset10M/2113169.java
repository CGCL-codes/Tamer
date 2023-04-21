package phim.client;

import phim.client.ui.UIEvents;
import phim.client.ui.console.ConsoleEvents;
import java.lang.InterruptedException;

public class Main {

    public static void main(String[] args) {
        UIEvents ui = new ConsoleEvents();
        while (ui.alive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
