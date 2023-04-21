package sdloader;

import sdloader.internal.CommandLineHelper;
import sdloader.util.Browser;

/**
 * SDLoaderをオープンします.
 *
 * @author c9katayama
 */
public class CommandLineOpen {

    public static void main(String[] args) {
        CommandLineHelper helper = new CommandLineHelper(args);
        if (helper.hasHelpOption()) {
            helper.printUsage();
            System.exit(0);
        }
        SDLoader sdloader = new SDLoader();
        try {
            helper.applySDLoaderProperties(sdloader);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            helper.printHelpOption();
            System.exit(0);
        }
        try {
            sdloader.start();
            if (helper.isOpenBrowser()) {
                int port = sdloader.getPort();
                String url = "http://localhost:" + port;
                Browser.open(url);
            }
            sdloader.waitForStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
