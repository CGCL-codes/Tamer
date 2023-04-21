package hu.schmidtsoft.map.gps.impl;

import hu.schmidtsoft.map.model.MCoordinate;
import hu.schmidtsoft.map.util.UtilString;
import hu.schmidtsoft.map.util.UtilThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * A GPS interface that reads GPS information from a file on a periodical schedule.
 * @author rizsi
 *
 */
public class FileGPS extends AbstractGPS {

    File f;

    public FileGPS(File f) {
        super();
        this.f = f;
    }

    boolean stopped = false;

    @Override
    void myStart() {
        Thread th = new Thread(new Runnable() {

            public void run() {
                while (!stopped) {
                    try {
                        try {
                            FileInputStream fis = new FileInputStream(f);
                            try {
                                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                                BufferedReader br = new BufferedReader(isr);
                                String line = br.readLine();
                                List<String> strs = UtilString.tokenize(line, " ");
                                MCoordinate coo = new MCoordinate(Double.parseDouble(strs.get(0)), Double.parseDouble(strs.get(1)), Double.parseDouble(strs.get(2)));
                                long time = System.currentTimeMillis();
                                position(coo, time, 0);
                            } finally {
                                fis.close();
                            }
                        } catch (Exception e) {
                            signalLost();
                        }
                    } catch (Exception e) {
                    }
                    UtilThread.sleep(1000);
                }
            }
        });
        th.start();
    }

    public void stop() {
        stopped = true;
    }
}
