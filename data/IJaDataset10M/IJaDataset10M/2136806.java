package utils;

import java.io.File;
import java.util.Properties;
import remedium.Remedium;
import system.mq.msg;

/**
 *
 * @author Nuno Brito, 29th of May 2011 in Darmstadt, Germany.
 */
public class tweaks implements msg {

    /** Remove the system tray icon from public sight*/
    public static void removeTrayIcon(String URL) {
        String address = "http://" + URL + "/" + trayicon + "?action=hide";
        utils.internet.getTextFile(address);
    }

    /** Send out a notification */
    public static void notification(String URL, String message) {
        String cleanURL = URL.replace("http://", "");
        String address = "http://" + cleanURL + "/" + trayicon + "?notification=" + utils.text.quickEncode(message);
        utils.internet.getTextFile(address);
    }

    /** Send out a notification */
    public static void updateTrayIconAction(String URL, String message) {
        String cleanURL = URL.replace("http://", "");
        String address = "http://" + cleanURL + "/" + trayicon + "?update=" + utils.text.quickEncode(message);
        utils.internet.getTextFile(address);
    }

    /** Delete the database folder for our testings */
    public static void deleteDBFolder(String port) {
        String toDelete = "storage" + File.separator + port;
        System.out.println("Deleting folder '" + toDelete + "'");
        File file = new File(toDelete);
        utils.files.deleteDir(file);
    }

    /**
       * Create a database filled with data about processed files.
       * This method is handy for automating test cases that require data to be
       * present.
       * @param portNumber The port inside the storage folder to be used
       */
    public static void generateIndexedData(Remedium instance, String where, int Seconds) {
        Properties message = new Properties();
        message.setProperty(FIELD_FROM, sentinel_gui);
        message.setProperty(FIELD_TO, sentinel_scanner);
        message.setProperty(FIELD_TASK, "scan");
        message.setProperty(FIELD_DIR, where);
        message.setProperty(FIELD_DEPTH, "5");
        System.out.println("Starting a base line scan");
        message.setProperty(SCAN, "" + START);
        instance.getMQ().send(message);
        utils.time.wait(Seconds);
        message.setProperty(SCAN, "" + STOPPED);
        instance.getMQ().send(message);
        System.out.println("Stopped scanning, we should have some data now.");
        utils.time.wait(3);
    }

    /**
   * This method will request one component at a given location to engage into
   * a synchronization with its counterpart component at some other location.
   *
   * @param from The instance with the data we want to fetch
   * @param to  The instance that we want to update
   * @param what What is the component address (e.g.: sentinel/indexer)
   * @param since Gather since when?
   * @param until Gather data until?
   * @return The result from this operation: "Synchronization complete"
   */
    public static String batchSynchronize(String from, String to, String what, long since, long until) {
        String request = "http://" + to + "/" + what + "?box=crc32" + "&action=remotesync" + "&who=" + from + "&since=" + since + "&until=" + until;
        String result = utils.internet.getTextFile(request);
        return result;
    }
}
