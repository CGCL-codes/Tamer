package polyester.example;

import java.util.Scanner;
import javax.swing.JOptionPane;
import polyester.util.SearchString;
import lights.interfaces.ITupleSpace;

public class WebInputWorker extends WebWorker {

    public WebInputWorker(ITupleSpace space) {
        super(space);
    }

    @Override
    public void work() {
        search(inputSearch());
    }

    /**
	 * Very rudimentary UI to get user's xpath search expression.
	 * In a real implementation, this would be done through a web 
	 * interface. 
	 */
    public String inputSearch() {
        return JOptionPane.showInputDialog("Enter your search");
    }
}
