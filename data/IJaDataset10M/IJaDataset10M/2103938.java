package mudstrate;

import java.io.IOException;
import mudstrate.init.MudEnvironment;

public class Main {

    public static String statusMsg;

    /**
	 * Main Function
	 * 
	 * @param args Command line arguments supplied at initial run time
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        try {
            MudEnvironment.getInstance().init();
        } catch (Exception e) {
            System.out.println("<Main> MudEnvironment could not be created: " + e.getMessage());
            return;
        }
    }
}
