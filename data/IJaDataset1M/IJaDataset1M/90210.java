package test.de.hpi.eworld.sumo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.de.hpi.eworld.EWorldConstants;
import test.de.hpi.eworld.EWorldTest;
import test.de.hpi.eworld.util.FileSysUtils;
import de.hpi.eworld.core.PersistenceManager;
import de.hpi.eworld.exporter.sumo.EWorld2sumo;
import de.hpi.eworld.observer.NotificationType;
import de.hpi.eworld.observer.ObserverNotification;

public class SumoExportTest extends EWorldTest implements Observer {

    private static final String DIR_SUMO_TESTRES = "./resources/sumo_test/gen/";

    private static final String DIR_SUMO_TESTRES_ORIG = "./resources/sumo_test/original/";

    private static final String RES_FILE_PREFIX = "gen_result";

    private boolean exportResult = false;

    @Before
    public void prepareTest() {
        FileSysUtils.deleteDirectory(DIR_SUMO_TESTRES);
        FileSysUtils.makeDirectory(DIR_SUMO_TESTRES);
    }

    @Test
    public void testFileGeneration() {
        System.out.println("Loading test data");
        initTestData();
        EWorld2sumo ew2s = new EWorld2sumo();
        ew2s.setData(DIR_SUMO_TESTRES, RES_FILE_PREFIX, true, EWorldConstants.getNetConvertFile(), "", true, EWorldConstants.getDuaRouterFile(), "", true, false, false, false, true, new Integer[] { 10000 }, false);
        System.out.println("Performing export");
        System.out.println("Generating edg, evt, poi, nod, net, rou and a cfg file...");
        ew2s.addObserver(this);
        ew2s.run();
        Assert.assertTrue(this.exportResult);
        System.out.println("Checking result files");
        File gen_edg = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".edg.xml");
        Assert.assertTrue(gen_edg.exists());
        File gen_evt = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".evt.xml");
        Assert.assertTrue(gen_evt.exists());
        File gen_poi = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".poi.xml");
        Assert.assertTrue(gen_poi.exists());
        File gen_nod = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".nod.xml");
        Assert.assertTrue(gen_nod.exists());
        File gen_net = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".net.xml");
        Assert.assertTrue(gen_net.exists());
        File gen_rou = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".rou.xml");
        Assert.assertTrue(gen_rou.exists());
        File gen_tls = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".tls.xml");
        Assert.assertTrue(gen_tls.exists());
        File gen_cfg = new File(DIR_SUMO_TESTRES + RES_FILE_PREFIX + ".sumo.cfg");
        Assert.assertTrue(gen_cfg.exists());
        System.out.println("Export Done");
    }

    public void exportWasSuccessful() {
        this.exportResult = true;
    }

    private HashMap<String, Integer> initTestData() {
        PersistenceManager.getInstance().loadFromFile("./resources/export/berlin_test.ewd");
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        try {
            int noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.edg.xml");
            result.put(".edg", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.evt.xml");
            result.put(".evt", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.net.xml");
            result.put(".net", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.nod.xml");
            result.put(".nod", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.poi.xml");
            result.put(".poi", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.rou.xml");
            result.put(".rou", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.tls.xml");
            result.put(".tls", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.add.xml");
            result.put(".add", noLines);
            noLines = countLines(DIR_SUMO_TESTRES_ORIG + "berlin_test.sumo.cfg");
            result.put(".sumo.cfg", noLines);
        } catch (IOException e) {
            System.out.println("Cannot find source files.");
            e.printStackTrace();
        }
        return result;
    }

    private int countLines(String filename) throws IOException {
        File f = new File(filename);
        return countLines(f);
    }

    private int countLines(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufR = new BufferedReader(isr);
            int result = 0;
            String tmp = bufR.readLine();
            while (tmp != null) {
                result++;
                tmp = bufR.readLine();
            }
            bufR.close();
            isr.close();
            fis.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void update(Observable o, Object arg) {
        ObserverNotification notification = (ObserverNotification) arg;
        if (notification.getType() == NotificationType.exportSuccessful) {
            this.exportWasSuccessful();
        }
    }
}
