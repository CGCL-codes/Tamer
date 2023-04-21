package com.koozi.backup;

import com.koozi.Settings;
import com.koozi.TimeAndDate;

/**
 * Class that handles backup of tracks/waypoints
 * @author Sven-Ove Bjerkan
 */
public class Backup implements Runnable {

    private Zip zip;

    private Settings settings;

    public Backup() {
        settings = Settings.getInstance();
    }

    @Override
    public void run() {
        zip = new Zip();
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(settings.getBackupInterval() * 60 * 1000);
                zip.compress("kl " + TimeAndDate.getNiceTime(".") + ".zip", false);
            } catch (InterruptedException ex) {
            }
        }
    }
}
