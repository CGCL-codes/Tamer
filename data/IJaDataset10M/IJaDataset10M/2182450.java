package utilities;

import kernel.Process;
import osloader.Constants;

/**
 * @author Tmy
 * 
 */
public class echo extends Process {

    @Override
    public void main() throws InterruptedException {
        output.printOutput(arguments.toString() + Constants.EOL);
    }

    @Override
    public String getInfo() {
        return "Displays command arguments.";
    }
}
