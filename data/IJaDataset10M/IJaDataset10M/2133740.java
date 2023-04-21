package org.eun.oai.server.crosswalk;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eun.oai.server.util.IloxCatalogUtil;
import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.OAIInternalServerError;

public class Lom2oai_lom extends Crosswalk {

    public static String LRE3_LOCATION = "http://ltsc.ieee.org/xsd/LOM http://ltsc.ieee.org/xsd/lomv1.0/lomLoose.xsd";

    private IloxCatalogUtil crosswalkUtil;

    public Lom2oai_lom(Properties properties) throws OAIInternalServerError {
        super(LRE3_LOCATION);
        try {
            crosswalkUtil = IloxCatalogUtil.getInstance(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getAnonymousLogger().log(Level.INFO, "An Cmr2oai_cmr object is created!");
    }

    @Override
    public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
        return crosswalkUtil.createMetadata(nativeItem);
    }

    @Override
    public boolean isAvailableFor(Object nativeItem) {
        return true;
    }
}
