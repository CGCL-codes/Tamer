package it.unina.seclab.jafimon.test;

import it.unina.seclab.jafimon.DataCollection;
import it.unina.seclab.jafimon.MonitoredData;
import it.unina.seclab.jafimon.MonitoredDataManager;
import it.unina.seclab.jafimon.util.SystemEnvironmentVariables;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Classe ad uso interno per il testing
 * 
 * @author 	Mauro Iorio
 *
 */
public class TestMainClass {

    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) throws Throwable {
        try {
            PropertyConfigurator.configure(SystemEnvironmentVariables.getEnvironmentVariable("JAFIMON_HOME") + "/log4j.properties");
        } catch (IOException e) {
            PropertyConfigurator.configure("log4j.properties");
        }
        logger.info("starting execution");
        MonitoredDataManager dMgr = new MonitoredDataManager();
        dMgr.appendDateTimeToName = true;
        MonitoredData mData = new MonitoredData();
        int id = dMgr.newDataCollection("Pippo");
        for (int i = 0; i < 10; i++) dMgr.appendData(id, mData.setData("Ullalla" + i));
        String[] lista = dMgr.getCollectionList();
        for (int i = 0; i < lista.length; i++) {
            logger.trace(lista[i]);
        }
        DataCollection coll = dMgr.getCollection("Pippo-20070117191741");
        String data = coll.getFirstData();
        while (data.length() > 0) {
            System.out.print(data);
            data = coll.getNextData();
        }
        data = coll.getFirstData();
        System.out.print(data);
    }
}
