package org.tigr.seq.translator;

import org.tigr.cloe.utils.ApplicationProperties;
import org.tigr.seq.tdb.TDBConnection;
import org.tigr.seq.tdb.TDBDataStoreTranslator;
import org.tigr.seq.datastore.DataStoreException;
import java.util.Properties;
import java.io.*;

/**
 * @author dkatzel
 * Creates a DataStoreTranslator for Cloe to interact with.
 *
 */
public class DataStoreTranslatorFactory {

    private static DataStoreTranslator translator;

    private static boolean allowCoverageHoles;

    private static double maxFeatureChangePercentSize;

    private DataStoreTranslatorFactory() {
    }

    static {
        Properties props = ApplicationProperties.getInstance().getProperties();
        String allowHolesString = props.getProperty("allowCoverageHoles");
        if (allowHolesString != null && allowHolesString.equals("1")) {
            allowCoverageHoles = true;
        } else {
            allowCoverageHoles = false;
        }
        maxFeatureChangePercentSize = Double.parseDouble(props.getProperty("maxFeatureChangeSizePercent", ".04"));
        System.out.println("AllowCoverageHoles = " + allowCoverageHoles);
        System.out.println("FeaturepercentSize = " + maxFeatureChangePercentSize);
    }

    public static DataStoreTranslator getInstance() throws DataStoreException {
        try {
            translator = new TDBDataStoreTranslator(TDBConnection.getInstance(), allowCoverageHoles);
        } catch (Exception e) {
            throw new DataStoreException("Can not create DataStoreTranslator");
        }
        return translator;
    }

    public static boolean allowCoverageHoles() {
        return allowCoverageHoles;
    }

    public static double getMaxFeatureChangePercentSize() {
        return maxFeatureChangePercentSize;
    }
}
