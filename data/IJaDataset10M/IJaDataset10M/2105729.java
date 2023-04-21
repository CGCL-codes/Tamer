package pyroman;

import java.util.Scanner;

/**
 *
 * @author DonHoeks
 */
public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        View view = new View(controller);
        view.interact();
    }
}
