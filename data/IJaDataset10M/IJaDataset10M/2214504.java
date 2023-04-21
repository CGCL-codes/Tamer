package org.personalsmartspace.cm.location.impl.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.personalsmartspace.cm.location.inference.SoftLocation;
import org.personalsmartspace.cm.source.api.pss3p.ICtxSourceManager;
import org.personalsmartspace.cm.sources.CSMProvider;
import org.personalsmartspace.cm.sources.NavShoeWrapper;
import org.personalsmartspace.cm.sources.UbisenseCoordinateSource;
import org.personalsmartspace.cm.sources.UbisenseDistributor;
import org.personalsmartspace.cm.sources.UbisenseReceiverThread;
import org.personalsmartspace.cm.sources.UbisenseSymLocSource;
import org.personalsmartspace.log.impl.PSSLog;

/**
 * @author fran_ko
 * Logic:
 * IndoorPositioning starts fusion (SoftLocation)
 * IndoorPositioning starts Ubisense and gives each measurement to the fusion.
 * IndoorPositioning starts NavShoeWrapper
 * 		- this receives Shoe Measurements (at navshoePort)
 * 		- converts them to step measurements (in class NavShoefusion)
 * 		- Gives Step information (received at fusionPort) to fusion:
 */
public class IndoorPositioningStarter implements UbisenseDistributor {

    protected ICtxSourceManager csm = new MockCSM();

    private final PSSLog log = new PSSLog(this);

    private UbisenseSymLocSource symlocSource;

    private UbisenseCoordinateSource coordinateSource;

    private SoftLocation fusion;

    private static String relativePathFile = "src/main/resources/";

    private static String relativePathOSGI = "./../../../../";

    private static String propertyFileName = "ubisense.properties";

    private Properties properties;

    private double coordinateOffset;

    public static void main(String[] args) {
        new IndoorPositioningStarter();
    }

    public IndoorPositioningStarter() {
        CSMProvider.setCSM(csm);
        properties = new Properties();
        InputStream stream;
        try {
            stream = this.getClass().getResourceAsStream(relativePathOSGI + propertyFileName);
            log.debug("IndoorPositioning: properties file = " + stream);
            if (stream == null) stream = new FileInputStream(relativePathFile + "ubisense.properties");
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        coordinateOffset = Double.parseDouble(properties.getProperty("coordinateOffset"));
        boolean fakeUbisense = Boolean.parseBoolean((properties.getProperty("fakeUbisense")));
        fusion = new SoftLocation();
        if (!fakeUbisense) {
            new UbisenseReceiverThread(this).start();
            log.info("ubisense wrapper STARTED");
        }
        new NavShoeWrapper(fusion);
        log.info("navshoe wrapper STARTED");
    }

    /**
	 * @param rs
	 */
    public void received(ResultSet rs) {
        if (symlocSource == null) {
            this.symlocSource = new UbisenseSymLocSource();
            this.coordinateSource = new UbisenseCoordinateSource();
        }
        String coordinates = "";
        String room = "";
        String complete = "";
        String temp = "";
        try {
            rs.last();
            temp = rs.getDouble("x") + ",";
            coordinates += temp;
            complete += temp;
            temp = (rs.getDouble("y") + coordinateOffset) + ",";
            coordinates += temp;
            complete += temp;
            complete += rs.getDouble("z") + ",";
            room = rs.getString("room");
            complete += room + ",";
            temp = rs.getString("floor");
            coordinates += temp;
            complete += temp;
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
        }
        coordinateSource.newMeasurement(coordinates);
        symlocSource.newMeasurement(room);
        fusion.handleUbisenseMeasurement(complete);
    }
}
