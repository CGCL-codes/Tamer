package examples;

import java.io.*;
import java.net.URL;
import org.ebayopensource.twin.*;
import org.ebayopensource.twin.element.*;

public class Notepad {

    public static void pause(double seconds) {
        long millis = (long) (seconds * 1000);
        if (millis == 0) return;
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws TwinException, IOException {
        Application session = new Application(new URL("http://localhost:4444/"));
        session.open("notepad", null);
        try {
            System.out.println("Application name: " + session.getApplicationName());
            System.out.println("Application version: " + session.getApplicationVersion());
            Window window = session.getWindow();
            System.out.println(window);
            System.out.println(session.getDesktop().getStructure());
            System.out.println("Bounds=" + window.getBounds());
            window.setSize(600, 200);
            System.out.println("\tNow bounds=" + window.getBounds());
            window.type("Hello world!\n");
            window.type("This window will self-destruct in 3...");
            pause(0.3);
            window.type("2...");
            pause(0.3);
            window.type("1...");
            pause(0.3);
            window.type("BOOM!");
            window.contextMenu(100, 100).item("Undo").click();
            window.click(100, 100, MouseButton.Right);
            Menu openMenu = session.getDesktop().waitForDescendant(Criteria.type(Menu.class), 1.0);
            MenuItem undo = openMenu.getChild(Criteria.name("Undo"));
            undo.click();
            Screenshot screenshot = window.getScreenshot();
            System.out.println("Got screenshot: type=" + screenshot.getContentType() + " size=" + screenshot.getData().length);
            window.menu("File").openMenu().item("Exit").click();
        } finally {
            session.close();
        }
    }
}
