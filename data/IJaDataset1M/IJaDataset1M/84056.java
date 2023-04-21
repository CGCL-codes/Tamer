package de.aaron.tighturl.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import de.aaron.tighturl.db.DatabaseConnector;

/**
 * Description: Thread
 * Licence: http://creativecommons.org/licenses/by-sa/3.0/
 * 
 * @author Aaron Kreis (www.aaron.de)
 * @version $Revision$
 */
public class TightURLCronjobThread extends Thread {

    public boolean running = true;

    public void run() {
        Helper helper = new Helper();
        Properties props = helper.getProperties("config");
        while (running) {
            try {
                sleep(Long.parseLong(props.getProperty("cronjob.interval")));
                try {
                    DatabaseConnector databaseconnector = new DatabaseConnector();
                    databaseconnector.selectAndDeleteEntries();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void endThread() {
        this.running = false;
    }
}
